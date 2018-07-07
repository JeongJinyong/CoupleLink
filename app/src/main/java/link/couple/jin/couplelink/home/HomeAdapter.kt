package link.couple.jin.couplelink.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.facebook.ads.Ad
import com.facebook.ads.AdChoicesView
import com.facebook.ads.AdError
import com.facebook.ads.AdListener
import com.facebook.ads.NativeAd

import java.util.ArrayList

import link.couple.jin.couplelink.R
import link.couple.jin.couplelink.data.CoupleClass
import link.couple.jin.couplelink.databinding.AdsItemBinding
import link.couple.jin.couplelink.databinding.HomeItemBinding
import link.couple.jin.couplelink.utile.Log

/**
 * Created by jeongjin-yong on 2017. 10. 13..
 */

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    internal var context: Context
    internal var coupleClassArrayList: ArrayList<CoupleClass>

    constructor() {}

    constructor(context: Context, coupleClassArrayList: ArrayList<CoupleClass>) {
        this.context = context
        this.coupleClassArrayList = coupleClassArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder: ViewHolder
        val view: View
        if (viewType != 0) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
            viewHolder = ViewHolder(view, false)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.ads_item, parent, false)
            viewHolder = ViewHolder(view, true)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coupleClass = coupleClassArrayList[position]
        if (position % 10 == 0 && position != 0) {
            showNativeAd(holder.adsItemBinding)
        } else {
            holder.homeItemBinding.couple = coupleClass
            val homeImgAdapter = HomeImgAdapter(context, coupleClass.imageList, holder.homeItemBinding.linkImage)
            holder.homeItemBinding.linkImage.adapter = homeImgAdapter
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 10 == 0 && position != 0) 0 else 1
    }

    override fun getItemCount(): Int {
        return coupleClassArrayList.size
    }

    inner class ViewHolder(view: View, isAds: Boolean) : RecyclerView.ViewHolder(view) {
        internal var homeItemBinding: HomeItemBinding
        internal var adsItemBinding: AdsItemBinding

        init {
            if (isAds)
                adsItemBinding = AdsItemBinding.bind(view)
            else
                homeItemBinding = HomeItemBinding.bind(view)
        }
    }

    private fun showNativeAd(adsItemBinding: AdsItemBinding) {
        val nativeAd = NativeAd(context, "1982649925309658_1987240794850571")
        nativeAd.setAdListener(object : AdListener {

            override fun onError(ad: Ad, error: AdError) {
                // Ad error callback
                Log.e(error.errorMessage + "///" + error.errorCode)
            }

            override fun onAdLoaded(ad: Ad) {
                // Ad loaded callback
                nativeAd?.unregisterView()

                // Set the Text.
                adsItemBinding.nativeAdTitle.setText(nativeAd.getAdTitle())
                adsItemBinding.nativeAdSocialContext.text = nativeAd.adSocialContext
                adsItemBinding.nativeAdBody.setText(nativeAd.getAdBody())
                adsItemBinding.nativeAdCallToAction.text = nativeAd.adCallToAction

                // Download and display the ad icon.
                val adIcon = nativeAd.adIcon
                NativeAd.downloadAndDisplayImage(adIcon, adsItemBinding.nativeAdIcon)

                // Download and display the cover image.
                adsItemBinding.nativeAdMedia.setNativeAd(nativeAd)

                // Add the AdChoices icon
                val adChoicesView = AdChoicesView(context, nativeAd, true)
                adsItemBinding.adChoicesContainer.addView(adChoicesView)

                // Register the Title and CTA button to listen for clicks.
                val clickableViews = ArrayList<View>()
                clickableViews.add(adsItemBinding.nativeAdTitle)
                clickableViews.add(adsItemBinding.nativeAdCallToAction)
                nativeAd.registerViewForInteraction(adsItemBinding.root, clickableViews)
            }

            override fun onAdClicked(ad: Ad) {
                // Ad clicked callback
            }

            override fun onLoggingImpression(ad: Ad) {
                // Ad impression logged callback
            }
        })

        // Request an ad
        nativeAd.loadAd()
    }

}
