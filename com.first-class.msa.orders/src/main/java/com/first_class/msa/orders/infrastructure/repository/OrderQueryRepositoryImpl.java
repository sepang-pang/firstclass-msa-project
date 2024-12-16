package com.first_class.msa.orders.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.first_class.msa.orders.application.dto.ResOrderSearchDTO;
import com.first_class.msa.orders.application.dto.AuthSearchConditionDTO;
import com.first_class.msa.orders.domain.model.QOrder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	QOrder qOrder = QOrder.order;

	@Override
	public ResOrderSearchDTO findAll(AuthSearchConditionDTO authSearchConditionDTO, Pageable pageable) {

		List<ResOrderSearchDTO.OrderPage> results = jpaQueryFactory
			.select(Projections.constructor(
				ResOrderSearchDTO.OrderPage.class,
				qOrder.id,
				qOrder.address,
				qOrder.requestInfo.value,
				qOrder.orderTotalPrice.value
			))
			.from(qOrder)
			.where(
				deliveryOrderEquals(authSearchConditionDTO.getOrderIdList()),
				userIdEquals(authSearchConditionDTO.getUserId()),
				businessIdEquals(authSearchConditionDTO.getBusinessId()),
				hubIdEquals(authSearchConditionDTO.getHubId())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = Optional.ofNullable(
			jpaQueryFactory
				.select(qOrder.count())
				.from(qOrder)
				.where(
					deliveryOrderEquals(authSearchConditionDTO.getOrderIdList()),
					userIdEquals(authSearchConditionDTO.getUserId()),
					businessIdEquals(authSearchConditionDTO.getBusinessId()),
					hubIdEquals(authSearchConditionDTO.getHubId())
				)
				.fetchOne()
		).orElse(0L);

		Page<ResOrderSearchDTO.OrderPage> page = new PageImpl<>(results, pageable, total);
		return ResOrderSearchDTO.of(page);
	}


	private BooleanExpression deliveryOrderEquals(List<Long> orderIdList){
		return orderIdList != null ? qOrder.id.in(orderIdList) : null;
	}

	private BooleanExpression userIdEquals(Long userId) {
		return userId != null ? qOrder.userId.eq(userId): null;
	}

	private BooleanExpression businessIdEquals(Long businessId) {
		return businessId != null ? qOrder.businessId.eq(businessId) : null;
	}

	private BooleanExpression hubIdEquals(Long hubId) {
		return hubId != null ? qOrder.hubId.eq(hubId) : null;
	}

}
