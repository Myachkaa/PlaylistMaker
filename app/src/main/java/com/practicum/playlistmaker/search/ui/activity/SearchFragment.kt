package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private var inputText: String = TEXT_DEF
    private val adapter = TrackAdapter()
    private lateinit var lastTrack: String
    private lateinit var historyAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            setState(it)
        }

        if (savedInstanceState == null) {
            viewModel.setDefaultState()
        }

        historyAdapter = TrackAdapter()
        binding.recyclerViewHistory.adapter = historyAdapter
        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())

        adapter.onItemClick = { track -> onTrackClicked(track) }

        historyAdapter.onItemClick = { track -> onTrackClicked(track) }

        binding.recyclerViewTracks.adapter = adapter
        binding.recyclerViewTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        historyVisibility()

        binding.iconClear.setOnClickListener {
            binding.editText.setText("")
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val currentFocus = requireActivity().currentFocus
            if (currentFocus != null) {
                inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                viewModel.clearTracks()
                adapter.notifyDataSetChanged()
            }
        }

        binding.updateButton.setOnClickListener { viewModel.retryLastSearch() }


        binding.clearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
            historyAdapter.notifyDataSetChanged()
            binding.searchHistory.isVisible = false
        }

        binding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.editText.text.isNotEmpty()) {
                    searchRequest(binding.editText.text.toString())
                }
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                val currentFocus = requireActivity().currentFocus
                if (currentFocus != null) {
                    inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
                true
            } else false
        }


        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            binding.searchHistory.isVisible =
                hasFocus && binding.editText.text.isEmpty() && viewModel.getSearchHistory()
                    .isNotEmpty()
        }


        binding.editText.addTextChangedListener(
            beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                hideAllViews()
                binding.iconClear.isVisible = !charSequence.isNullOrEmpty()
                binding.searchHistory.isVisible =
                    charSequence.isNullOrEmpty() && binding.editText.hasFocus() && viewModel.getSearchHistory()
                        .isNotEmpty()
                viewModel.onSearchTextChanged(charSequence.toString())
            },
            afterTextChanged = { _ ->
                inputText = binding.editText.text.toString()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_TEXT, viewModel.searchText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            viewModel.searchText = savedInstanceState.getString(KEY_SEARCH_TEXT, TEXT_DEF)
        }
        if (viewModel.searchText.isEmpty()) {
            viewModel.setDefaultState()
        }
    }

    private fun hideAllViews() {
        binding.recyclerViewTracks.isVisible = false
        binding.progressBar.isVisible = false
        binding.placeholderNetworkError.isVisible = false
        binding.updateButton.isVisible = false
        binding.placeholderNotFound.isVisible = false
        binding.searchHistory.isVisible = false
    }

    private fun showProgressbar() {
        hideAllViews()
        binding.progressBar.isVisible = true
    }

    private fun showTrackList() {
        hideAllViews()
        binding.recyclerViewTracks.isVisible = true
    }

    private fun showServerError() {
        viewModel.clearTracks()
        adapter.notifyDataSetChanged()
        hideAllViews()
        binding.placeholderNetworkError.isVisible = true
        binding.updateButton.isVisible = true
    }

    private fun showNotFound() {
        hideAllViews()
        binding.placeholderNotFound.isVisible = true
    }


    private fun searchRequest(track: String) {
        lastTrack = track
        viewModel.searchRequest(track)
    }

    private fun historyVisibility() {
        val history = viewModel.getSearchHistory()
        if (history.isNotEmpty() && binding.editText.hasFocus()) {
            historyAdapter.tracks = history as ArrayList<Track>
            historyAdapter.notifyDataSetChanged()
            binding.searchHistory.isVisible = true
        } else {
            if (history.isNotEmpty()) {
                historyAdapter.tracks = history as ArrayList<Track>
                historyAdapter.notifyDataSetChanged()
            }
            binding.searchHistory.isVisible = false
        }
    }

    private fun onTrackClicked(track: Track) {

        if (viewModel.clickDebounce()) {
            viewModel.saveTrack(track)
            historyAdapter.tracks = viewModel.getSearchHistory() as ArrayList<Track>
            historyAdapter.notifyDataSetChanged()

            val audioPlayerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
            audioPlayerIntent.putExtra(KEY_TRACK, track)
            startActivity(audioPlayerIntent)
        }
    }

    private fun setState(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showProgressbar()
            is SearchState.TrackList -> {
                showTrackList()
                adapter.tracks = state.tracks
                adapter.notifyDataSetChanged()
            }

            is SearchState.HistoryTrackList -> historyVisibility()
            is SearchState.ServerError -> showServerError()
            is SearchState.NotFound -> showNotFound()
            else -> hideAllViews()
        }
    }

    companion object {
        const val KEY_TRACK = "track"
        const val KEY_SEARCH_TEXT = "KEY_SEARCH_TEXT"
        const val TEXT_DEF = ""
    }
}