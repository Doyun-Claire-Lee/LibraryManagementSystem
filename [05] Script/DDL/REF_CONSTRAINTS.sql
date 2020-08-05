--------------------------------------------------------
--  Ref Constraints for Table TBLBLACK
--------------------------------------------------------

  ALTER TABLE "LMS"."TBLBLACK" ADD FOREIGN KEY ("MEMBER_SEQ")
	  REFERENCES "LMS"."TBLMEMBER" ("SEQ") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLBOOK
--------------------------------------------------------

  ALTER TABLE "LMS"."TBLBOOK" ADD FOREIGN KEY ("DECIMAL_NUM")
	  REFERENCES "LMS"."TBLDECIMALCATEGORY" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLBOOKSTATE
--------------------------------------------------------

  ALTER TABLE "LMS"."TBLBOOKSTATE" ADD FOREIGN KEY ("BOOK_SEQ")
	  REFERENCES "LMS"."TBLBOOK" ("SEQ") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLRENT
--------------------------------------------------------

  ALTER TABLE "LMS"."TBLRENT" ADD FOREIGN KEY ("BOOK_CODE")
	  REFERENCES "LMS"."TBLBOOKSTATE" ("BOOK_CODE") ENABLE;
  ALTER TABLE "LMS"."TBLRENT" ADD FOREIGN KEY ("MEMBER_SEQ")
	  REFERENCES "LMS"."TBLMEMBER" ("SEQ") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLRESERVATION
--------------------------------------------------------

  ALTER TABLE "LMS"."TBLRESERVATION" ADD FOREIGN KEY ("BOOK_SEQ")
	  REFERENCES "LMS"."TBLBOOK" ("SEQ") ENABLE;
  ALTER TABLE "LMS"."TBLRESERVATION" ADD FOREIGN KEY ("MEMBER_SEQ")
	  REFERENCES "LMS"."TBLMEMBER" ("SEQ") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLSUGGESTIONS
--------------------------------------------------------

  ALTER TABLE "LMS"."TBLSUGGESTIONS" ADD FOREIGN KEY ("MEMBER_SEQ")
	  REFERENCES "LMS"."TBLMEMBER" ("SEQ") ENABLE;
