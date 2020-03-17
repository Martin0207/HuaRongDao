package com.example.huarongdao.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.huarongdao.bean.RecordEntity;

import java.util.List;

@Dao
public interface RecordDao {

    /**
     * 插入数据
     */
    @Insert
    Long insertRecord(RecordEntity entity);

    /**
     * 通过难度系数查询记录
     *
     * @param difficulty 难度系数
     * @return 历史记录
     */
    @Query("SELECT * FROM RecordEntity WHERE difficulty = :difficulty")
    List<RecordEntity> queryByDifficulty(int difficulty);

}
