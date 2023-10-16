package com.example.data.util

class InsertionException(override val message: String?) : Throwable()

class UpdateException(override val message: String?) : Throwable()

class UpsertException(override val message: String?) : Throwable()

class GenericException(override val message: String = "Oops. Something went wrong!") : Throwable()