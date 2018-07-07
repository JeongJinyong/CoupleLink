package link.couple.jin.couplelink.utile

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * 메인 리스트 동영상 썸네일 크기때문에 커스텀뷰 만듦
 * Created by Jaeyun on 2016-10-17.
 */
class CustomImageView : AppCompatImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    protected fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d = getDrawable()

        if (d != null) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = Math.ceil((width.toFloat() * d!!.getIntrinsicHeight().toFloat() / d!!.getIntrinsicWidth().toFloat()).toDouble()).toInt()
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
