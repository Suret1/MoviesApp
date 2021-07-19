package com.suret.moviesapp.data.model

import androidx.annotation.Keep

@Keep
data class TrailerModelRoot(
    val id: Int? = null,
    val results: List<Result>? = null
)

@Keep
data class Result(
    val id: String? = null,
    val iso_3166_1: String? = null,
    val iso_639_1: String? = null,
    val key: String? = null,
    val name: String? = null,
    val site: String? = null,
    val size: Int? = null,
    val type: String? = null
)