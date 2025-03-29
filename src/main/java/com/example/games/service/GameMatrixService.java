package com.example.games.service;

import com.example.games.dto.GameMatrixDTO;
import com.example.games.entity.GameMatrixEntity;
import com.example.games.mapper.GameMatrixMapper;
import com.example.games.repository.GameMatrixRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameMatrixService {

    private final GameMatrixRepository repository;
    private final GameMatrixMapper mapper;

    public GameMatrixService(GameMatrixRepository repository, GameMatrixMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<GameMatrixDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public GameMatrixDTO getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Матрица не найдена"));
    }

    @Transactional
    public GameMatrixDTO save(GameMatrixDTO dto) {
        dto.setRows(dto.getRows());
        dto.setColumns(dto.getColumns());
        GameMatrixEntity saved = repository.save(mapper.toEntity(dto));
        return mapper.toDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
