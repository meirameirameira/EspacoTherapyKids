package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SessaoDTO {
    @NotNull @Positive(message = "preco deve ser > 0")
    private Double preco;

    @NotNull @Min(value = 1, message = "horas deve ser >= 1")
    private Integer horas;

    @NotNull @Positive(message = "reembolsoInformado deve ser > 0")
    private Double reembolsoInformado;

    public SessaoDTO() { }

    public SessaoDTO(Double preco, Integer horas, Double reembolsoInformado) {
        this.preco = preco;
        this.horas = horas;
        this.reembolsoInformado = reembolsoInformado;
    }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
    public Integer getHoras() { return horas; }
    public void setHoras(Integer horas) { this.horas = horas; }
    public Double getReembolsoInformado() { return reembolsoInformado; }
    public void setReembolsoInformado(Double reembolsoInformado) { this.reembolsoInformado = reembolsoInformado; }
}
