package farmerline.com.dev.farmerserviceapp.dashboard.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmerline.com.dev.farmerserviceapp.R;
import farmerline.com.dev.farmerserviceapp.dashboard.home.baseFragment.BaseFragment;
import farmerline.com.dev.farmerserviceapp.root.App;
import farmerline.com.dev.farmerserviceapp.utils.Constant;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,HomeMVP.View {

    @Inject
    HomePresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ((App) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this);

        presenter.setView(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        presenter.getDefaultFragment();
        setTitle("Agent Overview");
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            presenter.removeFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setFragement(BaseFragment fragment) {
        fragment.attachPresenter(presenter);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.constraintLayout,fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    @Override
    public void removeFragment() {

        int numOfFrag= getSupportFragmentManager().getFragments().size();
        if (numOfFrag>1){
            getSupportFragmentManager().popBackStack();
            /*getSupportFragmentManager().addOnBackStackChangedListener(
                    new FragmentManager.OnBackStackChangedListener() {
                        public void onBackStackChanged() {
                            // Update your UI here.
                            Log.d(Constant.TAG,"On back pressed");
                        }
                    });*/
        }else{
            finish();
        }

    }


}
