package com.example.forthtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class musicmenu extends AppCompatActivity {
    List<String> song = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicmenu);
        song.add("小尖尖");
        song.add("趁早");
        song.add("我知道");
        song.add("Selfish");
        song.add("别消失在我的世界");
        song.add("peril");
        //定义适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                song);
        ListView list1 = (ListView)findViewById(R.id.list1);
        list1.setAdapter(adapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(musicmenu.this,playmusic.class);
                //获取歌曲名
                String a = song.get(position);
                intent.putExtra("name",a);
                intent.putExtra("index",String.valueOf(position));
                startActivity(intent);
            }
        });

    }
    }
