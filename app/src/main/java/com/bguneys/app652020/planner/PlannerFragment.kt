package com.bguneys.app652020.planner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bguneys.app652020.R
import com.bguneys.app652020.database.Plan
import com.bguneys.app652020.database.PlanRepository
import com.bguneys.app652020.databinding.FragmentPlannerBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PlannerFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentPlannerBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlannerBinding.inflate(inflater, container, false)

        val mRepository = PlanRepository.getInstance(requireContext())
        val planViewModelFactory = PlanViewModelFactory(mRepository!!)
        val planViewModel = ViewModelProvider(this, planViewModelFactory).get(PlanViewModel::class.java)

        val adapter = PlanRecyclerViewAdapter(PlanRecyclerViewAdapter.PlanClickListener{
            Toast.makeText(activity, it.planTitle, Toast.LENGTH_SHORT).show()
        })

        planViewModel.planList.observe(viewLifecycleOwner, Observer { list ->
            adapter.planList = list
        })

        binding.planListRecyclerView.adapter = adapter
        binding.planListRecyclerView.layoutManager = LinearLayoutManager(activity)

        binding.fabAddPlan.setOnClickListener {
            //Testing
            val testPlan = Plan(planTitle="Plan", planDescription="description")
            planViewModel.insert(testPlan)
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
