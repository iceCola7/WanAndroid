package com.cxz.wanandroid.http.exception

/**
 * Created by chenxz on 2018/4/21.
 */
class ApiException : RuntimeException {

    private var code: Int? = null

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}