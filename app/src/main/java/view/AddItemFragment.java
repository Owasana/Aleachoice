package view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.aleachoice.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import model.Collection;
import model.Item;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends BasicItemFragment {
    private TextView add_text;
    private FloatingActionButton add_button;
    private ImageButton delete_button;
    private Dialog dialog;

    public AddItemFragment() {
        // Required empty public constructor
    }

    public static AddItemFragment newInstance(Collection collection) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(COLLECTION_PARAM, collection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        // On ne controlle pas la création du fragment, la collection peut ne pas encore exister.
        if (collection != null) {
            super.initView(view, new BasicItemAdapter(collection));


            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            //LayoutInflater inflater = getLayoutInflater(); TODO

            View dialogView = inflater.inflate(R.layout.add_dialog, null);
            add_text = dialogView.findViewById(R.id.add_text);

            builder.setView(dialogView)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Item item = new Item(add_text.getText().toString());
                            collection.addItem(item);
                        }
                    });

            dialog = builder.create();

            add_button = view.findViewById(R.id.add_button);
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });


            delete_button = view.findViewById(R.id.delete_button);
            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.switchDelete();
                }
            });
        }

        return view;
    }
}
