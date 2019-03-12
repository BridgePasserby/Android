package com.zingking.demo.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.zingking.demo.R;

import java.lang.ref.WeakReference;

// https://blog.csdn.net/lmj623565791/article/details/42094215
// 重点：
// 1.比例缩放
// 2.Xfermode使用
// 3.mask图层实现

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 * author：Z.kai
 * date：2019/3/12
 * description：圆形图片
 */
class CircleImageView extends AppCompatImageView {
    private final int maskImageId;
    private Paint paint;
    public static final int RADIUS_DEFAULT = 10;
    private int radius;
    private Bitmap mMaskBitmap;
    private WeakReference<Bitmap> mWeakBitmap;// todo 没看懂这个
    private PorterDuffXfermode xFermode;
    /**
     * 图片的类型，圆形or圆角
     */
    private int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    private static final int TYPE_HEART = 2;
    private static final int TYPE_MASK_IMAGE = 3;


    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        radius = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_radius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, RADIUS_DEFAULT, getResources().getDisplayMetrics()));
        type = typedArray.getInt(R.styleable.CircleImageView_type, TYPE_CIRCLE);
        maskImageId = typedArray.getResourceId(R.styleable.CircleImageView_maskImage, R.mipmap.ic_launcher_round);
        typedArray.recycle();
        xFermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setFilterBitmap(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 如果是圆形，则改变view宽高一致，以小值为准
        if (type == TYPE_CIRCLE) {
            int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 图片掩码不加这句不管用
        int sc;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint);
        } else {
            sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        }
        // 从缓存中取bitmap
        Bitmap bitmap = mWeakBitmap == null ? null : mWeakBitmap.get();
        if (bitmap == null || bitmap.isRecycled()) {
            // 得到drawable
            Drawable drawable = getDrawable();
            if (drawable != null) {
                // 获取drawable宽高
                int dWidth = drawable.getIntrinsicWidth();
                int dHeight = drawable.getIntrinsicHeight();
                // 创建bitmap
                bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                float scale = 1.0f;
                // 创建画布
                Canvas drawCanvas = new Canvas(bitmap);
                // 按照bitmap的宽高以及view的宽高及时缩放比例，因为设置的src宽高比例可能和imageview的宽高比例不同，这里我们不希望图片失真；
                if (type == TYPE_ROUND) {
                    // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                    scale = Math.max(getWidth() * 1.0f / dWidth, getHeight() * 1.0f / dHeight);
                } else {
                    scale = getWidth() * 1.0F / Math.min(dWidth, dHeight);
                }
                //根据缩放比例，设置bounds，相当于缩放图片了
                drawable.setBounds(0, 0, (int) (scale * dWidth), (int) (scale * dHeight));
                drawable.draw(drawCanvas);
                if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
                    mMaskBitmap = getBitmap();
                }
                // Draw Bitmap.
                paint.reset();
                paint.setFilterBitmap(false);
                paint.setXfermode(xFermode);
                //绘制形状
                drawCanvas.drawBitmap(mMaskBitmap, 0, 0, paint);

                paint.setXfermode(null);
                //将准备好的bitmap绘制出来
                canvas.drawBitmap(bitmap, 0, 0, null);
                //bitmap缓存起来，避免每次调用onDraw，分配内存
                mWeakBitmap = new WeakReference<Bitmap>(bitmap);
            }
        }
        if (bitmap != null) {
            paint.setXfermode(null);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        }
        paint.setColor(Color.RED);
        canvas.restoreToCount(sc);
    }

    private Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), radius, radius, paint);
        } else if (type == TYPE_CIRCLE) {
            canvas.drawCircle(getWidth() / 2.0f, getWidth() / 2.0f, getWidth() / 2.0f, paint);
        } else if (type == TYPE_HEART) {
            float radiusWidth = radius;
            float radiusHeight = radius;
            if (radius == 0) {
                radiusWidth = getWidth() / 2.0f;
                radiusHeight = getHeight() / 2.0f;
            }
            canvas.drawArc(new RectF(0, 0, radiusWidth, radiusHeight), -180f, 180f, true, paint);
            canvas.drawArc(new RectF(radiusWidth, 0, 2 * radiusWidth, radiusHeight), -180f, 180f, true, paint);
            Path path = new Path();
            path.moveTo(0, radiusHeight / 2);// 此点为多边形的起点
            path.lineTo(2 * radiusWidth, radiusHeight / 2);
            path.lineTo(radiusWidth, 2 * radiusHeight);
            path.close(); // 使这些点构成封闭的多边形
            canvas.drawPath(path, paint);
        } else if (type == TYPE_MASK_IMAGE) {
            return BitmapFactory.decodeResource(getResources(), maskImageId);
        }
        return bitmap;
    }

    @Override
    public void invalidate() {
        mWeakBitmap = null;
        if (mMaskBitmap != null) {
            mMaskBitmap.recycle();
            mMaskBitmap = null;
        }
        super.invalidate();
    }
}
