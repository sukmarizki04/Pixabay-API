package com.example.pixabayapps.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.POST

data class SearchResponse(
  @SerializedName("total")
  val total : Int?,
  @SerializedName("totalHits")
  val totalHits : Int?,
  @SerializedName("hits")
  val posts : List<Post>?

)
