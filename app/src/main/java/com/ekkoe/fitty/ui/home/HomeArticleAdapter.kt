package com.ekkoe.fitty.ui.home

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.view.setPadding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ekkoe.fitty.R
import com.ekkoe.fitty.data.Article
import com.ekkoe.fitty.dp

class HomeArticleAdapter: PagingDataAdapter<Article, ArticleViewHolder>(COMPARATOR) {

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
    private val llTag: LinearLayout = itemView.findViewById(R.id.tag)
    private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
    private val tvAuthor: TextView = itemView.findViewById(R.id.tv_author)
    private val tvTime: TextView = itemView.findViewById(R.id.tv_time)
    private var article: Article? = null

    init {
        itemView.setOnClickListener {

        }
    }

    fun bind(data: Article?) {
        article = data
        data?.run {
            llTag.removeAllViews()
            tvTitle.text = HtmlCompat.fromHtml(title, FROM_HTML_MODE_COMPACT)
            if (fresh)
                addTagView(itemView.context.getString(R.string.fresh), R.color.purple_200)

            tags?.forEach { tag ->
                addTagView(tag.name, R.color.teal_200)
            }
            llTag.visibility = if (llTag.childCount > 0) View.VISIBLE else View.GONE

            tvAuthor.text = if (author.isNullOrBlank()) shareUser else author
            tvSubtitle.text = "$superChapterName·$chapterName"
            tvTime.text = niceDate
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
            setPadding(2.dp)
            setTextColor(ContextCompat.getColor(itemView.context, res))
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).also { it.marginEnd = 3.dp }
        llTag.addView(tagView, params)
    }

    companion object {
        fun create(parent: ViewGroup) = ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_item_article, parent, false
            )
        )
    }
}

