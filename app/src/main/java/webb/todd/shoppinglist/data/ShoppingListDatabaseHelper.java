package webb.todd.shoppinglist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoppingListDatabaseHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "ShoppingList.db";
    private final static int DATABASE_VERSION = 1;

    ShoppingListDatabaseHelper(Context context){
         super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // SQL fore creatig table
        final String CREATE_ITEM_TABLE = "CREATE TABLE " + DatabaseDescription.Item.TABLE_NAME
                + "(" + DatabaseDescription.Item._ID + " integer primary key, "
                + DatabaseDescription.Item.COLUMN_NAME + " TEXT, "
                + DatabaseDescription.Item.COLUMN_DETAIL + " TEXT, "
                + DatabaseDescription.Item.COLUMN_COST + " TEXT, "
                + DatabaseDescription.Item.COLUMN_COUNT + " TEXT);";
        sqLiteDatabase.execSQL( CREATE_ITEM_TABLE );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
       // no op for now
    }
}
