package webb.todd.shoppinglist.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {

    // COntent Providers name
    public final static String AUTHORITY = "webb.todd.shoppinglist.data";

    // base URI to interact with ContentProvider
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" +AUTHORITY);

    public static final class Item implements BaseColumns {
         public final static String TABLE_NAME = "items";

         public final static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath( TABLE_NAME ).build();

         public final static String COLUMN_NAME = "name";
        public final static String COLUMN_DETAIL = "detail";
        public final static String COLUMN_COST = "cost";
        public final static String COLUMN_COUNT = "count";

        public static Uri buildItemUri( long id ){
           return ContentUris.withAppendedId( CONTENT_URI, id );
        }

    }
}
