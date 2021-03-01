package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity {
    public static MyAdapter adapter;
    private RecyclerView recyclerView;
    private List<Memo> data;
    private List<Long> multiSelections = new ArrayList<>();
    public static final String EXTRA_MESSAGE_TITLE = "com.example.memo.TITLE",
            EXTRA_MESSAGE_CONTENT = "com.example.memo.CONTENT",
            EXTRA_MESSAGE_ID = "com.example.memo.ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                Intent intent = new Intent(this, SearchResults.class);
                startActivity(intent);
                break;
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
                Toast.makeText(SearchResults.this, "Something is wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}