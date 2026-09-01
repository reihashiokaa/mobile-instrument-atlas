package com.example.drawerlayout;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.drawerlayout.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.FirstFragment, R.id.SecondFragment, R.id.thirdFragment)
                    .setOpenableLayout(drawer)
                    .build();
            NavigationUI.setupActionBarWithNavController(
                    this, navController, appBarConfiguration);

            BottomNavigationView bottomNavigationView =
                    findViewById(R.id.bottomNav);

            NavigationUI.setupWithNavController(
                    bottomNavigationView,
                    navController
            );
            NavigationUI.setupWithNavController(
                    navigationView, navController);
        }


    }

    private void mostrarConfiguracoes() {

        String[] opcoes = {"Tema", "Cor do aplicativo"};

        new AlertDialog.Builder(this)
                .setTitle("Configurações")
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

        String[] temas = {"Modo Claro", "Modo Noturno"};

        new AlertDialog.Builder(this)
                .setTitle("Escolha o tema")
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

        String[] cores = {"Azul", "Verde", "Laranja"};

        new AlertDialog.Builder(this)
                .setTitle("Escolha a cor")
                .setItems(cores, (dialog, which) -> {

                    int cor;

                    if (which == 0) {
                        cor = R.color.instrument_blue;
                    } else if (which == 1) {
                        cor = R.color.instrument_green;
                    } else {
                        cor = R.color.instrument_orange;
                    }

                    // altera elementos visuais da tela principal
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

        if (id == R.id.action_settings) {
            mostrarConfiguracoes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}