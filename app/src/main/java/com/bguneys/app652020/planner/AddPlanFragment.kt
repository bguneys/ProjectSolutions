package com.bguneys.app652020.planner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.bguneys.app652020.R
import com.bguneys.app652020.database.PlanRepository
import com.bguneys.app652020.databinding.FragmentAddPlanBinding

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

        planViewModel._datePickerResult.observe(viewLifecycleOwner, Observer {
            binding.pickEventDateTextView.text = it
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

        binding.pickEventDateButton.setOnClickListener{
            val dialogFragment : DialogFragment = DatePickerFragment()
            dialogFragment.show(activity?.supportFragmentManager!!, "DatePicker" )
        }

        binding.addNotificationButton.setOnClickListener {
            //planViewModel._datePickerResult.value = "Bulut"
        }


        return binding.root
    }

}
