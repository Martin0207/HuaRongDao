package com.example.huarongdao.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huarongdao.R;
import com.example.huarongdao.util.SharedPreferencesUtil;
import com.example.huarongdao.util.TimingUtil;
import com.example.huarongdao.widget.PlayView;

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

    private TextView tvTiming;

    /**
     * 难度系数
     */
    int difficulty = 3;

    /**
     * 计时标识, 也做发送延时时长使用
     */
    private static final int WHAT_TIMING = 1000;

    /**
     * 计时,
     */
    private int timing = 0;


    private Handler timingHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == WHAT_TIMING) {
                timing++;
                tvTiming.setText(TimingUtil.timing2String(timing));
                timingHandler.sendEmptyMessageDelayed(WHAT_TIMING, WHAT_TIMING);
            }
        }
    };

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
        tvTiming.setText(TimingUtil.timing2String(timing));
        timingHandler.sendEmptyMessageDelayed(WHAT_TIMING, WHAT_TIMING);
    }

    private void init() {
        difficulty = getIntent().getIntExtra("difficulty", 3);
        pv = findViewById(R.id.pv);
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTiming = findViewById(R.id.tv_timing);
        tvTitle.setText(String.format("%d X %d", difficulty, difficulty));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pv.setDifficulty(difficulty);
        pv.setListener(new PlayView.OnPlayOverListener() {
            @Override
            public void onPlayOver(int state) {
                timingHandler.removeMessages(WHAT_TIMING);
                showSuccessDialog();
            }
        });
    }

    private void showSuccessDialog() {
        SharedPreferences sb = SharedPreferencesUtil.getSharedPreference(this);
        int record = sb.getInt(String.valueOf(difficulty), 0);
        String optimal;
        if (record == 0 || timing < record) {
            sb.edit().putInt(String.valueOf(difficulty), timing);
            optimal = TimingUtil.timing2String(timing);
        } else {
            optimal = TimingUtil.timing2String(timing);
        }
        new AlertDialog.Builder(this)
                .setTitle("恭喜过关")
                .setMessage(String.format("恭喜你以 %s 通过本关.\n最佳时间:%s", tvTiming.getText(), optimal))
                .setNegativeButton("重新开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("提高难度", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
