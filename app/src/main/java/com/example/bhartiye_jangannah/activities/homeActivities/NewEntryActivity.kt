package com.example.bhartiye_jangannah.activities.homeActivities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
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
import com.example.bhartiye_jangannah.databinding.ActivityNewEntryBinding
import com.example.bhartiye_jangannah.databinding.SubmitLayoutBinding
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class NewEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewEntryBinding
    private lateinit var storedVerificationId: String

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Auto verification (optional)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@NewEntryActivity, e.message, Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            storedVerificationId = verificationId
            Toast.makeText(this@NewEntryActivity, "OTP Sent", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    var fileUri: Uri?= null
    private lateinit var storageRef: StorageReference

    var userId: String ?= null

    companion object {
        var id = 0
        const val PICK_IMAGE_REQUEST = 1
    }
//    private var id = 0
    var age: Int ?= 0

    private var arrayList = CensusEntry()
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
        binding = ActivityNewEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        //Head of household radio group
        binding.headRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val selectedButtonHeadOfHousehold = findViewById<RadioButton>(checkedId)
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
                        println("$selectedItemReligion")
                        Toast.makeText(
                            this@NewEntryActivity,
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
                            this@NewEntryActivity,
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
                            this@NewEntryActivity,
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
                            this@NewEntryActivity,
                            "Please select marital status",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
                binding.etAge.isEnabled= false
                binding.etAge.isFocusable= false
                binding.etAge.isCursorVisible= false
                binding.etAge.keyListener= null
                binding.etAge.setTextColor(Color.BLACK)
                // Display or use the calculated age
                println("Age: $age")
            }
            dialog.show()
        }
        val selectedYear = Calendar.YEAR
        val selectedMonth = Calendar.MONTH
        val selectedDay = Calendar.DAY_OF_MONTH
        val isValidDate = validateDate(selectedYear, selectedMonth, selectedDay)

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
                            this@NewEntryActivity,
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

        dbRef= FirebaseDatabase.getInstance().getReference("Census Entry")
        storageRef = Firebase.storage.reference

        binding.btnChoose.setOnClickListener {
//            val intent = Intent()
//            intent.type= "image/*"
//            intent.action=Intent.ACTION_GET_CONTENT
//            startActivityForResult(
//                Intent.createChooser(intent,"Choose image"),0
//            )
            selectImage()
        }
