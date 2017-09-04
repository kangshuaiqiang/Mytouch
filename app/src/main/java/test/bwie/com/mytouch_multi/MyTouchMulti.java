package test.bwie.com.mytouch_multi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 黑白 on 2017/9/4.
 */

public class MyTouchMulti extends View {
    //初始化画笔
    private Paint paint;
    //X轴
    private float canX;
    //Y轴
    private float canY;
    private int canColor;
    private float radii;
    private final int DRAG = 1;//拖拽
    private final int ZOOM = 2;//缩放
    private final int IDLE = 3;  //空闲状态
    private int move = IDLE;
    private double initial;
    private float end;

    public MyTouchMulti(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //获得自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTouchMulti);
        canX = typedArray.getDimension(R.styleable.MyTouchMulti_canX, 20);
        canY = typedArray.getDimension(R.styleable.MyTouchMulti_canY, 20);
        canColor = typedArray.getColor(R.styleable.MyTouchMulti_canColor, Color.RED);
        radii = typedArray.getDimension(R.styleable.MyTouchMulti_radii, 20);
        typedArray.recycle();

        //初始化画笔
        paint = new Paint();
        paint.setColor(canColor);
        //抗锯齿
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘画
        canvas.drawCircle(canX, canY, radii, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //MotionEvent.ACTION_MASK可用于多点触控
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN://单点触控
                move = DRAG;
                break;

            case MotionEvent.ACTION_POINTER_DOWN://多点触控
                move = ZOOM;
                float x0 = event.getX(0);
                float x1 = event.getX(1);
                float y0 = event.getY(0);
                float y1 = event.getY(1);
                float vx = x1 - x0;
                float vy = y1 - y0;
                initial = Math.sqrt(vx * vx + vy * vy);
                break;

            case MotionEvent.ACTION_MOVE:
                //移动
                if (move == DRAG) {
                    //移动
                    canX = event.getX();
                    canY = event.getY();
                    invalidate();
                } else {
                    float x0_1 = event.getX(0);
                    float x1_1 = event.getX(1);
                    float y0_1 = event.getY(0);
                    float y1_1 = event.getY(1);
                    float vx1 = x1_1 - x0_1;
                    float vy1 = y1_1 - y0_1;
                    double sqrt = Math.sqrt(vx1 * vx1 + vy1 * vy1);
                    end = (float) (sqrt = initial);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                move = IDLE;
                break;

        }

        //返回true 表示事件不再向下传
        return true;
    }
}
