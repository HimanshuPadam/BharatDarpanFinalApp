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
import com.example.bhartiye_jangannah.adapters.ChildRecyclerViewAdapter
import com.example.bhartiye_jangannah.adapters.RecyclerViewAdapter
import com.example.bhartiye_jangannah.databinding.ActivityChildPopulationDetailsBinding
import com.example.bhartiye_jangannah.databinding.ActivityPopulationDetailsBinding
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.example.bhartiye_jangannah.modals.ChildEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChildPopulationDetailsActivity : AppCompatActivity() {
    private lateinit var entryRecyclerView: RecyclerView
    private var arrayList= arrayListOf<ChildEntry>()
    lateinit var binding: ActivityChildPopulationDetailsBinding
    private lateinit var dbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityChildPopulationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        entryRecyclerView=binding.recyclerView
        entryRecyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        arrayList= arrayListOf<ChildEntry>()

        getChildEntry()
    }
    private fun getChildEntry() {
        binding.tvLoadingData.visibility= View.GONE
        binding.recyclerView.visibility= View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Child Entry")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                if(snapshot.exists()){
                    for(entrySnap in snapshot.children){
                        val entry = entrySnap.getValue(ChildEntry::class.java)
                        arrayList.add(entry!!)
                    }
                    println(arrayList)
                    val mAdapter = ChildRecyclerViewAdapter(arrayList)
                    entryRecyclerView.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()

                    entryRecyclerView.visibility = View.VISIBLE
                    binding.tvLoadingData.visibility= View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}