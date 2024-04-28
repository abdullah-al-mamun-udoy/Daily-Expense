package com.example.expensenote.util

import android.content.Context
import android.net.Uri
import android.util.Base64
import java.io.File
import java.io.FileInputStream

object CommonExtension {

    fun imagePathToBase64(ctx: Context, imagePath: String): String {
        val file = File(imagePath)
        val inputStream = FileInputStream(file)
        val bytes = inputStream.readBytes()
        inputStream.close()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
    fun String.toUri(): Uri {
        return Uri.parse(this)
    }




}