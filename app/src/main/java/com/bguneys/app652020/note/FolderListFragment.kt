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

    private var _binding : FragmentFolderListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFolderListBinding.inflate(inflater, container, false)

        val mRepository = ProjectRepository(this.activity?.applicationContext)
        val noteViewModelFactory = NoteViewModelFactory(mRepository)
        val noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

        //val folder = Folder(folderTitle = "Market", noteTitle = "Title",noteText = "Note")
        //noteViewModel.insert(folder)

        val adapter = FolderRecyclerViewAdapter(FolderRecyclerViewAdapter.FolderClickListener {

            val action = FolderListFragmentDirections.actionFolderListFragmentToNoteListFragment(it)
            findNavController().navigate(action)

        })

        noteViewModel.folderList.observe(viewLifecycleOwner, Observer { list ->
            adapter.folderList = list
        })

        binding.folderListRecyclerView.adapter = adapter
        binding.folderListRecyclerView.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
