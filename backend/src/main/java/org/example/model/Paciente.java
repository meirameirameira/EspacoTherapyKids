package org.example.model;

public class Paciente {
    private long codigo;
    private String nome;
    private Sessao fono;
    private Sessao terapiaOcupacional;
    private Sessao aba;

    public Paciente(String nome, Sessao fono, Sessao terapiaOcupacional, Sessao aba) {
        this.nome = nome;
        this.fono = fono;
        this.terapiaOcupacional = terapiaOcupacional;
        this.aba = aba;
    }

    public Paciente(long codigo, String nome, Sessao fono, Sessao terapiaOcupacional, Sessao aba) {
        this(nome, fono, terapiaOcupacional, aba);
        this.codigo = codigo;
    }

    public long getCodigo() { return codigo; }
    public void setCodigo(long codigo) { this.codigo = codigo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Sessao getFono() { return fono; }
    public void setFono(Sessao fono) { this.fono = fono; }
    public Sessao getTerapiaOcupacional() { return terapiaOcupacional; }
    public void setTerapiaOcupacional(Sessao terapiaOcupacional) { this.terapiaOcupacional = terapiaOcupacional; }
    public Sessao getAba() { return aba; }
    public void setAba(Sessao aba) { this.aba = aba; }

    public void exibirInformacoes() {
        System.out.println("Paciente [" + codigo + "] - " + nome);
        imprimirSessao("Fonoaudiologia", fono);
        imprimirSessao("Terapia Ocupacional", terapiaOcupacional);
        imprimirSessao("ABA", aba);
        System.out.println("-----------------------------------");
    }

    private void imprimirSessao(String titulo, Sessao s) {
        System.out.println("--- " + titulo + " ---");
        System.out.printf("Preço/Hora: R$ %.2f | Horas: %d | Total: R$ %.2f%n",
                s.getPreco(), s.getHoras(), s.calcularTotal());
        System.out.printf("Reembolso: R$ %.2f | NF necessárias: %d%n",
                s.getReembolsoInformado(), s.calcularNF());
    }
}