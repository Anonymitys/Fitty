package com.ekkoe.fitty.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ekkoe.fitty.R
import com.ekkoe.fitty.data.HomeBanner
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.CircleIndicator

class HomeBannerAdapter(private var data: List<HomeBanner> = mutableListOf()) :
    RecyclerView.Adapter<HomeBannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBannerViewHolder =
        HomeBannerViewHolder.create(parent)

    override fun onBindViewHolder(holder: HomeBannerViewHolder, position: Int) {
        holder.bind(data)
    }

    override fun getItemCount(): Int = 1

    fun submitData(banner: List<HomeBanner>) {
        data = banner
        notifyItemChanged(0)
    }
}

class HomeBannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val bannerView =
        itemView.findViewById<Banner<HomeBanner, ItemBannerAdapter>>(R.id.banner)

    fun bind(banner: List<HomeBanner>) {
        bannerView.setAdapter(ItemBannerAdapter(banner))
        bannerView.viewPager2.offscreenPageLimit = 4
        bannerView.indicator = CircleIndicator(itemView.context)
    }


    companion object {
        fun create(parent: ViewGroup): HomeBannerViewHolder = HomeBannerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_home_banner, parent, false
            )
        )
    }
}


class ItemBannerAdapter(data: List<HomeBanner>) :
    BannerAdapter<HomeBanner, ItemBannerViewHolder>(data) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): ItemBannerViewHolder =
        ItemBannerViewHolder.create(parent)

    override fun onBindView(
        holder: ItemBannerViewHolder,
        data: HomeBanner,
        position: Int,
        size: Int
    ) {
        holder.bind(data)
    }

}


class ItemBannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val image = itemView.findViewById<ImageView>(R.id.image)

    fun bind(banner: HomeBanner) {
        title.text = banner.title
        image.load(banner.imagePath) {
            crossfade(300)
        }
    }

    companion object {
        fun create(parent: ViewGroup) = ItemBannerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_item_banner_title, parent, false)
        )
    }

}