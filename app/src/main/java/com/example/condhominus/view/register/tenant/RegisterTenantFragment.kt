package com.example.condhominus.view.register.tenant

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.condhominus.customview.CondhominusEditTextAutoCompleteView
import com.example.condhominus.databinding.FragmentRegisterTenantBinding
import com.example.condhominus.ext.DateMask
import com.example.condhominus.ext.DocumentMask
import com.example.condhominus.ext.formatBornDate
import com.example.condhominus.ext.gone
import com.example.condhominus.ext.visible
import com.example.condhominus.model.condominium.CondominiumItem
import com.example.condhominus.model.tenant.Person
import com.example.condhominus.model.tenant.Tenant
import com.example.condhominus.view.register.viewmodel.RegisterTenantViewModel

class RegisterTenantFragment : Fragment() {

    private var _binding: FragmentRegisterTenantBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterTenantViewModel
    private var listCondominiums: List<CondominiumItem>? = null
    private var condominiumIdSelected: Int? = null

    companion object {
        @JvmStatic
        fun newInstance() = RegisterTenantFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterTenantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterTenantViewModel::class.java]
        viewModel.getCondominiums()
        hideViews()
        showLoading()

        with(binding) {
            bornView.addTextChangedListener(DateMask(bornView))
            documentView.addTextChangedListener(DocumentMask(documentView))
            buttonSave.setOnClickListener {
                if (!condominioAutoCompleteView.getTextValue().isNullOrEmpty() && nameView.text.toString().isNotEmpty() && aptoView.text.toString().isNotEmpty() && bornView.text.toString().isNotEmpty()) {
                    warningAllFields.gone()
                    viewModel.setTenant(
                        Tenant(
                            aptoView.text.toString(),
                            condominiumIdSelected ?: 0,
                            Person(
                                nameView.text.toString(),
                                documentView.text.toString().replace("\\D".toRegex(), ""),
                                bornView.text.toString().formatBornDate()
                            )
                        )
                    )
                } else {
                    warningAllFields.visible()
                }
            }
        }

        with(viewModel) {
            errorLive.observe(viewLifecycleOwner, Observer {
                println(it)
                hideLoading()
                hideViews()
                showErrorMessage("Aconteceu um erro, tente cadastrar novamente!")
            })

            registerTenantLive.observe(viewLifecycleOwner, Observer {
                if (it.first) {
                    showLoading()
                    hideViews()
                } else {
                    hideLoading()
                    showViews()
                    alertDialogRegister()
                }
            })

            listCondominiumsLive.observeForever { listCondominiums ->
                if (!listCondominiums.condominios.isNullOrEmpty()) {
                    hideLoading()
                    showViews()
                    this@RegisterTenantFragment.listCondominiums = listCondominiums.condominios
                    bindAutoCompleteEditText(listCondominiums.condominios.map { it.descricaoCondominio })

                } else {
                    hideLoading()
                    hideViews()
                    showErrorMessage("Para cadastrar um inquilino, você precisa ter condomínios cadastrados.")
                }
            }
        }
    }

    private fun alertDialogRegister() {
        AlertDialog.Builder(context)
            .setTitle("Cadastro")
            .setMessage("Inquilino ${binding.nameView.text.toString()} cadastrado com sucesso!")
            .setNegativeButton("OK") { dialog, _ ->
                clearFields()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun clearFields() {
        with(binding) {
            condominioAutoCompleteView.clearField()
            nameView.text ?.clear()
            bornView.text?.clear()
            documentView.text?.clear()
            aptoView.text?.clear()
        }
    }


    private fun showLoading() {
        binding.loadingProgress.visible()
    }

    private fun hideLoading() {
        binding.loadingProgress.gone()
    }

    private fun showViews() {
        binding.registerViewGroup.visible()
    }

    private fun hideViews() {
        binding.registerViewGroup.gone()
    }

    private fun showErrorMessage(message: String) {
        binding.errorMessageView.apply {
            visible()
            text = message
        }
    }

    private fun bindAutoCompleteEditText(condominiumName: List<String>) {
        with(binding) {
            condominioAutoCompleteView.initViews(condominiumName, object :
                CondhominusEditTextAutoCompleteView.AutoCompleteListener {
                override fun onItemClicked(itemClicked: String) {
                    findCondominiumIdSelected(itemClicked)
                }

                override fun onEmptyList() {
                    println("lista vazia")
                }
            })
        }
    }

    fun findCondominiumIdSelected(name: String): Int? {
        listCondominiums?.let {
            for (item in it) {
                if (item.descricaoCondominio == name) {
                    this.condominiumIdSelected = item.idCondominio
                }
            }
        }
        return null
    }
}