package net.kiwigeeks.spotify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

        // MenuItem searchItem = menu.findItem(R.id.action_search);
        // SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // Configure the search info and add any event listeners
        // return true;

        // handleSearchView(searchView);

        //     Get the action view by calling getActionView() on the corresponding MenuItem:
        //  menu.findItem(R.id.action_search).getActionView();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}