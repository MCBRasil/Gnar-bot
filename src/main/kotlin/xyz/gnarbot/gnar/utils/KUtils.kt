@file:JvmName("KUtils")
@file:Suppress("NOTHING_TO_INLINE")

package xyz.gnarbot.gnar.utils

import java.io.File
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

inline fun File.child(path: String) = File(this, path)

inline fun ScheduledExecutorService.schedule(duration: Long, timeUnit: TimeUnit, crossinline runnable: () -> Unit) {
    this.schedule({ runnable() }, duration, timeUnit)
}