package com.techapp.james.buybuygo.model.networkManager

import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream
import java.net.URL

object NetworkManager {
    fun downloadImage(link: String, path: String): Single<File> {
        return Single.create<File> { emitter ->
            var file = File(path)
            URL(link).openStream().use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            emitter.onSuccess(file)
        }
    }
}