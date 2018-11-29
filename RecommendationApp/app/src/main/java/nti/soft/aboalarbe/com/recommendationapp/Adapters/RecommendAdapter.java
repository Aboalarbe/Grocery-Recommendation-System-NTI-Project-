package nti.soft.aboalarbe.com.recommendationapp.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nti.soft.aboalarbe.com.recommendationapp.R;

/**
 * Created by m_abo on 11/27/2018.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {

    private Activity activity;
    private List<String> list;
    private RecyclerView mRecyclerView;

    public RecommendAdapter(Activity activity, List<String> list, RecyclerView mRecyclerView) {
        this.activity = activity;
        this.list = list;
        this.mRecyclerView = mRecyclerView;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_row, parent, false);
        RecommendAdapter.ViewHolder vh = new RecommendAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecommendAdapter.ViewHolder holder, int position) {
        holder.name.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.item_name);
        }
    }
}

