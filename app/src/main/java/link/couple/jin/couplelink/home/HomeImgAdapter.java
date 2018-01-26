package link.couple.jin.couplelink.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    public HomeImgAdapter(Context context, ArrayList<String> imageList) {
        this.context = context;
        this.imageList = imageList;
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

    @OnClick({R.id.left_btn, R.id.right_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                break;
            case R.id.right_btn:
                break;
        }
    }

    class ViewHolder {
        @BindView(R.id.link_image)
        ImageView linkImage;
        @BindView(R.id.left_btn)
        FrameLayout leftBtn;
        @BindView(R.id.right_btn)
        FrameLayout rightBtn;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
