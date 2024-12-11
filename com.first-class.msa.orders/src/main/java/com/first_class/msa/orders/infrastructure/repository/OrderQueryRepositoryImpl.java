package com.first_class.msa.orders.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.domain.model.QOrder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	QOrder qOrder = QOrder.order;

	// TODO: 2024-12-11 hubId, businessId 로직 추가
	@Override
	public ResOrderSearchDTO findAll(Long userId, Pageable pageable) {

		List<ResOrderSearchDTO.OrderPage> results = jpaQueryFactory
			.select(Projections.constructor(
				ResOrderSearchDTO.OrderPage.class,
				qOrder.id,
				qOrder.requestInfo.value,
				qOrder.orderTotalPrice.value
			))
			.from(qOrder)
			.where(userIdEquals(userId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = Optional.ofNullable(
			jpaQueryFactory
				.select(qOrder.count())
				.from(qOrder)
				.where(userIdEquals(userId))
				.fetchOne()
		).orElse(0L);

		Page<ResOrderSearchDTO.OrderPage> page = new PageImpl<>(results, pageable, total);
		return ResOrderSearchDTO.of(page);
	}

	private BooleanExpression userIdEquals(Long userId) {
		return userId != null ? qOrder.userId.eq(userId): null;
	}

}
