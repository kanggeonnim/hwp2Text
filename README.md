# hwp2text
기존에 있던 hwplib를 활용하여 hwp 파일을 텍스트값으로 이루어진 객체로 반환해주는 코드입니다.
참고 https://github.com/neolord0/hwplib

## Cell
- 테이블을 이루고 있는 하나의 셀에 대한 텍스트

## Row
- 테이블에서 하나의 Row에 대한 텍스트. ArrayList<Cell>로 이루어짐.

## Table
- ArrayList<Row>로 이루어진 테이블

## Paragraph
- ArrayList<테이블>
- 문단단위로 하나의 문단에는 여러개의 Table이 포함될 수 있음

## Document
- hwp문서 하나에 대한 모든 Text가 ArrayList<Paragraph> 형태로 이루어짐.
