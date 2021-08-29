package com.suret.moviesapp.data.model

import androidx.annotation.Keep

@Keep
data class ReviewModel(
    val id: Int? = null,
    val page: Int? = null,
    val results: List<ReviewResult>? = null,
    val total_pages: Int? = null,
    val total_results: Int? = null
)

@Keep
data class ReviewResult(
    val author: String? = null,
    val author_details: AuthorDetails? = null,
    val content: String? = null,
    val created_at: String? = null,
    val id: String? = null,
    val updated_at: String? = null,
    val url: String? = null
)

@Keep
data class AuthorDetails(
    val avatar_path: String? = null,
    val name: String? = null,
    val rating: Double? = null,
    val username: String? = null
)