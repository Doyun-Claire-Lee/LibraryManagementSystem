--------------------------------------------------------
--  DDL for Procedure PROCADDBLACKMEMBER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCADDBLACKMEMBER" (
    presult out sys_refcursor
)
is
begin
open presult for 
    select * from tblrent 
        where (return_date - rent_date >= 8 and extension_count = 0)
            or (return_date - rent_date >= 15 and extension_count=1)
            or (return_date - rent_date >= 22 and extension_count = 2);       
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDBOOK
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCADDBOOK" (
 pName VARCHAR2, --도서정보 제목
 pPublisher VARCHAR2, --도서정보 출판사
 pAuthor VARCHAR2, --도서정보 저자
 pDemical Number, -- 도서정보 십진분류번호
 pNum Number, --도서정보 시리즈 번호
 pCode Varchar2,
 plocation varchar2
 )
is
    pbookseq number;
begin
    -- Insert tblBook
    insert into tblBook(seq, book_name, publisher, author, decimal_num, series_num, delete_exist) 
        values (Book_seq.nextVal,pName,pAuthor,pPublisher,pDemical,PNum,'0');
    -- Book_seq를 찾아옴
    select max(seq) into pbookseq from tblBook;
    -- Insert tblBookState
    insert into tblBookstate(book_code,book_seq,book_location,delete_exist) values
        (pCode, pbookseq, plocation, '0');

    commit;

exception
    when others then
        rollback;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDBOOKSTATE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCADDBOOKSTATE" (
pCode varchar2, --도서코드
pbookseq number, --tblbook seq
plocation varchar2 --위치
)
is
begin
insert into tblBookstate(book_code,book_seq,book_location,delete_exist) values
(pCode,pbookseq, plocation, '0');
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDMEMBER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCADDMEMBER" (
    pname varchar2,
    ptel varchar2,
    paddress varchar2,
    pssn varchar2,
    presult out number
)
is
    vcount number;  --중복 주민번호가 있는지 확인해줄 변수
begin

    --입력받은 주민번호와 동일한 값이 있는지 확인
    select count(*) into vcount from tblMember where ssn = pssn;

    --동일한 값이 있으면 presult값에 0을 대입하고 아무 행동도 하지 않음
    if vcount > 0 then 
        presult := 0;
    --동일한 값이 없으면 회원테이블에 데이터 추가
    else
        insert into tblMember values (member_seq.nextval, pname, ptel, paddress, pssn, 0);
        presult := 1;
    end if;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCCHECKRESERVE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCCHECKRESERVE" (    
    pDATE date, -- 예약날짜 입력받기
    
    presult out SYS_REFCURSOR
)
is
begin
    open presult for
    select * from vwReservation where 예약날짜 = pDATE;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCDECIMALBIG
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCDECIMALBIG" ( 
    presult out sys_refcursor
)
is
begin
    open presult for 
    select * from tblDecimalCategory where num between 100 and 999;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCDECIMALMIDDLE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCDECIMALMIDDLE" ( 
    presult out sys_refcursor
)
is
begin
    open presult for 
    select * from tblDecimalCategory where num between 10 and 99;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCDECIMALSMALL
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCDECIMALSMALL" ( 

    presult out sys_refcursor
)
is
begin
    open presult for 
    select * from tblDecimalCategory where num between 1 and 9;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCDELETEMEMBER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCDELETEMEMBER" (
    pseq number, -- 회원 번호
    presult out number
)
is
    pwith number;
