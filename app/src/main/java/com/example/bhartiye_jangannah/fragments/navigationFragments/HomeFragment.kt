package com.example.bhartiye_jangannah.fragments.navigationFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bhartiye_jangannah.activities.homeActivities.AadharDetailsActivity
import com.example.bhartiye_jangannah.activities.homeActivities.AdultPopulationActivity
import com.example.bhartiye_jangannah.activities.homeActivities.CasteWiseDetailsActivity
import com.example.bhartiye_jangannah.activities.homeActivities.ChildPopulationDetailsActivity
import com.example.bhartiye_jangannah.activities.homeActivities.DeleteEntryActivity
import com.example.bhartiye_jangannah.activities.homeActivities.DisabilityListActivity
import com.example.bhartiye_jangannah.activities.homeActivities.GovtOfIndiaActivity
import com.example.bhartiye_jangannah.activities.homeActivities.IntroductionActivity
import com.example.bhartiye_jangannah.activities.homeActivities.LiteracyRateActivity
import com.example.bhartiye_jangannah.activities.homeActivities.MaritalStatusActivity
import com.example.bhartiye_jangannah.activities.homeActivities.NewEntryActivity
import com.example.bhartiye_jangannah.activities.homeActivities.NewEntryChild
import com.example.bhartiye_jangannah.activities.homeActivities.PopulationDetailsActivity
import com.example.bhartiye_jangannah.activities.homeActivities.ReligionWiseDetailsActivity
import com.example.bhartiye_jangannah.activities.homeActivities.SexRatioActivity
import com.example.bhartiye_jangannah.activities.homeActivities.UpdateDetailsActivity
import com.example.bhartiye_jangannah.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var fragmentHomeBinding: FragmentHomeBinding

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
        fragmentHomeBinding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding.linearLayoutIntroduction.setOnClickListener {
            var intent = Intent(requireActivity(), IntroductionActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutNewEntry.setOnClickListener{
            var intent= Intent(requireActivity(), NewEntryActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutPopulationDetails.setOnClickListener {
            var intent= Intent(requireActivity(), PopulationDetailsActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutUpdateDetails.setOnClickListener {
            var intent= Intent(requireActivity(), UpdateDetailsActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutDeleteEntry.setOnClickListener {
            var intent= Intent(requireActivity(), DeleteEntryActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutSexRatio.setOnClickListener {
            var intent= Intent(requireActivity(), SexRatioActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutReligionWiseDetails.setOnClickListener {
            var intent= Intent(requireActivity(), ReligionWiseDetailsActivity::class.java)
            startActivity(intent)
        }

        fragmentHomeBinding.linearLayoutCasteWiseDetails.setOnClickListener {
            var intent= Intent(requireActivity(), CasteWiseDetailsActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutMaritalStatus.setOnClickListener {
            var intent= Intent(requireActivity(), MaritalStatusActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutDisabilityList.setOnClickListener {
            var intent = Intent(requireActivity(), DisabilityListActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutLiteracyRate.setOnClickListener{
            var intent= Intent(requireActivity(), LiteracyRateActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutAdultPopulation.setOnClickListener {
            var intent= Intent(requireActivity(), AdultPopulationActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutAadharDetails.setOnClickListener {
            var intent = Intent(requireActivity(), AadharDetailsActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutGovtOfIndia.setOnClickListener {
            var intent = Intent(requireActivity(), GovtOfIndiaActivity::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutNewEntryChild.setOnClickListener {
            var intent = Intent(requireActivity(), NewEntryChild::class.java)
            startActivity(intent)
        }
        fragmentHomeBinding.linearLayoutPopulationDetailsChild.setOnClickListener {
            var intent = Intent(requireActivity(), ChildPopulationDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}