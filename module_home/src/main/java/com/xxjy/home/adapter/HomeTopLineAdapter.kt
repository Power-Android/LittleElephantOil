package com.xxjy.home.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SpanUtils
import com.xxjy.common.entity.OrderNewsEntity
import com.xxjy.common.provide.MContext
import com.xxjy.home.R
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.util.BannerUtils

class HomeTopLineAdapter(datas: List<OrderNewsEntity>, isWhite: Boolean) :
    BannerAdapter<OrderNewsEntity, HomeTopLineAdapter.TopLineHolder>(datas) {
    private var mIsWhite = false
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): TopLineHolder {
        return TopLineHolder(BannerUtils.getView(parent, R.layout.item_home_message_2))
    }

    override fun onBindView(
        holder: TopLineHolder,
        data: OrderNewsEntity,
        position: Int,
        size: Int
    ) {
        holder.message.setTextColor(
            MContext.context().resources.getColor(
                if (mIsWhite) R.color.white else R.color.colorAccent
            )
        )
        SpanUtils.with(holder.message)
            .append("车主")
            .append(data.account.toString() + "")
            .append("加油")
            .append(data.amount.toString() + "")
            .append("元，节省")
            .append(data.discount)
            .setForegroundColor(
                MContext.context().resources.getColor(
                    if (mIsWhite) R.color.white else R.color.colorAccent
                )
            )
            .append("元")
            .create()
    }

    inner class TopLineHolder(view: View) : RecyclerView.ViewHolder(view) {
        var message: TextView = view.findViewById<TextView>(R.id.message)
    }

    init {
        mIsWhite = isWhite
    }
}