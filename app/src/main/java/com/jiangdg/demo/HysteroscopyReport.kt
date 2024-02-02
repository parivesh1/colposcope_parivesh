package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.hysteroscopy.clearButton
import kotlinx.android.synthetic.main.hysteroscopy.exitButton
import kotlinx.android.synthetic.main.hysteroscopy.listViewLayout
import kotlinx.android.synthetic.main.hysteroscopy.reports
import kotlinx.android.synthetic.main.hysteroscopy.saveButton
import kotlinx.android.synthetic.main.hysteroscopy.scrollView
import kotlinx.android.synthetic.main.hysteroscopy.toolbar

class HysteroscopyReport: AppCompatActivity() {
    var report = arrayOf(
        "Cavum Uteri", "Cervical Canal Bleeding", "Cervical Stenosis ",
        "Craurosis Fornicis", "Endometrium", "Fibroid Addition", "Focal Lesions",
        "Hyperplasia", "Intra Cervical Addition", "Intra Uterine Adhesion", "Isthmus Uteri",
        "IUD", "Left Tubal Ostia", "Lesions", "Myoma", "Nullipara", "Polyps", "Right Tubal Ostia",
        "Uterus"
    )
    var cavumUteri = arrayOf("Yes", "No")
    var cervicalCanalBleeding = arrayOf("Yes", "No")
    var cervicalStenosis = arrayOf("Yes", "No")
    var craurosisFornicis = arrayOf("Yes", "No")
    var Endometrium =
        arrayOf("Atrophic", "Hyper Plastic", "Normal", "Proliferative Phase", "Small Cystic")
    var fibroidAddition = arrayOf("Yes", "No")
    var focalLesions = arrayOf("Yes", "No")
    var Hyperplasia = arrayOf("Focal", "Partially Persistent", "Persistent", "Progressive")
    var intraCervicalAddition = arrayOf("Grade-I", "Grade-II", "Grade-III", "Grade-IV")
    var intraUterineAdhesion = arrayOf(
        "Grade-I", "Grade-II", "Grade-III", "Grade-IV", "Grade-II A",
        "Grade-III A", "Grade-III B"
    )
    var isthmusUteri = arrayOf("Yes", "No")
    var iUD = arrayOf("Yes", "No")
    var leftTubalOstia = arrayOf("Yes", "No")
    var lesions = arrayOf("Yes", "No")
    var myoma = arrayOf("Intracavity", "Intramural", "Mucous", "Submucous")
    var nullipara = arrayOf("Yes", "No")
    var polyps = arrayOf("Corpus", "Cystic Corpus", "Fibrotic", "Matron")
    var rightTubalOstia = arrayOf("Yes", "No")
    var uterus = arrayOf("Arcuatus", "Bicornis", "Hypo Plastic", "Septus")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hysteroscopy)
        toolbar.title="Hysteroscopy Report"

        val arr: ArrayAdapter<String>
        arr = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            report
        )
        reports.setAdapter(arr)

        reports.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            if (i == 0) {
                makeVisible(view)
                createCheckBox(
                    cavumUteri,
                    "cavumUteri"
                )
            }
            if (i == 1) {
                makeVisible(view)
                createCheckBox(
                    cervicalCanalBleeding,
                    "cervicalCanalBleeding"
                )
            }
            if (i == 2) {
                makeVisible(view)
                createCheckBox(
                    cervicalStenosis,
                    "cervicalStenosis"
                )
            }
            if (i == 3) {
                makeVisible(view)
                createCheckBox(
                    craurosisFornicis,
                    "craurosisFornicis"
                )
            }
            if (i == 4) {
                makeVisible(view)
                createCheckBox(
                    Endometrium,
                    "Endometrium"
                )
            }
            if (i == 5) {
                makeVisible(view)
                createCheckBox(
                    fibroidAddition,
                    "fibroidAddition"
                )
            }
            if (i == 6) {
                makeVisible(view)
                createCheckBox(
                    focalLesions,
                    "swabSample"
                )
            }
            if (i == 7) {
                makeVisible(view)
                createCheckBox(
                    Hyperplasia,
                    "Hyperplasia"
                )
            }
            if (i == 8) {
                makeVisible(view)
                createCheckBox(
                    intraCervicalAddition,
                    "intraCervicalAddition"
                )
            }
            if (i == 9) {
                makeVisible(view)
                createCheckBox(
                    intraUterineAdhesion,
                    "intraUterineAdhesion"
                )
            }
            if (i == 10) {
                makeVisible(view)
                createCheckBox(
                    isthmusUteri,
                    "isthmusUteri"
                )
            }
            if (i == 11) {
                makeVisible(view)
                createCheckBox(
                    iUD,
                    "iUD"
                )
            }
            if (i == 12) {
                makeVisible(view)
                createCheckBox(
                    leftTubalOstia,
                    "leftTubalOstia"
                )
            }
            if (i == 13) {
                makeVisible(view)
                createCheckBox(
                    lesions,
                    "lesions"
                )
            }
            if (i == 14) {
                makeVisible(view)
                createCheckBox(
                    myoma,
                    "myoma"
                )
            }
            if (i == 15) {
                makeVisible(view)
                createCheckBox(
                    nullipara,
                    "nullipara"
                )
            }
            if (i == 16) {
                makeVisible(view)
                createCheckBox(
                    polyps,
                    "polyps"
                )
            }
            if (i == 17) {
                makeVisible(view)
                createCheckBox(
                    rightTubalOstia,
                    "rightTubalOstia"
                )
            }
            if (i == 18) {
                makeVisible(view)
                createCheckBox(
                    uterus,
                    "uterus"
                )
            }
        })
        saveButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@HysteroscopyReport, ReportSelection::class.java)
            startActivity(intent)
        })
        clearButton.setOnClickListener(View.OnClickListener {
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
            finish()
            startActivity(intent)
        })
        exitButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@HysteroscopyReport, ReportSelection::class.java)
            startActivity(intent)
        })
    }
    private fun makeVisible(view: View) {
        listViewLayout.removeAllViews()
        scrollView.setVisibility(View.VISIBLE)
    }

    private fun createCheckBox(arrList: Array<String>, name: String) {
        for (i in arrList.indices) {
            val checkBox = CheckBox(listViewLayout.getContext())
            checkBox.text = arrList[i]
            listViewLayout.addView(checkBox)
            if (name === "uterus") {
                if (Global.uterusList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "rightTubalOstia") {
                if (Global.rightTubalOstiaList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "polyps") {
                if (Global.polypsList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "nullipara") {
                if (Global.nulliparaList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "myoma") {
                if (Global.myomaList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "lesions") {
                if (Global.lesionsList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "leftTubalOstia") {
                if (Global.leftTubalOstiaList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "iUD") {
                if (Global.iUDList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "isthmusUteri") {
                if (Global.isthmusUteriList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "intraUterineAdhesion") {
                if (Global.intraUterineAdhesionList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "intraCervicalAddition") {
                if (Global.intraCervicalAdditionList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "Hyperplasia") {
                if (Global.hyperplasiaList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "focalLesions") {
                if (Global.focalLesionsList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "fibroidAddition") {
                if (Global.fibroidAdditionList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "Endometrium") {
                if (Global.endometriumList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "craurosisFornicis") {
                if (Global.craurosisFornicisList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "cervicalStenosis") {
                if (Global.cervicalStenosisList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "cervicalCanalBleeding") {
                if (Global.cervicalCanalBleedingList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "cavumUteri") {
                if (Global.cavumUteriList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            checkBox.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    if (name === "uterus") {
                        Global.uterusList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "rightTubalOstia") {
                        Global.rightTubalOstiaList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "polyps") {
                        Global.polypsList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "nullipara") {
                        Global.nulliparaList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "myoma") {
                        Global.myomaList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "lesions") {
                        Global.lesionsList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "leftTubalOstia") {
                        Global.leftTubalOstiaList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "iUD") {
                        Global.iUDList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "isthmusUteri") {
                        Global.isthmusUteriList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "intraUterineAdhesion") {
                        Global.intraUterineAdhesionList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "intraCervicalAddition") {
                        Global.intraCervicalAdditionList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "Hyperplasia") {
                        Global.hyperplasiaList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "focalLesions") {
                        Global.focalLesionsList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "fibroidAddition") {
                        Global.fibroidAdditionList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "Endometrium") {
                        Global.endometriumList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "craurosisFornicis") {
                        Global.craurosisFornicisList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "cervicalStenosis") {
                        Global.cervicalStenosisList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "cervicalCanalBleeding") {
                        Global.cervicalCanalBleedingList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "cavumUteri") {
                        Global.cavumUteriList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                } else {
                    if (name === "uterus") {
                        Global.uterusList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "rightTubalOstia") {
                        Global.rightTubalOstiaList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "polyps") {
                        Global.polypsList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "nullipara") {
                        Global.nulliparaList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "myoma") {
                        Global.myomaList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "lesions") {
                        Global.lesionsList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "leftTubalOstia") {
                        Global.leftTubalOstiaList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "iUD") {
                        Global.iUDList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "isthmusUteri") {
                        Global.isthmusUteriList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "intraUterineAdhesion") {
                        Global.intraUterineAdhesionList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "intraCervicalAddition") {
                        Global.intraCervicalAdditionList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "Hyperplasia") {
                        Global.hyperplasiaList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "focalLesions") {
                        Global.focalLesionsList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "fibroidAddition") {
                        Global.fibroidAdditionList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "Endometrium") {
                        Global.endometriumList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "craurosisFornicis") {
                        Global.craurosisFornicisList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "cervicalStenosis") {
                        Global.cervicalStenosisList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "cervicalCanalBleeding") {
                        Global.cervicalCanalBleedingList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "cavumUteri") {
                        Global.cavumUteriList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                }
            }
        }
    }
}