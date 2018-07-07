package link.couple.jin.couplelink.home

import android.app.Activity
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import link.couple.jin.couplelink.R
import link.couple.jin.couplelink.utile.Log
import link.couple.jin.couplelink.utile.Util

/**
 * Created by jin on 2018-01-23.
 */

class HomeImgAdapter(internal var context: Context, internal var imageList: ArrayList<String>, internal var viewPager: ViewPager) : PagerAdapter() {
    internal var mInflater: LayoutInflater

    init {
        mInflater = (context as Activity).layoutInflater
    }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = mInflater.inflate(R.layout.link_img_item, container, false) as ViewGroup
        val holder = ViewHolder(layout)
        Util.loadImage(holder.linkImage, imageList[position])
        onClicked(holder.leftBtn, position)
        onClicked(holder.rightBtn, position)
        container.addView(layout)
        return layout
    }

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun onClicked(view: View?, position: Int) {
        view!!.setOnClickListener { v ->
            when (v.id) {
                R.id.left_btn -> if (position > 0)
                    viewPager.currentItem = position - 1
                R.id.right_btn -> if (position < count + 1)
                    viewPager.currentItem = position + 1
            }
        }
    }

    internal inner class ViewHolder(view: View) {
        @BindView(R.id.link_image)
        var linkImage: ImageView? = null
        @BindView(R.id.left_btn)
        var leftBtn: ImageView? = null
        @BindView(R.id.right_btn)
        var rightBtn: ImageView? = null

        init {
            ButterKnife.bind(this, view)
        }
    }
}
