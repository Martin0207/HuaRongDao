package com.example.huarongdao.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.huarongdao.R;
import com.example.huarongdao.adapter.DifficultyAdapter;
import com.example.huarongdao.widget.PieceView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * 返回按钮
     */
    private ImageView ivBack;

    /**
     * 列表
     */
    private ListView lvContent;

    /**
     * 列表适配器
     */
    private DifficultyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        initListener();
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayActivity.start(MainActivity.this, adapter.getItem(position));
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    /**
     * 初始化控件及数据展示
     */
    private void init() {
        ivBack = findViewById(R.id.iv_back);
        lvContent = findViewById(R.id.lv);

        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 3; i <= 8; i++) {
            list.add(i);
        }
        adapter = new DifficultyAdapter(this, list);

        lvContent.setAdapter(adapter);
    }

}