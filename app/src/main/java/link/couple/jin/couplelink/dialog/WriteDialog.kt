package link.couple.jin.couplelink.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.TextCrawler

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import link.couple.jin.couplelink.home.HomeActivity
import link.couple.jin.couplelink.R
import link.couple.jin.couplelink.data.CoupleClass
import link.couple.jin.couplelink.utile.Log
import link.couple.jin.couplelink.utile.Util

/**
 * Created by jin on 2017-10-26.
 */

class WriteDialog(internal var mContext: Context) : Dialog(mContext) {
    internal var homeActivity: HomeActivity
    internal var util: Util
    @BindView(R.id.edit_address)
    internal var editAddress: EditText? = null
    @BindView(R.id.link_image)
    internal var linkImage: ImageView? = null
    @BindView(R.id.edit_title)
    internal var editTitle: EditText? = null
    internal var url = ""

    init {
        homeActivity = mContext as HomeActivity
        util = Util(mContext)
    }

    constructor(context: Context, url: String) : this(context) {
        this.url = url
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_write)
        ButterKnife.bind(this)
        if (url != "") {
            editAddress!!.setText(url)
        }
    }

    @OnClick(R.id.btn_preview, R.id.btn_cancel, R.id.btn_confirm)
    fun onViewClicked(view: View) {
        val title = editTitle!!.text.toString()
        val link = editAddress!!.text.toString()
        when (view.id) {
            R.id.btn_preview -> {
                if (link == "") {
                    Toast.makeText(mContext, "링크를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }
                val textCrawler = TextCrawler()
                textCrawler.makePreview(object : LinkPreviewCallback {
                    override fun onPre() {}

                    override fun onPos(sourceContent: SourceContent, isNull: Boolean) {
                        Log.e(isNull.toString() + "")
                        Log.e(sourceContent.url + "")
                        if (isNull || sourceContent.finalUrl == "") {

                        } else {
                            if (!sourceContent.images.isEmpty())
                                util.loadImage(linkImage, sourceContent.images[0])
                        }
                    }
                }, link)
            }
            R.id.btn_cancel -> dismiss()
            R.id.btn_confirm -> {
                if (title == "") {
                    Toast.makeText(mContext, "타이틀을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }
                if (link == "") {
                    Toast.makeText(mContext, "링크를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }
                val coupleClass = CoupleClass(editAddress!!.text.toString(), util.ymdTime, "qqq/qqq/qqq", title)
                if (homeActivity.setCoupleLink(coupleClass)) {
                    dismiss()
                }
            }
        }
    }
}
