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
import kotlinx.android.synthetic.main.leep_report.editButton
import kotlinx.android.synthetic.main.leep_report.BisthourySpecification
import kotlinx.android.synthetic.main.leep_report.BisthourySpecificationText
import kotlinx.android.synthetic.main.leep_report.anaesthesiaMode
import kotlinx.android.synthetic.main.leep_report.anaesthesiaModeText
import kotlinx.android.synthetic.main.leep_report.bleedingWhileOperation
import kotlinx.android.synthetic.main.leep_report.bleedingWhileOperationText
import kotlinx.android.synthetic.main.leep_report.daysAfterOperation
import kotlinx.android.synthetic.main.leep_report.daysAfterOperationText
import kotlinx.android.synthetic.main.leep_report.diagnosisBeforeOperation
import kotlinx.android.synthetic.main.leep_report.diagnosisBeforeOperationText
import kotlinx.android.synthetic.main.leep_report.image1
import kotlinx.android.synthetic.main.leep_report.image2
import kotlinx.android.synthetic.main.leep_report.image3
import kotlinx.android.synthetic.main.leep_report.image4
import kotlinx.android.synthetic.main.leep_report.imageView2
import kotlinx.android.synthetic.main.leep_report.leepReport
import kotlinx.android.synthetic.main.leep_report.operationDeepness
import kotlinx.android.synthetic.main.leep_report.operationDeepnessText
import kotlinx.android.synthetic.main.leep_report.operationMode
import kotlinx.android.synthetic.main.leep_report.operationModeText
import kotlinx.android.synthetic.main.leep_report.operationRange
import kotlinx.android.synthetic.main.leep_report.operationRangeText
import kotlinx.android.synthetic.main.leep_report.operationTime
import kotlinx.android.synthetic.main.leep_report.operationTimeText
import kotlinx.android.synthetic.main.leep_report.pathologyResultAfterOperation
import kotlinx.android.synthetic.main.leep_report.pathologyResultAfterOperationText
import kotlinx.android.synthetic.main.leep_report.patientDob
import kotlinx.android.synthetic.main.leep_report.patientImages
import kotlinx.android.synthetic.main.leep_report.patientMobile
import kotlinx.android.synthetic.main.leep_report.patientName
import kotlinx.android.synthetic.main.leep_report.savePdfButton
import kotlinx.android.synthetic.main.leep_report.toolbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class LeepReportPdf : AppCompatActivity(){
    private var mDatabaseRef: DatabaseReference? = null
    private var uploadTask: StorageTask<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leep_report)
        toolbar.title = "Report"
        getPatientInfo()
        savePdfButton.setOnClickListener(View.OnClickListener { generatePdf() })
        editButton.setOnClickListener{
            val intent = Intent(this@LeepReportPdf, ReportSelection::class.java)
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
        if (Global.anaesthesiaModeList.size == 0 && Global.operationModeList.size == 0 && Global.bisthourySpecificationList.size == 0 && Global.operationTimeList.size == 0 && Global.operationDeepnessList.size == 0 && Global.daysAfterOperationList.size == 0 && Global.operationRangeList.size == 0 && Global.bleedingWhileOperationList.size == 0 && Global.pathologyResultAfterOperationList == null && Global.diagnosisBeforeOperationList == null) {
            leepReport.removeAllViews()
        } else {
            if (Global.anaesthesiaModeList.size == 0) {
                anaesthesiaMode.removeAllViews()
            } else {
                anaesthesiaModeText.setText(
                    Global.anaesthesiaModeList
                        .toString().substring(1, Global.anaesthesiaModeList.toString().length - 1)
                )
            }
            if (Global.operationModeList.size == 0) {
                operationMode.removeAllViews()
            } else {
                operationModeText.setText(
                    Global.operationModeList
                        .toString().substring(1, Global.operationModeList.toString().length - 1)
                )
            }
            if (Global.bisthourySpecificationList.size == 0) {
                BisthourySpecification.removeAllViews()
            } else {
                BisthourySpecificationText.setText(
                    Global.bisthourySpecificationList
                        .toString()
                        .substring(1, Global.bisthourySpecificationList.toString().length - 1)
                )
            }
            if (Global.operationTimeList.size == 0) {
                operationTime.removeAllViews()
            } else {
                operationTimeText.setText(
                    Global.operationTimeList
                        .toString().substring(1, Global.operationTimeList.toString().length - 1)
                )
            }
            if (Global.operationDeepnessList.size == 0) {
                operationDeepness.removeAllViews()
            } else {
                operationDeepnessText.setText(
                    Global.operationDeepnessList
                        .toString()
                        .substring(1, Global.operationDeepnessList.toString().length - 1)
                )
            }
            if (Global.daysAfterOperationList.size == 0) {
                daysAfterOperation.removeAllViews()
            } else {
                daysAfterOperationText.setText(
                    Global.daysAfterOperationList
                        .toString()
                        .substring(1, Global.daysAfterOperationList.toString().length - 1)
                )
            }
            if (Global.operationRangeList.size == 0) {
                operationRange.removeAllViews()
            } else {
                operationRangeText.setText(
                    Global.operationRangeList
                        .toString().substring(1, Global.operationRangeList.toString().length - 1)
                )
            }
            if (Global.bleedingWhileOperationList.size == 0) {
                bleedingWhileOperation.removeAllViews()
            } else {
                bleedingWhileOperationText.setText(
                    Global.bleedingWhileOperationList
                        .toString()
                        .substring(1, Global.bleedingWhileOperationList.toString().length - 1)
                )
            }
            if (Global.pathologyResultAfterOperationList == null) {
                pathologyResultAfterOperation.removeAllViews()
            } else {
                pathologyResultAfterOperationText.setText(Global.pathologyResultAfterOperationList)
            }
            if (Global.diagnosisBeforeOperationList == null) {
                diagnosisBeforeOperation.removeAllViews()
            } else {
                diagnosisBeforeOperationText.setText(Global.diagnosisBeforeOperationList)
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
                    Picasso.get().load(Uri.parse(Global.imageList[i])).into(image1)
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
                            this@LeepReportPdf, "Uploading!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val today = Calendar.getInstance().time
                        val sdf = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        )
                        val date = sdf.format(today)
                        Toast.makeText(
                            this@LeepReportPdf, "Report Uploaded!!",
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
                        val intent = Intent(this@LeepReportPdf, ReportSelection::class.java)
                        startActivity(intent)
                    }
                }).addOnFailureListener { e ->
                    Toast.makeText(
                        this@LeepReportPdf, "Uploading! failed$e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Toast.makeText(
                    this@LeepReportPdf, "Pdf Created Successfully!",
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