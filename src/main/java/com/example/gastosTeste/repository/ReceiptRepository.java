package com.example.gastosTeste.repository;

import com.example.gastosTeste.dto.ReceiptDTO;
import com.example.gastosTeste.dto.ReceiptMonthlyReportDTO;
import com.example.gastosTeste.dto.ReceiptReportDTO;
import com.example.gastosTeste.model.Receipt;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    boolean existsByAcessKey(String acessKey);

    @Query("""
    SELECT new com.example.gastosTeste.dto.ReceiptMonthlyReportDTO(
        SUM(r.total),
        COUNT(r),
        MAX(r.total),
        MIN(r.total),
        AVG(r.total)
    )
    FROM Receipt r
    WHERE r.issueDate >= :startDate
      AND r.issueDate < :endDate
    """)
    ReceiptMonthlyReportDTO getMonthlyReport(@Param("startDate")LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT new com.example.gastosTeste.dto.ReceiptReportDTO(
            r.storeName,
            r.cnpj,
            r.issueDate,
            r.total)
            FROM Receipt r
            WHERE r.issueDate >= :startDate
            AND r.issueDate < :endDate
            ORDER BY r.issueDate DESC
            """)
    List<ReceiptReportDTO> findAllByIssueDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);
}
