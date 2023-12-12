package com.example.condhominus.view.scheduling

import AvailableSchedulesAdapters
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.condhominus.databinding.FragmentPopUpRegisterScheduleBinding
import com.example.condhominus.databinding.FragmentSchedulingBinding
import com.example.condhominus.ext.formatDate
import com.example.condhominus.ext.gone
import com.example.condhominus.ext.visible
import com.example.condhominus.model.Schedule
import com.example.condhominus.view.scheduling.viewmodel.SchedulingViewModel

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
                                    alertDialogWithEditText(schedule)
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    private fun alertDialogWithEditText(schedule: Schedule) {
        var selectedRadioButton: AppCompatRadioButton?
        var selectedRadioText: String?
        val customAlertViewBinding = FragmentPopUpRegisterScheduleBinding.inflate(layoutInflater)
        customAlertViewBinding.apply {
            aptoView.apply {
                isEnabled = false
                setText(schedule.data.formatDate())
            }

            when (schedule.periodos.periodoManha) {
                0 -> {
                    radioAfternoonView.isChecked = true
                    radioMorningView.gone()
                }

                1 -> {
                    radioMorningView.visible()
                }
            }

            when (schedule.periodos.periodoTarde) {
                0 -> {
                    radioMorningView.isChecked = true
                    radioAfternoonView.gone()
                }

                2 -> {
                    radioAfternoonView.visible()
                }
            }
        }

       AlertDialog.Builder(requireContext())
            .setTitle("Agendamento ${schedule.data.formatDate()}")
            .setView(customAlertViewBinding.root)
            .setPositiveButton("Agendar") { dialog, _ ->
                with(customAlertViewBinding) {
                    selectedRadioButton = when (radioViewGroup.checkedRadioButtonId) {
                        radioMorningView.id -> radioMorningView
                        radioAfternoonView.id -> radioAfternoonView
                        else -> null
                    }

                     selectedRadioText = selectedRadioButton?.text?.toString()
                    println("Botão de rádio selecionado: $selectedRadioText")
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}