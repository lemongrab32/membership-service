package com.github.lemongrab32.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table("tariff")
public class Tariff {

	@Id
	private Integer id;

	private String name;

	@Column("base_price")
	private Double basePrice;

	@Column("client_category")
	private ClientCategory clientCategory;

	@Column("client_type")
	private ClientType clientType;

}


