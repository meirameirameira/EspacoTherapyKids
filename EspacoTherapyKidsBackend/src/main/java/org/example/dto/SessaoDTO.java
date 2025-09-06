package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

public class SessaoDTO {

    @DecimalMin(value = "0.0", inclusive = true, message = "preco deve ser >= 0")
    private double preco;

    @Min(value = 0, message = "horas deve ser >= 0")
    private int horas;

    @DecimalMin(value = "0.0", inclusive = true, message = "reembolsoInformado deve ser >= 0")
    private double reembolsoInformado;

    public SessaoDTO() { }

    public SessaoDTO(double preco, int horas, double reembolsoInformado) {
        this.preco = preco;
        this.horas = horas;
        this.reembolsoInformado = reembolsoInformado;
    }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public int getHoras() { return horas; }
    public void setHoras(int horas) { this.horas = horas; }

    public double getReembolsoInformado() { return reembolsoInformado; }
    public void setReembolsoInformado(double reembolsoInformado) { this.reembolsoInformado = reembolsoInformado; }
}
