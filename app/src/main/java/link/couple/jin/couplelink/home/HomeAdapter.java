package link.couple.jin.couplelink.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

import link.couple.jin.couplelink.R;
import link.couple.jin.couplelink.data.CoupleClass;
import link.couple.jin.couplelink.databinding.AdsItemBinding;
import link.couple.jin.couplelink.databinding.HomeItemBinding;
import link.couple.jin.couplelink.utile.Log;

/**
 * Created by jeongjin-yong on 2017. 10. 13..
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

    Context context;
    ArrayList<CoupleClass> coupleClassArrayList;

    public HomeAdapter(){}

    public HomeAdapter(Context context,ArrayList<CoupleClass> coupleClassArrayList){
        this.context = context;
        this.coupleClassArrayList = coupleClassArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view;
        if(viewType != 0) {
            view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.home_item, parent, false);
            viewHolder = new ViewHolder(view,false);
        }else{
            view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.ads_item, parent, false);
            viewHolder = new ViewHolder(view,true);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CoupleClass coupleClass = coupleClassArrayList.get(position);
        if(position%10 == 0 && position != 0) {
            showNativeAd(holder.adsItemBinding);
        }else{
            holder.homeItemBinding.setCouple(coupleClass);
            HomeImgAdapter homeImgAdapter = new HomeImgAdapter(context, coupleClass.imageList, holder.homeItemBinding.linkImage);
            holder.homeItemBinding.linkImage.setAdapter(homeImgAdapter);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position%10 == 0 && position != 0) return 0;
        return 1;
    }

    @Override
    public int getItemCount() {
        return coupleClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HomeItemBinding homeItemBinding;
        AdsItemBinding adsItemBinding;
        public ViewHolder(View view,boolean isAds) {
            super(view);
            if(isAds)
            adsItemBinding = AdsItemBinding.bind(view);
            else homeItemBinding = HomeItemBinding.bind(view);
        }
    }

    private void showNativeAd(final AdsItemBinding adsItemBinding) {
        final NativeAd nativeAd = new NativeAd(context, "1982649925309658_1987240794850571");
        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                // Ad error callback
                Log.e(error.getErrorMessage() + "///" + error.getErrorCode());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
                if (nativeAd != null) {
                    nativeAd.unregisterView();
                }

                // Set the Text.
                adsItemBinding.nativeAdTitle.setText(nativeAd.getAdTitle());
                adsItemBinding.nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                adsItemBinding.nativeAdBody.setText(nativeAd.getAdBody());
                adsItemBinding.nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

                // Download and display the ad icon.
                NativeAd.Image adIcon = nativeAd.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, adsItemBinding.nativeAdIcon);

                // Download and display the cover image.
                adsItemBinding.nativeAdMedia.setNativeAd(nativeAd);

                // Add the AdChoices icon
                AdChoicesView adChoicesView = new AdChoicesView(context, nativeAd, true);
                adsItemBinding.adChoicesContainer.addView(adChoicesView);

                // Register the Title and CTA button to listen for clicks.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(adsItemBinding.nativeAdTitle);
                clickableViews.add(adsItemBinding.nativeAdCallToAction);
                nativeAd.registerViewForInteraction(adsItemBinding.getRoot(),clickableViews);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });

        // Request an ad
        nativeAd.loadAd();
    }

}
