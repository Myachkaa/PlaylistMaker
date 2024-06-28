package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModel<SearchViewModel>()
    private var inputText: String = TEXT_DEF
    private val adapter = TrackAdapter()

    private lateinit var updateButton: Button
    private lateinit var placeholderNetworkError: TextView
    private lateinit var placeholderNotFound: TextView
    private lateinit var inputEditText: EditText
    private lateinit var tracksList: RecyclerView
    private lateinit var lastTrack: String
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backView = findViewById<ImageView>(R.id.back)
        val clearButton = findViewById<ImageView>(R.id.icon_clear)
        val clearHistory = findViewById<Button>(R.id.clearHistory)
        val recyclerViewHistory = findViewById<RecyclerView>(R.id.recyclerViewHistory)

        viewModel.observeState().observe(this) {
            setState(it)
        }

        historyAdapter = TrackAdapter()
        recyclerViewHistory.adapter = historyAdapter
        recyclerViewHistory.layoutManager = LinearLayoutManager(this)

        searchHistoryLayout = findViewById(R.id.searchHistory)
        tracksList = findViewById(R.id.recyclerViewTracks)
        updateButton = findViewById(R.id.updateButton)
        placeholderNetworkError = findViewById(R.id.placeholderNetworkError)
        placeholderNotFound = findViewById(R.id.placeholderNotFound)
        inputEditText = findViewById(R.id.editText)
        progressBar = findViewById(R.id.progressBar)

        adapter.onItemClick = { track -> onTrackClicked(track) }

        historyAdapter.onItemClick = { track -> onTrackClicked(track) }

        tracksList.adapter = adapter
        tracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        historyVisibility()

        backView.setOnClickListener { finish() }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val currentFocus = currentFocus
            if (currentFocus != null) {
                inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                viewModel.clearTracks()
                adapter.notifyDataSetChanged()
            }
        }

        updateButton.setOnClickListener { viewModel.retryLastSearch() }


        clearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
            historyAdapter.notifyDataSetChanged()
            searchHistoryLayout.isVisible = false
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    searchRequest(inputEditText.text.toString())
                }
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                val currentFocus = currentFocus
                if (currentFocus != null) {
                    inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
                true
            } else false
        }


        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryLayout.isVisible =
                hasFocus && inputEditText.text.isEmpty() && viewModel.getSearchHistory()
                    .isNotEmpty()
        }


        inputEditText.addTextChangedListener(
            beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { charSequence, _, _, _ ->
                hideAllViews()
                clearButton.isVisible = !charSequence.isNullOrEmpty()
                searchHistoryLayout.isVisible =
                    charSequence.isNullOrEmpty() && inputEditText.hasFocus() && viewModel.getSearchHistory()
                        .isNotEmpty()
                viewModel.onSearchTextChanged(charSequence.toString())
            },
            afterTextChanged = { _ ->
                inputText = inputEditText.text.toString()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_TEXT, viewModel.searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.searchText = savedInstanceState.getString(KEY_SEARCH_TEXT, TEXT_DEF)
    }

    private fun hideAllViews() {
        tracksList.isVisible = false
        progressBar.isVisible = false
        placeholderNetworkError.isVisible = false
        updateButton.isVisible = false
        placeholderNotFound.isVisible = false
        searchHistoryLayout.isVisible = false
    }

    private fun showProgressbar() {
        hideAllViews()
        progressBar.isVisible = true
    }

    private fun showTrackList() {
        hideAllViews()
        tracksList.isVisible = true
    }

    private fun showServerError() {
        viewModel.clearTracks()
        adapter.notifyDataSetChanged()
        hideAllViews()
        placeholderNetworkError.isVisible = true
        updateButton.isVisible = true
    }

    private fun showNotFound() {
        hideAllViews()
        placeholderNotFound.isVisible = true
    }


    private fun searchRequest(track: String) {
        lastTrack = track
        viewModel.searchRequest(track)
    }

    private fun historyVisibility() {
        val history = viewModel.getSearchHistory()
        if (history.isNotEmpty() && inputEditText.hasFocus()) {
            historyAdapter.tracks = history as ArrayList<Track>
            historyAdapter.notifyDataSetChanged()
            searchHistoryLayout.isVisible = true
        } else {
            if (history.isNotEmpty()) {
                historyAdapter.tracks = history as ArrayList<Track>
                historyAdapter.notifyDataSetChanged()
            }
            searchHistoryLayout.isVisible = false
        }
    }

    private fun onTrackClicked(track: Track) {

        if (viewModel.clickDebounce()) {
            viewModel.saveTrack(track)
            historyAdapter.tracks = viewModel.getSearchHistory() as ArrayList<Track>
            historyAdapter.notifyDataSetChanged()

            val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java)
            audioPlayerIntent.putExtra(KEY_TRACK_JSON, viewModel.toJson(track))
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
        }
    }

    companion object {
        const val KEY_TRACK_JSON = "trackJson"
        const val KEY_SEARCH_TEXT = "KEY_SEARCH_TEXT"
        const val TEXT_DEF = ""
    }
}
