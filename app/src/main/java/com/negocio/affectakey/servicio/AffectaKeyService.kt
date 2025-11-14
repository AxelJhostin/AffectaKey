package com.negocio.affectakey.servicio

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.negocio.affectakey.viewmodel.KeyboardViewModel
import com.negocio.affectakey.vista.KeyboardScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.negocio.affectakey.ui.theme.AffectaKeyTheme
import android.view.ViewGroup

class AffectaKeyService : InputMethodService(),
    SavedStateRegistryOwner,
    ViewModelStoreOwner,
    LifecycleOwner {

    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    override val savedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    private val _viewModelStore = ViewModelStore()
    override val viewModelStore: ViewModelStore
        get() = _viewModelStore

    private val lifecycleRegistry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private lateinit var viewModel: KeyboardViewModel

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        viewModel = ViewModelProvider(this)[KeyboardViewModel::class.java]

        serviceScope.launch {
            viewModel.keyEvents.collectLatest { key ->
                handleKeyPress(key)
            }
        }
    }

    override fun onCreateInputView(): View {
        val composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@AffectaKeyService)
            setViewTreeViewModelStoreOwner(this@AffectaKeyService)
            setViewTreeSavedStateRegistryOwner(this@AffectaKeyService)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, // Ancho: Ocupar toda la pantalla
                ViewGroup.LayoutParams.WRAP_CONTENT  // Alto: Ajustarse al contenido (nuestras 4 filas)
            )

            setContent {
                AffectaKeyTheme {
                    KeyboardScreen(viewModel = viewModel)
                }
            }
        }
        return composeView
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        super.onFinishInputView(finishingInput)
    }

    private fun handleKeyPress(key: String) {
        val ic = currentInputConnection ?: return

        when (key) {
            "⌫" -> {
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
            }
            "↵" -> {
                val action = currentInputEditorInfo.imeOptions and EditorInfo.IME_MASK_ACTION
                when (action) {
                    EditorInfo.IME_ACTION_SEARCH,
                    EditorInfo.IME_ACTION_GO,
                    EditorInfo.IME_ACTION_SEND,
                    EditorInfo.IME_ACTION_DONE -> {
                        ic.performEditorAction(action)
                    }
                    else -> {
                        ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
                        ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
                    }
                }
            }
            else -> {
                ic.commitText(key, 1)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        viewModelStore.clear()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}