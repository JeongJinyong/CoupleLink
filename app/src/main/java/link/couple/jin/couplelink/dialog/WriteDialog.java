package link.couple.jin.couplelink.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import link.couple.jin.couplelink.HomeActivity;
import link.couple.jin.couplelink.R;
import link.couple.jin.couplelink.data.CoupleClass;
import link.couple.jin.couplelink.utile.Log;
import link.couple.jin.couplelink.utile.Util;

/**
 * Created by jin on 2017-10-26.
 */

public class WriteDialog extends Dialog {

    Context mContext;
    HomeActivity homeActivity;
    Util util;
    @BindView(R.id.edit_address)
    EditText editAddress;
    @BindView(R.id.link_image)
    ImageView linkImage;
    @BindView(R.id.edit_title)
    EditText editTitle;

    public WriteDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        homeActivity = (HomeActivity)context;
        util = new Util(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_write);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_preview, R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        String title = editTitle.getText().toString();
        String link = editAddress.getText().toString();
        switch (view.getId()) {
            case R.id.btn_preview:
                if(link.equals("")){
                    Toast.makeText(mContext, "링크를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                TextCrawler textCrawler = new TextCrawler();
                textCrawler.makePreview(new LinkPreviewCallback() {
                    @Override
                    public void onPre() {
                    }

                    @Override
                    public void onPos(final SourceContent sourceContent, boolean isNull) {
                        Log.e(isNull+"");
                        Log.e(sourceContent.getUrl()+"");
                        if (isNull || sourceContent.getFinalUrl().equals("")) {

                        } else {
                            if(!sourceContent.getImages().isEmpty())
                                util.loadImage(linkImage, sourceContent.getImages().get(0));
                        }
                    }
                },link);
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                if(title.equals("")){
                    Toast.makeText(mContext, "타이틀을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(link.equals("")){
                    Toast.makeText(mContext, "링크를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                CoupleClass coupleClass = new CoupleClass(editAddress.getText().toString(), util.getYMDTime(), "qqq/qqq/qqq", title);
                if(homeActivity.setCoupleLink(coupleClass)){
                    dismiss();
                }
                break;
        }
    }
}
