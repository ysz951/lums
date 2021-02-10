package com.zhou.lums.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.zhou.lums.model.Sale;
import com.zhou.lums.respository.SaleRepository;
import com.zhou.lums.service.SaleService;

@RestController
@RequestMapping("/api")
public class SaleController {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleService saleService;

    @GetMapping("/sale")
    public List<Sale> getAllSale() {
        return saleRepository.findAllOrderedByPurchasedDate();
    }

    @PostMapping("/sale")
    public Sale createNewSate(@Valid @RequestBody Sale sale) {
        sale.setActive(true);
        saleRepository.save(sale);
        return sale;
    }

    @PutMapping("/sale/{saleId}")
    public ResponseEntity<?> changeSaleActive(
            @PathVariable("saleId") long saleId,
            @RequestParam("new_active") boolean newActive) {
        return saleService.changeSaleActive(saleId, newActive);
    }
}