# 환경
Spring Boot 2.3.2.RELEASE
Kotlin 1.4.10
Gradle 6.6.1
Java 1.8

Aws MySql

# Run
./gradlew :app:bootRun

# 구조
```
Root project 'kkp'
+--- Project ':app'
\--- Project ':core'
     +--- Project ':core:domain'
     \--- Project ':core:service'
```
- app
  - 웹어플리케이션 구성에 대한 책임을 갖는다.
- core:domain
  - entity, enum 등 비지니스 로직에 대한 책임을 갖는다.
- core:service
  - domain 에 적용된 비지니스 유스케이스를 적절히 사용한다.
  - entity 들의 적절한 사용법을 정의한다.

# Working List
## Entity 관계
- chatRoom : user = N : N
- moneyDistribution : distributionItem = 1 : N

## usecase 설명
- 각 usecase 는 최대한 entity 에 작성
  - 뿌리기는 MoneyDistribution 에서 알아서 분배
- visibility 는 최소한으로 보이게 작성 (ex: DistributionItem::receive)
    - internal 로 지정함에 따라 core:domain 나 app 에서 접근이 불가능하고 MoneyDistribution 에서만 뿌리기 받기가 가능하므로 데이터 정합성 향상

## 정의된 usecase
- 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다.
    - 정의된 인원수에 맞게 평등하게 분배
    
- token 은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.
    - 3자리 문자열을 랜덤하게 생성하며 이미 생성된 적이 있다면 재생성
    - 이미 만들어진 적이 있는 토큰이라면 재생성
    - 테스트 환경이므로 만들어질 때까지 계속해서 생성


# TODO
- web controller test
- jigsaw module

