package user;

import java.util.Scanner;

public class MemberPage {
	//회원 - 마이페이지
	
	public int pageMenu(MemberUser memberUser) {
		
		int result = 0;
		
		while (true) {
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t            마이페이지");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 대여 내역 조회"); 
			System.out.println("\t\t\t2. 연체 내역 조회"); 
			System.out.println("\t\t\t3. 연체 규정 조회"); 
			System.out.println("\t\t\t4. 회원정보 수정"); 
			System.out.println("\t\t\t5. 회원 탈퇴"); 
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String num = sc.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			// 사용자에게 번호 입력받음

			// 대여 내역 조회
			if (num.equals("1")) {
				MyShowRent rent = new MyShowRent();
				rent.menu(memberUser);
			}
			// 연체 내역 조회
			else if (num.equals("2")) {
				MyShowOverdue overdue = new MyShowOverdue();
				overdue.show(memberUser);
			}
			// 연체 규정 조회
			else if (num.equals("3")) {
				MyShowOverdueRule rule = new MyShowOverdueRule();
				rule.showRule();
			}
			// 회원정보 수정
			else if (num.equals("4")) {
				MyUpdateInfo update = new MyUpdateInfo();
				update.menu(memberUser);
			}
			// 회원 탈퇴
			else if (num.equals("5")) {
				MyDeleteInfo delete = new MyDeleteInfo();
				result = delete.withdrowal(memberUser);
				// 탈퇴된 회원인 경우 이전메뉴
				if(result == 1) {
					break;
				}
			}
			// 뒤로 가기
			else if (num.equals("0")) {
				break;
			}
			// 예외
			else {
				System.out.println("\t\t\t번호를 다시 입력해주세요");
			}
			
		}//while
		
		return result;
		
	}//pageMenu
	
	//1. 대여 내역 조회
	
	
	
	
	
}
