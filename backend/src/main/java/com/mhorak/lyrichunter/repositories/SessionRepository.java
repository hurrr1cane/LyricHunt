package com.mhorak.lyrichunter.repositories;

import com.mhorak.lyrichunter.models.Session;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Session s WHERE s.createdAt < :cutoff")
    void deleteOldSessions(LocalDateTime cutoff);
}
