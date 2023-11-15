package com.example.condhominus.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class DocumentMask(private val editText: EditText) : TextWatcher {

    private var isUpdating = false
    private val mask = "###.###.###-##"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        if (isUpdating) {
            return
        }

        isUpdating = true

        val unmaskedCpf = editable.toString().replace("[^\\d]".toRegex(), "")

        val formattedCpf = applyMask(unmaskedCpf)

        editText.apply {
            setText(formattedCpf)
            setSelection(formattedCpf.length)
        }

        isUpdating = false
    }

    private fun applyMask(cpf: String): String {
        val maskedText = StringBuilder()
        var i = 0
        var j = 0

        while (i < cpf.length && j < mask.length) {
            if (mask[j] == '#') {
                maskedText.append(cpf[i])
                i++
            } else {
                maskedText.append(mask[j])
            }
            j++
        }

        return maskedText.toString()
    }
}

class DateMask(private val editText: EditText) : TextWatcher {

    private var isUpdating = false
    private val mask = "##/##/####"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        if (isUpdating) {
            return
        }

        isUpdating = true

        val unmaskedDate = editable.toString().replace("[^\\d]".toRegex(), "")

        val formattedDate = applyMask(unmaskedDate)

        editText.apply {
            setText(formattedDate)
            setSelection(formattedDate.length)
        }

        isUpdating = false
    }

    private fun applyMask(date: String): String {
        val maskedText = StringBuilder()
        var i = 0
        var j = 0

        while (i < date.length && j < mask.length) {
            if (mask[j] == '#') {
                maskedText.append(date[i])
                i++
            } else {
                maskedText.append(mask[j])
            }
            j++
        }

        return maskedText.toString()
    }
}

