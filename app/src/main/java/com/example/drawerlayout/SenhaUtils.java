package com.example.drawerlayout;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class SenhaUtils {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final String PREFIXO = "pbkdf2_sha256";

    private static final int ITERACOES = 600_000;
    private static final int TAMANHO_SALT = 16;
    private static final int TAMANHO_HASH_BITS = 256;

    private SenhaUtils() {
    }

    public static String gerarHash(String senha) {
        if (senha == null) {
            throw new IllegalArgumentException("Senha não pode ser nula.");
        }

        try {
            byte[] salt = new byte[TAMANHO_SALT];
            new SecureRandom().nextBytes(salt);

            byte[] hash = derivar(
                    senha,
                    salt,
                    ITERACOES
            );

            return PREFIXO
                    + "$" + ITERACOES
                    + "$" + Base64.encodeToString(salt, Base64.NO_WRAP)
                    + "$" + Base64.encodeToString(hash, Base64.NO_WRAP);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Não foi possível proteger a senha.",
                    e
            );
        }
    }

    public static boolean verificar(
            String senha,
            String hashArmazenado
    ) {
        if (senha == null || hashArmazenado == null) {
            return false;
        }

        try {
            String[] partes = hashArmazenado.split("\\$");

            if (partes.length != 4) {
                return false;
            }

            if (!PREFIXO.equals(partes[0])) {
                return false;
            }

            int iteracoes = Integer.parseInt(partes[1]);

            byte[] salt = Base64.decode(
                    partes[2],
                    Base64.NO_WRAP
            );

            byte[] hashEsperado = Base64.decode(
                    partes[3],
                    Base64.NO_WRAP
            );

            byte[] hashCalculado = derivar(
                    senha,
                    salt,
                    iteracoes
            );

            return MessageDigest.isEqual(
                    hashEsperado,
                    hashCalculado
            );

        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] derivar(
            String senha,
            byte[] salt,
            int iteracoes
    ) throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
                senha.toCharArray(),
                salt,
                iteracoes,
                TAMANHO_HASH_BITS
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(ALGORITMO);

            return factory.generateSecret(spec).getEncoded();

        } finally {
            spec.clearPassword();
        }
    }
}