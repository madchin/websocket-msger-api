package com.example.util

class InsertionException(override val message: String?) : Throwable()

class UpdateException(override val message: String?) : Throwable()

class UpsertException(override val message: String?) : Throwable()

object ForbiddenException : Throwable()

object WrongCredentialsException : Throwable(message = "Provided credentials are wrong")

object GenericException: Throwable(message = "Oops. Something went wrong!")

class DuplicateUserException(override val message: String?) : Throwable()