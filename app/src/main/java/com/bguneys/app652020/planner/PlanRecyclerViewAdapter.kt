package com.bguneys.app652020.planner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bguneys.app652020.R
import com.bguneys.app652020.database.Plan

class PlanRecyclerViewAdapter(val clickListener : PlanClickListener)
    : RecyclerView.Adapter<PlanRecyclerViewAdapter.ViewHolder>() {

    var planList = listOf<Plan>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder private constructor(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val planTitle : TextView = itemView.findViewById(R.id.plan_title_textView)

        fun bind(plan : Plan, clickListener : PlanClickListener) {
            planTitle.text = plan.planTitle
            itemView.setOnClickListener {
                clickListener.onClick(plan)
            }
        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.planner_list_item, parent, false)

                return ViewHolder(view)
            }
        }

    }

    class PlanClickListener(val clickListener : (plan : Plan) -> Unit) {
        fun onClick(plan : Plan) = clickListener(plan)
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
    override fun onBindViewHolder(holder: PlanRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = planList[position]
        holder.bind(item, clickListener)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = planList.size


}