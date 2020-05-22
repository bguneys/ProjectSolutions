package com.bguneys.app652020.note

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
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

        //Showing confirmation dialog message by applying custom back navigation
        val callback = requireActivity().onBackPressedDispatcher
            .addCallback(this) {

                //check if there is any changes made to the plan details
                if (args.selectedNoteText.equals(binding.editTextNoteText.text.toString())) {

                    findNavController().navigateUp()

                } else {

                    //show dialog to ask for saving changes before quit
                    val dialogBuilder = AlertDialog.Builder(requireContext())
                    dialogBuilder.setMessage("Save changes?")
                        .setCancelable(false)
                        .setPositiveButton("Save", { dialog, id ->
                            saveTheNote() //custom method for updating the current note
                            findNavController().navigateUp()
                        })
                        .setNegativeButton("No", { dialog, id ->
                            dialog.dismiss() //do nothing and dismiss the dialog
                            findNavController().navigateUp()
                        })

                    val alert = dialogBuilder.create()
                    alert.show()
                }
            }

        callback.isEnabled


        //get arguments from NoteListFragment
        args = NoteFragmentArgs.fromBundle(requireArguments())

        //change toolbar label to note title
        (activity as AppCompatActivity).supportActionBar?.setTitle(args.selectedNoteTitle)


        val mRepository = ProjectRepository.getInstance(requireActivity())
        val noteViewModelFactory = NoteViewModelFactory(mRepository!!)
        noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

        //set note text retrieved from NoteListFragment
        binding.editTextNoteText.setText(args.selectedNoteText)

/*
        //Showing the text of selected note inside selected folder
        noteViewModel.getFolderByFolderTitleAndNoteTitle(args.selectedFolderTitle, args.selectedNoteTitle)
            .observe(viewLifecycleOwner, Observer {
                binding.editTextNoteText.setText(it.noteText)
            })
*/

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
                return true
            }

            R.id.action_save_note -> {
                saveTheNote() //custom method for updating the current note
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


    /**
     * Custom method for updating the current note text
     */
    fun saveTheNote() {
        val newNoteText = binding.editTextNoteText.text.toString()

        val newFolder = Folder(recordId = args.SelectedFolderId,
            folderTitle = args.selectedFolderTitle,
            noteTitle = args.selectedNoteTitle,
            noteText = newNoteText)

        noteViewModel.update(newFolder)
    }

}

