package link.couple.jin.couplelink.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;

import java.util.ArrayList;

import link.couple.jin.couplelink.R;
import link.couple.jin.couplelink.data.CoupleClass;
import link.couple.jin.couplelink.databinding.HomeItemBinding;
import link.couple.jin.couplelink.utile.Log;
import link.couple.jin.couplelink.utile.Util;

/**
 * Created by jeongjin-yong on 2017. 10. 13..
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

    Context context;
    ArrayList<CoupleClass> coupleClassArrayList;
    Util util;

    public HomeAdapter(){}

    public HomeAdapter(Context context,ArrayList<CoupleClass> coupleClassArrayList){
        this.context = context;
        this.coupleClassArrayList = coupleClassArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        util = new Util(context);
        View view = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.home_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CoupleClass coupleClass = coupleClassArrayList.get(position);
        holder.homeItemBinding.setCouple(coupleClass);
        Log.e(coupleClass.imageList.size() + "");
        TextCrawler textCrawler = new TextCrawler();
        textCrawler.makePreview(new LinkPreviewCallback() {
            @Override
            public void onPre() {
            }

            @Override
            public void onPos(SourceContent sourceContent, boolean isNull) {
                if(isNull || sourceContent.getFinalUrl().equals("")){
                    util.loadImage(holder.homeItemBinding.linkImage, coupleClass.imageList.get(0));
                }else{
                    if(!sourceContent.getImages().isEmpty()) {
                        coupleClass.imageList.add(0,sourceContent.getImages().get(0));
                        util.loadImage(holder.homeItemBinding.linkImage, sourceContent.getImages().get(0));
                    }else{
                        util.loadImage(holder.homeItemBinding.linkImage, coupleClass.imageList.get(0));
                    }
                }
            }
        }, coupleClass.link);
    }

    @Override
    public int getItemCount() {
        return coupleClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HomeItemBinding homeItemBinding;
        public ViewHolder(View view) {
            super(view);
            homeItemBinding = HomeItemBinding.bind(view);
        }
    }
}
