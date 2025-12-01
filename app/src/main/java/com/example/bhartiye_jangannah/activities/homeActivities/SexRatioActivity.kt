package com.example.bhartiye_jangannah.activities.homeActivities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.databinding.ActivitySexRatioBinding
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SexRatioActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySexRatioBinding
    private lateinit var dbRef: DatabaseReference
    var total:Int =0
    var males:Int =0
    var females:Int =0
    var others:Int =0
    var selectedItemState: String?= null

    private val colorClassArray = listOf<Int>(
        Color.LTGRAY,
        Color.BLUE,
        Color.RED,
        Color.CYAN,
        Color.DKGRAY,
        Color.GREEN,
        Color.MAGENTA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySexRatioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbRef= FirebaseDatabase.getInstance().getReference("Census Entry")
        getTotalPopulation()

        val adapterState: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.states_array,
            android.R.layout.simple_spinner_item
        )
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerState.adapter = adapterState

        binding.spinnerState.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        selectedItemState = parent?.getItemAtPosition(position).toString()
                        getPopulationDetails()
                    }
                    else{
                        getTotalPopulation()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    getTotalPopulation()
                }
            }

    }
    private fun getPopulationDetails(){
        dbRef.orderByChild("state").equalTo(selectedItemState).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                total= 0
                males= 0
                females= 0
                others= 0
                // Iterate through the dataSnapshot to get user data
                for (userSnapshot in dataSnapshot.children) {
                    total++
                    val entries = userSnapshot.getValue(CensusEntry::class.java)
                    if (entries != null) {
                        // Use user data to count males and females
                        countGender(entries.gender)
                    }
                }
                binding.population.text=total.toString()
                binding.populationMale.text=males.toString()
                binding.populationFemale.text=females.toString()
                binding.populationOthers.text=others.toString()

                val pieDataSet = PieDataSet(dataValues1(), "")
                binding.pieChart.setEntryLabelTextSize(16f)
                binding.pieChart.setHoleColor(Color.TRANSPARENT)
                pieDataSet.valueFormatter= PercentFormatter()
                binding.pieChart.setUsePercentValues(true)
                binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
                pieDataSet.valueTextSize = 10f
                pieDataSet.valueTextColor = Color.BLACK
                binding.pieChart.centerText="Sex Ratio"
                pieDataSet.colors = colorClassArray
                val pieData = PieData(pieDataSet)
                binding.pieChart.data = pieData
                binding.pieChart.invalidate()
//                drawGraph()
                println("total= {$total}, male= {$males}, female= {$females}, others= {$others}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
    private fun getTotalPopulation(){
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                total= 0
                males= 0
                females= 0
                others= 0
                // Iterate through the dataSnapshot to get user data
                for (userSnapshot in dataSnapshot.children) {
                    total++
                    val entries = userSnapshot.getValue(CensusEntry::class.java)
                    if (entries != null) {
                        println(entries.gender)
                        // Use user data to count males and females
                        countGender(entries.gender)
                    }
                }
                binding.population.text=total.toString()
                binding.populationMale.text=males.toString()
                binding.populationFemale.text=females.toString()
                binding.populationOthers.text=others.toString()

                val pieDataSet = PieDataSet(dataValues1(), "")
                binding.pieChart.setEntryLabelTextSize(16f)
                binding.pieChart.setHoleColor(Color.TRANSPARENT)
                pieDataSet.valueFormatter=PercentFormatter()
                binding.pieChart.setUsePercentValues(true)
                binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
                pieDataSet.valueTextSize = 10f
                pieDataSet.valueTextColor = Color.BLACK
                binding.pieChart.centerText="Sex Ratio"
                pieDataSet.colors = colorClassArray
                val pieData = PieData(pieDataSet)
                binding.pieChart.data = pieData
                binding.pieChart.invalidate()
//                drawGraph()
                println("total= {$total}, male= {$males}, female= {$females}, others= {$others}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
    fun countGender(gender: String?) {
        when (gender) {
            "Male" -> males++
            "Female" -> females++
            "Others" -> others++

        }
    }

    private fun dataValues1(): ArrayList<PieEntry> {
        val dataVals = ArrayList<PieEntry>()
        dataVals.add(PieEntry(males.toFloat(), "Males"))
        dataVals.add(PieEntry(females.toFloat(), "Females"))
        dataVals.add(PieEntry(others.toFloat(), "Others"))
        return dataVals
    }

    private fun drawGraph() {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(others.toFloat(), "Others"))
        entries.add(PieEntry(males.toFloat(), "Male"))
        entries.add(PieEntry(females.toFloat(), "Female"))
        binding.pieChart.setEntryLabelTextSize(16f)
        binding.pieChart.setHoleColor(Color.TRANSPARENT)
        val dataSet = PieDataSet(entries, "Population")
        dataSet.valueFormatter=PercentFormatter()
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
        dataSet.colors= listOf(Color.BLUE, Color.LTGRAY, Color.RED)
        dataSet.valueTextSize = 10f
        dataSet.valueTextColor = Color.BLACK
        binding.pieChart.centerText="Sex Ratio"

        val pieData = PieData(dataSet)
        binding.pieChart.data = pieData
    }
}