package com.example.util

class InsertionException(override val message: String?) : Throwable()

class UpdateException(override val message: String?) : Throwable()

class UpsertException(override val message: String?) : Throwable()

object ForbiddenException : Throwable()

class UserNotFoundException(override val message: String?) : Throwable()

class ChatNotFoundException(override val message: String?) : Throwable()

object WrongCredentialsException : Throwable(message = "Provided credentials are wrong")

object GenericException: Throwable(message = "Oops. Something went wrong!")

class DuplicateUserException(override val message: String?) : Throwable()