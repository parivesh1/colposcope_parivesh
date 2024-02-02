package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.leep.clearButton
import kotlinx.android.synthetic.main.leep.exitButton
import kotlinx.android.synthetic.main.leep.listViewLayout
import kotlinx.android.synthetic.main.leep.reports
import kotlinx.android.synthetic.main.leep.saveButton
import kotlinx.android.synthetic.main.leep.scrollView
import kotlinx.android.synthetic.main.leep.textField
import kotlinx.android.synthetic.main.leep.toolbar

class LeepReport: AppCompatActivity() {
    var report = arrayOf(
        "Anaesthesia Mode", "Operation mode", "Bisthoury Specification",
        "Operation time", "Operation Deepness", "Days After Operation", "Operation range",
        "Bleeding While Operation", "Diagnosis Before Operation",
        "Pathology Result After Operation"
    )
    var anaesthesiaMode = arrayOf("No", "Local Anaesthesia", "Intravenous  Anaesthesia", "Others")
    var operationMode =
        arrayOf("Single Pass", "Multi Pass", "Cowboy Final", "Multi Pass + Cowboy Final")
    var BisthourySpecification = arrayOf(
        "Large Loop (2.0*1.5)", "Large Loop (2.0*1.2)",
        "Large Loop (2.0*0.8)", "Medium Loop (1.5*1.0)", "Medium Loop (1.2*0.8)",
        "Small Loop (1.0*1.0)", "Small Loop(1.2*0.8)", "Triangle Loop(2.0*1.5)",
        "Triangle Loop(2.0*1.0)", "Triangle Loop(1.0*0.8)"
    )
    var operationTime = arrayOf("", "")
    var operationDeepness = arrayOf("", "")
    var daysAfterOperation = arrayOf("", "")
    var operationRange = arrayOf("", "")
    var bleedingWhileOperation = arrayOf("", "")
    var pathologyResultAfterOperation = arrayOf("", "")
    var diagnosisBeforeOperation = arrayOf("", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leep)
        toolbar.title = "Leep Report"

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
                    anaesthesiaMode,
                    "anaesthesiaMode"
                )
            }
            if (i == 1) {
                makeVisible(view)
                createCheckBox(
                    operationMode,
                    "operationMode"
                )
            }
            if (i == 2) {
                makeVisible(view)
                createCheckBox(
                    BisthourySpecification,
                    "BisthourySpecification"
                )
            }
            if (i == 3) {
                makeVisible(view)
                createCheckBox(
                    operationTime,
                    "operationTime"
                )
            }
            if (i == 4) {
                makeVisible(view)
                createCheckBox(
                    operationDeepness,
                    "operationDeepness"
                )
            }
            if (i == 5) {
                makeVisible(view)
                createCheckBox(
                    daysAfterOperation,
                    "daysAfterOperation"
                )
            }
            if (i == 6) {
                makeVisible(view)
                createCheckBox(
                    operationRange,
                    "operationRange"
                )
            }
            if (i == 7) {
                makeVisible(view)
                createCheckBox(
                    bleedingWhileOperation,
                    "bleedingWhileOperation"
                )
            }
            if (i == 8) {
                //                    makeVisible(view);
                textField.visibility = View.VISIBLE
                scrollView.setVisibility(View.GONE)
                textField.setText("")
                if (Global.diagnosisBeforeOperationList != null) {
                    textField.setText(Global.diagnosisBeforeOperationList)
                }
                textField.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                        println("beforeTextChanged $charSequence")
                    }

                    override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                        println("onTextChanged $charSequence")
                    }

                    override fun afterTextChanged(editable: Editable) {
                        Global.diagnosisBeforeOperationList = editable.toString()
                        println("afterTextChanged $editable")
                    }
                })
            }
            if (i == 9) {
                //                    makeVisible(view);
                textField.visibility = View.VISIBLE
                scrollView.setVisibility(View.GONE)
                textField.setText("")
                if (Global.pathologyResultAfterOperationList != null) {
                    textField.setText(Global.pathologyResultAfterOperationList)
                }
                textField.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    override fun afterTextChanged(editable: Editable) {
                        Global.pathologyResultAfterOperationList = editable.toString()
                    }
                })
            }
        })

        saveButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LeepReport, ReportSelection::class.java)
            startActivity(intent)
        })
        exitButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LeepReport, ReportSelection::class.java)
            startActivity(intent)
        })
        clearButton.setOnClickListener(View.OnClickListener {
            Global.anaesthesiaModeList.clear()
            Global.operationModeList.clear()
            Global.bisthourySpecificationList.clear()
            Global.operationTimeList.clear()
            Global.operationDeepnessList.clear()
            Global.daysAfterOperationList.clear()
            Global.operationRangeList.clear()
            Global.pathologyResultAfterOperationList = ""
            Global.bleedingWhileOperationList.clear()
            Global.diagnosisBeforeOperationList = ""
            finish()
            startActivity(intent)
        })
    }

    private fun makeVisible(view: View) {
        listViewLayout.removeAllViews()
        scrollView.setVisibility(View.VISIBLE)
        textField.setVisibility(View.GONE)
    }

    private fun createCheckBox(arrList: Array<String>, name: String) {
        if (name == "anaesthesiaMode" || name == "operationMode" || name == "BisthourySpecification") {
            for (i in arrList.indices) {
                val checkBox = CheckBox(listViewLayout.getContext())
                checkBox.text = arrList[i]
                listViewLayout.addView(checkBox)
                if (name == "anaesthesiaMode") {
                    if (Global.anaesthesiaModeList.contains(arrList[i])) {
                        checkBox.isChecked = true
                    }
                }
                if (name == "operationMode") {
                    if (Global.operationModeList.contains(arrList[i])) {
                        checkBox.isChecked = true
                    }
                }
                if (name == "BisthourySpecification") {
                    if (Global.bisthourySpecificationList.contains(arrList[i])) {
                        checkBox.isChecked = true
                    }
                }
                checkBox.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        if (name == "anaesthesiaMode") {
                            Global.anaesthesiaModeList.add(
                                checkBox.text
                                    .toString()
                            )
                        }
                        if (name == "operationMode") {
                            Global.operationModeList.add(
                                checkBox.text
                                    .toString()
                            )
                        }
                        if (name == "BisthourySpecification") {
                            Global.bisthourySpecificationList.add(
                                checkBox.text
                                    .toString()
                            )
                        }
                    } else {
                        if (name == "anaesthesiaMode") {
                            Global.anaesthesiaModeList.remove(
                                checkBox.text
                                    .toString()
                            )
                        }
                        if (name == "operationMode") {
                            Global.operationModeList.remove(
                                checkBox.text
                                    .toString()
                            )
                        }
                        if (name == "BisthourySpecificationList") {
                            Global.bisthourySpecificationList.remove(
                                checkBox.text
                                    .toString()
                            )
                        }
                    }
                }
            }
        }
        if (name == "operationTime") {
            for (i in 0..59) {
                val checkBox = CheckBox(listViewLayout.getContext())
                checkBox.text = (i + 1).toString()
                listViewLayout.addView(checkBox)
                if (Global.operationTimeList.contains((i + 1).toString())) {
                    checkBox.isChecked = true
                }
                checkBox.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        Global.operationTimeList.add(
                            checkBox.text
                                .toString()
                        )
                    } else {
                        Global.operationTimeList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                }
            }
        }
        if (name == "bleedingWhileOperation") {
            for (i in 0..89) {
                val checkBox = CheckBox(listViewLayout.getContext())
                checkBox.text = (i + 1).toString()
                listViewLayout.addView(checkBox)
                if (Global.bleedingWhileOperationList.contains((i + 1).toString())) {
                    checkBox.isChecked = true
                }
                checkBox.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        Global.bleedingWhileOperationList.add(
                            checkBox.text
                                .toString()
                        )
                    } else {
                        Global.bleedingWhileOperationList.remove(
                            checkBox.text
                                .toString()
                        )
                    }
                }
            }
        }
        if (name == "operationDeepness" || name == "operationRange" || name == "daysAfterOperation") {
            for (i in 0..89) {
                val checkBox = CheckBox(listViewLayout.getContext())
                checkBox.text = (i + 1).toString()
                listViewLayout.addView(checkBox)
                if (name == "operationDeepness") {
                    if (Global.operationDeepnessList.contains((i + 1).toString())) {
                        checkBox.isChecked = true
                    }
                }
                if (name == "operationRange") {
                    if (Global.operationRangeList.contains((i + 1).toString())) {
                        checkBox.isChecked = true
                    }
                }
                if (name == "daysAfterOperation") {
                    if (Global.daysAfterOperationList.contains((i + 1).toString())) {
                        checkBox.isChecked = true
                    }
                }
                checkBox.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        if (name == "operationDeepness") {
                            Global.operationDeepnessList.add(
                                checkBox.text
                                    .toString()
                            )
                        }
                        if (name == "operationRange") {
                            Global.operationRangeList.add(
                                checkBox.text
                                    .toString()
                            )
                        }
                        if (name == "daysAfterOperation") {
                            Global.daysAfterOperationList.add(
                                checkBox.text
                                    .toString()
                            )
                        }
                    } else {
                        if (name == "operationDeepness") {
                            Global.operationDeepnessList.remove(
                                checkBox.text
                                    .toString()
                            )
                        }
                        if (name == "operationRange") {
                            Global.operationRangeList.remove(
                                checkBox.text
                                    .toString()
                            )
                        }
                        if (name == "daysAfterOperation") {
                            Global.daysAfterOperationList.remove(
                                checkBox.text
                                    .toString()
                            )
                        }
                    }
                }
            }
        }
    }
}