package link.couple.jin.couplelink.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import link.couple.jin.couplelink.R;
import link.couple.jin.couplelink.data.CoupleClass;
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
        HomeImgAdapter homeImgAdapter = new HomeImgAdapter(context, coupleClass.imageList,holder.homeItemBinding.linkImage);
        holder.homeItemBinding.linkImage.setAdapter(homeImgAdapter);
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
