package com.zhou.lums.respository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.zhou.lums.model.Sale;
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    //    @Query("SELECT c FROM Card c where c.user.id = :userId")
    //    List<Card> findByUserId(@Param("userId") Long userId);
    @Query("SELECT s from Sale s ORDER BY s.purchasedDate DESC")
    List<Sale> findAllOrderedByPurchasedDate();

    @Modifying
    @Query("Update Sale SET active = :arg1 WHERE id = :arg2")
    public int updateSaleActive(@Param("arg1")boolean active, @Param("arg2")long id);

    @Modifying
    @Query("Update Sale SET expire_date = :arg1 WHERE id = :arg2")
    public int updateSaleExpire(@Param("arg1")LocalDate expireDate, @Param("arg2")long id);
}
