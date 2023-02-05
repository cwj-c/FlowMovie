package com.flow.moviesearch.domain.model

import java.io.IOException

sealed interface DomainException {
    data class MaxPageException(
        override val message: String? = "max page"
    ): DomainException, Exception()

    data class NetworkConnectionException(
        override val message: String? = "Network connection exception"
    ): DomainException, IOException()
}
