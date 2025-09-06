package org.example.dao;

import org.example.factory.ConnectionFactory;
import org.example.exception.EntidadeNaoEncontradaException;
import org.example.model.Paciente;
import org.example.model.Sessao;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

@Repository
public class PacienteDao {
    private final Connection conexao;
    public PacienteDao() throws SQLException { this.conexao = ConnectionFactory.getConnection(); }

    // Helpers numéricos
    private BigDecimal bd2(double v) { return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP); }
    private double preco(Sessao s) { return s != null ? s.getPreco() : 0.0; }
    private int horas(Sessao s)    { return s != null ? s.getHoras() : 0; }
    private double remb(Sessao s)  { return s != null ? s.getReembolsoInformado() : 0.0; }
    private double total(Sessao s) { return s != null ? s.calcularTotal() : 0.0; }

    private int nfSeguro(Sessao s) {
        double r = remb(s);
        if (r <= 0) return 0;
        return (int)Math.ceil(total(s) / r);
    }
    private int nfAbaSeguro(Sessao aba) {
        double r = remb(aba);
        if (r <= 0) return 0;
        return (int)Math.ceil(preco(aba) / r);
    }

    public void cadastrar(Paciente p) throws SQLException {
        String sql =
                "INSERT INTO tb_paciente (" +
                        " nr_responsavel, nm_responsavel, nm_paciente," +
                        " preco_fono, horas_fono, total_fono, reembolso_fono, nf_fono," +
                        " preco_to, horas_to, total_to, reembolso_to, nf_to," +
                        " preco_aba, reembolso_aba, nf_aba" +
                        ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                        "RETURNING cd_paciente INTO ?";

        // use OraclePreparedStatement para suportar RETURNING ... INTO ?
        try (OraclePreparedStatement stm = (OraclePreparedStatement) conexao.prepareStatement(sql)) {
            int i = 1;

            // -- dados básicos
            stm.setLong  (i++, p.getNrResponsavel());
            stm.setString(i++, p.getNmResponsavel());
            stm.setString(i++, p.getNome());

            // -- FONO
            Sessao f = p.getFono();
            stm.setBigDecimal(i++, bd2(preco(f)));
            stm.setInt       (i++, horas(f));
            stm.setBigDecimal(i++, bd2(total(f)));
            stm.setBigDecimal(i++, bd2(remb(f)));
            stm.setInt       (i++, nfSeguro(f));

            // -- TO
            Sessao t = p.getTerapiaOcupacional();
            stm.setBigDecimal(i++, bd2(preco(t)));
            stm.setInt       (i++, horas(t));
            stm.setBigDecimal(i++, bd2(total(t)));
            stm.setBigDecimal(i++, bd2(remb(t)));
            stm.setInt       (i++, nfSeguro(t));

            // -- ABA
            Sessao a = p.getAba();
            stm.setBigDecimal(i++, bd2(preco(a)));
            stm.setBigDecimal(i++, bd2(remb(a)));
            stm.setInt       (i++, nfAbaSeguro(a));

            // -- parâmetro de retorno da PK
            stm.registerReturnParameter(i, OracleTypes.NUMBER);

            stm.executeUpdate();

            // pega a PK retornada com segurança
            try (ResultSet rs = stm.getReturnResultSet()) {
                if (rs != null && rs.next()) {
                    // Oracle retorna NUMBER -> BigDecimal
                    java.math.BigDecimal id = rs.getBigDecimal(1);
                    if (id != null) p.setCodigo(id.longValue());
                }
            }
        }
    }

    public void atualizar(Paciente p) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "UPDATE tb_paciente SET nr_responsavel=?, nm_responsavel=?, nm_paciente=?, " +
                "preco_fono=?, horas_fono=?, total_fono=?, reembolso_fono=?, nf_fono=?, " +
                "preco_to=?, horas_to=?, total_to=?, reembolso_to=?, nf_to=?, " +
                "preco_aba=?, reembolso_aba=?, nf_aba=? WHERE cd_paciente=?";
        try (PreparedStatement stm = conexao.prepareStatement(sql)) {
            int i = 1;
            stm.setLong  (i++, p.getNrResponsavel());
            stm.setString(i++, p.getNmResponsavel());
            stm.setString(i++, p.getNome());

            Sessao f = p.getFono();
            stm.setBigDecimal(i++, bd2(preco(f)));
            stm.setInt       (i++, horas(f));
            stm.setBigDecimal(i++, bd2(total(f)));
            stm.setBigDecimal(i++, bd2(remb(f)));
            stm.setInt       (i++, nfSeguro(f));

            Sessao t = p.getTerapiaOcupacional();
            stm.setBigDecimal(i++, bd2(preco(t)));
            stm.setInt       (i++, horas(t));
            stm.setBigDecimal(i++, bd2(total(t)));
            stm.setBigDecimal(i++, bd2(remb(t)));
            stm.setInt       (i++, nfSeguro(t));

            Sessao a = p.getAba();
            stm.setBigDecimal(i++, bd2(preco(a)));
            stm.setBigDecimal(i++, bd2(remb(a)));
            stm.setInt       (i++, nfAbaSeguro(a));

            stm.setLong      (i++, p.getCodigo());

            if (stm.executeUpdate() == 0)
                throw new EntidadeNaoEncontradaException("Paciente não encontrado");
        }
    }

    public List<Paciente> listar() throws SQLException {
        String sql="SELECT * FROM tb_paciente";
        try (PreparedStatement stm = conexao.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            List<Paciente> lista = new ArrayList<>();
            while (rs.next()) {
                Sessao f = new Sessao(rs.getDouble("preco_fono"), rs.getInt("horas_fono"), rs.getDouble("reembolso_fono"));
                Sessao t = new Sessao(rs.getDouble("preco_to"),   rs.getInt("horas_to"),   rs.getDouble("reembolso_to"));
                Sessao a = new Sessao(rs.getDouble("preco_aba"),  rs.getDouble("reembolso_aba"));
                lista.add(new Paciente(
                        rs.getLong("cd_paciente"),
                        rs.getLong("nr_responsavel"),
                        rs.getString("nm_responsavel"),
                        rs.getString("nm_paciente"),
                        f, t, a
                ));
            }
            return lista;
        }
    }

    public Paciente pesquisar(long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql="SELECT * FROM tb_paciente WHERE cd_paciente = ?";
        try (PreparedStatement stm = conexao.prepareStatement(sql)) {
            stm.setLong(1,id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) return new Paciente(
                        rs.getLong("cd_paciente"),
                        rs.getLong("nr_responsavel"),
                        rs.getString("nm_responsavel"),
                        rs.getString("nm_paciente"),
                        new Sessao(rs.getDouble("preco_fono"), rs.getInt("horas_fono"), rs.getDouble("reembolso_fono")),
                        new Sessao(rs.getDouble("preco_to"),   rs.getInt("horas_to"),   rs.getDouble("reembolso_to")),
                        new Sessao(rs.getDouble("preco_aba"),  rs.getDouble("reembolso_aba"))
                );
            }
        }
        throw new EntidadeNaoEncontradaException("Paciente não encontrado");
    }

    public void remover(long id) throws SQLException, EntidadeNaoEncontradaException {
        try (PreparedStatement stm = conexao.prepareStatement("DELETE FROM tb_paciente WHERE cd_paciente = ?")) {
            stm.setLong(1,id);
            if (stm.executeUpdate()==0) throw new EntidadeNaoEncontradaException("Paciente não encontrado");
        }
    }
}
