package com.example.drawerlayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.drawerlayout.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment != null) {

            NavController navController = navHostFragment.getNavController();

            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.FirstFragment,
                    R.id.SecondFragment,
                    R.id.thirdFragment
            ).build();

            NavigationUI.setupActionBarWithNavController(
                    this,
                    navController,
                    appBarConfiguration
            );

            BottomNavigationView bottomNavigationView =
                    findViewById(R.id.bottomNav);

            NavigationUI.setupWithNavController(
                    bottomNavigationView,
                    navController
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            mostrarConfiguracoes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarConfiguracoes() {

        String[] opcoes = getResources()
                .getStringArray(R.array.settings_options);

        new AlertDialog.Builder(this)
                .setTitle(R.string.settings_title)
                .setItems(opcoes, (dialog, which) -> {

                    if (which == 0) {
                        mostrarTemas();
                    } else {
                        mostrarCores();
                    }
                })
                .show();
    }

    private void mostrarTemas() {

        String[] temas = getResources()
                .getStringArray(R.array.theme_options);

        new AlertDialog.Builder(this)
                .setTitle(R.string.choose_theme)
                .setItems(temas, (dialog, which) -> {

                    if (which == 0) {

                        AppCompatDelegate.setDefaultNightMode(
                                AppCompatDelegate.MODE_NIGHT_NO
                        );

                    } else {

                        AppCompatDelegate.setDefaultNightMode(
                                AppCompatDelegate.MODE_NIGHT_YES
                        );
                    }
                })
                .show();
    }

    private void mostrarCores() {

        String[] cores = getResources()
                .getStringArray(R.array.color_options);

        new AlertDialog.Builder(this)
                .setTitle(R.string.choose_color)
                .setItems(cores, (dialog, which) -> {

                    int cor;

                    if (which == 0) {
                        cor = R.color.instrument_blue;
                    } else if (which == 1) {
                        cor = R.color.instrument_green;
                    } else {
                        cor = R.color.instrument_orange;
                    }

                    binding.toolbar.setBackgroundColor(
                            ContextCompat.getColor(this, cor)
                    );

                    BottomNavigationView bottomNavigationView =
                            findViewById(R.id.bottomNav);

                    bottomNavigationView.setBackgroundColor(
                            ContextCompat.getColor(this, cor)
                    );
                })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {

        NavController navController =
                Navigation.findNavController(
                        this,
                        R.id.nav_host_fragment_content_main
                );

        return NavigationUI.navigateUp(
                navController,
                appBarConfiguration
        ) || super.onSupportNavigateUp();
    }
}