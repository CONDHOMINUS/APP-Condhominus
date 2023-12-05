package com.example.condhominus.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.condhominus.R

fun AppCompatActivity.replaceFragmentWithAnimation(fragment: Fragment, frameId: Int, addToBackStack: Boolean = false) {
    supportFragmentManager.beginTransaction().apply {
        setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_bottom, 0, R.anim.exit_to_bottom)
        replace(frameId, fragment, fragment.javaClass.simpleName)
        setPrimaryNavigationFragment(fragment)
        if (addToBackStack) {
            addToBackStack(fragment.javaClass.simpleName)
        }
        commit()
    }
}
