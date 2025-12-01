package com.example.bhartiye_jangannah.activities.homeActivities

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.databinding.ActivityCasteWiseDetailsBinding
import com.example.bhartiye_jangannah.databinding.ActivityReligionWiseDetailsBinding
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CasteWiseDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCasteWiseDetailsBinding
    private lateinit var dbRef: DatabaseReference
    var total:Int =0
    var general:Int =0
    var obc:Int =0
    var bc:Int =0
    var sc:Int =0
    var st:Int =0
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
        binding= ActivityCasteWiseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
                        getCasteDetails()
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
    private fun getCasteDetails(){
        dbRef.orderByChild("state").equalTo(selectedItemState).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                total= 0
                general= 0
                obc= 0
                bc= 0
                sc= 0
                st= 0
                // Iterate through the dataSnapshot to get user data
                for (userSnapshot in dataSnapshot.children) {
                    total++
                    val entries = userSnapshot.getValue(CensusEntry::class.java)
                    if (entries != null) {
                        // Use user data to count males and females
                        countGender(entries.caste)
                    }
                }
                println("Gen=$general, OBC=$obc")
                binding.population.text= total.toString()
                binding.populationGeneral.text= general.toString()
                binding.populationSC.text= sc.toString()
                binding.populationBC.text= bc.toString()
                binding.populationST.text= st.toString()
                binding.populationOBC.text= obc.toString()

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

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    fun countGender(gender: String?) {
        when (gender) {
            "General" -> general++
            "OBC" -> obc++
            "SC" -> sc++
            "ST" -> st++
            "BC" -> bc++
        }
    }

    private fun getTotalPopulation(){
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                total= 0
                general= 0
                obc= 0
                bc= 0
                sc= 0
                st= 0
                // Iterate through the dataSnapshot to get user data
                for (userSnapshot in dataSnapshot.children) {
                    total++
                    val entries = userSnapshot.getValue(CensusEntry::class.java)
                    if (entries != null) {
                        println("Function" +entries.caste)
                        // Use user data to count males and females
                        countGender(entries.caste)
                    }
                }
                println("Gen=$general, OBC=$obc")
                binding.population.text= total.toString()
                binding.populationGeneral.text= general.toString()
                binding.populationSC.text= sc.toString()
                binding.populationBC.text= bc.toString()
                binding.populationST.text= st.toString()
                binding.populationOBC.text= obc.toString()

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
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
    private fun dataValues1(): ArrayList<PieEntry> {
        val dataVals = ArrayList<PieEntry>()
        dataVals.add(PieEntry(general.toFloat(), "General"))
        dataVals.add(PieEntry(sc.toFloat(), "SC"))
        dataVals.add(PieEntry(obc.toFloat(), "OBC"))
        dataVals.add(PieEntry(st.toFloat(), "ST"))
        dataVals.add(PieEntry(bc.toFloat(), "BC"))
        return dataVals
    }
}