package com.example.drawerlayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class FotoUtils {

    private static final int TAMANHO_MAXIMO = 512;

    public static final int TAMANHO_MAXIMO_BYTES = 200 * 1024;

    private static final int QUALIDADE_INICIAL_JPEG = 90;
    private static final int REDUCAO_QUALIDADE_JPEG = 10;
    private static final int QUALIDADE_MINIMA_JPEG = 40;

    private FotoUtils() {}

    public static byte[] lerFotoCompactada(String caminho) throws IOException {
        BitmapFactory.Options opcoes = new BitmapFactory.Options();
        opcoes.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(caminho, opcoes);

        opcoes.inSampleSize = calcularAmostragem(opcoes.outWidth, opcoes.outHeight);

        opcoes.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(caminho, opcoes);

        if (bitmap == null) {
            throw new IOException("Não foi possível carregar a foto.");
        }

        bitmap = corrigirOrientacao(bitmap, caminho);
        bitmap = redimensionar(bitmap);

        byte[] resultado = compactar(bitmap);

        bitmap.recycle();

        return resultado;
    }

    public static Bitmap decodificar(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private static int calcularAmostragem(int largura, int altura) {
        int amostragem = 1;

        while (largura / amostragem > TAMANHO_MAXIMO * 2
                || altura / amostragem > TAMANHO_MAXIMO * 2) {
            amostragem *= 2;
        }

        return amostragem;
    }

    private static Bitmap redimensionar(Bitmap bitmap) {
        int largura = bitmap.getWidth();
        int altura = bitmap.getHeight();

        if (largura <= TAMANHO_MAXIMO && altura <= TAMANHO_MAXIMO) {
            return bitmap;
        }

        float escala = Math.min((float) TAMANHO_MAXIMO / largura, (float) TAMANHO_MAXIMO / altura);

        int novaLargura = Math.round(largura * escala);
        int novaAltura = Math.round(altura * escala);

        Bitmap redimensionado = Bitmap.createScaledBitmap(bitmap, novaLargura, novaAltura, true);

        if (redimensionado != bitmap) {
            bitmap.recycle();
        }

        return redimensionado;
    }

    private static Bitmap corrigirOrientacao(Bitmap bitmap, String caminho) throws IOException {

        ExifInterface exif = new ExifInterface(caminho);

        int orientacao =
                exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        float graus;

        switch (orientacao) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                graus = 90;
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                graus = 180;
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                graus = 270;
                break;

            default:
                return bitmap;
        }

        Matrix matriz = new Matrix();
        matriz.postRotate(graus);

        Bitmap corrigido =
                Bitmap.createBitmap(
                        bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matriz, true);

        if (corrigido != bitmap) {
            bitmap.recycle();
        }

        return corrigido;
    }

    private static byte[] compactar(Bitmap bitmap) {
        int qualidade = QUALIDADE_INICIAL_JPEG;
        byte[] bytes;

        do {
            ByteArrayOutputStream saida = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, qualidade, saida);

            bytes = saida.toByteArray();
            qualidade -= REDUCAO_QUALIDADE_JPEG;

        } while (bytes.length > TAMANHO_MAXIMO_BYTES && qualidade >= QUALIDADE_MINIMA_JPEG);

        return bytes;
    }
}
