package com.everydaysolutions.bguneys.everydaysolutions.planner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.everydaysolutions.bguneys.everydaysolutions.R
import com.everydaysolutions.bguneys.everydaysolutions.database.PlanRepository
import com.everydaysolutions.bguneys.everydaysolutions.databinding.FragmentPlannerBinding

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


            //Sending plan details via action while navigating to ViewPlanFragment
            val action = PlannerFragmentDirections.actionPlannerFragmentToViewPlanFragment(
                it.planId,
                it.planTitle,
                it.planDescription,
                it.planEndDate
            )
            findNavController().navigate(action)


        })

        planViewModel.planList.observe(viewLifecycleOwner, Observer { list ->
            adapter.planList = list

            //if there is no item in the list show empty list message
            if(list.isEmpty()) {
                binding.emptyTextView.visibility = View.VISIBLE
            } else {
                binding.emptyTextView.visibility = View.GONE
            }

        })



        binding.planListRecyclerView.adapter = adapter
        binding.planListRecyclerView.layoutManager = LinearLayoutManager(activity)

        binding.fabAddPlan.setOnClickListener {

            findNavController().navigate(PlannerFragmentDirections.actionPlannerFragmentToAddPlanFragment())
        }

        //Adding ItemTouchHelper for swipe to delete functionality
        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            /**
             * Called when ItemTouchHelper wants to move the dragged item from its old position
             * to the new position. Not used for this app but still needs to be overriden.
             */
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Called when a ViewHolder is swiped by the user. Used for swipe to delete functionality
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                //show dialog to ensure deleting folder
                val dialogBuilder = AlertDialog.Builder(context!!)
                dialogBuilder.setMessage(getString(R.string.delete_event_question))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.delete), { dialog, id ->
                        //delete folder
                        val position = viewHolder.adapterPosition
                        val selectedPlan = adapter.planList.get(position)
                        planViewModel.delete(selectedPlan)
                    })
                    .setNegativeButton(getString(R.string.cancel), { dialog, id ->
                        dialog.dismiss() //do nothing and dismiss the dialog
                        adapter.notifyDataSetChanged() //revert swipe action when cancelled
                    })

                val alert = dialogBuilder.create()
                alert.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.planListRecyclerView)

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
