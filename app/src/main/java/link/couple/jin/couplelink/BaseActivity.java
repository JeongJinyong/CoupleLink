package link.couple.jin.couplelink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import link.couple.jin.couplelink.data.UserClass;
import link.couple.jin.couplelink.utile.Log;
import link.couple.jin.couplelink.utile.Util;

import static link.couple.jin.couplelink.utile.Constant.COUPLE_UID;
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

    public BaseActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        util = new Util(this);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        try {
            PackageInfo info = getPackageManager().getPackageInfo("link.couple.jin.couplelink", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        ClipboardManager clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        clipBoard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                Log.e("asdasd");
            }
        });

    }

    public Query getUserQuery(String str, int type){
        switch (type){
            case USER_UID:
                return databaseReference.child("user").child(str);
            case USER_EMAIL_ALL:
                return databaseReference.child("user").orderByChild("/email").equalTo(str);
            case USER_COUPLE:
                return databaseReference.child("user").orderByChild("/couple").equalTo(str);
            case COUPLE_UID:
                return databaseReference.child("couple").child(str);
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

    @Override
    public void test() {

    }
}
