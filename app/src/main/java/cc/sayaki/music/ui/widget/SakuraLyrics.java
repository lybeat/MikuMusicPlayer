package cc.sayaki.music.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cc.sayaki.music.R;
import cc.sayaki.music.data.model.Lyrics;
import cc.sayaki.music.utils.UnitUtil;

/**
 * Author: lybeat
 * Date: 2016/8/16
 */
public class SakuraLyrics extends View {

    private static final String TAG = "SakuraLyrics";

    private int width;
    private int height;
    private int lightSize;
    private int normalSize;
    private int lightColor;
    private int normalColor;
    private int lineHeight;
    private TextPaint lightPaint;
    private TextPaint normalPaint;

    private List<Lyrics> lrcs;
    private int index;
    private int currentIndex;
    private int translateY;
    private boolean isLoading = true;

    private MediaPlayer mediaPlayer;

    private Timer timer;

    public SakuraLyrics(Context context) {
        this(context, null);
    }

    public SakuraLyrics(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SakuraLyrics(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setFocusable(true);

        initAttrs(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SakuraLyrics, defStyleAttr, 0);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SakuraLyrics_lyricsLightColor) {
                lightColor = a.getColor(attr, lightColor);
            } else if (attr == R.styleable.SakuraLyrics_lyricsNormalColor) {
                normalColor = a.getColor(attr, normalColor);
            } else if (attr == R.styleable.SakuraLyrics_lyricsLightSize) {
                lightSize = a.getDimensionPixelSize(attr, lightSize);
            } else if (attr == R.styleable.SakuraLyrics_lyricsNormalSize) {
                normalSize = a.getDimensionPixelSize(attr, normalSize);
            } else if (attr == R.styleable.SakuraLyrics_lyricsLineHeight) {
                lineHeight = a.getDimensionPixelSize(attr, lineHeight);
            }
        }
        a.recycle();

        initPaint();
    }

    private void initAttrs(Context context) {
        lightColor = 0xff666666;
        normalColor = 0xff999999;
        lightSize = UnitUtil.sp2px(context, 16);
        normalSize = UnitUtil.sp2px(context, 14);
        lineHeight = UnitUtil.dp2px(context, 28);
    }

    private void initPaint() {
        lightPaint = new TextPaint();
        lightPaint.setAntiAlias(true);
        lightPaint.setTextAlign(Paint.Align.CENTER);
        lightPaint.setColor(lightColor);
        lightPaint.setTextSize(lightSize);

        normalPaint = new TextPaint();
        normalPaint.setAntiAlias(true);
        normalPaint.setTextAlign(Paint.Align.CENTER);
        normalPaint.setColor(normalColor);
        normalPaint.setTextSize(normalSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isLoading) {
            canvas.drawText("正在加载歌词…", width / 2, height / 2, lightPaint);
            return;
        }

        if (lrcs == null || lrcs.isEmpty()) {
            canvas.drawText("暂无歌词", width / 2, height / 2, lightPaint);
            return;
        }

        float centerY = height / 2 + translateY;
//        canvas.translate(0, centerY);
//        StaticLayout staticLayout = new StaticLayout(lrcs.get(index).getLrc(),
//                lightPaint, canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, false);
//        staticLayout.draw(canvas);

        canvas.drawText(lrcs.get(index).getLrc(), width / 2, centerY, lightPaint);

        for (int i = index - 1; i >= 0; i--) {
            centerY -= lineHeight;
            canvas.drawText(lrcs.get(i).getLrc(), width / 2, centerY, normalPaint);
        }
        centerY = height / 2 + translateY;
        for (int i = index + 1; i < lrcs.size(); i++) {
            centerY += lineHeight;
            canvas.drawText(lrcs.get(i).getLrc(), width / 2, centerY, normalPaint);
        }
    }

//    private float measureLrcHeight(String lrc, Paint paint) {
//        int lrcWidth = (int) paint.measureText(lrc);
//        float lh = lrcWidth > width ? lineHeight * (lrcWidth / width + 1) : lineHeight;
//        return lh;
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    public void setLrcs(List<Lyrics> lrcs) {
        this.lrcs = lrcs;

        initLyrics();
    }

    private void initLyrics() {
        index = 0;
        currentIndex = 0;
        translateY = 0;
        syncLyrics();
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    private void syncLyrics() {
        isLoading = false;
        if (lrcs == null || lrcs.isEmpty()) {
            postInvalidate();
            return;
        }
        if (mediaPlayer == null) {
            return;
        }
        if (timer != null) {
            timer.cancel();
        }

        long progress = mediaPlayer.getCurrentPosition();
        int size = lrcs.size();
        for (int i = 0; i < size; i++) {
            if (progress == lrcs.get(i).getTimestamp()) {
                index = i;
                break;
            }
            if (progress < lrcs.get(i).getTimestamp()) {
                index = i - 1;
                if (index < 0) {
                    index = 0;
                }
                break;
            }
        }

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                SakuraLyrics.this.post(new Runnable() {
                    @Override
                    public void run() {
                        if (index != currentIndex) {
                            currentIndex = index;
                            startAnimation();
                        }
                        syncLyrics();
                    }
                });
            }
        };
        timer.schedule(timerTask, 200);
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void update() {
        isLoading = true;
        postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancel();
    }

    private void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(lineHeight, 0);
        valueAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                translateY = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.start();
    }
}
