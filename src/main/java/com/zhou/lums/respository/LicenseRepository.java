package com.zhou.lums.respository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.zhou.lums.model.License;
import com.zhou.lums.model.License.Duration;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long>{
//    @Query("SELECT v.poll.id FROM Vote v WHERE v.user.id = :userId")
//    Page<Long> findVotedPollIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT l from License l WHERE l.duration = :arg1")
    List<License> findAllOrderedByIdFilterByType(@Param("arg1") Duration duration);

    @Query("SELECT l FROM License l ORDER BY l.id")
    List<License> findAllOrderedById();

    @Modifying
    @Query("Update License SET active = :arg1 WHERE id = :arg2")
    public int updateLicenseActive(@Param("arg1")boolean active, @Param("arg2")long id);
}
