package com.example.expensetracker.controller;

import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExcelExportService excelExportService;

    // GET - List all expenses with filters
    @GetMapping
    public ResponseEntity<?> getAllExpenses(
            @RequestParam Long userId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String search) {

        try {
            List<Expense> expenses;

            if (startDate != null && endDate != null) {
                expenses = expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);
            } else if (category != null && !category.isEmpty()) {
                expenses = expenseRepository.findByUserIdAndCategory(userId, category);
            } else if (search != null && !search.isEmpty()) {
                expenses = expenseRepository.findByUserIdAndTitleContainingIgnoreCase(userId, search);
            } else {
                expenses = expenseRepository.findByUserId(userId);
            }

            return ResponseEntity.ok(expenses);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // POST - Add new expense
    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.parseLong(request.get("userId").toString());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Expense expense = new Expense();
            expense.setUser(user);
            expense.setTitle((String) request.get("title"));
            expense.setAmount(Double.parseDouble(request.get("amount").toString()));
            expense.setCategory((String) request.get("category"));
            expense.setPaymentMethod((String) request.get("paymentMethod"));
            expense.setExpenseDate(LocalDate.parse((String) request.get("expenseDate")));

            Expense saved = expenseRepository.save(expense);

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // PUT - Update expense
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Expense expense = expenseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Expense not found"));

            expense.setTitle((String) request.get("title"));
            expense.setAmount(Double.parseDouble(request.get("amount").toString()));
            expense.setCategory((String) request.get("category"));
            expense.setPaymentMethod((String) request.get("paymentMethod"));
            expense.setExpenseDate(LocalDate.parse((String) request.get("expenseDate")));

            Expense updated = expenseRepository.save(expense);

            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // DELETE - Delete expense
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        try {
            expenseRepository.deleteById(id);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // GET - Monthly summary
    @GetMapping("/summary/monthly")
    public ResponseEntity<?> getMonthlySummary(@RequestParam Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Double totalSpent = expenseRepository.getCurrentMonthTotal(userId);
            if (totalSpent == null) totalSpent = 0.0;

            List<Object[]> categoryData = expenseRepository.getCategoryTotals(userId);
            Map<String, Double> categoryTotals = new HashMap<>();
            for (Object[] row : categoryData) {
                categoryTotals.put((String) row[0], (Double) row[1]);
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("totalSpent", totalSpent);
            summary.put("categories", categoryTotals);
            summary.put("budget", user.getMonthlyBudget());
            summary.put("remaining", user.getMonthlyBudget() - totalSpent);
            summary.put("isOverBudget", totalSpent > user.getMonthlyBudget());

            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // GET - Export expenses to Excel
    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcel(@RequestParam Long userId) {
        try {
            List<Expense> expenses = expenseRepository.findByUserId(userId);

            ByteArrayInputStream in = excelExportService.exportExpensesToExcel(expenses);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=expenses_report.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new InputStreamResource(in));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}