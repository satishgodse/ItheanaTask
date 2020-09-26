package com.itheanatest.main.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SongsData(
    @SerializedName("name")
    val songName: String?,
    @SerializedName("path")
    val songPath: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("album")
    val album: String?
): Parcelable