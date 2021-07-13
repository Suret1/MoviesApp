package com.suret.moviesapp.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ActorModel(
    val adult: Boolean? = null,
    val biography: String? = null,
    val birthday: String? = null,
    val deathday: String? = null,
    val gender: Int? = null,
    val homepage: String? = null,
    val id: Int? = null,
    val imdb_id: String? = null,
    val known_for_department: String? = null,
    val name: String? = null,
    val place_of_birth: String? = null,
    val popularity: Double? = null,
    val profile_path: String? = null
) : Parcelable