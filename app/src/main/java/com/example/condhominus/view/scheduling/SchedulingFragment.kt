package com.example.condhominus.view.scheduling

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.condhominus.R
import com.example.condhominus.databinding.FragmentSchedulingBinding
import com.example.condhominus.model.Schedule
import com.example.condhominus.view.scheduling.adapter.AvailableSchedulesAdapters
import com.example.condhominus.ui.scheduling.PopUpRegisterScheduleFragment
import com.example.condhominus.view.scheduling.viewmodel.SchedulingViewModel
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

//                            var showPopUp = PopUpRegisterScheduleFragment() // Passar os paramêtros por aqui e carregar na do POPUP
//                            showPopUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")

                            alertDialogWithEditText()
                            // vc não precisa criar um fragment pra isso, até poderia mas é mais dificil gerenciar
                        }
                    }
                }
            }
        }
    }

//    private fun alertDialogWithEditText(valorInput: String, ...)
    private fun alertDialogWithEditText() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val customView = layoutInflater.inflate(R.layout.fragment_pop_up_register_schedule, null)
        val loginPassword = customView.findViewById<EditText>(R.id.loginPassword)

        //aqui passamos os valores default para os inputs, pegaremos do item clickado no adapter, e recebemos aqui como parametro
        loginPassword.setText("Valor Inicial")

        AlertDialog.Builder(requireContext())
            .setTitle("Informe os dados")
            .setView(customView)
            .setPositiveButton("OK") { dialog, _ ->
            //provavelmente aqui pegaremos os valores definidos nos inputs, algo como idDoInput.text.toString()
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

    companion object {
        @JvmStatic
        fun newInstance() = SchedulingFragment()
    }
}