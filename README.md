# 대용량 트래픽 프로젝트 - 서울시 온라인 쇼핑몰 현황
- 팀 구성 : 장준혁 (팀장) 권순형, 김민주, 노하영 (팀원)
- 프로젝트기간 : 2/14 ~ 2/22
- 개발 도구 : Kotrlin, SpringBoot, Intellij, Redis, OpenAPI

## INTRODUCE
- 본 프로젝트는 10만건 이상의 데이터를 효율적으로 데이터베이스에 업로드하고 , 등록/조회/수정/삭제 등의 관리 업무를 수행할 수 있도록 제작되었습니다.
- 데이터는 초기 csv 파일에서 추출하여 업로드하는 방식과 OpenAPI 의 현황 데이터를 직접 가져오는 방식 두 가지를 테스트해보았습니다.
- 데이터 조회시엔 QueryDsl 을 활용한 동적쿼리를 통해 조건에 맞는 데이터를 유연하고 효율적으로 조회할 수 있도록 하였습니다.
- 조회된 데이터는 Redis 의 Cache 에 저장되어 이후 같은 요청 발생 시 빠르게 응답할 수 있도록 하였습니다.

## 기능 소개
- [x]  **IntelliJ Ultimate 또는 DBeaver를 사용하여 CSV 파일을 database 테이블에 입력하기**
    - [x]  IntelliJ Ultimate 사용해서 csv 파일로 데이터베이스 테이블을 생성 
- [x]  **QueryDSL 을 사용한 커서 기반 페이지네이션 및 필터**
    - ‘전체평가’ 필터 조회와 ‘업소상태’ 필터 조회(2개 필터 동시 적용, 각각 1개씩 적용) 상위 10개 리스트 조회
    - [x]  ‘전체평가’ 필터 조회
        - ‘전체평가’는 0점 ~ 3점 으로 이루어져 있고 점수를 입력하여 해당 업체 리스트만 조회
    - [x]  ‘업소상태’ 필터 조회
        - `사이트운영중단`, `휴업중`, `광고용(홍보용)`, `등록정보불일치`, `사이트폐쇄`, `영업중`, `확인안됨` 상태 중 1개를 선택하여 해당 업체 리스트만 조회
    - [x]  커서 기반 페이지네이션을 적용
    - [x]  프로젝션 적용
- [x]  **csv를 database에 입력하는 kotlin 코드 만들기**
    - [x]  `/collection`  API 를 통해 서버 내 특정 위치의 csv 파일을 읽어서 Database에 차례대로 insert 하는 로직을 구현.
    - [x]  100개씩 읽어서 Database에 입력하기
- [x]  **OpenAPI 를 통해 database에 입력하는 kotlin 코드 만들기**
    - [x]  https://data.seoul.go.kr/dataList/OA-2256/S/1/datasetView.do 의 OpenAPI를 이용
    - [x]  **스케쥴링 이용하여 OpenApi 값 최신화**

- [x]  **QueryDSL 단위 테스트**
- [x]  **Redis 이용하여 Cacheing 구현**
    - [x]  최신 자료 20건 등록
    - [x]  단건 조회시 캐싱 적용
    - [x]  AOP 통한 성능 확인

## 역할 분담
장준혁
- Store CRUD
    - 쇼핑몰 정보 등록/조회/업데이트/삭제
    - 프로젝션 적용 여부를 선택할 수 있는 전체 조회( 페이지네이션, 다이나믹 프로젝션 적용)
    - 쇼핑몰 id/상호명/쇼핑몰명/전화번호 통한 검색( 동적 쿼리 적용 )
        - 대용량 문제로 like 사용된 쇼핑몰명/전화번호 검색 시 응답시간 초과 . 전문검색엔진 연구 후 도입 필요
