package com.example.util

class InsertionException(override val message: String?) : Throwable()

class UpdateException(override val message: String?) : Throwable()

class UpsertException(override val message: String?) : Throwable()

object ForbiddenException : Throwable()

object GenericException: Throwable(message = "Oops. Something went wrong!")