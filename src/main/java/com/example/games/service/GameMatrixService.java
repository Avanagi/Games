package com.example.games.service;

import com.example.games.dto.GameMatrixDTO;
import com.example.games.entity.GameMatrixEntity;
import com.example.games.mapper.GameMatrixMapper;
import com.example.games.repository.GameMatrixRepository;
import com.example.games.utils.GameMatrixServiceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class GameMatrixService {

    private final GameMatrixRepository repository;
    private final GameMatrixMapper mapper;
    private final GameMatrixServiceUtils utils;

    public GameMatrixService(GameMatrixRepository repository, GameMatrixMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.utils = new GameMatrixServiceUtils();
    }

    @Transactional(readOnly = true)
    public List<GameMatrixDTO> getAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public GameMatrixDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(() -> new RuntimeException("Матрица не найдена"));
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

    @Transactional
    public String calculateMaximin(Long id, int player) {
        GameMatrixDTO matrix = getById(id);
        List<List<List<Integer>>> mtx = utils.parse(matrix.getMatrixData());
        StringBuilder process = new StringBuilder();

        if (player == 1) {
            process.append("Максимин для Игрока 1:\n");

            List<Integer> rowMinima = new ArrayList<>();
            for (int i = 0; i < mtx.size(); i++) {
                int rowMin = mtx.get(i).stream().mapToInt(cell -> cell.get(0)).min().orElse(0);
                rowMinima.add(rowMin);
                process.append("Строка ").append(i + 1).append(": минимум = ").append(rowMin).append("\n");
            }

            int maximin = rowMinima.stream().mapToInt(Integer::intValue).max().orElse(0);
            process.append("Максимальное значение среди минимумов: ").append(maximin).append("\n");
            return process.append("Результат: ").append(maximin).toString();
        } else {
            process.append("Максимин для Игрока 2:\n");

            List<Integer> colMinima = new ArrayList<>();
            List<List<List<Integer>>> transposed = utils.transpose(mtx);
            for (int j = 0; j < transposed.size(); j++) {
                int colMin = transposed.get(j).stream().mapToInt(cell -> cell.get(1)).min().orElse(0);
                colMinima.add(colMin);
                process.append("Столбец ").append(j + 1).append(": минимум = ").append(colMin).append("\n");
            }

            int maximin = colMinima.stream().mapToInt(Integer::intValue).max().orElse(0);
            process.append("Максимальное значение среди минимумов: ").append(maximin).append("\n");
            return process.append("Результат: ").append(maximin).toString();
        }
    }

    @Transactional
    public String calculateMinimax(Long id, int player) {
        GameMatrixDTO matrix = getById(id);
        List<List<List<Integer>>> mtx = utils.parse(matrix.getMatrixData());
        StringBuilder process = new StringBuilder();

        if (player == 1) {
            process.append("Минимакс для Игрока 1:\n");

            List<Integer> rowMaxima = new ArrayList<>();
            for (int i = 0; i < mtx.size(); i++) {
                int rowMax = mtx.get(i).stream().mapToInt(cell -> cell.get(0)).max().orElse(0);
                rowMaxima.add(rowMax);
                process.append("Строка ").append(i + 1).append(": максимум = ").append(rowMax).append("\n");
            }

            int minimax = rowMaxima.stream().mapToInt(Integer::intValue).min().orElse(0);
            process.append("Минимальное значение среди максимумов: ").append(minimax).append("\n");
            return process.append("Результат: ").append(minimax).toString();
        } else {
            process.append("Минимакс для Игрока 2:\n");

            List<Integer> colMaxima = new ArrayList<>();
            List<List<List<Integer>>> transposed = utils.transpose(mtx);
            for (int j = 0; j < transposed.size(); j++) {
                int colMax = transposed.get(j).stream().mapToInt(cell -> cell.get(1)).max().orElse(0);
                colMaxima.add(colMax);
                process.append("Столбец ").append(j + 1).append(": максимум = ").append(colMax).append("\n");
            }

            int minimax = colMaxima.stream().mapToInt(Integer::intValue).min().orElse(0);
            process.append("Минимальное значение среди максимумов: ").append(minimax).append("\n");
            return process.append("Результат: ").append(minimax).toString();
        }
    }

    @Transactional
    public String calculateSaddlePoint(Long id) {
        GameMatrixDTO matrix = getById(id);
        List<List<List<Integer>>> mtx = utils.parse(matrix.getMatrixData());
        StringBuilder process = new StringBuilder();
        process.append("Поиск седловой точки:\n");

        for (int i = 0; i < mtx.size(); i++) {
            for (int j = 0; j < mtx.get(0).size(); j++) {
                int a = mtx.get(i).get(j).get(0);
                int b = mtx.get(i).get(j).get(1);

                boolean isRowMin = mtx.get(i).stream().allMatch(cell -> a <= cell.get(0));
                boolean isColMax = utils.transpose(mtx).get(j).stream().allMatch(cell -> a >= cell.get(0));

                boolean isColMin = utils.transpose(mtx).get(j).stream().allMatch(cell -> b <= cell.get(1));
                boolean isRowMax = mtx.get(i).stream().allMatch(cell -> b >= cell.get(1));

                process.append("Ячейка (").append(i + 1).append(", ").append(j + 1)
                        .append("): ").append("(").append(a).append(", ").append(b)
                        .append(")\n").append("  Проверка: ").append("Минимум в строке = ")
                        .append(isRowMin).append(", Максимум в столбце = ").append(isColMax).append("\n");

                if (isRowMin && isColMax && isColMin && isRowMax) {
                    process.append("Седловая точка найдена: строка ").append(i + 1)
                            .append(", столбец ").append(j + 1).append(" со значением (")
                            .append(a).append(", ").append(b).append(").");

                    if (process.length() > 1000) {
                        return "Седловая точка найдена: строка " + (i + 1)
                                + ", столбец " + (j + 1) + " со значением (" + a + ", " + b + ").";
                    }
                    return process.toString();

                }
            }
        }

        process.append("Седловая точка отсутствует.\n");

        if (process.length() > 1000) {
            return "Седловая точка отсутствует, подробности слишком велики для отображения.";
        }

        return process.toString();
    }

    @Transactional
    public String calculateNashEquilibrium(Long id) {
        GameMatrixDTO matrix = getById(id);
        List<List<List<Integer>>> mtx = utils.parse(matrix.getMatrixData());
        StringBuilder process = new StringBuilder();
        process.append("Поиск равновесия по Нэшу в чистых стратегиях:\n");

        for (int i = 0; i < mtx.size(); i++) {
            for (int j = 0; j < mtx.get(0).size(); j++) {
                int player1Value = mtx.get(i).get(j).get(0);
                int player2Value = mtx.get(i).get(j).get(1);

                process.append("Проверка ячейки: строка ").append(i + 1).append(", столбец ").append(j + 1)
                        .append(" со значением (").append(player1Value).append(", ").append(player2Value).append(")\n");

                int finalJ = j;

                boolean isBestForPlayer1 = mtx.stream().allMatch(row -> player1Value >= row.get(finalJ).get(0));
                boolean isBestForPlayer2 = mtx.get(i).stream().allMatch(cell -> player2Value >= cell.get(1));

                if (isBestForPlayer1 && isBestForPlayer2) {
                    process.append("Найдено равновесие по Нэшу: строка ").append(i + 1).append(", столбец ")
                            .append(j + 1).append(" со значением (").append(player1Value).append(", ")
                            .append(player2Value).append(").\n");
                    if (process.length() > 1000) {
                        return "Найдено равновесие по Нэшу: строка " + (i + 1) + ", столбец " + (j + 1)
                                + " со значением (" + player1Value + ", " + player2Value + ").\n";
                    }
                    return process.toString();
                }
            }
        }

        process.append("Равновесие по Нэшу отсутствует.\n");
        if (process.length() > 1000) {
            return "Равновесие по Нэшу отсутствует, подробности слишком велики для отображения.";
        }
        return process.toString();
    }

    @Transactional
    public String calculateNashEquilibriumMixed(Long id) {
        GameMatrixDTO matrix = getById(id);
        List<List<List<Integer>>> mtx = utils.parse(matrix.getMatrixData());
        StringBuilder process = new StringBuilder("Поиск равновесия по Нэшу в смешанных стратегиях:\n");

        int a = mtx.get(0).get(0).get(0);
        int b = mtx.get(0).get(0).get(1);
        int c = mtx.get(0).get(1).get(0);
        int d = mtx.get(0).get(1).get(1);
        int e = mtx.get(1).get(0).get(0);
        int f = mtx.get(1).get(0).get(1);
        int g = mtx.get(1).get(1).get(0);
        int h = mtx.get(1).get(1).get(1);

        double q = (double) (h - f) / ((b - f) + (h - d));
        double p = (double) (g - e) / ((a - e) + (g - c));

        if (p < 0 || p > 1 || q < 0 || q > 1) {
            process.append("Решение в смешанных стратегиях не найдено (вероятности вне диапазона [0,1]).\n");
            return process.toString();
        }

        process.append("Равновесие Нэша в смешанных стратегиях:\n");
        process.append("Игрок 1: (" + String.format("%.2f", p)
                + ", " + String.format("%.2f", 1 - p) + ")\n");
        process.append("Игрок 2: (" + String.format("%.2f", q)
                + ", " + String.format("%.2f", 1 - q) + ")\n");
        return process.toString();
    }
}
