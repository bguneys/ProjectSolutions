package com.bguneys.app652020.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bguneys.app652020.R
import com.bguneys.app652020.database.Folder

class FolderRecyclerViewAdapter : RecyclerView.Adapter<FolderRecyclerViewAdapter.ViewHolder>() {

    var folderList = listOf<Folder>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder private constructor(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val folderTitle : TextView = itemView.findViewById(R.id.folder_title_textView)

        fun bind(item : Folder) {
            folderTitle.text = item.folderTitle
        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.folder_list_item, parent, false)

                return ViewHolder(view)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = folderList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = folderList.size

}