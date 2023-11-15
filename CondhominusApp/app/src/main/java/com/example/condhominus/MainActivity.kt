package com.example.condhominus

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.condhominus.databinding.ActivityMainBinding
import com.example.condhominus.ext.UserSharedPreferences
import com.example.condhominus.ext.replaceFragmentWithAnimation
import com.example.condhominus.ui.home.HomeFragment
import com.example.condhominus.ui.login.LoginFragment
import com.example.condhominus.ui.register.RegisterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        replaceFragmentWithAnimation(HomeFragment.newInstance(), R.id.container, false)
        setContentView(binding.root)
        setupToolbar(true)
        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        setupBottomNavigation()
    }

    override fun onBackPressed() {
        setupToolbar(true)
        with(supportFragmentManager) {
            if (backStackEntryCount >= 1) {
                Handler().postDelayed({
                    popBackStackImmediate()
                }, 200)
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.apply {
            menu.findItem(R.id.navLogout).isVisible = !UserSharedPreferences(this@MainActivity).getUserDocument().isNullOrEmpty()
            menu.findItem(R.id.navRegister).isVisible = !UserSharedPreferences(this@MainActivity).getUserDocument().isNullOrEmpty()
            setOnItemSelectedListener  { menuItem ->
                when (menuItem.itemId) {
                    R.id.navHome -> {
                        setupToolbar(true)
                        with(supportFragmentManager) {
                            while (backStackEntryCount >= 1) {
                                popBackStackImmediate()
                            }
                        }
                        true
                    }
                    R.id.navProfile -> {
                        setupToolbar(false)
                        replaceFragmentWithAnimation(LoginFragment.newInstance(), R.id.container, true)
                        true
                    }
                    R.id.navRegister -> {
                        replaceFragmentWithAnimation(RegisterFragment.newInstance(), R.id.container, true)
                        true
                    }
                    R.id.navLogout -> {
                        alertDialogLogOut()
                        false
                    }
                    else -> false
                }
            }
        }
    }

    private fun alertDialogLogOut() {
        AlertDialog.Builder(this)
            .setTitle("Sair")
            .setMessage("Deseja realmente sair do aplicativo?")
            .setPositiveButton("Sair") { dialog, _ ->
                UserSharedPreferences(this).deleteUserDocument()
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                }
                this.finish()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun setupToolbar(showIcon: Boolean) {
        with(binding) {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                if (showIcon) {
                    setHomeAsUpIndicator(ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_profile, null))
                    setDisplayHomeAsUpEnabled(true)
                } else {
                    setDisplayHomeAsUpEnabled(false)
                }
            }

            toolbar.setNavigationOnClickListener {
                setupToolbar(false)
                replaceFragmentWithAnimation(LoginFragment.newInstance(), R.id.container, true)
            }
        }
    }
}