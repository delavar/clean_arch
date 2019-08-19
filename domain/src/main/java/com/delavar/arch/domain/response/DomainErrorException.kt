package com.delavar.arch.domain.response

class DomainErrorException(val errorModel: ErrorModel): Throwable() {
}