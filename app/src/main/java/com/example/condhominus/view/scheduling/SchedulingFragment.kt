package com.example.condhominus.view.scheduling

import AvailableSchedulesAdapters
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.condhominus.databinding.FragmentPopUpRegisterScheduleBinding
import com.example.condhominus.databinding.FragmentSchedulingBinding
import com.example.condhominus.ext.UserSharedPreferences
import com.example.condhominus.ext.formatDate
import com.example.condhominus.ext.gone
import com.example.condhominus.ext.visible
import com.example.condhominus.model.login.Login
import com.example.condhominus.model.schedule.Schedule
import com.example.condhominus.model.schedule.ScheduleBody
import com.example.condhominus.view.scheduling.viewmodel.SchedulingViewModel

class SchedulingFragment : Fragment() {

    private lateinit var viewModel: SchedulingViewModel
    private var _binding: FragmentSchedulingBinding? = null
    private val binding get() = _binding!!
    private lateinit var date: String
    private lateinit var period: String
    private var user : Login? = null

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
        user = UserSharedPreferences(requireActivity() as AppCompatActivity).getUserSaved()
        viewModel = ViewModelProvider(this)[SchedulingViewModel::class.java]
        viewModel.getAvailableSchedules()
        binding.apply {
            rootViewGroup.gone()
            loadingView.visible()
        }
        with(viewModel) {

            availableSchedulesLive.observeForever { newList ->
                newList?.let {
                    binding.apply {
                        val adapter = AvailableSchedulesAdapters(binding.root.context, it)

                        cardViewList.adapter = adapter.apply {
                            setOnItemClickListener(object : AvailableSchedulesAdapters.OnItemClickListener {
                                override fun onItemClick(position: Int, schedule: Schedule) {
                                    alertDialogWithEditText(schedule)
                                }
                            })
                        }
                        adapter.updateData(it)
                        binding.apply {
                            rootViewGroup.visible()
                            loadingView.gone()
                        }
                    }
                }
            }

//            availableSchedulesLive.observeForever {
//                it.let {
//                    binding.apply {
//                        cardViewList.adapter = AvailableSchedulesAdapters(binding.root.context, it).apply {
//                            setOnItemClickListener(object : AvailableSchedulesAdapters.OnItemClickListener {
//                                override fun onItemClick(position: Int, schedule: Schedule) {
//                                    alertDialogWithEditText(schedule)
//                                }
//                            })
//                        }
//                    }
//                    binding.apply {
//                        rootViewGroup.visible()
//                        loadingView.gone()
//                    }
//                }
//            }

            toScheduleLive.observeForever {
                if (it.sucesso) {
                    it.let {
                        binding.apply {
                            rootViewGroup.visible()
                            loadingView.gone()
                        }
                        showAlertScheduleConfirmation()
                    }
                }
            }
        }
    }

    private fun showAlertScheduleConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Agendamento confirmado!")
            .setMessage("Para dia $date, no período da $period")
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
               viewModel.getAvailableSchedules()
                binding.apply {
                    rootViewGroup.gone()
                    loadingView.visible()
                }
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun alertDialogWithEditText(schedule: Schedule) {
        var selectedRadioButton: AppCompatRadioButton?
        var periodSelected: Int?
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

                    date = schedule.data.formatDate()
                    period = selectedRadioButton?.text.toString()

                    periodSelected = when (period) {
                        "Manhã" -> {
                            1
                        }

                        "Tarde" -> {
                            2
                        }

                        else -> {
                            0
                        }
                    }

                    viewModel.toSchedule(
                        ScheduleBody(
                            schedule.data,
                            periodSelected!!,
                            user?.idUsuario?.toInt() ?: 0
                        )
                    )
                    binding.apply {
                        rootViewGroup.gone()
                        loadingView.visible()
                    }
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