import java.util.Scanner;

import admin.AdminUser;
import user.MemberUser;

public class Main {
		
	//메인메뉴
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
			
		while (true) {
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t  도서관 운영 관리 시스템");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
			System.out.println("\t\t\t1. 관리자 로그인");
			System.out.println("\t\t\t2. 회원 로그인");
			System.out.println("\t\t\t3. 회원가입");
			System.out.println("\t\t\t0. 종료");
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력: ");
			
			String num = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
			//관리자 로그인
			if (num.equals("1")) {
				AdminUser adminUser = new AdminUser();
				adminUser.login(adminUser);
			}
			//회원 로그인
			else if (num.contentEquals("2")) {
				MemberUser memberUser = new MemberUser();
				memberUser.login(memberUser);
				
			}
			else if (num.contentEquals("3")) {
				//회원가입
				
			}
			//종료
			else if (num.equals("0")) {
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t프로그램을 종료합니다."); // needMore
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				break;
			}
			//예외
			else {
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}	
			
		}//while
			
	}
	
}//main
