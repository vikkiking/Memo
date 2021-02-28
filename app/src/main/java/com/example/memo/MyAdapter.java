package com.example.memo;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Memo[] localDataSet;
    private onItemClickListener listener;
    private boolean multiSelectMode;

    public void setMultiSelectMode(boolean multiSelectMode) {
        this.multiSelectMode = multiSelectMode;
    }

    public boolean isMultiSelectMode() {
        return multiSelectMode;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, content;
        private Long id;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            title = (TextView) view.findViewById(R.id.memoTitle);
            content = (TextView) view.findViewById(R.id.memoContent);
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public MyAdapter(List<Memo> dataSet) {
        localDataSet = dataSet.toArray(new Memo[0]);
    }

    public void setLocalDataSet(List<Memo> dataSet) {
        localDataSet = dataSet.toArray(new Memo[0]);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }


    public interface onItemClickListener {
        void onItemClick(View view, int position);

        boolean onLongClick(View view, int position);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.title.setText(localDataSet[position].getTitle());
        viewHolder.content.setText(localDataSet[position].getContent());
        viewHolder.id=localDataSet[position].getId();
        // if (!isMultiSelectMode())
        viewHolder.itemView.setBackgroundResource(R.drawable.border);
        // else viewHolder.itemView.setBackgroundResource(R.drawable.border_selected);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, position);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return listener.onLongClick(view, position);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
