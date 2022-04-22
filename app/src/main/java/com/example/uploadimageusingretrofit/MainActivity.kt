package com.example.uploadimageusingretrofit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uploadimageusingretrofit.databinding.ActivityMainBinding
import com.example.uploadimageusingretrofit.network.ApiClient
import com.example.uploadimageusingretrofit.network.UploadRequestBody
import com.example.uploadimageusingretrofit.response.UploadResponse
import com.example.uploadimageusingretrofit.utils.displaySnackbar
import com.example.uploadimageusingretrofit.utils.getFileName
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {

    companion object {
        const val REQUEST_CODE = 100
    }

    private lateinit var binding : ActivityMainBinding
    private var imgUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setOnClickListener {
            selectImage()
        }

        binding.buttonUpload.setOnClickListener {
            uploadImage()
        }
    }

    private fun selectImage() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeType = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
            startActivityForResult(it, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
        {
            when(requestCode) {
                REQUEST_CODE -> {
                    imgUri = data?.data
                    binding.imageView.setImageURI(imgUri)
                }
            }
        }
    }

    private fun uploadImage() {
        if (imgUri == null) {
            binding.root.displaySnackbar("Please select an image first.")
            return
        }

        val parcelFileDescriptor = contentResolver.openFileDescriptor(imgUri!!, "r", null) ?: return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(imgUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        binding.progressBar.progress = 0

        val body = UploadRequestBody(file, "image", this)

        ApiClient.apiService.uploadImage(
            MultipartBody.Part.createFormData("image", file.name, body),
            RequestBody.create(MediaType.parse("multipart/form-data"), "You can send user input here as well.")
        ).enqueue(object :  Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                binding.progressBar.progress = 100
                binding.root.displaySnackbar(response.body()?.message.toString())
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                binding.root.displaySnackbar(t.message!!)
            }

        })
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }
}