package com.example.huarongdao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.huarongdao.R;
import com.example.huarongdao.bean.Position;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PlayView extends ViewGroup implements PieceView.OnPositionChangedListener {

    /**
     * 难度系数
     * 默认为3
     */
    private int difficulty = 3;
    /**
     * 当前布局的长款
     */
    private int length = 0;

    /**
     * 棋子正确的数量
     */
    private int correctCount = 0;

    /**
     * 空闲位置
     */
    private Position idlePosition;

    /**
     * 游戏结束监听
     */
    private OnPlayOverListener listener;

    public PlayView(Context context) {
        super(context);
    }

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        correctCount = difficulty * difficulty - 1;
        //初始化空格定位,默认为最后一个格子
        idlePosition = new Position(difficulty, difficulty);

        //清空所有child
        //如果不清空,会出现底层背景重叠的问题
        removeAllViews();
        for (int i = 1; i < difficulty * difficulty; i++) {
            //创建棋子
            PieceView pieceView = new PieceView(getContext(), difficulty, i, this);
            //给棋子添加布局属性
            pieceView.setBackgroundResource(R.drawable.bg_piece);
            LayoutParams layoutParams = new LayoutParams(pieceView.getLength(), pieceView.getLength());
            pieceView.setLayoutParams(layoutParams);
            //将棋子添加有序临时数组中
            addView(pieceView);
            //设置棋子的点击事件
            pieceView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    processPieceClick((PieceView) v);
                }
            });
        }

        upsetPieces();
    }

    /**
     * 打乱棋子
     * 之前使用的随机数打乱法，会导致最终无解
     */
    public void upsetPieces() {
        //打乱次数,难度系数的平方
        int count = difficulty * difficulty;
        do {
            //随机数,模拟点击位置
            int round = (int) Math.floor(Math.random() * getChildCount());
            //根据难度系数与模拟点击的位置数字,得出棋子的正确坐标
            int randomX = round % difficulty == 0 ? difficulty : round % difficulty;
            int randomY = (round - 1) / difficulty + 1;
            //空格坐标
            int idleX = idlePosition.getX();
            int idleY = idlePosition.getY();
            if (randomX == idleX && randomY != idleY) {
                count--;
                idlePosition.setY(randomY);
                for (int i = 0; i < getChildCount(); i++) {
                    PieceView pv = (PieceView) getChildAt(i);
                    int currX = pv.getCurrentPosition().getX();
                    int currY = pv.getCurrentPosition().getY();
                    if (currX == idleX) {
                        if (randomY > idleY && currY <= randomY && currY > idleY) {
                            pv.setCurrentPosition(currX, currY - 1);
                        } else if (randomY < idleY && currY >= randomY && currY < idleY) {
                            pv.setCurrentPosition(currX, currY + 1);
                        }
                    }
                }

            } else if (randomY == idleY && randomX != idleX) {
                count--;
                idlePosition.setX(randomX);
                for (int i = 0; i < getChildCount(); i++) {
                    PieceView pv = (PieceView) getChildAt(i);
                    int currY = pv.getCurrentPosition().getY();
                    int currX = pv.getCurrentPosition().getX();
                    if (currY == idleY) {
                        if (randomX > idleX && currX <= randomX && currX > idleX) {
                            pv.setCurrentPosition(currX - 1, currY);
                        } else if (randomX < idleX && currX >= randomX && currX < idleX) {
                            pv.setCurrentPosition(currX + 1, currY);
                        }
                    }
                }
            }
        } while (count >= 0);

    }

    /**
     * 处理棋子的点击事件
     *
     * @param pieceView 棋子
     */
    private void processPieceClick(PieceView pieceView) {
        Position clickPosition = pieceView.getCurrentPosition();
        int idleX = idlePosition.getX();
        int idleY = idlePosition.getY();
        //若点击的棋子与空格在X轴交汇，则为上下移动
        if (clickPosition.getX() == idleX) {
            //将空格定位到点击的棋子位置
            idlePosition.setY(clickPosition.getY());
            //遍历棋子,找出被点击的棋子与原空格位置之间的所有棋子,一起移动
            for (int i = 0; i < getChildCount(); i++) {
                PieceView pv = (PieceView) getChildAt(i);
                int currX = pv.getCurrentPosition().getX();
                int currY = pv.getCurrentPosition().getY();
                if (currX == idleX) {
                    if (clickPosition.getY() > idleY && currY <= clickPosition.getY() && currY > idleY) {
                        pv.moveY(true);
                    } else if (clickPosition.getY() < idleY && currY >= clickPosition.getY() && currY < idleY) {
                        pv.moveY(false);
                    }
                }
            }
        }
        //此处逻辑与上面类似,不重复
        else if (clickPosition.getY() == idleY) {
            idlePosition.setX(clickPosition.getX());
            for (int i = 0; i < getChildCount(); i++) {
                PieceView pv = (PieceView) getChildAt(i);
                int currY = pv.getCurrentPosition().getY();
                int currX = pv.getCurrentPosition().getX();
                if (currY == idleY) {
                    if (clickPosition.getX() > idleX && currX <= clickPosition.getX() && currX > idleX) {
                        pv.moveX(true);
                    } else if (clickPosition.getX() < idleX && currX >= clickPosition.getX() && currX < idleX) {
                        pv.moveX(false);
                    }
                }

            }
        }
    }

    /**
     * 设置难度系数
     */
    public void setDifficulty(int dif) {
        difficulty = dif;
        init();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //将宽高设置为统一长度,取最小值
        int defaultWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int defaultHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int minSpec = defaultWidth < defaultHeight ? widthMeasureSpec : heightMeasureSpec;
        super.onMeasure(minSpec, minSpec);
        length = Math.min(defaultWidth, defaultHeight);
        int childLength = this.length / difficulty;
        for (int i = 0; i < getChildCount(); i++) {
            ((PieceView) getChildAt(i)).setLength(childLength);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //遍历所有的child,将子View依次排开
        for (int i = 0; i < getChildCount(); i++) {
            PieceView child = (PieceView) getChildAt(i);
            Position cp = child.getCurrentPosition();
            int cX = (cp.getX() - 1) * child.getLength();
            int cY = (cp.getY() - 1) * child.getLength();
            child.layout(cX, cY, cX + child.getLength(), cY + child.getLength());
        }
    }

    @Override
    public void onPositionChanged(boolean correct) {
        if (correct) {
            correctCount++;
        } else {
            correctCount--;
        }
        Timber.e("当前正确的数量: " + correctCount);
        if (correctCount >= ((difficulty * difficulty) - 1)) {
            if (listener != null) {
                listener.onPlayOver(0);
            }
        }
    }

    public void setListener(OnPlayOverListener listener) {
        this.listener = listener;
    }

    public interface OnPlayOverListener {
        /**
         * 游戏结束回调
         *
         * @param state 结束状态,目前默认为0
         *              预留后面添加超时失败等状态
         */
        void onPlayOver(int state);
    }
}
