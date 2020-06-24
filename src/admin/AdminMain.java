package admin;

import java.util.Scanner;


public class AdminMain {

	public void mainmenu(AdminUser adminUser) {
		
		
		AdminUser user = adminUser;
		

		Scanner scan = new Scanner(System.in);
		String num = "";
		
		//관리자 메인메뉴
		while (true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 도서 관리");
			System.out.println("\t\t\t2. 회원 관리");
			System.out.println("\t\t\t3. 문의게시판 관리");
			System.out.println("\t\t\t0. 로그아웃");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력: ");
			num = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			// 사용자에게 번호 입력받음

			// 도서 관리
			if (num.equals("1")) {
				AdminBook book = new AdminBook();
				book.bookMenu();
			}
			// 회원 관리
			else if (num.equals("2")) {
				AdminMember member = new AdminMember();
				member.memberMenu();
			}
			// 문의게시판 관리
			else if (num.equals("3")) {
				AdminBoard board = new AdminBoard();
				board.boardMenu();
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
	
}//AdminMain
