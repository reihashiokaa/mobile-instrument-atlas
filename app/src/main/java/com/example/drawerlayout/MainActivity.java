package com.example.drawerlayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.drawerlayout.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    private AuthViewModel authViewModel;

    private ImageView imageAvatarToolbar;
    private TextView textNomeToolbar;
    private View usuarioToolbar;

    private Menu menuPrincipal;

    private boolean autenticado;
    private boolean reiniciandoParaLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        imageAvatarToolbar = binding.toolbar.findViewById(R.id.imageAvatarToolbar);

        textNomeToolbar = binding.toolbar.findViewById(R.id.textNomeToolbar);

        usuarioToolbar = binding.toolbar.findViewById(R.id.usuarioToolbar);

        NavHostFragment navHostFragment =
                (NavHostFragment)
                        getSupportFragmentManager()
                                .findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment == null) {
            return;
        }

        navController = navHostFragment.getNavController();

        appBarConfiguration =
                new AppBarConfiguration.Builder(
                                R.id.FirstFragment, R.id.SecondFragment, R.id.thirdFragment)
                        .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        bottomNavigationView = findViewById(R.id.bottomNav);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        observarSessao();
        observarLogout();
        protegerDestinos();
    }

    private void observarSessao() {
        authViewModel
                .getUsuarioAtivo()
                .observe(
                        this,
                        usuario -> {
                            if (usuario == null) {
                                autenticado = false;

                                mostrarInterfaceLogin();
                                atualizarMenu();

                                if (navController.getCurrentDestination() != null
                                        && navController.getCurrentDestination().getId()
                                                != R.id.loginFragment) {

                                    reiniciarNoLogin();
                                }

                                return;
                            }

                            autenticado = true;

                            mostrarInterfaceAutenticada();
                            atualizarToolbar(usuario);
                            atualizarMenu();

                            if (navController.getCurrentDestination() != null
                                    && navController.getCurrentDestination().getId()
                                            == R.id.loginFragment) {

                                navController.navigate(R.id.FirstFragment);
                            }
                        });
    }

    private void observarLogout() {
        authViewModel
                .getEstadoLogout()
                .observe(
                        this,
                        estado -> {
                            if (estado == null) {
                                return;
                            }

                            switch (estado.status) {
                                case SUCESSO:
                                    autenticado = false;

                                    mostrarInterfaceLogin();
                                    atualizarMenu();

                                    authViewModel.limparEstadoLogout();

                                    reiniciarNoLogin();
                                    break;

                                case ERRO:
                                    Toast.makeText(this, estado.mensagem, Toast.LENGTH_SHORT)
                                            .show();

                                    authViewModel.limparEstadoLogout();
                                    break;

                                default:
                                    break;
                            }
                        });
    }

    private void protegerDestinos() {
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> {
                    if (!autenticado && destination.getId() != R.id.loginFragment) {

                        reiniciarNoLogin();
                    }
                });
    }

    private void reiniciarNoLogin() {
        if (reiniciandoParaLogin) {
            return;
        }

        reiniciandoParaLogin = true;

        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        finish();
    }

    private void mostrarInterfaceLogin() {
        bottomNavigationView.setVisibility(View.GONE);

        binding.toolbar.setVisibility(View.GONE);

        usuarioToolbar.setVisibility(View.GONE);
    }

    private void mostrarInterfaceAutenticada() {
        bottomNavigationView.setVisibility(View.VISIBLE);

        binding.toolbar.setVisibility(View.VISIBLE);

        usuarioToolbar.setVisibility(View.VISIBLE);
    }

    private void atualizarToolbar(Usuario usuario) {
        textNomeToolbar.setText(usuario.nome);

        Bitmap foto = FotoUtils.decodificar(usuario.foto);

        if (foto != null) {
            imageAvatarToolbar.setImageBitmap(foto);
        } else {
            imageAvatarToolbar.setImageResource(R.mipmap.ic_launcher);
        }
    }

    private void abrirEdicaoPerfil() {
        Intent intent = new Intent(this, CadastroActivity.class);

        intent.putExtra(ContratoApp.EXTRA_MODO_EDICAO, true);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuPrincipal = menu;

        atualizarMenu();

        return true;
    }

    private void atualizarMenu() {
        if (menuPrincipal == null) {
            return;
        }

        MenuItem editar = menuPrincipal.findItem(R.id.action_edit_profile);

        MenuItem sair = menuPrincipal.findItem(R.id.action_logout);

        MenuItem configuracoes = menuPrincipal.findItem(R.id.action_settings);

        editar.setVisible(autenticado);
        sair.setVisible(autenticado);
        configuracoes.setVisible(autenticado);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit_profile) {
            abrirEdicaoPerfil();
            return true;
        }

        if (id == R.id.action_logout) {
            authViewModel.logout();
            return true;
        }

        if (id == R.id.action_settings) {
            mostrarConfiguracoes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarConfiguracoes() {
        String[] opcoes = getResources().getStringArray(R.array.settings_options);

        new AlertDialog.Builder(this)
                .setTitle(R.string.settings_title)
                .setItems(
                        opcoes,
                        (dialog, which) -> {
                            if (which == 0) {
                                mostrarTemas();
                            } else {
                                mostrarCores();
                            }
                        })
                .show();
    }

    private void mostrarTemas() {
        String[] temas = getResources().getStringArray(R.array.theme_options);

        new AlertDialog.Builder(this)
                .setTitle(R.string.choose_theme)
                .setItems(
                        temas,
                        (dialog, which) -> {
                            if (which == 0) {
                                AppCompatDelegate.setDefaultNightMode(
                                        AppCompatDelegate.MODE_NIGHT_NO);
                            } else {
                                AppCompatDelegate.setDefaultNightMode(
                                        AppCompatDelegate.MODE_NIGHT_YES);
                            }
                        })
                .show();
    }

    private void mostrarCores() {
        String[] cores = getResources().getStringArray(R.array.color_options);

        new AlertDialog.Builder(this)
                .setTitle(R.string.choose_color)
                .setItems(
                        cores,
                        (dialog, which) -> {
                            int cor;

                            if (which == 0) {
                                cor = R.color.instrument_blue;
                            } else if (which == 1) {
                                cor = R.color.instrument_green;
                            } else {
                                cor = R.color.instrument_orange;
                            }

                            binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, cor));

                            bottomNavigationView.setBackgroundColor(
                                    ContextCompat.getColor(this, cor));
                        })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController controller =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        return NavigationUI.navigateUp(controller, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
