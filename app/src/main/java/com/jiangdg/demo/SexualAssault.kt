package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.sexual_assault.clearButton
import kotlinx.android.synthetic.main.sexual_assault.exitButton
import kotlinx.android.synthetic.main.sexual_assault.listViewLayout
import kotlinx.android.synthetic.main.sexual_assault.reports
import kotlinx.android.synthetic.main.sexual_assault.saveButton
import kotlinx.android.synthetic.main.sexual_assault.scrollView
import kotlinx.android.synthetic.main.sexual_assault.toolbar

class SexualAssault : AppCompatActivity() {
    var report = arrayOf(
        "Anal Injuries or injury to anal folds", "Erythema", "Green Filter ",
        "Posterior Fourchette", "Presence of Foreign bodies", "Skin or Mucosal Trauma",
        "Staining Effect", "Swab Sample"
    )
    var analInjuriesorinjurytoanalfolds = arrayOf("Yes", "No")

    var erythema = arrayOf("Cervix", "Hymen", "Labia", "Minoro")

    var greenFilter = arrayOf("Visualisation of abnormal vessels pattern", "Visualisation of Scar")

    var posteriorFourchette = arrayOf("Yes", "No")

    var presenceOfForeignBodies = arrayOf("Blood", "Fibers", "Hairs", "Salina", "Semen")

    var SkinOrMucosalTrauma = arrayOf(
        "Abrasions", "Bruising", "Focal Edema", "Hymenal Tears",
        "Lacerations", "Petechiae", "Swelling of Hymen", "Tears"
    )

    var stainingEffect = arrayOf(
        "Appearance of scar and other abnormality",
        "Non Appearance of scar and other abnormality"
    )

    var swabSample = arrayOf("Cervix", "Vagina", "Vulva")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sexual_assault)
        toolbar.title="Sexual Assault"

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
                    analInjuriesorinjurytoanalfolds,
                    "analInjuriesorinjurytoanalfolds"
                )
            }
            if (i == 1) {
                makeVisible(view)
                createCheckBox(
                    erythema,
                    "erythema"
                )
            }
            if (i == 2) {
                makeVisible(view)
                createCheckBox(
                    greenFilter,
                    "greenFilter"
                )
            }
            if (i == 3) {
                makeVisible(view)
                createCheckBox(
                    posteriorFourchette,
                    "posteriorFourchette"
                )
            }
            if (i == 4) {
                makeVisible(view)
                createCheckBox(
                    presenceOfForeignBodies,
                    "presenceOfForeignBodies"
                )
            }
            if (i == 5) {
                makeVisible(view)
                createCheckBox(
                    SkinOrMucosalTrauma,
                    "SkinOrMucosalTrauma"
                )
            }
            if (i == 6) {
                makeVisible(view)
                createCheckBox(
                    stainingEffect,
                    "stainingEffect"
                )
            }
            if (i == 7) {
                makeVisible(view)
                createCheckBox(
                    swabSample,
                    "swabSample"
                )
            }
        })

        saveButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@SexualAssault, ReportSelection::class.java)
            startActivity(intent)
        })
        clearButton.setOnClickListener(View.OnClickListener {
            Global.analInjuriesorinjurytoanalfoldsList.clear()
            Global.erythemaList.clear()
            Global.greenFilterList.clear()
            Global.posteriorFourchetteList.clear()
            Global.presenceOfForeignBodiesList.clear()
            Global.skinOrMucosalTraumaList.clear()
            Global.stainingEffectList.clear()
            Global.swabSampleList.clear()
            finish()
            startActivity(intent)
        })
        exitButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@SexualAssault, ReportSelection::class.java)
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
            if (name === "analInjuriesorinjurytoanalfolds") {
                if (Global.analInjuriesorinjurytoanalfoldsList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "erythema") {
                if (Global.erythemaList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "greenFilter") {
                if (Global.greenFilterList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "posteriorFourchette") {
                if (Global.posteriorFourchetteList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "presenceOfForeignBodies") {
                if (Global.presenceOfForeignBodiesList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "SkinOrMucosalTrauma") {
                if (Global.skinOrMucosalTraumaList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "stainingEffect") {
                if (Global.stainingEffectList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "swabSample") {
                if (Global.swabSampleList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            checkBox.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    if (name === "analInjuriesorinjurytoanalfolds") {
                        Global.analInjuriesorinjurytoanalfoldsList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "erythema") {
                        Global.erythemaList.add(checkBox.text.toString())
                    }
                    if (name === "greenFilter") {
                        Global.greenFilterList.add(checkBox.text.toString())
                    }
                    if (name === "posteriorFourchette") {
                        Global.posteriorFourchetteList.add(checkBox.text.toString())
                    }
                    if (name === "presenceOfForeignBodies") {
                        Global.presenceOfForeignBodiesList.add(checkBox.text.toString())
                    }
                    if (name === "SkinOrMucosalTrauma") {
                        Global.skinOrMucosalTraumaList.add(checkBox.text.toString())
                    }
                    if (name === "stainingEffect") {
                        Global.stainingEffectList.add(checkBox.text.toString())
                    }
                    if (name === "swabSample") {
                        Global.swabSampleList.add(checkBox.text.toString())
                    }
                } else {
                    if (name === "analInjuriesorinjurytoanalfolds") {
                        Global.analInjuriesorinjurytoanalfoldsList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "erythema") {
                        Global.erythemaList.remove(checkBox.text.toString())
                    }
                    if (name === "greenFilter") {
                        Global.greenFilterList.remove(checkBox.text.toString())
                    }
                    if (name === "posteriorFourchette") {
                        Global.posteriorFourchetteList.remove(checkBox.text.toString())
                    }
                    if (name === "presenceOfForeignBodies") {
                        Global.presenceOfForeignBodiesList.remove(checkBox.text.toString())
                    }
                    if (name === "SkinOrMucosalTrauma") {
                        Global.skinOrMucosalTraumaList.remove(checkBox.text.toString())
                    }
                    if (name === "stainingEffect") {
                        Global.stainingEffectList.remove(checkBox.text.toString())
                    }
                    if (name === "swabSample") {
                        Global.swabSampleList.remove(checkBox.text.toString())
                    }
                }
            }
        }
    }
}