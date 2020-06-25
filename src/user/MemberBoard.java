package user;

import java.util.Scanner;

public class MemberBoard {
	//회원 - 문의게시판
	
	public void boardMenu(MemberUser memberUser) {
		
		MemberUser user = memberUser;
		
		while (true) {
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t            문의게시판");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 문의글 작성"); 
			System.out.println("\t\t\t2. 문의글 수정 및 삭제"); 
			System.out.println("\t\t\t3. 문의글 답변 조회");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String num = sc.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			// 사용자에게 번호 입력받음

			// 문의글 작성
			if (num.equals("1")) {
				
			}
			// 문의글 수정 및 삭제
			else if (num.equals("2")) {
				
			}
			// 문의글 답변 조회
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
		
		
	}//boardMenu
	
	
}
