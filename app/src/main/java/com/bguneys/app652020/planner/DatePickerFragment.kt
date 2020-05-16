package com.bguneys.app652020.planner

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bguneys.app652020.database.PlanRepository
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

        planViewModel.getDatePickerResult(year, month, day)
/*
        val yearString = Integer.toString(year)
        val monthString = Integer.toString(month + 1)
        val dayString = Integer.toString(day)

        if (Locale.getDefault().language == "en_US") {
            PlanViewModel._datePickerResult.value = "$monthString / $dayString / $yearString"
        } else {
            PlanViewModel._datePickerResult.value = "$dayString / $monthString / $yearString"
        }

        Log.i("PlanViewModel", "getDaePickerResult() called. Result: ${PlanViewModel._datePickerResult.value} ")
*/

        //receive chosen date and turn it into millis then pass it to the ViewModel
        val calendar : Calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val millis : Long? = calendar.timeInMillis
        planViewModel._datePickerMillis.value = millis

    }


}