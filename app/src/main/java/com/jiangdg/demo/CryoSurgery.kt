package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.cryo_surgery.clearButton
import kotlinx.android.synthetic.main.cryo_surgery.exitButton
import kotlinx.android.synthetic.main.cryo_surgery.listViewLayout
import kotlinx.android.synthetic.main.cryo_surgery.reports
import kotlinx.android.synthetic.main.cryo_surgery.saveButton
import kotlinx.android.synthetic.main.cryo_surgery.scrollView
import kotlinx.android.synthetic.main.cryo_surgery.toolbar

class CryoSurgery: AppCompatActivity() {
    var report = arrayOf("Cryo Surgery", "Freezing done", "Ice Ball", "Probe Tip", "Thaw Cycle")
    var cryoSurgery = arrayOf("Carbon Dioxide", "Gas Used", "Nitrous Oxide")
    var freezingDone = arrayOf("Numerous Application", "Single Application")
    var iceBall = arrayOf("Conical", "Flat", "Others")
    var probeTip = arrayOf(
        "Extension to >/=5mm outside lesion-Yes",
        "Extension to >/=5mm outside lesion-No"
    )
    var thawCycle = arrayOf("Single Cycle", "Double Freeze")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cryo_surgery)
        toolbar.title="Cryo Surgery"

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
                    cryoSurgery,
                    "cryoSurgery"
                )
            }
            if (i == 1) {
                makeVisible(view)
                createCheckBox(
                    freezingDone,
                    "freezingDone"
                )
            }
            if (i == 2) {
                makeVisible(view)
                createCheckBox(
                    iceBall,
                    "iceBall"
                )
            }
            if (i == 3) {
                makeVisible(view)
                createCheckBox(
                    probeTip,
                    "probeTip"
                )
            }
            if (i == 4) {
                makeVisible(view)
                createCheckBox(
                    thawCycle,
                    "thawCycle"
                )
            }
        })
        saveButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@CryoSurgery, ReportSelection::class.java)
            startActivity(intent)
        })
        exitButton.setOnClickListener(View.OnClickListener {
            Global.cryoSurgeryList.clear()
            Global.freezingDoneList.clear()
            Global.iceBallList.clear()
            Global.probeTipList.clear()
            Global.thawCycleList.clear()
            finish()
            startActivity(intent)
        })

        clearButton.setOnClickListener(View.OnClickListener {
            Global.cryoSurgeryList.clear()
            Global.freezingDoneList.clear()
            Global.iceBallList.clear()
            Global.probeTipList.clear()
            Global.thawCycleList.clear()
            finish()
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
            if (name === "cryoSurgery") {
                if (Global.cryoSurgeryList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "freezingDone") {
                if (Global.freezingDoneList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "iceBall") {
                if (Global.iceBallList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "probeTip") {
                if (Global.probeTipList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            if (name === "thawCycle") {
                if (Global.thawCycleList.contains(arrList[i])) {
                    checkBox.isChecked = true
                }
            }
            checkBox.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    if (name === "cryoSurgery") {
                        Global.cryoSurgeryList.add(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "freezingDone") {
                        Global.freezingDoneList.add(checkBox.text.toString())
                    }
                    if (name === "iceBall") {
                        Global.iceBallList.add(checkBox.text.toString())
                    }
                    if (name === "probeTip") {
                        Global.probeTipList.add(checkBox.text.toString())
                    }
                    if (name === "thawCycle") {
                        Global.thawCycleList.add(checkBox.text.toString())
                    }
                } else {
                    if (name === "cryoSurgery") {
                        Global.cryoSurgeryList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                    if (name === "freezingDone") {
                        Global.freezingDoneList.remove(checkBox.text.toString())
                    }
                    if (name === "iceBall") {
                        Global.iceBallList.remove(checkBox.text.toString())
                    }
                    if (name === "probeTip") {
                        Global.probeTipList.remove(checkBox.text.toString())
                    }
                    if (name === "thawCycle") {
                        Global.thawCycleList.remove(checkBox.text.toString())
                    }
                }
            }
        }
    }
}