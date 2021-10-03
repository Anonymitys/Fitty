package com.ekkoe.fitty.ui.hierarchy

import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ekkoe.fitty.R
import com.ekkoe.fitty.data.Hierarchy
import com.ekkoe.fitty.data.SubHierarchy
import com.ekkoe.fitty.extension.dp
import com.google.android.material.internal.FlowLayout


class HierarchyAdapter(
    private val itemClick: (Hierarchy) -> Unit,
    private val contentClick: (SubHierarchy) -> Unit
) :
    ListAdapter<Hierarchy, HierarchyViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HierarchyViewHolder =
        HierarchyViewHolder.create(parent, itemClick, contentClick)

    override fun onBindViewHolder(holder: HierarchyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Hierarchy>() {
            override fun areItemsTheSame(oldItem: Hierarchy, newItem: Hierarchy): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Hierarchy, newItem: Hierarchy): Boolean =
                oldItem.id == newItem.id
        }
    }
}


class HierarchyViewHolder(
    itemView: View,
    itemClick: (Hierarchy) -> Unit,
    private val contentClick: (SubHierarchy) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val title = itemView.findViewById<TextView>(R.id.title)
    private val flowLayout = itemView.findViewById<FlowLayout>(R.id.flow_layout)
    private lateinit var hierarchy: Hierarchy

    init {
        itemView.setOnClickListener {
            itemClick(hierarchy)
        }
    }


    fun bind(data: Hierarchy) {
        hierarchy = data
        title.text = data.name
        flowLayout.removeAllViews()
        data.children.forEach {
            val textView = TextView(itemView.context).apply {
                setTextAppearance(R.style.TextAppearance_MaterialComponents_Caption)
                textSize = 12f
                text = HtmlCompat.fromHtml(it.name, FROM_HTML_MODE_COMPACT)
                setPadding(12.dp, 4.dp, 12.dp, 4.dp)
                val colorStateList = ContextCompat.getColorStateList(context, R.color.ripple_color)
                val drawable = ContextCompat.getDrawable(context, R.drawable.bg_rect_round_12_solid)
                val rippleDrawable = RippleDrawable(colorStateList!!, drawable, null)
                background = rippleDrawable
                setOnClickListener { _ ->
                    contentClick(it)
                }
            }
            flowLayout.addView(textView)
        }
    }

    companion object {
        fun create(parent: ViewGroup, itemClick: (Hierarchy) -> Unit, contentClick: (SubHierarchy) -> Unit) =
            HierarchyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_item_hierarchy, parent, false
                ), itemClick, contentClick
            )
    }

}