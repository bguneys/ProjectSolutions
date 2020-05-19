package com.bguneys.app652020.planner

import android.os.Bundle
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

import com.bguneys.app652020.R
import com.bguneys.app652020.database.Plan
import com.bguneys.app652020.database.PlanRepository
import com.bguneys.app652020.databinding.FragmentEditPlanBinding

class EditPlanFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentEditPlanBinding? = null
    private val binding
        get() = _binding!!

    lateinit var args : EditPlanFragmentArgs
    lateinit var planViewModel : PlanViewModel

    lateinit var editedPlan : Plan

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditPlanBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true) //required to add new menu

        //Showing confirmation dialog message by applying custom back navigation
        val callback = requireActivity().onBackPressedDispatcher
            .addCallback(this) {

                //show dialog to ask for saving changes before quit
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setMessage("Save changes?")
                    .setCancelable(false)
                    .setPositiveButton("Save", { dialog, id ->
                        savePlan() //custom method for updating the current Plan
                        findNavController().navigateUp()
                    })
                    .setNegativeButton("No", { dialog, id ->
                        dialog.dismiss() //do nothing and dismiss the dialog
                        findNavController().navigateUp()
                    })

                val alert = dialogBuilder.create()
                alert.show()
            }

        callback.isEnabled

        val mRepository = PlanRepository.getInstance(requireContext())
        val planViewModelFactory = PlanViewModelFactory(mRepository!!)
        planViewModel = ViewModelProvider(requireActivity(),
            planViewModelFactory).get(PlanViewModel::class.java)

        planViewModel.datePickerResult.observe(requireActivity(), Observer{
            binding.pickEventDateTextView.text = it
        })

        //get arguments from PlannerFragment
        args = EditPlanFragmentArgs.fromBundle(requireArguments())

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

        binding.addNotificationButton.setOnClickListener {
            //TODO
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
                requireActivity().onBackPressedDispatcher.onBackPressed()
                true
            }

            R.id.action_save_plan -> {
                savePlan() //custom method for updating the current Plan
                Toast.makeText(activity, "Changes saved", Toast.LENGTH_SHORT).show()
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

}