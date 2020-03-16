package com.example.huarongdao.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huarongdao.R;
import com.example.huarongdao.widget.PlayView;

import timber.log.Timber;

public class PlayActivity extends BaseActivity {

    /**
     * 游戏控件
     */
    private PlayView pv;

    /**
     * 返回按钮
     */
    private ImageView ivBack;

    /**
     * 标题
     */
    private TextView tvTitle;

    public static void start(Context context, int difficulty) {
        Intent starter = new Intent(context, PlayActivity.class);
        starter.putExtra("difficulty", difficulty);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        init();
    }

    private void init() {
        // 难度系数
        int difficulty = getIntent().getIntExtra("difficulty", 3);
        pv = findViewById(R.id.pv);
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(String.format("%d X %d", difficulty, difficulty));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pv.setDifficulty(difficulty);
    }
}
