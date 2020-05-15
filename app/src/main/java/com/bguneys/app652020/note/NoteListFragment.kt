package com.bguneys.app652020.note

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        //get arguments from FolderListFragment
        val args = NoteListFragmentArgs.fromBundle(requireArguments())

        //change toolbar label to folder title
        (activity as AppCompatActivity).supportActionBar?.setTitle(args.selectedFolderTitle)

        val mRepository = ProjectRepository(this.activity?.applicationContext)
        val noteViewModelFactory = NoteViewModelFactory(mRepository)
        val noteViewModel = ViewModelProvider(this, noteViewModelFactory)
            .get(NoteViewModel::class.java)

        val adapter = NoteRecyclerViewAdapter(NoteRecyclerViewAdapter.NoteClickListener{
            //Sending folder title, folder record Id and note title via action
            //while navigating to NoteFragment
            val action = NoteListFragmentDirections
                .actionNoteListFragmentToNoteFragment(
                    it.noteTitle!!,
                    args.selectedFolderTitle,
                    it.recordId)
            findNavController().navigate(action)
        })

        //Showing most recent list of notes
        noteViewModel.getFolderByTitle(args.selectedFolderTitle)
            .observe(viewLifecycleOwner, Observer { list ->
            adapter.noteList = list
        })

        //Adding new note to the database
        binding.fabAddNote.setOnClickListener {

            var isNoteTitleSuitable : Boolean = true

            //check if there is a note with same name. If yes then show a warning message
            for (folder in adapter.noteList) {
                if(folder.noteTitle.equals(binding.editTextNoteTitle.text.toString())) {
                    isNoteTitleSuitable = false
                    Toast.makeText(activity,
                        "Note with title ${binding.editTextNoteTitle.text} already available.",
                        Toast.LENGTH_SHORT).show()
                }

            }

            //check if note title is given. If not then show a warning message
            if (TextUtils.isEmpty(binding.editTextNoteTitle.text.toString())) {
                isNoteTitleSuitable = false
                Toast.makeText(activity, "Please type title for note", Toast.LENGTH_SHORT).show()
            }

            //if note title is suitable then add the note to the database
            if (isNoteTitleSuitable) {
                val newNoteTitle = binding.editTextNoteTitle.text.toString()
                val newFolder = Folder(
                    folderTitle = args.selectedFolderTitle,
                    noteTitle = newNoteTitle,
                    noteText = "Type your note here.."
                )
                noteViewModel.insert(newFolder)
                Log.i("NoteListFragment", "Folder added")
            }

            //hide soft keyboard after FAB click
            val inputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

            //clear EditText after FAB click
            binding.editTextNoteTitle.setText("")
        }

        binding.noteListRecyclerView.adapter = adapter
        binding.noteListRecyclerView.layoutManager = LinearLayoutManager(activity)

        //Adding ItemTouchHelper for swipe to delete functionality
        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            /**
             * Called when ItemTouchHelper wants to move the dragged item from its old position
             * to the new position. Not used for this app but still needs to be overriden.
             */
            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            // Called when a ViewHolder is swiped by the user. Used for swipe to delete functionality
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                //show dialog to ensure deleting note
                val dialogBuilder = AlertDialog.Builder(context!!)
                dialogBuilder.setMessage("Delete folder?")
                    .setCancelable(false)
                    .setPositiveButton("Delete", {dialog, id ->
                        //delete note
                        val position = viewHolder.adapterPosition
                        val selectedFolder = adapter.noteList.get(position)
                        noteViewModel.deleteByFolderTitleAndNoteTitle(
                            selectedFolder.folderTitle,
                            selectedFolder.noteTitle!!)

                        })
                    .setNegativeButton("Cancel", { dialog, id ->
                        dialog.dismiss() //do nothing and dismiss the dialog
                        adapter.notifyDataSetChanged() //revert swipe action when cancelled
                    })

                val alert = dialogBuilder.create()
                alert.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.noteListRecyclerView)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
