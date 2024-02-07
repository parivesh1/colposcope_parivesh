package com.jiangdg.demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.report_selection.imgSelector
import kotlinx.android.synthetic.main.report_selection.listViewLayout
import kotlinx.android.synthetic.main.report_selection.multiImage
import kotlinx.android.synthetic.main.report_selection.nextButton
import kotlinx.android.synthetic.main.report_selection.noImage
import kotlinx.android.synthetic.main.report_selection.refButton
import kotlinx.android.synthetic.main.report_selection.reports
import kotlinx.android.synthetic.main.report_selection.toolbar

class ReportSelection: AppCompatActivity() {
    var report = arrayOf(
        "Colposcopy Report",
        "swede score",
        "Reid score",
        "Biopsy",
        "Sexual Assault",
        "Cryo Surgery",
        "Hysteroscopy",
        "LEEP/LEITZ"
    )

    var patientImage: DatabaseReference? = null
    var mDatabaseRef_Info:DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_selection)
        toolbar.title="Select Report"

        mDatabaseRef_Info = FirebaseDatabase.getInstance().reference
        patientImage = mDatabaseRef_Info!!.child("DoctorsList").child(Global.doctorUID)
            .child("PatientList").child(Global.patientId).child("Images")

        noImage.isChecked = true

        var arr: ArrayAdapter<String?> = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            report
        )
        reports.adapter = arr
        reports.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            if (i == 0) {
                val intent = Intent(this@ReportSelection, UrologyReport::class.java)
                startActivity(intent)
            }
            if (i == 1) {
                val intent = Intent(this@ReportSelection, SwedeScore::class.java)
                startActivity(intent)
            }
            if (i == 2) {
                val intent = Intent(this@ReportSelection, ReidScore::class.java)
                startActivity(intent)
            }
            if (i == 3) {
                val intent = Intent(this@ReportSelection, MarkBiopsy::class.java)
                startActivity(intent)
            }
            if (i == 4) {
                val intent = Intent(this@ReportSelection, SexualAssault::class.java)
                startActivity(intent)
            }
            if (i == 5) {
                val intent = Intent(this@ReportSelection, CryoSurgery::class.java)
                startActivity(intent)
            }
            if (i == 6) {
                val intent = Intent(this@ReportSelection, HysteroscopyReport::class.java)
                startActivity(intent)
            }
            if (i == 7) {
                val intent = Intent(this@ReportSelection, LeepReport::class.java)
                startActivity(intent)
            }
        }


        nextButton.setOnClickListener(View.OnClickListener {
            if (Global.chiefComplaintList.size != 0 || Global.pathologicalReportList.size != 0 || Global.finalImpressionList.size != 0 || Global.cytologyReportList.size != 0 || Global.urologyFindingsList.size != 0) {
                val intent = Intent(this@ReportSelection, ColposcopeReportPdf::class.java)
                startActivity(intent)
            } else if (Global.analInjuriesorinjurytoanalfoldsList.size != 0 || Global.erythemaList.size != 0 || Global.greenFilterList.size != 0 || Global.posteriorFourchetteList.size != 0 || Global.presenceOfForeignBodiesList.size != 0 || Global.skinOrMucosalTraumaList.size != 0 || Global.stainingEffectList.size != 0 || Global.swabSampleList.size != 0) {
                val intent = Intent(this@ReportSelection, SexualAssaultReportPdf::class.java)
                startActivity(intent)
            } else if (Global.cryoSurgeryList.size != 0 || Global.freezingDoneList.size != 0 || Global.iceBallList.size != 0 || Global.probeTipList.size != 0 || Global.thawCycleList.size != 0) {
                val intent = Intent(this@ReportSelection, CryoSurgeryReportPdf::class.java)
                startActivity(intent)
            } else if (Global.cavumUteriList.size != 0 || Global.cervicalCanalBleedingList.size != 0 || Global.cervicalStenosisList.size != 0 || Global.craurosisFornicisList.size != 0 || Global.endometriumList.size != 0 || Global.fibroidAdditionList.size != 0 || Global.focalLesionsList.size != 0 || Global.hyperplasiaList.size != 0 || Global.intraCervicalAdditionList.size != 0 || Global.intraUterineAdhesionList.size != 0 || Global.isthmusUteriList.size != 0 || Global.iUDList.size !== 0 || Global.leftTubalOstiaList.size != 0 || Global.lesionsList.size != 0 || Global.myomaList.size != 0 || Global.nulliparaList.size != 0 || Global.polypsList.size != 0 || Global.rightTubalOstiaList.size != 0 || Global.uterusList.size != 0) {
                val intent = Intent(this@ReportSelection, HysteroscopyReportPdf::class.java)
                startActivity(intent)
            } else if (Global.anaesthesiaModeList.size != 0 || Global.operationModeList.size != 0 || Global.bisthourySpecificationList.size != 0 || Global.operationTimeList.size != 0 || Global.operationDeepnessList.size != 0 || Global.daysAfterOperationList.size != 0 || Global.operationRangeList.size != 0 || Global.bleedingWhileOperationList.size != 0 || Global.pathologyResultAfterOperationList != null || Global.diagnosisBeforeOperationList != null) {
                val intent = Intent(this@ReportSelection, LeepReportPdf::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@ReportSelection, "No data to create report !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        refButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ReportSelection, ReferPatient::class.java)
            startActivity(intent)
        })

        imgSelector.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (noImage.isChecked()) {
                println("value of i$i")
                listViewLayout.removeAllViews()
            }
            if (multiImage.isChecked()) {
                patientImage!!.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children) {
                            val keyDate = ds.key
                            println("date $keyDate")
                            patientImage!!.child(keyDate!!)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (ds in snapshot.children) {
                                            val imageKey = ds.key
                                            println("img key $imageKey")
                                            patientImage!!.child(keyDate!!).child(imageKey!!)
                                                .addValueEventListener(object : ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        val value = snapshot.getValue(
                                                            String::class.java
                                                        )
                                                        println("imgUri $value")
                                                        addImageCards(imageKey, Uri.parse(value))
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {}
                                                })
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }
        })


    }
    private fun addImageCards(chkBoxName: String, imgUri: Uri) {
        val view: View = layoutInflater.inflate(R.layout.image_card, null)
        val checkBox = view.findViewById<CheckBox>(R.id.checkbox)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(imgUri).into(imageView)
        checkBox.text = chkBoxName
        if (Global.imageList.contains(imgUri.toString())) {
            checkBox.isChecked = true
        }
        listViewLayout.addView(view)
        checkBox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                Global.imageList.add(imgUri.toString())
            } else {
                Global.imageList.remove(imgUri.toString())
            }
        }
    }
}