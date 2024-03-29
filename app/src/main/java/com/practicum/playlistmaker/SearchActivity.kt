package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunsBaseUrl = "https://itunes.apple.com"
    private var inputText: String = ""
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunsBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunsService = retrofit.create(ITunsApi::class.java)
    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter()

    private lateinit var updateButton: Button
    private lateinit var placeholderNetworkError: TextView
    private lateinit var placeholderNotFound: TextView
    private lateinit var inputEditText: EditText
    private lateinit var tracksList: RecyclerView
    private lateinit var lastTrack: String
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var searchHistoryLayout: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backView = findViewById<ImageView>(R.id.back)
        val clearButton = findViewById<ImageView>(R.id.icon_clear)
        val clearHistory = findViewById<Button>(R.id.clearHistory)
        val recyclerViewHistory = findViewById<RecyclerView>(R.id.recyclerViewHistory)

        historyAdapter = TrackAdapter()
        recyclerViewHistory.adapter = historyAdapter
        recyclerViewHistory.layoutManager = LinearLayoutManager(this)

        searchHistoryLayout = findViewById(R.id.searchHistory)
        tracksList = findViewById(R.id.recyclerViewTracks)
        updateButton = findViewById(R.id.updateButton)
        placeholderNetworkError = findViewById(R.id.placeholderNetworkError)
        placeholderNotFound = findViewById(R.id.placeholderNotFound)
        inputEditText = findViewById(R.id.editText)


        adapter.tracks = tracks

        adapter.onItemClick = { track -> clicking(track) }

        historyAdapter.onItemClick = { track -> clicking(track) }

        tracksList.adapter = adapter
        tracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchHistory = SearchHistory(getSharedPreferences(SEARCH_HISTORY_KEY, MODE_PRIVATE))

        historyVisibility()

        backView.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val currentFocus = currentFocus
            if (currentFocus != null) {
                inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                tracks.clear()
                adapter.notifyDataSetChanged()
            }
        }

        updateButton.setOnClickListener {
            search(lastTrack)
        }


        clearHistory.setOnClickListener {
            searchHistory.clearSearchHistory()
            historyAdapter.notifyDataSetChanged()
            searchHistoryLayout.isVisible = false
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    search(inputEditText.text.toString())
                }
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                val currentFocus = currentFocus
                if (currentFocus != null) {
                    inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
                true
            }
           else false
        }


        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryLayout.isVisible = hasFocus && inputEditText.text.isEmpty() && searchHistory.getSearchHistory().isNotEmpty()
        }


        inputEditText.addTextChangedListener (
            beforeTextChanged = {_, _, _, _ -> },
            onTextChanged = {charSequence, _, _, _ ->
                clearButton.isVisible = !charSequence.isNullOrEmpty()
                searchHistoryLayout.isVisible = charSequence.isNullOrEmpty() && inputEditText.hasFocus() && searchHistory.getSearchHistory().isNotEmpty()},
            afterTextChanged = {_ -> inputText = inputEditText.text.toString()}
        )
    }

    private var searchText: String = TEXT_DEF
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(KEY_SEARCH_TEXT, TEXT_DEF)
    }

    private fun showMessage() {
        tracks.clear()
        adapter.notifyDataSetChanged()
        placeholderNetworkError.isVisible = true
        updateButton.isVisible = true
        placeholderNotFound.isVisible = false
    }

    private fun search(track: String) {
        iTunsService.getTracks(track)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            placeholderNetworkError.isVisible = false
                            updateButton.isVisible = false
                            placeholderNotFound.isVisible = false
                            if (response.body()?.tracks?.isNotEmpty() == true) {
                                tracks.clear()
                                tracks.addAll(response.body()?.tracks!!)
                                tracksList.isVisible = true
                                adapter.notifyDataSetChanged()
                            } else {
                                tracks.clear()
                                adapter.notifyDataSetChanged()
                                tracksList.isVisible = false
                                placeholderNotFound.isVisible = true
                            }
                        }

                        else -> {
                            lastTrack = track
                            showMessage()
                        }
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    lastTrack = track
                    showMessage()
                }
            })
    }

    private fun historyVisibility(){
        val history = searchHistory.getSearchHistory()
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
    private fun clicking(track: Track){
        searchHistory.saveTrack(track)
        historyAdapter.tracks = searchHistory.getSearchHistory() as ArrayList<Track>
        historyAdapter.notifyDataSetChanged()
    }

    companion object {
        const val KEY_SEARCH_TEXT = "KEY_SEARCH_TEXT"
        const val TEXT_DEF = ""
        private const val SEARCH_HISTORY_KEY = "search_history"
    }
}