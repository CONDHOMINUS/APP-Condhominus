package com.example.condhominus.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.condhominus.R
import com.example.condhominus.databinding.ViewEdittextAutoCompleteBinding

class CondhominusEditTextAutoCompleteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var _binding: ViewEdittextAutoCompleteBinding? = null
    private val binding get() = _binding!!
    private lateinit var listener: AutoCompleteListener
    private var hint: String? = null

    init {
        attrs?.also {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CondhominusEditTextAutoCompleteView, 0, 0)
            hint = typedArray.getString(R.styleable.CondhominusEditTextAutoCompleteView_editTextHint)
            typedArray.recycle()
        }

        _binding = ViewEdittextAutoCompleteBinding.inflate(LayoutInflater.from(context), this, true)
        requireNotNull(_binding).root
    }

    fun initViews(newList: List<String>, listener: AutoCompleteListener) {
        this.listener = listener

        updateAutoCompleteList(newList)
        getSelectedString()
        binding.autoCompleteTextView.isCursorVisible = false
        binding.autoCompleteTextView.hint = this@CondhominusEditTextAutoCompleteView.hint.orEmpty()
    }

    fun updateAutoCompleteList(newList: List<String>) {
        with(binding) {
            if (newList.isNotEmpty()) {
                (autoCompleteTextView as? AutoCompleteTextView)?.setAdapter(ArrayAdapter(context, R.layout.view_list_auto_complete_item, newList))
            }
            autoCompleteTextView.setOnTouchListener { _, _ ->
                if (newList.isNotEmpty()) {
                    showDropdown()
                } else {
                    listener.onEmptyList()
                }
                true
            }
        }
    }

    fun getTextValue(): String? {
        return if (!binding.autoCompleteTextView.text.isNullOrBlank()) {
            binding.autoCompleteTextView.text.toString()
        } else {
            null
        }
    }

    private fun showDropdown() {
        with(binding) {
            autoCompleteTextView.showDropDown()
        }
    }

    private fun getSelectedString() {
        binding.autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            listener.onItemClicked(parent.getItemAtPosition(position) as String)
        }
    }

    fun clearField() {
        binding.autoCompleteTextView.apply {
            setText("")
        }
    }

    interface AutoCompleteListener {
        fun onItemClicked(itemClicked: String)
        fun onEmptyList()
    }
}
