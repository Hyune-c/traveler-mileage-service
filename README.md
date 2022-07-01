# 트리플여행자 클럽 마일리지 서비스

트리플 사용자들이 장소에 리뷰를 작성할 때 포인트를 부여하고, 전체/개인에 대한 포인트 부여 히스토리와 개인별 누적포인트를 관리하고자 합니다.

## Specifications

- 리뷰 작성 이벤트를 처리할 수 있는 API를 개발합니다.
    - 사용자는 장소마다 리뷰를 1개 작성할 수 있고, 수정 또는 삭제할 수 있습니다.
    - 리뷰 작성 보상 점수는 아래와 같습니다.
        - 내용 점수: 1자 이상 텍스트 (1점), 1장 이상 사진 첨부 (1점)
        - 보너스 점수: 특정 장소에 첫 리뷰 작성 (1점)

```http request
POST /events

{
  "type": "REVIEW",
  "action": "ADD",
  /* "MOD", "DELETE" */
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
