package com.first_class.msa.business.infrastructure.repository;

import com.first_class.msa.business.domain.model.Business;
import com.first_class.msa.business.domain.model.BusinessType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.first_class.msa.business.domain.model.QBusiness.business;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class BusinessQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Business> findBusinessByDeletedAtIsNullWithConditions(Pageable pageable, String name, String address, String type, String sort) {

        List<Business> results = queryFactory
                .selectFrom(business)
                .where(
                        business.deletedAt.isNull(),
                        businessNameLike(name),
                        addressLike(address),
                        typeLike(type)
                )
                .orderBy(
                        orderSpecifier(sort)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(business.count())
                .from(business)
                .where(
                        business.deletedAt.isNull(),
                        businessNameLike(name),
                        addressLike(address),
                        typeLike(type)
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression businessNameLike(String businessName) {
        return hasText(businessName) ? business.name.containsIgnoreCase(businessName) : null;
    }

    private BooleanExpression addressLike(String address) {
        return hasText(address) ? business.address.containsIgnoreCase(address) : null;
    }

    private BooleanExpression typeLike(String type) {
        return hasText(type) ? business.type.eq(BusinessType.fromLabel(type)) : null;
    }

    private OrderSpecifier<?> orderSpecifier(String sort) {
        return switch (sort) {
            case "OLDEST" -> business.createdAt.asc();
            case "NAME_ASC" -> business.name.asc();
            case "NAME_DESC" -> business.name.desc();
            default -> business.createdAt.desc();
        };
    }
}
