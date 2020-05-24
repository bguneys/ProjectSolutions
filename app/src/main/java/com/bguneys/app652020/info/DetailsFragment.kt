package com.bguneys.app652020.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bguneys.app652020.R
import com.bguneys.app652020.databinding.FragmentDetailsBinding

/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

    lateinit var args : DetailsFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        //get arguments from InfoFragment
        args = DetailsFragmentArgs.fromBundle(requireArguments())

        binding.textViewInfoHeader.text = args.infoName

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
