package webb.todd.shoppinglist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import webb.todd.shoppinglist.data.DatabaseDescription;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MainListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LIST_LOADER = 0;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private ItemViewAdapter itemViewAdapter;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MainListFragment newInstance( int columnCount ) {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        // Set the adapter
        RecyclerView rView;
        if (view instanceof RecyclerView) {
            rView = (RecyclerView) view;
        }
        else {
            rView = view.findViewById(R.id.list);
        }
        if (mColumnCount <= 1) {
            rView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        } else {
            rView.setLayoutManager(new GridLayoutManager(view.getContext(), mColumnCount));
        }
        itemViewAdapter = new ItemViewAdapter(new ItemViewAdapter.ItemClickListener() {
            @Override
            public void onClick(Uri itemUri) {
                mListener.onItemSelected( itemUri );
            }
        });
        rView.setAdapter(itemViewAdapter);

        FloatingActionButton addFAB = view.findViewById ( R.id.addFAB );
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's an ADD Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mListener.onAddItem( null );
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader( LIST_LOADER, null, this );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateList(){
        itemViewAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if( id == LIST_LOADER ){
            return new CursorLoader( getActivity(),
                    DatabaseDescription.Item.CONTENT_URI,
                    null,
                    null,
                    null,
                    DatabaseDescription.Item.COLUMN_NAME + " COLLATE NOCASE ASC" );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        itemViewAdapter.swapCursor( data );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        itemViewAdapter.swapCursor( null );
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction( Uri itemUri );

        void onItemSelected( Uri itemUri );

        void onAddItem(Uri itemUri );
    }
}
