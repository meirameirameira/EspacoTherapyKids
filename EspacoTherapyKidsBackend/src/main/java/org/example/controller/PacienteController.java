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

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

    @GetMapping("/export")
    public void exportPacientesXlsx(HttpServletResponse resp) throws Exception {
        var lista = dao.listar();

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sh = wb.createSheet("Pacientes");
            int rowIdx = 0;

            CellStyle headerStyle = wb.createCellStyle();
            var bold = wb.createFont(); bold.setBold(true);
            headerStyle.setFont(bold);

            Row h = sh.createRow(rowIdx++);
            String[] cols = {
                    "Código", "Nome", "Nome Responsável", "Contato Responsável",
                    "Fono Preço", "Fono Horas", "Fono Reembolso", "Fono Total", "Fono NF",
                    "TO Preço", "TO Horas", "TO Reembolso", "TO Total", "TO NF",
                    "ABA Preço", "ABA Reembolso", "ABA NF"
            };
            for (int i=0;i<cols.length;i++) {
                Cell c = h.createCell(i);
                c.setCellValue(cols[i]);
                c.setCellStyle(headerStyle);
            }

            for (var p : lista) {
                Row r = sh.createRow(rowIdx++);

                // dados base
                int c=0;
                r.createCell(c++).setCellValue(p.getCodigo());
                r.createCell(c++).setCellValue(nvl(p.getNome()));
                r.createCell(c++).setCellValue(nvl(p.getNmResponsavel()));
                r.createCell(c++).setCellValue(p.getNrResponsavel() == null ? "" : String.valueOf(p.getNrResponsavel()));

                // Fono
                var f = p.getFono();
                r.createCell(c++).setCellValue(f != null ? f.getPreco() : 0);
                r.createCell(c++).setCellValue(f != null ? f.getHoras() : 0);
                r.createCell(c++).setCellValue(f != null ? f.getReembolsoInformado() : 0);
                r.createCell(c++).setCellValue(f != null ? f.calcularTotal() : 0);
                r.createCell(c++).setCellValue(f != null ? f.calcularNF() : 0);

                // TO
                var t = p.getTerapiaOcupacional();
                r.createCell(c++).setCellValue(t != null ? t.getPreco() : 0);
                r.createCell(c++).setCellValue(t != null ? t.getHoras() : 0);
                r.createCell(c++).setCellValue(t != null ? t.getReembolsoInformado() : 0);
                r.createCell(c++).setCellValue(t != null ? t.calcularTotal() : 0);
                r.createCell(c++).setCellValue(t != null ? t.calcularNF() : 0);

                // ABA
                var a = p.getAba();
                r.createCell(c++).setCellValue(a != null ? a.getPreco() : 0);
                r.createCell(c++).setCellValue(a != null ? a.getReembolsoInformado() : 0);
                r.createCell(c++).setCellValue(a != null ? a.calcularNF() : 0);
            }

            // Ajusta largura das colunas
            for (int i=0;i<cols.length;i++) sh.autoSizeColumn(i);

            // Resposta
            resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            resp.setHeader("Content-Disposition", "attachment; filename=\"pacientes.xlsx\"");
            wb.write(resp.getOutputStream());
            resp.flushBuffer();
        }
    }

    // helper simples
    private String nvl(String s) { return s == null ? "" : s; }
}
