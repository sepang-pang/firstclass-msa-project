package com.first_class.msa.delivery.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.first_class.msa.delivery.domain.model.QBusinessDeliveryRoute;
import com.first_class.msa.delivery.domain.model.QDelivery;
import com.first_class.msa.delivery.domain.model.QHubDeliveryRoute;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryQueryRepositoryImpl implements DeliveryQueryRepository{

	private final JPAQueryFactory jpaQueryFactory;

	QDelivery qDelivery = QDelivery.delivery;
	QBusinessDeliveryRoute qBusinessDeliveryRoute = QBusinessDeliveryRoute.businessDeliveryRoute;
	QHubDeliveryRoute qHubDeliveryRoute = QHubDeliveryRoute.hubDeliveryRoute;

	public List<Long> findOrderIdsByAgentId(Long agentId) {
		BooleanExpression businessAgentCondition = qBusinessDeliveryRoute.deliveryAgentId.eq(agentId);
		BooleanExpression hubAgentCondition = qHubDeliveryRoute.hubAgentId.eq(agentId);


		return jpaQueryFactory.select(qDelivery.orderId)
			.from(qDelivery)
			.leftJoin(qDelivery.businessDeliveryRoute, qBusinessDeliveryRoute)
			.leftJoin(qDelivery.hubDeliveryRouteList, qHubDeliveryRoute)
			.where(businessAgentCondition.or(hubAgentCondition))
			.distinct()
			.fetch();
	}

}
