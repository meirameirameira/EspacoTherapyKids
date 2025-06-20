package org.example.model;

public class Sessao {
    private double preco;
    private int horas;
    private double reembolsoInformado;

    public Sessao() { }

    public Sessao(double preco, int horas, double reembolsoInformado) {
        this.preco = preco;
        this.horas = horas;
        this.reembolsoInformado = reembolsoInformado;
    }

    public Sessao(double preco, double reembolsoInformado) {
        this(preco, 1, reembolsoInformado);
    }

    public double calcularTotal() {
        return preco * horas;
    }

    public double calcularReembolso() {
        return reembolsoInformado;
    }

    public int calcularNF() {
        return (int) Math.ceil(calcularTotal() / reembolsoInformado);
    }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    public int getHoras() { return horas; }
    public void setHoras(int horas) { this.horas = horas; }
    public double getReembolsoInformado() { return reembolsoInformado; }
    public void setReembolsoInformado(double reembolsoInformado) { this.reembolsoInformado = reembolsoInformado; }
}