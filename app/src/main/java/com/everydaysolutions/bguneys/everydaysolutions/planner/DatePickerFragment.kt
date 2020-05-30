package com.everydaysolutions.bguneys.everydaysolutions.planner

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.everydaysolutions.bguneys.everydaysolutions.database.PlanRepository
import io.reactivex.rxjava3.annotations.NonNull
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var repository : PlanRepository? = null
    lateinit var planViewModelFactory : PlanViewModelFactory
    lateinit var planViewModel: PlanViewModel

    @NonNull
    @Override
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Setting up ViewModel
        repository = PlanRepository.getInstance(requireContext())
        planViewModelFactory = PlanViewModelFactory(repository!!)
        planViewModel = ViewModelProvider(requireActivity(), planViewModelFactory).get(PlanViewModel::class.java)

        val calendar : Calendar = Calendar.getInstance()
        val year : Int = calendar.get(Calendar.YEAR)
        val month : Int = calendar.get(Calendar.MONTH)
        val day : Int = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {

        //receive chosen date and turn it into millis then pass it to the ViewModel
        val calendar : Calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val millis : Long? = calendar.timeInMillis
        planViewModel.setDatePickerMillis(millis!!)


        planViewModel.getDatePickerResult(requireActivity(), year, month, day)
    }


}
