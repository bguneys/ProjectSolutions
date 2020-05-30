package com.everydaysolutions.bguneys.everydaysolutions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.everydaysolutions.bguneys.everydaysolutions.databinding.FragmentMainBinding
import com.bumptech.glide.Glide

class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        Glide.with(this).load(R.drawable.img_note_taker).into(binding.imageViewNoteTaker)
        Glide.with(this).load(R.drawable.img_planner).into(binding.imageViewPlanner)
        Glide.with(this).load(R.drawable.img_world_overview).into(binding.imageViewWorldOverview)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.materialCardViewNoteTaker.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_folderListFragment)
        }

        binding.materialCardViewPlanner.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_plannerFragment)
        }

        binding.materialCardViewWorldOverview.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_infoFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
