package com.bguneys.app652020.note

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.bguneys.app652020.R
import com.bguneys.app652020.database.Folder
import com.bguneys.app652020.database.ProjectRepository

import com.bguneys.app652020.databinding.FragmentNoteBinding


class NoteFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentNoteBinding? = null
    private val binding
        get() = _binding!!

    lateinit var args : NoteFragmentArgs
    lateinit var noteViewModel : NoteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true) //required to add new menu

        args = NoteFragmentArgs.fromBundle(requireArguments())

        val mRepository = ProjectRepository(this.activity?.applicationContext)
        val noteViewModelFactory = NoteViewModelFactory(mRepository)
        noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

        //Showing the text of selected note inside selected folder
        noteViewModel.getFolderByFolderTitleAndNoteTitle(args.selectedFolderTitle, args.selectedNoteTitle)
            .observe(viewLifecycleOwner, Observer {
                binding.editTextNoteText.setText(it.noteText)
            })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_save_note -> {
                //Saving text to the selected note
                val newNoteText = binding.editTextNoteText.text.toString()

                val newFolder = Folder(recordId = args.SelectedFolderId,
                    folderTitle = args.selectedFolderTitle,
                    noteTitle = args.selectedNoteTitle,
                    noteText = newNoteText)

                noteViewModel.update(newFolder)

                Toast.makeText(activity, "Note saved", Toast.LENGTH_SHORT).show()

                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
