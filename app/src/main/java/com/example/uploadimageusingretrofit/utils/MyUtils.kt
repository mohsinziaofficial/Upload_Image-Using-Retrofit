package com.example.uploadimageusingretrofit.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.displaySnackbar(messae : String) {
    Snackbar.make(this, messae, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("OK") {
            snackbar.dismiss()
        }
    }.show()
}

fun ContentResolver.getFileName(imgUri : Uri) : String {
    var name = ""
    val cursor = query(imgUri, null, null, null, null)
    if (cursor != null) {
        val colIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        name = cursor.getString(colIndex)
    }
    return name
}