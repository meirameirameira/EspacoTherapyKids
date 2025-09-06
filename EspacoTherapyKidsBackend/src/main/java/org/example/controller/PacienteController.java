package org.example.controller;

import org.example.dao.PacienteDao;
import org.example.dto.PacienteMapper;
import org.example.dto.PacienteRequestDTO;
import org.example.dto.PacienteResponseDTO;
import org.example.dto.PageResponse;
import org.example.exception.EntidadeNaoEncontradaException;
import org.example.model.Paciente;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Comparator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteDao dao;

    public PacienteController(PacienteDao dao) {
        this.dao = dao;
    }

    // ====== CADASTRAR ======
    @PostMapping
    public ResponseEntity<PacienteResponseDTO> cadastrar(@RequestBody @Valid PacienteRequestDTO dto) throws Exception {
        Paciente entity = PacienteMapper.toEntity(dto);
        dao.cadastrar(entity); // DAO recebe Paciente e não retorna; espera-se que preencha 'codigo' no entity
        return ResponseEntity
                .created(URI.create("/api/pacientes/" + entity.getCodigo()))
                .body(PacienteMapper.toResponse(entity));
    }

    // ====== LISTAR ======
    @GetMapping
    public ResponseEntity<PageResponse<PacienteResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false, name = "nmResponsavel") String nmResponsavel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "codigo") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws Exception {
        // carrega tudo do DAO
        List<Paciente> lista = dao.listar();

        // filtros (case-insensitive, contém)
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

        // ordenação (default: codigo asc)
        Comparator<Paciente> cmp;
        switch (sortBy) {
            case "nome" -> cmp = Comparator.comparing(
                    p -> p.getNome() == null ? "" : p.getNome(), String.CASE_INSENSITIVE_ORDER);
            case "nmResponsavel" -> cmp = Comparator.comparing(
                    p -> p.getNmResponsavel() == null ? "" : p.getNmResponsavel(), String.CASE_INSENSITIVE_ORDER);
            case "nrResponsavel" -> cmp = Comparator.comparing(
                    p -> p.getNrResponsavel() == null ? Long.MIN_VALUE : p.getNrResponsavel());
            case "codigo" -> cmp = Comparator.comparingLong(Paciente::getCodigo);
            default -> cmp = Comparator.comparingLong(Paciente::getCodigo); // fallback seguro
        }
        if ("desc".equalsIgnoreCase(sortDir)) {
            cmp = cmp.reversed();
        }
        List<Paciente> ordenada = filtrada.stream().sorted(cmp).toList();

        // paginação
        int from = Math.max(0, page * size);
        int to = Math.min(ordenada.size(), from + size);
        List<Paciente> pageSlice = from >= ordenada.size() ? List.of() : ordenada.subList(from, to);

        var content = pageSlice.stream().map(PacienteMapper::toResponse).toList();
        PageResponse<PacienteResponseDTO> body = new PageResponse<>(content, page, size, ordenada.size());

        return ResponseEntity.ok(body);
    }



    // ====== PESQUISAR POR ID ======
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> pesquisar(@PathVariable long id) throws Exception {
        // nome do seu DAO: pesquisar
        Paciente p = dao.pesquisar(id); // ajuste se sua assinatura for diferente
        if (p == null) {
            throw new EntidadeNaoEncontradaException("Paciente id=" + id + " não encontrado");
        }
        return ResponseEntity.ok(PacienteMapper.toResponse(p));
    }

    // ====== ATUALIZAR ======
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> atualizar(@PathVariable long id,
                                                         @RequestBody @Valid PacienteRequestDTO dto) throws Exception {
        Paciente existente = dao.pesquisar(id);
        if (existente == null) {
            throw new EntidadeNaoEncontradaException("Paciente id=" + id + " não encontrado");
        }
        PacienteMapper.copyToEntity(dto, existente);
        existente.setCodigo(id);
        dao.atualizar(existente); // DAO recebe Paciente e não retorna
        return ResponseEntity.ok(PacienteMapper.toResponse(existente));
    }

    // ====== REMOVER ======
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable long id) throws Exception {
        Paciente existente = dao.pesquisar(id);
        if (existente == null) {
            throw new EntidadeNaoEncontradaException("Paciente id=" + id + " não encontrado");
        }
        dao.remover(id); // nome do seu DAO: remover
        return ResponseEntity.noContent().build();
    }
}
