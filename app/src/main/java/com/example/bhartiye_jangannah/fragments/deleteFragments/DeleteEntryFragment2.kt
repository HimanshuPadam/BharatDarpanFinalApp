package com.example.bhartiye_jangannah.fragments.deleteFragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.bhartiye_jangannah.activities.HomePageActivity
import com.example.bhartiye_jangannah.databinding.FragmentDeleteEntry2Binding
import com.example.bhartiye_jangannah.databinding.SubmitLayoutBinding
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DeleteEntryFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeleteEntryFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding : FragmentDeleteEntry2Binding
    private var aadharNoMilGya: String?= null
    private var arrayList = CensusEntry()

    private var aadharCardNo= listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding= FragmentDeleteEntry2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbRef= FirebaseDatabase.getInstance().getReference("Census Entry")
        arguments?.let {
            aadharNoMilGya = it.getString("aadharNoMilGya")
            aadharCardNo= aadharNoMilGya!!.split("")
            getDetailsToBeUpdated()

            binding.btnDelete.setOnClickListener {
                println("$arrayList")
                var dialog = AlertDialog.Builder(requireActivity())
                dialog.setTitle("Delete Entry")
                dialog.setMessage("Are you sure that you want to delete this entry?")
                dialog.setCancelable(false)
                dialog.setPositiveButton("Yes") {_,_->

                    dbRef= FirebaseDatabase.getInstance().getReference("Census Entry")
                    dbRef.child(aadharNoMilGya!!).removeValue()
                        .addOnSuccessListener {
                            var dialog2 = Dialog(requireActivity())
                            var dialogBinding = SubmitLayoutBinding.inflate(layoutInflater)
                            dialogBinding.tvSubmit.text = "Deleted successfully"
                            dialog2.setContentView(dialogBinding.root)
                            dialog2.setCancelable(false)
                            dialogBinding.btnGotoHome.setOnClickListener {
                                var intent = Intent(requireActivity(), HomePageActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                                dialog2.dismiss()
                            }
                            dialog2.show()
                        }
                }
                dialog.setNegativeButton("No") { _, _ ->
                    Toast.makeText(requireActivity(),"Okayy", Toast.LENGTH_SHORT).show()
                }
                dialog.show()
            }

        }
    }

    private fun getDetailsToBeUpdated(){
        dbRef = FirebaseDatabase.getInstance().getReference("Census Entry")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(entrySnap in snapshot.children){
                        val entry = entrySnap.getValue(CensusEntry::class.java)
                        if (entry != null) {
                            if(entry.aadharNo==aadharNoMilGya) {
                                arrayList=entry
                                System.out.println(arrayList)
                            }
                        }
                    }
                    if (arrayList != null) {
                        binding.tvName.text= arrayList.name
                        binding.tvHeadOfFamily.text = arrayList.headOfFamily
                        if(arrayList.headOfFamily=="No") {
                            binding.tvRelationshipWithHeadQues.isVisible = true
                            binding.tvRelationshipWithHead.isVisible = true
                        }else{
                            binding.tvRelationshipWithHeadQues.isVisible = false
                            binding.tvRelationshipWithHead.isVisible = false
                            binding.tvRelationshipWithHead.text = arrayList.relationshipWithHead
                        }

                        binding.tvAadharNo.text = arrayList.aadharNo

                        binding.tvGender.text = arrayList.gender

                        if (arrayList.gender == "Female"){
                            binding.tvFathersNameQues.text = "Father's/Husband's Name"
                        }

                        binding.tvFathersName.text = arrayList.fathersName

                        binding.tvReligion.text = arrayList.religion

                        binding.tvCaste.text = arrayList.caste

                        binding.tvLiteracyStatus.text = arrayList.literacyStatus

                        if(arrayList.literacyStatus== "Literate") {
                            binding.tvLastClassStudied.isVisible = true
                            binding.tvLastClassStudiedQues.isVisible = true
                            binding.tvLastClassStudied.text = arrayList.lastClassStudied
                        }
                        else{
                            binding.tvLastClassStudied.isVisible = false
                            binding.tvLastClassStudiedQues.isVisible = false
                        }

                        binding.tvMaritalStatus.text = arrayList.maritalStatus

                        if(arrayList.maritalStatus == "Unmarried"){
                            binding.tvAgeAtMarriage.isVisible = false
                            binding.tvAgeAtMarriageQues.isVisible = false
                        }
                        else {
                            binding.tvAgeAtMarriage.isVisible = true
                            binding.tvAgeAtMarriageQues.isVisible = true
                            binding.tvAgeAtMarriage.text = arrayList.ageAtMarriage
                        }

                        binding.tvMotherTongue.text = arrayList.motherTongue

                        binding.tvOtherLanguagesKnown.text = arrayList.otherLanguagesKnown

                        binding.tvDOB.text = arrayList.dateOfBirth

                        binding.tvAge.text = arrayList.age

                        binding.tvOccupation.text = arrayList.occupation

                        binding.tvAddress.text = arrayList.address

                        binding.tvDistrict.text = arrayList.district

                        binding.tvState.text = arrayList.state

                        binding.tvBirthMark.text = arrayList.birthmark

                        binding.tvDisability.text = arrayList.disability

                        if(arrayList.disability == "Yes"){
                            binding.tvTypeOfDisabilityQues.isVisible = true
                            binding.tvTypeOfDisability.isVisible = true
                            binding.tvTypeOfDisability.text = arrayList.typeOfDisability
                        }
                        else{
                            binding.tvTypeOfDisabilityQues.isVisible = false
                            binding.tvTypeOfDisability.isVisible = false
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DeleteEntryFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeleteEntryFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}