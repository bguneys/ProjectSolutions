package com.bguneys.app652020.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bguneys.app652020.database.Folder

import com.bguneys.app652020.database.ProjectRepository
import com.bguneys.app652020.databinding.FragmentNoteListBinding

class NoteListFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentNoteListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        val args = NoteListFragmentArgs.fromBundle(requireArguments())

        val mRepository = ProjectRepository(this.activity?.applicationContext)
        val noteViewModelFactory = NoteViewModelFactory(mRepository)
        val noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

        val adapter = NoteRecyclerViewAdapter(NoteRecyclerViewAdapter.NoteClickListener{
            //Sending folder title, folder record Id and note title via action while navigating to NoteFragment
            val action = NoteListFragmentDirections.actionNoteListFragmentToNoteFragment(it.noteTitle!!, args.selectedFolderTitle, it.recordId)
            findNavController().navigate(action)
        })

        //Showing most recent list of notes
        noteViewModel.getFolderByTitle(args.selectedFolderTitle).observe(viewLifecycleOwner, Observer { list ->
            adapter.noteList = list
        })

        //Adding new note to the database
        binding.fabAddNote.setOnClickListener {
            val newNoteTitle = binding.editTextNoteTitle.text.toString()
            val newFolder = Folder(folderTitle = args.selectedFolderTitle, noteTitle = newNoteTitle, noteText = "Type your note here..")
            noteViewModel.insert(newFolder)
        }

        binding.noteListRecyclerView.adapter = adapter
        binding.noteListRecyclerView.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
