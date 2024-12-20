# 🖥️ Comsci-QuizGame 컴퓨터 과학 지식 퀴즈 게임
### ERD
![image](https://github.com/user-attachments/assets/9102f2ac-c397-4fcb-aa5f-0103223ae296)

### FlowChart
![image](https://github.com/user-attachments/assets/781ff55d-7f47-414b-8d5f-e0055bb68db7)

### 프로젝트 설명
대학교에 입학하기 전에는 컴퓨터 과학에 대한 지식이 전무했던 학생 이었어서 전공과목을 학습하는데 있어 용어와 개념들이 생소하고 어렵게 다가왔고, 학습하는 데 있어 많은 시간을 쏟았어야 했습니다. <br>
따라서 전공과목 공부를 보다 효과적으로 익히고 반복할 수 있는 방법에 대해 고민하였고,<br> 이에 전공 지식을 시각적으로 성취하는 방식이 학습 효과를 높이는 데 기여할 수 있을 것이라 판단하여 CS 지식을 기반으로 한 퀴즈 게임 Quiz&Conquer를 기힉하게 되었습니다.<br>

상점에서 게임 아이템 (시간 30초 추가, 목숨 아이템, 악세서리 아이템 적용)을구매할 수 있으며,<br>
게임은 총 4개의 카테고리 프론트엔드, 백엔드, 자료구조, 운영체제 중 하나를 선택해 퀴즈 게임을 진행할 수 있습니다.<br>
한번 사용자가 푸는데 성공한 퀴즈는 user_quiz 테이블에 추적되어 다시 표시되지 않습니다.

### 퀴즈 데이터 
카테고리 '백엔드' 예시
```sql
INSERT INTO quiz (categoryId, title, option1, option2, option3, correct_option) VALUES
(2, 'HTTP란 무엇인가?', 'Hyper Text Transfer Protocol', 'High Text Transfer Protocol', 'Hyper Transfer Text Protocol', 1),
(2, 'RESTful API의 주요 특징은 무엇인가?', '상태 비저장', '세션 기반', '디스패치 기반', 1),
(2, 'GET 요청과 POST 요청의 차이점은?', 'GET은 서버에게 데이터를 요청한다', 'GET은 서버로부터 데이터를 받아온다', 'POST는 데이터를 서버에게 전송한다', 2),
(2, 'JWT란 무엇인가?', 'JSON Web Token', 'Java Web Token', 'JavaScript Web Token', 1),
(2, 'CORS란 무엇인가?', 'Cross-Origin Resource Sharing', 'Cross-Origin Request Sharing', 'Common Origin Resource Sharing', 1),
(2, '세션과 쿠키의 차이점은?', '세션은 서버에 저장, 쿠키는 클라이언트에 저장', '세션은 클라이언트에 저장, 쿠키는 서버에 저장', '세션과 쿠키는 동일하다', 1),
(2, 'SQL에서 JOIN이란 무엇인가?', '두 개 이상의 테이블을 합치는 작업', '데이터를 삭제하는 작업', '데이터를 업데이트하는 작업', 1),
(2, '무엇이 RESTful API의 장점인가?', '속도와 효율성', '복잡성 증가', '데이터베이스 의존성 증가', 1),
(2, 'MySQL에서 테이블을 생성하는 명령어는 무엇인가?', 'CREATE DATABASE', 'CREATE TABLE', 'CREATE SCHEMA', 2),
(2, 'API의 응답 코드 중 200은 무엇을 의미하는가?', '요청 성공', '서버 오류', '리다이렉션', 1),
(2, 'MVC 패턴에서 C는 무엇을 의미하는가?', 'Controller', 'Client', 'Container', 1),
(2, 'Node.js의 주요 특징은 무엇인가?', '비동기 I/O', '동기 I/O', '블로킹 I/O', 1),
(2, 'API 요청에서 GET 메소드는 어떤 용도로 사용되는가?', '리소스를 조회하기 위해', '리소스를 생성하기 위해', '리소스를 삭제하기 위해', 1),
(2, 'SQL에서 GROUP BY 절의 용도는 무엇인가?', '집계 함수와 함께 사용하여 데이터를 그룹화', '테이블의 데이터를 정렬', '중복 데이터를 제거', 1),
(2, 'CORS를 해결하기 위한 방법은?', '서버에서 Access-Control-Allow-Origin 헤더 추가', '서버에서 CORS 설정을 제거', '클라이언트에서 CORS 요청을 차단', 1),
(2, 'Node.js에서 Express를 사용하여 서버를 만드는 코드의 기본은?', 'const app = express(); app.listen(3000);', 'const server = http.createServer(); server.listen(3000);', 'const express = require("http"); express.createServer();', 1),
(2, 'MySQL에서 SELECT 쿼리를 사용하는 기본 문법은?', 'SELECT * FROM 테이블명;', 'SELECT 테이블명 FROM *;', 'SELECT 테이블명;', 1),
(2, 'SQL에서 데이터 삽입을 위한 명령어는 무엇인가?', 'INSERT INTO', 'INSERT', 'ADD INTO', 1),
(2, 'RESTful API에서 HTTP 메소드 중 PUT의 용도는 무엇인가?', '리소스 교체', '리소스 생성', '리소스 삭제', 1);
(2, 'AWS 클라우드 방식에 대해서 옳은 것은?', 'On-Demand 방식이다.(종량제 방식)', 'On-Premise 방식이다. (주문형 방식)', '서버를 운용하기 위한 서버 구동용 컴퓨터가 필요하다.',1);
```
