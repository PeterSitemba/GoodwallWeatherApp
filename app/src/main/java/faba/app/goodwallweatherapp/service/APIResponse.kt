package faba.app.goodwallweatherapp.service

import faba.app.goodwallweatherapp.utils.Status

data class APIResponse<ResultType>(
    var status: Status,
    var data: ResultType? = null,
    var errorMessage: String? = null
) {

    companion object {
        /**
         * Creates [Resource] object with `SUCCESS` status and [data].
         * Returning object of Resource(Status.SUCCESS, data, null)  last value is null so passing it optionally
         */
        fun <ResultType> success(data: ResultType): APIResponse<ResultType> =
            APIResponse(Status.SUCCESS, data)

        /**
         * Creates [Resource] object with `LOADING` status to notify
         * the UI to showing loading.
         * Returning object of Resource(Status.SUCCESS, null, null) last two values are null so passing them optionally
         */
        fun <ResultType> loading(): APIResponse<ResultType> = APIResponse(Status.LOADING)

        /**
         * Creates [Resource] object with `ERROR` status and [message].
         * Returning object of Resource(Status.ERROR, errorMessage = message)
         */
        fun <ResultType> error(message: String?): APIResponse<ResultType> =
            APIResponse(Status.ERROR, errorMessage = message)

    }
}