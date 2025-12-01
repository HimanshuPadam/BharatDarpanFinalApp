package com.example.bhartiye_jangannah.activities.homeActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.databinding.ActivityUpdateDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UpdateDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateDetailsBinding
    private var aadharCardList= arrayListOf<EditText>()
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbRef= FirebaseDatabase.getInstance().getReference("Census Entry")
        binding = ActivityUpdateDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }

                override fun afterTextChanged(p0: Editable?) {
                    if(p0?.length == 1 && index < aadharCardList.size - 1){
                        aadharCardList[index+1].requestFocus()
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

        binding.btnUpdate.setOnClickListener {
            var intent= Intent(this, UpdateDetails2Activity::class.java)
            intent.putExtra("aadharNo", readAadhar())
            startActivity(intent)
            this.finish()
        }
    }
    //function to concatenate or append aadhar digits into 1 variable
    private fun readAadhar(): String {
        val aadharNumber = StringBuilder()
        for (editText in aadharCardList) {
            aadharNumber.append(editText.text)
        }
        return aadharNumber.toString()
    }
}