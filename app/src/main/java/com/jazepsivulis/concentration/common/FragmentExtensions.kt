package com.jazepsivulis.concentration.common

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.jazepsivulis.concentration.R

fun Fragment.openFragment(id: Int) = activity?.findNavController(R.id.nav_host)?.run {

    if (!popBackStack(id, false)) {
        navigate(id)
    }
}
