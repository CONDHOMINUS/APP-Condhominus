package com.example.condhominus.ui.scheduling

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.condhominus.databinding.FragmentSchedulingBinding
import com.example.condhominus.model.Schedule
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SchedulingFragment : Fragment() {

    private var schedulesAdapters: AvailableSchedulesAdapters? = null

    private lateinit var viewModel: SchedulingViewModel

    private var _binding: FragmentSchedulingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                        cardViewList.adapter = AvailableSchedulesAdapters(binding.root.context, it)
                        cardViewList.onItemClickListener = AdapterView.OnItemClickListener {
                                adapterView, view, position, id ->

                            val selectedSchedule = adapterView.getItemAtPosition(position) as Schedule

                            var showPopUp = PopUpRegisterScheduleFragment() // Passar os paramêtros por aqui e carregar na do POPUP
                            showPopUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
                        }
                    }
                }
            }
        }
    }

    private fun getCurrentDate(): String =
        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Calendar.getInstance().time)

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calendar.time)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SchedulingFragment()
    }
}