package com.bguneys.app652020.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.bguneys.app652020.R
import com.bguneys.app652020.database.ProjectRepository
import com.bguneys.app652020.databinding.FragmentNoteListBinding

class NoteListFragment : Fragment() {

    private var _binding : FragmentNoteListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        val args = NoteListFragmentArgs.fromBundle(requireArguments())

        val mRepository = ProjectRepository(this.activity?.applicationContext)
        val noteViewModelFactory = NoteViewModelFactory(mRepository)
        val noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

        val adapter = NoteRecyclerViewAdapter()

        noteViewModel.getFolderByName(args.selectedFolderTitle).observe(viewLifecycleOwner, Observer { list ->
            adapter.noteList = list
        })

        binding.noteListRecyclerView.adapter = adapter
        binding.noteListRecyclerView.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
