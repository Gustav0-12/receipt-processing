package com.example.gastosTeste.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_receipt")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String acessKey;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private LocalDateTime issueDate;

    @Column(nullable = false)
    private BigDecimal total;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceiptItem> items = new ArrayList<>();

    public Receipt() {

    }

    public Receipt(Long id, String acessKey, String storeName, String cnpj, LocalDateTime issueDate, BigDecimal total, List<ReceiptItem> items) {
        this.id = id;
        this.acessKey = acessKey;
        this.storeName = storeName;
        this.cnpj = cnpj;
        this.issueDate = issueDate;
        this.total = total;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public String getAcessKey() {
        return acessKey;
    }

    public void setAcessKey(String acessKey) {
        this.acessKey = acessKey;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCnpj() { return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    public void addItem(ReceiptItem item) {
        this.items.add(item);
        item.setReceipt(this);
    }

}
