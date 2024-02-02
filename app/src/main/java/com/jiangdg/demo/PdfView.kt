package com.jiangdg.demo

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.android.synthetic.main.pdf_viewer.toolbar
import kotlinx.android.synthetic.main.pdf_viewer.report_Pdf_View
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class PdfView : AppCompatActivity(){
    lateinit var pdfView: PDFView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdf_viewer)
        pdfView = findViewById(R.id.report_Pdf_View)

        //setting the title
        toolbar.title = "Patient Report"

        RetrievePdfFromUri(pdfView).execute(Global.pdfUri)
    }
    class RetrievePdfFromUri(pdfView: PDFView): AsyncTask<String, Void, InputStream>(){
        val mypdfView: PDFView = pdfView
        override fun doInBackground(vararg p0: String?): InputStream? {
            var inputStream: InputStream? = null
            try {
                val url = URL(p0.get(0))
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection
                if (urlConnection.responseCode == 200) {
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
            return inputStream
        }
        override fun onPostExecute(inputStream: InputStream?) {
            mypdfView.fromStream(inputStream).load()
        }
    }

//    class RetrievePdfFromUri :
//        AsyncTask<String?, Void?, InputStream?>() {
////        private val report_Pdf_View: Any
//
//        override fun doInBackground(vararg strings: String?): InputStream? {
//            var inputStream: InputStream? = null
//            try {
//                val url = URL(strings[0])
//                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection
//                if (urlConnection.responseCode == 200) {
//                    inputStream = BufferedInputStream(urlConnection.inputStream)
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//                return null
//            }
//            return inputStream!!
//        }
//
//        override fun onPostExecute(inputStream: InputStream?) {
////            report_Pdf_View.fromStream(inputStream).load()
//        }
//    }
}