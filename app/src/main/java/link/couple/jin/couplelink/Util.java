package link.couple.jin.couplelink;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

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
import java.util.UUID;


/**
 * 이곳저곳 쓰게될 메서드들은 모아두자
 */

public class Util {

    static String log = "COUPLE";
    Context context;

    Util(Context context){
        this.context = context;
    }

    /**
     * 로그를 뿌린다.
     * @param logKinds 로그종류(e,w,i,d,v)
     * @param message 로그메세지
     */
    public void log(String logKinds ,String message){
        String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        switch (logKinds){
            case "e":
                Log.e(log,"[" + className + "." + methodName + "():" + lineNumber + "] : "+message);
                break;
            case "w":
                Log.w(log,"[" + className + "." + methodName + "():" + lineNumber + "] : "+message);
                break;
            case "i":
                Log.i(log,"[" + className + "." + methodName + "():" + lineNumber + "] : "+message);
                break;
            case "d":
                Log.d(log,"[" + className + "." + methodName + "():" + lineNumber + "] : "+message);
                break;
            case "v":
                Log.w(log,"[" + className + "." + methodName + "():" + lineNumber + "] : "+message);
                break;
        }

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
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
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
     * Base64 인코딩
     */
    public static String getBase64encode(String content){
        return Base64.encodeToString(content.getBytes(), 0);
    }

    /**
     * Base64 디코딩
     */
    public static String getBase64decode(String content){
        return new String(Base64.decode(content, 0));
    }
}
