package cc.sayaki.music.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Author: sayaki
 * Date: 2017/6/8
 */
public class ShadowImageView extends ImageView {

    private static final int KEY_SHADOW_COLOR = 0x1E000000;
    private static final int FILL_SHADOW_COLOR = 0x3D000000;

    private static final float X_OFFSET = 0f;
    private static final float Y_OFFSET = 1.75f;

    private static final float SHADOW_RADIUS = 24f;
    private static final int SHADOW_ELEVATION = 16;

    private static final int DEFAULT_BACKGROUND_COLOR = 0xFF3C5F78;

    private int shadowRadius;

    // Animation
    private ObjectAnimator rotateAnimator;
    private long lastAnimationValue;

    public ShadowImageView(Context context) {
        this(context, null);
    }

    public ShadowImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        final float density = getContext().getResources().getDisplayMetrics().density;
        final int shadowXOffset = (int) (density * X_OFFSET);
        final int shadowYOffset = (int) (density * Y_OFFSET);

        shadowRadius = (int) (density * SHADOW_RADIUS);

        ShapeDrawable circle;
        if (elevationSupported()) {
            circle = new ShapeDrawable(new OvalShape());
            ViewCompat.setElevation(this, SHADOW_ELEVATION * density);
        } else {
            OvalShape oval = new OvalShadow(shadowRadius);
            circle = new ShapeDrawable(oval);
            ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, circle.getPaint());
            circle.getPaint().setShadowLayer(shadowRadius, shadowXOffset, shadowYOffset, KEY_SHADOW_COLOR);
            final int padding = shadowRadius;
            // set padding so the inner image sits correctly within the shadow.
            setPadding(padding, padding, padding, padding);
        }
        circle.getPaint().setAntiAlias(true);
        circle.getPaint().setColor(DEFAULT_BACKGROUND_COLOR);
        setBackground(circle);

        rotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
        rotateAnimator.setDuration(10800);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    private boolean elevationSupported() {
        return android.os.Build.VERSION.SDK_INT >= 21;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!elevationSupported()) {
            setMeasuredDimension(getMeasuredWidth() + shadowRadius * 2, getMeasuredHeight() + shadowRadius * 2);
        }
    }

    // Animation
    public void startRotateAnimation() {
        if (rotateAnimator != null) {
            rotateAnimator.cancel();
            rotateAnimator.start();
        }
    }

    public void cancelRotateAnimation() {
        lastAnimationValue = 0;
        if (rotateAnimator != null) {
            rotateAnimator.cancel();
        }
    }

    public void pauseRotateAnimation() {
        if (rotateAnimator != null) {
            lastAnimationValue = rotateAnimator.getCurrentPlayTime();
            rotateAnimator.cancel();
        }
    }

    public void resumeRotateAnimation() {
        if (rotateAnimator != null) {
            rotateAnimator.start();
            rotateAnimator.setCurrentPlayTime(lastAnimationValue);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (rotateAnimator != null) {
            rotateAnimator.cancel();
            rotateAnimator = null;
        }
    }

    /**
     * Draw oval shadow below ImageView under lollipop.
     */
    private class OvalShadow extends OvalShape {
        private RadialGradient radialGradient;
        private Paint shadowPaint;

        OvalShadow(int shadowRadius) {
            super();
            shadowPaint = new Paint();
            ShadowImageView.this.shadowRadius = shadowRadius;
            updateRadialGradient((int) rect().width());
        }

        @Override
        protected void onResize(float width, float height) {
            super.onResize(width, height);
            updateRadialGradient((int) width);
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            final int viewWidth = ShadowImageView.this.getWidth();
            final int viewHeight = ShadowImageView.this.getHeight();
            canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2, shadowPaint);
            canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2 - shadowRadius, paint);
        }

        private void updateRadialGradient(int diameter) {
            radialGradient = new RadialGradient(diameter / 2, diameter / 2,
                    shadowRadius, new int[]{FILL_SHADOW_COLOR, Color.TRANSPARENT},
                    null, Shader.TileMode.CLAMP);
            shadowPaint.setShader(radialGradient);
        }
    }
}
