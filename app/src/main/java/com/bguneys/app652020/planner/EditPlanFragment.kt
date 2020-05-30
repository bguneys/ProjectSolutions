package com.everydaysolutions.bguneys.everydaysolutions.planner

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.everydaysolutions.bguneys.everydaysolutions.R
import com.everydaysolutions.bguneys.everydaysolutions.database.Plan
import com.everydaysolutions.bguneys.everydaysolutions.database.PlanRepository
import com.everydaysolutions.bguneys.everydaysolutions.databinding.FragmentEditPlanBinding

class EditPlanFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentEditPlanBinding? = null
    private val binding
        get() = _binding!!

    lateinit var args : EditPlanFragmentArgs
    lateinit var planViewModel : PlanViewModel

    lateinit var editedPlan : Plan

    var isUpButtonPressed = false //boolean for checking if up button pressed once

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditPlanBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true) //required to add new menu

        //get arguments from PlannerFragment
        args = EditPlanFragmentArgs.fromBundle(requireArguments())

        val mRepository = PlanRepository.getInstance(requireContext())
        val planViewModelFactory = PlanViewModelFactory(mRepository!!)
        planViewModel = ViewModelProvider(requireActivity(),
            planViewModelFactory).get(PlanViewModel::class.java)

        //Showing confirmation dialog message by applying custom back navigation
        val callback = requireActivity().onBackPressedDispatcher
            .addCallback(this) {

                //check if there is any changes made to the plan details
                if (args.planTitle.equals(binding.eventTitleTextInputEditText.text.toString()) &&
                    args.planDescription.equals(binding.eventDescriptionTextInputEditText.text.toString()) &&
                    args.planDate.equals(planViewModel.datePickerMillis.value)) {

                    // custom method for navigating to ViewPlanFragment with default calues
                    navigateToViewPlanFragment(
                        args.planId,
                        args.planTitle,
                        args.planDescription,
                        args.planDate)

                } else {

                    //show dialog to ask for saving changes before quit
                    val dialogBuilder = AlertDialog.Builder(requireContext())
                    dialogBuilder.setMessage(getString(R.string.save_changes_question))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.save), { dialog, id ->
                            savePlan() //custom method for updating the current Plan

                            // custom method for navigating to ViewPlanFragment with edited values
                            navigateToViewPlanFragment(
                                editedPlan.planId,
                                editedPlan.planTitle,
                                editedPlan.planDescription,
                                editedPlan.planEndDate)
                        })
                        .setNegativeButton(getString(R.string.no), { dialog, id ->
                            dialog.dismiss() //do nothing and dismiss the dialog

                            // custom method for navigating to ViewPlanFragment with default calues
                            navigateToViewPlanFragment(
                                args.planId,
                                args.planTitle,
                                args.planDescription,
                                args.planDate)
                        })

                    val alert = dialogBuilder.create()
                    alert.show()
                }

            }

        callback.isEnabled


        planViewModel.datePickerResult.observe(viewLifecycleOwner, Observer{
            binding.pickEventDateTextView.text = it
        })

        //Set arguments to Views in the fragment.
        binding.eventTitleTextInputEditText.setText(args.planTitle)
        binding.eventDescriptionTextInputEditText.setText(args.planDescription)
        planViewModel.setDatePickerMillis(args.planDate) //set milliseconds for initial start
        binding.pickEventDateTextView.text = DateUtils.formatDateTime(requireActivity(),
            args.planDate,
            DateUtils.FORMAT_ABBREV_RELATIVE)

        binding.pickEventDateButton.setOnClickListener{
            val dialogFragment : DialogFragment = DatePickerFragment()
            dialogFragment.show(activity?.supportFragmentManager!!, "DatePicker" )
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save_plan, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {

                //check if Up button is pressed once using boolean to prevent double press
                if (!isUpButtonPressed) {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    isUpButtonPressed = true
                }

                true
            }

            R.id.action_save_plan -> {
                savePlan() //custom method for updating the current Plan
                Toast.makeText(activity, getString(R.string.changes_saved), Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Custom method for updating the current note text
     */
    fun savePlan() {
        editedPlan = Plan(
            planId = args.planId,
            planTitle = binding.eventTitleTextInputEditText.text.toString(),
            planDescription = binding.eventDescriptionTextInputEditText.text.toString(),
            planEndDate = planViewModel.datePickerMillis.value!!
        )

        planViewModel.update(editedPlan)

    }

    /**
     * Custom method for updating the current note text
     */
    fun navigateToViewPlanFragment(
        planId : Int,
        planTitle : String,
        planDescription :
        String, planEndDate : Long) {

        //Sending plan details via action while navigating to EditPlanFragment
        val action = EditPlanFragmentDirections.actionEditPlanFragmentToViewPlanFragment(
            planId,
            planTitle,
            planDescription,
            planEndDate
        )
        //navigate back to PlannerFragment after item added to the database
        findNavController().navigate(action)
    }

}
