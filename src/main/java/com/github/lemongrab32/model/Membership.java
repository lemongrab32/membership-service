package com.github.lemongrab32.model;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Сущность абонемента
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "membership")
public class Membership {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "final_price")
	private Double finalPrice;

	@Column(name = "client_id")
	private UUID clientId;

	@Column(name = "hours_remaining")
	private Integer hoursRemaining;

	@Column(name = "is_active")
	private boolean isActive;

	@Column(name = "tariff_id")
	private Integer tariffId;

}