- Redis 를 통한 캐싱 기능
    - 최신 자료 n 건을 미리 캐시에 등록하여 사용될 수 있도록 함 ( 스케쥴링 적용 )
    - id 로 단건 조회시 캐시에 등록되어있는지 우선검색하도록 함
        - 캐시에 등록할 때 id 를 key 로 만들어 생성.
    - AOP 통한 성능 테스트 

- 내용 설명
  - [CRUD]
  - DB 에 저장된 서울시 인터넷 쇼핑몰 정보를 전부 호출하는 것과 사용자들이 주로 찾을만한 속성들은 별도의 DTO 를 생성하여 Projection 을 적용하였습니다.
  - Projection 을 적용하기 위해 별도의 메서드를 작성하던 중 ‘ 전체적인 기능은 똑같은데 Projection을 위해 SELECT 하는 부분만 차이점이 있다면 하나의 메서드로 관리할 수 있지 않을까? ‘ 하는 생각을 갖게 되었습니다.
  - 핵심 문제점은 사용자의 Projection 적용 여부 선택에 따라  ‘반환형’이 달라져야 하는 것이었는데, 이는 제네릭 메서드를 활용하는 것으로 해결할 수 있었습니다.
  - Controller 와 Service, Repository 의 메서드를 제네릭으로 교체하고, Projection 여부를 선택하는 불리언 값에 따라 변환해야할 DTO 를 지정하여 메서드를 호출한 뒤 Page 형태로 변환하여 반환하는 것으로 구현하였습니다.
  - [Redis Cache]
  - 추가구현으로 Redis 를 통해 캐싱을 구현하였습니다.
  - 처음엔 @Cacheable 어노테이션을 통해 단건조회시 “storeCache::$id” 로 키를 지정하여 값을 저장하고, 호출하도록 하였습니다.
  - 하지만 이후 DB 의 id 값을 기준으로 최근 저장된 n 건의 데이터를 캐시에 등록하는 과정에서 문제가 있었습니다.
  - @Cacheable 로는 반환값만을 등록할 수 있기 때문에 n건의 데이터가 List째로 저장되기 때문이었습니다.
  - redisTemplate 를 활용하여 opsForValue() 로 최신데이터들을 각각  “storeCache::$id” 로 저장하여 단건조회 시 활용할 수 있도록 하였습니다.
  - cache 에 저장된 데이터를 역직렬화 하는 과정에선 Jackson 에서 LocalDateTime 을 제대로 변환할 수 없는 문제점이 있어

  -     - ElasticSearch 등의 검색 엔진 통한 검색 기능 구현
  - 캐시 데이터 동기화 문제
    

권순형

-

-

- 내용 설명

김민주

- CSV파일 데이터를 database에 입력
    - CSV파일 데이터를 database에 입력하는 kotlin 코드 만들기
    - CSV파일의 데이터를 100개씩 읽어서 Database에 입력하기
- OpenAPI 데이터를 database에 입력
    - XML 문서로 된 데이터를 파싱해서 DB에 입력하기
    - 중복데이터가 있을 경우 가장 먼저 찾은 데이터를 DB에 입력하기
- 스케줄러 기능을 사용하여 OpenAPI 데이터 최신화
    - HvtProjectApplication.kt 에 EnableSchedulling 어노테이션 사용
    - StoreController의 fetchDataAndStore 메서드에 Scheduled 어노테이션 사용
    - cron 표현식을 사용하여 시간 설정
