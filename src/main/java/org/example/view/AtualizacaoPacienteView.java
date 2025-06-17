package org.example.view;

import org.example.dao.PacienteDao;
import org.example.exception.EntidadeNaoEncontradaException;
import org.example.model.Paciente;
import org.example.model.Sessao;

import java.sql.SQLException;
import java.util.Scanner;

public class AtualizacaoPacienteView {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do paciente a atualizar: "); long id = sc.nextLong(); sc.nextLine();
        try {
            Paciente p = new PacienteDao().pesquisar(id);
            System.out.print("Novo nome (" + p.getNome() + "): ");
            p.setNome(sc.nextLine());

            if (confirm(sc, "Atualizar Fonoaudiologia? (s/n): ")) {
                p.setFono(lerSessao(sc, "Fonoaudiologia"));
            }
            if (confirm(sc, "Atualizar Terapia Ocupacional? (s/n): ")) {
                p.setTerapiaOcupacional(lerSessao(sc, "Terapia Ocupacional"));
            }
            if (confirm(sc, "Atualizar ABA? (s/n): ")) {
                System.out.println("-- ABA --");
                System.out.print("Preço ABA: "); double preco = sc.nextDouble();
                System.out.print("Reembolso ABA: "); double ref = sc.nextDouble();
                sc.nextLine();
                p.setAba(new Sessao(preco, ref));
            }

            new PacienteDao().atualizar(p);
            System.out.println("Atualização concluída.");
        } catch (EntidadeNaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }

    private static boolean confirm(Scanner sc, String msg) {
        System.out.print(msg);
        return sc.nextLine().trim().equalsIgnoreCase("s");
    }

    private static Sessao lerSessao(Scanner sc, String titulo) {
        System.out.println("-- " + titulo + " --");
        System.out.print("Preço/Hora: "); double preco = sc.nextDouble();
        System.out.print("Horas: ");        int horas = sc.nextInt();
        System.out.print("Reembolso: ");   double ref = sc.nextDouble();
        sc.nextLine();
        return new Sessao(preco, horas, ref);
    }
}
