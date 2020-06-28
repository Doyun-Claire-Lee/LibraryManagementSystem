package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import admin.DBUtil;
import oracle.jdbc.internal.OracleTypes;

/**
 * 
 * @author Sujin Shin
 * 회원 마이페이지의 "회원 정보 수정 클래스"
 * 
 */
public class MyUpdateInfo {

	/**
	 * 
	 * 회원 정보를 수정하는 메소드
	 * - 비밀번호를 입력하여 본인 확인
	 * - 연락처와 주소 수정 가능
	 * - 무엇을 수정할 것인지 선택하여 하나씩 수정할 수 있음
	 * @param memberUser 로그인한 회원의 객체
	 * 
	 */
	public void menu(MemberUser memberUser) {
		
		MemberUser user = memberUser;
		int seq = user.getNum();	// 회원번호
		String pw_check = user.getPw();
		
		// 이전 정보를 저장할 변수
		String pre_tel = "";
		String pre_add = "";
		
		Scanner sc = new Scanner(System.in);
		
		// 개인정보 수정을 위한 본인 확인 > 비밀번호로
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("\t\t\t 본인 확인을 위해 비밀번호를 입력하세요.");
		System.out.println("\t\t\t 이전 메뉴로 가시려면 0을 입력하세요.");
		
		while (true) {
			
			System.out.print("\t\t\t > 비밀번호 입력 : ");
			String pw = sc.nextLine();
			
			if(pw.equals("0")) {
				
				// 이전메뉴
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t 입력을 취소하였습니다. 이전 메뉴로 돌아가겠습니다.");
				System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
				sc.nextLine();
				break;
				
			} else if (pw.equals(pw_check)) {
				
				// 본인 확인 성공
				
				Connection conn = null;
				Statement stat = null;
				ResultSet rs = null;
				DBUtil util = new DBUtil();
				
				try {
					
					conn = util.open();
					stat = conn.createStatement();
					
					while (true) {
						
						String sql = String.format("select * from tblmember where seq = %d", seq);
						rs = stat.executeQuery(sql);
						
						// 회원 기존 정보 가져옴
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println("\t\t\t[이름]\t[주민등록번호]\t[연락처]\t[주소]");
						if (rs.next()) {
							pre_tel = rs.getString("tel");
							pre_add = rs.getString("address");
							System.out.printf("\t\t\t%s\t%s\t%s\t%s\n"
									, rs.getString("name")
									, rs.getString("ssn")
									, pre_tel
									, pre_add);
						}
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					
						
						System.out.println("\t\t\t수정할 정보를 선택하세요.");
						System.out.println("\t\t\t1. 연락처");
						System.out.println("\t\t\t2. 주소");
						System.out.println("\t\t\t0. 이전메뉴");
						System.out.print("\t\t\t▷ 입력 : ");
						String input = sc.nextLine();
						System.out.println();
						
						if (input.equals("1")) {
							
							// 연락처 수정
							updateTel(memberUser, pre_tel);
							
						} else if (input.equals("2")) {
							
							// 주소 수정
							updateAdd(memberUser, pre_add);
							
						} else if (input.equals("0")) {
							
							// 이전 메뉴
							break;
							
						} else {
							
							// 잘못된 번호 입력
							System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println("\t\t\t 잘못된 번호를 입력하였습니다. 다시 입력해주세요.");
							
						}
						
					}//while
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
				
			} else {
				
				// 비밀번호 틀림
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t 잘못된 번호를 입력하였습니다. 다시 입력해주세요.");
				
			}
		}//while
	}//menu
	
	/**
	 * 
	 * 회원 전화번호 정보를 수정하는 메소드
	 * - 새 연락처 입력
	 * - 수정 여부 확인
	 * @param memberUser 로그인한 회원의 객체
	 * @param pre_tel 회원의 이전 연락처
	 * 
	 */
	private void updateTel(MemberUser memberUser, String pre_tel) {

		Scanner sc = new Scanner(System.in);
		
		MemberUser user = memberUser;
		int seq = user.getNum();	// 회원번호
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		CallableStatement callstat = null;
		
		conn = util.open();
		
		// 새 연락처 입력
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("\t\t\t수정 할 새로운 연락처를 입력하세요.");
		System.out.print("\t\t\t▷ 입력 : ");
		String tel = sc.nextLine();

		// 수정 확인 입력을 잘못했을 떄를 대비한 while loop
		while (true) {
			
			// 이전 번호 > 새 번호 : 바꿀지말지 한 번 더 물어보기
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t 연락처 변경 사항");
			System.out.printf("\t\t\t %s -> %s\n", pre_tel, tel);
			System.out.print("\t\t\t 위의 정보로 수정하시겠습니까? (y/n)");
			String input = sc.nextLine();
			
			if (input.toLowerCase().equals("y")) {
				
				// Y 입력(수정하기)
				try {
					
					// 연락처 수정
					String sql = "{ call procUpdateMember_Tel(?, ?, ?) }";
					callstat = conn.prepareCall(sql);
					
					callstat.setInt(1, seq);
					callstat.setString(2, tel);
					callstat.registerOutParameter(3, OracleTypes.NUMBER);
					
					callstat.executeUpdate();

					if (callstat.getString(3).equals("1")) {
						
						// 전화번호 수정 완료
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println("\t\t\t 정보 수정을 완료하였습니다.");
						System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
						sc.nextLine();
						
					} else {
						
						// 전화번호 수정 실패
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println("\t\t\t 정보 수정을 실패하였습니다.");
						System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
						sc.nextLine();
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
				
			} else if (input.toLowerCase().equals("n")) {
				
				// N 입력(수정취소)
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t 정보 수정을 취소하였습니다.");
				System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
				sc.nextLine();
				break;
				
			} else {
				
				// 입력 오류
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t 잘못된 번호를 입력하였습니다. 다시 입력해주세요.");
			}
		}//while
		
	}//updateTel
	
	/**
	 * 
	 * 회원 주소 정보를 수정하는 메소드
	 * - 새 주소 입력
	 * - 수정 여부 확인
	 * @param memberUser 로그인한 회원의 객체
	 * @param pre_add 회원의 이전 주소
	 * 
	 */
	private void updateAdd(MemberUser memberUser, String pre_add) {

		Scanner sc = new Scanner(System.in);
		
		MemberUser user = memberUser;
		int seq = user.getNum();	// 회원번호
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		CallableStatement callstat = null;
		
		conn = util.open();
		
		// 새 주소 입력
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("\t\t\t수정 할 새로운 주소를 입력하세요.");
		System.out.print("\t\t\t▷ 입력 : ");
		String add = sc.nextLine();

		// 수정 확인 입력을 잘못했을 떄를 대비한 while loop
		while (true) {
			
			// 이전 주소 > 새 주소 : 바꿀지말지 한 번 더 물어보기
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t 주소 변경 사항");
			System.out.printf("\t\t\t %s -> %s\n", pre_add, add);
			System.out.print("\t\t\t 위의 정보로 수정하시겠습니까? (y/n)");
			String input = sc.nextLine();
			
			if (input.toLowerCase().equals("y")) {
				
				// Y 입력(수정하기)
				try {
					
					// 주소 수정
					String sql = "{ call procUpdateMember_Add(?, ?, ?) }";
					callstat = conn.prepareCall(sql);
					
					callstat.setInt(1, seq);
					callstat.setString(2, add);
					callstat.registerOutParameter(3, OracleTypes.NUMBER);
					
					callstat.executeUpdate();
					
					if (callstat.getString(3).equals("1")) {
						
						// 주소 수정 완료
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println("\t\t\t 정보 수정을 완료하였습니다.");
						System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
						sc.nextLine();
						
					} else {
						
						// 주소 수정 실패
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println("\t\t\t 정보 수정을 실패하였습니다.");
						System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
						sc.nextLine();
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
				
			} else if (input.toLowerCase().equals("n")) {
				
				// N 입력(수정취소)
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t 정보 수정을 취소하였습니다.");
				System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
				sc.nextLine();
				break;
				
			} else {
				
				// 입력 오류
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t 잘못된 번호를 입력하였습니다. 다시 입력해주세요.");
			}
		}//while
		
	}//updateAdd
}
