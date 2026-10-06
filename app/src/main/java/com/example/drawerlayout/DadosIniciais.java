package com.example.drawerlayout;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public final class DadosIniciais {

    private DadosIniciais() {}

    public static void popular(Context context, AppDatabase banco) {
        banco.runInTransaction(
                () -> {
                    banco.familiaDao().inserir(criarFamilias());

                    banco.instrumentoDao().inserir(criarInstrumentos(context));
                });
    }

    private static List<Familia> criarFamilias() {
        List<Familia> familias = new ArrayList<>();

        familias.add(criarFamilia(1L, "Corda", 1));

        familias.add(criarFamilia(2L, "Sopro", 2));

        familias.add(criarFamilia(3L, "Percussão", 3));

        return familias;
    }

    private static Familia criarFamilia(long id, String nome, int ordem) {
        Familia familia = new Familia();

        familia.id = id;
        familia.nome = nome;
        familia.ordem = ordem;

        return familia;
    }

    private static List<Instrumento> criarInstrumentos(Context context) {
        List<Instrumento> instrumentos = new ArrayList<>();

        instrumentos.add(criarInstrumento(context, 1L, 1L, "violino", false, 1, true));

        instrumentos.add(criarInstrumento(context, 2L, 1L, "violao", false, 2, true));

        instrumentos.add(criarInstrumento(context, 3L, 1L, "harpa", false, 3, true));

        instrumentos.add(criarInstrumento(context, 4L, 2L, "flauta", false, 1, true));

        instrumentos.add(criarInstrumento(context, 5L, 2L, "clarinete", false, 2, true));

        instrumentos.add(criarInstrumento(context, 6L, 2L, "trompete", false, 3, true));

        instrumentos.add(criarInstrumento(context, 7L, 3L, "bateria", false, 1, true));

        instrumentos.add(criarInstrumento(context, 8L, 3L, "tambor", false, 2, true));

        instrumentos.add(criarInstrumento(context, 9L, 3L, "pandeiro", false, 3, true));

        instrumentos.add(criarInstrumento(context, 10L, 1L, "violino_eletrico", true, 1, false));

        instrumentos.add(criarInstrumento(context, 11L, 1L, "violao_12_cordas", true, 2, false));

        instrumentos.add(criarInstrumento(context, 12L, 1L, "harpa_celta", true, 3, false));

        instrumentos.add(criarInstrumento(context, 13L, 2L, "flauta_piccolo", true, 1, false));

        instrumentos.add(criarInstrumento(context, 14L, 2L, "clarinete_baixo", true, 2, false));

        instrumentos.add(criarInstrumento(context, 15L, 2L, "trompete_piccolo", true, 3, false));

        instrumentos.add(criarInstrumento(context, 16L, 3L, "bateria_eletronica", true, 1, false));

        instrumentos.add(criarInstrumento(context, 17L, 3L, "tambor_africano", true, 2, false));

        instrumentos.add(criarInstrumento(context, 18L, 3L, "pandeiro_meia_lua", true, 3, false));

        return instrumentos;
    }

    private static Instrumento criarInstrumento(
            Context context,
            long id,
            long familiaId,
            String recursoBase,
            boolean variacao,
            int ordem,
            boolean possuiAudio) {
        Instrumento instrumento = new Instrumento();

        instrumento.id = id;
        instrumento.familiaId = familiaId;

        instrumento.nome = obterTexto(context, recursoBase + "_nome");

        instrumento.descricao = obterTexto(context, recursoBase + "_descricao");

        instrumento.detalhes = obterTexto(context, recursoBase + "_detalhes");

        instrumento.imagemUri = criarUri(context, "drawable", recursoBase);

        if (possuiAudio) {
            instrumento.audioUri = criarUri(context, "raw", recursoBase);
        } else {
            instrumento.audioUri = null;
        }

        instrumento.variacao = variacao;
        instrumento.ordem = ordem;

        return instrumento;
    }

    private static String obterTexto(Context context, String nomeRecurso) {
        int recursoId =
                context.getResources()
                        .getIdentifier(nomeRecurso, "string", context.getPackageName());

        if (recursoId == 0) {
            throw new IllegalStateException("String não encontrada: " + nomeRecurso);
        }

        return context.getString(recursoId);
    }

    private static String criarUri(Context context, String tipo, String nome) {
        return "android.resource://" + context.getPackageName() + "/" + tipo + "/" + nome;
    }
}
