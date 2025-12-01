package com.example.bhartiye_jangannah.activities.homeActivities

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
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
import androidx.annotation.RequiresApi
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.activities.HomePageActivity
import com.example.bhartiye_jangannah.databinding.ActivityUpdateDetails2Binding
import com.example.bhartiye_jangannah.databinding.SubmitLayoutBinding
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateDetails2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateDetails2Binding
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    var censusEntry : Map <String, Any> ?= null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    var fileUri: Uri?= null
    var age: Int ?= 0
    private var id: String?= null
    private var lastClassStudied: String ?= null
    private var ageAtMarriage: String ?= null
    private var typeOfDisability: String ?= null
    private var aadharCardNo= listOf<String>()
    private var arrayList = CensusEntry()
    private var aadharNoMilGya : String ?= null
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDetails2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= Firebase.auth
        dbRef= FirebaseDatabase.getInstance().getReference("Census Entry")
        storageRef = FirebaseStorage.getInstance().reference
        aadharNoMilGya = intent.getStringExtra("aadharNo")
        aadharCardNo= aadharNoMilGya!!.split("")
        getDetailsToBeUpdated()

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

        //Head of household radio group
        binding.headRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val selectedButtonHeadOfHousehold = findViewById<RadioButton>(checkedId)
            selectedButtonHeadOfHouseholdText = selectedButtonHeadOfHousehold.text.toString()
            binding.tvErrorHead.visibility= View.GONE
        }
