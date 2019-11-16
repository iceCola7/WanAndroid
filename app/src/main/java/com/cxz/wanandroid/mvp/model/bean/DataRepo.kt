package com.cxz.wanandroid.mvp.model.bean

import com.squareup.moshi.Json
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * Created by chenxz on 2018/4/21.
 */
//data class HttpResult<T>(@Json(name = "data") val data: T,
//                         @Json(name = "errorCode") val errorCode: Int,
//                         @Json(name = "errorMsg") val errorMsg: String)

data class HttpResult<T>(
        @Json(name = "data") val data: T
) : BaseBean()

// 通用的带有列表数据的实体
data class BaseListResponseBody<T>(
        @Json(name = "curPage") val curPage: Int,
        @Json(name = "datas") val datas: List<T>,
        @Json(name = "offset") val offset: Int,
        @Json(name = "over") val over: Boolean,
        @Json(name = "pageCount") val pageCount: Int,
        @Json(name = "size") val size: Int,
        @Json(name = "total") val total: Int
)

//文章
data class ArticleResponseBody(
        @Json(name = "curPage") val curPage: Int,
        @Json(name = "datas") var datas: MutableList<Article>,
        @Json(name = "offset") val offset: Int,
        @Json(name = "over") val over: Boolean,
        @Json(name = "pageCount") val pageCount: Int,
        @Json(name = "size") val size: Int,
        @Json(name = "total") val total: Int
)

//文章
data class Article(
        @Json(name = "apkLink") val apkLink: String,
        @Json(name = "audit") val audit: Int,
        @Json(name = "author") val author: String,
        @Json(name = "chapterId") val chapterId: Int,
        @Json(name = "chapterName") val chapterName: String,
        @Json(name = "collect") var collect: Boolean,
        @Json(name = "courseId") val courseId: Int,
        @Json(name = "desc") val desc: String,
        @Json(name = "envelopePic") val envelopePic: String,
        @Json(name = "fresh") val fresh: Boolean,
        @Json(name = "id") val id: Int,
        @Json(name = "link") val link: String,
        @Json(name = "niceDate") val niceDate: String,
        @Json(name = "niceShareDate") val niceShareDate: String,
        @Json(name = "origin") val origin: String,
        @Json(name = "prefix") val prefix: String,
        @Json(name = "projectLink") val projectLink: String,
        @Json(name = "publishTime") val publishTime: Long,
        @Json(name = "shareDate") val shareDate: String,
        @Json(name = "shareUser") val shareUser: String,
        @Json(name = "superChapterId") val superChapterId: Int,
        @Json(name = "superChapterName") val superChapterName: String,
        @Json(name = "tags") val tags: MutableList<Tag>,
        @Json(name = "title") val title: String,
        @Json(name = "type") val type: Int,
        @Json(name = "userId") val userId: Int,
        @Json(name = "visible") val visible: Int,
        @Json(name = "zan") val zan: Int,
        @Json(name = "top") var top: String
)

data class Tag(
        @Json(name = "name") val name: String,
        @Json(name = "url") val url: String
)

//轮播图
data class Banner(
        @Json(name = "desc") val desc: String,
        @Json(name = "id") val id: Int,
        @Json(name = "imagePath") val imagePath: String,
        @Json(name = "isVisible") val isVisible: Int,
        @Json(name = "order") val order: Int,
        @Json(name = "title") val title: String,
        @Json(name = "type") val type: Int,
        @Json(name = "url") val url: String
)

data class HotKey(
        @Json(name = "id") val id: Int,
        @Json(name = "link") val link: String,
        @Json(name = "name") val name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "visible") val visible: Int
)

//常用网站
data class Friend(
        @Json(name = "icon") val icon: String,
        @Json(name = "id") val id: Int,
        @Json(name = "link") val link: String,
        @Json(name = "name") val name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "visible") val visible: Int
)

//知识体系
data class KnowledgeTreeBody(
        @Json(name = "children") val children: MutableList<Knowledge>,
        @Json(name = "courseId") val courseId: Int,
        @Json(name = "id") val id: Int,
        @Json(name = "name") val name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "parentChapterId") val parentChapterId: Int,
        @Json(name = "visible") val visible: Int
) : Serializable

data class Knowledge(
        @Json(name = "children") val children: List<Any>,
        @Json(name = "courseId") val courseId: Int,
        @Json(name = "id") val id: Int,
        @Json(name = "name") val name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "parentChapterId") val parentChapterId: Int,
        @Json(name = "visible") val visible: Int
) : Serializable

// 登录数据
data class LoginData(
        @Json(name = "chapterTops") val chapterTops: MutableList<String>,
        @Json(name = "collectIds") val collectIds: MutableList<String>,
        @Json(name = "email") val email: String,
        @Json(name = "icon") val icon: String,
        @Json(name = "id") val id: Int,
        @Json(name = "password") val password: String,
        @Json(name = "token") val token: String,
        @Json(name = "type") val type: Int,
        @Json(name = "username") val username: String
)

//收藏网站
data class CollectionWebsite(
        @Json(name = "desc") val desc: String,
        @Json(name = "icon") val icon: String,
        @Json(name = "id") val id: Int,
        @Json(name = "link") var link: String,
        @Json(name = "name") var name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "userId") val userId: Int,
        @Json(name = "visible") val visible: Int
)

