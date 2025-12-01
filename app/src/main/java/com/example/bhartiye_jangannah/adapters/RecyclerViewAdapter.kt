package com.example.bhartiye_jangannah.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(var arrayList: ArrayList<CensusEntry>):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tvName=view.findViewById<TextView>(R.id.tvName)
        var tvHeadOfFamily=view.findViewById<TextView>(R.id.tvHeadOfFamily)
        var tvRelationshipWithHead = view.findViewById<TextView>(R.id.tvRelationshipWithHead)
        var tvAadharNo = view.findViewById<TextView>(R.id.tvAadharNo)
        var tvGender = view.findViewById<TextView>(R.id.tvGender)
        var tvFathersName = view.findViewById<TextView>(R.id.tvFathersName)
        var tvReligion = view.findViewById<TextView>(R.id.tvReligion)
        var tvCaste = view.findViewById<TextView>(R.id.tvCaste)
        var tvLiteracyStatus = view.findViewById<TextView>(R.id.tvLiteracyStatus)
        var tvLastClassStudied = view.findViewById<TextView>(R.id.tvLastClassStudied)
        var tvMaritalStatus = view.findViewById<TextView>(R.id.tvMaritalStatus)
        var tvAgeAtMarriage = view.findViewById<TextView>(R.id.tvAgeAtMarriage)
        var tvMotherTongue = view.findViewById<TextView>(R.id.tvMotherTongue)
        var tvOtherLanguagesKnown = view.findViewById<TextView>(R.id.tvOtherLanguagesKnown)
        var tvDOB = view.findViewById<TextView>(R.id.tvDOB)
        var tvAge = view.findViewById<TextView>(R.id.tvAge)
        var tvOccupation = view.findViewById<TextView>(R.id.tvOccupation)
        var tvAddress = view.findViewById<TextView>(R.id.tvAddress)
        var tvDistrict = view.findViewById<TextView>(R.id.tvDistrict)
        var tvState = view.findViewById<TextView>(R.id.tvState)
        var tvBirthMark = view.findViewById<TextView>(R.id.tvBirthMark)
        var tvDisability = view.findViewById<TextView>(R.id.tvDisability)
        var tvTypeOfDisability = view.findViewById<TextView>(R.id.tvTypeOfDisability)
        var imageView = view.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.entry_recycle_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        print("hjgdhjghdhsa"+arrayList[position].imageUrl)

        
            holder.tvName.text=arrayList[position].name
            holder.tvHeadOfFamily.text=arrayList[position].headOfFamily
            if(arrayList[position].headOfFamily=="Yes") {
                holder.tvRelationshipWithHead.text = "-"
            }
            else{
                holder.tvRelationshipWithHead.text = arrayList[position].relationshipWithHead
            }
            holder.tvAadharNo.text=arrayList[position].aadharNo
            holder.tvGender.text=arrayList[position].gender
            holder.tvFathersName.text=arrayList[position].fathersName
            holder.tvReligion.text=arrayList[position].religion
            holder.tvCaste.text=arrayList[position].caste
            holder.tvLiteracyStatus.text=arrayList[position].literacyStatus
            if(arrayList[position].literacyStatus == "Literate") {
                holder.tvLastClassStudied.text = arrayList[position].lastClassStudied
            }
            else{
                holder.tvLastClassStudied.text = "-"
            }
            holder.tvMaritalStatus.text=arrayList[position].maritalStatus
            if(arrayList[position].maritalStatus=="Unmarried") {
                holder.tvAgeAtMarriage.text = "-"
            }
            else{
                holder.tvAgeAtMarriage.text = arrayList[position].ageAtMarriage
            }
            holder.tvMotherTongue.text=arrayList[position].motherTongue
            holder.tvOtherLanguagesKnown.text=arrayList[position].otherLanguagesKnown
            holder.tvDOB.text=arrayList[position].dateOfBirth
            holder.tvAge.text=arrayList[position].age
            holder.tvOccupation.text=arrayList[position].occupation
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
////        var uri = Uri.parse(arrayList[position].imageUrl)
//        Picasso.get().load(arrayList[position].imageUrl).into(holder.imageView)
//        loadImage(arrayList[position].imageUrl, holder.imageView)
        val imageUrl = arrayList[position].imageUrl
        if (imageUrl != null) {
            if (imageUrl.isNotEmpty()) {
                Picasso.get()
                    .load(imageUrl)
                    .into(holder.imageView)
            }
        }
        print(arrayList)
    }

//    private fun loadImage(imageUrl: String?, imageView: ImageView?) {
//        val storageRef = FirebaseStorage.getInstance().reference
//        storageRef.child(imageUrl!!).downloadUrl.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val downloadUri = task.result
//                Picasso.get().load(downloadUri).into(imageView)
//            } else {
//                // Handle download URL retrieval failure
//            }
//        }
//    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}