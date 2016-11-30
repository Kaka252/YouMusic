package com.zhouyou.music.module.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wonderkiln.blurkit.BlurKit;
import com.zhouyou.library.utils.Scale;

/**
 * 作者：ZhouYou
 * 日期：2016/11/29.
 */
public class AlbumImageView extends ImageView {

    private Context context;
    private Paint paint;
    private BitmapShader shader;
    private Matrix matrix;
    // 加载的图片
    private Bitmap bitmap;
    // 圆形半径
    private int radius;
    // 设置图片模糊
    private boolean isBlur;
    // 设置图片为圆形
    private boolean isCircle;

    public AlbumImageView(Context context) {
        this(context, null);
    }

    public AlbumImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlbumImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        matrix = new Matrix();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    private void setupShader() {
        float scale;
        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        int bSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        scale = getWidth() * 1.0f / bSize;
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        matrix.setScale(scale, scale);
        // 设置变换矩阵
        shader.setLocalMatrix(matrix);
        // 设置shader
        paint.setShader(shader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Scale.getDisplayWidth(context) - Scale.dp2px(context, 100);
        radius = width / 2;
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setupShader();
        if (isCircle) {
            canvas.drawCircle(radius, radius, radius, paint);
        } else {
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        if (isBlur) bitmap = BlurKit.getInstance().blur(bitmap, 18);
        this.bitmap = bitmap;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }

    public void setBlur(boolean blur) {
        isBlur = blur;
    }
}
