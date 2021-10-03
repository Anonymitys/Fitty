package com.ekkoe.fitty.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class HomeArticle(
    val curPage: Int,
    val datas: MutableList<Article>?,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

@Serializable
data class Article(
    val apkLink: String?,
    val audit: Int,
    val author: String?,
    val canEdit: Boolean,
    val chapterId: Int,
    val chapterName: String?,
    val collect: Boolean,
    val courseId: Int,
    val desc: String?,
    val descMd: String?,
    val envelopePic: String?,
    val fresh: Boolean,
    val host: String?,
    val id: Int,
    val link: String?,
    val niceDate: String?,
    val niceShareDate: String?,
    val origin: String?,
    val prefix: String?,
    val projectLink: String?,
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long?,
    val shareUser: String?,
    val superChapterId: Int,
    val superChapterName: String?,
    val tags: List<Tag>?,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
)


@Serializable
data class Tag(val name: String, val url: String?)

data class Tab(val name: String,val id:Int):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(name)
        p0?.writeInt(id)
    }

    companion object CREATOR : Parcelable.Creator<Tab> {
        override fun createFromParcel(parcel: Parcel): Tab {
            return Tab(parcel)
        }

        override fun newArray(size: Int): Array<Tab?> {
            return arrayOfNulls(size)
        }
    }
}


@Serializable
data class HomeBanner(
    val desc: String?,
    val id: Int,
    val imagePath: String?,
    val isVisible: Int,
    val order: Int,
    val title: String?,
    val type: Int,
    val url: String?
)


