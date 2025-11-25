package com.github.lemongrab32.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table("membership")
public class Membership {

	@Id
	private Long id;

	@Column("start_date")
	private LocalDate startDate;

	@Column("end_date")
	private LocalDate endDate;

	@Column("final_price")
	private Double finalPrice;

	@Column("client_id")
	private UUID clientId;

	@Column("tariff_id")
	private Integer tariffId;

}
