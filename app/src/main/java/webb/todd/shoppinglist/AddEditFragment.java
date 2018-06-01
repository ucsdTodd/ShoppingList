package webb.todd.shoppinglist;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import webb.todd.shoppinglist.data.DatabaseDescription;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddEditFragment.AddEditFragmentListener} interface
 * to handle interaction events.
 * Use the {@link AddEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final static int ITEM_LOADER = 0;

    private TextInputLayout nameTextInput;
    private TextInputLayout detailTextInput;
    private TextInputLayout costTextInput;
    private TextInputLayout countTextInput;
    private FloatingActionButton saveFAB;

    private AddEditFragmentListener mListener;

    CoordinatorLayout coordinatorLayout;
    private Uri itemUri;
    private boolean adding = true;

    private String defaultName = null;


    public AddEditFragment() {
        // Required empty public constructor
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditFragment newInstance(String param1, String param2) {
        AddEditFragment fragment = new AddEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById( R.id.coordinatorLayout );

        nameTextInput = (TextInputLayout) view.findViewById( R.id.nameTextInput );
        if( defaultName != null ){
            nameTextInput.getEditText().setText( defaultName );
        }
        nameTextInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateSaveButtonFAB();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        detailTextInput = (TextInputLayout) view.findViewById( R.id.detailTextInput );
        costTextInput = (TextInputLayout) view.findViewById( R.id.costTextInput );
        countTextInput = (TextInputLayout) view.findViewById( R.id.countTextInput );

        saveFAB = view.findViewById ( R.id.saveFAB );
        saveFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ((InputMethodManager)getActivity().getSystemService( Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow( getView().getWindowToken(), 0);
                saveItem();
            }
        });

        return view;
    }

    void setName( final String newName ){
        defaultName = newName;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed( Uri uri ) {
        if (mListener != null) {
            mListener.onAddEditCompleted(uri);
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach(context);
        if (context instanceof AddEditFragmentListener) {
            mListener = (AddEditFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddEditFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateSaveButtonFAB() {
        EditText input = nameTextInput.getEditText();
        if( input == null || !input.getText().toString().trim().isEmpty() ){
            saveFAB.show();
        }
        else{
            saveFAB.hide();
        }

    }

    private void saveItem(){
        ContentValues values = new ContentValues();
        final String name = nameTextInput.getEditText().getText().toString();
        values.put(DatabaseDescription.Item.COLUMN_NAME, name );
        values.put(DatabaseDescription.Item.COLUMN_DETAIL, detailTextInput.getEditText().getText().toString());
        values.put(DatabaseDescription.Item.COLUMN_COST, costTextInput.getEditText().getText().toString());
        values.put(DatabaseDescription.Item.COLUMN_COUNT, countTextInput.getEditText().getText().toString());

        if( adding ){
            Uri newItemUri = getActivity().getContentResolver().insert(
                    DatabaseDescription.Item.CONTENT_URI, values );
            if( newItemUri != null ){
                // Snackbar.make( coordinatorLayout, "Item " + name +" added", Snackbar.LENGTH_LONG).show();
                mListener.onAddEditCompleted( newItemUri );
            }
        }
        else{
            int updateRow =  getActivity().getContentResolver().update(
                    itemUri, values, null, null );
            if( updateRow > 0 ){
                Snackbar.make( coordinatorLayout, "Item " + name +" updated", Snackbar.LENGTH_LONG).show();
                mListener.onAddEditCompleted( itemUri );
            }
        }
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
    public interface AddEditFragmentListener {
        // TODO: Update argument type and name
        void onAddEditCompleted( Uri itemUri );
    }
}
