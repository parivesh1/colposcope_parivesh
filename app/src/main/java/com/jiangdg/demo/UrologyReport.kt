package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.urology_report.clearButton
import kotlinx.android.synthetic.main.urology_report.exitButton
import kotlinx.android.synthetic.main.urology_report.listViewLayout
import kotlinx.android.synthetic.main.urology_report.remark
import kotlinx.android.synthetic.main.urology_report.reports
import kotlinx.android.synthetic.main.urology_report.saveButton
import kotlinx.android.synthetic.main.urology_report.scrollView
import kotlinx.android.synthetic.main.urology_report.toolbar


class UrologyReport: AppCompatActivity() {
    var report = arrayOf(
        "Chief Complaint",
        "Urology Findings",
        "Cytology Report",
        "Final Impression",
        "Pathological Report",
        "Precautions",
        "Remarks",
        "Treatment"
    )

    private var chiefComplaint = arrayOf("Benign Prostatic Enlargement (BPE)", "Urinary Tract Infections (UTIs)", "Kidney Stones", "Hematuria", "Urinary Incontinence", "Overactive bladder",
        "Enlarged prostate", "Bladder muscles become weak", "Sphincter muscles become weak", "Weak bladder muscles", "Neuromuscular problems",
        "Overhydration", "Prostate Cancer", "Frequent urge to urinate", "Foul-smelling urine",
        "Pain or burning sensation while urination", "Fever or chills", "Fatigue", "Muscle aches", "Pain in lower stomach, side of back or groin",
        "Hernia", "Lump in abdomen", "Unexplained weight loss")

    var urologyFindings = arrayOf("Prostate cancer", "Urinary tract infections", "Erectile dysfunction", "Incontinence",
        "Kidney stones",
        "Overactive bladder", "Prostatitis", "Bladder prolapse", "Hematuria", "Interstitial cystitis",
        "Hydronephrosis", "Kidney cancer", "Infertility", "Congenital urologic conditions",
        "Hypospadias", "Urethral cancer", "Bedwetting", "Bladder exstrophy",
        "Common paediatric tumours ",
        "Ureterocele", "Ureteropelvic junction obstruction", "Urethral stricture disease")

    private var cytologyReport = arrayOf("Actinomyces", "Adenocarcinoma In Suit(AIS)", "AGUS", "ASCUS", "Atrophy", "Chlamydia",
        "Endometrial Adenocarcinoma", "Extrauterine Adenocarcinoma", "Fungal Organisms",
        "Herpes Simplex Viruss", "HPV",
        "HSIL", "IVD", "LSIL", "Mild Endocervical Cell Dysplasia", "Mild Inflamation", "Moderate Inflamation",
        "Normal",
        "Predominance Of Coccobacilli ", "Severe Inflamation", "Squamous Cell Carcinoma", "Trichomonas",
        "Radiation", "Severe Endocervical Cell Dysplasia")

    var finalImpression = arrayOf("Urinary Tract infection (UTIs) Detected", "Cancer of the Bladder","Kidney Cancer","Cancer in Adrenal gland", "Urinary Tract infections", " Kidney stones",
        "Regular Three Monthly Follow-Up")

    var pathologicalReport = arrayOf("Acute Cervicitis", "Adenoid Cystic Carcinoma", "Adenosquamous Carcinoma",
        "Cervical Malignant Meanoma", "Cervical Papilloma",
        "Cervical Polyp", "Cervical Sarcoma", "CIN-1", "CIN-2", "CIN-3", "HPV",
        "Invasive Squamous Carcinoma", "CMIcro Invasive Squamous Carcinoma (MIC)")

    var Precautions = arrayOf("Stay well hydrated", "Minimize douching, sprays, or powders in the genital area",
        "Maintain Good Sexual Hygiene")

    var Treatment = arrayOf("Trimethoprim and sulfamethoxazole", "Conization", "ACryothalamectomy", "Endocervical Curettage(ECC)", "Laser",
        "LEEP", "Radiation", "Radical Hysterectomy", "Routine Follow-Up")

