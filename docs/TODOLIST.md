- [x] CI CD
    - [x] GithubActions를 이용한 CI 환경 구축
    - [x] GithubActions, dockerhub를 이용한 CD 환경 구축

- [x] Swagger
    - [x] Swaager를 통한 API문서 자동 완성 환경 구축
- [x] SpringSecurity
    - [x] SpringSecurity를 이용한 인증 인가 환경 구축

- [x] 회원가입
    - [x] loginId,password,email로 회원가입
    - [x] 유저 정보 저장

- [ ] 로그인
    - [ ] KAKAO, GOOGLE을 통한 Oauth 로그인
    - [x] loginId,password를 입력받아 로그인
    - [x] RDB와 비교하여 아이디 비밀번호 검증
    - [x] 토큰 생성
    - [x] Redis에 로그인된 유저 정보 저장
    - [x] 유저에게 AccessToken,RefreshToken 응답

- [ ] 캘린더
    - [ ] 영수증
        - [ ] CLOVA API활용, 구매 목록 도출
        - [x] 유저가 업로드한 사진 리사이징
        - [x] 사진 S3에 업로드 후 url 취득
        - [ ] 추출한 데이터와 사진URL 테이블에 저장
    - [ ] 검색 API
        - [ ] 네이버 쇼핑 검색 API 사용, 카테고리, 상품 이미지 추출
        - [ ] 추출한 카테고리와, 상품 이미지, 상품명 저장
    - [ ] 조회
        - [ ] 유저가 업로드한 상품 내역 월별로 응답

- [ ] 분리수거 미션
    - [ ] 분리수거 사진 업로드
        - 사진은 자신이 구매한 품목에 대한 것이여야함
    - [ ] 분리수거 미션을 관리하는 테이블에 저장
    - [ ] 관리자 검토 후 확인 또는 거절
    - [ ] 확인된 케이스는 점수 지급

- [ ] 분리수거장 지도(미정)
    - [ ] 분리수거장 마킹을 위한 좌표 응답