//        val checkedRadioButton = findCheckedRadioButton(binding.headRadioGroup)
        val headCheckedRadioButtonId= binding.headRadioGroup.checkedRadioButtonId

        binding.headRadioGroupYes.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.layoutRelationshipWithHead.visibility = View.GONE
                binding.etRelationshipWithHead.visibility = View.GONE
            } else {
                binding.layoutRelationshipWithHead.visibility = View.VISIBLE
                binding.etRelationshipWithHead.visibility = View.VISIBLE
            }
        }

        aadharCardList.clear()
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
        println(aadharCardList)

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
            val selectedButtonGender = findViewById<RadioButton>(checkedId)
            selectedButtonGenderText = selectedButtonGender.text.toString()
            Toast.makeText(this, "$selectedButtonGenderText", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(
                            this@UpdateDetails2Activity,
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
                            this@UpdateDetails2Activity,
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
            val selectedButtonCurrentlyStudying = findViewById<RadioButton>(checkedId)
            selectedButtonLiteracyStatus = selectedButtonCurrentlyStudying.text.toString()
            Toast.makeText(this, "$selectedButtonLiteracyStatus", Toast.LENGTH_SHORT).show()
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
                            this@UpdateDetails2Activity,
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
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    if(binding.spinnerMaritalStatus.selectedItem=="--Select--") {
                        Toast.makeText(
                            this@UpdateDetails2Activity,
                            "Please select marital status",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        //date of birth date picker
        binding.constraintLayoutDatePicker.setOnClickListener {
//            val dialog = DatePickerDialog(this)
//            dialog.setOnDateSetListener { view, year, month, dayOfMonth ->
//                val simpleDateFormat = SimpleDateFormat("dd MMM, yyyy")
//                val calendar = Calendar.getInstance()
//                calendar.set(Calendar.YEAR, year)
//                calendar.set(Calendar.MONTH, month)
//                calendar.set(Calendar.DATE, dayOfMonth)
//                //stringDate has the date of birth of the person
//                stringDate = simpleDateFormat.format(calendar.time)
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
                        Toast.makeText(
                            this@UpdateDetails2Activity,
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

        binding.btnChoose.setOnClickListener{
            selectImage()
        }

        binding.btnUpdate.setOnClickListener {
            //to get the aadhar number in single variable
            aadharCardList.forEachIndexed { index, editText ->
                editText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                        // Read the Aadhar number from the EditText fields
                        aadharNumber = readAadhar()
                        // we can perform Aadhar verification or submit action here
                        editText.clearFocus()
                    }
                    false
                }
            }
            if (isValid()) {
                val relationshipWithHead = binding.etRelationshipWithHead.text.toString()

                dbRef = FirebaseDatabase.getInstance().getReference("Census Entry")

                var userId = FirebaseAuth.getInstance().currentUser?.uid

                if(stringDate == null){
                    stringDate = censusEntry?.get("dateOfBirth").toString()
                }
                if(fileUri != null) {
                    uploadImage(fileUri!!)
                }
                else{
                    fileUri = censusEntry?.get("imageUrl") as Uri?
                }
                val censusEntry = CensusEntry(
                    userId,
                    id,
                    binding.etName.text.toString(),
                    selectedButtonHeadOfHouseholdText,
                    binding.etFathersName.text.toString(),
                    relationshipWithHead,
                    readAadhar(),
                    selectedButtonGenderText,
                    selectedItemReligion,
                    selectedItemCaste,
                    selectedButtonLiteracyStatus,
                    lastClassStudied,
                    selectedItemMaritalStatus,
                    ageAtMarriage,
                    binding.etMotherTongue.text.toString(),
                    binding.etOtherLanguages.text.toString(),
                    stringDate,
                    binding.etAge.text.toString(),
                    binding.etOccupation.text.toString(),
                    binding.etAddress.text.toString(),
                    binding.etDistrict.text.toString(),
                    selectedItemState,
                    binding.etBirthmark.text.toString(),
                    selectedButtonDisabilityText,
                    typeOfDisability,
                    fileUri.toString()
                )

                dbRef.child(readAadhar()).setValue(censusEntry).addOnCompleteListener {
                    val dialog = Dialog(this)
                    val dialogBinding = SubmitLayoutBinding.inflate(layoutInflater)
                    dialogBinding.tvSubmit.text = "Updated successfully"
                    dialog.setContentView(dialogBinding.root)
                    dialog.setCancelable(false)
                    dialogBinding.btnGotoHome.setOnClickListener {
                        val intent = Intent(this, HomePageActivity::class.java)
                        startActivity(intent)
                        this.finish()
                        dialog.dismiss()
                    }
                    dialog.show()
                }
                    .addOnFailureListener { err ->
                        Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            else{
                Toast.makeText(this, "Fill the details first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val imageRef = storageRef.child("images").child(readAadhar())
//        val imageRef = storageRef
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading Image")
        progressDialog.setMessage("Processing....")
        progressDialog.show()
        imageRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
            Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            imageRef.downloadUrl.addOnSuccessListener { uri ->
//                if (task.isSuccessful) {
                val imageUrl = uri.toString()
                try {
                    dbRef.child(readAadhar()).child("imageUrl").setValue(imageUrl)
                        .addOnSuccessListener {
                            // Image URL stored successfully in database
                            Toast.makeText(this, "Image stored successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            // Handle database error
                            Toast.makeText(this, "Failed to store image URL: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                } catch (e: Exception) {
                    // Handle serialization error
                    Toast.makeText(this, "Failed to serialize image URL: ${e.message}", Toast.LENGTH_SHORT).show()
                }
//                    dbRef.child(readAadhar()).child("imageUrl").setValue(imageUrl).addOnSuccessListener {
//                        // Image URL stored successfully in database
//                        Toast.makeText(this, "Image stored successfully", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this, "Image not stored successfully", Toast.LENGTH_SHORT).show()
//                    // Handle download URL retrieval failure
//                }
            }
            progressDialog.dismiss()
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Failed to upload image: ${exception.message}", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, NewEntryActivity.PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NewEntryActivity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri= data.data!!
            binding.imageView.setImageURI(fileUri)
            binding.imageView.visibility=View.VISIBLE
//            uploadImage(fileUri!!)
        }
    }

    private fun getDetailsToBeUpdated(){
//        var censusEntry : Map <String, Any> ?= null
        dbRef = FirebaseDatabase.getInstance().getReference("Census Entry")
        dbRef.child(aadharNoMilGya!!).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    censusEntry = snapshot.value as? Map<String, Any>
                    println(censusEntry?.get("name"))
                    if (censusEntry != null) {
                        // Iterate over the children to retrieve keys and values
                        for ((key, value) in censusEntry!!) {
                            println("$key: $value")
                        }

                    } else {
                        println("Error: Unable to parse census entry data")
                    }
//                    var matchedEntry : Any = snapshot.value!!
//                    var name = (matchedEntry as? String ?: "")
//                    println()
//                    arrayList= snapshot.value as CensusEntry
//                    for(entrySnap in snapshot.children){
//                        val entry = entrySnap.getValue(CensusEntry::class.java)
//                        if (entry != null) {
//                            if(entry.aadharNo==aadharNoMilGya) {
//                                arrayList=entry
//                                System.out.println(arrayList)
//                            }
//                        }
//                    }

                    if (censusEntry != null) {
                        binding.etName.text =
                            Editable.Factory.getInstance().newEditable(censusEntry?.get("name").toString())

                        if (censusEntry?.get("headOfFamily").toString() == "No") {
                            binding.headRadioGroupYes.isChecked = false
                            binding.headRadioGroupNo.isChecked = true
                            binding.etRelationshipWithHead.text = Editable.Factory.getInstance()
                                .newEditable(censusEntry?.get("relationshipWithHead").toString())
                        } else {
                            binding.headRadioGroupNo.isChecked = false
                            binding.headRadioGroupYes.isChecked = true
                        }
                        binding.etAadharDigit1.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[1])
                        binding.etAadharDigit2.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[2])
                        binding.etAadharDigit3.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[3])
                        binding.etAadharDigit4.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[4])
                        binding.etAadharDigit5.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[5])
                        binding.etAadharDigit6.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[6])
                        binding.etAadharDigit7.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[7])
                        binding.etAadharDigit8.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[8])
                        binding.etAadharDigit9.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[9])
                        binding.etAadharDigit10.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[10])
                        binding.etAadharDigit11.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[11])
                        binding.etAadharDigit12.text =
                            Editable.Factory.getInstance().newEditable(aadharCardNo[12])

                        when (censusEntry?.get("gender").toString()) {
                            "Male" -> {
                                binding.genderRadioGroupMale.isChecked = true
                            }

                            "Female" -> {
                                binding.genderRadioGroupFemale.isChecked = true
                            }

                            else -> {
                                binding.genderRadioGroupOthers.isChecked = true
                            }
                        }

                        binding.etFathersName.text =
                            Editable.Factory.getInstance().newEditable(censusEntry?.get("fathersName").toString())

                        binding.spinnerReligion.setSelection(
                            resources.getStringArray(R.array.religion_array)
                                .indexOfFirst { element ->
                                    element.equals(censusEntry?.get("religion").toString())
                                }, true
                        )

                        binding.spinnerCaste.setSelection(
                            resources.getStringArray(R.array.castes_array)
                                .indexOfFirst { element ->
                                    element.equals(censusEntry?.get("caste").toString())
                                }, true
                        )

                        if (censusEntry?.get("literacyStatus").toString() == "Literate") {
                            binding.etLastClassStudied.text =
                                Editable.Factory.getInstance()
                                    .newEditable(censusEntry?.get("lastClassStudied").toString())
                            binding.literacyStatusRadioGroupLiterate.isChecked = true
                        } else {
                            binding.literacyStatusRadioGroupIlliterate.isChecked = true
                        }

                        binding.spinnerMaritalStatus.setSelection(
                            resources.getStringArray(R.array.marital_status_array)
                                .indexOfFirst { element ->
                                    element.equals(censusEntry?.get("maritalStatus").toString())
                                }, true
                        )
                        if (censusEntry?.get("maritalStatus").toString() != "Unmarried") {
                            binding.etAgeAtMarriage.text =
                                Editable.Factory.getInstance().newEditable(censusEntry?.get("ageAtMarriage").toString())
                        }
                        binding.etMotherTongue.text =
                            Editable.Factory.getInstance().newEditable(censusEntry?.get("motherTongue").toString())
                        binding.etOtherLanguages.text =
                            Editable.Factory.getInstance()
                                .newEditable(censusEntry?.get("otherLanguagesKnown").toString())

                        //dob pending
                        binding.tvDatePicker.text =
                            Editable.Factory.getInstance().newEditable(censusEntry?.get("dateOfBirth").toString())

                        binding.etAge.text =
                            Editable.Factory.getInstance().newEditable(censusEntry?.get("age").toString())
                        binding.etOccupation.text =
                            Editable.Factory.getInstance().newEditable(censusEntry?.get("occupation").toString())
                        binding.etAddress.text =
                            Editable.Factory.getInstance().newEditable(censusEntry?.get("address").toString())
                        binding.etDistrict.text =
                            Editable.Factory.getInstance().newEditable(censusEntry?.get("district").toString())
                        binding.spinnerState.setSelection(
                            resources.getStringArray(R.array.states_array)
                                .indexOfFirst { element ->
                                    element.equals(censusEntry?.get("state").toString())
                                }, true
                        )
                        binding.etBirthmark.text =
                            Editable.Factory.getInstance().newEditable(censusEntry?.get("birthmark").toString())
                        if (censusEntry?.get("disability").toString() == "Yes") {
                            binding.etTypeOfDisability.text =
                                Editable.Factory.getInstance()
                                    .newEditable(censusEntry?.get("typeOfDisability").toString())
                            binding.disabilityRadioGroupYes.isChecked = true
                        } else {
                            binding.disabilityRadioGroupNo.isChecked = true
                        }
                        val imageRef = storageRef.child("images/$aadharNoMilGya")
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                                Picasso.get().load(uri).into(binding.imageView)
                        }
                            .addOnFailureListener {exception ->
                                Toast.makeText(this@UpdateDetails2Activity, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun readAadhar(): String {
        val aadharNumber = StringBuilder()
        aadharNumber.clear()
        for (editText in aadharCardList) {
            aadharNumber.append(editText.text)
        }
        return aadharNumber.toString()
    }
    private fun isValid(): Boolean {
        if(binding.etName.text.isNullOrEmpty()){
            binding.etName.error= "Enter the name"
            return false
        } else if(binding.etAadharDigit1.text.isNullOrEmpty()||binding.etAadharDigit2.text.isNullOrEmpty()||
            binding.etAadharDigit3.text.isNullOrEmpty()||binding.etAadharDigit4.text.isNullOrEmpty()||
            binding.etAadharDigit5.text.isNullOrEmpty()||binding.etAadharDigit6.text.isNullOrEmpty()||
            binding.etAadharDigit7.text.isNullOrEmpty()||binding.etAadharDigit8.text.isNullOrEmpty()||
            binding.etAadharDigit9.text.isNullOrEmpty()||binding.etAadharDigit10.text.isNullOrEmpty()||
            binding.etAadharDigit11.text.isNullOrEmpty()||binding.etAadharDigit12.text.isNullOrEmpty()){
            binding.tvErrorAadhar.visibility= View.VISIBLE
            return false
        } else if(binding.etFathersName.text.isNullOrEmpty()){
            binding.tvErrorAadhar.visibility= View.GONE
            binding.etFathersName.error = "Enter father's name"
            return false
        } else if(binding.spinnerMaritalStatus.selectedItem != "Unmarried"){
            if(binding.etAgeAtMarriage.text.isNullOrEmpty()){
                binding.etAgeAtMarriage.error= "Enter the age at the time of marriage"
                return false
            }
        } else if(binding.etMotherTongue.text.isNullOrEmpty()){
            binding.etMotherTongue.error = "Enter the mother tongue"
            return false
        } else if(binding.etOtherLanguages.text.isNullOrEmpty()){
            binding.etOtherLanguages.error = "Enter other languages known"
            return false
        } else if(binding.etAge.text.isNullOrEmpty()){
            binding.etAge.error = "Enter the age"
            return false
        } else if(binding.etOccupation.text.isNullOrEmpty()){
            binding.etOccupation.error = "Enter the occupation"
            return false
        } else if(binding.etAddress.text.isNullOrEmpty()){
            binding.etAddress.error = "Enter the address"
            return false
        } else if(binding.etDistrict.text.isNullOrEmpty()){
            binding.etDistrict.error = "Enter the district"
            return false
        } else if(binding.etBirthmark.text.isNullOrEmpty()){
            binding.etBirthmark.error = "Specify the birthmark"
            return false
        } else if(binding.disabilityRadioGroupYes.isChecked){
            if(binding.etTypeOfDisability.text.isNullOrEmpty()){
                binding.etTypeOfDisability.error = "Specify the type of disability"
                return false
            }
        }
        relationshipWithHead = if(selectedButtonHeadOfHouseholdText=="Yes"){
            binding.etRelationshipWithHead.text.toString()
        } else{
            null
        }
        lastClassStudied= if(selectedButtonLiteracyStatus == "Literate"){
            binding.etLastClassStudied.text.toString()
        } else{
            null
        }
        ageAtMarriage= if(selectedItemMaritalStatus!= "Unmarried"){
            binding.etAgeAtMarriage.text.toString()
        } else{
            null
        }
        return true
    }
}