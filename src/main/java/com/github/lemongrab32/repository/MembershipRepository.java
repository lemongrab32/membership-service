package com.github.lemongrab32.repository;

import com.github.lemongrab32.model.Membership;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends CrudRepository<Membership, Long> {
}
