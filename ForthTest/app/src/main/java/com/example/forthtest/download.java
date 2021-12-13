package com.example.forthtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class download extends AppCompatActivity implements View.OnClickListener{

    TextView text;
    String str;
    String getCode;
    Intent intent_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Button download = findViewById(R.id.download);
        download.setOnClickListener(this);
        Button back = findViewById(R.id.back);
        back.setOnClickListener(this);
        Intent intent_down = getIntent();
        str = intent_down.getStringExtra("name");
        getCode = getIntent().getStringExtra("finish");
        text = findViewById(R.id.down_text);
        intent_2 = new Intent(download.this,musicmenu.class);

        if(getCode!= null && getCode.equals("true")){
            Toast.makeText(this, "已下载完成，为您自动跳转", Toast.LENGTH_SHORT).show();
            startActivity(intent_2);
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.download:
                Toast.makeText(this, "准备下载", Toast.LENGTH_SHORT).show();
                Intent intent_download = new Intent(download.this,MyIntentService.class);
                intent_download.putExtra("url",str);
                text.setText("正在下载...");
                startService(intent_download);
                break;
            case R.id.back:
                startActivity(intent_2);
                break;
        }

    }
}