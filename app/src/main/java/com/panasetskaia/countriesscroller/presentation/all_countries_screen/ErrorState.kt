package com.panasetskaia.countriesscroller.presentation.all_countries_screen

import com.panasetskaia.countriesscroller.presentation.base.Event

data class ErrorState(val status: Event<ErrorStatus>, val msg: String?){

    companion object {
        fun error(msg: String?): ErrorState {
            return ErrorState(Event(ErrorStatus.ALL_BAD), msg)
        }
        fun perfect(): ErrorState {
            return ErrorState(Event(ErrorStatus.ALL_GOOD), null)
        }
    }
}

enum class ErrorStatus {
    ALL_GOOD,
    ALL_BAD
}
