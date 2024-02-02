package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.swede_score.clearButton
import kotlinx.android.synthetic.main.swede_score.exitButton
import kotlinx.android.synthetic.main.swede_score.listViewLayout
import kotlinx.android.synthetic.main.swede_score.reports
import kotlinx.android.synthetic.main.swede_score.saveButton
import kotlinx.android.synthetic.main.swede_score.score
import kotlinx.android.synthetic.main.swede_score.scoreResult
import kotlinx.android.synthetic.main.swede_score.toolbar


class SwedeScore : AppCompatActivity(){
    var report = arrayOf("Aceto uptake", "Margins", "Vessels", "Lession Size", "Iodine Staining")
    var acetoUpTake = arrayOf(
        "Zero or Transparent", "Shady, Milky(not transparent, not opaque",
        "Distinct, opaque white"
    )
    var margins = arrayOf(
        "Diffuse", "Sharp but irregular, jagged, geographical satelites",
        "Sharp and even, difference in surface level, including cuffing"
    )
    var vessels = arrayOf(
        "<5mm", "5-15mm or 2 quadrants",
        ">15mm or 3-4 quadrants/endocervically undefined"
    )

    var lessionSize = arrayOf(
        "Fine, regular", "Absent",
        "Coarse or atypical"
    )
    var iodineStaining = arrayOf(
        "Brown", "Faintly or patchy yellow",
        "Distinct yellow"
    )

