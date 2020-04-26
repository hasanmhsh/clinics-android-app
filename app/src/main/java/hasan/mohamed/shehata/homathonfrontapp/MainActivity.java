package hasan.mohamed.shehata.homathonfrontapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import hasan.mohamed.shehata.homathonfrontapp.databinding.ActivityMainBinding;
import hasan.mohamed.shehata.homathonfrontapp.internet.APIClient;
import hasan.mohamed.shehata.homathonfrontapp.internet.APIInterface;
import hasan.mohamed.shehata.homathonfrontapp.pojo.Clinic;
import hasan.mohamed.shehata.homathonfrontapp.ui.ClinicTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mDataBinding;


    @Override
    protected void onPostResume() {
        super.onPostResume();
        forceRTLIfSupported();
//        getActionBar().getCustomView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.



        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mDataBinding.fab.hide();
        mDataBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentManager fm = getSupportFragmentManager();
                    ClinicTemplate addQuestionDialogFragment = ClinicTemplate.newInstance(null,true);
                    addQuestionDialogFragment.show(fm, "New clinic");
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
//        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch(menuItem.getItemId()){
//
//                    case R.id.navigation_dashboard:{
//                        mDataBinding.fab.show();
//                    }
//                    break;
//                    case R.id.navigation_home:
//                    default:{
//                        mDataBinding.fab.hide();
//                    }
//                    break;
//                }
//                return true;
//            }
//        });


    }

    public void showFab(){
        mDataBinding.fab.show();
    }

    public void hideFab(){
        mDataBinding.fab.hide();
    }

}
