package com.example.bhartiye_jangannah.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.modals.ChildEntry

class ChildRecyclerViewAdapter(var arrayList: ArrayList<ChildEntry>) :
    RecyclerView.Adapter<ChildRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tvName=view.findViewById<TextView>(R.id.tvName)
        var tvChildHasAadhar=view.findViewById<TextView>(R.id.tvChildHasAadhar)
        var tvAadharNo = view.findViewById<TextView>(R.id.tvAadharNo)
        var tvMothersAadharNo = view.findViewById<TextView>(R.id.tvMothersAadharNo)
        var tvGender = view.findViewById<TextView>(R.id.tvGender)
        var tvFathersName = view.findViewById<TextView>(R.id.tvFathersName)
        var tvMothersName = view.findViewById<TextView>(R.id.tvMothersName)
        var tvReligion = view.findViewById<TextView>(R.id.tvReligion)
        var tvCaste = view.findViewById<TextView>(R.id.tvCaste)
        var tvDOB = view.findViewById<TextView>(R.id.tvDOB)
        var tvAge = view.findViewById<TextView>(R.id.tvAge)
        var tvAddress = view.findViewById<TextView>(R.id.tvAddress)
        var tvDistrict = view.findViewById<TextView>(R.id.tvDistrict)
        var tvState = view.findViewById<TextView>(R.id.tvState)
        var tvBirthMark = view.findViewById<TextView>(R.id.tvBirthMark)
        var tvDisability = view.findViewById<TextView>(R.id.tvDisability)
        var tvTypeOfDisability = view.findViewById<TextView>(R.id.tvTypeOfDisability)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChildRecyclerViewAdapter.ViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.child_recycle_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChildRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.tvName.text=arrayList[position].name
        holder.tvChildHasAadhar.text=arrayList[position].childHasAadhar
        if(arrayList[position].childHasAadhar=="Yes") {
            holder.tvMothersAadharNo.text = "-"
            holder.tvAadharNo.text = arrayList[position].aadharNo
        }
        else{
            holder.tvAadharNo.text = "-"
            holder.tvMothersAadharNo.text = arrayList[position].mothersAadharNo
        }
        holder.tvGender.text=arrayList[position].gender
        holder.tvFathersName.text=arrayList[position].fathersName
        holder.tvMothersName.text=arrayList[position].mothersName
        holder.tvReligion.text=arrayList[position].religion
        holder.tvCaste.text=arrayList[position].caste
        holder.tvDOB.text=arrayList[position].dateOfBirth
        holder.tvAge.text=arrayList[position].age
        holder.tvAddress.text=arrayList[position].address
        holder.tvDistrict.text=arrayList[position].district
        holder.tvState.text=arrayList[position].state
        holder.tvBirthMark.text=arrayList[position].birthmark
        holder.tvDisability.text=arrayList[position].disability
        if(arrayList[position].disability=="No") {
            holder.tvTypeOfDisability.text = "-"
        }
        else{
            holder.tvTypeOfDisability.text = arrayList[position].typeOfDisability
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}