package com.first_class.msa.product.infrastructure.repository;

import com.first_class.msa.product.domain.model.Product;
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
import java.util.Map;

import static com.first_class.msa.product.domain.model.QProduct.product;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Product> findProductByDeletedAtIsNullWithConditions(Long hubId, Pageable pageable, String name, Integer minPrice, Integer maxPrice, String sort) {

        List<Product> results = queryFactory
                .selectFrom(product)
                .where(
                        product.deletedAt.isNull(),
                        hubIdEq(hubId),
                        productNameLike(name),
                        productPriceGoe(minPrice),
                        productPriceLoe(maxPrice)
                )
                .orderBy(
                        orderSpecifier(sort)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        product.deletedAt.isNull(),
                        hubIdEq(hubId),
                        productNameLike(name),
                        productPriceGoe(minPrice),
                        productPriceLoe(maxPrice)
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression hubIdEq(Long hubId) {
        return hubId != null ? product.hubId.eq(hubId) : null;
    }

    private BooleanExpression productNameLike(String productName) {
        return hasText(productName) ? product.name.containsIgnoreCase(productName) : null;
    }

    private BooleanExpression productPriceGoe(Integer minPrice) {
        return (minPrice != null) ? product.price.goe(minPrice) : null;
    }

    private BooleanExpression productPriceLoe(Integer maxPrice) {
        return (maxPrice != null) ? product.price.loe(maxPrice) : null;
    }

    private static final Map<String, OrderSpecifier<?>> SORT_OPTIONS = Map.of(
            "NAME_ASC", product.name.asc(),
            "NAME_DESC", product.name.desc(),
            "PRICE_ASC", product.price.asc(),
            "PRICE_DESC", product.price.desc(),
            "OLDEST", product.createdAt.asc(),
            "NEWEST", product.createdAt.desc()
    );

    private OrderSpecifier<?> orderSpecifier(String sort) {
        return SORT_OPTIONS.getOrDefault(sort, product.createdAt.desc());
    }
}
