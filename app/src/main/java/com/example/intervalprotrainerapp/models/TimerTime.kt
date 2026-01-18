package com.example.intervalprotrainerapp.models

data class TimerTime(
    var _seconds: Int
) {
    companion object {
        const val MAX_SECONDS = 59
        const val MAX_MINUTES = 59
    }

    constructor(
        _minutes: Int,
        _seconds: Int
    ) : this(_seconds)

    val seconds
        get() = if (_seconds > MAX_SECONDS)
            _seconds % 60
                else _seconds

    val minutes
        get() = if (_seconds > MAX_SECONDS)
            _seconds / 60
        else 0

    fun returnTime() : String {
        return "$minutes минут $seconds секунд"
    }

    override fun toString(): String {
        val minutesString = if(minutes < 10) "0$minutes" else minutes
        val secondsString = if(seconds < 10) "0$seconds" else seconds
        return "$minutesString:$secondsString"
    }
}
