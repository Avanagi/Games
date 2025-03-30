package com.example.games.controller;

import com.example.games.dto.GameMatrixDTO;
import com.example.games.service.GameMatrixService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/matrices")
public class GameMatrixController {

    private final GameMatrixService service;

    public GameMatrixController(GameMatrixService service) {
        this.service = service;
    }

    @GetMapping
    public String listMatrices(Model model) {
        List<GameMatrixDTO> matrices = service.getAll();
        model.addAttribute("matrices", matrices);
        return "matrix/list";
    }

    @GetMapping("/{id}")
    public String viewMatrix(@PathVariable Long id, Model model,
                             @RequestParam(required = false) String result) throws Exception {

        GameMatrixDTO matrix = service.getById(id);
        ObjectMapper mapper = new ObjectMapper();
        List<List<List<Integer>>> parsedMatrix = mapper.readValue(matrix.getMatrixData(), List.class);

        model.addAttribute("matrix", matrix);
        model.addAttribute("matrixParsed", parsedMatrix);
        model.addAttribute("result", result);

        return "matrix/view";
    }

    @GetMapping("/new")
    public String newMatrixForm(Model model) {
        model.addAttribute("matrix", new GameMatrixDTO());
        return "matrix/form";
    }

    @PostMapping
    public String saveMatrix(@ModelAttribute("matrix") GameMatrixDTO dto, RedirectAttributes redirectAttributes) {
        service.save(dto);
        redirectAttributes.addFlashAttribute("success", "Матрица успешно сохранена");
        return "redirect:/matrices";
    }

    @PostMapping("/{id}/delete")
    public String deleteMatrix(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        service.delete(id);
        redirectAttributes.addFlashAttribute("success", "Матрица удалена");
        return "redirect:/matrices";
    }

    @PostMapping("/{id}/solve(maximin)")
    public String solveMaximin(@PathVariable Long id, @RequestParam int player, RedirectAttributes ra) {
        String result = service.calculateMaximin(id, player);
        ra.addAttribute("result", result);
        return "redirect:/matrices/{id}";
    }

    @PostMapping("/{id}/solve(minimax)")
    public String solveMinimax(@PathVariable Long id, @RequestParam int player, RedirectAttributes ra) {
        String result = service.calculateMinimax(id, player);
        ra.addAttribute("result", result);
        return "redirect:/matrices/{id}";
    }

    @PostMapping("/{id}/solve(saddle)")
    public String solveSaddle(@PathVariable Long id, RedirectAttributes ra) {
        String result = service.calculateSaddlePoint(id);
        ra.addAttribute("result", result);
        return "redirect:/matrices/{id}";
    }

    @PostMapping("/{id}/solve(nash)")
    public String solveNash(@PathVariable Long id, RedirectAttributes ra) {
        String result = service.calculateNashEquilibrium(id);
        ra.addAttribute("result", result);
        return "redirect:/matrices/{id}";
    }

    @PostMapping("/{id}/solve(nashMixed)")
    public String solveNashMixed(@PathVariable Long id, RedirectAttributes ra) {
        String result = service.calculateNashEquilibriumMixed(id);
        ra.addAttribute("result", result);
        return "redirect:/matrices/{id}";
    }

}
