package com.first_class.msa.business.application.service;

import org.springframework.web.bind.annotation.PathVariable;

public interface HubService {

    boolean existsBy(@PathVariable Long hubId);

    Long getHubIdBy(@PathVariable Long userId);
}
