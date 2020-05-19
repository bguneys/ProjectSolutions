package com.bguneys.app652020.planner

import android.os.Bundle
import android.text.format.DateUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.bguneys.app652020.R
import com.bguneys.app652020.database.PlanRepository
import com.bguneys.app652020.databinding.FragmentViewPlanBinding


class ViewPlanFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentViewPlanBinding? = null
    private val binding
        get() = _binding!!

    lateinit var args : ViewPlanFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentViewPlanBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true) //required to add new menu

        //get arguments from PlannerFragment
        args = ViewPlanFragmentArgs.fromBundle(requireArguments())

        val mRepository = PlanRepository.getInstance(requireContext())
        val planViewModelFactory = PlanViewModelFactory(mRepository!!)
        val planViewModel = ViewModelProvider(requireActivity(),
            planViewModelFactory).get(PlanViewModel::class.java)

        //Set arguments to TextViews in the fragment.
        binding.textViewPlanTitle.text = args.planTitle
        binding.textViewPlanDescription.text = args.planDescription
        planViewModel.setDatePickerMillis(args.planDate) //set milliseconds for initial start
        binding.textViewPlanDate.text = DateUtils.formatDateTime(requireActivity(),
            args.planDate,
            DateUtils.FORMAT_ABBREV_RELATIVE)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_plan, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_edit_plan -> {
                //Sending plan details via action while navigating to EditPlanFragment
                val action = ViewPlanFragmentDirections.actionViewPlanFragmentToEditPlanFragment(
                    args.planId,
                    args.planTitle,
                    args.planDescription,
                    args.planDate
                )

                findNavController().navigate(action)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
