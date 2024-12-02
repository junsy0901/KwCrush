package com.cookandroid.candycrush;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView; // ImageView로 원래대로

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.candycrush.R;
import com.cookandroid.candycrush.util.SQLiteHelper;
import com.cookandroid.candycrush.view.PlayActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView playButton; // ImageView로 원래대로

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SQLiteHelper dbHelper = new SQLiteHelper(this);
        //dbHelper.clearDatabase();

        playButton = findViewById(R.id.playBtn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play 버튼 클릭 시 PlayActivity로 이동
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
    }
}
