package com.shoon.android_challenge_intermediatejava;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Draw symbol。
 * @author Shoon Akagi
  *  */
public class SymbolSliderView extends View implements Animatable {

    private Paint paint;
    private float fX;    // 図形を描画する X 座標    // (1)
    private float fY;    // 図形を描画する Y 座標    // (2)
    private int iWidthCanvas;
    private int iHeightCanvas;
    private int iStartingRating = 0;
    private int iRate = iStartingRating;
    private int iRateBefore;
    private int iSizeText = 100;
    private int iMaxRating = 10;
    private int iStartingPointX = 100;
    private int iStartingPointY = iSizeText;
    private int iEndPointX;
    private int iEndPointY;
    private int iGapText = 10;
    private String strEmpty = "☆";
    private String strFilled = "★";
    private int iColorSymbol = Color.BLACK;
    private static Drawable drawable;
    private static AnimatedVectorDrawable animationDrawable;
    private static Drawable drawableAnime;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SymbolSliderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SymbolSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public SymbolSliderView(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        // 画面のサイズを取得する
        WindowManager manager = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        iWidthCanvas = display.getWidth();
        iHeightCanvas = display.getHeight();

        // ペイントオブジェクトを設定する
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);    // (4)
        paint.setStyle(Style.FILL);    // (5)

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    private void initialize(AttributeSet attrs) {
        Context context = getContext();
        // 画面のサイズを取得する
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        iWidthCanvas = display.getWidth();
        iHeightCanvas = display.getHeight();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SymbolSliderlView);

