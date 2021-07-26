package com.xxjy.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * @author power
 * @date 2019/5/14 2:19 PM
 * @project BuildTalk
 * @description:
 */
class MyViewPagerAdapter(var mTitles: Array<String>, var mViews: List<View>) : PagerAdapter() {
    override fun getCount(): Int {
        return mViews.size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view === any
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitles[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val parent = mViews[position].parent as ViewGroup
        parent.removeView(mViews[position])
        val v = mViews[position]
        container.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }

    fun refreshData(mTitles: Array<String>, mViews: List<View>) {
        this.mTitles = mTitles
        this.mViews = mViews
        notifyDataSetChanged()
    }
}