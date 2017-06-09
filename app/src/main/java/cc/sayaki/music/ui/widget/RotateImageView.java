package cc.sayaki.music.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Author: sayaki
 * Date: 2016/11/15
 */
public class RotateImageView extends AppCompatImageView {

    private static final float ROTATE_DEGREE = 15.0f; // deg/sec

    private float currentDegree = 0.0f; // [0, 359]
    private float startDegree = 0.0f;
    private float stopDegree = 0.0f;

    private boolean enableAnimation = false;

    private long startTime = 0;

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateImageView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        Rect bounds = drawable.getBounds();
        int w = bounds.right - bounds.left;
        int h = bounds.bottom - bounds.top;

        if (w == 0 || h == 0) {
            return;
        }
        if (enableAnimation) {
            long time = System.currentTimeMillis();
            int deltaTime = (int) (time - startTime);
            float degree = startDegree + ROTATE_DEGREE * (deltaTime > 0 ? deltaTime : -deltaTime) / 1000;
            currentDegree = degree >= 0 ? degree % 360 : degree % 360 + 360;
            invalidate();
        }

        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();
        int width = getWidth() - left - right;
        int height = getHeight() - top - bottom;

        int saveCount = canvas.getSaveCount();

        // Scale down the image first if required.
        if ((getScaleType() == ScaleType.FIT_CENTER) &&
                ((width < w) || (height < h))) {
            float ratio = Math.min((float) width / w, (float) height / h);
            canvas.scale(ratio, ratio, width / 2.0f, height / 2.0f);
        }
        canvas.translate(left + width / 2, top + height / 2);
        canvas.rotate(currentDegree);
        canvas.translate(-w / 2, -h / 2);
        drawable.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    public void setStartDegree(int degree) {
        this.startDegree = degree >= 0 ? degree % 360 : degree % 360 + 360;
    }

    public void setStopDegree(int degree) {
        this.stopDegree = degree >= 0 ? degree % 360 : degree % 360 + 360;
    }

    public void initDegree() {
        this.enableAnimation = false;
        this.startDegree = 0.0f;
        this.currentDegree = 0.0f;
        postInvalidate();
    }

    public void start() {
        enableAnimation = true;
        startTime = System.currentTimeMillis();
        invalidate();
    }

    public void stop() {
        enableAnimation = false;
        this.startDegree = this.currentDegree;
    }
}
