package com.example.bhartiye_jangannah.fragments.navigationFragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.activities.HomePageActivity
import com.example.bhartiye_jangannah.databinding.FragmentNewEntryBinding
import com.example.bhartiye_jangannah.databinding.SubmitLayoutBinding
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewEntryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewEntryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentNewEntryBinding
    private lateinit var dbRef: DatabaseReference


    private var aadharCardList= arrayListOf<EditText>()
    private var aadharNumber: String= ""
    private var selectedButtonHeadOfHouseholdText: String ?= null
    private var relationshipWithHead : String ?= null
    private var selectedButtonGenderText : String ?= null
    private var selectedItemReligion : String ?= null
    private var selectedItemCaste : String ?= null
    private var selectedButtonLiteracyStatus : String ?= null
    private var selectedItemMaritalStatus : String ?= null
    private var stringDate : String ?= null
    private var selectedItemState : String ?= null
    private var selectedButtonDisabilityText : String ?= null

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
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentNewEntryBinding.inflate(layoutInflater, container,false)
        dbRef= FirebaseDatabase.getInstance().getReference("Census Entry")
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Head of household radio group
        binding.headRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val selectedButtonHeadOfHousehold = view.findViewById<RadioButton>(checkedId)
            selectedButtonHeadOfHouseholdText = selectedButtonHeadOfHousehold.text.toString()
            binding.tvErrorHead.visibility= View.GONE
        }
