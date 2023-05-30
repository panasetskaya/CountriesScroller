package com.panasetskaia.countriesscroller.presentation.all_countries_screen

data class ScreenState<out T>(
    val status: ScreenStatus,
    val errorState: ErrorState,
    val data: T?,
    val filtersApplied: Boolean
) {

    companion object {
        fun <T> loading(): ScreenState<T> {
            return ScreenState(ScreenStatus.LOADING, ErrorState.perfect(), null, false)
        }

        fun <T> finished(
            errorState: ErrorState,
            data: T?,
            filters: Boolean
        ): ScreenState<T> {
            return ScreenState(ScreenStatus.FINISHED, errorState, data, filters)
        }
    }
}

enum class ScreenStatus {
    FINISHED,
    LOADING
}