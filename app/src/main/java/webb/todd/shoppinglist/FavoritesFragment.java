package webb.todd.shoppinglist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import webb.todd.shoppinglist.data.DatabaseDescription;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFavoriteSelectedListener} interface
 * to handle interaction events.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFavoriteSelectedListener mListener;

    private final static String[] COMMON_FAVORITES = new String[]{
            "apples", "bananas", "corn", "eggs", "milk", "mango",
            "potatoes", "raspberries", "tomatoes", "yogurt"
    } ;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        RecyclerView rView = (RecyclerView) view.findViewById( R.id.favorites );
        rView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        rView.setAdapter( new FavoritesListAdapter() );
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoriteSelectedListener) {
            mListener = (OnFavoriteSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFavoriteSelectedListener");
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
    public interface OnFavoriteSelectedListener {
        void onFavoriteSelected( String favoriteName );
    }

    public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

        @Override
        public FavoritesViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
            View view = LayoutInflater.from( parent.getContext())
                    .inflate( android.R.layout.simple_list_item_1, parent, false);
            //.inflate(R.layout.fragment_main_list, parent, false);
            return new FavoritesViewHolder(view );
        }

        @Override
        public void onBindViewHolder( FavoritesViewHolder holder, int position ) {
            holder.textView.setText( COMMON_FAVORITES[position] );
        }

        @Override
        public int getItemCount() {
            return COMMON_FAVORITES.length;
        }
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public FavoritesViewHolder( View itemView ) {
            super( itemView );
            textView = (TextView) itemView.findViewById( android.R.id.text1 );
            itemView.setOnClickListener( new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if( mListener != null ) {
                        mListener.onFavoriteSelected( textView.getText().toString() );
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView.getText().toString() + "'";
        }
    }
}
