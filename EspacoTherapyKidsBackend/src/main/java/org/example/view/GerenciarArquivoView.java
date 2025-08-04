package org.example.view;

import org.example.dao.PacienteDao;
import org.example.factory.ConnectionFactory;
import org.example.model.Paciente;
import org.example.model.Sessao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.util.List;

public class GerenciarArquivoView {
    private static final String FILE_NAME = "dadosPaciente.txt";

    public static void iniciar() {
        exportarDados();
        System.out.println("\nArquivo '" + FILE_NAME + "' gerado com sucesso com os dados do banco.");
    }

    private static void exportarDados() {
        try (Connection conexao = ConnectionFactory.getConnection()) {
            PacienteDao dao = new PacienteDao();
            List<Paciente> pacientes = dao.listar();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (Paciente p : pacientes) {
                    bw.write(formatarLinha(p));
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao exportar dados para arquivo: " + e.getMessage());
        }
    }

    private static String formatarLinha(Paciente p) {
        StringBuilder sb = new StringBuilder();

        // Cabeçalho do paciente
        sb.append("Paciente [")
                .append(p.getCodigo())
                .append("] - ")
                .append(p.getNome());

        // Dados do responsável
        sb.append("\nResponsável: [")
                .append(p.getNrResponsavel())
                .append("] - ")
                .append(p.getNmResponsavel());

        Sessao fono = p.getFono();
        Sessao to   = p.getTerapiaOcupacional();
        Sessao aba  = p.getAba();

        sb.append("\n\nFonoaudiologia:");
        sb.append("\n Valor da Sessão: R$").append(fono.getPreco());
        sb.append("\n Horas de Sessão: ").append(fono.getHoras()).append("h");
        sb.append("\n Total: R$").append(fono.calcularTotal());
        sb.append("\n Reembolso: R$").append(fono.getReembolsoInformado());
        sb.append("\n Horas para Nota Fiscal: ").append(fono.calcularNF()).append("h");

        sb.append("\n\nTerapia Ocupacional:");
        sb.append("\n Valor da Sessão: R$").append(to.getPreco());
        sb.append("\n Horas de Sessão: ").append(to.getHoras()).append("h");
        sb.append("\n Total: R$").append(to.calcularTotal());
        sb.append("\n Reembolso: R$").append(to.getReembolsoInformado());
        sb.append("\n Horas para Nota Fiscal: ").append(to.calcularNF()).append("h");

        sb.append("\n\nTerapia ABA:");
        sb.append("\n Valor do Pacote: R$").append(aba.getPreco());
        sb.append("\n Reembolso: R$").append(aba.getReembolsoInformado());
        sb.append("\n Horas para Nota Fiscal: ").append(aba.calcularNF()).append("h");

        sb.append("\n-------------------------------------------");
        return sb.toString();
    }
}
