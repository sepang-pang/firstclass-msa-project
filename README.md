# :package::First Class::package:
## 프로젝트 소개 및 목적
- B2B 물류 관리 및 배송 시스템을 개발했습니다.<br><br>
- 저희 물류는 각 지역에 허브센터를 가지고 있으며 각 허브 센터는 여러 업체의 물건을 보관합니다.<br><br>
- 업체의 상품은 허브에서 필요한 경우 바로 허브로 전달되며 해당 상품의 배송 요청이 들어오면 목적지 허브로 물품을 이동시켜 목적지에 배송합니다.<br><br>
- 시스템의 구조는 다음과 같습니다.
    - **도별 공급 허브**: 시스템은 허브를 가지고 있으며, 이 허브는 해당 도 내의 모든 주문, 재고 관리, 물류 운영을 담당합니다.
    - **플라스틱 가공 업체의 역할**:  <br>
      ex)경기도 일산의 플라스틱 가공 업체: 이 업체는 경기도에 위치한 플라스틱 가공품 생산 업체입니다.
    - **주문 발생:**<br>
    ex)부산에 위치한 바구니 제작 업체는 스파르타 물류로 연락하여 1000개의 플라스틱 가공품을 주문합니다. 이에 따라 저희 물류는 허브에 저장된 가공품의 배송을 합니다.
    - **물류 처리 및 재고 이동:**<br>
    ex)경기도 허브에서 부산시 허브로 물품 이동: 시스템은 주문을 처리하기 위해, 경기도 허브에 저장된 플라스틱 가공품을 부산시 허브로 이동 시키도록 계획합니다. 이 과정에서 허브 간 이동 경로에 따라, 물품이 부산시 허브로 안전하게 전달됩니다.<br><br>
- 모놀리식 구조가 아닌 MSA 구조로 애플리케이션을 구성하고 서로 호출하면서 협력했으며 이를 통해 실무에서 발생할 수 있는 다양한 상황을 경험해보고자 했습니다.<br><br>

## 팀원 구성
<div align="center">

 | 오세창 | 정성호 | 장윤지 | 
 |  :------: | :------: | :------: | 
