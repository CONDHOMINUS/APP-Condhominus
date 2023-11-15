package com.example.condhominus.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.condhominus.R
import com.example.condhominus.databinding.FragmentHomeBinding
import com.example.condhominus.ext.UserSharedPreferences
import com.example.condhominus.ext.gone
import com.example.condhominus.ext.replaceFragmentWithAnimation
import com.example.condhominus.ext.visible
import com.example.condhominus.ui.financial.FinancialFragment
import com.example.condhominus.ui.scheduling.SchedulingFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("preferences_session", Context.MODE_PRIVATE)
        with(binding) {
            agendamentoMudanca.setOnClickListener{
                (requireActivity() as AppCompatActivity).replaceFragmentWithAnimation(SchedulingFragment.newInstance(), R.id.container, true)
            }
            financeiro.setOnClickListener{
                (requireActivity() as AppCompatActivity).replaceFragmentWithAnimation(FinancialFragment.newInstance(), R.id.container, true)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        with(binding) {
            if (!UserSharedPreferences(requireActivity()).getUserDocument().isNullOrEmpty()) {
                userLoggedTitle.apply {
                    visible()
                    text = requireActivity().resources.getString(R.string.logged)
                }
            } else {
                userLoggedTitle.gone()
                userDontLoggedTitle.visible(true)
                agendamentoMudanca.gone(true)
                card2.gone(true)
                financeiro.gone(true)
                card4.gone(true)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}