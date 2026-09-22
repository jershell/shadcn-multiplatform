package com.github.jershell.shadcn.services

import com.github.jershell.shadcn.data.JSON
import com.github.jershell.shadcn.services.models.AppSession
import com.github.jershell.shadcn.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppService(
    private val fileService: FileService,
) {
    private val sessionFile = ".demoapp_session.json"
    private val _session = MutableStateFlow(AppSession())
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var saveJob: Job? = null

    init {
        loadSession()
    }

    fun loadSession() {
        val content = fileService.readTextFile(sessionFile)
        if (content != null) {
            try {
                _session.value = JSON.decodeFromString(AppSession.serializer(), content)
            } catch (e: Exception) {
                // On error, keep the default session
                Log.error("Failed to load session: ${e.message}")
            }
        }
    }

    suspend fun saveSession() {
        try {
            val content = JSON.encodeToString(AppSession.serializer(), _session.value)
            fileService.writeTextFile(sessionFile, content)
        } catch (e: Exception) {
            Log.error("Failed to save session: ${e.message}")
        }
    }

    fun updateSession(update: AppSession.() -> AppSession) {
        _session.value = _session.value.update()
        // Cancel the previous save job if any
        saveJob?.cancel()
        // Start a new save job with a small delay
        saveJob = scope.launch {
            delay(500) // Small delay for debouncing
            saveSession()
        }
    }

    val session: StateFlow<AppSession> = _session.asStateFlow()

    val isDark = session.map {
        it.isDark
    }.stateIn(scope, started = SharingStarted.WhileSubscribed(), false)

    fun setIsDark(value: Boolean) {
        updateSession {
            copy(isDark = value)
        }
    }

    fun setLastScreen(path: String?) {
        Log.debug("setLastScreen $path")
        updateSession {
            copy(lastScreen = path)
        }
    }

    fun setLastPreset(code: String?) {
        updateSession {
            copy(lastPreset = code)
        }
    }

    fun updateComponentSettings(componentId: String, settings: Map<String, String>) {
        updateSession {
            copy(componentSettings = componentSettings + (componentId to settings))
        }
    }

    fun dispose() {
        scope.cancel()
    }
}
