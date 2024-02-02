package com.jiangdg.demo

import android.os.Environment
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileUtils {

    private var outputStream: BufferedOutputStream? = null
    var ROOT_PATH = Environment.getExternalStorageDirectory().absolutePath + File.separator

    fun createfile(path: String?) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
        try {
            outputStream = BufferedOutputStream(FileOutputStream(file))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createfile_csv(path: String?): File? {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
        try {
            outputStream = BufferedOutputStream(FileOutputStream(file))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    fun releaseFile() {
        try {
            if (outputStream != null) {
                outputStream!!.flush()
                outputStream!!.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun putFileStream(data: ByteArray?, offset: Int, length: Int) {
        if (outputStream != null) {
            try {
                outputStream!!.write(data, offset, length)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun putFileStream(data: ByteArray?) {
        if (outputStream != null) {
            try {
                outputStream!!.write(data)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        val ROOT_PATH:String= Environment.getExternalStorageDirectory().absolutePath + File.separator
        private var outputStream: BufferedOutputStream? = null
//        var ROOT_PATH = Environment.getExternalStorageDirectory().absolutePath + File.separator

        fun createfile(path: String?) {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
            try {
                outputStream = BufferedOutputStream(FileOutputStream(file))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun createfile_csv(path: String?): File? {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
            try {
                outputStream = BufferedOutputStream(FileOutputStream(file))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return file
        }

        fun releaseFile() {
            try {
                if (outputStream != null) {
                    outputStream!!.flush()
                    outputStream!!.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun putFileStream(data: ByteArray?, offset: Int, length: Int) {
            if (outputStream != null) {
                try {
                    outputStream!!.write(data, offset, length)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun putFileStream(data: ByteArray?) {
            if (outputStream != null) {
                try {
                    outputStream!!.write(data)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}