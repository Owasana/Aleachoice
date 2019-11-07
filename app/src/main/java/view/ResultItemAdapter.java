package view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aleachoice.R;

import model.Collection;

public class ResultItemAdapter extends BasicItemAdapter {

    public ResultItemAdapter(Collection collection){
        super(collection);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_row_layout, parent, false);

        return new ViewHolder(itemView);
    }

}
