package com.example.bhartiye_jangannah.fragments.navigationFragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bhartiye_jangannah.activities.EditProfileActivity
import com.example.bhartiye_jangannah.activities.MainActivity
import com.example.bhartiye_jangannah.databinding.FragmentProfileBinding
import com.example.bhartiye_jangannah.modals.CensusEntry
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentProfileBinding
    private lateinit var dbRef : DatabaseReference
    lateinit var auth: FirebaseAuth
    private var arrayList = CensusEntry()
    private var imageUri: Uri? = null

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
        binding= FragmentProfileBinding.inflate(layoutInflater, container, false)
        dbRef = FirebaseDatabase.getInstance().getReference("Census Entry")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEditProfile.setOnClickListener {
            var intent= Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        auth= Firebase.auth
        binding.btnLogOut.setOnClickListener {
            var dialog = AlertDialog.Builder(requireActivity())
            dialog.setTitle("Log Out")
            dialog.setMessage("Are you sure you want to sign out? ")
            dialog.setCancelable(false)
            dialog.setPositiveButton("Yes") { _, _ ->
                Toast.makeText(requireActivity(),"Log out Successful", Toast.LENGTH_SHORT).show()
                auth.signOut()
                var intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            dialog.setNegativeButton("No") { _, _ ->
            }
            dialog.show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}