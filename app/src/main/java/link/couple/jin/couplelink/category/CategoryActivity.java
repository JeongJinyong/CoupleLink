package link.couple.jin.couplelink.category;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import link.couple.jin.couplelink.BaseActivity;
import link.couple.jin.couplelink.R;

/**
 * Created by image on 2017-12-16.
 * TODO NoSQL로는 카테고리 구현의 어려움이 있어 카테고리는 우선 나중으로 미룸
 */

public class CategoryActivity extends BaseActivity {

    @BindView(R.id.category_depth_list)
    RecyclerView categoryDepthList;
    @BindView(R.id.category_list)
    RecyclerView categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_back, R.id.btn_menu, R.id.btn_search, R.id.btn_write})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_menu:
                break;
            case R.id.btn_search:
                break;
            case R.id.btn_write:
                break;
        }
    }
}
