package com.ekkoe.fitty.api

import java.lang.RuntimeException

data class CustomHttpException(val errorCode: Int, val errorMessage: String) : RuntimeException()