package user;

import java.util.Scanner;

public class MemberBook {
	//회원 - 도서 검색 및 대여
	
	public void bookMenu(MemberUser memberUser) {

		MemberUser user = memberUser;
		
		while (true) {
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t          도서 검색 및 대여");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 저자별 검색"); 
			System.out.println("\t\t\t2. 제목별 검색"); 
			System.out.println("\t\t\t3. 출판사별 검색"); 
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String num = sc.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			// 사용자에게 번호 입력받음

			// 저자별 검색
			if (num.equals("1")) {
				
			}
			// 제목별 검색
			else if (num.equals("2")) {
				
			}
			// 출판사별 검색
			else if (num.equals("3")) {
				
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
		
		
	}//bookMenu
	
	
}
