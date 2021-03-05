package com.example.memo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;

public class newMemo extends AppCompatActivity {
    private long id = -1;
    String tag = "hello";
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        sdf.applyPattern("yyyy-MM-dd");
        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_TITLE),
                content = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_CONTENT),
                ID = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_ID);

        if (ID != null) {
            id = Long.parseLong(ID);
            EditText editTitle = findViewById(R.id.editTitle),
                    editContent = findViewById(R.id.editContent);
            editTitle.setText(title);
            editContent.setText(content);
        }
    }

    /*  @Override
      public boolean onCreateOptionsMenu(Menu menu) {
          getMenuInflater().inflate(R.menu.complete, menu);
          return true;
      }*/
    @SuppressLint("ShowToast")
    @Override
    public void onBackPressed() {
        String title = ((EditText) findViewById(R.id.editTitle)).getText().toString(),
                content = ((EditText) findViewById(R.id.editContent)).getText().toString();
        Toast toast;
        if (title.equals("") && content.equals("")) {
            if (id != -1)
                LitePal.delete(Memo.class, id);
            toast = Toast.makeText(getApplicationContext(), "舍弃空白记事", Toast.LENGTH_SHORT);
        } else {
            Memo memo = new Memo();
            memo.setDate(sdf.format(new Date()));
            memo.setTitle(title);
            memo.setContent(content);
            if (id != -1) {
                memo.update(id);
                // Log.i(tag, LitePal.find(Memo.class, id).getContent());
                toast = Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT);
            } else {
                memo.save();
                toast = Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT);
            }
        }
        toast.show();
        super.onBackPressed();
    }
}