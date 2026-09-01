package com.example.drawerlayout;

public class ItemModel {

    private int imagemResId;
    private String nome;
    private String descricao;
    private String detalhes;
    private int somResId;

    // item sem detalhes próprios e sem som
    public ItemModel(int imagemResId, String nome, String descricao) {
        this.imagemResId = imagemResId;
        this.nome = nome;
        this.descricao = descricao;
        this.detalhes = descricao;
        this.somResId = 0;
    }

    // item com som, mantendo a descrição também como detalhe
    public ItemModel(int imagemResId, String nome, String descricao, int somResId) {
        this.imagemResId = imagemResId;
        this.nome = nome;
        this.descricao = descricao;
        this.detalhes = descricao;
        this.somResId = somResId;
    }

    // item com detalhes próprios, mas sem som
    public ItemModel(
            int imagemResId,
            String nome,
            String descricao,
            String detalhes
    ) {
        this.imagemResId = imagemResId;
        this.nome = nome;
        this.descricao = descricao;
        this.detalhes = detalhes;
        this.somResId = 0;
    }

    // item com detalhes próprios e som
    public ItemModel(
            int imagemResId,
            String nome,
            String descricao,
            String detalhes,
            int somResId
    ) {
        this.imagemResId = imagemResId;
        this.nome = nome;
        this.descricao = descricao;
        this.detalhes = detalhes;
        this.somResId = somResId;
    }

    public int getImagemResId() {
        return imagemResId;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public int getSomResId() {
        return somResId;
    }
}