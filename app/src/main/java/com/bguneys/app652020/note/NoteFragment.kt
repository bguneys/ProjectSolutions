package com.bguneys.app652020.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bguneys.app652020.database.ProjectRepository

import com.bguneys.app652020.databinding.FragmentNoteBinding


class NoteFragment : Fragment() {

    private var _binding : FragmentNoteBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)

        val args = NoteFragmentArgs.fromBundle(requireArguments())

        val mRepository = ProjectRepository(this.activity?.applicationContext)
        val noteViewModelFactory = NoteViewModelFactory(mRepository)
        val noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

        noteViewModel.getFolderByFolderTitleAndNoteTitle(args.selectedFolderTitle, args.selectedNoteTitle)
            .observe(viewLifecycleOwner, Observer {
                binding.editTextNoteText.setText(it.noteText)
            })




        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
