package com.sistema.pos.service;

import com.sistema.pos.entity.Categoria;
import com.sistema.pos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> findById(Long idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }


    public void deleteById(Long id) {
        categoriaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return categoriaRepository.existsById(id);
    }
}
