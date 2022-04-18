package net.flow.jetpackmvvm.ext

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

inline fun AppCompatActivity.launchWithScope(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend () -> Unit
){
    lifecycleScope.launch {
        repeatOnLifecycle(state){
            block()
        }
    }
}

inline fun Fragment.launchWithScope(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend () -> Unit
){
    lifecycleScope.launch {
        repeatOnLifecycle(state){
            block()
        }
    }
}