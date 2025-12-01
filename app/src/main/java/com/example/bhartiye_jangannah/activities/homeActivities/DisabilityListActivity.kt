package com.example.bhartiye_jangannah.activities.homeActivities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.adapters.RecyclerViewAdapter
import com.example.bhartiye_jangannah.databinding.ActivityAdultPopulationBinding
import com.example.bhartiye_jangannah.databinding.ActivityDisabilityListBinding
import com.example.bhartiye_jangannah.databinding.ActivityPopulationDetailsBinding
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class DisabilityListActivity : AppCompatActivity() {
    private lateinit var entryRecyclerView: RecyclerView
    private var arrayList= arrayListOf<CensusEntry>()
    lateinit var binding: ActivityDisabilityListBinding
    private lateinit var dbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityDisabilityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        entryRecyclerView=binding.recyclerView
        entryRecyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        entryRecyclerView.setHasFixedSize(true)

        arrayList= arrayListOf<CensusEntry>()

        getCensusEntry()
    }
    private fun getCensusEntry() {
        binding.tvLoadingData.visibility= View.GONE
        binding.recyclerView.visibility= View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Census Entry")
        val query : Query = dbRef.orderByChild("disability").equalTo("Yes")
        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                if(snapshot.exists()){
                    for(entrySnap in snapshot.children){
                        val entry = entrySnap.getValue(CensusEntry::class.java)
                        if (entry != null) {
                            arrayList.add(entry)
                        }
                        println(arrayList)
                    }

                    val mAdapter = RecyclerViewAdapter(arrayList)
                    entryRecyclerView.adapter = mAdapter

                    entryRecyclerView.visibility = View.VISIBLE
                    binding.tvLoadingData.visibility= View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}