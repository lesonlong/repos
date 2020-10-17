package com.lesonlong.assessment.util

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class FileUtil {

    companion object {

        fun downloadFile(
            context: Context,
            fileUrl: String,
            currFileIndex: Int,
            progress: MutableLiveData<String>? = null
        ): File {
            val tempFile =
                createTempFile(context, "$currFileIndex" + fileUrl.split("/").last())

            // check file is exist or not
            if (!tempFile.exists()) {
                var count: Int
                try {
                    val url = URL(fileUrl)
                    val connection = url.openConnection()
                    connection.connect()

                    // download the file
                    val input = BufferedInputStream(
                        url.openStream(),
                        8192
                    )

                    // Output stream
                    val output = FileOutputStream(tempFile)
                    val data = ByteArray(1024)
                    var total = 0L
                    while ((input.read(data).also { count = it }) != -1) {
                        total += count.toLong()
                        // publishing the progress
                        progress?.postValue("$total kb downloading")

                        // writing data to file
                        output.write(data, 0, count)
                    }
                    // flushing output
                    output.flush()

                    // closing streams
                    output.close()
                    input.close()

                    progress?.postValue("Download completed")
                } catch (e: Exception) {
                    // something went wrong, so we should delete the file if existing
                    if (tempFile.exists()) {
                        tempFile.delete()
                    }
                    progress?.postValue("Download error,\nPlease check your connection.")
                    Log.e("Error:", e.message ?: "download file error")
                }
                return tempFile
            } else {
                progress?.postValue("Download completed")
                return tempFile // return existing file
            }
        }

        private fun createTempFile(context: Context, fileName: String): File {
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File(storageDir, fileName)
        }
    }
}