package com.example.condhominus.ui.register.condominium

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.condhominus.databinding.FragmentRegisterCondominiumBinding
import com.example.condhominus.ext.ZipCodeMask
import com.example.condhominus.ext.gone
import com.example.condhominus.ext.visible
import com.example.condhominus.model.CondominiumRegister
import com.example.condhominus.model.Endereco

class RegisterCondominiumFragment : Fragment() {

    private var _binding: FragmentRegisterCondominiumBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterCondominiumViewModel

    companion object {
        @JvmStatic
        fun newInstance() = RegisterCondominiumFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterCondominiumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterCondominiumViewModel::class.java]

        with(binding) {
            zipCodeView.addTextChangedListener(ZipCodeMask(zipCodeView) {
                viewModel.getAddress(zipCodeView.text.toString().replace("\\D".toRegex(), ""))
                hideKeyboard(zipCodeView)
                condominiumRootView.gone()
                loadingProgress.visible()
                warningView.gone()
            })

            buttonSave.setOnClickListener {
                if (nameView.text!!.isNotEmpty() && zipCodeView.text!!.isNotEmpty() && streetView.text!!.isNotEmpty() && cityView.text!!.isNotEmpty() && countryView.text!!.isNotEmpty() && stateView.text!!.isNotEmpty() && numberView.text!!.isNotEmpty()) {
                    viewModel.registerCondominium(
                        CondominiumRegister(
                            nameView.text!!.toString(),
                            "",
                            Endereco(
                                zipCodeView.text!!.toString(),
                                streetView.text!!.toString(),
                                cityView.text!!.toString(),
                                stateView.text!!.toString(),
                                countryView.text!!.toString(),
                                numberView.text!!.toString()
                            )
                        )
                    )
                    loadingProgress.visible()
                    condominiumRootView.gone()
                } else {
                    warningView.visible()
                }
            }
        }

        with(viewModel) {
            addressLive.observeForever {
                it.let {
                    binding.apply {
                        condominiumRootView.visible()
                        loadingProgress.gone()
                        streetView.setText(it.logradouro)
                        cityView.setText(it.localidade)
                        countryView.setText("Brasil")
                        stateView.setText(it.uf)

                        if (it.cep == null) {
                            zipCodeView.setText("")
                            countryView.setText("")
                            warningView.apply {
                                visible()
                                text = "CEP não encontrado"
                            }
                        }
                    }
                }

                errorAddressLive.observeForever {
                    binding.apply {
                        condominiumRootView.visible()
                        loadingProgress.gone()
                        zipCodeView.setText("")
                        warningView.apply {
                            visible()
                            text = "CEP não encontrado"
                        }
                    }
                }
            }

            condominiumLive.observeForever {
                alertRegisterConfirm(binding.nameView.text!!.toString())
                binding.apply {
                    loadingProgress.gone()
                    condominiumRootView.visible()
                }
            }
        }
    }

    private fun alertRegisterConfirm(condominiumName: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Registrado!")
            .setMessage("Condomínio ${condominiumName} cadastrado com sucesso.")
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
                with(binding) {
                    nameView.setText("")
                    zipCodeView.setText("")
                    streetView.setText("")
                    cityView.setText("")
                    stateView.setText("")
                    countryView.setText("")
                    numberView.setText("")
                }
            }
            .setCancelable(false)
            .create()
            .show()
    }

    fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}