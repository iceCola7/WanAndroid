package com.cxz.wanandroid.api

import com.cxz.wanandroid.mvp.model.bean.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by chenxz on 2018/4/21.
 */
interface ApiService {

    /**
     * 获取轮播图
     * http://www.wanandroid.com/banner/json
     */
    @GET("banner/json")
    fun getBanners(): Observable<HttpResult<List<Banner>>>

    /**
     * 获取文章列表
     * http://www.wanandroid.com/article/list/0/json
     * @param pageNum
     */
    @GET("article/list/{pageNum}/json")
    fun getArticles(@Path("pageNum") pageNum: Int): Observable<HttpResult<ArticleResponseBody>>

    /**
     * 获取知识体系
     * http://www.wanandroid.com/tree/json
     */
    @GET("tree/json")
    fun getKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>>

    /**
     * 知识体系下的文章
     * http://www.wanandroid.com/article/list/0/json?cid=168
     * @param page
     * @param cid
     */
    @GET("article/list/{page}/json")
    fun getKnowledgeList(@Path("page") page: Int, @Query("cid") cid: Int): Observable<HttpResult<ArticleResponseBody>>

    /**
     * 导航数据
     * http://www.wanandroid.com/navi/json
     */
    @GET("navi/json")
    fun getNavigationList(): Observable<HttpResult<List<NavigationBean>>>

    /**
     * 项目数据
     * http://www.wanandroid.com/project/tree/json
     */
    @GET("project/tree/json")
    fun getProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>>

}