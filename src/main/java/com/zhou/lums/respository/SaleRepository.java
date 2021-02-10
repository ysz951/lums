package com.zhou.lums.respository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.zhou.lums.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    //    @Query("SELECT c FROM Card c where c.user.id = :userId")
    //    List<Card> findByUserId(@Param("userId") Long userId);
    @Query("SELECT s from Sale s ORDER BY s.purchasedDate DESC")
    List<Sale> findAllOrderedByPurchasedDate();
}
