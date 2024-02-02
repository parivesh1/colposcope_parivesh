package com.jiangdg.demo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
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
import kotlinx.android.synthetic.main.sexual_assault_report.editButton
import kotlinx.android.synthetic.main.sexual_assault_report.Erythema
import kotlinx.android.synthetic.main.sexual_assault_report.ErythemaText
import kotlinx.android.synthetic.main.sexual_assault_report.SkinOrMucosalTrauma
import kotlinx.android.synthetic.main.sexual_assault_report.SkinOrMucosalTraumaText
import kotlinx.android.synthetic.main.sexual_assault_report.analInjuriesorinjurytoanalfolds
import kotlinx.android.synthetic.main.sexual_assault_report.analInjuriesorinjurytoanalfoldsText
import kotlinx.android.synthetic.main.sexual_assault_report.greenFilter
import kotlinx.android.synthetic.main.sexual_assault_report.greenFilterText
import kotlinx.android.synthetic.main.sexual_assault_report.image1
import kotlinx.android.synthetic.main.sexual_assault_report.image2
import kotlinx.android.synthetic.main.sexual_assault_report.image3
import kotlinx.android.synthetic.main.sexual_assault_report.image4
import kotlinx.android.synthetic.main.sexual_assault_report.imageView2
import kotlinx.android.synthetic.main.sexual_assault_report.patientDob
import kotlinx.android.synthetic.main.sexual_assault_report.patientImages
import kotlinx.android.synthetic.main.sexual_assault_report.patientMobile
import kotlinx.android.synthetic.main.sexual_assault_report.patientName
import kotlinx.android.synthetic.main.sexual_assault_report.posteriorFourchette
import kotlinx.android.synthetic.main.sexual_assault_report.posteriorFourchetteText
import kotlinx.android.synthetic.main.sexual_assault_report.presenceOfForeignBodies
import kotlinx.android.synthetic.main.sexual_assault_report.presenceOfForeignBodiesText
import kotlinx.android.synthetic.main.sexual_assault_report.savePdfButton
import kotlinx.android.synthetic.main.sexual_assault_report.sexualAssault
import kotlinx.android.synthetic.main.sexual_assault_report.stainingEffect
import kotlinx.android.synthetic.main.sexual_assault_report.stainingEffectText
import kotlinx.android.synthetic.main.sexual_assault_report.swabSample
import kotlinx.android.synthetic.main.sexual_assault_report.swabSampleText
import kotlinx.android.synthetic.main.sexual_assault_report.toolbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class SexualAssaultReportPdf : AppCompatActivity(){

    private var mDatabaseRef: DatabaseReference? = null
    private var uploadTask: StorageTask<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sexual_assault_report)
        toolbar.title="Report"

        getPatientInfo()
        savePdfButton.setOnClickListener(View.OnClickListener { generatePdf() })

        editButton.setOnClickListener{
            val intent = Intent(this@SexualAssaultReportPdf, ReportSelection::class.java)
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
        if (Global.analInjuriesorinjurytoanalfoldsList.size === 0 && Global.erythemaList.size === 0 && Global.greenFilterList.size === 0 && Global.posteriorFourchetteList.size === 0 && Global.presenceOfForeignBodiesList.size === 0 && Global.skinOrMucosalTraumaList.size === 0 && Global.stainingEffectList.size === 0 && Global.swabSampleList.size === 0) {
            sexualAssault.removeAllViews()
        } else {
            if (Global.analInjuriesorinjurytoanalfoldsList.size === 0) {
                analInjuriesorinjurytoanalfolds.removeAllViews()
            } else {
                analInjuriesorinjurytoanalfoldsText.setText(
                    Global.analInjuriesorinjurytoanalfoldsList
                        .toString().substring(
                            1,
                            Global.analInjuriesorinjurytoanalfoldsList.toString().length - 1
                        )
                )
            }
            if (Global.erythemaList.size === 0) {
                Erythema.removeAllViews()
            } else {
                ErythemaText.setText(
                    Global.erythemaList
                        .toString().substring(1, Global.erythemaList.toString().length - 1)
                )
            }
            if (Global.greenFilterList.size === 0) {
                greenFilter.removeAllViews()
            } else {
                greenFilterText.setText(
                    Global.greenFilterList
                        .toString().substring(1, Global.greenFilterList.toString().length - 1)
                )
            }
            if (Global.posteriorFourchetteList.size === 0) {
                posteriorFourchette.removeAllViews()
            } else {
                posteriorFourchetteText.setText(
                    Global.posteriorFourchetteList
                        .toString()
                        .substring(1, Global.posteriorFourchetteList.toString().length - 1)
                )
            }
            if (Global.presenceOfForeignBodiesList.size === 0) {
                presenceOfForeignBodies.removeAllViews()
            } else {
                presenceOfForeignBodiesText.setText(
                    Global.presenceOfForeignBodiesList
                        .toString()
                        .substring(1, Global.presenceOfForeignBodiesList.toString().length - 1)
                )
            }
            if (Global.skinOrMucosalTraumaList.size === 0) {
                SkinOrMucosalTrauma.removeAllViews()
            } else {
                SkinOrMucosalTraumaText.setText(
                    Global.skinOrMucosalTraumaList
                        .toString()
                        .substring(1, Global.skinOrMucosalTraumaList.toString().length - 1)
                )
            }
            if (Global.stainingEffectList.size === 0) {
                stainingEffect.removeAllViews()
            } else {
                stainingEffectText.setText(
                    Global.stainingEffectList
                        .toString().substring(1, Global.stainingEffectList.toString().length - 1)
                )
            }
            if (Global.swabSampleList.size === 0) {
                swabSample.removeAllViews()
            } else {
                swabSampleText.setText(
                    Global.swabSampleList
                        .toString().substring(1, Global.swabSampleList.toString().length - 1)
                )
            }
        }
        if (Global.imageList.size === 0) {
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
        val filepath =
            FileUtils.ROOT_PATH + "Colposcope" + "/" + Global.patientId + "/PDF Files/" + currentDateString + "/"
        val filename = System.currentTimeMillis()
            .toString() + "report.pdf" //now if multiple times "csv btn" is clicked, it'll make multiple csv's rather than appending in one
        println("file name =$filename")
        val folder = File(filepath)
        var success = true
        if (!folder.exists()) {
            success = folder.mkdirs()
        }
        if (success) {
            Toast.makeText(
                this@SexualAssaultReportPdf,
                "Folder Created Successfully!",
                Toast.LENGTH_SHORT
            ).show()
            // Do something on success
        } else {
            Toast.makeText(
                this@SexualAssaultReportPdf,
                "Temporary error in creating folder!",
                Toast.LENGTH_SHORT
            ).show()
            // Do something else on failure
        }
        val myExternalFile: File
        myExternalFile = File(filepath + filename)
        try {
            val fileOutputStream = FileOutputStream(myExternalFile, true)
            pdfDocument.writeTo(fileOutputStream)
            pdfDocument.close()
            fileOutputStream.flush()
            fileOutputStream.close()
            if (myExternalFile.exists()) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.setType("application/pdf");
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myExternalFile));
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(Intent.createChooser(intent, "Share Invoice"));

//                startActivity(intent);
                val pdfUri: Uri
                pdfUri = Uri.fromFile(myExternalFile)
                val reference = FirebaseStorage.getInstance().reference
                    .child("/pdf/" + Global.patientId + "/" + filename)
                uploadTask = reference.putFile(pdfUri)
                (uploadTask as UploadTask).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot?> {
                    reference.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUri = uri.toString()
                        Toast.makeText(
                            this@SexualAssaultReportPdf, "Uploading!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val today = Calendar.getInstance().time
                        val sdf = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        )
                        val date = sdf.format(today)
                        Toast.makeText(
                            this@SexualAssaultReportPdf, "Report Uploaded!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val reference1 = FirebaseDatabase.getInstance()
                            .reference
                        reference1.child("DoctorsList").child(Global.doctorUID)
                            .child("PatientList").child(Global.patientId).child("report")
                            .child(date).child(System.currentTimeMillis().toString())
                            .setValue(downloadUri)
                        Global.analInjuriesorinjurytoanalfoldsList.clear()
                        Global.erythemaList.clear()
                        Global.greenFilterList.clear()
                        Global.posteriorFourchetteList.clear()
                        Global.presenceOfForeignBodiesList.clear()
                        Global.skinOrMucosalTraumaList.clear()
                        Global.stainingEffectList.clear()
                        Global.swabSampleList.clear()
                        val intent = Intent(this@SexualAssaultReportPdf, ReportSelection::class.java)
                        startActivity(intent)
                    }
                }).addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(
                        this@SexualAssaultReportPdf, "Uploading! failed$e",
                        Toast.LENGTH_SHORT
                    ).show()
                })
                Toast.makeText(
                    this@SexualAssaultReportPdf, "Pdf Created Successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.e("DEBUG", "File doesn't exist")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}