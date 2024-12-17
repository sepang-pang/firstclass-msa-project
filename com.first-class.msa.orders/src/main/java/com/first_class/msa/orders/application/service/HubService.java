package com.first_class.msa.orders.application.service;

import org.springframework.web.bind.annotation.PathVariable;

import com.first_class.msa.orders.application.dto.ResHubDTO;

public interface HubService {

	Long getHubByID(@PathVariable(name = "userId") Long userId);



}
