package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import admin.DBUtil;
import oracle.jdbc.internal.OracleTypes;

/**
 * 책 번호를 넣으면 정보가 상세하게 나오는 클래스
 * @author shin
 *
 */
public class MemberBookInformationPrint {
	
	/**
	 * 책 보여주는것
	 * @param bookSeq 도서정보번호
	 */
	public void bookPrint(int bookSeq) {
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		try {
			
			String sql = "{call procPrintForm(?,?)}";//프로시저 불러주기
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, bookSeq);//도서정보번호
			stat.registerOutParameter(2, OracleTypes.CURSOR);//커서 넣어주기!
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			
			System.out.println("\t\t\t[책이름]\t\t[출판사]\t[저자]");
			while(rs.next()) {
				
				System.out.printf("\t\t\t%s\t\t%s\t%s\n",
						rs.getString(1),//책이름
						rs.getString(2),//출판사
						rs.getString(3));//저자
	
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("오류발생");
		}
		
		
	}

}
