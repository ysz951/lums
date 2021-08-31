package com.zhou.lums.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.zhou.lums.exception.ResourceNotFoundException;
import com.zhou.lums.model.Sale;
import com.zhou.lums.payload.ApiResponse;
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
        return saleRepository.findAllOrderedById();
    }

    @GetMapping("/sale/user")
    public List<Sale> getAllSaleByUserId(@RequestParam("userId") long userId) {
        return saleRepository.findAllByUserId(userId);
    }

    @GetMapping("/sale/{saleId}")
    public Sale getSaleById(@PathVariable("saleId") long saleId) {
        System.out.println(saleRepository.findById(saleId).get().getExpireDate());
        return saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", saleId));
    }

    @PostMapping("/sale")
    public ResponseEntity<?> createNewSate(@Valid @RequestBody Sale sale) {
        sale.setActive(true);
        Sale result = saleRepository.save(sale);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/sale/{saleId}")
                .buildAndExpand(result.getId()).toUri();
        System.out.println(location.toString());
        return ResponseEntity.created(location).body(new ApiResponse(true, "Sale created successfully"));
    }

    @PutMapping("/sale/active/{saleId}/{newActive}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<?> changeSaleActive(
            @PathVariable("saleId") long saleId,
            @PathVariable("newActive") boolean newActive) {
        return saleService.changeSaleActive(saleId, newActive);
    }

    @PutMapping("sale/{saleId}/expirationdate")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<?> changeSaleExpiration(
            @PathVariable("saleId") long saleId,
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("day") int day) {

        LocalDate expireDate = LocalDate.of(year, month, day);
        return saleService.changeSaleExpiration(saleId, expireDate);
    }

    @PutMapping("sale/expiration")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<?> checkSaleExpiration() {
        return saleService.checkSaleExpiration();
    }

    @DeleteMapping("/sale/{saleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERUSER')")
    public ResponseEntity<?> deleteSale(@PathVariable("saleId") long saleId) {
        return saleService.deleteSale(saleId);
    }

}