    var mylist = ArrayList<String>()
    var urologyReport = Global()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.urology_report)
        toolbar.title="Urology Report"

        val arr: ArrayAdapter<String>
        arr = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            report
        )
        reports.adapter = arr

        reports.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                makeVisible(view)
                createCheckBox(chiefComplaint, "chiefComplaint")
            }
            if (position == 1) {
                makeVisible(view)
                createCheckBox(urologyFindings, "urologyFindings")
            }
            if (position == 2) {
                makeVisible(view)
                createCheckBox(cytologyReport, "cytologyReport")
            }
            if (position == 3) {
                makeVisible(view)
                createCheckBox(finalImpression, "finalImpression")
            }
            if (position == 4) {
                makeVisible(view)
                createCheckBox(pathologicalReport, "pathologicalReport")
            }
            if (position == 5) {
                makeVisible(view)
                createCheckBox(Precautions, "Precautions")
            }
            if (position == 6) {
                scrollView.visibility = View.GONE
                remark.visibility = View.VISIBLE
            }
            if (position == 7) {
                makeVisible(view)
                createCheckBox(Treatment, "Treatment")
            }
            if (position == 8) {
                scrollView.visibility = View.GONE
                remark.visibility = View.VISIBLE
            }
        })

        saveButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@UrologyReport, ReportSelection::class.java)
            startActivity(intent)
            System.out.println(Global.chiefComplaintList)
        })
        clearButton.setOnClickListener(View.OnClickListener {
            Global.chiefComplaintList.clear()
            mylist.clear()
            Global.urologyFindingsList.clear()
            Global.cytologyReportList.clear()
            Global.finalImpressionList.clear()
            Global.pathologicalReportList.clear()
            Global.precautionsList.clear()
            Global.treatmentList.clear()
            finish()
            startActivity(intent)
        })
        exitButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@UrologyReport, ReportSelection::class.java)
            startActivity(intent)
        })
    }
    private fun makeVisible(view: View) {
        listViewLayout.removeAllViews()
        scrollView.visibility = View.VISIBLE
        remark.visibility = View.GONE
    }

    private fun createCheckBox(arrList: Array<String>, name: String) {
        for (i in arrList.indices) {
            val checkBox = CheckBox(listViewLayout.getContext())
            checkBox.text = arrList[i]
            listViewLayout.addView(checkBox)
            if (mylist.contains(arrList[i]) || Global.urologyFindingsList.contains(arrList[i])
                || Global.cytologyReportList.contains(arrList[i]) ||
                Global.finalImpressionList.contains(arrList[i]) ||
                Global.pathologicalReportList.contains(arrList[i]) ||
                Global.precautionsList.contains(arrList[i]) ||
                Global.treatmentList.contains(arrList[i]) ||
                Global.chiefComplaintList.contains(arrList[i])
            ) {
                checkBox.isChecked = true
            }
            //            checkBox.setChecked(true);
            checkBox.setOnCheckedChangeListener {_, b ->
                if (b) {
                    mylist.add(checkBox.text.toString())
                    if (name === "Treatment") {
                        Global.treatmentList +=checkBox.text.toString()
                    }
                    if (name === "Precautions") {
                        Global.precautionsList += checkBox.text.toString()
                    }
                    if (name === "pathologicalReport") {
                        Global.pathologicalReportList += checkBox.text.toString()
                    }
                    if (name === "finalImpression") {
                        Global.finalImpressionList += checkBox.text.toString()
                    }
                    if (name === "cytologyReport") {
                        Global.cytologyReportList += checkBox.text.toString()
                    }
                    if (name === "urologyFindings") {
                        Global.urologyFindingsList += checkBox.text.toString()
                    }
                    if (name === "chiefComplaint") {
                        Global.chiefComplaintList += checkBox.text.toString()
                    }
                } else {
                    mylist.remove(checkBox.text.toString())
                    if (name === "Treatment") {
                        Global.treatmentList -= checkBox.text.toString()
                    }
                    if (name === "Precautions") {
                        Global.precautionsList -= checkBox.text.toString()
                    }
                    if (name === "pathologicalReport") {
                        Global.precautionsList -= checkBox.text.toString()
                    }
                    if (name === "finalImpression") {
                        Global.finalImpressionList -= checkBox.text.toString()
                    }
                    if (name === "cytologyReport") {
                        Global.cytologyReportList -= checkBox.text.toString()
                    }
                    if (name === "urologyFindings") {
                        Global.urologyFindingsList -= checkBox.text.toString()
                    }
                    if (name === "chiefComplaint") {
                        Global.chiefComplaintList -= checkBox.text.toString()
                    }
                }
            }
        }
    }


}