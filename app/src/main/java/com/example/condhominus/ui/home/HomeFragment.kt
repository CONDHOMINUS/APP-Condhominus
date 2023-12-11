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
import com.example.condhominus.model.login.Login
import com.example.condhominus.ui.financial.FinancialFragment
import com.example.condhominus.ui.scheduling.SchedulingFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private var user: Login? = null
    private lateinit var listener : HomeFragmentListener

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        user = UserSharedPreferences(requireActivity() as AppCompatActivity).getUserSaved()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener = requireActivity() as HomeFragmentListener
        sharedPreferences = requireActivity().getSharedPreferences("preferences_session", Context.MODE_PRIVATE)
        with(binding) {
            agendamentoMudanca.setOnClickListener{
                (requireActivity() as AppCompatActivity).replaceFragmentWithAnimation(SchedulingFragment.newInstance(), R.id.container, true)
            }
            financeiro.setOnClickListener{
                (requireActivity() as AppCompatActivity).replaceFragmentWithAnimation(FinancialFragment.newInstance(), R.id.container, true)
            }

            if (user?.administrador == true) {
                adminViewGroup.visible()
                regularViewGroup.gone()
                adminNameTitleView.text = activity?.getString(R.string.title_admin_name, user!!.nomeUsuario)
                cadastroInquilinoViewGroup.setOnClickListener {
                    listener.onNavigateToRegisterTenant()
                }
                cadastroCondominioViewGroup.setOnClickListener {
                    listener.onNavigateToRegisterCondominium()
                }
            } else {
                adminViewGroup.gone()
                regularViewGroup.visible()
            }
        }
        listener.setupBottomNavigationItems()
    }

    override fun onStart() {
        super.onStart()
        with(binding) {
            if (user != null) {
                userLoggedTitle.apply {
                    visible()
                    text = (requireActivity().resources.getString(R.string.logged, user!!.nomeUsuario))
                }
            } else {
                userLoggedTitle.gone()
                welcomeView.visible()
                userDontLoginView.apply {
                    visible(true)
                    setOnClickListener {
                        listener?.onNavigateToLogin()
                    }
                }
                agendamentoMudanca.gone(true)
                card2.gone(true)
                financeiro.gone(true)
                card4.gone(true)
            }
        }
    }

    interface HomeFragmentListener {
        fun onNavigateToLogin()
        fun onNavigateToRegisterCondominium()
        fun onNavigateToRegisterTenant()
        fun setupBottomNavigationItems()
    }
}