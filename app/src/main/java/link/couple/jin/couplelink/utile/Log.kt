package link.couple.jin.couplelink.utile

/**
 * 로그를 뿌린다.
 */

object Log {

    internal var log = "COUPLE"

    fun e(message: String) {
        val fullClassName = Thread.currentThread().stackTrace[3].className
        val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
        val methodName = Thread.currentThread().stackTrace[3].methodName
        val lineNumber = Thread.currentThread().stackTrace[3].lineNumber
        android.util.Log.e(log, "[$className.$methodName():$lineNumber] : $message")
    }

    fun w(message: String) {
        val fullClassName = Thread.currentThread().stackTrace[3].className
        val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
        val methodName = Thread.currentThread().stackTrace[3].methodName
        val lineNumber = Thread.currentThread().stackTrace[3].lineNumber
        android.util.Log.w(log, "[$className.$methodName():$lineNumber] : $message")
    }

    fun d(message: String) {
        val fullClassName = Thread.currentThread().stackTrace[3].className
        val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
        val methodName = Thread.currentThread().stackTrace[3].methodName
        val lineNumber = Thread.currentThread().stackTrace[3].lineNumber
        android.util.Log.d(log, "[$className.$methodName():$lineNumber] : $message")
    }

    fun i(message: String) {
        val fullClassName = Thread.currentThread().stackTrace[3].className
        val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
        val methodName = Thread.currentThread().stackTrace[3].methodName
        val lineNumber = Thread.currentThread().stackTrace[3].lineNumber
        android.util.Log.i(log, "[$className.$methodName():$lineNumber] : $message")
    }

    fun v(message: String) {
        val fullClassName = Thread.currentThread().stackTrace[3].className
        val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
        val methodName = Thread.currentThread().stackTrace[3].methodName
        val lineNumber = Thread.currentThread().stackTrace[3].lineNumber
        android.util.Log.v(log, "[$className.$methodName():$lineNumber] : $message")
    }

}
