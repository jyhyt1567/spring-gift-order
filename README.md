# spring-gift-order

# step 0
- 기본 코드 준비 완료

# step 1
- 카카오계정 로그인을 통해 인증 코드 받기
  - 'GET /kakao/login'으로 접속 시 kauth.kakao.com/oauth/authorize 으로 리디렉션, 카카오 로그인 진행
  - 로그인이 성공하면 localhost:8080/ 으로 요청이 도착
- 액세스 토큰을 추출
  - 로그인이 성공적으로 진행되면 요청에서 얻은 인가 코드를 추출 후 카카오 서버에 요청, 액세스 토큰을 발급 받는다. 
- 시크릿 키 유출 방지
  - 시크릿 키는 application.properties의 환경 변수로 설정, 커밋 시 삭제하였음.