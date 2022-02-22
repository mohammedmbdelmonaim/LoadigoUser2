package com.mywork.loadigouser.ui.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseActivity
import com.mywork.loadigouser.databinding.ActivityAuthBinding
import com.mywork.loadigouser.databinding.ActivityBoardingBinding
import com.mywork.loadigouser.databinding.ActivityUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : BaseActivity() {
    private var _binding: ActivityUserBinding? = null
    val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}