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

/// Specialisation de l'affichage des items avec la suppression lorsque l'on clique
public class DeleteItemAdapter extends BasicItemAdapter {
    public DeleteItemAdapter(Collection collection) {
        super(collection);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_collection.remove(position);
            }
        });
    }
}
