package com.example.bhartiye_jangannah.fragments.loginFragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.activities.HomePageActivity
import com.example.bhartiye_jangannah.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var loginBinding: FragmentLoginBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

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
        loginBinding= FragmentLoginBinding.inflate(layoutInflater,container,false)
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbRef= FirebaseDatabase.getInstance().getReference("Census Entry")
        auth= Firebase.auth
        loginBinding.tvForgot.setOnClickListener {
                findNavController().navigate(R.id.forgotFirstFragment)
        }
        loginBinding.btnLogin.setOnClickListener {
            if(loginBinding.etUsername.text.toString().isNullOrEmpty()){
                loginBinding.etUsername.error="Please Enter your username"
            }
            else if(loginBinding.etPassword.text.toString().isNullOrEmpty()){
                loginBinding.etPassword.error="Please Enter your password"
            }
            else{
                var email= loginBinding.etUsername.text.toString()
                var password = loginBinding.etPassword.text.toString()
//                if(username=="user"&&password=="1234"){
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                requireActivity(),
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
//                            updateUI(null)
                        }
                    }

            }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        var intent= Intent(requireActivity(), HomePageActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}