package com.example.bhartiye_jangannah.fragments.deleteFragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.databinding.FragmentDeleteEntry1Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DeleteEntryFragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeleteEntryFragment1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentDeleteEntry1Binding
    private var aadharCardList= arrayListOf<EditText>()

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
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDeleteEntry1Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.btnDelete.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("aadharNoMilGya", "${readAadhar()}")
            findNavController().navigate(R.id.deleteEntryFragment2, bundle)
        }
    }

    private fun readAadhar(): String {
        val aadharNumber = StringBuilder()
        for (editText in aadharCardList) {
            aadharNumber.append(editText.text)
        }
        return aadharNumber.toString()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DeleteEntryFragment1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeleteEntryFragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}