begin
    select withdrawal into pwith from tblmember where seq=pseq;
    if pwith = 0 then
        update tblmember set withdrawal = 1 where seq=pseq;
        presult := 1;
    else
        presult := 0;
    end if;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCGETMYSUGGESTIONS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCGETMYSUGGESTIONS" (
    presult out SYS_REFCURSOR,
    pmember_seq number
)
is
begin
    open presult for
     select sug.seq 글번호, sug.title 제목, mem.name 이름, to_char(sug.regdate,'yyyymmdd') 날짜 ,sug.sug_contents 건의사항, sug.answer 답변, mem.tel 전화번호
    from tblsuggestions sug
        inner join tblmember mem
            on sug.member_seq=mem.seq
            where sug.delete_sug=0 and mem.seq=pmember_seq
            order by sug.regdate desc;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCGETSUGGESTIONS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCGETSUGGESTIONS" (
    presult out SYS_REFCURSOR
)
is
begin
    open presult for
    select sug.seq 글번호, sug.title 제목, mem.name 이름, to_char(sug.regdate,'yyyymmdd') 날짜 ,sug.sug_contents 건의사항, nvl(sug.answer,'답변') 답변, mem.tel 전화번호
    from tblsuggestions sug
        inner join tblmember mem
            on sug.member_seq=mem.seq
            where sug.delete_sug=0
            order by sug.regdate desc;
end;       

