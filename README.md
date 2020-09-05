# Menu management program with MySQL, Java, JDBC

샌드위치 가게의 메뉴 관리 시스템을 개발했습니다

## 주사용기술
MySQL, Java, JDBC

## 역할
모든 기능 구현. 데이터베이스 CRUD 구현 및 TEXT 기반 UI 구현.

## 주요 기능
<ul>
<li>메뉴와 관련 정보 생성, 수정, 삭제
<li>전체 메뉴/메뉴 관련 정보(가격 등) 확인
<li>현재 매출/순이익 확인
<li>메뉴당 매출 및 순이익 확인
<li>오늘의 매출 기록
<li>가장 인기있는 메뉴 확인
</ul>

## 수행 과정
<ul>
<li>JDBC api 사용
<li>preparedStatement를 활용하여 query에 parameterized variables 사용
<li>index, view 자료구조 활용
<li>transaction을 통해 연관된 메뉴의 업데이트가 한 번에 이루어질 수 있도록 구현
</ul>
