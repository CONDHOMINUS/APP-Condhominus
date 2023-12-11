package com.example.condhominus.view.scheduling

import AvailableSchedulesAdapters
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.condhominus.databinding.FragmentPopUpRegisterScheduleBinding
import com.example.condhominus.databinding.FragmentSchedulingBinding
import com.example.condhominus.ext.formatBornDate
import com.example.condhominus.model.Schedule
import com.example.condhominus.view.scheduling.viewmodel.SchedulingViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SchedulingFragment : Fragment() {

    private lateinit var viewModel: SchedulingViewModel

    private var _binding: FragmentSchedulingBinding? = null
    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance() = SchedulingFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSchedulingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SchedulingViewModel::class.java]
        viewModel.getAvailableSchedules()
        with(viewModel) {
            availableSchedulesLive.observeForever {
                it.let {
                    binding.apply {
                        cardViewList.adapter = AvailableSchedulesAdapters(binding.root.context, it).apply {
                            setOnItemClickListener(object : AvailableSchedulesAdapters.OnItemClickListener {
                                override fun onItemClick(position: Int, schedule: Schedule) {
                                    alertDialogWithEditText(schedule.data)
                                }
                            })
                        }
                    }
                }
            }
        }
    }
    private fun alertDialogWithEditText(date: String) {
        val customAlertViewBinding = FragmentPopUpRegisterScheduleBinding.inflate(layoutInflater)
        customAlertViewBinding.aptoView.setText(date)

       AlertDialog.Builder(requireContext())
            .setTitle("Agendamento")
            .setView(customAlertViewBinding.root)
            .setPositiveButton("Confirmar") { dialog, _ ->
                println("Data que está no input: $date")
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun getCurrentDate(): String =
        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Calendar.getInstance().time)

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calendar.time)
    }
}