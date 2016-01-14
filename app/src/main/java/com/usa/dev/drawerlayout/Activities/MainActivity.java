package com.usa.dev.drawerlayout.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.appevents.AppEventsLogger;
import com.usa.dev.drawerlayout.Helper.PreferencesManager;
import com.usa.dev.drawerlayout.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String[] mDrawerTitle;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    //private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Preferences manager
        PreferencesManager.initializeInstance(getApplicationContext());

        mDrawerTitle = getResources().getStringArray(R.array.drawer_titles);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mDrawerTitle));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    /**
     * OnResume -- Start tracking user
     */
    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    /**
     * OnPause -- Stop tracking user
     */
    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new DrawerFragment();
        Bundle args = new Bundle();
        args.putInt(DrawerFragment.ARG_OPTION_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitle[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        //getActionBar().setTitle(mTitle);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class DrawerFragment extends Fragment {
        public static final String ARG_OPTION_NUMBER = "option_number";

        public DrawerFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_option, container, false);
            int i = getArguments().getInt(ARG_OPTION_NUMBER);
            String options = getResources().getStringArray(R.array.drawer_titles)[i];

            int imageId = getResources().getIdentifier(options.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(options);
            return rootView;
        }
    }
}
