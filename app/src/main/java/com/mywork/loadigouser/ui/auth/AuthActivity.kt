package com.mywork.loadigouser.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseActivity
import com.mywork.loadigouser.databinding.ActivityAuthBinding
import com.mywork.loadigouser.databinding.ActivityBoardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {
    private lateinit var navController: NavController
    private var _binding: ActivityAuthBinding? = null
    val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val isSignUp = intent.getBooleanExtra("isSignUp", false)
        navController = findNavController(R.id.auth_nav)
        var graph: NavGraph? = null

        if (isSignUp)
            graph = navController.navInflater.inflate(R.navigation.auth_nav_graph).also {
                it.startDestination = R.id.registerFragment
            }
        else
            graph = navController.navInflater.inflate(R.navigation.auth_nav_graph).also {
                it.startDestination = R.id.loginFragment
            }

        navController.setGraph(graph, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}