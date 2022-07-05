# 트리플여행자 클럽 마일리지 서비스

> 회사명 노출 및 public repository 사용을 허락받았습니다.

트리플 사용자들이 장소에 리뷰를 작성할 때 포인트를 부여하고, 전체/개인에 대한 포인트 부여 히스토리와 개인별 누적포인트를 관리하고자 합니다.

## # Specifications

- 리뷰 작성 이벤트를 처리할 수 있는 API를 개발합니다.
    - 사용자는 장소마다 리뷰를 1개 작성할 수 있고, 수정 또는 삭제할 수 있습니다.
    - 리뷰 작성 보상 점수는 아래와 같습니다.
        - 내용 점수: 1자 이상 텍스트 (1점), 1장 이상 사진 첨부 (1점)
        - 보너스 점수: 특정 장소에 첫 리뷰 작성 (1점)

```http request
POST /events

{
  "type": "REVIEW",
  "action": "ADD", /* "MOD", "DELETE" */
  "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
  "content": "좋아요!",
  "attachedPhotoIds": [
    "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8",
    "afb0cef2-851d-4a50-bb07-9cc15cbdc332"
  ],
  "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
  "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```

- 포인트 조회 API를 개발합니다.

## # 실행 방법

### with h2

1. 실행

```shell
# local profile 
> ./gradlew clean bootRun --args='--spring.profiles.active=local'
```

2. h2-console 접속

http://localhost:8080/h2-console

![image](https://user-images.githubusercontent.com/55722186/177390797-4ac154f8-320f-4263-873c-895a43055bc8.png)

### with mysql

1. application.yml을 참고하여 설정 or 수정

```yaml
datasource:
  driver-class-name:net.sf.log4jdbc.sql.jdbcapi.DriverSpy
  url:${DATASOURCE_URL:jdbc:log4jdbc:mysql://localhost:4306/review_schema?useSSL=false&useUnicode=true&characterEncoding=UTF-8}
  username:${DATASOURCE_USERNAME:root}
  password:${DATASOURCE_PASSWORD:password1}
```

2. 실행

```shell
# default profile 
> ./gradlew clean bootRun 
```

### Http Request Sample

https://github.com/Hyune-c/traveler-mileage-service/wiki

## # 주요 기능 & 구현 의도

### 1. ERD 설계

![image](https://user-images.githubusercontent.com/55722186/177860478-8b468996-5cee-43a1-92fd-f60add66d744.png)

### 2. 단위 테스트 & LargeTest 구현 with dynamicTest

![image](https://user-images.githubusercontent.com/55722186/177392995-b533e1f6-9d51-4b03-9cb0-e3bb697074c5.png)

### 3. 설계에 대한 생각

- 레이어드 아키텍처 설계
    - web 레이어 분리
    - domain 레이어로 review와 point를 설계
    - SRP를 준수한 service 레이어 (business 레이어)
- 포인트 적립을 Facade 패턴으로 설계 `PointCreateFacade`
- JPA 기능을 한정적으로 활용한 영속성 레이어 설계
    - 성능이 필요한 기능은 querydsl 활용

### 3-1. Event Driven 구조의 포인트 설계

![image](https://user-images.githubusercontent.com/55722186/177184936-4fc6005d-f4d8-4f9b-b427-8b9ac481aec3.png)

- 과제 조건에는 포인트 변경의 소스가 리뷰뿐이지만, 실무라면 포인트 변경의 소스가 계속 늘어날 것입니다.
    - 포인트는 이력이 중요하며 레이스 컨디션의 주체가 적고 조회가 주가 됩니다.
    - 차후 마이크로 서비스의 분리 대상이 됩니다.
- 따라서 history-replay 기반의 설계로 이력과 현재 포인트 조회를 가져오도록 설계 했습니다.
    - 가장 많은 요청인 현재 포인트 조회는 캐싱한 후 userId (createdBy) 기반으로 변경이 생기면 evict 합니다.
    - 단순 이력으로 설계했기에 큐를 활용하거나 비동기 로직을 통해 빠른 응답을 기대하도록 개선할 수 있습니다.
    - 마이크로 서비스로 분리된다면 CDC (Change Data Capture) 등의 기술을 활용합니다.
- 캐시는 구현되어 있지 않습니다.

### 4. 지속 가능한 개발에 대한 생각

- 레이어간/객체간 메시지 전달에 dto를 활용하고 구분된 네이밍을 정의
    - web 레이어 dto - request, response
    - service 레이어 dto - dto
- 가독성과 안전함을 고려한 코딩
    - 함수형 프로그래밍 및 kotlin의 베스트 케이스 적용
    - google style guide, sonarlint를 활용한 컨벤션 체크
- 가독성과 구현 의도를 알리기 위한 javadoc 작성 - public 메서드 위주
    - 실무에서도 작성하는 편이지만, 구현 의도를 알리기 위해 조금 더 자세하게 작성했습니다.

### 4-1. Spring Validator를 활용한 dto 검증

- web 레이어의 bean validation만이라면 어노테이션만으로도 쉽게 됩니다.
- 하지만 구현 요구상
    - 하나의 request에서 type에 따라 복수의 요청을 처리해야하고,
    - 비지니스 검증도 필요한 로직이 있기에 Validator를 실험적으로 활용했습니다.

### 4-2. 표준 예외 처리 구현 `ErrorResponse`

- transactionId를 통해 요청을 추적할 수 있습니다.
    - 구현 방법에 따라 서버에서 발급한 transactionId/requestId를 사용하거나, 프론트에서 직접 발급할 수도 있습니다.
    - 이 프로젝트에서는 내부 로깅용으로 에러 추적을 위해서만 사용합니다.

<img width="435" alt="image" src="https://user-images.githubusercontent.com/55722186/177984342-b8abdd79-4678-40f4-a857-9c21618195db.png">
<img width="437" alt="image" src="https://user-images.githubusercontent.com/55722186/177984487-97c018b8-e38c-418b-976b-435618296391.png">

