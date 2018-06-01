package webb.todd.shoppinglist;

import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import webb.todd.shoppinglist.MainListFragment.OnListFragmentInteractionListener;
import webb.todd.shoppinglist.data.DatabaseDescription;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a Item Uriand makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ViewHolder> {

    //private final List<Uri> mValues;
    //private final OnListFragmentInteractionListener mListener;
    private Cursor cursor = null;
    private final ItemClickListener clickListener;

    public ItemViewAdapter( ItemClickListener listener ) {
        clickListener = listener;
    }


   // public ItemViewAdapter( List<Uri> items, OnListFragmentInteractionListener listener ) {
   //     mValues = items;
  //      mListener = listener;
  //  }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( android.R.layout.simple_list_item_1, parent, false);
                //.inflate(R.layout.fragment_main_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, int position ) {
        if( position < cursor.getCount() ) {
            cursor.moveToPosition(position);
            holder.setRowID(cursor.getLong(cursor.getColumnIndex(DatabaseDescription.Item._ID)));
            final String name = cursor.getString(cursor.getColumnIndex(DatabaseDescription.Item.COLUMN_NAME));
            final String count = cursor.getString(cursor.getColumnIndex(DatabaseDescription.Item.COLUMN_COUNT));
            String cost = cursor.getString(cursor.getColumnIndex(DatabaseDescription.Item.COLUMN_COST));
            if( !cost.startsWith("$") ){
                cost = "$" + cost;
            }
            holder.textView.setText(cost + "   " + name + "\t(" + count + ")\t");
            holder.textView.setTypeface( null, Typeface.NORMAL );
        }
        else{
            // use last row for total
            float total = 0;
            for( int i = 0; i < cursor.getCount(); i++ ){
                cursor.moveToPosition( i );
                String cost = cursor.getString(cursor.getColumnIndex(DatabaseDescription.Item.COLUMN_COST));
                if(cost.startsWith("$")) {
                    cost = cost.substring(1);
                }
                try {
                    total += Float.parseFloat(cost);
                }
                catch (final Exception x){
                    // ignore for now
                }
            }
            String finalCost = Float.toString( total );
            if( finalCost.length() - finalCost.indexOf('.') > 3 ){
                finalCost = finalCost.substring( 0, finalCost.indexOf('.') +3 );
            }
            holder.textView.setText( "$" +finalCost + " Total");
            holder.textView.setTypeface( null, Typeface.BOLD );

        }

  //      holder.mItem = mValues.get(position);
//        holder.mIdView.setText( mValues.get(position).id );
//        holder.mContentView.setText( mValues.get(position).content );

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if( cursor == null || cursor.getCount() < 1){
            return 0;
        }
        return cursor.getCount() +1;
        //return mValues.size();
    }

    public void swapCursor( Cursor cursor ){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        //public final TextView mIdView;
        //public final TextView mContentView;
        public Uri mItem;
        private long rowId;

        public ViewHolder( View itemView ) {
            super( itemView );
            textView = (TextView) itemView.findViewById( android.R.id.text1 );
            itemView.setOnClickListener( new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    clickListener.onClick(DatabaseDescription.Item.buildItemUri( rowId ));
                }
            });
           // mIdView = (TextView) view.findViewById(R.id.item_number);
            //mContentView = (TextView) view.findViewById(R.id.content);
        }

        public void setRowID( long rowID ){
            this.rowId = rowID;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView.getText().toString() + "'";
        }
    }

    public interface ItemClickListener {
        void onClick( Uri itemUri );
    }
}
