package com.bguneys.app652020.planner

import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bguneys.app652020.R
import com.bguneys.app652020.databinding.FragmentViewPlanBinding


class ViewPlanFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentViewPlanBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentViewPlanBinding.inflate(inflater, container, false)

        //get arguments from PlannerFragment
        val args = ViewPlanFragmentArgs.fromBundle(requireArguments())

        //Set arguments to TextViews in the fragment.
        binding.textViewPlanTitle.text = args.planTitle
        binding.textViewPlanDescription.text = args.planDescription
        binding.textViewPlanDate.text = DateUtils.formatDateTime(requireActivity(),
            args.planDate,
            DateUtils.FORMAT_ABBREV_RELATIVE)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
