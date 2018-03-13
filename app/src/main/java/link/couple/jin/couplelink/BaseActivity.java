package link.couple.jin.couplelink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import link.couple.jin.couplelink.data.CoupleClass;
import link.couple.jin.couplelink.data.UserClass;
import link.couple.jin.couplelink.utile.Log;
import link.couple.jin.couplelink.utile.Util;

import static com.kakao.util.helper.Utility.getPackageInfo;
import static link.couple.jin.couplelink.utile.Constant.COUPLE_UID;
import static link.couple.jin.couplelink.utile.Constant.TITLE;
import static link.couple.jin.couplelink.utile.Constant.USER_COUPLE;
import static link.couple.jin.couplelink.utile.Constant.USER_EMAIL_ALL;
import static link.couple.jin.couplelink.utile.Constant.USER_UID;

/**
 * 엑티비티에서 공용으로 쓰는건 최대한 메인으로 빼도록 하자.
 */

public class BaseActivity extends Activity implements MainInterface {

    public ProgressDialog mProgressDialog;
    public Util util;
    public static UserClass userLogin;

    public FirebaseAuth firebaseAuth;
    public DatabaseReference databaseReference;

    public ArrayList<CoupleClass> classArrayList = new ArrayList<>();
    public RecyclerView.Adapter adapter;

    String refreshedToken;
    public BaseActivity(){

    }

    public void settingList(RecyclerView.Adapter adapter, ArrayList<CoupleClass> classArrayList){
        this.adapter = adapter;
        this.classArrayList = classArrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Util(this);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        PackageInfo packageInfo = getPackageInfo(this, PackageManager.GET_SIGNATURES);
        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e(Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            } catch (NoSuchAlgorithmException e) {
                Log.w("Unable to get MessageDigest. signature=" + signature);
            }
        }

        ClipboardManager clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        clipBoard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                Log.e("asdasd");
            }
        });

    }

    public Query getUserQuery(String str, int type) {
        switch (type) {
            case USER_UID:
                return databaseReference.child("user").child(str);
            case USER_EMAIL_ALL:
                return databaseReference.child("user").orderByChild("/email").equalTo(str);
            case USER_COUPLE:
                return databaseReference.child("user").orderByChild("/couple").equalTo(str);
            case COUPLE_UID:
                return databaseReference.child("couple").child(str);
            case TITLE:
                return databaseReference.child("couple").child(userLogin.couple).orderByChild("/title");
        }
        return null;
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("로딩중입니다.");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 데이터 수집 후 어댑터 체인
     * @param coupleClasses
     */
    public void notifyDataSetChanged(ArrayList<CoupleClass> coupleClasses){
        classArrayList = coupleClasses;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void test() {

    }
}
