package com.murmur.murmur.config.respond

enum class ResponseCode(val code: Int,val message: String) {
    SUCCESS(200, "Success"),
    FAILURE(400, "Failure"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    BAD_REQUEST(400, "Bad Request"),
    UNKNOWN(0, "Unknown")
}