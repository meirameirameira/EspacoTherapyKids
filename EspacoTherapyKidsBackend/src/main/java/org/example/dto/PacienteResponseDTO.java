package org.example.dto;

public class PacienteResponseDTO {
    private long codigo;
    private Long nrResponsavel;
    private String nmResponsavel;
    private String nome;

    private SessaoDTO fono;
    private SessaoDTO terapiaOcupacional;
    private SessaoDTO aba;

    public PacienteResponseDTO() { }

    public PacienteResponseDTO(long codigo, Long nrResponsavel, String nmResponsavel,
                               String nome, SessaoDTO fono, SessaoDTO terapiaOcupacional, SessaoDTO aba) {
        this.codigo = codigo;
        this.nrResponsavel = nrResponsavel;
        this.nmResponsavel = nmResponsavel;
        this.nome = nome;
        this.fono = fono;
        this.terapiaOcupacional = terapiaOcupacional;
        this.aba = aba;
    }

    public long getCodigo() { return codigo; }
    public void setCodigo(long codigo) { this.codigo = codigo; }
    public Long getNrResponsavel() { return nrResponsavel; }
    public void setNrResponsavel(Long nrResponsavel) { this.nrResponsavel = nrResponsavel; }
    public String getNmResponsavel() { return nmResponsavel; }
    public void setNmResponsavel(String nmResponsavel) { this.nmResponsavel = nmResponsavel; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public SessaoDTO getFono() { return fono; }
    public void setFono(SessaoDTO fono) { this.fono = fono; }
    public SessaoDTO getTerapiaOcupacional() { return terapiaOcupacional; }
    public void setTerapiaOcupacional(SessaoDTO terapiaOcupacional) { this.terapiaOcupacional = terapiaOcupacional; }
    public SessaoDTO getAba() { return aba; }
    public void setAba(SessaoDTO aba) { this.aba = aba; }
}
