package com.example.condhominus.ui.scheduling

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.condhominus.databinding.FragmentSchedulingBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SchedulingFragment : Fragment() {

    private var _binding: FragmentSchedulingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSchedulingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            textDate.text = getCurrentDate()
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                textDate.text = formatDate(year, month, dayOfMonth)
            }
        }
    }

    private fun getCurrentDate(): String = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Calendar.getInstance().time)

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