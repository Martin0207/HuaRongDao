package com.example.huarongdao.bean;

import androidx.annotation.Nullable;

/**
 * 定位类
 */
public class Position {

    /**
     * X轴定位
     */
    private int x;

    /**
     * Y轴定位
     */
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Position)) {
            return false;
        }
        Position o = (Position) obj;
        return x == o.x && y == o.y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
