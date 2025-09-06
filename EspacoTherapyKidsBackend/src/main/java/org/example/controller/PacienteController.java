package org.example.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.dao.PacienteDao;
import org.example.dto.PageResponse;
import org.example.dto.PacienteMapper;
import org.example.dto.PacienteRequestDTO;
import org.example.dto.PacienteResponseDTO;
import org.example.exception.EntidadeNaoEncontradaException;
import org.example.model.Paciente;
import org.example.model.Sessao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Comparator;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteDao dao;

    public PacienteController(PacienteDao dao) {
        this.dao = dao;
    }

    private void sanitize(Paciente p) {
        if (p.getFono() == null) p.setFono(new Sessao(0, 1, 0));
        if (p.getTerapiaOcupacional() == null) p.setTerapiaOcupacional(new Sessao(0, 1, 0));
        if (p.getAba() == null) p.setAba(new Sessao(0, 1, 0));

        if (p.getFono().getPreco() <= 0 || p.getFono().getHoras() <= 0) {
            p.getFono().setPreco(0); p.getFono().setHoras(1); p.getFono().setReembolsoInformado(0);
        }
        if (p.getTerapiaOcupacional().getPreco() <= 0 || p.getTerapiaOcupacional().getHoras() <= 0) {
            p.getTerapiaOcupacional().setPreco(0); p.getTerapiaOcupacional().setHoras(1); p.getTerapiaOcupacional().setReembolsoInformado(0);
        }
        if (p.getAba().getPreco() <= 0) {
            p.getAba().setPreco(0); p.getAba().setReembolsoInformado(0);
        }
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> cadastrar(@RequestBody @Valid PacienteRequestDTO dto) throws Exception {
        Paciente entity = PacienteMapper.toEntity(dto);
        sanitize(entity);
        dao.cadastrar(entity);
        return ResponseEntity.created(URI.create("/api/pacientes/" + entity.getCodigo()))
                .body(PacienteMapper.toResponse(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<PacienteResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false, name = "nmResponsavel") String nmResponsavel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "codigo") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws Exception {
        List<Paciente> lista = dao.listar();

        var stream = lista.stream();
        if (nome != null && !nome.isBlank()) {
            String q = nome.toLowerCase();
            stream = stream.filter(p -> p.getNome() != null && p.getNome().toLowerCase().contains(q));
        }
        if (nmResponsavel != null && !nmResponsavel.isBlank()) {
            String q2 = nmResponsavel.toLowerCase();
            stream = stream.filter(p -> p.getNmResponsavel() != null && p.getNmResponsavel().toLowerCase().contains(q2));
        }
        List<Paciente> filtrada = stream.toList();

        Comparator<Paciente> cmp = switch (sortBy) {
            case "nome" -> Comparator.comparing(p -> p.getNome() == null ? "" : p.getNome(), String.CASE_INSENSITIVE_ORDER);
            case "nmResponsavel" -> Comparator.comparing(p -> p.getNmResponsavel() == null ? "" : p.getNmResponsavel(), String.CASE_INSENSITIVE_ORDER);
            case "nrResponsavel" -> Comparator.comparing(p -> p.getNrResponsavel() == null ? Long.MIN_VALUE : p.getNrResponsavel());
            default -> Comparator.comparingLong(Paciente::getCodigo);
        };
        if ("desc".equalsIgnoreCase(sortDir)) cmp = cmp.reversed();

        List<Paciente> ordenada = filtrada.stream().sorted(cmp).toList();

        int from = Math.max(0, page * size);
        int to = Math.min(ordenada.size(), from + size);
        List<Paciente> pageSlice = from >= ordenada.size() ? List.of() : ordenada.subList(from, to);

        var content = pageSlice.stream().map(PacienteMapper::toResponse).toList();
        PageResponse<PacienteResponseDTO> body = new PageResponse<>(content, page, size, ordenada.size());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> pesquisar(@PathVariable long id) throws Exception {
        Paciente p = dao.pesquisar(id);
        return ResponseEntity.ok(PacienteMapper.toResponse(p));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> atualizar(@PathVariable long id,
                                                         @RequestBody @Valid PacienteRequestDTO dto) throws Exception {
        Paciente existente = dao.pesquisar(id);
        PacienteMapper.copyToEntity(dto, existente);
        existente.setCodigo(id);
        sanitize(existente);
        dao.atualizar(existente);
        return ResponseEntity.ok(PacienteMapper.toResponse(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable long id) throws Exception {
        dao.pesquisar(id);
        dao.remover(id);
        return ResponseEntity.noContent().build();
    }
}
