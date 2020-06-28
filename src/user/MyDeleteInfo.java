package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import admin.DBUtil;
import oracle.jdbc.internal.OracleTypes;

/**
 * 
 * @author Sujin Shin
 * 회원 마이페이지의 "회원 탈퇴 클래스"
 * 
 */
public class MyDeleteInfo {
	
	/**
	 * 
	 * 회원 탈퇴를 진행하는 메소드
	 * - 현재 대여 내역을 먼저 확인
	 * - 현재 대여 중인 도서가 있으면 탈퇴할 수 없음
	 * @param memberUser 로그인한 회원의 객체
	 * @return 회원 탈퇴 여부 확인
	 * 
	 */
	public int withdrowal(MemberUser memberUser) {
		
		Scanner sc = new Scanner(System.in);
		
		MemberUser user = memberUser;
		int seq = user.getNum();	// 회원번호
		int result = 0;		// 탈퇴 여부 확인용 변수
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		CallableStatement callstat = null;
		ResultSet rs = null;
		
		// 현재 대여 내역을 저장할 ArrayList
		List<String> rentlist = new ArrayList<String>();
		
		conn = util.open("localhost", "lms", "java1234");
		
		try {
			
			// 현재 대여내역이 있는지 확인하기
			String sql = "{ call procshowNowrent(?, ?) }";
			callstat = conn.prepareCall(sql);
			
			callstat.setInt(1, seq);
			callstat.registerOutParameter(2, OracleTypes.CURSOR);
			
			callstat.executeUpdate();
			
			rs=(ResultSet)callstat.getObject(2);

			while (rs.next()) {
				String overdue = rs.getString("overdue");
				if (overdue.contains(".")) {
					overdue = overdue.substring(0,overdue.indexOf("."));
				}
				String list = String.format("%s▣%s▣%s▣%s▣%s▣%s"
						, rs.getString("bname")
						, rs.getString("bpubli")
						, rs.getString("rentd").split(" ")[0]
						, rs.getString("expect_returnd").split(" ")[0]
						, rs.getString("exten")
						, overdue);
				rentlist.add(list);
			}
			
			if (rentlist.size() > 0) {
				
				// 대여중 도서가 있기 때문에 탈퇴 불가능
				
				System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println();
				System.out.println("[번호]\t[대여일]\t[반납예정일] [연장횟수] [연체일수]\t\t\t[책제목]\t\t\t\t\t[출판사]");
		
				for (int i=1; i<=rentlist.size(); i++) {
					
					String[] list = rentlist.get(i-1).split(",");
					
					list = rentlist.get(i-1).split("▣");
					int ext = Integer.parseInt(list[5]);
					
					if(ext <= 0) {
						System.out.printf("  %d\t%s\t%s\t  %s\t   %s\t\t%-60s%-30s\r\n"
								, i, list[2], list[3], list[4], "0", list[0], list[1]);					
					} else {
						System.out.printf("  %d\t%s\t%s\t  %s\t   %s\t\t%-60s%-30s\r\n"
								, i, list[2], list[3], list[4], list[5], list[0], list[1]);
					}
					
				}
				System.out.println();
				System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t현재 위와 같이 대여중인 책이 있어서 회원 탈퇴를 하실 수 없습니다.");
				System.out.println("\t\t\t계속 하시려면 아무 키나 입력하세요.");
				sc.nextLine();
				System.out.println();
				
			} else {
				
				// 회원 탈퇴 진행
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t회원 탈퇴 시 더 이상 도서관 서비스를 이용할 수 없습니다.");
				System.out.print("\t\t\t정말로 탈퇴하시겠습니까? (y/n)");
				String input = sc.nextLine();
				System.out.println();
				
				if (input.toLowerCase().equals("y")) {
					
					// y, Y 입력시
					sql = "{ call procDeleteMember(?, ?) }";
					callstat = conn.prepareCall(sql);
					
					callstat.setInt(1, seq);
					callstat.registerOutParameter(2, OracleTypes.NUMBER); //회원 탈퇴가 잘 되었는지 확인하는 값
					
					callstat.executeUpdate();
					
					if (callstat.getString(2).equals("1")) {
						
						// 회원 탈퇴 완료
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println("\t\t\t 지금까지 저희 도서관을 이용해 주셔서 감사합니다.");
						System.out.println("\t\t\t 회원 탈퇴를 완료하였습니다.");
						System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
						sc.nextLine();
						result = 1;
						
					} else {
						
						// 회원 탈퇴 실패
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println("\t\t\t 회원 탈퇴를 실패하였습니다.");
						System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
						sc.nextLine();
					}
					
				} else if (input.toLowerCase().equals("n")) {
					
					// n, N 입력시
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t 회원 탈퇴를 취소하였습니다.");
					System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
					sc.nextLine();
					
				} else {
					
					// 잘못된 입력값
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t 잘못된 값을 입력하였습니다.");
					System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
					sc.nextLine();
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result; // 1이면 탈퇴회원, 0이면 탈퇴하지 않은 회원
		
	}

}
