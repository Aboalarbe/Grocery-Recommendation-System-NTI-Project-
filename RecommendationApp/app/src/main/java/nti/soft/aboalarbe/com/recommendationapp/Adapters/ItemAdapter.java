package nti.soft.aboalarbe.com.recommendationapp.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nti.soft.aboalarbe.com.recommendationapp.R;
import nti.soft.aboalarbe.com.recommendationapp.UI.RecommendationItems;

/**
 * Created by m_abo on 11/26/2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Activity activity;
    private List<String> list;
    private RecyclerView mRecyclerView;

    public ItemAdapter(Activity activity, List<String> list, RecyclerView mRecyclerView) {
        this.activity = activity;
        this.list = list;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_row, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                String itemName = list.get(itemPosition);
                Intent intent = new Intent(activity, RecommendationItems.class);
                intent.putExtra("selectedItem", itemName);
                activity.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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
