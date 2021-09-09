package com.suret.moviesapp.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ReviewModel(
    val id: Int? = null,
    val page: Int? = null,
    val results: List<ReviewResult>? = null,
    val total_pages: Int? = null,
    val total_results: Int? = null
) : Parcelable

@Keep
@Parcelize
data class ReviewResult(
    val author: String? = null,
    val author_details: AuthorDetails? = null,
    val content: String? = null,
    val created_at: String? = null,
    val id: String? = null,
    val updated_at: String? = null,
    val url: String? = null
) : Parcelable

@Keep
@Parcelize
data class AuthorDetails(
    val avatar_path: String? = null,
    val name: String? = null,
    val rating: Double? = null,
    val username: String? = null
) : Parcelable