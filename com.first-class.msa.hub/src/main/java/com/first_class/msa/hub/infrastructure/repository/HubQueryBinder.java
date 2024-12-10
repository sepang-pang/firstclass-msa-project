package com.first_class.msa.hub.infrastructure.repository;

import com.first_class.msa.hub.domain.model.Hub;
import com.first_class.msa.hub.domain.model.QHub;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface HubQueryBinder extends QuerydslPredicateExecutor<Hub>, QuerydslBinderCustomizer<QHub> {

    @Override
    default void customize(QuerydslBindings bindings, QHub qHub) {
        bindings.bind(String.class).all((StringPath path, Collection<? extends String> values) -> {
            List<String> valueList = new ArrayList<>(values.stream().map(String::trim).toList());
            if (valueList.isEmpty()) {
                return Optional.empty();
            }

            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for (String s : valueList) {
                booleanBuilder.or(path.containsIgnoreCase(s));
            }

            booleanBuilder.and(qHub.deletedAt.isNull());

            return Optional.of(booleanBuilder);
        });
    }
}
