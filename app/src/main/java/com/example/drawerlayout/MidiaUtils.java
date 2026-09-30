package com.example.drawerlayout;

import android.net.Uri;
import android.widget.ImageView;

public class MidiaUtils {

    public static void carregarImagem(ImageView destino, String uri) {
        if (destino == null) {
            return;
        }

        destino.setImageResource(R.drawable.ic_instrumentos);

        if (uri == null || uri.isEmpty()) {
            return;
        }

        try {
            destino.setImageURI(Uri.parse(uri));
        } catch (Exception e) {
            destino.setImageResource(R.drawable.ic_instrumentos);
        }
    }
}