package com.techapp.james.buybuygo.model.file

import android.content.Context
import android.net.Uri
import android.support.v4.content.FileProvider
import timber.log.Timber
import java.io.File

object FileManager {

    fun createImageFileUri(fileName: String, context: Context): FileData {
        var file: File? = null
        val filePath = context.cacheDir.toString() + "/images/$fileName.jpg"
        Timber.d("file path " + filePath)
        file = File(filePath)
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir()
        }
        Timber.d("file path " + filePath + " filePath " + file.absolutePath)
        //share file to camera
        var uri = FileProvider.getUriForFile(context, "buybuygo.fileProvider", file!!)
        var fileData = FileData(filePath, uri)
        return fileData
    }
}