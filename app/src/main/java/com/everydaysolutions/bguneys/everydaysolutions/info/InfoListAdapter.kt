package com.everydaysolutions.bguneys.everydaysolutions.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.everydaysolutions.bguneys.everydaysolutions.R

class InfoListAdapter(val clickListener: InfoClickListener) : ListAdapter<InfoItem, InfoListAdapter.ViewHolder>(DiffCallback) {

    var infoList = listOf<InfoItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView = itemView.findViewById(R.id.news_textView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.country_textView)

        fun bind(info : InfoItem, clickListener: InfoClickListener) {
            titleTextView.text = info.title
            descriptionTextView.text = info.description

            itemView.setOnClickListener {
                clickListener.onClick(info)
            }
        }

        companion object {
            fun from(parent : ViewGroup) : RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.info_list_item, parent, false)

                return ViewHolder(view)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<InfoItem>() {
        /**
         * Called to check whether two objects represent the same item.
         */
        override fun areItemsTheSame(oldItem: InfoItem, newItem: InfoItem): Boolean {
            return oldItem === newItem
        }

        /**
         * Called to check whether two items have the same data.
         */
        override fun areContentsTheSame(oldItem: InfoItem, newItem: InfoItem): Boolean {
            return oldItem.id == newItem.id
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
        return ViewHolder.from(parent) as ViewHolder
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
        val info = getItem(position)
        holder.bind(info!!, clickListener)
    }

    class InfoClickListener(val clickListener: (info : InfoItem) -> Unit) {
        fun onClick(info : InfoItem) = clickListener(info)
    }
}
