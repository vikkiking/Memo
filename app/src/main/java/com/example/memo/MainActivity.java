package com.example.memo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static MyAdapter adapter;
    private RecyclerView recyclerView;
    private List<Memo> data;
    private List<Long> multiSelections = new ArrayList<>();
    public static final String EXTRA_MESSAGE_TITLE = "com.example.memo.TITLE",
            EXTRA_MESSAGE_CONTENT = "com.example.memo.CONTENT",
            EXTRA_MESSAGE_ID = "com.example.memo.ID";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //创建数据库
        LitePal.getDatabase();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        data = LitePal.findAll(Memo.class);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(data);
        adapter.setOnItemClickListener(new MyAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (adapter.isMultiSelectMode()) {
                    if (multiSelections.contains(data.get(position).getId())) {
                        multiSelections.remove(data.get(position).getId());
                        view.setBackgroundResource(R.drawable.border);
                        if (multiSelections.size() == 0)
                            adapter.setMultiSelectMode(false);
                    } else {
                        multiSelections.add(data.get(position).getId());
                        view.setBackgroundResource(R.drawable.border_selected);
                    }
                    //adapter.notifyDataSetChanged();
                } else editMemo(position);
            }

            @Override
            public boolean onLongClick(View view, int position) {
                if (!adapter.isMultiSelectMode()) {
                    //Toast.makeText(MainActivity.this, "进入多选模式", Toast.LENGTH_SHORT).show();
                    view.setBackgroundResource(R.drawable.border_selected);
                    multiSelections.add(data.get(position).getId());
                    adapter.setMultiSelectMode(true);
                    //adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.index_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.selectAll:
                //全选
                multiSelections.clear();
                for (Memo i : data)
                    multiSelections.add(i.getId());
                for (int i = 0; i < recyclerView.getChildCount(); i++)
                    recyclerView.getChildAt(i).setBackgroundResource(R.drawable.border_selected);
                adapter.setMultiSelectMode(true);
                break;
            case R.id.cancelSelect:
                //取消选择
                multiSelections.clear();
                for (int i = 0; i < recyclerView.getChildCount(); i++)
                    recyclerView.getChildAt(i).setBackgroundResource(R.drawable.border);
                adapter.setMultiSelectMode(false);
                break;
            case R.id.delete:
                //删除
                for (long i : multiSelections)
                    LitePal.delete(Memo.class, i);
                data = LitePal.findAll(Memo.class);
                adapter.setLocalDataSet(data);
                adapter.setMultiSelectMode(false);
                adapter.notifyDataSetChanged();
                break;
            default:
                Toast.makeText(MainActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addMemo(View view) {
        Intent intent = new Intent(this, newMemo.class);
        startActivity(intent);
    }


    public void editMemo(int position) {
        Intent intent = new Intent(this, newMemo.class);
        String title = data.get(position).getTitle(),
                content = data.get(position).getContent();
        intent.putExtra(EXTRA_MESSAGE_TITLE, title);
        intent.putExtra(EXTRA_MESSAGE_CONTENT, content);
        intent.putExtra(EXTRA_MESSAGE_ID, String.valueOf(position + 1));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        data = LitePal.findAll(Memo.class);
        adapter.setLocalDataSet(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (adapter.isMultiSelectMode())
            adapter.setMultiSelectMode(false);
        else super.onBackPressed();
    }
}
