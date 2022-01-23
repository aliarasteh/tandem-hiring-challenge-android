package net.tandem.data.errors

class FieldError(
    val fieldName: String,
    val errorList: List<String>? = null
) : GeneralError {


    companion object {
        fun instance(fieldName: String, error: String): FieldError {
            return FieldError(fieldName, listOf(error))
        }
    }
}