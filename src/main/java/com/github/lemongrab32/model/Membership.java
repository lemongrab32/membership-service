package com.github.lemongrab32.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

	@Column("hours_remaining")
	private Integer hoursRemaining;

	@Column("is_active")
	private boolean isActive;

	@Column("tariff_id")
	private Integer tariffId;

}
