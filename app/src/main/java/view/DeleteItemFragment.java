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
 * {@link DeleteItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeleteItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteItemFragment extends BasicItemFragment {
    private ImageButton ok_button;
    private ImageButton cancel_button;
    private Collection tmpCollection;

    public DeleteItemFragment() {
        // Required empty public constructor
    }

    public static DeleteItemFragment newInstance(Collection collection) {
        DeleteItemFragment fragment = new DeleteItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(COLLECTION_PARAM, collection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete_item, container, false);

        // On ne controlle pas la création du fragment, la collection peut ne pas encore exister.
        if (collection != null) {
            tmpCollection = new Collection();
            tmpCollection.move(collection);
            super.initView(view, new DeleteItemAdapter(tmpCollection));

            ok_button = view.findViewById(R.id.ok_button);
            ok_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Copier le contenu de la collection modifié dans l'originale
                    collection.move(tmpCollection);
                    // Retour au fragment d'ajout
                    mListener.switchAdd();
                }
            });


            cancel_button = view.findViewById(R.id.cancel_button);
            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Restoration du contenu à l'original.
                    tmpCollection.move(collection);
                    mListener.switchAdd();
                }
            });
        }

        return view;
    }
}

