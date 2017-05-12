package link.couple.jin.couplelink.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.LinearLayout;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.kakaolink.v2.model.ButtonObject;
import com.kakao.kakaolink.v2.model.ContentObject;
import com.kakao.kakaolink.v2.model.FeedTemplate;
import com.kakao.kakaolink.v2.model.LinkObject;
import com.kakao.kakaolink.v2.model.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import link.couple.jin.couplelink.R;


/**
 * 커플초대 다이얼로그(카카오톡, 문자) [ 추가 공유는 후 업데이트, 페이스북이나 비트윈쪽을 생각중 ]
 * TODO 커플신청 및 커플등록 만든 후 다시코딩
 */

public class CoupleInvite extends Dialog implements View.OnClickListener {

    Context mContext;
    @BindView(R.id.sms_btn) LinearLayout smsBtn;

    public CoupleInvite(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_dialog);
        ButterKnife.bind(this);
        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS);
        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            smsBtn.setVisibility(View.GONE);
        else smsBtn.setVisibility(View.VISIBLE);
    }

    @Override
    @OnClick({R.id.sms_btn, R.id.kakao_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sms_btn:
                smsInvitation();
                break;
            case R.id.kakao_btn:
                kakaoInvitation();
                break;
        }
    }

    private void smsInvitation() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("01044258447", null, "커플링크 초대문자", null, null);
    }

    /**
     * 카카오톡 초대장 보내기
     */
    private void kakaoInvitation() {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("제목",
                        "사진url",
                        LinkObject.newBuilder().setWebUrl("웹url")
                                .setMobileWebUrl("모바일url").build())
                        .setDescrption("내용")
                        .build())
                .setSocial(SocialObject.newBuilder().setLikeCount(10).setCommentCount(20)
                        .setSharedCount(30).setViewCount(40).build())
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://dev.kakao.com")
                        .setMobileWebUrl("'https://dev.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();

        KakaoLinkService.getInstance().sendDefault(mContext, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {

            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {

            }
        });
    }
}