    var mylist = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.swede_score)
        toolbar.title="Colposcopy Report"


        if (Global.swedeScore != null) {
            scoreResult.setText(Global.swedeScoreResult)
            score.setText("Reid Score" + Global.swedeSum)
            mylist.add(Global.swedeMarginsList)
            mylist.add(Global.swedeLessionSizeList)
            mylist.add(Global.swedeAcetoUptakeList)
            mylist.add(Global.swedeIodineStainingList)
            mylist.add(Global.swedeVesselsList)
        }

        val arr: ArrayAdapter<String>
        arr = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            report
        )
        reports.setAdapter(arr)

        reports.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                listViewLayout.removeAllViews()
                createRadioButton(acetoUpTake, "val1")
            }
            if (position == 1) {
                listViewLayout.removeAllViews()
                createRadioButton(margins, "val2")
            }
            if (position == 2) {
                listViewLayout.removeAllViews()
                createRadioButton(vessels, "val3")
            }
            if (position == 3) {
                listViewLayout.removeAllViews()
                createRadioButton(lessionSize, "val4")
            }
            if (position == 4) {
                listViewLayout.removeAllViews()
                createRadioButton(iodineStaining, "val5")
            }
        })

        saveButton.setOnClickListener(View.OnClickListener {
            Global.swedeScore = java.lang.String.valueOf(Global.swedeSum)
            val intent = Intent(this@SwedeScore, ReportSelection::class.java)
            startActivity(intent)
        })

        exitButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@SwedeScore, ReportSelection::class.java)
            startActivity(intent)
        })

        clearButton.setOnClickListener(View.OnClickListener {
            Global.swedeAcetoUptakeList = ""
            Global.swedeMarginsList = ""
            Global.swedeVesselsList = ""
            Global.swedeLessionSizeList = ""
            Global.swedeIodineStainingList = ""
            Global.swedeSum = 0
            Global.swedeScore = ""
            finish()
            startActivity(intent)
        })
    }
    private fun createRadioButton(arrList: Array<String>, `val`: String) {
        val rb = arrayOfNulls<RadioButton>(arrList.size)
        val rg = RadioGroup(this) //create the RadioGroup
        rg.orientation = RadioGroup.VERTICAL //or RadioGroup.VERTICAL
        for (i in arrList.indices) {
            rb[i] = RadioButton(listViewLayout.getContext())
            rb[i]!!.text = arrList[i]
            rb[i]!!.id = i
            if (mylist.contains(arrList[i])) {
//                   rg.check(i);
                rb[i]!!.isChecked = true
            }
            rg.addView(rb[i])
        }
        listViewLayout.addView(rg) //you add the whole RadioGroup to the layout
        rg.setOnCheckedChangeListener { radioGroup, i ->
            if (`val` === "val1") {
                Global.swedeVal1 = i
                getResult(i, `val`)
            } else if (`val` === "val2") {
                Global.swedeVal2 = i
                getResult(i, `val`)
            } else if (`val` === "val3") {
                Global.swedeVal3 = i
                getResult(i, `val`)
            } else if (`val` === "val4") {
                Global.swedeVal4 = i
                getResult(i, `val`)
            } else if (`val` === "val5") {
                Global.swedeVal5 = i
                getResult(i, `val`)
            }
        }
    }


    fun getResult(i: Int, `val`: String) {
        val rb = findViewById<View>(i) as RadioButton
        if (`val` === "val1") {
            if (mylist.contains("Shady, Milky(not transparent, not opaque")) {
                mylist.remove("Shady, Milky(not transparent, not opaque")
            }
            if (mylist.contains("Distinct, opaque white")) {
                mylist.remove("Distinct, opaque white")
            }
            if (mylist.contains("Zero or Transparent")) {
                mylist.remove("Zero or Transparent")
            }
            if (!mylist.contains(rb.text.toString())) {
                mylist.add(rb.text.toString())
            }
            Global.swedeAcetoUptakeList = rb.text.toString()
        } else if (`val` === "val2") {
            if (mylist.contains("Diffuse")) {
                mylist.remove("Diffuse")
            }
            if (mylist.contains("Sharp but irregular, jagged, geographical satelites")) {
                mylist.remove("Sharp but irregular, jagged, geographical satelites")
            }
            if (mylist.contains("Sharp and even, difference in surface level, including cuffing")) {
                mylist.remove("Sharp and even, difference in surface level, including cuffing")
            }
            if (!mylist.contains(rb.text.toString())) {
                mylist.add(rb.text.toString())
            }
            Global.swedeMarginsList = rb.text.toString()
        } else if (`val` === "val3") {
            if (mylist.contains("<5mm")) {
                mylist.remove("<5mm")
            }
            if (mylist.contains("5-15mm or 2 quadrants")) {
                mylist.remove("5-15mm or 2 quadrants")
            }
            if (mylist.contains(">15mm or 3-4 quadrants/endocervically undefined")) {
                mylist.remove(">15mm or 3-4 quadrants/endocervically undefined")
            }
            if (!mylist.contains(rb.text.toString())) {
                mylist.add(rb.text.toString())
            }
            Global.swedeVesselsList = rb.text.toString()
        } else if (`val` === "val4") {
            if (mylist.contains("Fine, regular")) {
                mylist.remove("Fine, regular")
            }
            if (mylist.contains("Absent")) {
                mylist.remove("Absent")
            }
            if (mylist.contains("Coarse or atypical")) {
                mylist.remove("Coarse or atypical")
            }
            if (!mylist.contains(rb.text.toString())) {
                mylist.add(rb.text.toString())
            }
            Global.swedeLessionSizeList = rb.text.toString()
        } else if (`val` === "val5") {
            if (mylist.contains("Brown")) {
                mylist.remove("Brown")
            }
            if (mylist.contains("Faintly or patchy yellow")) {
                mylist.remove("Faintly or patchy yellow")
            }
            if (mylist.contains("Distinct yellow")) {
                mylist.remove("Distinct yellow")
            }
            if (!mylist.contains(rb.text.toString())) {
                mylist.add(rb.text.toString())
            }
            Global.swedeIodineStainingList = rb.text.toString()
        }
        Global.swedeSum =
            Global.swedeVal1 + Global.swedeVal2 + Global.swedeVal3 + Global.swedeVal4 + Global.swedeVal5
        score.text = "Swede Score = " + Global.swedeSum
        if (Global.swedeSum >= 0 && Global.swedeSum <= 4) {
            Global.swedeScoreResult = "Low Grade/normal or CIN 1"
            scoreResult.setText(Global.swedeScoreResult)
        } else if (Global.swedeSum >= 5 && Global.swedeSum <= 6) {
            Global.swedeScoreResult = "High grade/non-invasive cancer or CIN 2+"
            scoreResult.setText(Global.swedeScoreResult)
        } else if (Global.swedeSum >= 7 && Global.swedeSum <= 10) {
            Global.swedeScoreResult = "High grade/suspected invaise cancer CIN 2+"
            scoreResult.setText(Global.swedeScoreResult)
        }
    }
}