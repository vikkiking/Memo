package com.example.memo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

public class SearchResults extends AppCompatActivity {
    public SearchAdapter adapter;
    protected RecyclerView recyclerView;
    protected List<Long> multiSelections = new ArrayList<>();
    public static String EXTRA_MESSAGE_TITLE = "com.example.memo.TITLE",
            EXTRA_MESSAGE_CONTENT = "com.example.memo.CONTENT",
            EXTRA_MESSAGE_ID = "com.example.memo.ID";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LitePal.getDatabase();
        recyclerView = findViewById(R.id.recyclerViewSearch);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter(LitePal.findAll(Memo.class));
        adapter.setOnItemClickListener(new SearchAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (adapter.isMultiSelectMode()) {
                    if (multiSelections.contains(adapter.getLocalDataSet().get(position).getId())) {
                        multiSelections.remove(adapter.getLocalDataSet().get(position).getId());
                        view.setBackgroundResource(R.drawable.border);
                        if (multiSelections.size() == 0)
                            adapter.setMultiSelectMode(false);
                    } else {
                        multiSelections.add(adapter.getLocalDataSet().get(position).getId());
                        view.setBackgroundResource(R.drawable.border_selected);
                    }
                    //searchAdapter.notifyDataSetChanged();
                } else editMemo(position);
            }

            @Override
            public boolean onLongClick(View view, int position) {
                if (!adapter.isMultiSelectMode()) {
                    //Toast.makeText(MainActivity.this, "进入多选模式", Toast.LENGTH_SHORT).show();
                    view.setBackgroundResource(R.drawable.border_selected);
                    multiSelections.add(adapter.getLocalDataSet().get(position).getId());
                    adapter.setMultiSelectMode(true);
                    //searchAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void editMemo(int position) {
        Intent intent = new Intent(this, newMemo.class);
        String title = adapter.getLocalDataSet().get(position).getTitle(),
                content = adapter.getLocalDataSet().get(position).getContent();
        Long id = adapter.getLocalDataSet().get(position).getId();
        intent.putExtra(EXTRA_MESSAGE_TITLE, title);
        intent.putExtra(EXTRA_MESSAGE_CONTENT, content);
        intent.putExtra(EXTRA_MESSAGE_ID, String.valueOf(id));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconified(false);
        //searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.selectAll:
                //全选
                multiSelections.clear();
                for (Memo i : adapter.getLocalDataSet())
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
                /*adapter.setLocalDataSet(LitePal.findAll(Memo.class));
                adapter.setLocalDataSet(adapter.getLocalDataSet());*/
               /* if ((findViewById(R.id.search)) != null)
                    adapter.getFilter().filter(((SearchView) findViewById(R.id.search)).getQuery());*/
                adapter.setMultiSelectMode(false);
                adapter.notifyDataSetChanged();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
       /* if ((findViewById(R.id.search)) != null)
            adapter.getFilter().filter(((SearchView) findViewById(R.id.search)).getQuery());*/
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (adapter.isMultiSelectMode())
            adapter.setMultiSelectMode(false);
        else super.onBackPressed();
    }
}