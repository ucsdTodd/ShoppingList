package webb.todd.shoppinglist;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import webb.todd.shoppinglist.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements MainListFragment.OnListFragmentInteractionListener,
        AddEditFragment.AddEditFragmentListener, FavoritesFragment.OnFavoriteSelectedListener {

    public final static String ITEM_URI = "item_uri";

    public MainListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        //setSupportActionBar( toolbar );

//        if( savedInstanceState != null && findViewById( R.id.fragmentContainer ) != null ){
//            listFragment = new MainListFragment();
//
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.add( R.id.fragmentContainer, listFragment );
//            transaction.commit();
//        }
//        else{
            //listFragment = (MainListFragment) getSupportFragmentManager().findFragmentById( R.id.list) ;
        listFragment = new MainListFragment();
         FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
         transaction.add( R.id.fragmentContainer, listFragment );
         transaction.commit();
        }
//    }

    @Override
    public void onListFragmentInteraction( Uri itemUri ) {

    }

    @Override
    public void onItemSelected(Uri itemUri) {

    }

    @Override
    public void onAddItem(Uri itemUri) {
        if( findViewById( R.id.fragmentContainer) != null ){
            displayAddEditFragment( R.id.fragmentContainer, null );
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onAddEditCompleted( Uri itemUri ) {
        getSupportFragmentManager().popBackStack();
        listFragment.updateList();
    }

    private void displayAddEditFragment( int viewID, Uri itemUri ){
        AddEditFragment addEditFragment = new AddEditFragment();

        if( itemUri != null ){
            Bundle arguments = new Bundle();
            arguments.putParcelable( ITEM_URI, itemUri );
            addEditFragment.setArguments( arguments );
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace( viewID, addEditFragment );
        transaction.addToBackStack( null );
        transaction.commit();
    }

    private void displayFavoritesFragment(){
        if( findViewById( R.id.fragmentContainer) != null ){
            FavoritesFragment favFragment = new FavoritesFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace( R.id.fragmentContainer, favFragment );
            transaction.addToBackStack( null );
            transaction.commit();
        }

    }

    @Override
    public void onFavoriteSelected(String favoriteName) {
        if( findViewById( R.id.fragmentContainer) != null ){
            AddEditFragment addEditFragment = new AddEditFragment();
            addEditFragment.setName( favoriteName );

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace( R.id.fragmentContainer, addEditFragment );
            //transaction.addToBackStack( null );
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        boolean success = super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.menu, menu );
        menu.add( "Hi" );
        return success;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        if( item.getItemId() == R.id.app_bar_search ) {
            displayFavoritesFragment();
        }
        else{
            Toast.makeText(getApplicationContext(), "Hi There!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
