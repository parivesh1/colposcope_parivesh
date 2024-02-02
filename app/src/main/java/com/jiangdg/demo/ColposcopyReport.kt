package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.colposcopy_report.clearButton
import kotlinx.android.synthetic.main.colposcopy_report.exitButton
import kotlinx.android.synthetic.main.colposcopy_report.listViewLayout
import kotlinx.android.synthetic.main.colposcopy_report.remark
import kotlinx.android.synthetic.main.colposcopy_report.reports
import kotlinx.android.synthetic.main.colposcopy_report.saveButton
import kotlinx.android.synthetic.main.colposcopy_report.scrollView
import kotlinx.android.synthetic.main.colposcopy_report.toolbar


class ColposcopyReport: AppCompatActivity() {
    var report = arrayOf(
        "Chief Complaint",
        "Colposcopy Findings",
        "Cytology Report",
        "Final Impression",
        "Pathological Report",
        "Precautions",
        "Remarks",
        "Treatment",
        "Custom Remarks"
    )

    var chiefComplaint = arrayOf(
        "Abdominal Mass",
        "Abdominal Pain",
        "Abdominal Bleeding",
        "Amenorrhoea",
        "Anemia",
        "Become Thin",
        "Constipation",
        "Dyspareunia",
        "Fishy Odor",
        "Frequent Micturition",
        "Inter Menstrual Bleeding",
        "Pelvic Examine",
        "Post Coital Bleeding",
        "Post Menopausal Bleding",
        "Purulent Discharge",
        "Sterility",
        "Vaginal Pruritus",
        "Vaginal Soreness",
        "Vulvar Burning",
        "Vulvar Erthyema",
        "Vulvar Neoplasm",
        "Vulvar Pruritus",
        "Vulvar Ulcer"
    )
    var colposcopyFindings = arrayOf(
        "Aceto-White Epithelium",
        "Atrophy",
        "Atypical Vessels",
        "Cervix Not Vissible",
        "Colposcopically SuspectInvasive Carcinoma",
        "Columnar Epithelium",
        "Cyst",
        "Exophytic Condyloma",
        "Flat",
        "Gland Opening & Ectropion",
        "Inflamantion",
        "Iodine Masculine Area",
        "Leukoplakia",
        "Micropapillary or Micro Convoluted",
        "Moasic",
        "Nabothian Cyst",
        "Nabothian Follicle",
        "Non Aceto-White Micropapillay Surface",
        "Normal Transformation Zone ",
        "Original Squamous Epithelium",
        "Others",
        "Punctation",
        "Servere Inflamation or Severe Atophy",
        "Squamonocolumnar Junctive not Atrophy",
        "Ulcer",
        "White Ring & White Gland"
    )

    var cytologyReport = arrayOf(
        "Actinomyces",
        "Adenocarcinoma In Suit(AIS)",
        "AGUS",
        "ASCUS",
        "Atrophy",
        "Chlamydia",
        "Endometrial Adenocarcinoma",
        "Extrauterine Adenocarcinoma",
        "Fungal Organisms",
        "Herpes Simplex Viruss",
        "HPV",
        "HSIL",
        "IVD",
        "LSIL",
        "Mild Endocervical Cell Dysplasia",
        "Mild Inflamation",
        "Moderate Inflamation",
        "Normal",
        "Predominance Of Coccobacilli ",
        "Severe Inflamation",
        "Squamous Cell Carcinoma",
        "Trichomonas",
        "Radiation",
        "Severe Endocervical Cell Dysplasia"
    )
    var finalImpression = arrayOf(
        "Cervical Cnacer Detected",
        "HPV",
        "Normal Study",
        "PAP Smear Advised",
        "Regular Three Monthly Follow-Up"
    )
    var pathologicalReport = arrayOf(
        "Acute Cervicitis",
        "Adenoid Cystic Carcinoma",
        "Adenosquamous Carcinoma",
        "Cervical Malignant Meanoma",
        "Cervical Papilloma",
        "Cervical Polyp",
        "Cervical Sarcoma",
        "CIN-1",
        "CIN-2",
        "CIN-3",
        "HPV",
        "Invasive Squamous Carcinoma",
        "CMIcro Invasive Squamous Carcinoma (MIC)"
    )
    var Precautions = arrayOf(
        "Avoid Sexual Intercourse Within Two Weeks",
        "Come To Pick Up Pathology Report in One Week",
        "Remove Guage After 24 Hours & Keep Sanitary ",
        "Slight Bleeding May Occur In Vagina For 10 Days "
    )
    var Treatment = arrayOf(
        "Cervical Biopsy",
        "Conization",
        "ACryothalamectomy",
        "Endocervical Curettage(ECC)",
        "HPV Test",
        "Laser",
        "LEEP",
        "Radiation",
        "Radical Hysterectomy",
        "Repeat PAP 3-6 Months",
        "Routine Follow-Up"
    )
    var mylist = ArrayList<String>()
    var colpoReport = Global()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.colposcopy_report)
        toolbar.title="Colposcopy Report"

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
                createCheckBox(colposcopyFindings, "colposcopyFindings")
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
            val intent = Intent(this@ColposcopyReport, ReportSelection::class.java)
            startActivity(intent)
            System.out.println(Global.chiefComplaintList)
        })
        clearButton.setOnClickListener(View.OnClickListener {
            Global.chiefComplaintList.clear()
            mylist.clear()
            Global.colposcopyFindingsList.clear()
            Global.cytologyReportList.clear()
            Global.finalImpressionList.clear()
            Global.pathologicalReportList.clear()
            Global.precautionsList.clear()
            Global.treatmentList.clear()
            finish()
            startActivity(intent)
        })
        exitButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ColposcopyReport, ReportSelection::class.java)
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
            if (mylist.contains(arrList[i]) || Global.colposcopyFindingsList.contains(arrList[i])
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
            checkBox.setOnCheckedChangeListener { compoundButton, b ->
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
                    if (name === "colposcopyFindings") {
                        Global.colposcopyFindingsList += checkBox.text.toString()
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
                    if (name === "colposcopyFindings") {
                        Global.colposcopyFindingsList -= checkBox.text.toString()
                    }
                    if (name === "chiefComplaint") {
                        Global.chiefComplaintList -= checkBox.text.toString()
                    }
                }
            }
        }
    }


}