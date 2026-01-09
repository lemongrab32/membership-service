package com.github.lemongrab32.repository;

import com.github.lemongrab32.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для взаимодействия с хранилищем абонементов
 */
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
}