- 내용 설명  
CSV 파일을 읽어 데이터를 DB에 저장하는 기능은 다수의 데이터를 한 번에 처리하는데 큰 장점이 있습니다. 특히, 100개 단위로 데이터를 DB에 저장하는 방식을 사용함으로써, DB 작업을 최소화하고 성능을 향상 시킬 수 있었습니다. 그러나 이 방식은 CSV 파일 내의 데이터만 가져올 수 있어, 최신 데이터를 확보하는 데 한계가 있었습니다. 이를 해결하기 위해 OpenAPI를 사용하게 되었습니다. OpenAPI는 인터넷을 통해 실시간으로 데이터를 제공하므로, 데이터의 최신성을 보장할 수 있습니다. XML 문서로 이루어진 OpenAPI 데이터를 파싱해서 DB에 저장하기 위해 DocumentBuilder를 사용하였습니다.  DocumentBuilder의 장점은 다음과 같습니다.
    
    1. 표준 API 사용: DocumentBuilder는 Java에서 XML을 처리하기 위한 표준 API인 JAXP(Java API for XML Processing)의 일부입니다. 이 API를 사용하면 XML 문서를 읽고 쓰는 것이 간편해집니다.
    2. DOM 방식 파싱: DocumentBuilder는 DOM(Document Object Model) 방식으로 XML 문서를 파싱합니다. DOM 방식은 XML 문서를 트리 구조로 메모리에 올려놓고 원하는 노드를 자유롭게 접근할 수 있게 해줍니다. 이는 임의 접근이 필요한 복잡한 XML 문서의 처리에 적합합니다.
    3. 오류 검출: DocumentBuilder는 XML 문서를 파싱하는 과정에서 문법 오류를 자동으로 검출해줍니다. 이를 통해 데이터의 품질을 보장할 수 있습니다.
    
    중복데이터 처리는 StoreRepository에서 findTopByCompanyAndShopNameAndDomainName 메서드를 통해 여러 개의 결과가 있더라도 가장 먼저 찾은 하나의 결과만 반환하도록 하였습니다.
    
    스케줄러 기능을 통해, API에서 데이터를 주기적으로 추출하여 실시간으로 데이터를 최신화 하도록 하였습니다.
    

노하영

- 필터를 적용한 업체 리스트 조회 API
    - 모니터링날짜(REG_DATE)의 내림차순으로 정렬하여 상위 10개 리스트 조회
- 필터를 적용한 업체 페이지 조회 API
    - Id의 내림차순으로 정렬하여 10개씩 조회
    - 커서 기반 페이지네이션 적용
- 공통사항
    - 전체평가(TOT_RATINGPOINT)와 업소상태(STAT_NM) 컬럼에 대해 필터 적용
    - QueryDSL과 BooleanBuilder를 이용한 동적 쿼리
    - Projection을 적용한 리스트/페이지 조회 API 추가 (Projections.constructor와 @QueryProjection 사용)
    - @DataJpaTest를 이용한 단위테스트 작성
- 내용 설명
  - 리스트 조회는 필터를 적용한 결과에서 상위 10개를 리스트로 가져옵니다.
  - 페이지 조회는 커서 페이지네이션 방법을 사용했습니다. Id로 정렬을 하고, 마지막으로 조회되었던 건의 Id값을 쿼리의 Where절에서 조건으로 사용했습니다. offset이 아닌 Id값을 기준으로 하기 때문에 데이터 추가나 삭제로 인해 데이터가 중복되거나 누락되어서 조회되는 문제가 생기지 않습니다.
  - QueryDSL과 BooleanBuilder를 이용해서 필터에 입력값이 들어온 상황(개수)에 맞춰서 동적 쿼리를 생성하도록 작성했습니다.
  - 기존에 작성했던 업체 리스트 조회와 페이지 조회에 Projection을 적용한 API를 추가했습니다. 필드가 35개에서 9개로 줄어들어서 단순해진 응답을 받을 수 있습니다.
  - @DataJpaTest를 이용해서 조회 API 4개에 대한 테스트 코드를 작성했습니다. 필터 입력값의 개수에 따른 다양한 경우와 입력값이 잘못 들어온 경우에 대해서 작성했습니다.


## ERD
![image](https://github.com/JangCoding/HVT_Project/assets/62090021/3868ecbd-d4ee-4855-a1eb-5c38335408b3)

## REST API
![image](https://github.com/JangCoding/HVT_Project/assets/62090021/ad40d0d8-d1e7-4f24-8419-01f448cf14fa)

