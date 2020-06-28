package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import admin.DBUtil;
import oracle.jdbc.internal.OracleTypes;

/**
 * 
 * @author shin
 * 회원정보를 가지고 회원번호 seq 를 돌려준다
 */

public class MemberTransSeq {
	
	public int userSeq(String ssn, String tel) {
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		int userNum = -1;//유저의 키(seq) 번호
		
		try {
			
			
			String sql = "{call procGetUserSeq(?,?,?)}";//프로시저 불러주기
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			
			stat.setString(1, ssn);//주민번호
			stat.setString(2, tel);//전화번호
			stat.registerOutParameter(3, OracleTypes.CURSOR);//커서 넣어주기!
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(3);

			
			while(rs.next()) {
				
				userNum = rs.getInt(1);

			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("뭔가 잘못됨");
		}
		
		return userNum;
		
		
	}
	
	public static void main(String[] args) {//확인용
		
		MemberTransSeq mt = new MemberTransSeq();
		System.out.println(mt.userSeq("920508-1711683","010-3268-4411"));
		
		
	}

}
