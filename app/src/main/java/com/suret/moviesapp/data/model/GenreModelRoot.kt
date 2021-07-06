package com.suret.moviesapp.data.model

import androidx.annotation.Keep

@Keep
data class GenreModelRoot(
    val genres: List<GenreModel>?=null
)
@Keep
data class GenreModel(
    val id: Int? = null,
    val name: String? = null
)