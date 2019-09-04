package com.cxz.wanandroid.mvp.model

import com.cxz.wanandroid.base.BaseModel
import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.contract.MainContract
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.UserInfoBody
import io.reactivex.Observable

/**
 * @author chenxz
 * @date 2018/8/30
 * @desc
 */
class MainModel : BaseModel(), MainContract.Model {

    override fun logout(): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.logout()
    }

    override fun getUserInfo(): Observable<HttpResult<UserInfoBody>> {
        return RetrofitHelper.service.getUserInfo()
    }

}