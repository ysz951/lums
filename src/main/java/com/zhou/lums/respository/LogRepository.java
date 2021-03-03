package com.zhou.lums.respository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.zhou.lums.model.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
    @Query("SELECT l from Log l ORDER BY l.createdAt DESC")
    public List<Log> findAllOrderedByTimeDesc();

    @Query("SELECT l from Log l WHERE l.user.id = :arg1 ORDER BY l.createdAt DESC")
    public List<Log> findAllLogByUserIdLasestOrder(@Param("arg1") long userId);

    @Query("SELECT l from Log l WHERE l.user.id = :arg1 AND l.license.id = :arg2"
            + " ORDER BY l.createdAt DESC")
    public List<Log> findAllLogByUserIdAndLicenseId(@Param("arg1") long userId, @Param("arg2") long licenseId);

    @Query("SELECT l FROM Log l ORDER BY l.id")
    List<Log> findAllOrderedById();
}
