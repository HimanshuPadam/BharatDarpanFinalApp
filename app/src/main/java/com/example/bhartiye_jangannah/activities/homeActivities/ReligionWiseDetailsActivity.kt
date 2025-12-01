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
import com.example.bhartiye_jangannah.databinding.ActivityReligionWiseDetailsBinding
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

class ReligionWiseDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReligionWiseDetailsBinding
    private lateinit var dbRef: DatabaseReference
    var total: Int = 0
    var hindu: Int = 0
    var sikh: Int = 0
    var muslim: Int = 0
    var christian: Int = 0
    var jain: Int = 0
    var buddhist: Int = 0
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
        binding= ActivityReligionWiseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dbRef = FirebaseDatabase.getInstance().getReference("Census Entry")
        getTotalPopulation()
        println("total= {$total}, hindu= {$hindu}, sikh= {$sikh}, muslim= {$muslim}, christian= {$christian}, jain= {$jain}, buddhism= {$buddhist}")
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
                        getReligionDetails()
                    }
                    else{
                        getTotalPopulation()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    getTotalPopulation()
                }
            }
        if(total>0 || hindu>0 || sikh>0 || christian>0|| muslim>0|| jain>0|| buddhist>0) {
            binding.population.text= total.toString()
            binding.populationHindu.text= hindu.toString()
            binding.populationMuslim.text= muslim.toString()
            binding.populationSikh.text= sikh.toString()
            binding.populationChristian.text= christian.toString()
            binding.populationJain.text= jain.toString()
            binding.populationBuddhist.text= buddhist.toString()

            val pieDataSet = PieDataSet(dataValues1(), "")
            binding.pieChart.setEntryLabelTextSize(16f)
            binding.pieChart.setHoleColor(Color.TRANSPARENT)
            pieDataSet.valueFormatter= PercentFormatter()
            binding.pieChart.setUsePercentValues(true)
            binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
            pieDataSet.valueTextSize = 10f
            pieDataSet.valueTextColor = Color.BLACK
            binding.pieChart.centerText="Religion wise Population"
            pieDataSet.colors = colorClassArray
            val pieData = PieData(pieDataSet)
            binding.pieChart.data = pieData
            binding.pieChart.invalidate()
        }
    }

    private fun getReligionDetails(){
        dbRef.orderByChild("state").equalTo(selectedItemState).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                total= 0
                hindu= 0
                muslim= 0
                sikh= 0
                christian= 0
                jain= 0
                buddhist= 0
                // Iterate through the dataSnapshot to get user data
                for (userSnapshot in dataSnapshot.children) {
                    total++
                    val entries = userSnapshot.getValue(CensusEntry::class.java)
                    if (entries != null) {
                        // Use user data to count males and females
                        countReligion(entries.religion)
                    }
                }
                binding.population.text= total.toString()
                binding.populationHindu.text= hindu.toString()
                binding.populationMuslim.text= muslim.toString()
                binding.populationSikh.text= sikh.toString()
                binding.populationChristian.text= christian.toString()
                binding.populationJain.text= jain.toString()
                binding.populationBuddhist.text= buddhist.toString()

                val pieDataSet = PieDataSet(dataValues1(), "")
                binding.pieChart.setEntryLabelTextSize(16f)
                binding.pieChart.setHoleColor(Color.TRANSPARENT)
                pieDataSet.valueFormatter=PercentFormatter()
                binding.pieChart.setUsePercentValues(true)
                binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
                pieDataSet.valueTextSize = 10f
                pieDataSet.valueTextColor = Color.BLACK
                binding.pieChart.centerText="Religion wise Population"
                pieDataSet.colors = colorClassArray
                val pieData = PieData(pieDataSet)
                binding.pieChart.data = pieData
                binding.pieChart.invalidate()
                println("total= {$total}, hindu= {$hindu}, sikh= {$sikh}, muslim= {$muslim}, christian= {$christian}, jain= {$jain}, buddhism= {$buddhist}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    fun countReligion(religion: String?) {
        when (religion) {
            "Hindu" -> hindu++
            "Sikh" -> sikh++
            "Muslim" -> muslim++
            "Christian" -> christian++
            "Jainism" -> jain++
            "Buddhism" -> buddhist++

        }
    }

    private fun getTotalPopulation(){
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                total= 0
                hindu= 0
                muslim= 0
                sikh= 0
                christian= 0
                jain= 0
                buddhist= 0
                // Iterate through the dataSnapshot to get user data
                for (userSnapshot in dataSnapshot.children) {
                    total++
                    val entries = userSnapshot.getValue(CensusEntry::class.java)
                    if (entries != null) {
                        println("Function" +entries.religion)
                        // Use user data to count males and females
                        countReligion(entries.religion)
                    }
                }
                binding.population.text= total.toString()
                binding.populationHindu.text= hindu.toString()
                binding.populationMuslim.text= muslim.toString()
                binding.populationSikh.text= sikh.toString()
                binding.populationChristian.text= christian.toString()
                binding.populationJain.text= jain.toString()
                binding.populationBuddhist.text= buddhist.toString()

                val pieDataSet = PieDataSet(dataValues1(), "")
                binding.pieChart.setEntryLabelTextSize(16f)
                binding.pieChart.setHoleColor(Color.TRANSPARENT)
                pieDataSet.valueFormatter=PercentFormatter()
                binding.pieChart.setUsePercentValues(true)
                binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
                pieDataSet.valueTextSize = 10f
                pieDataSet.valueTextColor = Color.BLACK
                binding.pieChart.centerText="Religion wise Population"
                pieDataSet.colors = colorClassArray
                val pieData = PieData(pieDataSet)
                binding.pieChart.data = pieData
                binding.pieChart.invalidate()
                println("Function total= {$total}, hindu= {$hindu}, sikh= {$sikh}, muslim= {$muslim}, christian= {$christian}, jain= {$jain}, buddhism= {$buddhist}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
    private fun dataValues1(): ArrayList<PieEntry> {
        val dataVals = ArrayList<PieEntry>()
        dataVals.add(PieEntry(hindu.toFloat(), "Hindu"))
        dataVals.add(PieEntry(sikh.toFloat(), "Sikh"))
        dataVals.add(PieEntry(muslim.toFloat(), "Muslim"))
        dataVals.add(PieEntry(christian.toFloat(), "Christian"))
        dataVals.add(PieEntry(jain.toFloat(), "Jain"))
        dataVals.add(PieEntry(buddhist.toFloat(), "Buddhist"))
        return dataVals
    }
}