/
--------------------------------------------------------
--  DDL for Procedure PROCGETSUGGESTIONSBYKEYWORD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCGETSUGGESTIONSBYKEYWORD" 
(
    presult out SYS_REFCURSOR,
    pkeyword varchar2
)
is
begin
    open presult for
    select sug.seq 글번호, sug.title 제목, mem.name 이름, to_char(sug.regdate,'yyyymmdd') 날짜 ,sug.sug_contents 건의사항, sug.answer 답변, mem.tel 전화번호
    from tblsuggestions sug
        inner join tblmember mem
            on sug.member_seq=mem.seq
            where sug.title like '%'||pkeyword||'%' and sug.delete_sug=0 
            order by sug.regdate desc;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCGETSUGGESTIONSBYMEM_SEQ
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCGETSUGGESTIONSBYMEM_SEQ" (
    presult out SYS_REFCURSOR,
    pmember_seq number
)
is
begin
    open presult for
    select sug.seq 글번호, sug.title 제목, mem.name 이름, to_char(sug.regdate,'yyyymmdd') 날짜 ,sug.sug_contents 건의사항, sug.answer 답변, mem.tel 전화번호
        from tblsuggestions sug
            inner join tblmember mem
                on sug.member_seq=mem.seq
                where mem.seq=pmember_seq and sug.delete_sug=0
                order by sug.regdate desc;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCGETSUGGESTIONSBYSUG_SEQ
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCGETSUGGESTIONSBYSUG_SEQ" (
    presult out SYS_REFCURSOR,
    psug_seq number
)
is
begin
    open presult for
    select sug.seq 글번호, sug.title 제목, mem.name 이름, to_char(sug.regdate,'yyyymmdd') 날짜 ,sug.sug_contents 건의사항, sug.answer 답변, mem.tel 전화번호
        from tblsuggestions sug
            inner join tblmember mem
                on sug.member_seq=mem.seq
                where sug.seq=psug_seq and sug.delete_sug=0
                order by sug.regdate desc;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCGETSUGGESTIONSNOANSWER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCGETSUGGESTIONSNOANSWER" (
    presult out SYS_REFCURSOR
)
is
begin
    open presult for
    select sug.seq 글번호, sug.title 제목, mem.name 이름, to_char(sug.regdate,'yyyymmdd') 날짜 ,sug.sug_contents 건의사항, sug.answer 답변, mem.tel 전화번호
        from tblsuggestions sug
            inner join tblmember mem
                on sug.member_seq=mem.seq
                where sug.answer is null and sug.delete_sug=0
                order by sug.regdate desc;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCGETUSERSEQ
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCGETUSERSEQ" 
(
    pssn varchar2,--주민번호(tel)
    ptel varchar2,--전화번호(id)
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for
        select seq from tblmember where substr(ssn,8,7) = pssn and tel = ptel; 
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCLISTODRESERVATION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCLISTODRESERVATION" 
(
    pseq number,--회원번호
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for
        select 
            seq,--예약번호
            member_seq,--회원번호
            book_seq,--책번호
            res_date--예약날짜
        from tblReservation 
        where member_seq = pseq;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCLISTOFRES
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCLISTOFRES" 
(
    pbooknum number,--책 번호
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for
        select 
        member_seq,--멤버번호
        res_date--예약날짜
    from tblReservation
        where book_seq = pbooknum and res_date > sysdate order by res_date;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCNOTRETURNBOOK
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCNOTRETURNBOOK" 
(   
    presult out sys_refcursor--출력커서
)
is
begin
   open presult for 
    select
    tb.seq,--도서정보코드
    tbs.book_code,--도서코드
    tr.return_date,--반납일
    tb.book_name,--책 이름
    tr.member_seq--회원번호
    from tblBook tb
        inner join tblBookState tbs
            on tb.seq = tbs.book_seq
                inner join tblRent tr
                    on tr.book_code = tbs.book_code
                        where tr.return_date >= sysdate;    
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCNOTRETURNBOOKLIST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCNOTRETURNBOOKLIST" 
(
    pseq number,--회원번호
    presult out sys_refcursor--결과커서
)
is
begin
    open presult for
        select 
    tm.seq,--회원번호
    tm.name,--회원이름
    tbs.book_code,--책 코드
    tb.seq,--도서정보 코드
    tb.book_name,--책 이름
    tr.rent_date,--빌린일자
    tr.return_date--반납일자
from tblmember tm
    inner join tblrent tr
        on tr.member_seq = tm.seq
            inner join tblBookState tbs
                on tbs.book_code = tr.book_code
                    inner join tblBook tb
                        on tb.seq = tbs.book_seq
            where tr.return_date >= sysdate and tm.seq = pseq;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTFORM
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCPRINTFORM" 
(
    pseq number,--도서정보번호
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for
        select 
        book_name,--책 이름
        publisher,--출판사
        author--저자
        from tblBook 
        where seq = pseq;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTLATELISTBYBOOK
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCPRINTLATELISTBYBOOK" (
-- 책 이름으로 연체 대여 내역 찾기
-- 책이름을 먼저 %문자 써서 검색하여 책 번호 보여주기!
    pseq number,     --입력받은 책의 번호
    presult out sys_refcursor       --결과셋
)
is
begin
open presult for 
select 
    (select name from tblmember where r.member_seq = seq) as member,
    b.book_name as bookName,
    to_char(r.rent_date, 'yyyy-mm-dd') as rent,
    to_char(r.return_date, 'yyyy-mm-dd') as return,
    r.extension_count as extension,
    (r.return_date - r.rent_date) - (7*(r.extension_count + 1)) as lateDate
        from tblrent r
            inner join tblbookstate bs
             on r.book_code = bs.book_code
                inner join tblbook b
                    on bs.book_seq = b.seq
                        where  b.seq = pseq
                            and  ((((return_date is null and sysdate - rent_date >= 8) or (return_date - rent_date >= 8)) and extension_count = 0)
                            or (((return_date is null and sysdate - rent_date >= 15) or (return_date - rent_date >= 15)) and extension_count = 1)
                            or (((return_date is null and sysdate - rent_date >= 22) or (return_date - rent_date >= 22)) and extension_count = 2))
                                order by r.rent_date;  
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTLATELISTBYDATE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCPRINTLATELISTBYDATE" (
--시작 날짜와 종료 날짜 입력받아 연체 리스트 조회하기(대여일 기준)
     psdate varchar2,     --입력받은 시작 날짜(yyyy-mm-dd)
     pedate varchar2,     --입력받은 종료 날짜(yyyy-mm-dd)
     presult out sys_refcursor       --결과셋
)
is
begin
open presult for 
select 
    (select name from tblmember where r.member_seq = seq) as member,
    b.book_name as bookName,
    to_char(r.rent_date, 'yyyy-mm-dd') as rent,
    to_char(r.return_date, 'yyyy-mm-dd') as return,
    r.extension_count as extension,
    (r.return_date - r.rent_date) - (7*(r.extension_count + 1)) as lateDate
        from tblrent r
            inner join tblbookstate bs
             on r.book_code = bs.book_code
                inner join tblbook b
                    on bs.book_seq = b.seq
                        where  (rent_date >= to_date(psdate, 'yyyy-mm-dd') and rent_date < (to_date(pedate, 'yyyy-mm-dd') + 1))
                            and ((((return_date is null and sysdate - rent_date >= 8) or (return_date - rent_date >= 8)) and extension_count = 0)
                            or (((return_date is null and sysdate - rent_date >= 15) or (return_date - rent_date >= 15)) and extension_count = 1)
                            or (((return_date is null and sysdate - rent_date >= 22) or (return_date - rent_date >= 22)) and extension_count = 2))
                                order by r.rent_date;  
end;


/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTLATELISTBYMEMBER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCPRINTLATELISTBYMEMBER" (
--회원 번호로 연체 대여 내역 찾기
--회원이름을 먼저 %문자 써서 검색하여 회원번호 보여주기!
    pseq number,     --입력받은 멤버의 번호
    presult out sys_refcursor       --결과셋
)
is
begin
open presult for 
    select 
            m.name as member,       
            (select b.book_name from tblbook b 
                inner join tblbookstate bs 
                    on b.seq = bs.book_seq
                        where bs.book_code = r.book_code) as bookName,
            to_char(r.rent_date, 'yyyy-mm-dd') as rent,
            to_char(r.return_date, 'yyyy-mm-dd') as return,
            r.extension_count as extension,
            (r.return_date - r.rent_date) - (7*(r.extension_count + 1)) as lateDate
                    from tblrent r
                        inner join tblMember m
                            on r.member_seq = m.seq
                                where  m.seq = pseq
                                    and  ((((return_date is null and sysdate - rent_date >= 8) or (return_date - rent_date >= 8)) and extension_count = 0)
                                        or (((return_date is null and sysdate - rent_date >= 15) or (return_date - rent_date >= 15)) and extension_count = 1)
                                        or (((return_date is null and sysdate - rent_date >= 22) or (return_date - rent_date >= 22)) and extension_count = 2))
                                            order by r.rent_date;       
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPUBLISHERDUPLICATIONDELETE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCPUBLISHERDUPLICATIONDELETE" 
(
    pname varchar2,--출판사 이름
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for 
        select
            distinct tb.book_name,--책 이름
            tb.author,--저자이름
            tb.publisher,--출판사
            tb.delete_exist,--제거여부
            tbs.book_seq,--도서정보번호
            tbs.book_location--도서위치
        from tblBook tb
            inner join tblBookState tbs
                on tbs.book_seq = tb.seq
                    where tb.publisher like '%' || pname || '%';
end; 

/
--------------------------------------------------------
--  DDL for Procedure PROCPUBLISHERSEARCH
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCPUBLISHERSEARCH" (
    pname varchar2,--출판사 이름
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for 
        select
            *
        from tblBook tb
            inner join tblBookState tbs
                on tbs.book_seq = tb.seq
                    where publisher like '%' || pname || '%';      
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCRENTALINSERT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCRENTALINSERT" (
    pmember number,
    pbookcode varchar2
--    presult number
)
is
    pbcodecheck varchar2(100);
begin
    select book_code into pbcodecheck from tblbookstate where book_code = pbookcode;
    insert into tblRent(seq, member_seq, book_code, rent_date, return_date, extension_count)
    values (Rent_seq.nextval, pmember, pbcodecheck, sysdate, null, '0');
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCRENTPOSSIBLE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCRENTPOSSIBLE" 
(
    pbooknum number,--도서정보번호
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for
        select
        tr.return_date,--반납일
        tr.book_code--도서코드
        from tblRent tr
            inner join tblBookState tbs
                on tbs.book_code = tr.book_code
        where tbs.book_seq = pbooknum and return_date >= sysdate;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCRESERVEBOOK
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCRESERVEBOOK" 
(
   puserSeq number,-- 회원번호
   pbookNum number,-- 도서번호
   pyear varchar2,-- 예약 년도
   pmon varchar2,-- 예약 월
   pdate varchar2-- 예약 일
)
is
begin
    insert into tblReservation values (res_seq.nextVal,puserSeq,pbookNum,to_date(pyear || '-' || pmon || '-' || pdate,'yyyy-mm-dd'),0);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCRETRUNCHECK
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCRETRUNCHECK" (
    pcode varchar2,
    presult out sys_refcursor
)
is
begin
    open presult
        for select * from vwCheckReturnBook where 도서코드 = pcode and 반납여부 = 'N';
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSELECTBLACKFROMRENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCSELECTBLACKFROMRENT" (
    presult out sys_refcursor
)
is
begin
open presult for
    select * from vwblackfromrent;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSELECTBOOKLIST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCSELECTBOOKLIST" (    
    pcode varchar2, -- 도서코드 입력받기
    presult out SYS_REFCURSOR
)
is
begin
    open presult for
    select * from vwSelectBookList where 도서코드 = pcode;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSETDELSUGGESTION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCSETDELSUGGESTION" 
