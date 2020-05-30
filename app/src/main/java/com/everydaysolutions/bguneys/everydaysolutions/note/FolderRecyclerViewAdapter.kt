package com.everydaysolutions.bguneys.everydaysolutions.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.everydaysolutions.bguneys.everydaysolutions.R
import com.everydaysolutions.bguneys.everydaysolutions.database.Folder

class FolderRecyclerViewAdapter(val clickListener : FolderClickListener) : RecyclerView.Adapter<FolderRecyclerViewAdapter.ViewHolder>() {

    var folderList = listOf<Folder>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder private constructor(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val folderTitle : TextView = itemView.findViewById(R.id.folder_title_textView)

        fun bind(folder : Folder, clickListener : FolderClickListener) {
            folderTitle.text = folder.folderTitle
            itemView.setOnClickListener {
                clickListener.onClick(folder)
            }
        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.folder_list_item, parent, false)

                return ViewHolder(view)
            }
        }

    }

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     *
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = folderList[position]
        holder.bind(item, clickListener)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = folderList.size

    class FolderClickListener(val clickListener : (folder : Folder) -> Unit) {
        fun onClick(folder : Folder) = clickListener(folder)
    }

}