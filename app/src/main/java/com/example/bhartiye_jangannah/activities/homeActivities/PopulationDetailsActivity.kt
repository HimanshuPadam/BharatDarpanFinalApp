package com.example.bhartiye_jangannah.activities.homeActivities

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.adapters.RecyclerViewAdapter
import com.example.bhartiye_jangannah.databinding.ActivityPopulationDetailsBinding
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class PopulationDetailsActivity : AppCompatActivity() {
    private lateinit var entryRecyclerView: RecyclerView
    private var arrayList= arrayListOf<CensusEntry>()
    lateinit var binding: ActivityPopulationDetailsBinding
    private lateinit var dbRef : DatabaseReference
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPopulationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        entryRecyclerView=binding.recyclerView
        entryRecyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        arrayList= arrayListOf<CensusEntry>()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        getCensusEntry()

    }

    private fun getCensusEntry() {
        progressDialog.show()
        binding.recyclerView.visibility= View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Census Entry")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.dismiss()
                arrayList.clear()
                if(snapshot.exists()){
                    for(entrySnap in snapshot.children){
                        val entry = entrySnap.getValue(CensusEntry::class.java)
                        arrayList.add(entry!!)
                    }
                    println(arrayList)
                    val mAdapter = RecyclerViewAdapter(arrayList)
                    entryRecyclerView.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()

                    entryRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
            }
        })
    }
}