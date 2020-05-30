package com.everydaysolutions.bguneys.everydaysolutions.planner


import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.everydaysolutions.bguneys.everydaysolutions.R
import com.everydaysolutions.bguneys.everydaysolutions.database.Plan


class PlanRecyclerViewAdapter(val clickListener: PlanClickListener) :
    RecyclerView.Adapter<PlanRecyclerViewAdapter.ViewHolder>() {

    var planList = listOf<Plan>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val planTitleTextView: TextView = itemView.findViewById(R.id.plan_title_textView)
        val planDaysLeftTextView: TextView = itemView.findViewById(R.id.plan_days_left_textView)
        val planDateTextView : TextView = itemView.findViewById(R.id.plan_date_textView)

        fun bind(plan: Plan, clickListener: PlanClickListener) {
            planTitleTextView.text = plan.planTitle
            planDaysLeftTextView.text = calculateDate(plan) //custom method for calculating date
            planDateTextView.text = DateUtils.formatDateTime(itemView.context,
                plan.planEndDate,
                DateUtils.FORMAT_ABBREV_RELATIVE)

            itemView.setOnClickListener {
                clickListener.onClick(plan)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.planner_list_item, parent, false)

                return ViewHolder(view)
            }
        }

        /**
         * Custom method for calculation date in human readable months and day terms
         * taking if the target date is due or past into account
         */
        private fun calculateDate(plan: Plan): String {

            val ONE_DAY_TIME = 24L * 60L * 60L * 1000L
            val ONE_MONTH_TIME = 24L * 60L * 60L * 1000L * 30L

            var dateDifferenceInMillis: Long = plan.planEndDate - System.currentTimeMillis()

            var diffenceMonth: Long = dateDifferenceInMillis / ONE_MONTH_TIME

            if (diffenceMonth >= 1) {
                var diffenceDayInMillis = dateDifferenceInMillis % ONE_MONTH_TIME
                var differenceDay = diffenceDayInMillis / ONE_DAY_TIME
                var differenceMonthString = diffenceMonth.toString()
                var differenceDayString = differenceDay.toString()

                if (dateDifferenceInMillis < 0) {
                    differenceMonthString = diffenceMonth.unaryMinus().toString()
                    differenceDayString = differenceDay.unaryMinus().toString()

                    return "${differenceMonthString}  ${if (diffenceMonth >= 2) {
                        "months"
                    } else {
                        "month"
                    }} " +
                            "${differenceDayString} ${if (differenceDay >= 2) {
                                "days"
                            } else {
                                "day"
                            }} ago"

                } else {
                    return "in ${differenceMonthString} ${if (diffenceMonth >= 2) {
                        "months"
                    } else {
                        "month"
                    }} " +
                            "${differenceDayString} ${if (differenceDay >= 2) {
                                "days"
                            } else {
                                "day"
                            }}"
                }

            } else {
                var differenceDay = dateDifferenceInMillis / ONE_DAY_TIME
                var differenceDayString = differenceDay.toString()

                if (ONE_DAY_TIME.unaryMinus() < dateDifferenceInMillis && dateDifferenceInMillis < ONE_DAY_TIME) {
                    return "Today"

                } else if(dateDifferenceInMillis >= ONE_DAY_TIME) {
                    return "in ${differenceDayString} ${if (differenceDay >= 2) {
                        "days"
                    } else {
                        "day"
                    }}"

                } else {
                    differenceDayString = differenceDay.unaryMinus().toString()

                    return "${differenceDayString} ${if (differenceDay >= 2) {
                        "days"
                    } else {
                        "day"
                    }} ago"

                }

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


    class PlanClickListener(val clickListener: (plan: Plan) -> Unit) {
        fun onClick(plan: Plan) = clickListener(plan)
    }

}
