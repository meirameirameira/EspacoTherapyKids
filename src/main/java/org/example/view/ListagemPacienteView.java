package org.example.view;

import org.example.dao.PacienteDao;
import org.example.model.Paciente;

import java.sql.SQLException;
import java.util.List;

public class ListagemPacienteView {
    public static void main(String[] args) throws SQLException {
        PacienteDao dao = new PacienteDao();
        List<Paciente> lista = dao.listar();
        for (Paciente p : lista) {
            p.exibirInformacoes();
        }
    }
}