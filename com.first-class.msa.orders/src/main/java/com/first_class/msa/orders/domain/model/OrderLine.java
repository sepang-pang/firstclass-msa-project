package com.first_class.msa.orders.domain.model;

import java.time.LocalDateTime;

import com.first_class.msa.orders.domain.model.common.BaseTime;
import com.first_class.msa.orders.domain.model.valueobject.Count;
import com.first_class.msa.orders.domain.model.valueobject.SupplyPrice;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_order_lines")
public class OrderLine extends BaseTime {
	@Id
	@Tsid
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Embedded
	private Count count;

	@Embedded
	private SupplyPrice supplyPrice;

	public static OrderLine createOrderLine(Order order, Long productId, Count count, SupplyPrice supplyPrice) {
		return OrderLine.builder()
			.order(order)
			.productId(productId)
			.count(count)
			.supplyPrice(supplyPrice)
			.build();
	}

	public void deleteOrderLine(Long userId){
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(userId);
	}
}
