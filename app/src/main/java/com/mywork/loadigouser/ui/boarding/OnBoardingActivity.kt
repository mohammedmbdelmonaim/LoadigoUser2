package com.mywork.loadigouser.ui.boarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.mywork.loadigouser.R
import com.mywork.loadigouser.base.BaseActivity
import com.mywork.loadigouser.databinding.ActivityBoardingBinding
import com.mywork.loadigouser.model.locale.BoardingItem
import com.mywork.loadigouser.model.locale.SharedPreferenceCache
import com.mywork.loadigouser.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import io.paysky.yallasuperapp.ui.boarding.BoardingItemsAdapter
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity() {
    private var _binding: ActivityBoardingBinding? = null
    val binding get() = _binding!!
    private val boardingItemsAdapter = BoardingItemsAdapter()

    @Inject
    lateinit var sharedPreferenceCache: SharedPreferenceCache

    // TODO: Replace the placeholder with real data resources
    private val boardingItemsPlaceHolder = listOf(
        BoardingItem(
            R.drawable.onboard1,
            R.string.onboarding_title_1,
            R.string.onboarding_desc_1
        ),

        BoardingItem(
            R.drawable.onboard2,
            R.string.onboarding_title_2,
            R.string.onboarding_desc_2
        ),

        BoardingItem(
            R.drawable.onboard3,
            R.string.onboarding_title_3,
            R.string.onboarding_desc_3
        )
    )

    private val boardingViewPagerCb = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.boardingItemsIndicator.selection = position
            if (position != boardingItemsAdapter.itemCount.minus(1)) {
                binding.apply {
                    btnSkip.isVisible = true
                    btnNext.isVisible = true
                    btnSignIn.isVisible = false
                    btnSignUp.isVisible = false
                    btnNext.apply {
                        setOnClickListener { binding.onBoardingVp.currentItem = position.plus(1) }
                    }
                }

            } else {
                binding.apply {
                    btnSkip.isVisible = false
                    btnNext.isVisible = false
                    btnSignIn.isVisible = true
                    btnSignUp.isVisible = true
                    btnSignIn.apply {
                        setOnClickListener {
                            // change configurations for on boarding seen preference to prevent the user from seeing this screen again
                            sharedPreferenceCache.saveHasSeenOnBoardingScreens(true)

                            // start authentication screen
                            val intent = Intent(
                                this@OnBoardingActivity,
                                AuthActivity::class.java
                            )
                            intent.putExtra("isSignUp", false)
                            startActivity(intent)
                            finish()
                        }

                        btnSignUp.apply {
                            setOnClickListener {
                                // change configurations for on boarding seen preference to prevent the user from seeing this screen again
                                sharedPreferenceCache.saveHasSeenOnBoardingScreens(true)

                                // start authentication screen
                                val intent = Intent(
                                    this@OnBoardingActivity,
                                    AuthActivity::class.java
                                )
                                intent.putExtra("isSignUp", true)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBoardingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        boardingItemsAdapter.boardingItems = boardingItemsPlaceHolder
        binding.boardingItemsIndicator.count = boardingItemsAdapter.itemCount

        binding.onBoardingVp.adapter = boardingItemsAdapter
        binding.onBoardingVp.registerOnPageChangeCallback(boardingViewPagerCb)

        binding.btnSkip.setOnClickListener {
            binding.onBoardingVp.currentItem = boardingItemsAdapter.itemCount
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.onBoardingVp.unregisterOnPageChangeCallback(boardingViewPagerCb)
        _binding = null
    }
}