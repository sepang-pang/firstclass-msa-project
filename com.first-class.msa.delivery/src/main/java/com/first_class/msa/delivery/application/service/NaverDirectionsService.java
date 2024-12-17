package com.first_class.msa.delivery.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.first_class.msa.delivery.application.dto.ResNaverRouteInfoDTO;
import com.first_class.msa.delivery.libs.exception.ApiException;
import com.first_class.msa.delivery.libs.message.ErrorMessage;

@Service
public class NaverDirectionsService {

	@Value("${naver.api.client-id}")
	private String clientId;

	@Value("${naver.api.client-secret}")
	private String clientSecret;

	@Value("${naver.api.directions-url}")
	private String directionsUrl;

	@Value("${naver.api.geocoding-url}")
	private String geocodingUrl;

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public ResNaverRouteInfoDTO getRouteInfo(double startLat, double startLng, String destinationAddress) {
		// 1. 도착지 주소 -> 좌표 변환
		String goalCoordinates = getCoordinatesFromAddress(destinationAddress);
		if (goalCoordinates == null) {
			throw new IllegalArgumentException(new ApiException(ErrorMessage.NOT_FOUND_ADDRESS));
		}

		// 2. Directions API 호출
		String url = UriComponentsBuilder.fromHttpUrl(directionsUrl)
			.queryParam("start", startLng + "," + startLat) // 시작 좌표 (경도,위도)
			.queryParam("goal", goalCoordinates) // 도착 좌표 (경도,위도)
			.queryParam("option", "trafast") // 빠른 경로 옵션
			.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
		headers.set("X-NCP-APIGW-API-KEY", clientSecret);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
			url, org.springframework.http.HttpMethod.GET, entity, String.class);

		return parseRouteInfo(response.getBody());
	}

	private String getCoordinatesFromAddress(String address) {
		String url = geocodingUrl + "?query=" + address;

		System.out.println("Request URL: " + url);

		// 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-NCP-APIGW-API-KEY-ID", clientId);  // 클라이언트 ID
		headers.set("X-NCP-APIGW-API-KEY", clientSecret); // 클라이언트 시크릿
		headers.set("Accept", "application/json");        // JSON 응답 명시

		// 요청 엔터티 생성
		HttpEntity<String> entity = new HttpEntity<>(headers);

		// RestTemplate 요청
		ResponseEntity<String> response = restTemplate.exchange(
			url, org.springframework.http.HttpMethod.GET, entity, String.class);

		// 응답 로그 출력
		System.out.println("HTTP Status Code: " + response.getStatusCode());
		System.out.println("Response Body: " + response.getBody());

		return parseCoordinates(response.getBody());
	}

	private String parseCoordinates(String response) {
		try {
			JsonNode root = objectMapper.readTree(response);
			JsonNode addresses = root.path("addresses");
			if (addresses.isArray() && addresses.size() > 0) {
				JsonNode firstAddress = addresses.get(0);
				String x = firstAddress.path("x").asText(); // 경도
				String y = firstAddress.path("y").asText(); // 위도
				return x + "," + y;
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse geocoding response", e);
		}
		return null;
	}

	private ResNaverRouteInfoDTO parseRouteInfo(String response) {
		try {
			JsonNode root = objectMapper.readTree(response);
			JsonNode route = root.path("route").path("trafast").get(0);
			long distance = route.path("summary").path("distance").asLong(); // 거리
			long duration = route.path("summary").path("duration").asLong(); // 시간
			return new ResNaverRouteInfoDTO(distance, duration);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse route response", e);
		}
	}

}
