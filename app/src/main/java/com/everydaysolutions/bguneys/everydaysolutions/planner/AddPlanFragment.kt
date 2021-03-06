package com.everydaysolutions.bguneys.everydaysolutions.planner

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.everydaysolutions.bguneys.everydaysolutions.R
import com.everydaysolutions.bguneys.everydaysolutions.database.Plan
import com.everydaysolutions.bguneys.everydaysolutions.database.PlanRepository
import com.everydaysolutions.bguneys.everydaysolutions.databinding.FragmentAddPlanBinding

class AddPlanFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentAddPlanBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddPlanBinding.inflate(inflater, container, false)

        val mRepository = PlanRepository.getInstance(requireContext())
        val planViewModelFactory = PlanViewModelFactory(mRepository!!)
        val planViewModel = ViewModelProvider(requireActivity(), planViewModelFactory).get(PlanViewModel::class.java)

        planViewModel.datePickerResult.observe(viewLifecycleOwner, Observer {
            binding.pickEventDateTextView.text = getString(R.string.event_date_value, it)
        })

        binding.pickEventDateTextView.text = getString(R.string.event_date) //set initial text for first start

        binding.pickEventDateButton.setOnClickListener{
            val dialogFragment : DialogFragment = DatePickerFragment()
            dialogFragment.show(activity?.supportFragmentManager!!, "DatePicker" )
        }


        binding.insertPlanButton.setOnClickListener {

            var isPlanSuitable : Boolean = true

            //check if plan title is given. If not then show a warning message
            if (TextUtils.isEmpty(binding.eventTitleTextInputEditText.text.toString())) {
                isPlanSuitable = false
                Toast.makeText(activity, getString(R.string.type_title_for_event), Toast.LENGTH_SHORT).show()
            }

            //check if plan date is chosen. If not then show a warning message
            if (binding.pickEventDateTextView.text.equals("Event Date:")) {
                isPlanSuitable = false
                Toast.makeText(activity, "Please choose a date", Toast.LENGTH_SHORT).show()
            }

            //if plan is suitable for adding then add the plan to the database
            if (isPlanSuitable) {
                val testPlan = Plan(planTitle = binding.eventTitleTextInputEditText.text.toString(),
                    planDescription = binding.eventDescriptionTextInputEditText.text.toString(),
                    planEndDate = planViewModel.datePickerMillis.value!!
                )

                planViewModel.insert(testPlan)

                //navigate back to PlannerFragment after item added to the database
                findNavController().navigateUp()
            }
        }


        return binding.root
    }

}
