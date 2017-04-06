package com.example.demoshareimageview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.demoshareimageview.drawable.DrawableActivity;
import com.example.demoshareimageview.rect.RectActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_rect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RectActivity.class));
            }
        });
        findViewById(R.id.bt_drawable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DrawableActivity.class));
            }
        });
//        findViewById(R.id.bt_list_to_list).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO
//            }
//        });
    }
}
