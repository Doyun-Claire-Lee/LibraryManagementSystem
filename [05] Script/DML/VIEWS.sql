--------------------------------------------------------
--  DDL for View VWBLACKFROMRENT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LMS"."VWBLACKFROMRENT" ("MEMBER_SEQ", "BLACKYEAR", "LATECOUNT") AS 
  select member_seq as member_seq, to_char(rent_date, 'yyyy') as blackYear, count(*) as lateCount from tblrent 
        where (((return_date is null and sysdate - rent_date >= 8) or (return_date - rent_date >= 8)) and extension_count = 0)
            or (((return_date is null and sysdate - rent_date >= 15) or (return_date - rent_date >= 15)) and extension_count = 1)
            or (((return_date is null and sysdate - rent_date >= 22) or (return_date - rent_date >= 22)) and extension_count = 2)
                group by member_seq, to_char(rent_date, 'yyyy')
                    having count(*) >= 3
                        order by member_seq, blackYear
;
--------------------------------------------------------
--  DDL for View VWCHECKRENTBOOK
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LMS"."VWCHECKRENTBOOK" ("회원번호", "성함", "도서코드", "대여시작일", "대여종료일", "도서명") AS 
  select a."회원번호",a."성함",a."도서코드",a."대여시작일",a."대여종료일",a."도서명" from (select m.seq as 회원번호, 
m.name as 성함,
bs.book_code as 도서코드,
to_char(r.rent_date,'yyyy-mm-dd') as 대여시작일,
to_char(r.return_date,'yyyy-mm-dd') as 대여종료일,
b.book_name as 도서명
from tblMember m 
inner join tblRent r
on r.member_seq = m.seq
inner join tblBookState bs
on bs.book_code = r.book_code 
inner join tblBook b
on b.seq= bs.book_seq) a
where a.대여종료일 is null
;
--------------------------------------------------------
--  DDL for View VWCHECKRETURNBOOK
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LMS"."VWCHECKRETURNBOOK" ("도서코드", "성함", "회원번호", "대여날짜", "반납날짜", "반납여부", "연장횟수", "도서명") AS 
  select
bs.book_code as 도서코드,
m.name as 성함,
m.seq as 회원번호,
to_char(r.rent_date,'yyyy-mm-dd') as 대여날짜,
to_char(r.return_date,'yyyy-mm-dd') as 반납날짜,
(case
    when r.return_date is null then 'N'
    when r.return_date is not null then 'Y'
end) as 반납여부,
r.extension_count as 연장횟수,
b.book_name as 도서명
from tblMember m 
inner join tblRent r
on r.member_seq = m.seq
inner join tblBookState bs
on bs.book_code = r.book_code 
inner join tblBook b
on b.seq= bs.book_seq
order by 반납날짜 asc
;
--------------------------------------------------------
--  DDL for View VWCHECKRETURNLIST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LMS"."VWCHECKRETURNLIST" ("번호", "도서명", "도서번호", "성함", "회원번호", "대여날짜", "반납날짜") AS 
  select
b.seq as 번호,
b.book_name as 도서명,
bs.book_code as 도서번호,
m.name as 성함,
m.seq as 회원번호,
to_char(r.rent_date,'yyyy/mm/dd') as 대여날짜,
to_char(r.return_date,'yyyy/mm/dd') as 반납날짜
from tblMember m 
inner join tblRent r
on r.member_seq = m.seq
inner join tblBookState bs
on bs.book_code = r.book_code 
inner join tblBook b
on b.seq= bs.book_seq
order by 회원번호
;
--------------------------------------------------------
--  DDL for View VWDELETEBOOKLIST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LMS"."VWDELETEBOOKLIST" ("번호", "도서코드", "분류", "출판사", "저자", "도서명", "삭제가능여부") AS 
  select 
b.seq as 번호,
bs.book_code as 도서코드, 
b.decimal_num as 분류, 
b.publisher as 출판사, 
b.author as 저자, 
b.book_name as 도서명,
(case
 when bs.delete_exist = 0 then '삭제가능'
end) as 삭제가능여부
from tblBook b
inner join tblBookState bs
on b.seq=bs.book_seq
;
--------------------------------------------------------
--  DDL for View VWRESERVATION
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LMS"."VWRESERVATION" ("번호", "회원번호", "회원명", "도서명", "저자명", "예약날짜") AS 
  select
r.seq as 번호,
m.seq as 회원번호,
m.name as 회원명,
b.book_name as 도서명,
b.author as 저자명,
to_char(r.res_date,'YYYY/MM/DD') as 예약날짜
from tblReservation r
inner join tblBook b
on b.seq = r.book_seq
inner join tblMember m 
on m.seq = r.member_seq
;
--------------------------------------------------------
--  DDL for View VWSELECTBOOK
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LMS"."VWSELECTBOOK" ("SEQ", "도서번호", "분류", "도서명", "위치") AS 
  select 
b.seq,
bs.book_code as 도서번호,
dc.item as 분류,
b.book_name as 도서명,
bs.book_location as 위치
    from tblBook b
      inner join tblDecimalCategory dc
        on dc.num = b.decimal_num
            inner join tblBookstate bs
                on bs.book_seq = b.seq
;
--------------------------------------------------------
--  DDL for View VWSELECTBOOKLIST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LMS"."VWSELECTBOOKLIST" ("번호", "도서코드", "분류", "출판사", "저자", "도서명") AS 
  select 
b.seq as 번호,
bs.book_code as 도서코드, 
b.decimal_num as 분류, 
b.publisher as 출판사, 
b.author as 저자, 
b.book_name as 도서명
from tblBook b
inner join tblBookState bs
on b.seq=bs.book_seq
;
--------------------------------------------------------
--  DDL for View VWSHOWRENTLIST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LMS"."VWSHOWRENTLIST" ("RENTSEQ", "MSEQ", "BNAME", "BPUBLI", "RENTD", "RETURND", "EXTEN", "EXPECT_RETURND", "OVERDUE") AS 
  select 
    r.seq rentseq,
    r.member_seq mseq,
    b.book_name bname,
    b.publisher bpubli,
    r.rent_date rentd,
    r.return_date returnd,
    r.extension_count exten,
    r.rent_date+(r.extension_count+1)*7 expect_returnd,
    case
        when r.return_date is null then sysdate-(r.rent_date+(r.extension_count+1)*7)
        when r.return_date is not null then r.return_date-(r.rent_date+(r.extension_count+1)*7)
    end overdue
from tblrent r 
    inner join tblbookstate bs 
        on bs.book_code = r.book_code
    inner join tblbook b
        on b.seq = bs.book_seq
where b.delete_exist = 0 and bs.delete_exist = 0
;
