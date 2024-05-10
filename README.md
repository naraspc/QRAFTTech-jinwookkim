유저는 여러개의 포트폴리오를 가질 수 있고, 포트폴리오는 종목코드, 수량을 포함하는 포트폴리오 Stock 데이터를 여러개 가질 수 있는 관계로 정의했습니다.

포트폴리오는 이름과 날짜, 포트폴리오Stock을 가지고있습니다.

포트폴리오 Stock은 개인주식데이터입니다. 

Stock 도메인은 주식의 가격을 관리하는 도메인입니다.

포트폴리오 조회기능에서 일자별 총 포트폴리오 가치는 Map으로 일자 - 일별 가치 형태로 가공하였습니다. 

TDD를 적용하기엔 아직 실력적인 무리가 있어서, 비즈니스 코드 작성 후 테스트코드 작성의 순서로 진행하였습니다. 



# 1. 종목 가격 / 일별 수익률 정보 조회 API  
엔드포인트 : api/stocks/findstock (post)
요청 페이로드는 아래와 같이 정의했습니다.

```
public record StockPriceRequestDto(
        String stockCode,
        LocalDate startDate,
        LocalDate endDate,
        String type
) {
}
```

응답 페이로드는 아래와 같습니다.

```
public record StockPriceResponseDto(
        LocalDate date,
        BigDecimal value,
        String type
) {

}

```

# 2. 종목 포트폴리오 등록 API 
엔드포인트 : /api/portfolio/create/{userId} (post)
요청 페이로드는 아래와 같습니다.

```
public record PortfolioRequestDto(

        List<StockEntryDto> stockEntries

) {
    public record StockEntryDto(

            String stockCode, int quantity

    ) {
    }
}
```
user table과 관계를 정의하기 위해서 UUID 타입의 userId 파라미터를 임의로 정의했습니다.

위 API는 포트폴리오를 생성하고, 포트폴리오 기준 일대다 관계를 가지는 개인주식데이터를 생성시켜 포함하도록 구성되어있습니다. 

## 2-1. 이미 생성된 포트폴리오에 개인 주식 데이터를 추가하는 API는 아래와 같습니다.
엔드포인트 : /api/portfolio/add/{portfolioId} 입니다. 
요청 페이로드는 아래와 같습니다. 

```
 public record StockEntryDto(

            String stockCode, int quantity

    ) {
    }
```




# 3. 종목 포트폴리오 삭제 API
엔드포인트 : /api/portfolio/delete/{userId} (post)
요청 페이로드는 아래와 같습니다. 

```
@RequestBody String portfolioId, @PathVariable UUID userId

```

사용자 인증 관련하여 JWT등을 사용하여 로그인을 구현할 수 있겠지만, 요구사항에 없어 우선 엔드포인트로 userId를 받아온뒤 검증하도록 하였습니다.
추후 로그인기능을 추가한다면 요청시 Token을 받아 유저의 이메일 같은 정보를 통해 권한을 확인하고자 합니다.



# 4. 종목 포트폴리오 조회 API
엔드포인트 : /api/portfolio/find/{userId} (post)
요청 페이로드는 아래와 같습니다. 

```
@RequestBody String portfolioID, @PathVariable UUID userId
```

응답 페이로드는 아래와 같습니다.

```
public record PortfolioResponseDto(
        String portfolioName,
        Map<LocalDate,BigDecimal> totalValues,
        LocalDate createDate
){
}

```