|[<img width="98" src="https://github.com/user-attachments/assets/ba7beecb-29c6-49cb-a29a-ab852bf720f8"> <br> @sepang-pang](https://github.com/sepang-pang)|[<image width="98" src="https://github.com/user-attachments/assets/61cb3307-2e91-4f97-90e0-b74b6420303e"><br> @jshstar](https://github.com/jshstar)|[<img width="98" src="https://github.com/user-attachments/assets/e7ecc97f-f7b3-4180-91d1-5afe86f243d5"> <br>@elliaaa](https://github.com/elliaaa)|
</div><br>

## 1. 개발 환경
- 프로젝트 개발 환경 : IntelliJ
- 사용 기술 : Spring Boot,Cloud(Eureka Client,Feign Client),QueryDSL,RabbitMQ를 통한 비동기 메시징
- 버전 관리 : Git
- 협업 툴 : Slack, Notion
- 데이터베이스: PostgreSQL
- 캐싱: Redis
- 테스트: JUnit 5
- 보안: Spring Security, JWT
<br><br>
  
## 2. 개발 기간 및 작업 관리
- 2024-12-5 ~ 2024-12-17

## 3. ERD 
![image](https://github.com/user-attachments/assets/95a74aa7-2135-432c-af78-f73dbe2fd2e3) <br>
## 4. 아키텍쳐



## 5. 역할 분담
- 오세창 : 허브 업체상품 (상품 관리 / 업체 관리, 허브 관리 / 허브 이동경로)
- 정성호 : 주문 배송 배송관리자 (주문 관리, 배송 경로 / 배송, 관리자 )
- 장윤지 : 게이트웨이 어스 메시지 (사용자관리, 슬랙 / ai)

## 6. 실행방법

- auth-service

| **기능**              | **HTTP Method** | **엔드포인트**           | **설명**                                      |
|-----------------------|-----------------|--------------------------|---------------------------------------------|
| **회원가입**           | `POST`          | `/auth/sign-up`          | 회원 정보를 받아 신규 회원 생성              |
| **로그인**             | `POST`          | `/auth/sign-in`          | 계정 정보로 로그인 및 JWT 토큰 발급         |
| **회원 정보 수정**      | `PUT`           | `/auth/users/{userId}`   | 회원 ID로 회원 정보 업데이트                |
| **회원 삭제**          | `DELETE`        | `/auth/users/{userId}`   | 회원 ID로 소프트 삭제 처리                   |
| **로그아웃**           | `GET`           | `/auth/logout`           | 현재 인증된 사용자를 로그아웃 처리           |
| **회원 조회**          | `GET`           | `/auth/users`            | 회원 ID, Slack ID, 또는 이름으로 회원 조회   |
<br>

- message-service
  
| **기능**                              | **HTTP Method** | **엔드포인트**                       | **설명**                                                                 |
|---------------------------------------|-----------------|--------------------------------------|-------------------------------------------------------------------------|
| **공용 채널 메시지 전송**              | `POST`          | `/messages/send/{channelId}`         | 공용 Slack 채널에 메시지 생성 및 전송                                   |
| **DM 메시지 전송**                    | `POST`          | `/messages/sendDM`                   | Slack 사용자의 이메일(Slack Email)을 통해 개인 메시지(DM) 생성 및 전송  |
| **AI 메시지 생성 및 전송**             | `POST`          | `/messages/generate-ai-message`      | AI로 생성된 메시지를 Slack 공용 채널에 전송                             |
| **워크스페이스 메시지 동기화**         | `POST`          | `/messages/sync`                     | Slack 워크스페이스 내 모든 메시지를 동기화                               |
| **특정 채널 메시지 동기화**            | `POST`          | `/messages/sync/{channelId}`         | 지정된 Slack 채널의 메시지를 동기화                                      |
| **메시지 수정**                       | `PUT`           | `/messages/update`                   | Slack 메시지 수정                                                        |
| **메시지 삭제**                       | `DELETE`        | `/messages/{messageId}`              | 메시지 ID를 기반으로 메시지 삭제                                         |
<br>

- order-service

| **기능**           | **HTTP Method** | **엔드포인트**                      | **설명**                     |
|------------------|----------------|--------------------------------|----------------------------|
| **주문 생성**        | `POST`         | `/orders/business/{businessId}` | 주문 정보를 통해 주문 생성            |
| **주문 전체 조회**     | `GET`          | `/orders`                      | 권한에 따른 주몬 동적 전체 조회         |
| **주문 단건 조회**     | `GET`          | `/orders/{orderId}`            | 권한에 따른 주문 단건 상세조회          |
| **주문 삭제**        | `DELETE`       | `/orders/{orderId}`            | 권한별 주문 취소 배송 시작 전 상태에서만 가능 |
<br>

- agent-service

| **기능**                | **HTTP Method** | **엔드포인트**                 | **설명**                           |
|-----------------------|----------------|---------------------------|----------------------------------|
| **배송관리자 생성**          | `POST`         | `/agents`                 | 회원 정보와 배송 관리자 역할 정보를 통한 배송 관리자 생성 |
| **배송관리자 전체 조회**       | `POST`         | `/agents`                 | 권한별 배송관리자 동적 전체 조회               |
| **배송관리자 단건 조회**       | `POST`         | `/agents/{deliveryAgentId}` | 권한별 배송관리자 단건 조회                  |
| **배송관리자 정보 수정**       | `PUT`          | `/agents/{deliveryAgentId}` | 권한별 배송관리자 정보 수정 및 활성 상태 변경       |
| **배송관리자 삭제**          | `DELETE`       | `/agents/{orderId}`       | 주문 취소 배송 시작 전 상태에서만 가능           |
| **허브간 배송관리자 할당 요청**   | `POST`         | `/external/agents/global/{hubId}`  | 허브간 배송관리자 배정 요청                  |
| **허브 업체간 배송관리자 할당 요청** | `POST`         |  `/external/agents/hubs/{hubId}`   | 허브 업체간 배송관리자 배정 요청               |
| **배송관리자 정보 확인**       | `GET`          |  `/external/agents`   | 배송관리자 정보 조회                      |
<br>

- delivery-service

| **기능**                     | **HTTP Method** | **엔드포인트**                 | **설명**              |
|----------------------------|-----------------|---------------------------|---------------------|
| **허브간 배송 상태변경**            | `PUT`           | `/{deliveryId}/hubs/{hubDeliveryRouteId}` | 권한별 허브간 업체의 배송 상태 변경 |
| **허브 업체간 배송 상태변경**         | `PUT`           | `/deliverys/arrival/{businessDeliveryRouteId}` | 권한별 허브 업체간 배송 상태 변경 |
| **배송 단건조회**                | `GET`           | `/deliverys/{deliveryId}` | 권한별 배송 단건 상세조회      |
| **배송 취소**                  | `DELETE`        | `/deliverys/{deliveryId}`       | 배송 취소 배송 시작전 취소 가능  |
| **주문한 회원의 배송 전체 정보 확인 조회** | `GET`           | `/external/orders/deliveries`  | 주문자의 전체 배송 정보 확인 조회 |
| **배송 정보 확인 조회**            | `GET`           |  `/external/orders/{orderId}/deliveries`   | 주문자의 배송 정보 존재 확인 조회 |

