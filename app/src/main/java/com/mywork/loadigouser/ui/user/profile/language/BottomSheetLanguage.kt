package com.mywork.loadigouser.ui.user.profile.language

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mywork.loadigouser.R
import com.mywork.loadigouser.databinding.BottomSheetLangsBinding

class BottomSheetLanguage(private val lang: String) : BottomSheetDialogFragment() {


    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: BottomSheetLangsBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetLangsBinding.inflate(inflater, container, false)
        return binding.root
    }




    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (lang == "en"){
            binding.txtEn.setTextColor(R.color.grey)
            binding.txtEn.isEnabled = false
        } else{
            binding.txtAr.setTextColor(R.color.grey)
            binding.txtAr.isEnabled = false
        }

        binding.txtEn.setOnClickListener {
            onItemBottomSheetClickListener?.let { it1 ->
                it1("en")
            }
        }

        binding.txtAr.setOnClickListener {
            onItemBottomSheetClickListener?.let { it1 ->
                it1("ar")
            }
        }

    }


    private var onItemBottomSheetClickListener: ((String) -> Unit)? = null

    fun setOnItemBottomSheetClick(listener: (String) -> Unit) {
        onItemBottomSheetClickListener = listener
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(lang: String): BottomSheetLanguage {
            return BottomSheetLanguage(lang)
        }
    }


}