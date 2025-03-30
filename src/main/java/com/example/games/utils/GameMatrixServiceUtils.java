package com.example.games.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class GameMatrixServiceUtils {

    public List<List<List<Integer>>> parse(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, List.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения матрицы");
        }
    }

    public List<List<List<Integer>>> transpose(List<List<List<Integer>>> matrix) {
        int cols = matrix.get(0).size();
        List<List<List<Integer>>> transposed = new ArrayList<>();

        for (int j = 0; j < cols; j++) {
            List<List<Integer>> col = new ArrayList<>();
            for (List<List<Integer>> lists : matrix) {
                col.add(lists.get(j));
            }
            transposed.add(col);
        }
        return transposed;
    }

}
