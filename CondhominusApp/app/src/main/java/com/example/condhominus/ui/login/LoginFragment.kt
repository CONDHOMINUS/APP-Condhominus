package com.example.condhominus.ui.login

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.condhominus.databinding.FragmentLoginBinding
import com.example.condhominus.ext.DocumentMask
import com.example.condhominus.ext.UserSharedPreferences
import com.example.condhominus.ext.gone
import com.example.condhominus.ext.visible

class
LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            if (UserSharedPreferences(requireActivity()).getUserDocument().isNullOrEmpty()) {
                loginDocument.addTextChangedListener(DocumentMask(loginDocument))
                profileViewGroup.gone()
                loginViewGroup.visible()
                buttonLogin.setOnClickListener {
                    if (!loginDocument.text.isNullOrEmpty() && loginDocument.text?.length!! > 11 && loginDocument.text.toString() != "000.000.000-00"  && loginDocument.text.toString() != "999.999.999-99" && !loginPassword.text.isNullOrEmpty()) {
                        warningDocument.gone()
                        UserSharedPreferences(requireActivity()).saveUserDocument(loginDocument.text.toString())
                        Handler().postDelayed({
                            profileViewGroup.visible()
                            loginViewGroup.gone()
                        }, 200)
                    } else {
                        warningDocument.visible()
                    }
                }
            } else {
                profileViewGroup.visible()
                loginViewGroup.gone()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}