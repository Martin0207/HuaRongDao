package com.example.huarongdao.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecordEntity {

    @PrimaryKey
    Long id;

    /**
     * 记录时间
     */
    String datetime;

    /**
     * 难度系数
     */
    int difficulty;

    /**
     * 完成时间
     */
    int timing;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getTiming() {
        return timing;
    }

    public void setTiming(int timing) {
        this.timing = timing;
    }
}
