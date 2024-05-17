package com.practicum.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

class TracksResponse(@SerializedName("results") val tracks: ArrayList<TrackDto>) : Response()