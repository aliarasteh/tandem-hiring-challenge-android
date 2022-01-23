package net.tandem.data.errors

class ServerError(val statusCode: Int, val errorList: List<String>? = null) : GeneralError
