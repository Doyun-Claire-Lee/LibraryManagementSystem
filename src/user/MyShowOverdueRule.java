package user;

import java.util.Scanner;

/**
 * 
 * @author Sujin Shin
 * 회원 마이페이지의 "연체 규정 조회 클래스"
 *
 */
public class MyShowOverdueRule {
	
	/**
	 * 
	 * 연체 규정을 보여주는 메소드
	 * - 단순 출력 페이지
	 * 
	 */
	public void showRule() {
		
		Scanner sc = new Scanner(System.in);

		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println();
		System.out.println("\t\t\t               [도서관 연체 규정]");
		System.out.println();
		System.out.println("\t\t\t 도서관 대여 단위기간은 1년입니다.");
		System.out.println("\t\t\t 단위기간 내 누적 연체일 수가 30일이 넘는 경우,");
		System.out.println("\t\t\t 도서 대여가 불가합니다. 도서관 이용은 가능합니다.");
		System.out.println();
		System.out.println("\t\t\t 대여 제한은 매년 초기화 됩니다.");
		System.out.println();
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println();
		System.out.println("\t\t\t아무 키나 누르시면 이전메뉴로 돌아갑니다.");
		sc.nextLine();
		
	}

}
