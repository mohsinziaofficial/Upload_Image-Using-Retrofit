package com.example.uploadimageusingretrofit.network

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadRequestBody(
    private val file : File,
    private val contentType : String,
    private val callback : UploadCallback) : RequestBody() {

    companion object {
        const val BUFFER_DEFAULT_SIZE = 2048
    }

    interface UploadCallback {
        fun onProgressUpdate(percentage: Int)
    }

    override fun contentType() = MediaType.parse("$contentType/*")

    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {
        val length = file.length()
        val buffer = ByteArray(BUFFER_DEFAULT_SIZE)
        val fileInputStream = FileInputStream(file)
        var fileUploaded = 0L

        // .use function will close the stream automatically once it is completed
        fileInputStream.use { inputStream ->
            var read : Int
            val handler = Handler(Looper.getMainLooper())

            while (inputStream.read(buffer).also { read = it } != -1) {
                handler.post(ProgressUpdater(fileUploaded, length))
                fileUploaded += read
                sink.write(buffer, 0, read)
            }
        }
    }

    inner class ProgressUpdater(
        private val uploaded: Long,
        private val total: Long
    ) : Runnable {
        override fun run() {
            callback.onProgressUpdate((100 * uploaded / total).toInt())
        }
    }
}