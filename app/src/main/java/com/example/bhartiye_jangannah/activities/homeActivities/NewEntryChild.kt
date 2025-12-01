package com.example.bhartiye_jangannah.activities.homeActivities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bhartiye_jangannah.modals.ChildEntry
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.activities.HomePageActivity
import com.example.bhartiye_jangannah.databinding.ActivityNewEntryChildBinding
import com.example.bhartiye_jangannah.databinding.SubmitLayoutBinding
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NewEntryChild : AppCompatActivity() {

    private lateinit var binding: ActivityNewEntryChildBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    var userId: String ?= null
    companion object {
        var id = 0
    }
    //    private var id = 0
    var age: Int ?= 0

    var childAadharNo : String?= null
    var mothersAadharNo : String?= null

    private var arrayList = ChildEntry()
    private var aadharCardList= arrayListOf<EditText>()
    private var aadharNumber: String= ""
    private var selectedButtonChildHasAadharText: String ?= null
    private var selectedButtonGenderText : String ?= null
    private var selectedItemReligion : String ?= null
    private var selectedItemCaste : String ?= null
    private var stringDate : String ?= null
    private var selectedItemState : String ?= null
    private var selectedButtonDisabilityText : String ?= null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityNewEntryChildBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.childHasAadharRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val selectedButtonHeadOfHousehold = findViewById<RadioButton>(checkedId)
            selectedButtonChildHasAadharText = selectedButtonHeadOfHousehold.text.toString()
        }

        val childHasAadharCheckedRadioButtonId= binding.childHasAadharRadioGroup.checkedRadioButtonId

        binding.childHasAadharRadioGroupYes.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.tvAadharNo.text ="Aadhar Number"
            } else {
                binding.tvAadharNo.text ="Aadhar Number of Mother"
            }
        }


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

        binding.genderRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val selectedButtonGender = findViewById<RadioButton>(checkedId)
            selectedButtonGenderText = selectedButtonGender.text.toString()
            Toast.makeText(this, "$selectedButtonGenderText", Toast.LENGTH_SHORT).show()
        }
        val genderCheckedRadioButtonId= binding.genderRadioGroup.checkedRadioButtonId


        //Religion spinner
        val adapterReligion: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
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
                            this@NewEntryChild,
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
            this,
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
                            this@NewEntryChild,
                            "Selected Item: $selectedItemCaste",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


        //date of birth date picker
        binding.constraintLayoutDatePicker.setOnClickListener {
//            val currentDate = Calendar.getInstance()
//            val dialog = DatePickerDialog(this)
//            dialog.setOnDateSetListener { view, year, month, dayOfMonth ->
//                val simpleDateFormat = SimpleDateFormat("dd MMM, yyyy")
//                val selectedDate = Calendar.getInstance()
//                selectedDate.set(Calendar.YEAR, year)
//                selectedDate.set(Calendar.MONTH, month)
//                selectedDate.set(Calendar.DATE, dayOfMonth)
//                //stringDate has the date of birth of the person
//                stringDate = simpleDateFormat.format(selectedDate.time)
//                binding.tvDatePicker.text = stringDate
//            }
//            dialog.show()
            val currentDate = Calendar.getInstance()

            val dialog = DatePickerDialog(this)
            dialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                age = currentDate.get(Calendar.YEAR) - selectedDate.get(Calendar.YEAR)

                if (currentDate.get(Calendar.DAY_OF_YEAR) < selectedDate.get(Calendar.DAY_OF_YEAR)) {
                    age = age!! - 1
                }

                // Now 'age' variable holds the calculated age
                stringDate = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(selectedDate.time)
                binding.tvDatePicker.text = stringDate
                binding.etAge.text= Editable.Factory.getInstance().newEditable(age.toString())

                // Display or use the calculated age
                println("Age: $age")
            }
            dialog.show()
        }
        val selectedYear = Calendar.YEAR
        val selectedMonth = Calendar.MONTH
        val selectedDay = Calendar.DAY_OF_MONTH


        //State spinner
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
                        println("$selectedItemState")
                        Toast.makeText(
                            this@NewEntryChild,
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
            val selectedButtonDisability = findViewById<RadioButton>(checkedId)
            selectedButtonDisabilityText = selectedButtonDisability.text.toString()
            Toast.makeText(this, "$selectedButtonDisabilityText", Toast.LENGTH_SHORT).show()
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


        dbRef= FirebaseDatabase.getInstance().getReference("Child Entry")
        auth= Firebase.auth



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

                NewEntryActivity.id++

                userId = FirebaseAuth.getInstance().currentUser?.uid

                Toast.makeText(this, userId, Toast.LENGTH_SHORT)
                    .show()
                dbRef.child(readAadhar())
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()) {
                                var dialog = AlertDialog.Builder(this@NewEntryChild)
                                dialog.setTitle("Already Exists")
                                dialog.setMessage("Aadhar Number already Exists!!\nDo you want to update this entry?")
                                dialog.setCancelable(false)
                                dialog.setPositiveButton("Yes"){
                                        _,_->
                                    val entries = ChildEntry(
                                        userId = userId,
                                        id = NewEntryActivity.id.toString(),
                                        name = binding.etName.text.toString(),
                                        childHasAadhar = selectedButtonChildHasAadharText,
                                        aadharNo = childAadharNo,
                                        mothersAadharNo = mothersAadharNo,
                                        fathersName = binding.etFathersName.text.toString(),
                                        mothersName = binding.etMothersName.text.toString(),
                                        gender = "$selectedButtonGenderText",
                                        religion = "$selectedItemReligion",
                                        caste = "$selectedItemCaste",
                                        dateOfBirth = "$stringDate",
                                        age = binding.etAge.text.toString(),
                                        address = binding.etAddress.text.toString(),
                                        district = binding.etDistrict.text.toString(),
                                        state = "$selectedItemState",
                                        birthmark = binding.etBirthmark.text.toString(),
                                        disability = "$selectedButtonDisabilityText",
                                        typeOfDisability = binding.etTypeOfDisability.text.toString(),
                                    )
                                    dbRef.child(readAadhar()).setValue(entries).addOnCompleteListener {

                                        val dialog = Dialog(this@NewEntryChild)
                                        val dialogBinding = SubmitLayoutBinding.inflate(layoutInflater)
                                        dialogBinding.tvSubmit.text = "Updated successfully"
                                        dialog.setContentView(dialogBinding.root)
                                        dialog.setCancelable(false)
                                        dialogBinding.btnGotoHome.setOnClickListener {
                                            val intent = Intent(this@NewEntryChild, HomePageActivity::class.java)
                                            startActivity(intent)
                                            this@NewEntryChild.finish()
                                            dialog.dismiss()
                                        }
                                        dialog.show()
                                    }
                                        .addOnFailureListener { err ->
                                            Toast.makeText(this@NewEntryChild, "Error: ${err.message}", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                }
                                dialog.setNegativeButton("No"){_, _->
                                }
                                dialog.show()
                            }
                            else{
                                val entries = ChildEntry(
                                    userId = userId,
                                    id = NewEntryActivity.id.toString(),
                                    name = binding.etName.text.toString(),
                                    childHasAadhar = selectedButtonChildHasAadharText,
                                    aadharNo = childAadharNo,
                                    mothersAadharNo = mothersAadharNo,
                                    fathersName = binding.etFathersName.text.toString(),
                                    mothersName = binding.etMothersName.text.toString(),
                                    gender = "$selectedButtonGenderText",
                                    religion = "$selectedItemReligion",
                                    caste = "$selectedItemCaste",
                                    dateOfBirth = "$stringDate",
                                    age = binding.etAge.text.toString(),
                                    address = binding.etAddress.text.toString(),
                                    district = binding.etDistrict.text.toString(),
                                    state = "$selectedItemState",
                                    birthmark = binding.etBirthmark.text.toString(),
                                    disability = "$selectedButtonDisabilityText",
                                    typeOfDisability = binding.etTypeOfDisability.text.toString(),
                                )
                                userId= FirebaseAuth.getInstance().currentUser?.uid
                                dbRef.child(readAadhar()).setValue(entries).addOnCompleteListener {

                                    val dialog = Dialog(this@NewEntryChild)
                                    val dialogBinding = SubmitLayoutBinding.inflate(layoutInflater)
                                    dialog.setContentView(dialogBinding.root)
                                    dialog.setCancelable(false)
                                    dialogBinding.btnGotoHome.setOnClickListener {
                                        val intent = Intent(this@NewEntryChild, HomePageActivity::class.java)
                                        startActivity(intent)
                                        this@NewEntryChild.finish()
                                        dialog.dismiss()
                                    }
                                    dialog.show()
                                }
                                    .addOnFailureListener { err ->
                                        Toast.makeText(this@NewEntryChild, "Error: ${err.message}", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@NewEntryChild,
                                "Error occurred: ${error.toException()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                if(arrayList == null) {

                    println("After functionnnnn")
                }
                else{

                    Toast.makeText(this, "Already exists", Toast.LENGTH_SHORT).show()
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

    private fun isValid(): Boolean {
        if(binding.etName.text.isNullOrEmpty()){
            binding.etName.error= "Enter the name"
            return false
        }
        else if(selectedButtonChildHasAadharText==null){
            Toast.makeText(this,"Select head of family",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.etAadharDigit1.text.isNullOrEmpty()||binding.etAadharDigit2.text.isNullOrEmpty()||
            binding.etAadharDigit3.text.isNullOrEmpty()||binding.etAadharDigit4.text.isNullOrEmpty()||
            binding.etAadharDigit5.text.isNullOrEmpty()||binding.etAadharDigit6.text.isNullOrEmpty()||
            binding.etAadharDigit7.text.isNullOrEmpty()||binding.etAadharDigit8.text.isNullOrEmpty()||
            binding.etAadharDigit9.text.isNullOrEmpty()||binding.etAadharDigit10.text.isNullOrEmpty()||
            binding.etAadharDigit11.text.isNullOrEmpty()||binding.etAadharDigit12.text.isNullOrEmpty()){
            Toast.makeText(this,"Enter AADHAR Number",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedButtonChildHasAadharText=="Yes"){
            childAadharNo=readAadhar()
            mothersAadharNo= null
        }
        else if(selectedButtonChildHasAadharText=="No"){
            mothersAadharNo=readAadhar()
            childAadharNo= null
        }
        else if(selectedButtonGenderText==null){
            Toast.makeText(this,"Select gender",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.etFathersName.text.isNullOrEmpty()){
            binding.etFathersName.error = "Enter father's name"
            return false
        }
        else if(binding.etMothersName.text.isNullOrEmpty()){
            binding.etMothersName.error = "Enter mother's name"
            return false
        }
        else if(selectedItemReligion==null){
            Toast.makeText(this,"Select religion",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedItemCaste==null){
            Toast.makeText(this,"Select caste",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(stringDate==null){
            Toast.makeText(this,"Select date of birth",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.etAge.text.isNullOrEmpty()){
            binding.etAge.error = "Enter the age"
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
            Toast.makeText(this,"Select state",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.etBirthmark.text.isNullOrEmpty()){
            binding.etBirthmark.error = "Specify the birthmark"
            return false
        }
        else if(selectedButtonDisabilityText==null){
            Toast.makeText(this,"Select disability",Toast.LENGTH_SHORT).show()
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

}