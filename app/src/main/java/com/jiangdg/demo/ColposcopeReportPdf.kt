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
import android.widget.ImageView
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
import kotlinx.android.synthetic.main.create_repot.acetoUptake
import kotlinx.android.synthetic.main.create_repot.acetoUptakeList
import kotlinx.android.synthetic.main.create_repot.biopsyImageV
import kotlinx.android.synthetic.main.create_repot.biopsyView
import kotlinx.android.synthetic.main.create_repot.checkRecord
import kotlinx.android.synthetic.main.create_repot.chiefComplaint
import kotlinx.android.synthetic.main.create_repot.chiefComplaintList
import kotlinx.android.synthetic.main.create_repot.color
import kotlinx.android.synthetic.main.create_repot.colorList
import kotlinx.android.synthetic.main.create_repot.colposcopyFinding
import kotlinx.android.synthetic.main.create_repot.colposcopyFindingList
import kotlinx.android.synthetic.main.create_repot.cytologyReport
import kotlinx.android.synthetic.main.create_repot.cytologyReportList
import kotlinx.android.synthetic.main.create_repot.editButton
import kotlinx.android.synthetic.main.create_repot.finalImpression
import kotlinx.android.synthetic.main.create_repot.finalImpressionList
import kotlinx.android.synthetic.main.create_repot.image1
import kotlinx.android.synthetic.main.create_repot.image2
import kotlinx.android.synthetic.main.create_repot.image3
import kotlinx.android.synthetic.main.create_repot.image4
import kotlinx.android.synthetic.main.create_repot.imageView2
import kotlinx.android.synthetic.main.create_repot.iodineStaining
import kotlinx.android.synthetic.main.create_repot.iodineStainingList
import kotlinx.android.synthetic.main.create_repot.iodineStainingReid
import kotlinx.android.synthetic.main.create_repot.iodineStainingReidList
import kotlinx.android.synthetic.main.create_repot.lessionSize
import kotlinx.android.synthetic.main.create_repot.lessionSizeList
import kotlinx.android.synthetic.main.create_repot.margins
import kotlinx.android.synthetic.main.create_repot.marginsList
import kotlinx.android.synthetic.main.create_repot.marginsReid
import kotlinx.android.synthetic.main.create_repot.marginsReidList
import kotlinx.android.synthetic.main.create_repot.pathologicalReport
import kotlinx.android.synthetic.main.create_repot.pathologicalReportList
import kotlinx.android.synthetic.main.create_repot.patientDob
import kotlinx.android.synthetic.main.create_repot.patientImages
import kotlinx.android.synthetic.main.create_repot.patientMobile
import kotlinx.android.synthetic.main.create_repot.patientName
import kotlinx.android.synthetic.main.create_repot.precaution
import kotlinx.android.synthetic.main.create_repot.precautionList
import kotlinx.android.synthetic.main.create_repot.reidScore
import kotlinx.android.synthetic.main.create_repot.reidValue
import kotlinx.android.synthetic.main.create_repot.reidValueResult
import kotlinx.android.synthetic.main.create_repot.savePdfButton
import kotlinx.android.synthetic.main.create_repot.swedeScore
import kotlinx.android.synthetic.main.create_repot.swedeValue
import kotlinx.android.synthetic.main.create_repot.swedeValueResult
import kotlinx.android.synthetic.main.create_repot.treatment
import kotlinx.android.synthetic.main.create_repot.treatmentList
import kotlinx.android.synthetic.main.create_repot.vessels
import kotlinx.android.synthetic.main.create_repot.vesselsList
import kotlinx.android.synthetic.main.create_repot.vesselsReid
import kotlinx.android.synthetic.main.create_repot.vesselsReidList
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ColposcopeReportPdf : AppCompatActivity(){
    private var mDatabaseRef: DatabaseReference? = null
    private val biopsyImageView: ImageView? = null


    var biopsyImage: DatabaseReference? = null
    private val mDatabaseRef_Info: DatabaseReference? = null
    private var uploadTask: StorageTask<*>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_repot)

        getPatientInfo()
        savePdfButton.setOnClickListener(View.OnClickListener { generatePdf() })
        editButton.setOnClickListener{
            val intent = Intent(this@ColposcopeReportPdf, ReportSelection::class.java)
            startActivity(intent)
        }




    }
    private fun getPatientInfo() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoctorsList")
            .child(Global.doctorUID).child("PatientList")
            .child(Global.patientId) // initialise the DatabaseReference of the firebase cloud with path to DoctorsList
        mDatabaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val upload = snapshot.getValue(Upload::class.java)
                patientDob.setText(upload!!.DOB)
                patientName.setText(upload!!.Name)
                patientMobile.setText(upload!!.Mobile)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        if (Global.chiefComplaintList.size == 0 && Global.pathologicalReportList.size == 0 &&
            Global.finalImpressionList.size == 0 && Global.cytologyReportList.size == 0 &&
            Global.urologyFindingsList.size == 0) {
            checkRecord.removeAllViews()
        } else {
            System.out.println("size" + Global.pathologicalReportList.size)
            if (Global.chiefComplaintList.size == 0) {
                chiefComplaint.removeAllViews()
            } else {
                chiefComplaintList.text = Global.chiefComplaintList.toString()
                    .substring(1, Global.chiefComplaintList.toString().length - 1)
            }
            if (Global.urologyFindingsList.size == 0) {
                colposcopyFinding.removeAllViews()
            } else {
                colposcopyFindingList.text =
                    Global.urologyFindingsList.toString()
                        .substring(1, Global.urologyFindingsList.toString().length - 1)
            }
            if (Global.cytologyReportList.size == 0) {
                cytologyReport.removeAllViews()
            } else {
                cytologyReportList.text = Global.cytologyReportList.toString()
                    .substring(1, Global.cytologyReportList.toString().length - 1)
            }
            if (Global.finalImpressionList.size == 0) {
                finalImpression.removeAllViews()
            } else {
                finalImpressionList.text = Global.finalImpressionList.toString()
                    .substring(1, Global.finalImpressionList.toString().length - 1)
            }
            if (Global.pathologicalReportList.size == 0) {
                pathologicalReport.removeAllViews()
            } else {
                pathologicalReportList.text =
                    Global.pathologicalReportList.toString()
                        .substring(1, Global.pathologicalReportList.toString().length - 1)
            }
        }
        if (Global.swedeAcetoUptakeList.isEmpty() && Global.swedeMarginsList.isEmpty() && Global.swedeVesselsList.isEmpty() && Global.swedeLessionSizeList.isEmpty() && Global.swedeIodineStainingList.isEmpty()) {
            swedeScore.removeAllViews()
        } else {
            if (Global.swedeIodineStainingList.isEmpty()) {
                iodineStaining.removeAllViews()
            } else {
                iodineStainingList.text = Global.swedeIodineStainingList
            }
            if (Global.swedeLessionSizeList.isEmpty()) {
                lessionSize.removeAllViews()
            } else {
                lessionSizeList.text = Global.swedeLessionSizeList
            }
            if (Global.swedeVesselsList.isEmpty()) {
                vessels.removeAllViews()
            } else {
                vesselsList.text = Global.swedeVesselsList
            }
            if (Global.swedeAcetoUptakeList.isEmpty()) {
                acetoUptake.removeAllViews()
            } else {
                acetoUptakeList.text = Global.swedeAcetoUptakeList
            }
            if (Global.swedeMarginsList.isEmpty()) {
                margins.removeAllViews()
            } else {
                marginsList.text = Global.swedeMarginsList
            }
            swedeValueResult.text = Global.swedeScoreResult
            swedeValue.text = java.lang.String.valueOf(Global.swedeSum)
        }
        if (Global.reidIodineStainingList.isEmpty() && Global.reidColorList.isEmpty() && Global.reidVesselsList.isEmpty() && Global.reidMarginsList.isEmpty()) {
            reidScore.removeAllViews()
        } else {
            if (Global.reidMarginsList.isEmpty()) {
                marginsReid.removeAllViews()
            } else {
                marginsReidList.text = Global.reidMarginsList
            }
            if (Global.reidVesselsList.isEmpty()) {
                vesselsReid.removeAllViews()
            } else {
                vesselsReidList.text = Global.reidVesselsList
            }
            if (Global.reidColorList.isEmpty()) {
                color.removeAllViews()
            } else {
                colorList.text = Global.reidColorList
            }
            if (Global.reidIodineStainingList.isEmpty()) {
                iodineStainingReid.removeAllViews()
            } else {
                iodineStainingReidList.text = Global.reidIodineStainingList
            }
            reidValueResult.text = Global.reidScoreResult
            reidValue.text = java.lang.String.valueOf(Global.reidSum)
        }
        if (Global.precautionsList.size == 0) {
            precaution.removeAllViews()
        } else {
            precautionList.text =
                Global.precautionsList.toString()
                    .substring(1, Global.precautionsList.toString().length - 1)
        }
        if (Global.treatmentList.size == 0) {
            treatment.removeAllViews()
        } else {
            treatmentList.text =
                Global.treatmentList.toString()
                    .substring(1, Global.treatmentList.toString().length - 1)
        }
        val today = Calendar.getInstance().time
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.format(today)
        mDatabaseRef!!.child("Biopsy").child(date).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val imageKey = ds.key
                        println("img key $imageKey")
                        mDatabaseRef!!.child("Biopsy").child(date).child(imageKey!!)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val value = snapshot.value.toString()
                                    println("imgUri $value")
                                    Picasso.get().load(Uri.parse(value)).into(biopsyImageV)
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                    }
                } else {
                    biopsyView.removeAllViews()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                biopsyView.removeAllViews()
            }
        })
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
        val myExternalFile: File = File(getReportsPath() +"/" + filename)
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
                val pdfUri: Uri = Uri.fromFile(myExternalFile)
                val reference = FirebaseStorage.getInstance().reference
                    .child("/pdf/" + Global.patientId + "/" + filename)
                uploadTask = reference.putFile(pdfUri)
                (uploadTask as UploadTask).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot?> {
                    reference.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUri = uri.toString()
                        Toast.makeText(
                            this@ColposcopeReportPdf, "Uploading!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val today = Calendar.getInstance().time
                        val sdf = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        )
                        val date = sdf.format(today)
                        Toast.makeText(
                            this@ColposcopeReportPdf, "Report Uploaded!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val reference1 = FirebaseDatabase.getInstance()
                            .reference
                        reference1.child("DoctorsList").child(Global.doctorUID)
                            .child("PatientList").child(Global.patientId).child("report")
                            .child(date).child(System.currentTimeMillis().toString())
                            .setValue(downloadUri)
                        Global.chiefComplaintList.clear()
                        Global.imageList.clear()
                        Global.urologyFindingsList.clear()
                        Global.cytologyReportList.clear()
                        Global.finalImpressionList.clear()
                        Global.pathologicalReportList.clear()
                        Global.precautionsList.clear()
                        Global.treatmentList.clear()
                        Global.swedeAcetoUptakeList = ""
                        Global.swedeMarginsList = ""
                        Global.swedeVesselsList = ""
                        Global.swedeLessionSizeList = ""
                        Global.swedeSum = 0
                        Global.swedeScoreResult = ""
                        Global.reidSum = 0
                        Global.reidScoreResult = ""
                        Global.reidMarginsList = ""
                        Global.reidVesselsList = ""
                        Global.reidColorList = ""
                        Global.reidIodineStainingList = ""
                        val intent = Intent(this@ColposcopeReportPdf, ReportSelection::class.java)
                        startActivity(intent)
                    }
                }).addOnFailureListener { e ->
                    Toast.makeText(
                        this@ColposcopeReportPdf, "Uploading! failed$e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Toast.makeText(
                    this@ColposcopeReportPdf, "Pdf Created Successfully!",
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