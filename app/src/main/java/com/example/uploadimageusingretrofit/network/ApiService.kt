package com.example.uploadimageusingretrofit.network

import com.example.uploadimageusingretrofit.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("Api.php?apicall=upload")
    fun uploadImage(
        @Part image : MultipartBody.Part,
        @Part ("desc") desc : RequestBody
    ) : Call<UploadResponse>
}