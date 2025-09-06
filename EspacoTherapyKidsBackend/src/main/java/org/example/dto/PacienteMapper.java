package org.example.dto;

import org.example.model.Paciente;
import org.example.model.Sessao;

public class PacienteMapper {

    public static Paciente toEntity(PacienteRequestDTO dto) {
        return new Paciente(
                dto.getNrResponsavel(),
                dto.getNmResponsavel(),
                dto.getNome(),
                toSessao(dto.getFono()),
                toSessao(dto.getTerapiaOcupacional()),
                toSessao(dto.getAba())
        );
    }

    public static void copyToEntity(PacienteRequestDTO dto, Paciente entity) {
        entity.setNrResponsavel(dto.getNrResponsavel());
        entity.setNmResponsavel(dto.getNmResponsavel());
        entity.setNome(dto.getNome());
        entity.setFono(toSessao(dto.getFono()));
        entity.setTerapiaOcupacional(toSessao(dto.getTerapiaOcupacional()));
        entity.setAba(toSessao(dto.getAba()));
    }

    public static PacienteResponseDTO toResponse(Paciente entity) {
        return new PacienteResponseDTO(
                entity.getCodigo(),
                entity.getNrResponsavel(),
                entity.getNmResponsavel(),
                entity.getNome(),
                toSessaoDTO(entity.getFono()),
                toSessaoDTO(entity.getTerapiaOcupacional()),
                toSessaoDTO(entity.getAba())
        );
    }

    private static Sessao toSessao(SessaoDTO s) {
        if (s == null) return null;
        return new Sessao(
                s.getPreco(),
                s.getHoras(),
                s.getReembolsoInformado()
        );
    }

    private static SessaoDTO toSessaoDTO(Sessao s) {
        if (s == null) return null;
        return new SessaoDTO(
                s.getPreco(),
                s.getHoras(),
                s.getReembolsoInformado()
        );
    }
}
