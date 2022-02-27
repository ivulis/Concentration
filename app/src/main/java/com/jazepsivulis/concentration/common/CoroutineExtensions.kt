package com.jazepsivulis.concentration.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

fun launchIO(block: suspend CoroutineScope.() -> Unit) = ioScope.launch(
    block = block
)

fun Fragment.launchUI(block: suspend CoroutineScope.() -> Unit) = lifecycleScope.launch {
    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        block()
    }
}
