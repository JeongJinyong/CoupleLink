package link.couple.jin.couplelink.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import link.couple.jin.couplelink.R;
import link.couple.jin.couplelink.utile.Log;
import link.couple.jin.couplelink.utile.Util;

/**
 * Created by jin on 2018-01-23.
 */

public class HomeImgAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> imageList;
    LayoutInflater mInflater;
    ViewPager viewPager;

    public HomeImgAdapter(Context context, ArrayList<String> imageList, ViewPager viewPager) {
        this.context = context;
        this.imageList = imageList;
        this.viewPager = viewPager;
        mInflater = ((HomeActivity)context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup layout = (ViewGroup) mInflater.inflate(R.layout.link_img_item, container, false);
        ViewHolder holder = new ViewHolder(layout);
        Util.loadImage(holder.linkImage,imageList.get(position));
        onClicked(holder.leftBtn,position);
        onClicked(holder.rightBtn,position);
        container.addView(layout);
        return layout;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void onClicked(final View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.left_btn:
                        if(position > 0)
                            viewPager.setCurrentItem(position-1);
                        break;
                    case R.id.right_btn:
                        if(position < getCount()+1)
                            viewPager.setCurrentItem(position+1);
                        break;
                }
            }
        });
    }

    class ViewHolder {
        @BindView(R.id.link_image)
        ImageView linkImage;
        @BindView(R.id.left_btn)
        ImageView leftBtn;
        @BindView(R.id.right_btn)
        ImageView rightBtn;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
