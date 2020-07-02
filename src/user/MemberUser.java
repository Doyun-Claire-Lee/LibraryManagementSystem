package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import admin.AdminMain;
import admin.AdminUser;
import user.MemberMain;
import user.MemberUser;
import admin.DBUtil;
import oracle.jdbc.OracleTypes;

public class MemberUser {

	int num;
	String id;
	String pw;
	boolean loginFlag = false;
	
	public void login(MemberUser memberUser) {
		
		//Database connection
		//데이터베이스 연동
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		
		// Variable for Member Info
		// 회원 계정 데이터를 넣어줄 변수
		HashMap<String, String> loginInfo = new HashMap<String, String>();
		
		try {

			conn = util.open("localhost", "lms", "java1234");
			stat = conn.createStatement();

			String sql = String.format("select * from tblmember");
			rs = stat.executeQuery(sql);
			
			// Insert info to loginInfo map
			// 데이터 입력
			while (rs.next()) {
				
				loginInfo.put(rs.getString("tel"), rs.getString("ssn").substring(rs.getString("ssn").indexOf('-') +1) + "," + rs.getString("seq"));
				
				
			}
			// input id,pw
			// 사용자에게 id,pw 입력받기
			System.out.print("\t\t\t▷ ID: ");
			String inputId = scan.nextLine();
			System.out.print("\t\t\t▷ PW: ");
			String inputPw = scan.nextLine();
			System.out.println();


			// loginInfo search
			// id 탐색
			for (String id : loginInfo.keySet()) {

				// id matching
				if (id.equals(inputId)) {

					// password get
					String pw = loginInfo.get(id).split(",")[0];
					int num = Integer.parseInt(loginInfo.get(id).split(",")[1]);
					

					// password matching
					if (pw.equals(inputPw)) {
						
						
						MemberMain memberMain = new MemberMain();
						
						// set info
						memberUser.setId(id);
						memberUser.setPw(pw);
						memberUser.setNum(num);
						
						
						//탈퇴 회원 여부 확인
						String sqlDrawal = String.format("select * from tblmember where seq = %s", memberUser.getNum());
						stat = conn.prepareCall(sqlDrawal);
						rs = stat.executeQuery(sqlDrawal);
					
						//회원탈퇴 출력
						while(rs.next()) {
						
							
								if ( rs.getString("withdrawal").equals("0")) {
								
								System.out.println("\t\t\t탈퇴 처리가 된 회원 계정입니다.");
								System.out.println("\t\t\t엔터를 입력하면 이전 메뉴로 돌아갑니다.");
								scan.nextLine();								
								
								break;
								
								};
								
								
								
								// login on
								memberUser.loginFlag = true;
								
								
								//도서관 방문 인사 및 회원정보 출력
								memberLoginMessage(memberUser);
								
								// mainmenu method
								// 메인메뉴 메소드 실행
								memberMain.mainmenu(memberUser);
							
								
						}
						
						
						// Database connection close
						stat.close();
						conn.close();

					}
				} 
			}
			
			
			// when enter wrong info
			if(!memberUser.loginFlag) {
				
				System.out.println("\t\t\t아이디 혹은 비밀번호를 잘못 입력하셨습니다.");					
				memberUser.login(memberUser);
				
			} 
			// logout
			else {
				System.out.println("\t\t\t로그아웃을 진행합니다.");
				scan.nextLine();
				memberUser.loginFlag = false;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}//login
	

	private void memberLoginMessage(MemberUser memberUser) {
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		try {
			
			
			//프로시저 호출 준비
			conn = util.open("localhost","lms","java1234");
			
			//로그인 성공 시 해당 회원번호와 일치하는 데이터가져오기
			String sql = String.format("select * from tblmember where seq = %s", memberUser.getNum());
			stat = conn.prepareCall(sql);
			
			rs = stat.executeQuery(sql);
			
			//도서관 방문 인사 및 회원정보 출력하기
			while(rs.next()) {
				
				System.out.println();
				System.out.println();
				System.out.printf("\t\t\t%s회원님 도서관 방문을 환영합니다.\n"
						, rs.getString("name")
						, rs.getString("tel"));
				
				System.out.printf("\t\t\t| 이름: %s   연락처 : %s |\n "
													, rs.getString("name")
													, rs.getString("tel"));
			}

			//접속종료
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}



	// getter & setter
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
	
	
	
	
	
	
	
	
}//MemberUser
