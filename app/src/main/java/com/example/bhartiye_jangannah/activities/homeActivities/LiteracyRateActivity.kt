package com.example.bhartiye_jangannah.activities.homeActivities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.databinding.ActivityLiteracyRateBinding
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

class LiteracyRateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiteracyRateBinding
    private lateinit var dbRef: DatabaseReference
    var total:Int =0
    var literate:Int =0
    var illiterate:Int =0
    var selectedItemState: String?= null

    private val colorClassArray = listOf<Int>(
        Color.BLUE,
        Color.LTGRAY,
        Color.RED,
        Color.CYAN,
        Color.DKGRAY,
        Color.GREEN,
        Color.MAGENTA)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityLiteracyRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dbRef = FirebaseDatabase.getInstance().getReference("Census Entry")
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
                        getLiteracyDetails()
                    }
                    else{
                        getTotalPopulation()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    getTotalPopulation()
                }
            }
        if(total>0 || literate>0 || illiterate>0) {
            binding.population.text= total.toString()
            binding.populationLiterate.text= literate.toString()
            binding.populationIlliterate.text= illiterate.toString()


            val pieDataSet = PieDataSet(dataValues1(), "")
            binding.pieChart.setEntryLabelTextSize(16f)
            binding.pieChart.setHoleColor(Color.TRANSPARENT)
            pieDataSet.valueFormatter= PercentFormatter()
            binding.pieChart.setUsePercentValues(true)
            binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
            pieDataSet.valueTextSize = 10f
            pieDataSet.valueTextColor = Color.BLACK
            binding.pieChart.centerText="Literacy Rate"
            pieDataSet.colors = colorClassArray
            val pieData = PieData(pieDataSet)
            binding.pieChart.data = pieData
            binding.pieChart.invalidate()
        }
    }
    private fun getTotalPopulation(){
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                total= 0
                literate= 0
                illiterate= 0

                // Iterate through the dataSnapshot to get user data
                for (userSnapshot in dataSnapshot.children) {
                    total++
                    val entries = userSnapshot.getValue(CensusEntry::class.java)
                    if (entries != null) {
                        println("Function" +entries.literacyStatus)
                        // Use user data to count males and females
                        countLiteracyStatus(entries.literacyStatus)
                    }
                }
                binding.population.text= total.toString()
                binding.populationLiterate.text= literate.toString()
                binding.populationIlliterate.text= illiterate.toString()

                val pieDataSet = PieDataSet(dataValues1(), "")
                pieDataSet.valueFormatter=PercentFormatter()
                binding.pieChart.setEntryLabelTextSize(16f)
                binding.pieChart.setHoleColor(Color.TRANSPARENT)
                binding.pieChart.setUsePercentValues(true)
                binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
                pieDataSet.valueTextSize = 10f
                pieDataSet.valueTextColor = Color.BLACK
                binding.pieChart.centerText="Literacy Rate"
                pieDataSet.colors = colorClassArray
                val pieData = PieData(pieDataSet)
                binding.pieChart.data = pieData
                binding.pieChart.invalidate()
//                println("Function total= {$total}, hindu= {$hindu}, sikh= {$sikh}, muslim= {$muslim}, christian= {$christian}, jain= {$jain}, buddhism= {$buddhist}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun getLiteracyDetails(){
        dbRef.orderByChild("state").equalTo(selectedItemState).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                total= 0
                literate= 0
                illiterate= 0
                // Iterate through the dataSnapshot to get user data
                for (userSnapshot in dataSnapshot.children) {
                    total++
                    val entries = userSnapshot.getValue(CensusEntry::class.java)
                    if (entries != null) {
                        // Use user data to count males and females
                        countLiteracyStatus(entries.maritalStatus)
                    }
                }
                binding.population.text= total.toString()
                binding.population.text= total.toString()
                binding.populationLiterate.text= literate.toString()
                binding.populationIlliterate.text= illiterate.toString()

                val pieDataSet = PieDataSet(dataValues1(), "")
                pieDataSet.valueFormatter=PercentFormatter()
                binding.pieChart.setEntryLabelTextSize(16f)
                binding.pieChart.setHoleColor(Color.TRANSPARENT)
                binding.pieChart.setUsePercentValues(true)
                binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
                pieDataSet.valueTextSize = 10f
                pieDataSet.valueTextColor = Color.BLACK
                binding.pieChart.centerText="Literacy Rate"
                pieDataSet.colors = colorClassArray
                val pieData = PieData(pieDataSet)
                binding.pieChart.data = pieData
                binding.pieChart.invalidate()
//                println("total= {$total}, hindu= {$hindu}, sikh= {$sikh}, muslim= {$muslim}, christian= {$christian}, jain= {$jain}, buddhism= {$buddhist}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    fun countLiteracyStatus(lStatus: String?) {
        when (lStatus) {
            "Literate" -> literate++
            "Illiterate" -> illiterate++
        }
    }

    private fun dataValues1(): ArrayList<PieEntry> {
        val dataVals = ArrayList<PieEntry>()
        dataVals.add(PieEntry(literate.toFloat(), "Literate"))
        dataVals.add(PieEntry(illiterate.toFloat(), "Illiterate"))
        return dataVals
    }
}