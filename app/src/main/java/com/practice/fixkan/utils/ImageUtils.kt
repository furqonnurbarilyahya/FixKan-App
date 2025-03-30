package com.practice.fixkan.utils

import android.graphics.Bitmap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

fun bitmaptoMultipart(bitmap: Bitmap, fileName: String): MultipartBody.Part {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val byteArray = stream.toByteArray()
    val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("image", fileName, requestBody)
}