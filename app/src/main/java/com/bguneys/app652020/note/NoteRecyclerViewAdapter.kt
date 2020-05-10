package com.bguneys.app652020.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bguneys.app652020.R
import com.bguneys.app652020.database.Folder


class NoteRecyclerViewAdapter : RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder>() {

    var noteList = listOf<Folder>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    class ViewHolder private constructor(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val notetitle : TextView = itemView.findViewById(R.id.note_title_textView)

        fun bind(item : Folder) {
            notetitle.text = item.noteTitle
        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.note_list_item, parent, false)

                return ViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = noteList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = noteList.size

}