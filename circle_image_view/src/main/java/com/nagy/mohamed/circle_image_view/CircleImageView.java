package com.nagy.mohamed.circle_image_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.nagy.mohamed.circle_image_view.R.styleable;

public class CircleImageView extends ImageView {
    private Integer radius;
    private int borderSize;
    private int borderColor;
    private int squareSideLength;
    private static final int CENTER_FRAME_X = 0;
    private static final int CENTER_FRAME_Y = 0;
    private static final int DEFAULT_RADIUS = 200;
    private static final int DEFAULT_BORDER_SIZE = 0;
    private static final int DEFAULT_BORDER_COLOR = 0;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setAttr(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setAttr(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        Drawable imageDrawable = this.getDrawable();
        Bitmap imageBitmap = ((BitmapDrawable)imageDrawable).getBitmap();
        if(this.radius.intValue() != 0) {
            Bitmap circularBitmapImageFrame = this.getCircularBitmapImage(imageBitmap);
            canvas.drawBitmap(circularBitmapImageFrame, this.getXCenter(), this.getYCenter(), (Paint)null);
            this.drawCircularImageViewBorder(canvas);
        }
    }

    private float getXCenter() {
        return (float)(this.borderSize / 2);
    }

    private float getYCenter() {
        return (float)(this.borderSize / 2);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(this.squareSideLength + this.borderSize, this.squareSideLength + this.borderSize);
    }

    private void drawCircularImageViewBorder(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        int RED = Color.red(this.borderColor);
        int GREEN = Color.green(this.borderColor);
        int BLUE = Color.blue(this.borderColor);
        int ALPHA = Color.alpha(this.borderColor);
        paint.setStrokeWidth((float)this.borderSize);
        paint.setARGB(ALPHA, RED, GREEN, BLUE);
        canvas.drawCircle((float)this.radius.intValue() + this.getXCenter(), (float)this.radius.intValue() + this.getYCenter(), (float)this.radius.intValue(), paint);
    }

    private Bitmap getCircularBitmapImage(Bitmap imageBitmap) {
        Bitmap imageBitmapScaled = this.scaleBitmapImage(imageBitmap);
        Bitmap newImageViewBitmapFrame = this.getSquareImageViewBitmapFrame();
        Canvas canvas = new Canvas(newImageViewBitmapFrame);
        Paint framePaint = new Paint();
        canvas.drawCircle((float)this.radius.intValue(), (float)this.radius.intValue(), (float)this.radius.intValue(), framePaint);
        framePaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(imageBitmapScaled, 0.0F, 0.0F, framePaint);
        return newImageViewBitmapFrame;
    }

    private Bitmap getSquareImageViewBitmapFrame() {
        return Bitmap.createBitmap(this.squareSideLength, this.squareSideLength, Config.ARGB_8888);
    }

    private Bitmap scaleBitmapImage(Bitmap imageBitmap) {
        int IMAGE_WIDTH = imageBitmap.getWidth();
        int IMAGE_HEIGHT = imageBitmap.getHeight();
        Bitmap imageBitmapScaled = imageBitmap;
        if(IMAGE_HEIGHT != this.squareSideLength || IMAGE_WIDTH != this.squareSideLength) {
            imageBitmapScaled = Bitmap.createScaledBitmap(imageBitmap, this.squareSideLength, this.squareSideLength, false);
        }

        return imageBitmapScaled;
    }

    private void setAttr(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet, styleable.CircleImageView, 0, 0);

        try {
            this.radius = Integer.valueOf(typedArray.getInteger(styleable.CircleImageView_radius, 200));
            this.borderSize = typedArray.getInteger(styleable.CircleImageView_borderSize, 0);
            this.borderColor = typedArray.getColor(styleable.CircleImageView_borderColor, 0);
            this.squareSideLength = this.radius.intValue() * 2;
        } finally {
            typedArray.recycle();
        }

    }
}
