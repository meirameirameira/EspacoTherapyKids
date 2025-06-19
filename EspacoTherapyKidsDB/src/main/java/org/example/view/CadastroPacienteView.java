package org.example.view;

import org.example.dao.PacienteDao;
import org.example.model.Paciente;
import org.example.model.Sessao;

import java.sql.SQLException;
import java.util.Scanner;

public class CadastroPacienteView {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nNome do paciente: ");
        String nome = sc.nextLine();

        // Fonoaudiologia
        System.out.println("\nDados Fonoaudiologia");
        System.out.print("O paciente faz Fonoaudiologia? (s/n): ");
        boolean hasFono = sc.nextLine().trim().equalsIgnoreCase("s");
        Sessao fono;
        if (hasFono) {
            System.out.print("Valor da Sessão: "); double pFono = sc.nextDouble();
            System.out.print("Horas de Sessão: ");        int hFono = sc.nextInt();
            System.out.print("Valor do Reembolso: ");   double rFono = sc.nextDouble();
            sc.nextLine();
            fono = new Sessao(pFono, hFono, rFono);
        } else {
            fono = new Sessao(0.0, 0, 0.0);
        }

        // Terapia Ocupacional
        System.out.println("\nDados Terapia Ocupacional");
        System.out.print("O paciente faz Terapia Ocupacional? (s/n): ");
        boolean hasTO = sc.nextLine().trim().equalsIgnoreCase("s");
        Sessao to;
        if (hasTO) {
            System.out.print("Valor da Sessão: "); double pTO = sc.nextDouble();
            System.out.print("Horas de Sessão: ");        int hTO = sc.nextInt();
            System.out.print("Valor do Reembolso: ");   double rTO = sc.nextDouble();
            sc.nextLine();
            to = new Sessao(pTO, hTO, rTO);
        } else {
            to = new Sessao(0.0, 0, 0.0);
        }

        // ABA
        System.out.println("\nDados ABA");
        System.out.print("O paciente faz ABA? (s/n): ");
        boolean hasAba = sc.nextLine().trim().equalsIgnoreCase("s");
        Sessao aba;
        if (hasAba) {
            System.out.print("Valor do Pacote: "); double pAba = sc.nextDouble();
            System.out.print("Valor do Reembolso: ");   double rAba = sc.nextDouble();
            sc.nextLine();
            aba = new Sessao(pAba, rAba);
        } else {
            aba = new Sessao(0.0, 0, 0.0);
        }

        Paciente p = new Paciente(nome, fono, to, aba);
        PacienteDao dao = new PacienteDao();
        dao.cadastrar(p);
        System.out.println("\nPaciente cadastrado com ID: " + p.getCodigo());
    }
}