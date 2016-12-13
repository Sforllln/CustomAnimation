package zhengliang.com.customanimationframework.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import zhengliang.com.customanimationframework.R;

/**
 * Created by llln on 2016/12/8.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mSurfaceHolder;

    private Canvas mCanvas;

    private boolean startDraw;

    private Path mPath = new Path();

    private Paint mPaint = new Paint();


    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startDraw = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        startDraw = false;
    }

    @Override
    public void run() {
        while (startDraw) {
            draw();
        }
    }

    int penWidth ;
    int penHeight;
    Bitmap pen;
    private void draw() {
            try {
                mCanvas = mSurfaceHolder.lockCanvas();
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setAntiAlias(true);
                pen = BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher);
                penWidth = pen.getWidth();
                penHeight = pen.getHeight();
                if (isMoving){
                    //设置画笔的图标
                    mCanvas.drawBitmap(pen,x-penWidth/2, y-penHeight/2,mPaint);
                }else {
                    mCanvas.drawBitmap(pen,0, 0,mPaint);
                }
                mPaint.setStrokeWidth(px2dp(getContext(), 30));
                mPaint.setColor(Color.GRAY);
                mCanvas.drawPath(mPath, mPaint);
            } catch (Exception e) {

            } finally {
                if (mCanvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
            }

    }
    boolean isMoving = false;
    int x;
    int y;
    public static int sX ,sY;
    public static List<Integer> mX = new ArrayList<>();
    public static List<Integer> mY = new ArrayList<>();
    List<Path> path1=new ArrayList<>();

     @Override
    public boolean onTouchEvent(MotionEvent event) {
         x = (int) event.getX();
         y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("mtag", "onTouchEvent: DOWN- "+"X :" +x +"--"+"Y :"+y);
                mPath.moveTo(x,y);
                sY = this.y;
                sX = this.x;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("mtag", "onTouchEvent: MOVE- "+"X :" +x +"--"+"Y :"+y);
                isMoving = true;
                mPath.lineTo(x,y);
                path1.add(mPath);
                mX.add(x);
                mY.add(y);
                break;
            case MotionEvent.ACTION_UP:
                Log.d("mtag", "onTouchEvent: UP--   "+"X :" +x +"--"+"Y :"+y);

                break;
        }
        return true;
    }


    public void reSet(){
        x = penWidth/2;
        y = penHeight/2;
        mPath.reset();
    }


    /**
     * px转dp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
//
//    public void Drawerline(){
//        for (int i = 0;i<path1.size();i++){
//            ObjectAnimator anim =new ObjectAnimator().ofFloat(pen,"x","y",path1.get(i));
//            anim.start();
//        }
//    }

}
