package com.zhou.lums.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.zhou.lums.exception.ResourceNotFoundException;
import com.zhou.lums.model.Sale;
import com.zhou.lums.payload.ApiResponse;
import com.zhou.lums.respository.SaleRepository;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    public ResponseEntity<?> changeSaleActive(long saleId, boolean newActive) {
        Sale sale = saleRepository
                .findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", saleId));
        sale.setActive(newActive);
        saleRepository.save(sale);

        return ResponseEntity.ok(new ApiResponse(true, "Change sale active"));
    }

    public ResponseEntity<?> changeSaleExpiration(long saleId, LocalDate expireDate) {
        Sale sale = saleRepository
                .findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", saleId));
        sale.setExpireDate(expireDate);
        saleRepository.save(sale);
        return ResponseEntity.ok(new ApiResponse(true, "Change sale exipration"));
    }

    public ResponseEntity<?> checkSaleExpiration() {
        List<Sale> saleList = saleRepository.findAll();
        LocalDate now = LocalDate.now();
        for (Sale sale : saleList) {
            if (sale.getExpireDate() != null && sale.getExpireDate().compareTo(now) < 0) {
                sale.setActive(false);
                saleRepository.save(sale);
            }
        }
        return ResponseEntity.ok(new ApiResponse(true, "Check sale exipration"));
    }

}
