package com.example.drawerlayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.drawerlayout.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentLoginBinding.inflate(
                inflater,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel =
                new ViewModelProvider(requireActivity())
                        .get(AuthViewModel.class);

        binding.buttonLogin.setOnClickListener(v ->
                realizarLogin()
        );

        binding.buttonCadastro.setOnClickListener(v -> {
            Intent intent =
                    new Intent(
                            requireContext(),
                            CadastroActivity.class
                    );

            intent.putExtra(
                    ContratoApp.EXTRA_MODO_EDICAO,
                    false
            );

            startActivity(intent);
        });

        observarLogin();
    }

    private void realizarLogin() {
        String email =
                binding.editEmail.getText()
                        .toString();

        String senha =
                binding.editSenha.getText()
                        .toString();

        if (email.trim().isEmpty()
                || senha.isEmpty()) {

            Toast.makeText(
                    requireContext(),
                    R.string.auth_campos_obrigatorios,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        authViewModel.login(
                email,
                senha
        );
    }

    private void observarLogin() {
        authViewModel.getEstadoLogin().observe(
                getViewLifecycleOwner(),
                estado -> {
                    if (estado == null) {
                        return;
                    }

                    switch (estado.status) {
                        case OCIOSO:
                            mostrarCarregamento(false);
                            break;

                        case PROCESSANDO:
                            mostrarCarregamento(true);
                            break;

                        case SUCESSO:
                            mostrarCarregamento(false);

                            authViewModel.limparEstadoLogin();
                            break;

                        case ERRO:
                            mostrarCarregamento(false);

                            Toast.makeText(
                                    requireContext(),
                                    estado.mensagem,
                                    Toast.LENGTH_SHORT
                            ).show();

                            authViewModel.limparEstadoLogin();
                            break;
                    }
                }
        );
    }

    private void mostrarCarregamento(boolean carregando) {
        binding.progressLogin.setVisibility(
                carregando
                        ? View.VISIBLE
                        : View.GONE
        );

        binding.buttonLogin.setEnabled(!carregando);
        binding.buttonCadastro.setEnabled(!carregando);
        binding.editEmail.setEnabled(!carregando);
        binding.editSenha.setEnabled(!carregando);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}