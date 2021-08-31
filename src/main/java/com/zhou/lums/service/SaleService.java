package com.zhou.lums.service;

import com.zhou.lums.exception.ResourceNotFoundException;
import com.zhou.lums.model.Sale;
import com.zhou.lums.payload.ApiResponse;
import com.zhou.lums.respository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    public ResponseEntity<?> changeSaleActive(long saleId, boolean newActive) {
        if (saleRepository.updateSaleActive(newActive, saleId) == 0)
            throw new ResourceNotFoundException("Sale", "id", saleId);
        return ResponseEntity.ok(new ApiResponse(true, "Change sale active"));
    }

    public ResponseEntity<?> changeSaleExpiration(long saleId, LocalDate expireDate) {
        if (saleRepository.updateSaleExpire(expireDate, saleId) == 0)
            throw new ResourceNotFoundException("Sale", "id", saleId);
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

    public ResponseEntity<?> deleteSale(long saleId) {
        saleRepository.deleteById(saleId);
        return ResponseEntity.ok(new ApiResponse(true, "Sale is deleted"));
    }
}