(
    pseq number
)
is
begin
    update tblsuggestions set delete_sug=1 where seq=pseq;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSETSUGGESTION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCSETSUGGESTION" (
    pseq number,
    ptitle varchar2,
    pcontents varchar2
)
is
begin
    insert into tblsuggestions(seq,member_seq,title,sug_contents,regdate,delete_sug)  values((select max(seq)+1 from tblsuggestions),pseq,ptitle,pcontents,sysdate,0);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSETSUGGESTIONANSWER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCSETSUGGESTIONANSWER" (
    pseq number,
    panswer varchar2
)
is
begin
    update tblsuggestions set answer=panswer where seq=pseq;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSETSUGGESTIONUPDATE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCSETSUGGESTIONUPDATE" (
    pseq number,
    ptitle varchar2,
    pcontents varchar2
)
is
begin
    update tblsuggestions set title=(case when ptitle is null then title else ptitle end), sug_contents=(case when pcontents is null then sug_contents else pcontents end), regdate=sysdate where seq=pseq;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSHOWNOWRENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCSHOWNOWRENT" (
    pseq number,  -- 회원번호
    presult out sys_refcursor
)
is
begin
    open presult for
        select * from vwshowrentlist where mseq = pseq and returnd is null;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSHOWOVERDUE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCSHOWOVERDUE" (
    pseq number,  -- 회원번호
    pstart date,
    pend date,
    presult out sys_refcursor
)
is
begin
    open presult for
        select * from vwshowrentlist where mseq = pseq and rentd between pstart and pend and overdue > 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSHOWPREVIEWRENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCSHOWPREVIEWRENT" (
    pseq number,  -- 회원번호
    pstart date,
    pend date,
    presult out sys_refcursor
)
is
begin
    open presult for
        select * from vwshowrentlist 
        where rentd between pstart and pend and mseq = pseq and returnd is not null;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSUBJECTDUPLICATIONDELETE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCSUBJECTDUPLICATIONDELETE" 
