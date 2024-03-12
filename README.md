# hwp2text
- 기존에 있던 hwplib를 활용하여 hwp 파일을 텍스트값으로 이루어진 객체로 반환해주는 코드입니다.
- 참고 https://github.com/neolord0/hwplib

# 매니페스토 국회의원 이행률 현황 parser
- 한국매니페스토실천본부에서 공개한 21대 국회의원 공약 이행률.hwp 파일에 대해서 text를 추출하여 DB에 적재.
- 공약이행률 { 공약이행 전체 요약정보, List<상세 공약 이행내역> }의 형태의 객체로 추출해줌.

### Cell
- 테이블을 이루고 있는 하나의 셀에 대한 텍스트

### Row
- 테이블에서 하나의 Row에 대한 텍스트. ArrayList<Cell>로 이루어짐.

### Table
- ArrayList<Row>로 이루어진 테이블

### Document
- hwp문서 하나에 대한 모든 Text가 ArrayList<Table> 형태로 이루어짐.



