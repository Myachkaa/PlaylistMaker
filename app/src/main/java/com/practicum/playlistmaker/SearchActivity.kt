package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection.HTTP_OK

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backView = findViewById<ImageView>(R.id.back)
        val clearButton = findViewById<ImageView>(R.id.icon_clear)

        tracksList = findViewById(R.id.recyclerViewTracks)
        updateButton = findViewById(R.id.updateButton)
        placeholderNetworkError = findViewById(R.id.placeholderNetworkError)
        placeholderNotFound = findViewById(R.id.placeholderNotFound)
        inputEditText = findViewById(R.id.editText)

        adapter.tracks = tracks

        tracksList.adapter = adapter
        tracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


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
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                inputText = inputEditText.text.toString()
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
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
        placeholderNetworkError.visibility = View.VISIBLE
        updateButton.visibility = View.VISIBLE
        placeholderNotFound.visibility = View.GONE
    }

    private fun search(track: String) {
        iTunsService.getTracks(track)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    when (response.code()) {
                        HTTP_OK -> {
                            placeholderNetworkError.visibility = View.GONE
                            updateButton.visibility = View.GONE
                            placeholderNotFound.visibility = View.GONE
                            if (response.body()?.tracks?.isNotEmpty() == true) {
                                tracks.clear()
                                tracks.addAll(response.body()?.tracks!!)
                                tracksList.visibility = View.VISIBLE
                                adapter.notifyDataSetChanged()
                            } else {
                                tracks.clear()
                                adapter.notifyDataSetChanged()
                                tracksList.visibility = View.GONE
                                placeholderNotFound.visibility = View.VISIBLE
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


    companion object {
        const val KEY_SEARCH_TEXT = "KEY_SEARCH_TEXT"
        const val TEXT_DEF = ""
    }
}