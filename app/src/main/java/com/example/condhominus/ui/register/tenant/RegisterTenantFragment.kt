package com.example.condhominus.ui.register.tenant

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
import com.example.condhominus.ext.gone
import com.example.condhominus.ext.visible
import com.example.condhominus.model.Address
import com.example.condhominus.model.Condominium
import com.example.condhominus.model.Person
import com.example.condhominus.model.Tenant

class RegisterTenantFragment : Fragment() {

    private var _binding: FragmentRegisterTenantBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterTenantViewModel
    private val listCondominium = listOf("Vermont, 89035-212", "Star Gate, 89030-110", "Residencial Arboris, 89037-506")


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

        with(binding) {
            bornView.addTextChangedListener(DateMask(bornView))
            documentView.addTextChangedListener(DocumentMask(documentView))
            buttonSave.setOnClickListener {
                if (!condominioAutoCompleteView.getTextValue().isNullOrEmpty() && nameView.text.toString().isNotEmpty() && bornView.text.toString().isNotEmpty()) {
                    warningAllFields.gone()
                    viewModel.setTenant(Tenant(
                        condominio = Condominium(
                            condominioAutoCompleteView.getTextValue()!!,
                            Address("89035-212", "rua Gen Arthur Koheler", "Blumenau", "SC", "Brasil", Person( nameView.text.toString(), "2002-03-21T00:00:00"))
                        ),
                        pessoa = Person(
                            nameView.text.toString(),
                            "2002-03-21T00:00:00"
                        ),
                        numeroApartamento = "1203"
                    ))
                } else {
                    warningAllFields.visible()
                }
            }

            condominioAutoCompleteView.initViews(listCondominium, object :
                CondhominusEditTextAutoCompleteView.AutoCompleteListener {
                override fun onItemClicked(itemClicked: String) {
                    println("item: $itemClicked")
                }

                override fun onEmptyList() {
                   println("lista vazia")
                }
            })
        }

        with(viewModel) {
            errorLive.observe(viewLifecycleOwner, Observer {
                println(it)
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
        }
    }

    private fun alertDialogRegister() {
        AlertDialog.Builder(context)
            .setTitle("Cadastro")
            .setMessage("Inquilino ${binding.nameView.text.toString()}, cadastrado com sucesso!")
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
}