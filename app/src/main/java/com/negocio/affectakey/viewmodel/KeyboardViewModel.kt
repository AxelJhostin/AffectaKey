package com.negocio.affectakey.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class KeyboardViewModel : ViewModel() {

    private val _keyEvents = MutableSharedFlow<String>()
    val keyEvents = _keyEvents.asSharedFlow()

    val row1 = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
    val row2 = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l", "ñ")
    val row3 = listOf("z", "x", "c", "v", "b", "n", "m", "⌫")
    val row4 = listOf(",", " ", ".", "↵")

    fun onKeyPressed(key: String) {
        viewModelScope.launch {
            _keyEvents.emit(key)
        }
    }
}