(
    pname varchar2,--책 이름
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for 
        select
            distinct tb.book_name,--책 이름(1)
            tb.author,--저자이름2
            tb.publisher,--출판사3
            tb.delete_exist,--제거여부4
            tbs.book_seq,--도서정보번호5
            tbs.book_location--도서위치6
        from tblBook tb
            inner join tblBookState tbs
                on tbs.book_seq = tb.seq
                    where tb.book_name like '%' || pname || '%';
end;  

/
--------------------------------------------------------
--  DDL for Procedure PROCSUBJECTSEARCH
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCSUBJECTSEARCH" (
    pname varchar2,--책 제목
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for 
        select
            *
        from tblBook tb
            inner join tblBookState tbs
                on tbs.book_seq = tb.seq
                    where tb.book_name like '%' || pname || '%';      
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATEBOOK
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCUPDATEBOOK" (
    pseq number,
    pname varchar2,
    ppublisher varchar2,
    pauthor varchar2,
    pdecimal number,
    pseries number,
    pdelete number
)
is
begin
    update tblBook set book_name = pname,
                                    publisher = ppublisher,
                                    author = pauthor,
                                    decimal_num = pdecimal,
                                    delete_exist = pdelete
                                    where seq = pseq;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATEEXTENSION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCUPDATEEXTENSION" (
    pseq number, -- 대여 시퀀스
    presult out number -- 결과
)
is
    pextension number; -- 이전 연장 횟수
    pbcode varchar2(100); -- 도서 코드
    pbseq number; -- 도서 시퀀스
    pbdel number; -- 도서 삭제여부
    presdel number; -- 예약 삭제여부 
    pcount number;