            iStartingRating = typedArray.getInteger(R.styleable.SymbolSliderlView_iStartingRating, 0);
            iSizeText = typedArray.getInteger(R.styleable.SymbolSliderlView_iSizeText, 100);
            iMaxRating = typedArray.getInteger(R.styleable.SymbolSliderlView_iMaxRating, 10);
            iGapText = typedArray.getInteger(R.styleable.SymbolSliderlView_iGapText, 10);
            // ペイントオブジェクトを設定する
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.RED);    // (4)
            paint.setStyle(Style.FILL);    // (5)
            typedArray.recycle();
        } else {
            // ペイントオブジェクトを設定する
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.RED);    // (4)
            paint.setStyle(Style.FILL);    // (5)
        }
    }


    public void setRating(int iRating) {
        iRate = iRating;
        invalidate();
    }

    public int getRating() {
        return iRate;

    }

    public String getStringRating() {
        return Integer.toString(iRate);

    }

    public void setMaxRating(int iMaxRating) {
        this.iMaxRating = iMaxRating;
    }

    public void setStartingRating(int iRate) {
        this.iStartingRating = iRate;
    }

    public void setStrFilled(String strFilled) {
        this.strFilled = strFilled;
    }

    public void setStrEmpty(String strEmpty) {
        this.strEmpty = strEmpty;
    }

    @TargetApi(Build.VERSION_CODES.P)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        // 格子を描画する
        drawGrid(canvas, 50);


        int iEnd = iWidthCanvas - 50, iPitch = (iEnd - iStartingPointX) / iMaxRating;

        paint.setTextSize(iPitch);
        drawCommentOnCanvas("Interval=" + Integer.toString(50*getRatingByMouseLocation((int) fX, iStartingPointX, iEnd))+"mil seconds", 0,iPitch, canvas);

   //     drawCommentOnCanvas("Animated version", 0, iPitch, canvas);

        try{
            int iCurrentRating=getRatingByMouseLocation((int) fX, iStartingPointX, iEnd);
            iRate=iCurrentRating;
            if(iCurrentRating==iRateBefore){
                drawSymbol(canvas,R.drawable.ic_star_black_24dp,R.drawable.ic_star_border_black_24dp,
                        iStartingPointX,iEnd,iPitch,iStartingPointY+iPitch,
                        getRatingByMouseLocation((int)fX,iStartingPointX,iEnd),
                        iMaxRating);
            }else{
                drawSymbol(canvas, R.drawable.ic_star_black_24dp, R.drawable.ic_star_half_black_24dp,
                        R.drawable.star_lefttoright, R.drawable.ic_star_border_black_24dp,
                        iStartingPointX, iEnd, iPitch, iStartingPointY + iPitch,
                        getRatingByMouseLocation((int) fX, iStartingPointX, iEnd),
                        iMaxRating);
                iRateBefore=iCurrentRating;
            }


        }catch (Exception e){
        }
    //    drawCommentOnCanvas("Animated version by inputing number", 2, iPitch, canvas);


    }

    //Debug purpose
    void drawCommentOnCanvas(String strComment, int iRow, int iPitch, Canvas canvas) {
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        canvas.drawText(strComment,
                iStartingPointX, iStartingPointY + iPitch * iRow, paint);
    }

    //String slider
    private void drawSymbol(Canvas canvas, String strSymbolLeft, String strSymbolRight,
                            int iStart, int iEnd, int iPitch, int iY,
                            int iRating, int iMaxRating) {
        paint.setColor(iColorSymbol);

        paint.setTextSize(iPitch - iGapText);
        for (int i = 0; i < iMaxRating; i++) {
            if (i < iRating) {
                for (int j = 0; j < 100; j++) {
                    paint.setAlpha(j);
                    canvas.drawText(strSymbolLeft, iStart + iGapText + i * iPitch, iY, paint);
                }
            } else {
                canvas.drawText(strSymbolRight, iStart + i * iPitch, iY, paint);
            }

        }
    }

    //Simple Image slider
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void drawSymbol(Canvas canvas, int iSymbolLeft, int iSymbolRight,
                           int iStart, int iEnd, int iPitch, int iY,
                           int iRating, int iMaxRating) {

        for (int i = 0; i < iMaxRating; i++) {
            if (i < iRating) {
                paint.setColor(Color.BLACK);
                drawable = ContextCompat.getDrawable(getContext(), iSymbolLeft);
            } else {
                drawable = ContextCompat.getDrawable(getContext(), iSymbolRight);
                paint.setColor(Color.GREEN);
            }
            drawable.setBounds(iStart + i * iPitch, iY - iPitch, iStart + i * iPitch + iPitch, iY);
            drawable.draw(canvas);
            invalidate();
        }
    }

    //Animated Image slider
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void drawSymbol(Canvas canvas, int iSymbolLeft, int iSymbolRightToLeft, int iSymbolLeftToRight,
                           int iSymbolRight,
                           int iStart, int iEnd, int iPitch, int iY,
                           int iRating, int iMaxRating) throws InterruptedException {

        for (int i = 0; i < iMaxRating; i++) {
            paint.setColor(Color.BLACK);
            if (i < iRating - 1) {//left
                drawable = ContextCompat.getDrawable(getContext(), iSymbolLeft);
                drawable.setBounds(iStart + i * iPitch, iY - iPitch, iStart + i * iPitch + iPitch, iY);
                drawable.draw(canvas);

            } else if (i == iRating - 1) {
                if (iRating > iRateBefore) {
                    drawableAnime = ContextCompat.getDrawable(getContext(), iSymbolLeftToRight);
                    drawableAnime.setBounds(iStart + i * iPitch, iY - iPitch, iStart + i * iPitch + iPitch, iY);
                    animateStart(canvas, drawableAnime);
                } else {
                        drawable = ContextCompat.getDrawable(getContext(), iSymbolLeft);
                        drawable.setBounds(iStart + i * iPitch, iY - iPitch, iStart + i * iPitch + iPitch, iY);
                        drawable.draw(canvas);
                }
            } else if (i == iRating) {

                if (iRating < iRateBefore) {

                    drawableAnime = ContextCompat.getDrawable(getContext(), iSymbolRightToLeft);
                    drawableAnime.setBounds(iStart + i * iPitch, iY - iPitch, iStart + i * iPitch + iPitch, iY);
                    animateStart(canvas,drawableAnime);
                } else {
                        drawable = ContextCompat.getDrawable(getContext(), iSymbolRight);
                        drawable.setBounds(iStart + i * iPitch, iY - iPitch, iStart + i * iPitch + iPitch, iY);
                        drawable.draw(canvas);
                }
            } else {
                drawable = ContextCompat.getDrawable(getContext(), iSymbolRight);
                drawable.setBounds(iStart + i * iPitch, iY - iPitch, iStart + i * iPitch + iPitch, iY);
                drawable.draw(canvas);
            }
        }
    }


    private void animateStart(Canvas canvas, Drawable drawable) {

        int level = 2000;
        drawable.setLevel(level);

        if (Build.VERSION.SDK_INT > 27) {

            if (drawable instanceof AnimatedVectorDrawable) {
                animationDrawable = (AnimatedVectorDrawable) drawable;
                animationDrawable.draw(canvas);
                ViewCompat.postInvalidateOnAnimation(this);
                if (animationDrawable.isRunning()) {
                    animationDrawable.stop();
                } else {
                    animationDrawable.start();
               }

            }else{
                drawable.draw(canvas);
            }
        }
    }


    public int getRatingByMouseLocation(int iLocation, int iLeftLimit, int iRightLimit) {
        int iResult = iMaxRating*iLocation/(iRightLimit-iLeftLimit);
        return iResult;
    }

    public String getValues(){
        return Float.toString(fX)+","+Float.toString(fY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {    // (7)
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:    // 指をタッチした    // (8)
                assert true;    // 何もしない
                break;
            case MotionEvent.ACTION_MOVE:    // 指を動かしている    // (9)
                fX=event.getX();
                fY=event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:        // 指を離した    // (12)
                assert true;    // 何もしない
                break;
            default:
                assert true;    // 何もしない
                break;
        }
        invalidate();    // (13)
        return true;    // (14)
    }
    /**
     * 画面に格子を描画する。
     * @param canvas
     * @param interval 格子を描く間隔
     */
    private void drawGrid(final Canvas canvas, int interval) {
        // 格子を描画する
        Paint paint = new Paint();        // (15)
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        for (int i = 0; i < Math.max(iWidthCanvas, iHeightCanvas); i += interval) {
            canvas.drawText(Integer.toString(i), i, paint.getTextSize(), paint);
            canvas.drawLine(i, 0, i, iHeightCanvas, paint);
            canvas.drawText(Integer.toString(i), 0, i, paint);
            canvas.drawLine(0, i, iWidthCanvas, i, paint);
        }
    }

    @Override
    public void start() {
        invalidate();
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
