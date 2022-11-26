package com.rioa.configurer.respond

data class ResponseData<T> (
    val status: Int,
    val message: String,
    val data: T?
){
    companion object{
        fun <T> success(data: T): ResponseData<T> {
            return ResponseData(
                status = ResponseCode.SUCCESS.code,
                message = ResponseCode.SUCCESS.message,
                data = data
            )
        }

        fun <T> error(code: Int, message: String, data: T? = null): ResponseData<T> {
            return ResponseData(
                status = code,
                message = message,
                data = data
            )
        }
    }
}