//        val checkedRadioButton = findCheckedRadioButton(binding.headRadioGroup)
        val headCheckedRadioButtonId= binding.headRadioGroup.checkedRadioButtonId
        println(headCheckedRadioButtonId)

        binding.headRadioGroupYes.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.layoutRelationshipWithHead.visibility = View.GONE
                binding.etRelationshipWithHead.visibility = View.GONE
            } else {
                binding.layoutRelationshipWithHead.visibility = View.VISIBLE
                binding.etRelationshipWithHead.visibility = View.VISIBLE
            }
        }

        relationshipWithHead = binding.etRelationshipWithHead.text.toString()

        //adding all aadhar digits to array list
        aadharCardList.add(binding.etAadharDigit1)
        aadharCardList.add(binding.etAadharDigit2)
        aadharCardList.add(binding.etAadharDigit3)
        aadharCardList.add(binding.etAadharDigit4)
        aadharCardList.add(binding.etAadharDigit5)
        aadharCardList.add(binding.etAadharDigit6)
        aadharCardList.add(binding.etAadharDigit7)
        aadharCardList.add(binding.etAadharDigit8)
        aadharCardList.add(binding.etAadharDigit9)
        aadharCardList.add(binding.etAadharDigit10)
        aadharCardList.add(binding.etAadharDigit11)
        aadharCardList.add(binding.etAadharDigit12)

        //to move the cursor to next edit text of aadhar digit
        aadharCardList.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length == 1 && index < aadharCardList.size - 1) {
                        aadharCardList[index + 1].requestFocus()
                    }
                }
            })

            //to go to previous edit text when user presses the backspace key
            editText.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (index > 0 && editText.text.isEmpty()) {
                        //If the backspace key is pressed in the EditText and it is empty, then move focus to the previous EditText
                        aadharCardList[index - 1].requestFocus()
                    }
                }
                false
            }
        }
        // we got the full aadhar number in variable aadharNumber

        //Gender radio group
        binding.genderRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val selectedButtonGender = view.findViewById<RadioButton>(checkedId)
            selectedButtonGenderText = selectedButtonGender.text.toString()
            Toast.makeText(requireActivity(), "$selectedButtonGenderText", Toast.LENGTH_SHORT).show()
        }
        val genderCheckedRadioButtonId= binding.genderRadioGroup.checkedRadioButtonId

        binding.genderRadioGroupFemale.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.layoutFathersName.setHint("Father's/Husband's Name")
            } else {
                binding.layoutFathersName.setHint("Father's Name")
            }
        }

        //Religion spinner
        val adapterReligion: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.religion_array,
            android.R.layout.simple_spinner_item
        )
        adapterReligion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerReligion.adapter = adapterReligion

        binding.spinnerReligion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        selectedItemReligion = parent?.getItemAtPosition(position).toString()
                        println("$selectedItemReligion")
                        Toast.makeText(
                            requireActivity(),
                            "Selected Item: $selectedItemReligion",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        //Caste spinner
        val adapterCaste: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.castes_array,
            android.R.layout.simple_spinner_item
        )

        adapterCaste.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerCaste.adapter = adapterCaste

        binding.spinnerCaste.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        selectedItemCaste = parent?.getItemAtPosition(position).toString()
                        Toast.makeText(
                            requireActivity(),
                            "Selected Item: $selectedItemCaste",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        //Currently Studying radio group
        binding.literacyStatusRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val selectedButtonCurrentlyStudying = view.findViewById<RadioButton>(checkedId)
            selectedButtonLiteracyStatus = selectedButtonCurrentlyStudying.text.toString()
            Toast.makeText(requireActivity(), "$selectedButtonLiteracyStatus", Toast.LENGTH_SHORT).show()
        }
        binding.literacyStatusRadioGroupIlliterate.setOnCheckedChangeListener{compoundButton, isChecked ->
            if(isChecked){
                binding.layoutLastClassStudied.visibility= View.GONE
                binding.etLastClassStudied.visibility= View.GONE
            }
            else{
                binding.layoutLastClassStudied.visibility= View.VISIBLE
                binding.etLastClassStudied.visibility= View.VISIBLE
            }
        }
        //Marital Status radio group
        binding.spinnerMaritalStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        selectedItemMaritalStatus = parent?.getItemAtPosition(position).toString()
                        Toast.makeText(
                            requireActivity(),
                            "Selected Item: $selectedItemMaritalStatus",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (binding.spinnerMaritalStatus.selectedItem=="Unmarried") {
                            binding.etAgeAtMarriage.visibility = View.GONE
                            binding.layoutAgeAtMarriage.visibility = View.GONE
                        }
                        else {
                            binding.etAgeAtMarriage.visibility = View.VISIBLE
                            binding.layoutAgeAtMarriage.visibility = View.VISIBLE
                        }
//                        if(binding.spinnerMaritalStatus.selectedItem=="--Select--") {
//                            Toast.makeText(
//                                this@NewEntryActivity,
//                                "Please select marital status",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    if(binding.spinnerMaritalStatus.selectedItem=="--Select--") {
                        Toast.makeText(
                            requireActivity(),
                            "Please select marital status",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        //date of birth date picker
        binding.constraintLayoutDatePicker.setOnClickListener {
            val dialog = DatePickerDialog(requireActivity())
            dialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                val simpleDateFormat = SimpleDateFormat("dd MMM, yyyy")
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DATE, dayOfMonth)
                //stringDate has the date of birth of the person
                stringDate = simpleDateFormat.format(calendar.time)
                binding.tvDatePicker.text = stringDate
            }
            dialog.show()
        }
        val selectedYear = Calendar.YEAR
        val selectedMonth = Calendar.MONTH
        val selectedDay = Calendar.DAY_OF_MONTH
        val isValidDate = validateDate(selectedYear, selectedMonth, selectedDay)

        //State spinner
        val adapterState: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireActivity(),
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
                        println("$selectedItemState")
                        Toast.makeText(
                            requireActivity(),
                            "Selected Item: $selectedItemState",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        //Disability radio group
        binding.disabilityRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val selectedButtonDisability = view.findViewById<RadioButton>(checkedId)
            selectedButtonDisabilityText = selectedButtonDisability.text.toString()
            Toast.makeText(requireActivity(), "$selectedButtonDisabilityText", Toast.LENGTH_SHORT).show()
        }

        binding.disabilityRadioGroupNo.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.layoutTypeOfDisability.visibility = View.GONE
                binding.etTypeOfDisability.visibility = View.GONE
            } else {
                binding.layoutTypeOfDisability.visibility = View.VISIBLE
                binding.etTypeOfDisability.visibility = View.VISIBLE
            }
        }

        dbRef= FirebaseDatabase.getInstance().getReference("Census Entry")

        binding.btnSubmit.setOnClickListener {
            //to get the aadhar number in single variable
            aadharCardList.forEachIndexed { index, editText ->
                editText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                        // Read the Aadhar number from the EditText fields
                        aadharNumber = readAadhar()
                        println(aadharNumber)
                        // we can perform Aadhar verification or submit action here
                        editText.clearFocus()
                    }
                    false
                }
            }
            if (isValid()){
                Toast.makeText(requireActivity(), "Button clicked", Toast.LENGTH_SHORT).show()
                val id = dbRef.push().key!!

                val entries= CensusEntry(
                    id= id,
                    name = binding.etName.text.toString(),
                    headOfFamily = "$selectedButtonHeadOfHouseholdText",
                    fathersName = binding.etFathersName.text.toString(),
                    relationshipWithHead = binding.etRelationshipWithHead.text.toString(),
                    aadharNo = readAadhar(),
                    gender = "$selectedButtonGenderText",
                    religion = "$selectedItemReligion",
                    caste = "$selectedItemCaste",
                    literacyStatus = "$selectedButtonLiteracyStatus",
                    lastClassStudied = binding.etLastClassStudied.text.toString(),
                    maritalStatus = "$selectedItemMaritalStatus",
                    ageAtMarriage = binding.etAgeAtMarriage.text.toString(),
                    motherTongue = binding.etMotherTongue.text.toString(),
                    otherLanguagesKnown = binding.etOtherLanguages.text.toString(),
                    dateOfBirth = "$stringDate",
                    age = binding.etAge.text.toString(),
                    occupation = binding.etOccupation.text.toString(),
                    address = binding.etAddress.text.toString(),
                    district = binding.etDistrict.text.toString(),
                    state = "$selectedItemState",
                    birthmark = binding.etBirthmark.text.toString(),
                    disability = "$selectedButtonDisabilityText",
                    typeOfDisability = binding.etTypeOfDisability.text.toString(),
                )

                dbRef.child(id).setValue(entries).addOnCompleteListener {

                    var dialog= Dialog(requireActivity())
                    var dialogBinding = SubmitLayoutBinding.inflate(layoutInflater)
                    dialog.setContentView(dialogBinding.root)
                    dialog.setCancelable(false)
                    dialogBinding.btnGotoHome.setOnClickListener {
                        var intent = Intent(requireActivity(), HomePageActivity::class.java)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    dialog.show()
                }
                    .addOnFailureListener {err->
                        Toast.makeText(requireActivity(), "Error: ${err.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            else{
//                 Toast.makeText(this, "Fill the details first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun readAadhar(): String {
        val aadharNumber = StringBuilder()
        for (editText in aadharCardList) {
            aadharNumber.append(editText.text)
        }
        return aadharNumber.toString()
    }
    private fun validateDate(year: Int, month: Int, day: Int): Boolean {
        // Check if the selected date is in the future
        val currentDate = Calendar.getInstance()
        val selectedDate = Calendar.getInstance()
        selectedDate.set(year, month, day)

        if (selectedDate.before(currentDate)) {
            return false // Date is in the past, consider it invalid
        }

        return true // If the date passes all validation checks, consider it valid
    }
    private fun isValid(): Boolean {
        if(binding.etName.text.isNullOrEmpty()){
            binding.etName.error= "Enter the name"
            return false
        }
        else if(selectedButtonHeadOfHouseholdText==null){
            Toast.makeText(requireActivity(),"Select head of family",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedButtonHeadOfHouseholdText=="No"){
            if(binding.etRelationshipWithHead.text.isNullOrEmpty()){
                binding.etRelationshipWithHead.error="Enter relationship"
                return false
            }
        }
        else if(binding.etAadharDigit1.text.isNullOrEmpty()||binding.etAadharDigit2.text.isNullOrEmpty()||
            binding.etAadharDigit3.text.isNullOrEmpty()||binding.etAadharDigit4.text.isNullOrEmpty()||
            binding.etAadharDigit5.text.isNullOrEmpty()||binding.etAadharDigit6.text.isNullOrEmpty()||
            binding.etAadharDigit7.text.isNullOrEmpty()||binding.etAadharDigit8.text.isNullOrEmpty()||
            binding.etAadharDigit9.text.isNullOrEmpty()||binding.etAadharDigit10.text.isNullOrEmpty()||
            binding.etAadharDigit11.text.isNullOrEmpty()||binding.etAadharDigit12.text.isNullOrEmpty()){
            Toast.makeText(requireActivity(),"Enter AADHAR Number",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedButtonGenderText==null){
            Toast.makeText(requireActivity(),"Select gender",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.etFathersName.text.isNullOrEmpty()){
            binding.etFathersName.error = "Enter father's name"
            return false
        }
        else if(selectedItemReligion==null){
            Toast.makeText(requireActivity(),"Select religion",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedItemCaste==null){
            Toast.makeText(requireActivity(),"Select caste",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedButtonLiteracyStatus==null){
            Toast.makeText(requireActivity(),"Select literacy status",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedButtonLiteracyStatus=="Literate"){
            binding.etLastClassStudied.error="Enter last class studied"
            return false
        }
        else if(selectedItemMaritalStatus==null){
            Toast.makeText(requireActivity(),"Select marital status",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.spinnerMaritalStatus.selectedItem != "Unmarried"){
            if(binding.etAgeAtMarriage.text.isNullOrEmpty()){
                binding.etAgeAtMarriage.error= "Enter the age at the time of marriage"
                return false
            }
        }
        else if(binding.etMotherTongue.text.isNullOrEmpty()){
            binding.etMotherTongue.error = "Enter the mother tongue"
            return false
        }
        else if(binding.etOtherLanguages.text.isNullOrEmpty()){
            binding.etOtherLanguages.error = "Enter other languages known"
            return false
        }
        else if(stringDate==null){
            Toast.makeText(requireActivity(),"Select date of birth",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.etAge.text.isNullOrEmpty()){
            binding.etAge.error = "Enter the age"
            return false
        }
        else if(binding.etOccupation.text.isNullOrEmpty()){
            binding.etOccupation.error = "Enter the occupation"
            return false
        }
        else if(binding.etAddress.text.isNullOrEmpty()){
            binding.etAddress.error = "Enter the address"
            return false
        }
        else if(binding.etDistrict.text.isNullOrEmpty()){
            binding.etDistrict.error = "Enter the district"
            return false
        }
        else if(selectedItemState==null){
            Toast.makeText(requireActivity(),"Select state",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.etBirthmark.text.isNullOrEmpty()){
            binding.etBirthmark.error = "Specify the birthmark"
            return false
        }
        else if(selectedButtonDisabilityText==null){
            Toast.makeText(requireActivity(),"Select disability",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedButtonDisabilityText=="Yes"){
            if(binding.etTypeOfDisability.text.isNullOrEmpty()){
                binding.etTypeOfDisability.error = "Specify the type of disability"
                return false
            }
        }

        return true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewEntryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewEntryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}