package webb.todd.shoppinglist.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ShoppingListContentProvider extends ContentProvider {

    // used to access database
    private ShoppingListDatabaseHelper dbHelper;

    private final static UriMatcher uriMatcher = new UriMatcher( UriMatcher.NO_MATCH );

    private final static int ONE_ITEM = 1;
    private final static int ITEM_LIST = 2;

    static{
        uriMatcher.addURI( DatabaseDescription.AUTHORITY, DatabaseDescription.Item.TABLE_NAME
                + "/#", ONE_ITEM);
        uriMatcher.addURI( DatabaseDescription.AUTHORITY, DatabaseDescription.Item.TABLE_NAME,
                ITEM_LIST );
    }

    @Override
    public boolean onCreate() {
        dbHelper = new ShoppingListDatabaseHelper( getContext() );
        return true;
    }

    @Nullable
    @Override
    public Cursor query( @NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder ) {
        // create SQLiteQueryBuilder for querying contacts table
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables( DatabaseDescription.Item.TABLE_NAME);
        switch (uriMatcher.match(uri) ){
           case ONE_ITEM: // contact with specified id will be selected
                queryBuilder.appendWhere(
                        DatabaseDescription.Item._ID + "=" + uri.getLastPathSegment());
                	break;
            	case ITEM_LIST: // all contacts will be selected
                	break;
            	default:
                	throw new UnsupportedOperationException( "Invalid Query" + uri );
                        	//getContext().getString(  R.string.invalid_query_uri) + uri);
                 }

      	// execute the query to select one or all contacts
        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        // configure to watch for content changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri newItemUri = null;

        switch( uriMatcher.match( uri ) ){
            case ITEM_LIST:
                long rowID = dbHelper.getWritableDatabase().insert(
                        DatabaseDescription.Item.TABLE_NAME, null, values);
                if( rowID > 0 ) {
                    newItemUri = DatabaseDescription.Item.buildItemUri(rowID);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                else{
                    throw new SQLException( "Insert failed " +uri );
                }
                break;
            default:
                throw new UnsupportedOperationException( "" +uri );
        }
        return newItemUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
