package com.jiangdg.demo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.hysteroscopy_report.editButton
import kotlinx.android.synthetic.main.hysteroscopy_report.Endometrium
import kotlinx.android.synthetic.main.hysteroscopy_report.EndometriumText
import kotlinx.android.synthetic.main.hysteroscopy_report.Hyperplasia
import kotlinx.android.synthetic.main.hysteroscopy_report.HyperplasiaText
import kotlinx.android.synthetic.main.hysteroscopy_report.cavumUteri
import kotlinx.android.synthetic.main.hysteroscopy_report.cavumUteriText
import kotlinx.android.synthetic.main.hysteroscopy_report.cervicalCanalBleeding
import kotlinx.android.synthetic.main.hysteroscopy_report.cervicalCanalBleedingText
import kotlinx.android.synthetic.main.hysteroscopy_report.cervicalStenosis
import kotlinx.android.synthetic.main.hysteroscopy_report.cervicalStenosisText
import kotlinx.android.synthetic.main.hysteroscopy_report.craurosisFornicis
import kotlinx.android.synthetic.main.hysteroscopy_report.craurosisFornicisText
import kotlinx.android.synthetic.main.hysteroscopy_report.fibroidAddition
import kotlinx.android.synthetic.main.hysteroscopy_report.fibroidAdditionText
import kotlinx.android.synthetic.main.hysteroscopy_report.focalLesions
import kotlinx.android.synthetic.main.hysteroscopy_report.focalLesionsText
import kotlinx.android.synthetic.main.hysteroscopy_report.hystero
import kotlinx.android.synthetic.main.hysteroscopy_report.iUD
import kotlinx.android.synthetic.main.hysteroscopy_report.iUDText
import kotlinx.android.synthetic.main.hysteroscopy_report.image1
import kotlinx.android.synthetic.main.hysteroscopy_report.image2
import kotlinx.android.synthetic.main.hysteroscopy_report.image3
import kotlinx.android.synthetic.main.hysteroscopy_report.image4
import kotlinx.android.synthetic.main.hysteroscopy_report.imageView2
import kotlinx.android.synthetic.main.hysteroscopy_report.intraCervicalAddition
import kotlinx.android.synthetic.main.hysteroscopy_report.intraCervicalAdditionText
import kotlinx.android.synthetic.main.hysteroscopy_report.intraUterineAdhesion
import kotlinx.android.synthetic.main.hysteroscopy_report.intraUterineAdhesionText
import kotlinx.android.synthetic.main.hysteroscopy_report.isthmusUteri
import kotlinx.android.synthetic.main.hysteroscopy_report.isthmusUteriText
import kotlinx.android.synthetic.main.hysteroscopy_report.leftTubalOstia
import kotlinx.android.synthetic.main.hysteroscopy_report.leftTubalOstiaText
import kotlinx.android.synthetic.main.hysteroscopy_report.lesions
import kotlinx.android.synthetic.main.hysteroscopy_report.lesionsText
import kotlinx.android.synthetic.main.hysteroscopy_report.myoma
import kotlinx.android.synthetic.main.hysteroscopy_report.myomaText
import kotlinx.android.synthetic.main.hysteroscopy_report.nullipara
import kotlinx.android.synthetic.main.hysteroscopy_report.nulliparaText
import kotlinx.android.synthetic.main.hysteroscopy_report.patientDob
import kotlinx.android.synthetic.main.hysteroscopy_report.patientImages
import kotlinx.android.synthetic.main.hysteroscopy_report.patientMobile
import kotlinx.android.synthetic.main.hysteroscopy_report.patientName
import kotlinx.android.synthetic.main.hysteroscopy_report.polyps
import kotlinx.android.synthetic.main.hysteroscopy_report.polypsText
import kotlinx.android.synthetic.main.hysteroscopy_report.rightTubalOstia
import kotlinx.android.synthetic.main.hysteroscopy_report.rightTubalOstiaText
import kotlinx.android.synthetic.main.hysteroscopy_report.savePdfButton
import kotlinx.android.synthetic.main.hysteroscopy_report.toolbar
import kotlinx.android.synthetic.main.hysteroscopy_report.uterus
import kotlinx.android.synthetic.main.hysteroscopy_report.uterusText
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class HysteroscopyReportPdf  : AppCompatActivity(){
    private var mDatabaseRef: DatabaseReference? = null
    private var uploadTask: StorageTask<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hysteroscopy_report)
        toolbar.title = "Report"
        getPatientInfo()
        savePdfButton.setOnClickListener(View.OnClickListener { generatePdf() })

        editButton.setOnClickListener{
            val intent = Intent(this@HysteroscopyReportPdf, ReportSelection::class.java)
            startActivity(intent)
        }

    }
    private fun getPatientInfo() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoctorsList")
            .child(Global.doctorUID).child("PatientList").child(Global.patientId)
        mDatabaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val upload = snapshot.getValue(Upload::class.java)
                patientDob.setText(upload!!.DOB)
                patientName.setText(upload!!.Name)
                patientMobile.setText(upload!!.Mobile)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        if (Global.cavumUteriList.size == 0 && Global.cervicalCanalBleedingList.size == 0 && Global.cervicalStenosisList.size == 0 && Global.craurosisFornicisList.size == 0 && Global.endometriumList.size == 0 && Global.fibroidAdditionList.size == 0 && Global.focalLesionsList.size == 0 && Global.hyperplasiaList.size == 0 && Global.intraCervicalAdditionList.size == 0 && Global.intraUterineAdhesionList.size == 0 && Global.isthmusUteriList.size == 0 && Global.iUDList.size == 0 && Global.leftTubalOstiaList.size == 0 && Global.lesionsList.size == 0 && Global.myomaList.size == 0 && Global.nulliparaList.size == 0 && Global.polypsList.size == 0 && Global.rightTubalOstiaList.size == 0 && Global.uterusList.size == 0) {
            hystero.removeAllViews()
        } else {
            if (Global.cavumUteriList.size == 0) {
                cavumUteri.removeAllViews()
            } else {
                cavumUteriText.setText(
                    Global.cavumUteriList
                        .toString().substring(1, Global.cavumUteriList.toString().length - 1)
                )
            }
            if (Global.cervicalCanalBleedingList.size == 0) {
                cervicalCanalBleeding.removeAllViews()
            } else {
                cervicalCanalBleedingText.setText(
                    Global.cervicalCanalBleedingList
                        .toString()
                        .substring(1, Global.cervicalCanalBleedingList.toString().length - 1)
                )
            }
            if (Global.cervicalStenosisList.size == 0) {
                cervicalStenosis.removeAllViews()
            } else {
                cervicalStenosisText.setText(
                    Global.cervicalStenosisList
                        .toString()
                        .substring(1, Global.cervicalStenosisList.toString().length - 1)
                )
            }
            if (Global.craurosisFornicisList.size == 0) {
                craurosisFornicis.removeAllViews()
            } else {
                craurosisFornicisText.setText(
                    Global.craurosisFornicisList
                        .toString()
                        .substring(1, Global.craurosisFornicisList.toString().length - 1)
                )
            }
            if (Global.endometriumList.size == 0) {
                Endometrium.removeAllViews()
            } else {
                EndometriumText.setText(
                    Global.endometriumList
                        .toString().substring(1, Global.endometriumList.toString().length - 1)
                )
            }
            if (Global.fibroidAdditionList.size == 0) {
                fibroidAddition.removeAllViews()
            } else {
                fibroidAdditionText.setText(
                    Global.fibroidAdditionList
                        .toString().substring(1, Global.fibroidAdditionList.toString().length - 1)
                )
            }
            if (Global.focalLesionsList.size == 0) {
                focalLesions.removeAllViews()
            } else {
                focalLesionsText.setText(
                    Global.focalLesionsList
                        .toString().substring(1, Global.focalLesionsList.toString().length - 1)
                )
            }
            if (Global.hyperplasiaList.size == 0) {
                Hyperplasia.removeAllViews()
            } else {
                HyperplasiaText.setText(
                    Global.hyperplasiaList
                        .toString().substring(1, Global.hyperplasiaList.toString().length - 1)
                )
            }
            if (Global.intraCervicalAdditionList.size == 0) {
                intraCervicalAddition.removeAllViews()
            } else {
                intraCervicalAdditionText.setText(
                    Global.intraCervicalAdditionList
                        .toString()
                        .substring(1, Global.intraCervicalAdditionList.toString().length - 1)
                )
            }
            if (Global.intraUterineAdhesionList.size == 0) {
                intraUterineAdhesion.removeAllViews()
            } else {
                intraUterineAdhesionText.setText(
                    Global.intraUterineAdhesionList
                        .toString()
                        .substring(1, Global.intraUterineAdhesionList.toString().length - 1)
                )
            }
            if (Global.isthmusUteriList.size == 0) {
                isthmusUteri.removeAllViews()
            } else {
                isthmusUteriText.setText(
                    Global.isthmusUteriList
                        .toString().substring(1, Global.isthmusUteriList.toString().length - 1)
                )
            }
            if (Global.iUDList.size == 0) {
                iUD.removeAllViews()
            } else {
                iUDText.setText(
                    Global.iUDList
                        .toString().substring(1, Global.iUDList.toString().length - 1)
                )
            }
            if (Global.leftTubalOstiaList.size == 0) {
                leftTubalOstia.removeAllViews()
            } else {
                leftTubalOstiaText.setText(
                    Global.leftTubalOstiaList
                        .toString().substring(1, Global.leftTubalOstiaList.toString().length - 1)
                )
            }
            if (Global.lesionsList.size == 0) {
                lesions.removeAllViews()
            } else {
                lesionsText.setText(
                    Global.lesionsList
                        .toString().substring(1, Global.lesionsList.toString().length - 1)
                )
            }
            if (Global.myomaList.size == 0) {
                myoma.removeAllViews()
            } else {
                myomaText.setText(
                    Global.myomaList
                        .toString().substring(1, Global.myomaList.toString().length - 1)
                )
            }
            if (Global.nulliparaList.size == 0) {
                nullipara.removeAllViews()
            } else {
                nulliparaText.setText(
                    Global.nulliparaList
                        .toString().substring(1, Global.nulliparaList.toString().length - 1)
                )
            }
            if (Global.polypsList.size == 0) {
                polyps.removeAllViews()
            } else {
                polypsText.setText(
                    Global.polypsList
                        .toString().substring(1, Global.polypsList.toString().length - 1)
                )
            }
            if (Global.rightTubalOstiaList.size == 0) {
                rightTubalOstia.removeAllViews()
            } else {
                rightTubalOstiaText.setText(
                    Global.rightTubalOstiaList
                        .toString().substring(1, Global.rightTubalOstiaList.toString().length - 1)
                )
            }
            if (Global.uterusList.size == 0) {
                uterus.removeAllViews()
            } else {
                uterusText.setText(
                    Global.uterusList
                        .toString().substring(1, Global.uterusList.toString().length - 1)
                )
            }
        }
        if (Global.imageList.size == 0) {
            patientImages.removeAllViews()
        } else {
            for (i in 0 until Global.imageList.size) {
                println("at i = " + Uri.parse(Global.imageList[i]))
                if (Global.imageList.size < 3) {
                    imageView2.removeAllViews()
                }
                if (i == 0) {
                    Picasso.get().load(Uri.parse(Global.imageList[i]))
                        .into(image1)
                }
                if (i == 1) {
                    Picasso.get().load(Uri.parse(Global.imageList[i]))
                        .into(image2)
                }
                if (i == 2) {
                    Picasso.get().load(Uri.parse(Global.imageList[i]))
                        .into(image3)
                }
                if (i == 3) {
                    Picasso.get().load(Uri.parse(Global.imageList[i]))
                        .into(image4)
                }
            }
        }
    }

    private fun generatePdf() {
        val view = findViewById<ScrollView>(R.id.scrollView)

        //getting complete height and width of scrollView
        val totalHeight = view.getChildAt(0).height
        val totalWidth = view.getChildAt(0).width
        println("height = $totalHeight width = $totalWidth")
        var bitmap = Bitmap.createBitmap(totalWidth + 50, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        val paint = Paint()
        val pdfDocument = PdfDocument()
        val pageInfo = PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val pdfCanvas = page.canvas
        pdfCanvas.drawBitmap(bitmap, 0f, 0f, paint)
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
        //        Bitmap.createScaledBitmap(bitmap, 595, 842, true);
        pdfCanvas.drawBitmap(bitmap, 0f, 0f, paint)
        pdfDocument.finishPage(page)
        val csvCalendar = Calendar.getInstance()
        val csvSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val csvSimpleTimeFormat = SimpleDateFormat("hh:mm:ss")
        val currentDateString = DateFormat.getDateInstance().format(Date()) //yyyy-MM-dd

        //saving file

        val filename = System.currentTimeMillis()
            .toString() + "report.pdf" //now if multiple times "csv btn" is clicked, it'll make multiple csv's rather than appending in one
        println("file name =$filename")

        val myExternalFile: File
        myExternalFile = File(getReportsPath() +"/" + filename)
        try {
            val fileOutputStream = FileOutputStream(myExternalFile, true)
            pdfDocument.writeTo(fileOutputStream)
            pdfDocument.close()
            fileOutputStream.flush()
            fileOutputStream.close()
            if (myExternalFile.exists()) {
                val pdfUri: Uri
                pdfUri = Uri.fromFile(myExternalFile)
                val reference = FirebaseStorage.getInstance().reference
                    .child("/pdf/" + Global.patientId + "/" + filename)
                uploadTask = reference.putFile(pdfUri)
                (uploadTask as UploadTask).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot?> {
                    reference.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUri = uri.toString()
                        Toast.makeText(
                            this@HysteroscopyReportPdf, "Uploading!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val today = Calendar.getInstance().time
                        val sdf = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        )
                        val date = sdf.format(today)
                        Toast.makeText(
                            this@HysteroscopyReportPdf, "Report Uploaded!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val reference1 = FirebaseDatabase.getInstance()
                            .reference
                        reference1.child("DoctorsList").child(Global.doctorUID)
                            .child("PatientList").child(Global.patientId).child("report")
                            .child(date).child(System.currentTimeMillis().toString())
                            .setValue(downloadUri)
                        Global.cavumUteriList.clear()
                        Global.cervicalCanalBleedingList.clear()
                        Global.cervicalStenosisList.clear()
                        Global.craurosisFornicisList.clear()
                        Global.endometriumList.clear()
                        Global.fibroidAdditionList.clear()
                        Global.focalLesionsList.clear()
                        Global.hyperplasiaList.clear()
                        Global.intraCervicalAdditionList.clear()
                        Global.intraUterineAdhesionList.clear()
                        Global.isthmusUteriList.clear()
                        Global.iUDList.clear()
                        Global.leftTubalOstiaList.clear()
                        Global.lesionsList.clear()
                        Global.myomaList.clear()
                        Global.nulliparaList.clear()
                        Global.polypsList.clear()
                        Global.rightTubalOstiaList.clear()
                        Global.uterusList.clear()
                        val intent = Intent(
                            this@HysteroscopyReportPdf,
                            ReportSelection::class.java
                        )
                        startActivity(intent)
                    }
                }).addOnFailureListener { e ->
                    Toast.makeText(
                        this@HysteroscopyReportPdf, "Uploading! failed$e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Toast.makeText(
                    this@HysteroscopyReportPdf, "Pdf Created Successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.e("DEBUG", "File doesn't exist")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    fun getReportsPath() : String{
        // Create an image file name
        val storageDir = applicationContext?.getExternalFilesDir("Colposcope/"+Global.patientId+"/Reports/")
        return storageDir!!.absolutePath
    }


    fun commonDocumentDirPath(FolderName: String): File {
        val dir: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString() + "/" + FolderName
            )
        } else {
            File(Environment.getExternalStorageDirectory().toString() + "/" + FolderName)
        }
        return dir
    }
}