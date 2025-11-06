package org.example.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PacienteRequestDTO {
    @NotNull @Positive(message = "nrResponsavel deve ser > 0")
    private Long nrResponsavel;

    @NotBlank(message = "nmResponsavel é obrigatório")
    private String nmResponsavel;

    @NotBlank(message = "nome é obrigatório")
    private String nome;

    @Valid @NotNull(message = "fono é obrigatório")
    private SessaoDTO fono;

    @Valid @NotNull(message = "terapiaOcupacional é obrigatória")
    private SessaoDTO terapiaOcupacional;

    @Valid @NotNull(message = "aba é obrigatória")
    private SessaoDTO aba;

    public PacienteRequestDTO() { }

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
