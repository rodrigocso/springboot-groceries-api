package com.rodrigocso.groceries.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

public class PurchaseDto {
    private Long id;

    @NotNull(message = "IS_REQUIRED")
    private Long storeId;

    @NotNull(message = "IS_REQUIRED")
    @PastOrPresent(message = "NO_FUTURE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    @NotNull(message = "IS_REQUIRED")
    private Long itemId;

    @NotNull(message = "IS_REQUIRED")
    @Positive(message ="REQUIRES_GREATER_THAN_ZERO")
    private Float quantity;

    @NotNull(message = "IS_REQUIRED")
    @Positive(message = "REQUIRES_GREATER_THAN_ZERO")
    private Float price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
