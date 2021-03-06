package com.ekkoe.fitty.ui.home

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ekkoe.fitty.R
import com.ekkoe.fitty.api.ApiService
import com.ekkoe.fitty.data.Article
import com.ekkoe.fitty.extension.dp
import com.ekkoe.fitty.extension.removeBlank

class HomeArticleAdapter : PagingDataAdapter<Article, ArticleViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder.create(parent)

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}


class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
    private val tvSubTitle: TextView = itemView.findViewById(R.id.tv_subtitle)
    private val llTag: LinearLayout = itemView.findViewById(R.id.tag)
    private val tvChapter: TextView = itemView.findViewById(R.id.tv_chapter)
    private val tvTime: TextView = itemView.findViewById(R.id.tv_time)
    private val ivCover: ImageView = itemView.findViewById(R.id.iv_cover)
    private var article: Article? = null

    init {
        itemView.setOnClickListener {

        }
    }

    fun bind(data: Article?) {
        article = data
        data?.run {
            llTag.removeAllViews()
            tvTitle.text = HtmlCompat.fromHtml(title, FROM_HTML_MODE_LEGACY)
            tvSubTitle.visibility = if (desc.isNullOrBlank()) View.GONE else View.VISIBLE
            tvSubTitle.text = desc?.let { HtmlCompat.fromHtml(it, FROM_HTML_MODE_LEGACY) }.toString().removeBlank()
            if (fresh)
                addTagView(itemView.context.getString(R.string.fresh), R.color.red_700)

            if (type == 1) {
                addTagView(itemView.context.getString(R.string.top_article), R.color.red_700)
            }

            //tag
            tags?.forEach { tag ->
                addTagView(tag.name, R.color.teal_A400)
            }

            //??????
            val tvAuthor = TextView(itemView.context).also {
                it.setTextAppearance(R.style.TextAppearance_MaterialComponents_Caption)
                it.text = if (author.isNullOrBlank()) shareUser else author
            }
            llTag.addView(tvAuthor)

            tvChapter.text =
                HtmlCompat.fromHtml("$superChapterName??$chapterName", FROM_HTML_MODE_LEGACY)
            tvTime.text = niceDate
            ivCover.visibility = if (envelopePic.isNullOrBlank()) View.GONE else View.VISIBLE
            getCoverUrl(envelopePic)?.let {
                Glide.with(itemView.context).load(it).into(ivCover)
            }
        }
    }

    private fun addTagView(name: String, @ColorRes res: Int) {
        val drawable = (ContextCompat.getDrawable(
            itemView.context,
            R.drawable.bg_rect_round_2_stroke
        ) as? GradientDrawable)?.also {
            it.setStroke(1.dp, ContextCompat.getColor(itemView.context, res))
        }

        val tagView = TextView(itemView.context).apply {
            text = name
            background = drawable
            setPadding(3.dp, 2.dp, 3.dp, 2.dp)
            setTextAppearance(R.style.TextAppearance_MaterialComponents_Caption)
            setTextColor(ContextCompat.getColor(itemView.context, res))
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).also { it.marginEnd = 6.dp }
        llTag.addView(tagView, params)
    }

    private fun getCoverUrl(url: String?): String? {
        return url?.let {
            if (it.isEmpty()) {
                null
            } else if (!it.startsWith("http")) { //??????url???????????????host
                ApiService.BASE_URL.plus(it)
            } else if (!it.startsWith("https")) { //???http???????????????https
                it.replace("http", "https")
            } else {
                it
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup) = ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_item_article, parent, false
            )
        )
    }
}


