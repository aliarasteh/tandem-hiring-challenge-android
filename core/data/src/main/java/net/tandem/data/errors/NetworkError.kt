package net.tandem.data.errors


class NetworkError : GeneralError {
    companion object {
        fun instance() = NetworkError()
    }
}