package com.example.huarongdao.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.huarongdao.R;
import com.example.huarongdao.adapter.RecordAdapter;
import com.example.huarongdao.bean.RecordEntity;
import com.example.huarongdao.dao.DB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordListActivity extends BaseActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    private ListView lvContent;

    /**
     * 难度系数
     */
    private int difficulty;

    private RecordAdapter adapter;

    public static void start(Context context, int difficulty) {
        Intent starter = new Intent(context, RecordListActivity.class);
        starter.putExtra("difficulty", difficulty);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        lvContent = findViewById(R.id.lv);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        difficulty = getIntent().getIntExtra("difficulty", 3);
        tvTitle.setText(String.format("历史记录(%d)", this.difficulty));

        adapter = new RecordAdapter(new ArrayList<RecordEntity>(), this, difficulty);
        lvContent.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread() {
            @Override
            public void run() {
                final List<RecordEntity> recordEntities = DB.getInstance(RecordListActivity.this)
                        .recordDao()
                        .queryByDifficulty(difficulty);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (recordEntities != null) {
                            Collections.reverse(recordEntities);
                            adapter.refresh(recordEntities);
                        }
                    }
                });
            }
        }.start();

    }
}
