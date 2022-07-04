# 트리플여행자 클럽 마일리지 서비스

> public repository 사용 및 회사명 노출을 허락받은 후 작성된 repository 입니다.

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

```shell
# local profile 
> ./gradlew clean bootRun --args='--spring.profiles.active=local'
```

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

![image](https://user-images.githubusercontent.com/55722186/177181394-d327f75b-3e97-4094-a978-b8fd31060b97.png)

### 2. 단위 테스트 & LargeTest 구현 with dynamicTest

![image](https://user-images.githubusercontent.com/55722186/177181783-bb69b5f7-489f-4e1e-928c-bf035baabc04.png)

### 3. 설계 & 구현에 대한 생각

- 간소화된 DDD 설계
    - web 레이어 분리
    - SRP를 준수한 service 레이어 (business 레이어)
- 레이어간/객체간 메시지 전달에 dto를 활용하고 구분된 네이밍을 정의
    - web 레이어 dto - request, response
    - service 레이어 dto - dto
- JPA 기능을 한정적으로 활용한 영속성 레이어 설계
    - 성능이 필요한 기능은 querydsl 활용
- 가독성과 안전함을 고려한 코딩
    - 함수형 프로그래밍 및 kotlin의 베스트 케이스 적용
    - google style guide, sonarlint를 활용한 컨벤션 체크

### 3. Spring Validator를 활용한 dto 검증

- web 레이어의 bean validation만이라면 어노테이션만으로도 쉽게 됩니다.
- 하지만 구현 요구상
    - 하나의 request에서 type에 따라 복수의 요청을 처리해야하고,
    - 비지니스 검증도 필요한 로직이 있기에 Validator를 활용했습니다.

### 4. Event Driven 구조의 포인트 설계

![image](https://user-images.githubusercontent.com/55722186/177184936-4fc6005d-f4d8-4f9b-b427-8b9ac481aec3.png)

- 과제 조건에는 포인트 변경의 소스가 리뷰만이지만, 실무라면 포인트 변경의 소스가 계속 늘어날 것입니다.
    - 포인트는 이력이 중요하며 레이스 컨디션의 주체가 적고 조회가 주가 됩니다.
    - 차후 마이크로 서비스의 분리 대상이 됩니다.
- 따라서 history-replay 기반의 설계로 이력과 현재 포인트 조회를 가져오도록 설계 했습니다.
    - 가장 많은 요청인 현재 포인트 조회는 캐싱한 후 userId (createdBy) 기반으로 변경이 생기면 evict 합니다.
    - 단순 이력으로 설계했기에 큐를 활용하거나 비동기 로직을 통해 빠른 응답을 기대하도록 개선할 수 있습니다.
    - 마이크로 서비스로 분리된다면 CDC (Change Data Capture) 등의 기술을 활용합니다.
- 캐시는 현재 구현되어 있지 않습니다.

## # 개선 가능한 항목

- [ ] [필수] 리뷰 수정시 포인트 획득/차감
- [ ] [필수] 인덱스 및 쿼리 최적화
- [ ] 표준 예외 처리 구현
- [ ] 예외 처리 상세화
- [ ] service 클래스를 interface로 구조화
- [ ] 포인트 조회 캐싱
- [ ] 코드 정리
- [ ] docker-compose를 통한 기동 with mysql, redis
