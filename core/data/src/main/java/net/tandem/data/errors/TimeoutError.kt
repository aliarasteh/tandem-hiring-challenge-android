package net.tandem.data.errors


class TimeoutError : GeneralError {
    companion object {
        fun instance() = TimeoutError()
    }
}
