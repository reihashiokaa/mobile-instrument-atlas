package com.example.drawerlayout;

public class ItemModel {
    private int imagemResId;
    private String nome;
    private String descricao;
    private int somResId;

    public ItemModel(int imagemResId, String nome, String descricao) {
        this.imagemResId = imagemResId;
        this.nome = nome;
        this.descricao = descricao;
        this.somResId = 0;
    }

    public ItemModel(
            int imagemResId,
            String nome,
            String descricao,
            int somResId) {

        this.imagemResId = imagemResId;
        this.nome = nome;
        this.descricao = descricao;
        this.somResId = somResId;
    }
    public int getImagemResId() { return imagemResId; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }

    public int getSomResId() { return somResId; }
}
