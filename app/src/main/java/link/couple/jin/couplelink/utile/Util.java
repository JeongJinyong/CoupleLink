package link.couple.jin.couplelink.utile;

import android.content.Context;
import android.telephony.TelephonyManager;

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


/**
 * 이곳저곳 쓰게될 메서드들은 모아두자
 */

public class Util {

    Context context;

    public Util(Context context){
        this.context = context;
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
}
