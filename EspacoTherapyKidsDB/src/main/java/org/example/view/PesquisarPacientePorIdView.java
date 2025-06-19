package org.example.view;

import org.example.dao.PacienteDao;
import org.example.exception.EntidadeNaoEncontradaException;
import org.example.model.Paciente;

import java.sql.SQLException;
import java.util.Scanner;

public class PesquisarPacientePorIdView {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do paciente: "); long id = sc.nextLong();
        try {
            Paciente p = new PacienteDao().pesquisar(id);
            p.exibirInformacoes();
        } catch (EntidadeNaoEncontradaException e) {
            System.err.println(e.getMessage());
        }
    }
}