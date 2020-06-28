package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import admin.DBUtil;
import oracle.jdbc.internal.OracleTypes;
/**
 * 
 * @author shin
 *
 *	아직 반납되지 않은 책 리스트에대한 클래스
 */
public class MemberNotReturnBookInfoList {
	
	/**
	 * 아직 대여되지 않은 책 리스트.
	 */
	public List<String[]> notReturnBook() {
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		
		List<String[]> notReturnList = new ArrayList<String[]>();//반납하지 않은 책 정보 새로운 객체 생성.
		
		try {
			
			
			String sql = "{call procNotReturnBook(?)}";//프로시저 불러주기
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			
			
			stat.registerOutParameter(1, OracleTypes.CURSOR);//커서 넣어주기!
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(1);

			
			while(rs.next()) {
				String[] notReturnBookList = new String[5];
				
				notReturnBookList[0] = rs.getString(1);//도서정보코드
				notReturnBookList[1] = rs.getString(2);//도서코드
				notReturnBookList[2] = rs.getString(3).substring(0,11);//반납일짜
				notReturnBookList[3] = rs.getString(4);//책이름
				notReturnBookList[4] = rs.getString(5);//빌린회원번호
				
				notReturnList.add(notReturnBookList);//반납하지 않은 책 정보 리스트에 넣어주기.
				
//				System.out.print(rs.getString(1) + " ");//도서정보코드
//				System.out.print(rs.getString(2) + " ");//도서코드
//				System.out.print(rs.getString(3).substring(0,11) + " ");//반납일짜
//				System.out.print(rs.getString(4) + " ");//책이름
//				System.out.print(rs.getString(5) + " ");//빌린회원번호
//				System.out.println();
			}
			
			stat.close();
			conn.close();
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("뭔가 잘못됨");
		}
		
		
		return notReturnList;
		
		
	}
}
