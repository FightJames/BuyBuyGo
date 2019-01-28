package com.techapp.james.buybuygo.model.file

import android.content.Context
import android.net.Uri
import android.support.v4.content.FileProvider
import java.io.File

object FileManager {

    fun createImageFileUri(fileName: String, context: Context): Uri {
        var file: File? = null
        val filePath = context.filesDir.toString() + "/images/$fileName.jpg"
        file = File(filePath)
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir()
        }
        //share file to camera
        return FileProvider.getUriForFile(context, "buybuygo.fileProvider", file!!)
    }
}