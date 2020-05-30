package com.everydaysolutions.bguneys.everydaysolutions.note

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.everydaysolutions.bguneys.everydaysolutions.R
import com.everydaysolutions.bguneys.everydaysolutions.database.Folder
import com.everydaysolutions.bguneys.everydaysolutions.database.ProjectRepository

import com.everydaysolutions.bguneys.everydaysolutions.databinding.FragmentFolderListBinding


class FolderListFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentFolderListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFolderListBinding.inflate(inflater, container, false)

        val mRepository = ProjectRepository.getInstance(requireActivity())
        val noteViewModelFactory = NoteViewModelFactory(mRepository!!)
        val noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

        val adapter = FolderRecyclerViewAdapter(FolderRecyclerViewAdapter.FolderClickListener {

            //Sending folder title and Folder record Id via action while navigating to NoteListFragment
            val action = FolderListFragmentDirections.actionFolderListFragmentToNoteListFragment(it.folderTitle, it.recordId)
            findNavController().navigate(action)

        })

        //Showing most recent list of folders
        noteViewModel.folderList.observe(viewLifecycleOwner, Observer { list ->
            adapter.folderList = list

            //if there is no item in the list show empty list message
            if(list.isEmpty()) {
                binding.emptyTextView.visibility = View.VISIBLE
            } else {
                binding.emptyTextView.visibility = View.GONE
            }

        })

        binding.folderListRecyclerView.adapter = adapter
        binding.folderListRecyclerView.layoutManager = LinearLayoutManager(activity)

        //Adding new folder to the database
        binding.fabAddFolder.setOnClickListener {

            var isFolderTitleSuitable : Boolean = true

            //check if there is a folder with same name. If yes then show a warning message
            for (folder in adapter.folderList) {
                if(folder.folderTitle.equals(binding.editTextFolderTitle.text.toString())) {
                    isFolderTitleSuitable = false
                    Toast.makeText(activity,
                        getString(R.string.available_folder_text, binding.editTextFolderTitle.text),
                        Toast.LENGTH_SHORT).show()
                }

            }

            //check if folder title is given. If not then show a warning message
            if (TextUtils.isEmpty(binding.editTextFolderTitle.text.toString())) {
                isFolderTitleSuitable = false
                Toast.makeText(activity, getString(R.string.type_folder_title_text), Toast.LENGTH_SHORT).show()
            }

            //if folder title is suitable then add the folder to the database
            if (isFolderTitleSuitable) {
                val newFolderTitle = binding.editTextFolderTitle.text.toString()
                val newFolder =
                    Folder(folderTitle = newFolderTitle, noteTitle = null, noteText = null)
                noteViewModel.insert(newFolder)

            }

            hideKeyboard()  //custom method to hide soft keyboard after FAB click

            //clear EditText after FAB click
            binding.editTextFolderTitle.setText("")
        }

        //Adding ItemTouchHelper for swipe to delete functionality
        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            /**
            * Called when ItemTouchHelper wants to move the dragged item from its old position
            * to the new position. Not used for this app but still needs to be overriden.
            */
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Called when a ViewHolder is swiped by the user. Used for swipe to delete functionality
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                //show dialog to ensure deleting folder
                val dialogBuilder = AlertDialog.Builder(context!!)
                dialogBuilder.setMessage(getString(R.string.delete_folder_question))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.delete), { dialog, id ->
                        //delete folder
                        val position = viewHolder.adapterPosition
                        val selectedFolder = adapter.folderList.get(position)
                        noteViewModel.deleteByFolderTitle(selectedFolder.folderTitle)
                    })
                    .setNegativeButton(getString(R.string.cancel), { dialog, id ->
                        dialog.dismiss() //do nothing and dismiss the dialog
                        adapter.notifyDataSetChanged() //revert swipe action when cancelled
                    })

                val alert = dialogBuilder.create()
                alert.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.folderListRecyclerView)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Custom method for hiding soft keyboard
     */
    private fun hideKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}
