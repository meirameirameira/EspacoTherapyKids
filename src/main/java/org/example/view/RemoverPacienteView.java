package org.example.view;

import org.example.dao.PacienteDao;
import org.example.exception.EntidadeNaoEncontradaException;

import java.sql.SQLException;
import java.util.Scanner;

public class RemoverPacienteView {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nID do paciente a remover: "); long id = sc.nextLong();
        try {
            new PacienteDao().remover(id);
            System.out.println("Paciente removido com sucesso.");
        } catch (EntidadeNaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }
}