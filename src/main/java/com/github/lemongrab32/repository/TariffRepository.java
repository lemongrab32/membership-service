package com.github.lemongrab32.repository;

import com.github.lemongrab32.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Integer>,
	PagingAndSortingRepository<Tariff, Integer> {}