//        binding.btnUpload.setOnClickListener {
//            if(fileUri != null){
//                uploadImage()
//            }
//            else{
//                Toast.makeText(applicationContext, "Please select Image to upload",Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.tvVerify.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.tvVerify.setOnClickListener {
            val phone = binding.etPhoneNo.text.toString()
            sendOTP(phone)

            binding.layoutOTP.visibility = View.VISIBLE
            binding.btnVerify.visibility = View.VISIBLE
            binding.tvVerify.visibility = View.GONE
        }
        binding.btnVerify.setOnClickListener{
            val otp = binding.etOtp.text.toString()
            verifyOTP(otp)

            binding.layoutOTP.visibility = View.GONE
            binding.btnVerify.visibility = View.GONE
            binding.tvVerify.text = "Verified"
            binding.tvVerify.visibility = View.VISIBLE
            binding.tvVerify.isClickable = false
        }
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
                id++

                userId = FirebaseAuth.getInstance().currentUser?.uid

                Toast.makeText(this, userId, Toast.LENGTH_SHORT)
                    .show()
                dbRef.child(readAadhar())
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()) {
                                var dialog = AlertDialog.Builder(this@NewEntryActivity)
                                dialog.setTitle("Already Exists")
                                dialog.setMessage("Aadhar Number already Exists!!\nDo you want to update this entry?")
                                dialog.setCancelable(false)
                                dialog.setPositiveButton("Yes"){
                                    _,_->
                                    if(fileUri!=null) {
                                        uploadImage(fileUri!!)
                                    }

                                    val entries = CensusEntry(
                                        userId = userId,
                                        id = id.toString(),
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
                                        typeOfDisability = binding.etTypeOfDisability.text.toString()
//                                        imageUrl = fileUri.toString()
                                    )
                                    dbRef.child(readAadhar()).setValue(entries).addOnCompleteListener {

                                        val dialog = Dialog(this@NewEntryActivity)
                                        val dialogBinding = SubmitLayoutBinding.inflate(layoutInflater)
                                        dialogBinding.tvSubmit.text = "Updated successfully"
                                        dialog.setContentView(dialogBinding.root)
                                        dialog.setCancelable(false)
                                        dialogBinding.btnGotoHome.setOnClickListener {
                                            val intent = Intent(this@NewEntryActivity, HomePageActivity::class.java)
                                            startActivity(intent)
                                            this@NewEntryActivity.finish()
                                            dialog.dismiss()
                                        }
                                        dialog.show()
                                    }
                                        .addOnFailureListener { err ->
                                            Toast.makeText(this@NewEntryActivity, "Error: ${err.message}", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                }
                                dialog.setNegativeButton("No"){_, _->
                                }
                                dialog.show()
                            }
                            else{
                                if(fileUri!=null) {
                                    uploadImage(fileUri!!)
                                }
                                val entries = CensusEntry(
                                    userId= userId,
                                    id = id.toString(),
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
//                                    imageUrl = fileUri.toString()
                                )

                                userId= FirebaseAuth.getInstance().currentUser?.uid
                                dbRef.child(readAadhar()).setValue(entries).addOnSuccessListener {

                                    val dialog = Dialog(this@NewEntryActivity)
                                    val dialogBinding = SubmitLayoutBinding.inflate(layoutInflater)
                                    dialog.setContentView(dialogBinding.root)
                                    dialog.setCancelable(false)
                                    dialogBinding.btnGotoHome.setOnClickListener {
                                        val intent = Intent(this@NewEntryActivity, HomePageActivity::class.java)
                                        startActivity(intent)
                                        this@NewEntryActivity.finish()
                                        dialog.dismiss()
                                    }
                                    dialog.show()
                                }
                                    .addOnFailureListener { err ->
                                        Toast.makeText(this@NewEntryActivity, "Error: ${err.message}", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@NewEntryActivity,
                                "Error occurred: ${error.toException()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
//                if(arrayList == null) {
//                    println("After functionnnnn")
//                }
//                     else{
//                    Toast.makeText(this, "Already exists", Toast.LENGTH_SHORT).show()
//                }
            }
            else{
//                 Toast.makeText(this, "Fill the details first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri= data.data!!
            binding.imageView.setImageURI(fileUri)
            binding.imageView.visibility=View.VISIBLE
//            uploadImage(fileUri!!)
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


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == 0 && resultCode == RESULT_OK && data!= null && data.data!=null){
//            fileUri= data.data
//            try{
//                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
//                binding.imageView.setImageBitmap(bitmap)
//            } catch (e:Exception){
//                Log.e("Exception", "Error:$e")
//            }
//        }
//    }

//    private fun uploadImage(){
//        if(fileUri != null){
//            val progressDialog = ProgressDialog(this)
//            progressDialog.setTitle("Uploading Image")
//            progressDialog.setMessage("Processing....")
//            progressDialog.show()
//
//            val ref: StorageReference= FirebaseStorage.getInstance().getReference()
//                .child(UUID.randomUUID().toString())
//            ref.putFile(fileUri!!).addOnSuccessListener {
//                progressDialog.dismiss()
//                Toast.makeText(applicationContext, "File uploaded successfully",Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//                progressDialog.dismiss()
//                Toast.makeText(applicationContext, "File not uploaded successfully",Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun aadharExists() {
        dbRef= FirebaseDatabase.getInstance().getReference("Census Entry")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(entrySnap in snapshot.children){
                        val entry = entrySnap.getValue(CensusEntry::class.java)
                        if(entry!=null) {
                            if(entry.aadharNo == readAadhar()){
                                println()
                                arrayList=entry
                                println(arrayList)
                            }
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
            Toast.makeText(this,"Select head of family",Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this,"Enter AADHAR Number",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedButtonGenderText==null){
            Toast.makeText(this,"Select gender",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.etFathersName.text.isNullOrEmpty()){
            binding.etFathersName.error = "Enter father's name"
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
        else if(selectedButtonLiteracyStatus==null){
            Toast.makeText(this,"Select literacy status",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(selectedButtonLiteracyStatus=="Literate"){
            if(binding.etLastClassStudied.text.isNullOrEmpty()) {
                binding.etLastClassStudied.error = "Enter last class studied"
                return false
            }
        }
        else if(selectedItemMaritalStatus==null){
            Toast.makeText(this,"Select marital status",Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this,"Select date of birth",Toast.LENGTH_SHORT).show()
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
    private fun sendOTP(phone: String) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$phone")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun verifyOTP(otp: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp)

        // Just verifying – no login required
        if (credential.smsCode == otp) {
            Toast.makeText(this, "OTP Verified Successfully!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Invalid OTP!", Toast.LENGTH_LONG).show()
        }
    }

}