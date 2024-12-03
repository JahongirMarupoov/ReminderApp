package uz.marupoov.reminderapp.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber

fun myLogger(message: String, tag: String = "BAHA") {
    Timber.tag(tag).d(message)
}

val timeChangeFlow = MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

var isVisibleActivity = false

val makeToast = MutableSharedFlow<String>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)