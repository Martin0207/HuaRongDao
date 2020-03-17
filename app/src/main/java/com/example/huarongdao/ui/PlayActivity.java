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
        resetTitle();
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
                stopTiming();
                showSuccessDialog();
            }
        });
    }

    private void showSuccessDialog() {
        SharedPreferences sb = SharedPreferencesUtil.getSharedPreference(this);
        int record = sb.getInt(String.valueOf(difficulty), 0);
        String optimal;
        if (record == 0 || timing < record) {
            SharedPreferences.Editor edit = sb.edit();
            edit.putInt(String.valueOf(difficulty), timing);
            boolean commit = edit.commit();
            Timber.e("上传成功了没? " + commit);
            optimal = TimingUtil.timing2String(timing);
        } else {
            optimal = TimingUtil.timing2String(record);
        }
        new AlertDialog.Builder(this)
                .setTitle("恭喜过关")
                .setMessage(String.format("恭喜你以 %s 通过本关.\n最佳时间:%s", tvTiming.getText(), optimal))
                .setNegativeButton("重新开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //重新打乱棋子
                        pv.upsetPieces();
                        //重新布置棋子
                        pv.requestLayout();
                        startTiming();
                    }
                })
                .setPositiveButton("提高难度", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pv.setDifficulty(++difficulty);
                        resetTitle();
                        startTiming();
                    }
                })
                .setNeutralButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void resetTitle() {
        tvTitle.setText(String.format("%d X %d", difficulty, difficulty));
    }

    /**
     * 开始计时
     */
    private void startTiming() {
        //将计时归零,并发送倒计时通知
        timing = 0;
        tvTiming.setText(TimingUtil.timing2String(timing));
        timingHandler.sendEmptyMessageDelayed(WHAT_TIMING, WHAT_TIMING);
    }

    /**
     * 停止计时
     */
    private void stopTiming() {
        timingHandler.removeMessages(WHAT_TIMING);
    }
}
