package link.couple.jin.couplelink;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import link.couple.jin.couplelink.data.CoupleClass;
import link.couple.jin.couplelink.databinding.HomeItemBinding;

/**
 * Created by jeongjin-yong on 2017. 10. 13..
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

    ArrayList<CoupleClass> coupleClassArrayList;

    public HomeAdapter(){}

    public HomeAdapter(ArrayList<CoupleClass> coupleClassArrayList){
        this.coupleClassArrayList = coupleClassArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CoupleClass coupleClass = coupleClassArrayList.get(position);
        holder.homeItemBinding.setCouple(coupleClass);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HomeItemBinding homeItemBinding;
        public ViewHolder(View view) {
            super(view);
            homeItemBinding = DataBindingUtil.bind(view);
        }
    }

}
