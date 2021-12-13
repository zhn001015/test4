package com.example.forthtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class playmusic extends AppCompatActivity implements View.OnClickListener{
    //进度条
    List<String> song = new ArrayList<String>();


    private MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar sb ;
    private TextView music_time_now_seconds  ;
    private TextView music_time_now_minute;
    private TextView music_time_minute;
    private TextView music_time_seconds;
    private boolean isplay = false;
    String name = "";
    String pathname = null;
    int index = -1;
    static final int UPDATE_TEXT = 1;
    int music_time = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    //主线程中，实时更新音乐播放器的进度
                    int music_time_now = mediaPlayer.getCurrentPosition();
                    int minutes_now = music_time_now/1000/60;
                    int seconds_now = music_time_now/1000%60;
                    int minutes = music_time/1000/60;
                    int seconds = music_time/1000%60;
                    //实时显示时间进度
                    music_time_now_minute.setText(String.valueOf(minutes_now));
                    music_time_now_seconds.setText(String.valueOf(seconds_now));
                    music_time_minute.setText(String.valueOf(minutes));
                    music_time_seconds.setText(String.valueOf(seconds));
                    //实时显示进度条
                    sb.setProgress((int)((music_time_now/(float)music_time)*100));
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmusic);

        song.add("小尖尖");
        song.add("趁早");
        song.add("我知道");
        song.add("Selfish");
        song.add("别消失在我的世界");
        song.add("peril");

        Intent intent_get = getIntent();
        name = intent_get.getStringExtra("name");
        index = Integer.parseInt(intent_get.getStringExtra("index"));

        ImageButton play = findViewById(R.id.btn_play);
        ImageButton last = findViewById(R.id.btn_last);
        ImageButton next = findViewById(R.id.btn_next);
        TextView music_name = findViewById(R.id.song_name);
        music_time_now_minute = findViewById(R.id.tv_minute);
        music_time_now_seconds = findViewById(R.id.tv_second);
        music_time_minute = findViewById(R.id.music_time_minute);
        music_time_seconds = findViewById(R.id.music_time_seconds);
        sb = findViewById(R.id.sb);


        play.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);


        //动态申请权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else
        {
            Play(name);
        }
    }

    protected void Play(String str){
        pathname = searchFile(str);
        if(pathname.equals("找不到文件!!")){
            Toast.makeText(this, "没有文件，准备下载", Toast.LENGTH_SHORT).show();
            Intent intent_1 = new Intent(playmusic.this,download.class);
            intent_1.putExtra("name",str);
            startActivity(intent_1);

        }
        else{
            initMediaPlayer(pathname);
        }
    }


    //在指定文件夹下寻找音乐文件
    private String searchFile(String keyword) {
        String result = "";
        File []files = new File("/sdcard/atest").listFiles();
        for (File file : files) {
            if (file.getName().indexOf(keyword) >= 0){
                result += file.getPath();
            }
        }
        if (result.equals("")){
            result = "找不到文件!!";
        }
        return result;
    }

    //初始化音乐播放器
    private void initMediaPlayer(String pathname) {
        try {
            Log.i("音乐文件路径", pathname);
            mediaPlayer.setDataSource(pathname); // 指定音频文件的路径
            mediaPlayer.prepare();// 让MediaPlayer进入到准备状态
            music_time = mediaPlayer.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断用户是否授予权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Play(name);
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        TextView music_name = findViewById(R.id.song_name);
        music_name.getPaint().setFakeBoldText(true);
        ImageButton play = findViewById(R.id.btn_play);
        switch (v.getId()) {
            case R.id.btn_play:
                if(!isplay){


                    isplay = true;
                    play.setBackgroundResource(R.drawable.play);
                    if (!mediaPlayer.isPlaying()) {
                        music_name.setText(song.get(index));
                        mediaPlayer.start(); // 开始播放
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(true){
                                    Message message  = new Message();
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    message.what = 1;
                                    handler.sendMessage(message);
                                }
                            }
                        }).start();
                    }
                }
                else{

                    isplay = false;
                    play.setBackgroundResource(R.drawable.stop);
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause(); // 暂停播放
                    }
                }
                break;


            case R.id.btn_last:
                if(isplay){
                    play.setBackgroundResource(R.drawable.stop);
                    isplay = false;
                }

                if((index != 0) && (index != -1)){
                    mediaPlayer.reset();
                    Play(song.get(index - 1));
                    index = index - 1;
                    music_name.setText(song.get(index));
                    Toast.makeText(this, "切换至上一首", Toast.LENGTH_SHORT).show();
                }
                else{
                    mediaPlayer.pause();
                    Toast.makeText(this, "已经是第一首", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_next:
                if(isplay){
                    play.setBackgroundResource(R.drawable.stop);
                    isplay = false;
                }

                if((index != -1) && (index != song.size() - 1)){
                    mediaPlayer.reset();
                    Play(song.get(index + 1));
                    index = index + 1;
                    music_name.setText(song.get(index));
                    Toast.makeText(this, "切换至下一首", Toast.LENGTH_SHORT).show();
                }
                else{
                    mediaPlayer.pause();
                    Toast.makeText(this, "已经是最后一首", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

}
