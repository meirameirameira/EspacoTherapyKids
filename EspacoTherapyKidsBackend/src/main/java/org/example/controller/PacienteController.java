// src/main/java/org/example/controller/PacienteController.java
package org.example.controller;

import org.example.dao.PacienteDao;
import org.example.exception.EntidadeNaoEncontradaException;
import org.example.model.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteDao pacienteDao;

    @Autowired
    public PacienteController(PacienteDao pacienteDao) {
        this.pacienteDao = pacienteDao;
    }

    @GetMapping
    public List<Paciente> listar() {
        try {
            return pacienteDao.listar();
        } catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao listar pacientes", e);
        }
    }

    @GetMapping("/{id}")
    public Paciente buscar(@PathVariable long id) {
        try {
            return pacienteDao.pesquisar(id);
        } catch (EntidadeNaoEncontradaException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar paciente", e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Paciente criar(@RequestBody Paciente p) {
        try {
            pacienteDao.cadastrar(p);
            return p;
        } catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar paciente", e);
        }
    }

    @PutMapping("/{id}")
    public Paciente atualizar(@PathVariable long id, @RequestBody Paciente p) {
        try {
            p.setCodigo(id);
            pacienteDao.atualizar(p);
            return p;
        } catch (EntidadeNaoEncontradaException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar paciente", e);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable long id) {
        try {
            pacienteDao.remover(id);
        } catch (EntidadeNaoEncontradaException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao remover paciente", e);
        }
    }
}