begin
    select extension_count, book_code into pextension, pbcode from tblrent where seq=pseq;  -- 이전연장횟수, 도서코드 받아옴
    select book_seq into pbseq from tblbookstate where book_code=pbcode; -- 받아온 도서 코드로 도서 시퀀스 받아옴
    select delete_exist into pbdel from tblbook where seq=pbseq; -- 받아온 도서 시퀀스로 도서의 존재 여부 받아옴
    select count(*) into pcount from tblreservation where book_seq=pbseq; -- 받아온 도서 시퀀스로 예약 여부 받아옴

    if pcount > 0 then
        select delete_exist into presdel from tblreservation where book_seq=pbseq;
        if pextension >= 2 then -- 연장 이미 두번 하면 더 이상 못함
            presult := 3;
        elsif presdel = 1 and pbdel = 0 then -- 예약이 없으면 연장 성공
            update tblrent set extension_count = pextension + 1 where seq=pseq;
            presult := 1;
        elsif presdel = 0 and pbdel = 0 then -- 예약이 있으면 연장 실패
            presult := 2;
        else
            presult := 0;
        end if;
    else
        if pextension >= 2 then -- 연장 이미 두번 하면 더 이상 못함
            presult := 3;
        elsif pextension < 2 then  -- 연장 두번 미만이면 연장 성공
            update tblrent set extension_count = pextension + 1 where seq=pseq;
            presult := 1;
        else
            presult := 0;
        end if;
    end if;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATEMEMBER_ADD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCUPDATEMEMBER_ADD" (
    pseq number, -- 회원 번호
    paddress varchar2,
    presult out number
)
is
    pwith number;
begin
    select withdrawal into pwith from tblmember where seq=pseq;
    if pwith = 0 then
        update tblmember set address=paddress where seq=pseq;
        presult := 1;
    else
        presult := 0;
    end if;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATEMEMBER_TEL
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCUPDATEMEMBER_TEL" (
    pseq number, -- 회원 번호
    ptel varchar2,
    presult out number
)
is
    pwith number;
begin
    select withdrawal into pwith from tblmember where seq=pseq;
    if pwith = 0 then
        update tblmember set tel=ptel where seq=pseq;
        presult := 1;
    else
        presult := 0;
    end if;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUSERNOTRE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCUSERNOTRE" 
(
    pusernum number,--회원번호
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for 
        select
        * 
    from tblRent tr
        inner join tblBookState tbs
            on tbs.book_code = tr.book_code
        where tr.member_seq = pusernum and tr.return_date >= sysdate;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCWRITERDUPLICATIONDELETE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCWRITERDUPLICATIONDELETE" 
(
    pname varchar2,--저자 이름
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for 
        select
            distinct tb.book_name,--책 이름
            tb.author,--저자이름
            tb.publisher,--출판사
            tb.delete_exist,--제거여부
            tbs.book_seq,--도서정보번호
            tbs.book_location--도서위치
        from tblBook tb
            inner join tblBookState tbs
                on tbs.book_seq = tb.seq
                    where author like '%' || pname || '%';
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCWRITERSEARCH
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PROCWRITERSEARCH" (
    pname varchar2,--저자 이름
    presult out sys_refcursor--아웃커서
)
is
begin
    open presult for 
        select
            *
        from tblBook tb
            inner join tblBookState tbs
                on tbs.book_seq = tb.seq
                    where author like '%' || pname || '%';      
end;

/
--------------------------------------------------------
--  DDL for Procedure PRODECIMAL
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "LMS"."PRODECIMAL" ( 
    presult out sys_refcursor
)
is
begin
    open presult for 
    select * from tblDecimalCategory;
end;

/
