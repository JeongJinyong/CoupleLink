package link.couple.jin.couplelink.utile;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BindingAdapter;
import android.telephony.TelephonyManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.Glide.with;


/**
 * 이곳저곳 쓰게될 메서드들은 모아두자
 */

public class Util {

    Context context;
    SharedPreferences sharedPreferences;

    public Util(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("link",Context.MODE_PRIVATE);
    }

    /**
     * 자동로그인 설정 저장
     * @param isAuto
     */
    public void setAutoLogin(boolean isAuto){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("auto_login",isAuto);
        editor.commit();
    }

    /**
     * 로그인정보 저장 자동로그인이 아닐경우엔 비밀번호는 공백으로 처리 = ""
     * @param login_id
     * @param login_pw
     */
    public void setLoginInfo(String login_id,String login_pw){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("login_id",login_id);
        editor.putString("login_pw",login_pw);
        editor.commit();
    }

    /**
     * 자동로그인 설정 가져옴
     * @return
     */
    public boolean isAutoLogin(){
        return sharedPreferences.getBoolean("auto_login",false);
    }

    /**
     * 로그인 정보를 가져옴
     * @return
     */
    public String[] getLoginInfo(){
        String[] login_info = new String[1];
        login_info[0] = sharedPreferences.getString("login_id","");
        login_info[1] = sharedPreferences.getString("login_pw","");
        return login_info;
    }

    /**
     * string.xml에 있는 리소스를 가져옴
     * @param resId
     * @return
     */
    public String getStringResources(int resId){
        return context.getResources().getString(resId);
    }

    /**
     * yyyyMMdd 형식
     * @return time
     */
    public String getNowTime(){
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddhhmmss");
        return sdfNow.format(new Date(System.currentTimeMillis()));
    }

    /**
     * yyyy-MM-dd 형식
     * @return time
     */
    public String getYMDTime(){
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
        return sdfNow.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 디바이스 유니크 아이디 가져오기
     * @return deviceId
     */
    public String getDevicesUUID(){
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }

    /**
     * 랜덤 알파벳 3자리를 만듬 대소문자 구분없이
     * @return temp
     */
    public String getRandomAlphabet(){
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 3; i++) {
            int rIndex = rnd.nextInt(2);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
            }
        }
        return temp.toString();
    }

    /**
     * Json을 Map형태로 변경
     * @param object
     * @return
     * @throws JSONException
     */
    public Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * JsonArray를 MapList형태로 변경
     * @param array
     * @return
     * @throws JSONException
     */
    public List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    /**
     * 글라이드 사용 간단하게 이미지만 불러옴
     * @param imageView
     * @param imageUrl
     */
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(final ImageView imageView, String imageUrl) {
        if(!imageUrl.contains("https"))
            imageUrl = "http://" + imageUrl.substring(imageUrl.indexOf("//")+2,imageUrl.length());
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(RequestOptions.skipMemoryCacheOf(true).override(imageView.getMaxWidth(),imageView.getMaxHeight()))
                .into(imageView);
    }

    /**
     * 글라이드 사용 Circle Crop
     * @param imageView
     * @param imageUrl
     */
    @BindingAdapter({"bind:loadImageCircle"})
    public static void loadImageCircle(final ImageView imageView, String imageUrl) {
        with(imageView.getContext())
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }
}
