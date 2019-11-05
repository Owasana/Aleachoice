package view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aleachoice.R;

import java.util.Observable;
import java.util.Observer;

import model.Collection;
import model.Item;

public class BasicItemAdapter extends RecyclerView.Adapter<BasicItemAdapter.ViewHolder> implements Observer {
    private Collection m_collection;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = itemView.findViewById(R.id.item_name);
        }
    }

    public BasicItemAdapter(Collection collection) {
        m_collection = collection;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = m_collection.item(position);
        holder.textView.setText(item.name());
        holder.textView.setTextColor(item.color());
    }

    @Override
    public int getItemCount() {
        return m_collection.size();
    }

    @Override
    public void update(Observable obs, Object arg)
    {
        notifyDataSetChanged();
    }
}
