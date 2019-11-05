package view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.example.aleachoice.R;

import model.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicItemFragment extends Fragment {
    protected static final String COLLECTION_PARAM = "collection";

    protected Collection collection;
    protected RecyclerView.Adapter adapter;
    protected RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    protected OnFragmentInteractionListener mListener;

    public BasicItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            collection = (Collection) getArguments().getSerializable(COLLECTION_PARAM);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /** Initialisation de la vue avec un adapter pour la liste d'item
     *
     * @param view
     * @param adapter
     */
    protected void initView(View view, RecyclerView.Adapter adapter) {
        recyclerView = view.findViewById(R.id.item_list);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // Changement vers le fragment de suppression
        void switchDelete();
        // Changement vers le fragment d'ajout
        void switchAdd();
    }
}

