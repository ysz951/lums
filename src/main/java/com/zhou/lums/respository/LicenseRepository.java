package com.zhou.lums.respository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.zhou.lums.model.License;
import com.zhou.lums.model.License.Duration;

public interface LicenseRepository extends JpaRepository<License, Long>{
//    @Query("SELECT v.poll.id FROM Vote v WHERE v.user.id = :userId")
//    Page<Long> findVotedPollIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT l from License l WHERE l.duration = :arg1")
    List<License> findAllOrderedByIdFilterByType(@Param("arg1") Duration duration);
//
    List<License> findAllOrderedById(long id);
}
