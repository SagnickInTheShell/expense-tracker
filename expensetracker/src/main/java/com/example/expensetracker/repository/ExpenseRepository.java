package com.example.expensetracker.repository;

import com.example.expensetracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND MONTH(e.expenseDate) = MONTH(CURRENT_DATE)")
    Double getCurrentMonthTotal(@Param("userId") Long userId);

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND MONTH(e.expenseDate) = MONTH(CURRENT_DATE) GROUP BY e.category")
    List<Object[]> getCategoryTotals(@Param("userId") Long userId);

    List<Expense> findByUserIdAndTitleContainingIgnoreCase(Long userId, String search);

    List<Expense> findByUserIdAndExpenseDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<Expense> findByUserIdAndCategory(Long userId, String category);
}