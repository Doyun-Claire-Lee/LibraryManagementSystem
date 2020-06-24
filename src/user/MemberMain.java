package user;

import java.util.Scanner;

import admin.AdminUser;

public class MemberMain {

	public void mainmenu(MemberUser memberUser) {
		
		
		MemberUser user = memberUser;
		

		Scanner scan = new Scanner(System.in);
		String num = "";
		
		//회원 메인메뉴
		while (true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 도서 검색 및 대여");
			System.out.println("\t\t\t2. 문의게시판");
			System.out.println("\t\t\t3. 마이페이지");
			System.out.println("\t\t\t0. 로그아웃");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력: ");
			num = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			// 사용자에게 번호 입력받음

			// 도서 검색 및 대출
			if (num.equals("1")) {
				//해당 연결 클래스
			}
			// 문의게시판
			else if (num.equals("2")) {
				//해당 연결 클래스
			}
			// 마이페이지
			else if (num.equals("3")) {
				//해당 연결 클래스
			}
			// 뒤로 가기
			else if (num.equals("0")) {
				break;
			}
			// 예외
			else {
				System.out.println("\t\t\t번호를 다시 입력해주세요");
			}
		}

	}
	
}
