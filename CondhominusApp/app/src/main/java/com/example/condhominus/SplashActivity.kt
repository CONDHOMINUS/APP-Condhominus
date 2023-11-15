package com.example.condhominus

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.condhominus.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        removeActionBar()
        initializeSplash()
    }

    private fun initializeSplash() {
        CoroutineScope(Dispatchers.Main).launch {
            waitSplashScreenTimer()
        }
    }

    private fun waitSplashScreenTimer() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            callNewActivity()
            startActivity(intent)
            finish()
        }
    }

    private fun callNewActivity() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    private fun removeActionBar() {
        supportActionBar?.hide()
    }
}