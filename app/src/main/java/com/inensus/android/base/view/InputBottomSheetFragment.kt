package com.inensus.android.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inensus.android.R
import com.inensus.android.databinding.FragmentInputBottomSheetBinding
import com.inensus.android.util.Constants
import com.inensus.android.util.SharedPreferenceWrapper
import org.koin.android.ext.android.inject

class InputBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentInputBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val preferences: SharedPreferenceWrapper by inject()

    override fun getTheme() = R.style.AppTheme_BottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)
        val themedInflater = inflater.cloneInContext(contextThemeWrapper)

        _binding = FragmentInputBottomSheetBinding.inflate(themedInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputView.getTitleView().text = getString(R.string.title_server_url)
        binding.inputView.setText(preferences.baseUrl ?: "")
        binding.inputView.getMainTextView().hint = getString(R.string.hint_server_url)

        binding.negativeButton.setOnClickListener {
            dismiss()
        }

        binding.positiveButton.setOnClickListener {
            if (binding.inputView.getText().matches(Constants.HTTP_REGEX)) {
                preferences.baseUrl = binding.inputView.getText().removeSuffix("/")
                dismiss()
            } else {
                binding.inputView.showErrorMessage(getString(R.string.server_url_validation))
            }
        }
    }

    companion object {
        fun newInstance() = InputBottomSheetFragment()
    }
}
