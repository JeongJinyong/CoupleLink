package link.couple.jin.couplelink.utile;

/**
 * 로그를 뿌린다.
 */

public class Log {

    static String log = "COUPLE";

    public static void e(String message){
        String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        android.util.Log.e(log,"[" + className + "." + methodName + "():" + lineNumber + "] : "+message);
    }

    public static void w(String message){
        String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        android.util.Log.w(log,"[" + className + "." + methodName + "():" + lineNumber + "] : "+message);
    }

    public static void d(String message){
        String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        android.util.Log.d(log,"[" + className + "." + methodName + "():" + lineNumber + "] : "+message);
    }

    public static void i(String message){
        String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        android.util.Log.i(log,"[" + className + "." + methodName + "():" + lineNumber + "] : "+message);
    }

    public static void v(String message){
        String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        android.util.Log.v(log,"[" + className + "." + methodName + "():" + lineNumber + "] : "+message);
    }

}
