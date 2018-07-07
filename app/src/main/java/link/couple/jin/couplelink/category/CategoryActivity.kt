package link.couple.jin.couplelink.category

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import link.couple.jin.couplelink.BaseActivity
import link.couple.jin.couplelink.R

/**
 * Created by image on 2017-12-16.
 * TODO NoSQL로는 카테고리 구현의 어려움이 있어 카테고리는 우선 나중으로 미룸
 */

class CategoryActivity : BaseActivity() {

    @BindView(R.id.category_depth_list)
    internal var categoryDepthList: RecyclerView? = null
    @BindView(R.id.category_list)
    internal var categoryList: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_activity)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_back, R.id.btn_menu, R.id.btn_search, R.id.btn_write)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_back -> finish()
            R.id.btn_menu -> {
            }
            R.id.btn_search -> {
            }
            R.id.btn_write -> {
            }
        }
    }
}
