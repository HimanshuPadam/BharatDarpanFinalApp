package com.example.bhartiye_jangannah.fragments.loginFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.databinding.FragmentForgotFirstBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotFirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotFirstFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentForgotFirstBinding
    private lateinit var auth: FirebaseAuth
    val emailPattern = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex()

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
        binding= FragmentForgotFirstBinding.inflate(layoutInflater,container,false)
        auth= FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOtp.setOnClickListener {
            if(binding.etEmail.text.toString().trim().matches(emailPattern)){
                auth.sendPasswordResetEmail(binding.etEmail.text.toString().trim())
                    .addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Please check your email", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
                    }
                findNavController().navigate(R.id.loginFragment)
            }
            else {
                binding.etEmail.error="Please enter valid Email address"
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForgotFirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgotFirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}