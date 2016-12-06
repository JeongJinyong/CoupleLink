package link.couple.jin.couplelink;

import android.content.Context;
import android.util.Log;



/**
 * Created by jin on 2016-12-06.
 */

public class Util {

    static String log = "COUPLE";
    Context context;

    Util(Context context){
        this.context = context;
    }

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

    public String getStringResources(int resId){
        return context.getResources().getString(resId);
    }

}
