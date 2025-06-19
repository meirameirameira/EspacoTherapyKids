package org.example.dao;

import org.example.factory.ConnectionFactory;
import org.example.exception.EntidadeNaoEncontradaException;
import org.example.model.Paciente;
import org.example.model.Sessao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PacienteDao {
    private final Connection conexao;
    public PacienteDao() throws SQLException {
        this.conexao = ConnectionFactory.getConnection();
    }
    public void cadastrar(Paciente p) throws SQLException {
        String sql = "INSERT INTO tb_paciente (nm_paciente, preco_fono, horas_fono, total_fono, reembolso_fono, nf_fono, " +
                "preco_to, horas_to, total_to, reembolso_to, nf_to, preco_aba, reembolso_aba, nf_aba) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stm = conexao.prepareStatement(sql, new String[]{"CD_PACIENTE"});
        int i = 1;
        stm.setString(i++, p.getNome());
        // Fono
        stm.setDouble(i++, p.getFono().getPreco());
        stm.setInt   (i++, p.getFono().getHoras());
        stm.setDouble(i++, p.getFono().calcularTotal());
        stm.setDouble(i++, p.getFono().getReembolsoInformado());
        stm.setInt   (i++, p.getFono().calcularNF());
        // TO
        stm.setDouble(i++, p.getTerapiaOcupacional().getPreco());
        stm.setInt   (i++, p.getTerapiaOcupacional().getHoras());
        stm.setDouble(i++, p.getTerapiaOcupacional().calcularTotal());
        stm.setDouble(i++, p.getTerapiaOcupacional().getReembolsoInformado());
        stm.setInt   (i++, p.getTerapiaOcupacional().calcularNF());
        // ABA
        stm.setDouble(i++, p.getAba().getPreco());
        stm.setDouble(i++, p.getAba().getReembolsoInformado());
        stm.setInt   (i++, (int)Math.ceil(p.getAba().getPreco() / p.getAba().getReembolsoInformado()));
        stm.executeUpdate();
        ResultSet rs = stm.getGeneratedKeys(); if (rs.next()) p.setCodigo(rs.getLong(1));
    }
    public void atualizar(Paciente p) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "UPDATE tb_paciente SET nm_paciente=?, preco_fono=?, horas_fono=?, total_fono=?, reembolso_fono=?, nf_fono=?, " +
                "preco_to=?, horas_to=?, total_to=?, reembolso_to=?, nf_to=?, preco_aba=?, reembolso_aba=?, nf_aba=? WHERE cd_paciente=?";
        PreparedStatement stm = conexao.prepareStatement(sql);
        int i = 1;
        stm.setString(i++, p.getNome());
        // Fono
        stm.setDouble(i++, p.getFono().getPreco());
        stm.setInt   (i++, p.getFono().getHoras());
        stm.setDouble(i++, p.getFono().calcularTotal());
        stm.setDouble(i++, p.getFono().getReembolsoInformado());
        stm.setInt   (i++, p.getFono().calcularNF());
        // TO
        stm.setDouble(i++, p.getTerapiaOcupacional().getPreco());
        stm.setInt   (i++, p.getTerapiaOcupacional().getHoras());
        stm.setDouble(i++, p.getTerapiaOcupacional().calcularTotal());
        stm.setDouble(i++, p.getTerapiaOcupacional().getReembolsoInformado());
        stm.setInt   (i++, p.getTerapiaOcupacional().calcularNF());
        // ABA
        stm.setDouble(i++, p.getAba().getPreco());
        stm.setDouble(i++, p.getAba().getReembolsoInformado());
        stm.setInt   (i++, (int)Math.ceil(p.getAba().getPreco() / p.getAba().getReembolsoInformado()));
        stm.setLong  (i++, p.getCodigo());
        if (stm.executeUpdate()==0) throw new EntidadeNaoEncontradaException("Paciente não encontrado");
    }
    public List<Paciente> listar() throws SQLException {
        String sql="SELECT * FROM tb_paciente";
        PreparedStatement stm = conexao.prepareStatement(sql);
        ResultSet rs = stm.executeQuery(); List<Paciente> lista = new ArrayList<>();
        while (rs.next()) {
            Sessao f = new Sessao(rs.getDouble("preco_fono"), rs.getInt("horas_fono"), rs.getDouble("reembolso_fono"));
            Sessao t = new Sessao(rs.getDouble("preco_to"),   rs.getInt("horas_to"),   rs.getDouble("reembolso_to"));
            Sessao a = new Sessao(rs.getDouble("preco_aba"), rs.getDouble("reembolso_aba"));
            lista.add(new Paciente(rs.getLong("cd_paciente"), rs.getString("nm_paciente"), f, t, a));
        }
        return lista;
    }
    public Paciente pesquisar(long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql="SELECT * FROM tb_paciente WHERE cd_paciente = ?";
        PreparedStatement stm=conexao.prepareStatement(sql);
        stm.setLong(1,id); ResultSet rs=stm.executeQuery();
        if(rs.next()) return new Paciente(
                rs.getLong("cd_paciente"), rs.getString("nm_paciente"),
                new Sessao(rs.getDouble("preco_fono"), rs.getInt("horas_fono"), rs.getDouble("reembolso_fono")),
                new Sessao(rs.getDouble("preco_to"),   rs.getInt("horas_to"),   rs.getDouble("reembolso_to")),
                new Sessao(rs.getDouble("preco_aba"), rs.getDouble("reembolso_aba"))
        );
        throw new EntidadeNaoEncontradaException("Paciente não encontrado");
    }
    public void remover(long id) throws SQLException, EntidadeNaoEncontradaException {
        PreparedStatement stm=conexao.prepareStatement("DELETE FROM tb_paciente WHERE cd_paciente = ?");
        stm.setLong(1,id); if(stm.executeUpdate()==0) throw new EntidadeNaoEncontradaException("Paciente não encontrado");
    }
}