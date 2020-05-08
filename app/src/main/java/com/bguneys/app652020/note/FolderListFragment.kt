package com.bguneys.app652020.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bguneys.app652020.database.ProjectRepository

import com.bguneys.app652020.databinding.FragmentFolderListBinding


class FolderListFragment : Fragment() {

    private var _binding : FragmentFolderListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFolderListBinding.inflate(inflater, container, false)

        val mRepository = ProjectRepository(this.activity?.applicationContext)
        val noteViewModelFactory = NoteViewModelFactory(mRepository)
        val noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

        noteViewModel.folderList.observe(viewLifecycleOwner, Observer { list ->
            binding.folderListTextView.setText(list.toString())
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.folderListTextView.setText("Hello")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
