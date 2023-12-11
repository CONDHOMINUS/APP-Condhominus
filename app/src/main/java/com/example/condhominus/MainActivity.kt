package com.example.condhominus

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.condhominus.databinding.ActivityMainBinding
import com.example.condhominus.ext.UserSharedPreferences
import com.example.condhominus.ext.replaceFragmentWithAnimation
import com.example.condhominus.model.login.Login
import com.example.condhominus.view.home.HomeFragment
import com.example.condhominus.view.login.LoginFragment
import com.example.condhominus.view.register.condominium.RegisterCondominiumFragment
import com.example.condhominus.view.register.tenant.RegisterTenantFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), HomeFragment.HomeFragmentListener, FragmentManager.OnBackStackChangedListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomNavigation: BottomNavigationView
    private var user: Login? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        user = UserSharedPreferences(this).getUserSaved()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        replaceFragmentWithAnimation(HomeFragment.newInstance(), R.id.container, true)
        setContentView(binding.root)
        setupBottomNavigation()

        window.statusBarColor = ContextCompat.getColor(this, R.color.green_pattern_secondary)
        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onBackStackChanged() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        updateBottomNavigationSelection(currentFragment)
    }

    private fun updateBottomNavigationSelection(currentFragment: Fragment?) {
        when (currentFragment) {
            is HomeFragment -> bottomNavigation.selectedItemId = R.id.navHome
            is RegisterCondominiumFragment -> bottomNavigation.selectedItemId = R.id.navRegisterCondominium
            is RegisterTenantFragment -> bottomNavigation.selectedItemId = R.id.navRegisterTenant
            is LoginFragment -> bottomNavigation.selectedItemId = R.id.navLogin
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.apply {
            setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.navHome -> {
                        if (supportFragmentManager.findFragmentById(R.id.container) !is HomeFragment) {
                            replaceFragmentWithAnimation(HomeFragment.newInstance(), R.id.container, true)
                        }
                    }
                    R.id.navRegisterCondominium -> {
                        if (supportFragmentManager.findFragmentById(R.id.container) !is RegisterCondominiumFragment) {
                            replaceFragmentWithAnimation(RegisterCondominiumFragment.newInstance(), R.id.container, true)
                        }
                    }
                    R.id.navRegisterTenant -> {
                        if (supportFragmentManager.findFragmentById(R.id.container) !is RegisterTenantFragment) {
                            replaceFragmentWithAnimation(RegisterTenantFragment.newInstance(), R.id.container, true)
                        }
                    }
                    R.id.navLogin -> {
                        if (supportFragmentManager.findFragmentById(R.id.container) !is LoginFragment) {
                            replaceFragmentWithAnimation(LoginFragment.newInstance(), R.id.container, true)
                        }
                    }
                    R.id.navLogout -> {
                        alertDialogLogOut()
                    }
                }

                true
            }

            setupVisibleNavItems(this)
        }
    }

    private fun setupVisibleNavItems(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.apply {
            if (UserSharedPreferences(this@MainActivity).getUserSaved() != null) {
                if (UserSharedPreferences(this@MainActivity).getUserSaved()!!.administrador) {
                    menu.findItem(R.id.navLogout).isVisible = true
                    menu.findItem(R.id.navRegisterCondominium).isVisible = true
                    menu.findItem(R.id.navRegisterTenant).isVisible = true
                    menu.findItem(R.id.navLogin).isVisible = false
                } else {
                    menu.findItem(R.id.navLogout).isVisible = true
                    menu.findItem(R.id.navRegisterCondominium).isVisible = false
                    menu.findItem(R.id.navRegisterTenant).isVisible = false
                    menu.findItem(R.id.navLogin).isVisible = false
                }
            } else {
                menu.findItem(R.id.navLogout).isVisible = false
                menu.findItem(R.id.navRegisterCondominium).isVisible = false
                menu.findItem(R.id.navRegisterTenant).isVisible = false
                menu.findItem(R.id.navLogin).isVisible = true
            }
        }
    }

    private fun alertDialogLogOut() {
        AlertDialog.Builder(this)
            .setTitle("Sair")
            .setMessage("Deseja realmente sair do aplicativo?")
            .setPositiveButton("Sair") { dialog, _ ->
                UserSharedPreferences(this).deleteUser()
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

    override fun onNavigateToLogin() {
        replaceFragmentWithAnimation(LoginFragment.newInstance(), R.id.container, true)
    }

    override fun onNavigateToRegisterCondominium() {
        replaceFragmentWithAnimation(RegisterCondominiumFragment.newInstance(), R.id.container, true)
    }

    override fun onNavigateToRegisterTenant() {
        replaceFragmentWithAnimation(RegisterTenantFragment.newInstance(), R.id.container, true)
    }

    override fun setupBottomNavigationItems() {
        setupVisibleNavItems(bottomNavigation)
    }
}