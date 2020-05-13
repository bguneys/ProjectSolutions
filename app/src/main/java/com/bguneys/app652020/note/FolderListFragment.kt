package com.bguneys.app652020.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bguneys.app652020.database.Folder
import com.bguneys.app652020.database.ProjectRepository

import com.bguneys.app652020.databinding.FragmentFolderListBinding


class FolderListFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentFolderListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFolderListBinding.inflate(inflater, container, false)

        val mRepository = ProjectRepository(this.activity?.applicationContext)
        val noteViewModelFactory = NoteViewModelFactory(mRepository)
        val noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

        val adapter = FolderRecyclerViewAdapter(FolderRecyclerViewAdapter.FolderClickListener {

            //Sending folder title and Folder record Id via action while navigating to NoteListFragment
            val action = FolderListFragmentDirections.actionFolderListFragmentToNoteListFragment(it.folderTitle, it.recordId)
            findNavController().navigate(action)

        })

        //Showing most recent list of folders
        noteViewModel.folderList.observe(viewLifecycleOwner, Observer { list ->
            adapter.folderList = list
        })

        binding.folderListRecyclerView.adapter = adapter
        binding.folderListRecyclerView.layoutManager = LinearLayoutManager(activity)

        //Adding new folder to the database
        binding.fabAddFolder.setOnClickListener {
            val newFolderTitle = binding.editTextFolderTitle.text.toString()
            val newFolder = Folder(folderTitle = newFolderTitle, noteTitle = null, noteText = null)
            noteViewModel.insert(newFolder)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
