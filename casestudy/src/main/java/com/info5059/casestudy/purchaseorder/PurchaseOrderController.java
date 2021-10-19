package com.info5059.casestudy.purchaseorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@RestController

public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderDAO poDAO;
    @Autowired
    private PurchaseOrderRepository poRepository;

    // get all the purcahse order
    @GetMapping("/api/pos")
    public ResponseEntity<Iterable<PurchaseOrder>> findAll() {
        Iterable<PurchaseOrder> pos = poRepository.findAll();
        return new ResponseEntity<Iterable<PurchaseOrder>>(pos, HttpStatus.OK);
    }

    @PostMapping("/api/pos")
    public ResponseEntity<Long> addOne(@RequestBody PurchaseOrder clientpo) { // use RequestBody here
        Long poid = poDAO.create(clientpo);
        return new ResponseEntity<Long>(poid, HttpStatus.OK);
    }

}
