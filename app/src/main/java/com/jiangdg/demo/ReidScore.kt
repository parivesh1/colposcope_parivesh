package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.reid_score.exitButton
import kotlinx.android.synthetic.main.reid_score.listViewLayout
import kotlinx.android.synthetic.main.reid_score.reports
import kotlinx.android.synthetic.main.reid_score.saveButton
import kotlinx.android.synthetic.main.reid_score.score
import kotlinx.android.synthetic.main.reid_score.scoreResult
import kotlinx.android.synthetic.main.reid_score.toolbar

class ReidScore : AppCompatActivity(){


    var report = arrayOf("Margins", "Color", "Vessels", "Iodine Staining")
    var margins = arrayOf(
        "Condylomatous or micropapillary contour, indistinct borders.Flocculated or Feathered margins angular, geographic lesions.Satellite lesions, acetowhite lesions outside the transformation zone",
        "Regular Lessions with smooth, straight outliners, shape peripheral margin",
        "Rolled peeling edges, internal borders between lesions of dirently severity"
    )
    var vessels = arrayOf(
        "Fine punctuation or fine mosaic.Uniform,fine caliber,nondilated capillary loops.Narrow intercapillary distance ",
        "Absence of surface vessels following acetic acid aaplication",
        "Coarse punctuation or coarse mosaic. Individual vessels dilated.Wide intercapillary distance"
    )
    var iodineStaining = arrayOf(
        "Positive iodine uptake,producing a mahogany brown color. Negative iodine uptake(mustard yellow) of a lesion recognized as.Low grade by preliminary score(<=2)",
        "Negative iodine uptake(mustard yellow) of a lesion considered high.Grade by preliminary score (>=3)",
        "Partial iodine uptake. Variegated, tortoise-shell appearance"
    )
    var color = arrayOf(
        "Shiny, snow white transient, indistinct acetowhite, semitransparent",
        "Shiny, off-white.Intermediate white",
        "Rolled peeling edges, internal borders between lesions of dirently severity"
    )
    var mylist = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reid_score)
        toolbar.title="Reid Score"


        if (Global.reidScore != null) {
            scoreResult.setText(Global.reidScoreResult)
            score.setText("Reid Score" + Global.reidSum)
            mylist.add(Global.reidColorList)
            mylist.add(Global.reidMarginsList)
            mylist.add(Global.reidVesselsList)
            mylist.add(Global.reidIodineStainingList)
        }

        val arr: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            report
        )

        reports.setAdapter(arr)
        reports.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                listViewLayout.removeAllViews()
                createRadioButton(margins, "val1")
            }
            if (position == 1) {
                listViewLayout.removeAllViews()
                createRadioButton(color, "val2")
            }
            if (position == 2) {
                listViewLayout.removeAllViews()
                createRadioButton(vessels, "val3")
            }
            if (position == 3) {
                listViewLayout.removeAllViews()
                createRadioButton(iodineStaining, "val4")
            }
        })

        saveButton.setOnClickListener(View.OnClickListener {
            Global.reidScore = Global.reidSum
            val intent = Intent(this@ReidScore, ReportSelection::class.java)
            startActivity(intent)
        })
        exitButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ReidScore, ReportSelection::class.java)
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
                Global.reidVal1 = i
                getResult(i, `val`)
            } else if (`val` === "val2") {
                Global.reidVal2 = i
                getResult(i, `val`)
            } else if (`val` === "val3") {
                Global.reidVal3 = i
                getResult(i, `val`)
            } else if (`val` === "val4") {
                Global.reidVal4 = i
                getResult(i, `val`)
            }
        }
    }

    fun getResult(i: Int, `val`: String) {
        val rb = findViewById<View>(i) as RadioButton
        if (`val` === "val1") {
            if (mylist.contains("Condylomatous or micropapillary contour, indistinct borders.Flocculated or Feathered margins angular, geographic lesions.Satellite lesions, acetowhite lesions outside the transformation zone")) {
                mylist.remove("Condylomatous or micropapillary contour, indistinct borders.Flocculated or Feathered margins angular, geographic lesions.Satellite lesions, acetowhite lesions outside the transformation zone")
            }
            if (mylist.contains("Regular Lessions with smooth, straight outliners, shape peripheral margin")) {
                mylist.remove("Regular Lessions with smooth, straight outliners, shape peripheral margin")
            }
            if (mylist.contains("Rolled peeling edges, internal borders between lesions of dirently severity")) {
                mylist.remove("Rolled peeling edges, internal borders between lesions of dirently severity")
            }
            if (!mylist.contains(rb.text.toString())) {
                mylist.add(rb.text.toString())
            }
            Global.reidMarginsList = rb.text.toString()
        } else if (`val` === "val2") {
            if (mylist.contains("Shiny, snow white transient, indistinct acetowhite, semitransparent")) {
                mylist.remove("Shiny, snow white transient, indistinct acetowhite, semitransparent")
            }
            if (mylist.contains("Rolled peeling edges, internal borders between lesions of dirently severity")) {
                mylist.remove("Rolled peeling edges, internal borders between lesions of dirently severity")
            }
            if (mylist.contains("Shiny, off-white.Intermediate white")) {
                mylist.remove("Shiny, off-white.Intermediate white")
            }
            if (!mylist.contains(rb.text.toString())) {
                mylist.add(rb.text.toString())
            }
            Global.reidColorList = rb.text.toString()
        } else if (`val` === "val3") {
            if (mylist.contains("Fine punctuation or fine mosaic.Uniform,fine caliber,nondilated capillary loops.Narrow intercapillary distance ")) {
                mylist.remove("Fine punctuation or fine mosaic.Uniform,fine caliber,nondilated capillary loops.Narrow intercapillary distance ")
            }
            if (mylist.contains("Absence of surface vessels following acetic acid aaplication")) {
                mylist.remove("Absence of surface vessels following acetic acid aaplication")
            }
            if (mylist.contains("Coarse punctuation or coarse mosaic. Individual vessels dilated.Wide intercapillary distance")) {
                mylist.remove("Coarse punctuation or coarse mosaic. Individual vessels dilated.Wide intercapillary distance")
            }
            if (!mylist.contains(rb.text.toString())) {
                mylist.add(rb.text.toString())
            }
            Global.reidVesselsList = rb.text.toString()
        } else if (`val` === "val4") {
            if (mylist.contains("Positive iodine uptake,producing a mahogany brown color. Negative iodine uptake(mustard yellow) of a lesion recognized as.Low grade by preliminary score(<=2)")) {
                mylist.remove("Positive iodine uptake,producing a mahogany brown color. Negative iodine uptake(mustard yellow) of a lesion recognized as.Low grade by preliminary score(<=2)")
            }
            if (mylist.contains("Negative iodine uptake(mustard yellow) of a lesion considered high.Grade by preliminary score (>=3)")) {
                mylist.remove("Negative iodine uptake(mustard yellow) of a lesion considered high.Grade by preliminary score (>=3)")
            }
            if (mylist.contains("Partial iodine uptake. Variegated, tortoise-shell appearance")) {
                mylist.remove("Partial iodine uptake. Variegated, tortoise-shell appearance")
            }
            if (!mylist.contains(rb.text.toString())) {
                mylist.add(rb.text.toString())
            }
            Global.reidIodineStainingList = rb.text.toString()
        }
        Global.reidSum = Global.reidVal1 + Global.reidVal2 + Global.reidVal3 + Global.reidVal4
        score.text = "Reied Score = " + Global.reidSum
        if (Global.reidSum >= 0 && Global.reidSum <= 2) {
            Global.reidScoreResult = "Likely to be CIN I"
            scoreResult.setText(Global.reidScoreResult)
        } else if (Global.reidSum >= 3 && Global.reidSum <= 5) {
            Global.reidScoreResult = "Likely to be CIN I-II"
            scoreResult.setText(Global.reidScoreResult)
        } else if (Global.reidSum >= 6 && Global.reidSum <= 8) {
            Global.reidScoreResult = "Likely to be CIN II-III"
            scoreResult.setText(Global.reidScoreResult)
        }
    }
}