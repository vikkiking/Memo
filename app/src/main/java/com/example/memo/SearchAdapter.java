package com.example.memo;

import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

class SearchAdapter extends MyAdapter implements Filterable {
    public TempFilter myFilter;
    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public SearchAdapter(List<Memo> dataSet) {
        super(dataSet);
    }

    @Override
    public Filter getFilter() {
        if (myFilter == null)
            myFilter = new TempFilter();
        return myFilter;
    }

    class TempFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            localDataSet=LitePal.findAll(Memo.class);
            String q = charSequence.toString().toLowerCase();
            if (!q.isEmpty()) {
                List<Memo> filteredList = new ArrayList<>();
                for (Memo i : localDataSet)
                    if (i.getTitle().toLowerCase().contains(q) || i.getContent().toLowerCase().contains(q))
                        filteredList.add(i);
                localDataSet=filteredList;
            } //else localDataSet = localDataSet;
            Log.i("hello: ",getLocalDataSet().get(0).getTitle());
            FilterResults filterResults = new FilterResults();
            filterResults.values = getLocalDataSet();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notifyDataSetChanged();
        }
    }
}
