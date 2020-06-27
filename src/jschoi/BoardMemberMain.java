package jschoi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class BoardMemberMain {
	
	public static void main(String[] args) {
		exe(1);

	}// main
	static Scanner scan = new Scanner(System.in);

	private static void exe(int member_seq) {//회원 번호를 입력받아서 실행한다.
		
		member_seq = 1;
		boolean flag=true;
		while (flag) {
			Method m = new Method();
			ArrayList<String[]> suggetionRow = m.procGetSuggestions(true);
			ArrayList<String[]> notAnsweredSuggetionRow = m.procGetSuggestions(false);

			String header = String.format("\t\t\t[%s] [%s]   [%s]\t[%s]", "글번호", "작성자", "날짜", "제목");
			System.out.println("\t\t\t건의사항 게시판");
			System.out.println(header);
			int j = 0;
			while (true) {

				if (j >= suggetionRow.size() || j < 0) {// j 유효성검사
					j = 0;
				} else {
					getArrayInfo(suggetionRow, j);
					int num = paging(j, "문의글 작성", "문의글 수정 및 삭제", "문의글 답변 조회", "문의글 검색");// 반복자와 리턴값;//사용자에게 입력받을 메뉴번호,
																						// 아래 if문에서 값을 받는다.

					// 사용자에게 번호 입력받음
					if (num == 1) {// 다음 페이지
						j++;

						continue;
					} else if (num == 2 && j < 10) {// 첫 페이지에서 이전 페이지 입력-> 다음 페이지
						j++;

						continue;
					} else if (num == 2 && j > 10) {// 이전 페이지
						j = j - 19;

						continue;
					} else if (num == 3) {// 문의글 작성
						m.procSetSuggestion(member_seq);
						scan.nextLine();
						break;
					} else if (num == 4) {// 문의글 수정 및 삭제
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.print("\t\t\t1. 문의글 수정\n");
						System.out.print("\t\t\t2. 문의글 삭제\n");
						System.out.println("\t\t\t0. 뒤로가기");
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.print("\t\t\t▷입력: ");

						while (true) {
							String select = scan.nextLine();
							if (select.equals("1")) {
								m.procSetSuggestionUpdate(member_seq);
								break;
							} else if (select.equals("2")) {
								m.procSetDelSuggestion(member_seq);
								break;
							} else if (select.equals("0")) {
								break;
							} else {
								System.out.println("\t\t\t잘못 입력했습니다.");
							}
						}
						scan.nextLine();
						break;
					} else if (num == 5) {// 문의글 답변 조회
						ArrayList<String[]> row = m.procGetSuggestionsInfo();

						String aa = row.get(0)[2].substring(0, 1);
						String cc = row.get(0)[2].substring(2, 3);// 이름 가운데는 *로 익명 보장
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.printf("\t\t\t제목: %s\n", row.get(0)[1]);
						System.out.printf("\t\t\t작성자: %s\n", aa + "*" + cc);
						System.out.printf("\t\t\t작성 날짜: %s\n", row.get(0)[3]);
						System.out.printf("\t\t\t건의 내용: %s\n", row.get(0)[4]);
						System.out.println("\t\t\t--------------------------------------------------");
						System.out.printf("\t\t\t답변: %s\n", row.get(0)[5]);
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						scan.nextLine();
						break;
					} else if (num == 6) {// 문의글 검색
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.print("\t\t\t검색어 입력 : ");
						String keyWord = scan.nextLine();
						ArrayList<String[]> row = m.procGetSuggestions(keyWord);
						int k = 0;
						System.out.printf("\t\t\t총 %d건이 검색되었습니다.\n", row.size());
						while (k < row.size()) {
							getArrayInfo(row, k);
							int input=paging(k);
							if (input == 1) {// 다음 페이지
								k++;
								continue;
							} else if (input == 2 && k < 10) {// 첫 페이지에서 이전 페이지 입력-> 다음 페이지
								k++;
								continue;
							} else if (input == 2 && k > 10) {// 이전 페이지
								k = k - 19;
								continue;
							}else if(input==0) {
								break;
							}
							
							k++;
							if(k==row.size()) {
								System.out.println("\t\t\t마지막 페이지입니다.");
								System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							}
						}
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						
						scan.nextLine();
						break;
					} else if (num == 0) {// 뒤로 가기
						scan.nextLine();
						flag=false;
						break;
					} else if (num > 6) {// 메뉴에 없는 번호 선택시 처음 화면으로
						System.out.println("\t\t\t없는 메뉴입니다.");
						scan.nextLine();
						j = 0;
						continue;
					}
					j++;
				} // while
			} // else문
		}
	}// firstPage

	private static int paging(int j, String menu3,String menu4, String menu5, String menu6) {
		 int num=-1;
		if (j % 10 == 9) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 다음 페이지");
			System.out.println("\t\t\t2. 이전 페이지");
			System.out.printf("\t\t\t3. %s\n",menu3);
			System.out.printf("\t\t\t4. %s\n",menu4);
			System.out.printf("\t\t\t5. %s\n",menu5);
			System.out.printf("\t\t\t6. %s\n",menu6);
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");

			while (true) {// 입력받은 숫자 확인
				String temp2 = scan.nextLine();

				if (isNumber(temp2, 0, 9)) {
					num = Integer.parseInt(temp2);
					break;
				} else {
					System.out.print("\t\t\t▷재입력: ");
				}
			} // while

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

		} // if
		return num;
	}
	private static int paging(int j,  String menu3,String menu4, String menu5) {
		int num=-1;
		if (j % 10 == 9) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 다음 페이지");
			System.out.println("\t\t\t2. 이전 페이지");
			System.out.printf("\t\t\t3. %s\n",menu3);
			System.out.printf("\t\t\t4. %s\n",menu4);
			System.out.printf("\t\t\t5. %s\n",menu5);
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");

			while (true) {// 입력받은 숫자 확인
				String temp2 = scan.nextLine();

				if (isNumber(temp2, 0, 9)) {
					num = Integer.parseInt(temp2);
					break;
				} else {
					System.out.print("\t\t\t▷재입력: ");
				}
			} // while

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

		} // if
		return num;
	}
	private static int paging(int j,  String menu3,String menu4) {
		int num=-1;
		if (j % 10 == 9) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 다음 페이지");
			System.out.println("\t\t\t2. 이전 페이지");
			System.out.printf("\t\t\t3. %s\n",menu3);
			System.out.printf("\t\t\t4. %s\n",menu4);
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");

			while (true) {// 입력받은 숫자 확인
				String temp2 = scan.nextLine();

				if (isNumber(temp2, 0, 9)) {
					num = Integer.parseInt(temp2);
					break;
				} else {
					System.out.print("\t\t\t▷재입력: ");
				}
			} // while

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

		} // if
		return num;
	}
	private static int paging(int j,  String menu3 ) {
		int num=-1;
		if (j % 10 == 9) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 다음 페이지");
			System.out.println("\t\t\t2. 이전 페이지");
			System.out.printf("\t\t\t3. %s\n",menu3);
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");

			while (true) {// 입력받은 숫자 확인
				String temp2 = scan.nextLine();

				if (isNumber(temp2, 0, 9)) {
					num = Integer.parseInt(temp2);
					break;
				} else {
					System.out.print("\t\t\t▷재입력: ");
				}
			} // while

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

		} // if
		return num;
	}
	private static int paging(int j) {
		int num=-1;
		if (j % 10 == 9) {
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 다음 페이지");
			System.out.println("\t\t\t2. 이전 페이지");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");

			while (true) {// 입력받은 숫자 확인
				String temp2 = scan.nextLine();

				if (isNumber(temp2, 0, 9)) {
					num = Integer.parseInt(temp2);
					break;
				} else {
					System.out.print("\t\t\t▷재입력: ");
				}
			} // while

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

		} // if
		return num;
	}
	private static void getArrayInfo(ArrayList<String[]> row, int j) {
		String[] temp = row.get(j);
		String a = temp[2].substring(0, 1);
		String c = temp[2].substring(2, 3);// 이름 가운데는 *로 익명 보장

		String str = String.format("\t\t\t%5s %8s %10s\t%-40s", temp[0], a + "*" + c, temp[3], temp[1]);
		System.out.println(str);
	}

	private static boolean isNumber(String str, int a, int b) {// 문자열에 숫자만 있는지 확인하는 메소드
		return str.matches("^[" + a + "-" + b + "]*$");
	}// isNumber
}// main
