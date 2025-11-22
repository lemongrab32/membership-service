package com.github.lemongrab32.repository;

import com.github.lemongrab32.model.Tariff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TariffRepository extends CrudRepository<Tariff, Integer> {

	Page<Tariff> findAll(Pageable pageable);

}