data class CollectionArticle(
        @Json(name = "author") val author: String,
        @Json(name = "chapterId") val chapterId: Int,
        @Json(name = "chapterName") val chapterName: String,
        @Json(name = "courseId") val courseId: Int,
        @Json(name = "desc") val desc: String,
        @Json(name = "envelopePic") val envelopePic: String,
        @Json(name = "id") val id: Int,
        @Json(name = "link") val link: String,
        @Json(name = "niceDate") val niceDate: String,
        @Json(name = "origin") val origin: String,
        @Json(name = "originId") val originId: Int,
        @Json(name = "publishTime") val publishTime: Long,
        @Json(name = "title") val title: String,
        @Json(name = "userId") val userId: Int,
        @Json(name = "visible") val visible: Int,
        @Json(name = "zan") val zan: Int
)

// 导航
data class NavigationBean(
        val articles: MutableList<Article>,
        val cid: Int,
        val name: String
)

// 项目
data class ProjectTreeBean(
        @Json(name = "children") val children: List<Any>,
        @Json(name = "courseId") val courseId: Int,
        @Json(name = "id") val id: Int,
        @Json(name = "name") val name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "parentChapterId") val parentChapterId: Int,
        @Json(name = "visible") val visible: Int
)

// 热门搜索
data class HotSearchBean(
        @Json(name = "id") val id: Int,
        @Json(name = "link") val link: String,
        @Json(name = "name") val name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "visible") val visible: Int
)

// 搜索历史
data class SearchHistoryBean(val key: String) : LitePalSupport() {
    val id: Long = 0
}

// TODO工具 类型
data class TodoTypeBean(
        val type: Int,
        val name: String,
        var isSelected: Boolean
)

// TODO实体类
data class TodoBean(
        @Json(name = "id") val id: Int,
        @Json(name = "completeDate") val completeDate: String,
        @Json(name = "completeDateStr") val completeDateStr: String,
        @Json(name = "content") val content: String,
        @Json(name = "date") val date: Long,
        @Json(name = "dateStr") val dateStr: String,
        @Json(name = "status") val status: Int,
        @Json(name = "title") val title: String,
        @Json(name = "type") val type: Int,
        @Json(name = "userId") val userId: Int,
        @Json(name = "priority") val priority: Int
) : Serializable

data class TodoListBean(
        @Json(name = "date") val date: Long,
        @Json(name = "todoList") val todoList: MutableList<TodoBean>
)

// 所有TODO，包括待办和已完成
data class AllTodoResponseBody(
        @Json(name = "type") val type: Int,
        @Json(name = "doneList") val doneList: MutableList<TodoListBean>,
        @Json(name = "todoList") val todoList: MutableList<TodoListBean>
)

data class TodoResponseBody(
        @Json(name = "curPage") val curPage: Int,
        @Json(name = "datas") val datas: MutableList<TodoBean>,
        @Json(name = "offset") val offset: Int,
        @Json(name = "over") val over: Boolean,
        @Json(name = "pageCount") val pageCount: Int,
        @Json(name = "size") val size: Int,
        @Json(name = "total") val total: Int
)

// 新增TODO的实体
data class AddTodoBean(
        @Json(name = "title") val title: String,
        @Json(name = "content") val content: String,
        @Json(name = "date") val date: String,
        @Json(name = "type") val type: Int
)

// 更新TODO的实体
data class UpdateTodoBean(
        @Json(name = "title") val title: String,
        @Json(name = "content") val content: String,
        @Json(name = "date") val date: String,
        @Json(name = "status") val status: Int,
        @Json(name = "type") val type: Int
)

// 公众号列表实体
data class WXChapterBean(
        @Json(name = "children") val children: MutableList<String>,
        @Json(name = "courseId") val courseId: Int,
        @Json(name = "id") val id: Int,
        @Json(name = "name") val name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "parentChapterId") val parentChapterId: Int,
        @Json(name = "userControlSetTop") val userControlSetTop: Boolean,
        @Json(name = "visible") val visible: Int
)

// 用户个人信息
data class UserInfoBody(
        @Json(name = "coinCount") val coinCount: Int, // 总积分
        @Json(name = "rank") val rank: Int, // 当前排名
        @Json(name = "userId") val userId: Int,
        @Json(name = "username") val username: String
)

// 个人积分实体
data class UserScoreBean(
        @Json(name = "coinCount") val coinCount: Int,
        @Json(name = "date") val date: Long,
        @Json(name = "desc") val desc: String,
        @Json(name = "id") val id: Int,
        @Json(name = "reason") val reason: String,
        @Json(name = "type") val type: Int,
        @Json(name = "userId") val userId: Int,
        @Json(name = "userName") val userName: String
)

// 排行榜实体
data class CoinInfoBean(
        @Json(name = "coinCount") val coinCount: Int,
        @Json(name = "level") val level: Int,
        @Json(name = "rank") val rank: Int,
        @Json(name = "userId") val userId: Int,
        @Json(name = "username") val username: String
)

// 我的分享
data class ShareResponseBody(
        val coinInfo: CoinInfoBean,
        val shareArticles: ArticleResponseBody
)
