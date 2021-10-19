package com.info5059.casestudy.purchaseorder;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
@Entity
@Data
@RequiredArgsConstructor
public class PurchaseOrderLineitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long poid;
    private String productid;
    private int qty;
    private BigDecimal price;
}