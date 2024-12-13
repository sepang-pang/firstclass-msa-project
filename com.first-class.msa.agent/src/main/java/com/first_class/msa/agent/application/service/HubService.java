package com.first_class.msa.agent.application.service;

public interface HubService {
	boolean existsBy(Long hubId);

	Long getHubIdBy(Long userId);

}
