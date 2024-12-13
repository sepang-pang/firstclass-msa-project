package com.first_class.msa.agent.infratructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.first_class.msa.agent.application.dto.DeliveryAgentAuthSearchConditionDTO;
import com.first_class.msa.agent.application.dto.ResDeliveryAgentSearchDTO;
import com.first_class.msa.agent.domain.common.IsAvailable;
import com.first_class.msa.agent.domain.common.Type;
import com.first_class.msa.agent.domain.entity.QDeliveryAgent;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryAgentQueryRepositoryImpl implements DeliveryAgentQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	QDeliveryAgent qDeliveryAgent = QDeliveryAgent.deliveryAgent;

	@Override
	public ResDeliveryAgentSearchDTO findByAllDeliveryAgent(
		DeliveryAgentAuthSearchConditionDTO deliveryAgentAuthSearchConditionDTO,
		Pageable pageable
	) {

		List<ResDeliveryAgentSearchDTO.DeliveryAgentDetailDTO> result =
			jpaQueryFactory.query().select(
					Projections.constructor(
						ResDeliveryAgentSearchDTO
							.DeliveryAgentDetailDTO.class,
						qDeliveryAgent.id,
						qDeliveryAgent.userId,
						qDeliveryAgent.hubId,
						qDeliveryAgent.slackId,
						qDeliveryAgent.type,
						qDeliveryAgent.sequence
					)
				)
				.from(qDeliveryAgent)
				.where(
					typeEquals(deliveryAgentAuthSearchConditionDTO.getType()),
					isAvailableEquals(deliveryAgentAuthSearchConditionDTO.getIsAvailable()),
					hubIdEquals(deliveryAgentAuthSearchConditionDTO.getHubId())
					)
				.orderBy(getOrderSpecifierStore(pageable))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		long total = Optional.ofNullable(
			jpaQueryFactory
				.select(qDeliveryAgent.count())
				.from(qDeliveryAgent)
				.where(
					typeEquals(deliveryAgentAuthSearchConditionDTO.getType()),
					isAvailableEquals(deliveryAgentAuthSearchConditionDTO.getIsAvailable()),
					hubIdEquals(deliveryAgentAuthSearchConditionDTO.getHubId())
				)
				.fetchOne()
		).orElse(0L);

		Page<ResDeliveryAgentSearchDTO.DeliveryAgentDetailDTO> page
			= new PageImpl<>(result, pageable, total);
		return ResDeliveryAgentSearchDTO.from(page);
	}



	private BooleanExpression typeEquals(Type type) {
		return type != null ? qDeliveryAgent.type.eq(type) : null;
	}
	private BooleanExpression isAvailableEquals(IsAvailable isAvailable) {
		return isAvailable != null ? qDeliveryAgent.isAvailable.eq(isAvailable) : null;
	}


	private BooleanExpression hubIdEquals(Long hubId) {
		return hubId != null ? qDeliveryAgent.hubId.eq(hubId) : null;
	}

	private OrderSpecifier<?>[] getOrderSpecifierStore(Pageable pageable) {
		return pageable
			.getSort()
			.stream()
			.map(order -> {
				PathBuilder pathBuilder = new PathBuilder<>(
					QDeliveryAgent.deliveryAgent.getType(), QDeliveryAgent.deliveryAgent.getMetadata()
				);
				return new OrderSpecifier(
					order.isAscending() ? Order.ASC : Order.DESC,
					pathBuilder.get(order.getProperty()));
			}).toArray(OrderSpecifier[]::new);
	}
}
