package com.example.huarongdao.widget;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.huarongdao.bean.Position;

import timber.log.Timber;

public class PieceView extends View {

    /**
     * 难度系数
     */
    private int difficulty;

    /**
     * 当前数字
     */
    private int number;

    /**
     * 正确的定位
     */
    private Position correctPosition;

    /**
     * 当前的定位
     */
    private Position currentPosition;

    /**
     * 当前View的长款
     */
    private int length;

    /**
     * 画笔
     */
    private Paint paint;

    /**
     * 位置更改监听
     */
    private OnPositionChangedListener listener;

    /**
     * 当前定位是否正确
     * 默认为false,且false时,棋子变动后依旧为false,则不会调用改变通知
     * 反之亦然
     */
    private boolean correct = false;

    /**
     * 初始化
     *
     * @param context    上下文对象
     * @param difficulty 难度系数
     * @param number     数字
     */
    public PieceView(Context context, int difficulty, int number, OnPositionChangedListener listener) {
        super(context);
        init(difficulty, number, listener);
    }

    private void init(int difficulty, int number, OnPositionChangedListener listener) {
        this.difficulty = difficulty;
        this.number = number;
        //根据难度系数与棋子的数字,得出棋子的正确坐标
        int x = number % difficulty == 0 ? difficulty : number % difficulty;
        int y = (number - 1) / difficulty + 1;

        correctPosition = new Position(x, y);

        //初始化 画笔
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(100f * (4f / difficulty));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        this.listener = listener;
    }


    /**
     * 设置初始位置
     *
     * @param number 棋子定位
     */
    public void initCurrentPosition(int number) {
        int y = (number - 1) / difficulty + 1;
        int x = number % difficulty == 0 ? difficulty : number % difficulty;
        currentPosition = new Position(x, y);
        verifyCorrect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //将画布坐标系移到中间
        canvas.translate(length / 2, length / 2);

        //将数字画在画布上
        Rect rect = new Rect();
        String s = String.valueOf(number);
        paint.getTextBounds(s, 0, s.length(), rect);
        canvas.drawText(s, -rect.width() / 2, rect.height() / 2, paint);
    }

    /**
     * X轴移动
     *
     * @param left 是否向左移动
     */
    public void moveX(boolean left) {
        final int currX = getLeft();
        //目标X位置
        int targetX = currX + (left ? -length : length);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(currX, targetX);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                //重新布局自己
                layout(value, getTop(), value + length, getBottom());
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
        currentPosition.setX(currentPosition.getX() + (left ? -1 : 1));
        verifyCorrect();
    }

    /**
     * Y轴移动
     *
     * @param top 是否向上移动
     */
    public void moveY(boolean top) {
        final int currY = getTop();
        int targetY = currY + (top ? -length : length);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(currY, targetY);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                layout(getLeft(), value, getRight(), value + length);
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
        currentPosition.setY(currentPosition.getY() + (top ? -1 : 1));
        verifyCorrect();
    }

    /**
     * 验证当前位置是否正确,并根据逻辑调用改变监听
     */
    private void verifyCorrect() {
        if (correct != (correctPosition.equals(currentPosition))) {
            correct = !correct;
            listener.onPositionChanged(correct);
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Position getCorrectPosition() {
        return correctPosition;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public interface OnPositionChangedListener {
        /**
         * 位置调动时调用
         *
         * @param correct 位置是否正确
         */
        void onPositionChanged(boolean correct);
    }

}
