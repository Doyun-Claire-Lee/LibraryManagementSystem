package admin;

import java.util.Scanner;


public class AdminBook {
	//관리자 - 도서 관리
	
	public void bookMenu() {
		
		while (true) {
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t            도서 관리");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 도서 조회"); 
			System.out.println("\t\t\t2. 대여 확인"); 
			System.out.println("\t\t\t3. 예약 확인"); 
			System.out.println("\t\t\t4. 반납 처리 및 확인"); 
			System.out.println("\t\t\t5. 도서 추가 및 삭제");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String num = sc.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			// 사용자에게 번호 입력받음

			// 도서 검색
			if (num.equals("1")) {
				
			}
			// 대여 확인
			else if (num.equals("2")) {
				
			}
			// 예약 확인
			else if (num.equals("3")) {
				
			}
			// 반납 처리 및 확인
			else if (num.equals("4")) {
				
			}
			// 도서 추가 및 삭제
			else if (num.equals("5")) {
				
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
