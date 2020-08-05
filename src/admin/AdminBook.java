package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import oracle.jdbc.OracleTypes;

public class AdminBook {
	// 관리자 - 도서 관리

	public void bookMenu(AdminUser adminUser) {

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

			// 도서 조회
			if (num.equals("1")) {
				SelectBookList list = new SelectBookList();
				list.SelectBook(adminUser);

			}
			// 대여 확인
			else if (num.equals("2")) {
				CheckRentalBook rent = new CheckRentalBook();
				rent.RentalCheckMain(adminUser);
			}
			// 예약 확인
			else if (num.equals("3")) {
				CheckReservation reserve = new CheckReservation();
				reserve.SelectReservation(adminUser);

			}
			// 반납 처리 및 확인
			else if (num.equals("4")) {
				ReturnBook ReturnBook = new ReturnBook();
				ReturnBook.RentalCheck(adminUser);

			}
			// 도서 추가 및 삭제
			else if (num.equals("5")) {
				UpdateBook update = new UpdateBook();
				update.UpdateBookList(adminUser);

			}
			// 뒤로 가기
			else if (num.equals("0")) {
				break;
			}
			// 예외
			else {
				System.out.println("\t\t\t번호를 다시 입력해주세요");
			}

		} // while

	}// bookMenu

	/**
	 * 
	 * @author sojin Jang 관리자의 도서관리의 "도서목록 조회 클래스"
	 */
	public class SelectBookList {

		/**
		 * 도서목록을 페이징하기 위한 메소드
		 * 
		 * @param arrayList 도서목록을 담는 객체
		 * @param adminUser 로그인한 회원의 객체
		 */
		public void page(List<String> arrayList, AdminUser adminUser) {
			// 도서목록 페이징

			// 10개씩 분할
			List<String[]> depart = new ArrayList<String[]>();
			int firstindex = 0;
			int lastindex = 10;
			int index = 0;
			int totalcount = arrayList.size();

			while (true) {
				// 10개의 String 이 들어갈 묶음
				String[] ranking = new String[10];

				for (int j = firstindex; j < lastindex; j++) {
					if (j >= arrayList.size()) {
						break;
					}
					ranking[j - (index) * 10] = arrayList.get(j);
				}
				depart.add(ranking);

				firstindex = firstindex + 10;
				lastindex = lastindex + 10;
				index++;

				if (firstindex >= totalcount) {
					break;
				}

			}

			// 페이지 수
			int count = 0;

			int num = 1;

			// 페이지가 한개만 있는 경우
			if (depart.size() == 1) {
				// 첫페이지 보여주기
				for (int i = 0; i < depart.get(0).length; i++) {
					if (depart.get(0)[i] != null) {
						System.out.printf("\t\t\t%d. %s\n", i + 1, depart.get(0)[i]);
					}
				}
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t" + "넘어갈 페이지가 없습니다.");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			} else {

				// 페이지가 여러개인 경우
				while (true) {

					System.out.println();
					for (int i = 0; i < 10; i++) {
						System.out.printf("\t\t\t%d. ", num);
						if (depart.get(count)[i] != null) {
							System.out.printf("\t%s\t", depart.get(count)[i]);
						}
						System.out.println();
						num++;
					}
					// 페이지 반복
					System.out.println();
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.printf("\t\t\t" + "%d페이지 입니다.\n", count + 1);
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t" + "1. 이전 페이지");
					System.out.println("\t\t\t" + "2. 다음 페이지");
					System.out.println("\t\t\t0. 뒤로가기 ");
					System.out.println();

					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 번호 고르기
					Scanner scan = new Scanner(System.in);
					System.out.print("\t\t\t" + "입력:  ");
					String answer = scan.nextLine();
					// scan.skip("\r\n"); //엔터 무시
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 이전페이지
					if (answer.equals("1")) {
						// 첫페이지 일 경우
						if (count == 0) {
							count = depart.size() - 1;
							num = depart.size() * 10 - 9;
						} else {
							count = count - 1;
							num -= 20;
						}
					} else if (answer.equals("2")) {
						// 다음페이지

						// 마지막 페이지일 경우
						if (count == depart.size() - 1) {
							count = 0;
							num = 1;
						} else {
							count = count + 1;

						}
					} else if (answer.equals("3")) {
						break;

					} else if (answer.equals("0")) {
						// 뒤로가기
						return;
					}

					else {
						System.out.println("\t\t\t다시 입력하세요.");

					}
				}

			} // while

		}

		/**
		 * 현재 소장하고 있는 도서 목록을 보여주는 메소드
		 * 
		 * @param adminUser 로그인한 관리자의 객체
		 */
		public void SelectBook(AdminUser adminUser) {
			// 도서관리 1. 도서목록조회

			ArrayList<String> list = new ArrayList<String>();
			Scanner scan = new Scanner(System.in);
			Connection conn = null;
			PreparedStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();

			try {
				// 사용자 입력
				System.out.println("\t\t\t조회할 도서코드를 입력하세요. ");
				System.out.print("\t\t\t▷입력: ");
				String SelectUserInput = scan.nextLine();

				// 문자열의 공백제거
				SelectUserInput = SelectUserInput.replaceAll("\\p{Z}", "");

				// 도서코드에서 0입력 시 뒤로가기
				if (SelectUserInput.equals("0")) {
					return;

				} else {
					// open
					conn = util.open();

					// 명령문
					String sql = "select * from vwSelectBook where 도서번호 like ?";
					// connect
					stat = conn.prepareStatement(sql);
					// 입력값 넣어주기
					stat.setString(1, "%" + SelectUserInput + "%");
					// 불러오기
					rs = stat.executeQuery();

					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t[번호]\t[도서번호]\t[도서코드]\t\t[분류]\t\t[위치]\t[도서명]");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// 출력
					if (rs.next()) {
						while (rs.next()) {
							list.add(rs.getString("seq") + "\t\t" + rs.getString("도서번호") + "\t" + rs.getString("분류")
									+ "\t" + rs.getString("위치") + "\t" + rs.getString("도서명"));

							//
						} // while
						page(list, adminUser);
					} else {
						System.out.println("\t\t\t조회 되지 않습니다.");
					}
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// close
					stat.close();
					conn.close();
				} // else

			} catch (Exception e) {
				// TODO: handle exception
			}

		}// SelectBook()
	}// SelectBookList

	/**
	 * 
	 * @author sojin Jang 관리자의 도서관리의 "대여한 도서의 목록 확인과 신규 대여 도서 추가 가능 클래스"
	 *
	 */
	public class CheckRentalBook {
//도서관리-대여확인

		/**
		 * 도서 대여 목록 출력에 사용하는 페이징을 구현하기위한 메소드
		 * 
		 * @param arrayList      대여목록을 저장한 객체
		 * @param MemberNumInput 관리자로부터 입력받은 회원 번호 객체
		 * @param BookNumInput   관리자로부터 입력받은 도서 번호 객체
		 * @param adminUser      로그인한 관리자 객체
		 */
		public void page(List<String> arrayList, String MemberNumInput, String BookNumInput, AdminUser adminUser) {
			// 대여목록 출력 페이징
			// 10개씩 분할-> 페이지 메소드

			List<String[]> depart = new ArrayList<String[]>();
			int firstindex = 0;
			int lastindex = 10;
			int index = 0;
			int totalcount = arrayList.size();

			while (true) {
				// 10개의 String 이 들어갈 묶음
				String[] ranking = new String[10];

				for (int j = firstindex; j < lastindex; j++) {
					if (j >= arrayList.size()) {
						break;
					}
					ranking[j - (index) * 10] = arrayList.get(j);
				}
				depart.add(ranking);

				firstindex = firstindex + 10;
				lastindex = lastindex + 10;
				index++;

				if (firstindex >= totalcount) {
					break;
				}

			}

			// 페이지 수
			int count = 0;

			int num = 1;

			// 페이지가 한개만 있는 경우
			if (depart.size() == 1) {
				// 첫페이지 보여주기
				for (int i = 0; i < depart.get(0).length; i++) {
					if (depart.get(0)[i] != null) {
						System.out.printf("\t\t\t%d. %s\n", i + 1, depart.get(0)[i]);
					}
				}
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t" + "넘어갈 페이지가 없습니다.");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				Scanner scan = new Scanner(System.in);
				System.out.println("\t\t\t0. 뒤로가기");
				String backInput = scan.nextLine();

				if (backInput.equals("0")) {
					RentalCheckMain(adminUser);
				}

			} else {

				// 페이지가 여러개인 경우
				while (true) {

					System.out.println();
					for (int i = 0; i < 10; i++) {
						System.out.printf("\t\t\t%d. ", num);
						if (depart.get(count)[i] != null) {
							System.out.printf("\t%s\t", depart.get(count)[i]);
						}
						System.out.println();
						num++;
					}
					// 페이지 반복
					System.out.println();
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.printf("\t\t\t" + "%d페이지 입니다.\n", count + 1);
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t" + "1. 이전 페이지");
					System.out.println("\t\t\t" + "2. 다음 페이지");

					System.out.println("\t\t\t" + "0. 뒤로 가기");
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 번호 고르기
					Scanner scan = new Scanner(System.in);
					System.out.print("\t\t\t" + "▷입력:  ");
					String answer = scan.nextLine();
					// scan.skip("\r\n"); //엔터 무시
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 이전페이지
					if (answer.equals("1")) {
						// 첫페이지 일 경우
						if (count == 0) {
							count = depart.size() - 1;
							num = depart.size() * 10 - 9;
						} else {
							count = count - 1;
							num -= 20;
						}
					} else if (answer.equals("2")) {
						// 다음페이지

						// 마지막 페이지일 경우
						if (count == depart.size() - 1) {
							count = 0;
							num = 1;
						} else {
							count = count + 1;

						}

					} else if (answer.equals("0")) {
						// 뒤로가기
						RentalCheckMain(adminUser);

					}

					else {
						// System.out.println("\t\t\t"+"페이지 프로그램을 종료합니다.");

//					break;
					}

				} // while

			}
		}

		/**
		 * 대여 조회 메인 메소드
		 * 
		 * @param adminUser 로그인한 관리자 객체
		 */
		public void RentalCheckMain(AdminUser adminUser) {
			// 대여조회 메인

			Scanner scan = new Scanner(System.in);

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t대여확인 페이지");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 대여 조회");
			System.out.println("\t\t\t2. 대여 추가");
			System.out.println("\t\t\t0. 뒤로 가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String RentalInput = scan.nextLine();

			if (RentalInput.equals("1")) {
				// 대여조회
				RentalListCheck(adminUser);

			} else if (RentalInput.equals("2")) {
				// 대여 추가
				SelectBook(adminUser);

			} else if (RentalInput.equals("0")) {
				// 뒤로가기
				return;

			} else {
				System.out.println("\t\t\t다시 입력해주세요.");

			} // else
		}// RentalCheckMain()

		public void RentalListCheck(AdminUser adminUser) {
			// 대여중인 사람 조회

			Scanner scan = new Scanner(System.in);
			Connection conn = null;
			PreparedStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();

			try {
				// 사용자 입력
				System.out.println("\t\t\t조회할 대여날짜를 입력하세요. ");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t▷년도(year)(YYYY): ");
				String SelectYearInput = scan.nextLine();
				System.out.print("\t\t\t▷월(month)(MM): ");
				String SelectMonthInput = scan.nextLine();
				System.out.print("\t\t\t▷일(day)(DD): ");
				String SelectDayInput = scan.nextLine();

				if (SelectYearInput.equals("0") || SelectMonthInput.equals("0") || SelectDayInput.equals("0")) {
					// 뒤로가기
					return;

				} else {
					// 유효성검사-> 숫자만 입력가능하도록
					if (Pattern.matches("^[0-9]*$", SelectYearInput) || Pattern.matches("^[0-9]*$", SelectMonthInput)
							|| Pattern.matches("^[0-9]*$", SelectDayInput)) {

						// open
						conn = util.open();

						// 명령문
						String sql = "select * from vwCheckRentBook where 대여시작일 = ?";
						// connect
						stat = conn.prepareStatement(sql);

						// 입력값 넣어주기
						stat.setString(1, SelectYearInput + "-" + SelectMonthInput + "-" + SelectDayInput);
						// 불러오기
						rs = stat.executeQuery();
					} else {
						System.out.println("\t\t\t숫자만 입력가능합니다.");
					}

					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t[회원번호]\t[회원명]\t[도서코드]\t[대여시작일]\t[대여종료일]\t[도서명]");
					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// 출력
					if (rs.next()) {
						while (rs.next()) {

							System.out.printf("\t%s\t\t%s\t%s\t%s\t%s\t%s\n", rs.getString("회원번호"), rs.getString("성함"),
									rs.getString("도서코드"), rs.getString("대여시작일"), rs.getString("대여종료일"),
									rs.getString("도서명"));

						} // while
					} else {
						System.out.println("\t\t\t대여중인 도서가 없습니다.");
					}
					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// close
					stat.close();
					conn.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			// 뒤로가기
			System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String backtUserInput = scan.nextLine();

			if (backtUserInput.equals("0")) {
				// 0. 뒤로가기
				RentalCheckMain(adminUser);

			} else {
				System.out.println("\t\t\t다시 입력해주세요.");
			}

		}// SelectBook()

		/**
		 * 현재 소장한 도서의 목록을 페이징을 나타내기 위한 저장 메소드
		 * 
		 * @param arrayList 현재 소장한 책의 목록을 저장할 객체
		 * @param adminUser 로그인한 관리자 객체
		 */
		public void page1(List<String> arrayList, AdminUser adminUser) {
			// 대여추가

			// 10개씩 분할
			List<String[]> depart = new ArrayList<String[]>();
			int firstindex = 0;
			int lastindex = 10;
			int index = 0;
			int totalcount = arrayList.size();

			while (true) {
				// 10개의 String 이 들어갈 묶음
				String[] ranking = new String[10];

				for (int j = firstindex; j < lastindex; j++) {
					if (j >= arrayList.size()) {
						break;
					}
					ranking[j - (index) * 10] = arrayList.get(j);
				}
				depart.add(ranking);

				firstindex = firstindex + 10;
				lastindex = lastindex + 10;
				index++;

				if (firstindex >= totalcount) {
					break;
				}

			}

			// 페이지 수
			int count = 0;

			int num = 1;

			// 페이지가 한개만 있는 경우
			if (depart.size() == 1) {
				// 첫페이지 보여주기
				for (int i = 0; i < depart.get(0).length; i++) {
					if (depart.get(0)[i] != null) {
						System.out.printf("\t\t\t%d. %s\n", i + 1, depart.get(0)[i]);
					}
				}
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t" + "넘어갈 페이지가 없습니다.");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			} else {

				// 페이지가 여러개인 경우
				while (true) {

					System.out.println();
					for (int i = 0; i < 10; i++) {
						System.out.printf("\t\t\t%d. ", num);
						if (depart.get(count)[i] != null) {
							System.out.printf("\t%s\t", depart.get(count)[i]);
						}
						System.out.println();
						num++;
					}
					// 페이지 반복
					System.out.println();
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.printf("\t\t\t" + "%d페이지 입니다.\n", count + 1);
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t" + "1. 이전 페이지");
					System.out.println("\t\t\t" + "2. 다음 페이지");
					System.out.println("\t\t\t3. 입력 하기");
					System.out.println("\t\t\t0. 뒤로가기 ");
					System.out.println();

					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 번호 고르기
					Scanner scan = new Scanner(System.in);
					System.out.print("\t\t\t" + "입력:  ");
					String answer = scan.nextLine();
					// scan.skip("\r\n"); //엔터 무시
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 이전페이지
					if (answer.equals("1")) {
						// 첫페이지 일 경우
						if (count == 0) {
							count = depart.size() - 1;
							num = depart.size() * 10 - 9;
						} else {
							count = count - 1;
							num -= 20;
						}
					} else if (answer.equals("2")) {
						// 다음페이지

						// 마지막 페이지일 경우
						if (count == depart.size() - 1) {
							count = 0;
							num = 1;
						} else {
							count = count + 1;

						}
					} else if (answer.equals("3")) {
						break;

					} else if (answer.equals("0")) {
						// 뒤로가기
						return;
					} else {
						System.out.println("\t\t\t다시 입력하세요.");

					}
				}

			} // while

		}

		/**
		 * 현재 소장한 도서 목록을 조회할 수 있는 메소드 - 조회를 원하는 도서코드를 입력하면 관련된 도서의 목록 확인 가능
		 * 
		 * @param adminUser 로그인한 관리자 객체
		 */
		public void SelectBook(AdminUser adminUser) {

			ArrayList<String> list = new ArrayList<String>();
			Scanner scan = new Scanner(System.in);
			Connection conn = null;
			PreparedStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();

			try {
				// 사용자 입력
				System.out.println("\t\t\t검색할 도서코드를 입력하세요. ");
				System.out.print("\t\t\t▷입력: ");
				String SelectUserInput = scan.nextLine();

				// 문자열의 공백제거
				SelectUserInput = SelectUserInput.replaceAll("\\p{Z}", "");

				// 도서코드에서 0입력 시 뒤로가기
				if (SelectUserInput.equals("0")) {
					// 뒤로가기
					AdminBook main = new AdminBook();
					main.bookMenu(adminUser);

				} else {

					// open
					conn = util.open();

					// 명령문
					String sql = "select * from vwSelectBook where 도서번호 like ?";
					// connect
					stat = conn.prepareStatement(sql);
					// 입력값 넣어주기
					stat.setString(1, "%" + SelectUserInput + "%");
					// 불러오기
					rs = stat.executeQuery();

					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t[번호]\t[도서번호]\t[도서코드]\t\t[분류]\t\t[위치]\t[도서명]");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// 출력
					if (rs.next()) {
						while (rs.next()) {
							list.add(rs.getString("seq") + "\t\t" + rs.getString("도서번호") + "\t" + rs.getString("분류")
									+ "\t" + rs.getString("위치") + "\t" + rs.getString("도서명"));

						} // while

						// paging 출력
						page1(list, adminUser);

						// 데이터가 없을 시
					} else {
						System.out.println("\t\t\t 존재하지 않는 코드입니다.");
					}
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// close
					stat.close();
					conn.close();

				} // 도서코드입력 뒤로가기의 else

			} catch (Exception e) {
				// TODO: handle exception
			}

			// 다음페이지로 이동->대여추가
			Rentaladd(adminUser, list);

		}// SelectBook()

		/**
		 * 신규 대여 도서의 목록을 추가하기 위한 메소드 - 회원번호와 도서의 코드를 입력하여 저장 가능
		 * 
		 * @param adminUser 로그인한 관리자의 객체
		 * @param arrayList 페이징을 나타내기 위한 저장 객체
		 */
		public void Rentaladd(AdminUser adminUser, List<String> arrayList) {
			// 대여추가

			Connection conn = null;
			CallableStatement stat = null;
			DBUtil util = new DBUtil();

			Scanner scan = new Scanner(System.in);

			// insert new info to tblRent table
			try {

				// open
				conn = util.open();

				// User Input
				System.out.println("\t\t\t대여 정보를 입력해주세요.");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				// 도서번호 입력 유효성 검사
				boolean term = true;
				String BookNumInput = null;
				while (term) {
					while (true) {
						// User Input
						System.out.print("\t\t\t▷번호 입력: ");
						BookNumInput = scan.nextLine();
						try {
							// 0입력 시
							if (BookNumInput.equals("0")) {
								// 뒤로가기
								term = false;
								break;
							} else if (Integer.parseInt(BookNumInput) <= arrayList.size()) {
								// 길이 맞추기
								break;
							} else {
								System.out.println("\t\t\t범위 내의 숫자만 입력 가능합니다.\n");
							}
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("\t\t\t 범위내 숫자를 입력하세요.");
							System.out.println();
						} // catch
					} // while

					if (term == false) {
						break;
					} // if

					// User Input
					System.out.print("\t\t\t▷회원번호 입력: ");
					String MemberNumInput = scan.nextLine();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					String bookCode = "";

					// 0 입력 시
					if (MemberNumInput.equals("0")) {
						// 뒤로가기
						RentalCheckMain(adminUser);

						// 숫자만 입력가능한 유효성 검사
					} else if (Pattern.matches("^[0-9]*$", MemberNumInput)) {

						// 도서코드 가져오기
						int booknum = Integer.parseInt(BookNumInput);
						bookCode = arrayList.get(booknum - 1).split("\t")[2];
						// input new info

						String Sql = " { call procRentalInsert (?,?) }";

						stat = conn.prepareCall(Sql);

						stat.setString(1, MemberNumInput);
						stat.setString(2, bookCode);

						stat.executeUpdate();

					} else {
						System.out.println("\t\t\t다시 입력해주세요.");
					}
					System.out.println("\t\t\t대여 등록 완료 되었습니다.");
					System.out.println();

					// close
					stat.close();
					conn.close();

					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t▷계속 입력하려면 y아니면 n: ");
					String YNInput = scan.nextLine();

					if (YNInput.equals("y")) {
						// return to RentalCheck
						Rentaladd(adminUser, arrayList);

					} else if (YNInput.equals("n")) {
						// next page
						SelectRentalBook(MemberNumInput, bookCode, adminUser);

					} else if (YNInput.equals("0")) {
						// 뒤로가기
						RentalCheckMain(adminUser);

					} else {
						// exception
						System.out.println("\t\t\t다시 입력해주세요.");
					}

				} // while (term)
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("\t\t\terror in RentalBook");
			}
		}

		/**
		 * 당일 날짜의 대여 목록을 모두 조회 할 수 있는 메소드 - 당일 날짜의 회원번호로 대여한 모든 도서의 정보 확인 가능
		 * 
		 * @param MemberNumInput 회원번호 객체
		 * @param bookCode       도서코드 객체
		 * @param adminUser      로그인한 관리자의 객체
		 */
		public void SelectRentalBook(String MemberNumInput, String bookCode, AdminUser adminUser) {
			// 대여 목록 출력 페이지

			ArrayList<String> list = new ArrayList<String>();
			Scanner scan = new Scanner(System.in);
			Connection conn = null;
			Statement stat = null;
			DBUtil util = new DBUtil();
			ResultSet rs = null;

			try {

				// open
				conn = util.open();
				// connet
				stat = conn.createStatement();

				// 오늘 날짜로 대여한 목록 출력
				Calendar now = Calendar.getInstance();

				String sql = String.format("select * from vwCheckRentBook where 회원번호 = %s and 대여시작일 = '%tF' ",
						MemberNumInput, now);

				// 호출
				rs = stat.executeQuery(sql);

				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t[번호]\t[회원번호]\t[회원명]\t[도서코드]\t\t[대여시작일]\t[대여종료일]\t[도서명]");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				if (rs.next()) {

					while (rs.next()) {
						list.add("\t" + rs.getString("회원번호") + "\t" + rs.getString("성함") + "\t\t" + rs.getString("도서코드")
								+ "\t" + rs.getString("대여시작일") + "\t\t" + rs.getString("대여종료일") + "\t"
								+ rs.getString("도서명"));

					} // while
				} else {
					System.out.println("\t\t\t대여 중인 도서가 없습니다.");
				}

				// 페이지
				CheckRentalBook add = new CheckRentalBook();
				add.page(list, MemberNumInput, bookCode, adminUser);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("\t\t\terror in SelectRentalBook");
			} // catch

		}// SelectRentalBook()

	}// CheckRentalBook

	/**
	 * 
	 * @author sojin Jang 관리자 "도서관리의 예약 확인 클래스"
	 */
	public class CheckReservation {
//3. 예약확인

		/**
		 * 예약 도서 목록을 날짜별로 조회할 수 있는 클래스 예약한 회원정보와 도서정보 확인 가능
		 * 
		 * @param adminUser 로그인한 관리자의 객체
		 */
		public void SelectReservation(AdminUser adminUser) {

			Scanner scan = new Scanner(System.in);

			Connection conn = null;
			CallableStatement stat = null;
			DBUtil util = new DBUtil();
			ResultSet rs = null;

			try {
				// procedure 불러오기
				String sql = "{ call procCheckReserve(?,?) }";

				// 사용자 입력
				System.out.println("\t\t\t조회할 예약날짜를 입력하세요. ");

				System.out.print("\t\t\t▷년도(year)(yyyy): ");
				String SelectYearInput = scan.nextLine();

				if (SelectYearInput.equals("0")) {
					// 뒤로가기
					AdminBook main = new AdminBook();
					main.bookMenu(adminUser);

				} else {

					System.out.print("\t\t\t▷ 월(month)(mm): ");
					String SelectMonthInput = scan.nextLine();

					if (SelectMonthInput.equals("0")) {
						// 뒤로가기
						return;

					} else {

						System.out.print("\t\t\t▷일(day)(dd): ");
						String SelectDayInput = scan.nextLine();

						if (SelectDayInput.equals("0")) {
							// 뒤로가기
							return;
						} else {

							if (Pattern.matches("^[0-9]*$", SelectYearInput)
									|| Pattern.matches("^[0-9]*$", SelectMonthInput)
									|| Pattern.matches("^[0-9]*$", SelectDayInput)

							) {

								System.out.println(
										"〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
								System.out.println("[번호]\t\t[회원번호]\t\t[회원명]\t\t[예약날짜]\t\t[저자명]\t\t[도서명]");
								System.out.println(
										"〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

								// oepn sql
								conn = util.open();

								// sql 호출
								stat = conn.prepareCall(sql);

								// 사용자 입력값 명령값으로 넣기
								stat.setString(1, SelectYearInput + "/" + SelectMonthInput + "/" + SelectDayInput);

								// 그외 정보 불러오기
								stat.registerOutParameter(2, OracleTypes.CURSOR);
								stat.executeQuery();
								rs = (ResultSet) stat.getObject(2);
								ResultSetMetaData rsmd = rs.getMetaData();
							} else {
								System.out.println("다시 입력하세요.");
							}

							// 출력
							while (rs.next()) {

								System.out.printf("%s\t\t%s\t\t%s\t\t%s\t%s\t%s\n", rs.getString("번호"),
										rs.getString("회원번호"), rs.getString("회원명"), rs.getString("예약날짜"),
										rs.getString("저자명"), rs.getString("도서명"));
							} // while

							System.out.println(
									"〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

							// close
							rs.close();
							stat.close();
							conn.close();

						} // SelectYearInput else
					} // SelectMonthInput else
				} // SelectDayInput else

			} catch (Exception e) {
				System.out.println("\t\t\t에러발생,,");

			} // catch

			// 뒤로가기
			System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String backtUserInput = scan.nextLine();

			if (backtUserInput.equals("0")) {
				// 0. 뒤로가기

				return;

			} else {
				System.out.println("\t\t\t다시 입력해주세요.");

			} // else

		}// SelectReservation()

	}// CheckReservation

	/**
	 * 
	 * @author sojin Jang 관리자 도서관리의 "반납한 도서의 목록 조회와 새로 추가할수 있는 클래스"
	 *
	 */
	public class ReturnBook {

		public void RentalCheck(AdminUser adminUser) {
			// 도서관리 - 반납확인
			Scanner scan = new Scanner(System.in);

			System.out.println("\t\t\t1. 도서 반납 처리하기");
			System.out.println("\t\t\t2. 도서 반납 내역 조회");
			System.out.println("\t\t\t0. 뒤로가기");
			// 사용자 입력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\n\t\t\t▷입력: ");
			String RentablUserInput = scan.nextLine();

			if (RentablUserInput.equals("1")) {
				// 1. 도서 반납 처리하기
				UpdateRental(adminUser);

			} else if (RentablUserInput.equals("2")) {
				// 2. 도서 반납 내역 조회
				CheckPeriodBook(adminUser);

			} else if (RentablUserInput.equals("0")) {
				// 0. 뒤로가기
				return;

			} else {
				System.out.println("\t\t\t다시 입력해주세요.");
			}

		}// RentalCheck()

		/**
		 * 도서 반납 목록을 페이징하여 구현할 메소드
		 * 
		 * @param arrayList 도서의 목록을 저장하기위한 객체
		 * @param adminUser 로그인한 관리자의 객체
		 */
		public void page(List<String> arrayList, AdminUser adminUser) {

			// 10개씩 분할
			List<String[]> depart = new ArrayList<String[]>();
			int firstindex = 0;
			int lastindex = 10;
			int index = 0;
			int totalcount = arrayList.size();

			while (true) {
				// 10개의 String 이 들어갈 묶음
				String[] ranking = new String[10];

				for (int j = firstindex; j < lastindex; j++) {
					if (j >= arrayList.size()) {
						break;
					}
					ranking[j - (index) * 10] = arrayList.get(j);
				}
				depart.add(ranking);

				firstindex = firstindex + 10;
				lastindex = lastindex + 10;
				index++;

				if (firstindex >= totalcount) {
					break;
				}

			}

			// 페이지 수
			int count = 0;

			int num = 1;

			// 페이지가 한개만 있는 경우
			if (depart.size() == 1) {
				// 첫페이지 보여주기
				for (int i = 0; i < depart.get(0).length; i++) {
					if (depart.get(0)[i] != null) {
						System.out.printf("\t\t\t%d. %s\n", i + 1, depart.get(0)[i]);
					}
				}
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t" + "넘어갈 페이지가 없습니다.");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			} else {

				// 페이지가 여러개인 경우
				while (true) {

					System.out.println();
					for (int i = 0; i < 10; i++) {
						System.out.printf("\t\t\t%d. ", num);
						if (depart.get(count)[i] != null) {
							System.out.printf("\t%s\t", depart.get(count)[i]);
						}
						System.out.println();
						num++;
					}
					// 페이지 반복
					System.out.println();
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.printf("\t\t\t" + "%d페이지 입니다.\n", count + 1);
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t" + "1. 이전 페이지");
					System.out.println("\t\t\t" + "2. 다음 페이지");
					;
					System.out.println("\t\t\t0. 뒤로 가기");
					System.out.println();

					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 번호 고르기
					Scanner scan = new Scanner(System.in);
					System.out.print("\t\t\t" + "입력:  ");
					String answer = scan.nextLine();
					// scan.skip("\r\n"); //엔터 무시
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 이전페이지
					if (answer.equals("1")) {
						// 첫페이지 일 경우
						if (count == 0) {
							count = depart.size() - 1;
							num = depart.size() * 10 - 9;
						} else {
							count = count - 1;
							num -= 20;
						}
					} else if (answer.equals("2")) {
						// 다음페이지

						// 마지막 페이지일 경우
						if (count == depart.size() - 1) {
							count = 0;
							num = 1;
						} else {
							count = count + 1;

						}

					} else if (answer.equals("0")) {
						// 뒤로가기
						UpdateBook main = new UpdateBook();
						main.UpdateBookList(adminUser);
					}

					else {
						System.out.println("\t\t\t다시 입력하세요.");

					}
				}

			} // while

		}

		/**
		 * 도서 반납 처리하기 전 도서의 목록을 출력할 수 있는 메소드 - 도서코드로 확인 가능
		 * 
		 * @param adminUser 로그인한 관리자의 객체
		 */
		public void UpdateRental(AdminUser adminUser) {
			// 1. 도서 반납 처리하기

			Connection conn = null;
			CallableStatement stat = null;
			DBUtil util = new DBUtil();
			ResultSet rs = null;
			Scanner scan = new Scanner(System.in);

			try {

				// procedure 호출
				String sql = "{call procretrunCheck(?,?)}";

				// 사용자 입력
				System.out.print("\t\t\t▷도서코드 입력: ");
				String InputBookCode = scan.nextLine();

				// 0입력시 뒤로가기
				if (InputBookCode.equals("0")) {
					return;

				} else {

					// 문자열의 공백제거
					InputBookCode = InputBookCode.replaceAll("\\p{Z}", "");


					// oepn sql
					conn = util.open();
					// sql 호출
					stat = conn.prepareCall(sql);

					// 사용자 입력값 명령값으로 넣기
					stat.setString(1, '%'+ InputBookCode+'%');

					// 그외 정보 불러오기
					stat.registerOutParameter(2, OracleTypes.CURSOR);
					stat.executeQuery();
					rs = (ResultSet) stat.getObject(2);
					ResultSetMetaData rsmd = rs.getMetaData();
					
					System.out.println(
							"〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("[회원번호]\t\t[회원명]\t\t[대여일]\t\t[반납일]\t\t[반납여부]\t\t[연체횟수]\t\t[도서코드]\t\t\t[도서명]");
					System.out.println(
							"〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// 출력
					if (rs.next()) {
						while (rs.next()) {

							System.out.printf("%s\t\t%s\t\t%s\t%s\t\t%s\t\t%s\t\t%s\t\t%s\n", rs.getString("회원번호"),
									rs.getString("성함"), rs.getString("대여날짜"), rs.getString("반납날짜"),
									rs.getString("반납여부"), rs.getString("연체여부"), rs.getString("도서코드"),
									rs.getString("도서명"));

						} // while
					} else {
						System.out.println("\t\t\t내역이 없습니다.");
					}
					System.out.println(
							"〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t1. 반납처리하기");
					System.out.println("\t\t\t0. 뒤로가기");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t▷입력: ");
					String UserInput = scan.nextLine();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					if (UserInput.equals("1")) {
						// 1.반납처리하기
						returnstate(InputBookCode, adminUser);

					} else if (UserInput.equals("0")) {
						// 0.뒤로가기
						return;

					} else { // exception
						System.out.println("\t\t\t다시 입력해주세요.");
					}

					// close
					rs.close();
					stat.close();
					conn.close();

				} // InputBookCode else

			} catch (Exception e) {
				System.out.println("\t\t\t조회되지 않습니다. 다시 입력해주세요.");
				UpdateRental(adminUser);
			} // catch

		} // UpdateRental()

		/**
		 * 반납 된 도서의 목록 입력가능 - 회원번호와 도서코드로 저장 가능
		 * 
		 * @param InputBookCode 관리자가 입력한 도서코드 객체
		 * @param adminUser     로그인한 관리자 객체
		 */
		public void returnstate(String InputBookCode, AdminUser adminUser) {
			// 반납처리하기

			Connection conn = null;
			Statement stat = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			DBUtil util = new DBUtil();
			Scanner scan = new Scanner(System.in);

			System.out.println("\t\t\t반납 처리로 변경하려는 회원번호을 입력해주세요.");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			String MemberNumInput = scan.nextLine();

			// 0을 입력시
			if (MemberNumInput.equals("0")) {
				// 뒤로가기
				return;

				// 아니면 계속 진행
			} else {

				try {

					// open sql
					conn = util.open();

					// connet with sql
					stat = conn.createStatement();

					// sql update 명령
					String sql = String.format(
							"update tblRent set return_date = sysdate where member_seq= %s and book_code= '%s'",
							MemberNumInput, InputBookCode);

					// sql select 명령
					String selectsql = String.format("select * from vwCheckReturnBook where 도서코드 = '%s' and 회원번호 = %s",
							InputBookCode, MemberNumInput);

					// 명령값가져오기
					rs = stat.executeQuery(sql);

					// 실행
					System.out.printf("\t\t\t회원번호: %s\n\t\t\t도서코드:%s\n\n\t\t\t반납 처리 완료되었습니다.\n\n", MemberNumInput,
							InputBookCode);

					// 뒤로가기
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t0. 뒤로가기");
					System.out.print("\t\t\t▷입력: ");
					String BackUserInput = scan.nextLine();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					if (BackUserInput.equals("0")) {
						// 0.뒤로가기
						return;

					} else { // exception
						System.out.println("\t\t\t번호를 다시 입력해주세요.");
					}

				} catch (Exception e) {
					System.out.println("\t\t\t조회 되지 않습니다. 다시 입력해주세요.");
					returnstate(InputBookCode, adminUser);
				}
			}
		}

		/**
		 * 반납 처리된 도서 목록 확인 가능한 메소드 - 날짜별로 조회 가능
		 * 
		 * @param adminUser 로그인한 관리자의 객체
		 */
		public void CheckPeriodBook(AdminUser adminUser) {
			// 도서관리-반납확인-내역조회

			ArrayList<String> list = new ArrayList<String>();
			Connection conn = null;
			Statement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();
			Scanner scan = new Scanner(System.in);

			try {

				// 사용자 입력
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t조회할 반납일을 기간을 입력해주세요");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\n\t\t\t[조회 시작일]");

				System.out.print("\t\t\t▷년도(year)(YYYY): ");
				String SelectYearInput = scan.nextLine();
				// 0입력시 뒤로가기
				if (SelectYearInput.equals("0")) {
					return;
				} else {

					System.out.print("\t\t\t▷월(month)(MM): ");
					String SelectMonthInput = scan.nextLine();

					// 0입력 시 뒤로가기
					if (SelectMonthInput.equals("0")) {
						return;

					} else {

						System.out.print("\t\t\t▷일(day)(DD): ");
						String SelectDayInput = scan.nextLine();

						// 0입력 시 뒤로가기
						if (SelectDayInput.equals("0")) {
							return;

						} else {

							System.out.println("\n\t\t\t[조회 종료일]");

							System.out.print("\t\t\t▷년도(year)(YYYY): ");
							String LastYearInput = scan.nextLine();

							// 0입력 시 뒤로가기
							if (LastYearInput.equals("0")) {
								return;

							} else {

								System.out.print("\t\t\t▷월(month)(MM): ");
								String LastMonthInput = scan.nextLine();

								// 0입력 시 뒤로가기
								if (LastMonthInput.equals("0")) {
									return;

								} else {

									System.out.print("\t\t\t▷일(day)(DD): ");
									String LastDayInput = scan.nextLine();

									// 0입력 시 뒤로가기
									if (LastDayInput.equals("0")) {
										return;

									} else {

										if (Pattern.matches("^[0-9]*$", SelectYearInput)
												|| Pattern.matches("^[0-9]*$", SelectMonthInput)
												|| Pattern.matches("^[0-9]*$", SelectDayInput)
												|| Pattern.matches("^[0-9]*$", LastYearInput)
												|| Pattern.matches("^[0-9]*$", LastMonthInput)
												|| Pattern.matches("^[0-9]*$", LastDayInput)) {

											// open
											conn = util.open();

											// connet
											stat = conn.createStatement();

											// sql select 명령
											String sql = String.format(
													"select * from vwCheckReturnBook where 반납날짜 between '%s' and '%s'",
													SelectYearInput + "/" + SelectMonthInput + "/" + SelectDayInput,
													LastYearInput + "/" + LastMonthInput + "/" + LastDayInput);

											// 값 가져오기
											rs = stat.executeQuery(sql);
										} else {
											System.out.println("\t\t\t다시 입력해주세요.");
										}

										System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
										System.out.printf("\n\t\t\t[%s년%s월%s일]부터 [%s년%s월%s일]까지의 반납일 리스트 입니다.\n",
												SelectYearInput, SelectMonthInput, SelectDayInput, LastYearInput,
												LastMonthInput, LastDayInput);
										System.out.println(
												"\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
										System.out.println(
												"\t\t\t[번호]\t[도서코드]\t\t[고객명]\t[회원번호]\t[대여일]\t[반납일]\t[반납여부]\t[연장횟수]\t\t[도서명]");
										System.out.println(
												"\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

										// 명령값 출력
										if (rs.next()) {

											while (rs.next()) {
												list.add(rs.getString("도서코드") + "\t" + rs.getString("성함") + "\t\t"
														+ rs.getString("회원번호") + "\t\t" + rs.getString("대여날짜") + "\t"
														+ rs.getString("반납날짜") + "\t" + rs.getString("반납여부") + "\t\t"
														+ rs.getString("연장횟수") + "\t" + rs.getString("도서명"));

											} // while
											page(list, adminUser);
											// 명령값이 없을 시
										} else {
											System.out.println("\t\t\t조회되지 않습니다.");
										}
										System.out.println(
												"\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

									} // SelectYearInput else
								} // SelectMonthInput else
							} // SelectDayInput else
						} // LastYearInput else
					} // LastMonthInput else
				} // LastDayInput else
			} catch (Exception e) {
				System.out.println("\t\t\t조회 되지 않습니다. 다시 입력해주세요.");
				CheckPeriodBook(adminUser);
			}

		}

	}
// ReturnBook

	/**
	 * 
	 * @author Sojin Jang
	 * 관리자 도서관리의 "도서 추가, 수정 및 삭제 클래스"
	 *
	 */

	public class UpdateBook {
		// 도서 추가 및 삭제
		
		
		/**
		 * 도서관리의 도서 추가, 수정 및 삭제의 메인 클래스
		 * @param memberUser 로그인한 관리자의 객체
		 */
		public void UpdateBookList(AdminUser adminUser) {

			Scanner scan = new Scanner(System.in);

			System.out.println("\t\t\t1. 도서 추가");
			System.out.println("\t\t\t2. 도서 수정");
			System.out.println("\t\t\t3. 도서 삭제");
			System.out.println("\t\t\t0. 뒤로 가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t입력: ");
			String AddBookUserInput = scan.nextLine();

			if (AddBookUserInput.equals("1")) {
				// 1. 도서 추가
				AddBook2(adminUser);

			} else if (AddBookUserInput.equals("2")) {
				// 2. 도서 수정
				UpdateBookName(adminUser);

			} else if (AddBookUserInput.equals("3")) {
				// 3. 도서 삭제
				DeleteBookInfo(adminUser);

			} else if (AddBookUserInput.equals("0")) {
				// 0. 뒤로 가기 -> AdminBook
				return;
				
			} else {
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}

		}// AddBookList()

		
		/**
		 * 십진 대분류를 페이징하여 보여줄 메소드
		 * @param arrayList 로그인한 관리자의 객체
		 * @param adminUser 십진 대분류 리스트를 담을 객체
		 */
		public void page(List<String> arrayList, AdminUser adminUser) {
			// 십진 대분류 리스트

			// 10개씩 분할
			List<String[]> depart = new ArrayList<String[]>();
			int firstindex = 0;
			int lastindex = 10;
			int index = 0;
			int totalcount = arrayList.size();

			while (true) {
				// 10개의 String 이 들어갈 묶음
				String[] ranking = new String[10];

				for (int j = firstindex; j < lastindex; j++) {
					if (j >= arrayList.size()) {
						break;
					}
					ranking[j - (index) * 10] = arrayList.get(j);
				}
				depart.add(ranking);

				firstindex = firstindex + 10;
				lastindex = lastindex + 10;
				index++;

				if (firstindex >= totalcount) {
					break;
				}

			}

			// 페이지 수
			int count = 0;

			int num = 1;

			// 페이지가 한개만 있는 경우
			if (depart.size() == 1) {
				// 첫페이지 보여주기
				for (int i = 0; i < depart.get(0).length; i++) {
					if (depart.get(0)[i] != null) {
						System.out.printf("\t\t\t%d. %s\n", i + 1, depart.get(0)[i]);
					}
				}
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t" + "넘어갈 페이지가 없습니다.");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			} else {

				// 페이지가 여러개인 경우
				while (true) {

					System.out.println();
					for (int i = 0; i < 10; i++) {
						System.out.printf("\t\t\t%d. ", num);
						if (depart.get(count)[i] != null) {
							System.out.printf("\t%s\t", depart.get(count)[i]);
						}
						System.out.println();
						num++;
					}
					// 페이지 반복
					System.out.println();
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.printf("\t\t\t" + "%d페이지 입니다.\n", count + 1);
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t" + "1. 이전 페이지");
					System.out.println("\t\t\t" + "2. 다음 페이지");
					System.out.println("\t\t\t3. 입력하기");
					System.out.println();

					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 번호 고르기
					Scanner scan = new Scanner(System.in);
					System.out.print("\t\t\t" + "입력:  ");
					String answer = scan.nextLine();
					// scan.skip("\r\n"); //엔터 무시
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 이전페이지
					if (answer.equals("1")) {
						// 첫페이지 일 경우
						if (count == 0) {
							count = depart.size() - 1;
							num = depart.size() * 10 - 9;
						} else {
							count = count - 1;
							num -= 20;
						}
					} else if (answer.equals("2")) {
						// 다음페이지

						// 마지막 페이지일 경우
						if (count == depart.size() - 1) {
							count = 0;
							num = 1;
						} else {
							count = count + 1;

						}
					} else if (answer.equals("3")) {
						break;

					} else if (answer.equals("0")) {
						// 뒤로가기
						return;
					}

					else {
						System.out.println("\t\t\t다시 입력하세요.");

					}
				}

			} // while

		}
		/**
		 * 십진 중분류를 페이징하여 보여줄 메소드
		 * @param arrayList 로그인한 관리자의 객체
		 * @param adminUser 십진 중분류 리스트를 담을 객체
		 */
		public void page2(List<String> arrayList, AdminUser adminUser) {
			// 십진 중분류 리스트

			// 10개씩 분할
			List<String[]> depart = new ArrayList<String[]>();
			int firstindex = 0;
			int lastindex = 10;
			int index = 0;
			int totalcount = arrayList.size();

			while (true) {
				// 10개의 String 이 들어갈 묶음
				String[] ranking = new String[10];

				for (int j = firstindex; j < lastindex; j++) {
					if (j >= arrayList.size()) {
						break;
					}
					ranking[j - (index) * 10] = arrayList.get(j);
				}
				depart.add(ranking);

				firstindex = firstindex + 10;
				lastindex = lastindex + 10;
				index++;

				if (firstindex >= totalcount) {
					break;
				}

			}

			// 페이지 수
			int count = 0;

			int num = 1;

			// 페이지가 한개만 있는 경우
			if (depart.size() == 1) {
				// 첫페이지 보여주기
				for (int i = 0; i < depart.get(0).length; i++) {
					if (depart.get(0)[i] != null) {
						System.out.printf("\t\t\t%d. %s\n", i + 1, depart.get(0)[i]);
					}
				}
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t" + "넘어갈 페이지가 없습니다.");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			} else {

				// 페이지가 여러개인 경우
				while (true) {

					System.out.println();
					for (int i = 0; i < 10; i++) {
						System.out.printf("\t\t\t%d. ", num);
						if (depart.get(count)[i] != null) {
							System.out.printf("\t%s\t", depart.get(count)[i]);
						}
						System.out.println();
						num++;
					}
					// 페이지 반복
					System.out.println();
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.printf("\t\t\t" + "%d페이지 입니다.\n", count + 1);
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t" + "1. 이전 페이지");
					System.out.println("\t\t\t" + "2. 다음 페이지");
					System.out.println("\t\t\t3. 입력하기");
					System.out.println();

					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 번호 고르기
					Scanner scan = new Scanner(System.in);
					System.out.print("\t\t\t" + "입력:  ");
					String answer = scan.nextLine();
					// scan.skip("\r\n"); //엔터 무시
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 이전페이지
					if (answer.equals("1")) {
						// 첫페이지 일 경우
						if (count == 0) {
							count = depart.size() - 1;
							num = depart.size() * 10 - 9;
						} else {
							count = count - 1;
							num -= 20;
						}
					} else if (answer.equals("2")) {
						// 다음페이지

						// 마지막 페이지일 경우
						if (count == depart.size() - 1) {
							count = 0;
							num = 1;
						} else {
							count = count + 1;

						}
					} else if (answer.equals("3")) {
						break;

					} else if (answer.equals("0")) {
						// 뒤로가기
						return;
					}

					else {
						System.out.println("\t\t\t다시 입력하세요.");

					}
				}

			} // while

		}
		/**
		 * 십진 소분류를 페이징하여 보여줄 메소드
		 * @param arrayList 로그인한 관리자의 객체
		 * @param adminUser 십진 소분류 리스트를 담을 객체
		 */
		public void page3(List<String> arrayList, AdminUser adminUser) {
			// 십진 소분류 리스트

			// 10개씩 분할
			List<String[]> depart = new ArrayList<String[]>();
			int firstindex = 0;
			int lastindex = 10;
			int index = 0;
			int totalcount = arrayList.size();

			while (true) {
				// 10개의 String 이 들어갈 묶음
				String[] ranking = new String[10];

				for (int j = firstindex; j < lastindex; j++) {
					if (j >= arrayList.size()) {
						break;
					}
					ranking[j - (index) * 10] = arrayList.get(j);
				}
				depart.add(ranking);

				firstindex = firstindex + 10;
				lastindex = lastindex + 10;
				index++;

				if (firstindex >= totalcount) {
					break;
				}

			}

			// 페이지 수
			int count = 0;

			int num = 1;

			// 페이지가 한개만 있는 경우
			if (depart.size() == 1) {
				// 첫페이지 보여주기
				for (int i = 0; i < depart.get(0).length; i++) {
					if (depart.get(0)[i] != null) {
						System.out.printf("\t\t\t%d. %s\n", i + 1, depart.get(0)[i]);
					}
				}
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t" + "넘어갈 페이지가 없습니다.");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			} else {

				// 페이지가 여러개인 경우
				while (true) {

					System.out.println();
					for (int i = 0; i < 10; i++) {
						System.out.printf("\t\t\t%d. ", num);
						if (depart.get(count)[i] != null) {
							System.out.printf("\t%s\t", depart.get(count)[i]);
						}
						System.out.println();
						num++;
					}
					// 페이지 반복
					System.out.println();
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.printf("\t\t\t" + "%d페이지 입니다.\n", count + 1);
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t" + "1. 이전 페이지");
					System.out.println("\t\t\t" + "2. 다음 페이지");
					System.out.println("\t\t\t3. 입력하기");
					System.out.println();

					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 번호 고르기
					Scanner scan = new Scanner(System.in);
					System.out.print("\t\t\t" + "입력:  ");
					String answer = scan.nextLine();
					// scan.skip("\r\n"); //엔터 무시
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					// 이전페이지
					if (answer.equals("1")) {
						// 첫페이지 일 경우
						if (count == 0) {
							count = depart.size() - 1;
							num = depart.size() * 10 - 9;
						} else {
							count = count - 1;
							num -= 20;
						}
					} else if (answer.equals("2")) {
						// 다음페이지

						// 마지막 페이지일 경우
						if (count == depart.size() - 1) {
							count = 0;
							num = 1;
						} else {
							count = count + 1;

						}
					} else if (answer.equals("3")) {
						break;

					} else if (answer.equals("0")) {
						// 뒤로가기
						return;
					}

					else {
						System.out.println("\t\t\t다시 입력하세요.");

					}
				}

			} // while

		}

		/**
		 * 도서 정보 삭제하는 메소드
		 * - 불필요한 도서 정보 삭제 가능
		 * @param adminUser 로그인한 관리자의 객체
		 */
		public void DeleteBookInfo(AdminUser adminUser) {
			// 3. 도서 삭제

			Scanner scan = new Scanner(System.in);
			Connection conn = null;

			PreparedStatement stat = null;
			DBUtil util = new DBUtil();
			ResultSet rs = null;

			try {
				// procedure 불러오기
				String sql = "select * from vwSelectBookList where 도서코드 like ? ";

				// 사용자 입력
				System.out.println("\t\t\t삭제할 도서코드를 입력해주세요.");
				System.out.print("\t\t\t▷입력: ");
				String DeleteUserInput = scan.nextLine();
				// 문자열의 공백제거
				DeleteUserInput = DeleteUserInput.replaceAll("\\p{Z}", "");

				// 0입력 시 뒤로가기
				if (DeleteUserInput.equals("0")) {
					return;
				} else {

					// oepn sql
					conn = util.open();

					// sql 호출
					stat = conn.prepareStatement(sql);

					// 사용자 입력값 명령값으로 넣기
					stat.setString(1, "%" + DeleteUserInput + "%");

					// 그외 정보 불러오기
					rs = stat.executeQuery();

					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t[번호]\t[도서코드]\t\t[분류]\t\t[출판사]\t\t[저자]\t\t[도서명]");
					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// 출력
					while (rs.next()) {

						System.out.printf("\t%s\t%s\t%s\t\t%s\t\t%s\t%s\n", rs.getString("번호"), rs.getString("도서코드"),
								rs.getString("분류"), rs.getString("출판사"), rs.getString("저자"), rs.getString("도서명"));
					} // while

					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					System.out.println("\t\t\t삭제할 도서의 번호를 입력하세요. ");
					System.out.print("\t\t\t▷입력: ");
					String DeleteBookseq = scan.nextLine();
					// 유효성검사 (숫자만가능)

					if (Pattern.matches("^[0-9]*$", DeleteBookseq)) {

						System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

						System.out.println("\t\t\t1. 삭제하기");
						System.out.println("\t\t\t0. 뒤로가기");
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.print("\t\t\t▷입력: ");
						String DeleteUserInputNum = scan.nextLine();

						if (DeleteUserInputNum.equals("1")) {
							// 1. 삭제하기

							Connection conn2 = null;
							Statement stat2 = null;

							conn2 = util.open();

							String BookCodesql = String.format(
									"update tblBookstate set delete_exist = 1 where book_code = '%s' and book_seq = '%s' ",
									DeleteBookseq, DeleteBookseq);

							stat2 = conn2.createStatement();
							stat2.executeUpdate(BookCodesql);

							stat.close();
							conn.close();
							System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println("\t\t\t삭제 되었습니다.\n");
							System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

							BacktoMain(adminUser);

						} else if (DeleteUserInputNum.equals("0")) {
							// 0. 뒤로가기
							return;

						} else {
							System.out.println("\t\t\t다시 입력해주세요.");
						}

					} else {
						System.out.println("\t\t\t다시 입력해주세요.");
					}

					// close
					rs.close();
					stat.close();
					conn.close();

				} // DeleteUserInput else

			} catch (Exception e) {
				System.out.println("\t\t\t에러발생,,");

			} // catch

		}

		/**
		 * 도서 정보를 수정하는 메소드
		 * - 도서의 출판사, 저자, 도서명 수정 가능
		 * @param adminUser 로그인한 관리자 객체
		 */
		public void UpdateBookName(AdminUser adminUser) {
			// 2. 도서 수정

			Scanner scan = new Scanner(System.in);
			Connection conn = null;
			PreparedStatement stat = null;
			DBUtil util = new DBUtil();
			ResultSet rs = null;

			try {
				// procedure 불러오기

				// 사용자 입력
				System.out.println("\t\t\t수정할 도서코드를 입력해주세요.");
				System.out.print("\t\t\t▷입력: ");
				String UpdateUserInput = scan.nextLine();
				// 문자열의 공백제거
				UpdateUserInput = UpdateUserInput.replaceAll("\\p{Z}", "");

				// 0입력 시 뒤로가기
				if (UpdateUserInput.equals("0")) {
					return;

				} else {

					// oepn sql
					conn = util.open();

					String sql = "select * from vwSelectBookList where 도서코드 like ?";

					// sql 호출
					stat = conn.prepareStatement(sql);

					// 사용자 입력값 명령값으로 넣기
					stat.setString(1, "%" + UpdateUserInput + "%");

					// 정보 불러오기
					rs = stat.executeQuery();

					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t[번호]\t[도서코드]\t\t[분류]\t[출판사]\t\t[저자]\t\t[도서명]");
					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// 출력
					while (rs.next()) {

						System.out.printf("\t%s\t%s\t%s\t%s\t\t%s\t%s\n", rs.getString("번호"), rs.getString("도서코드"),
								rs.getString("분류"), rs.getString("출판사"), rs.getString("저자"), rs.getString("도서명"));
					} // while

					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					System.out.println("\t\t\t수정할 도서의 번호를 입력하세요. ");
					System.out.print("\t\t\t▷입력: ");
					String UpdateBookseq = scan.nextLine();

					// 0입력 시 뒤로가기
					if (UpdateBookseq.equals("0")) {
						return;

						// 아니면 계속 진행
					} else {

						System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
//				System.out.println("\t\t\t1. 도서 코드/대분류/소분류");
						System.out.println("\t\t\t1. 출판사");
						System.out.println("\t\t\t2. 저자");
						System.out.println("\t\t\t3. 도서명");
						System.out.println("\t\t\t0. 뒤로가기");
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.print("\t\t\t▷입력: ");
						String UserInput = scan.nextLine();
						System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");


						if (UserInput.equals("1")) {
							// 1. 출판사
							Connection conn2 = null;
							Statement stat2 = null;

							System.out.println("\t\t\t수정 내용을 입력해주세요.");
							System.out.print("\t\t\t▷입력: ");
							String publicsherUpdateInput = scan.nextLine();

							// 0입력 시 뒤로가기
							if (publicsherUpdateInput.equals("0")) {
								return;

							} else {

								conn2 = util.open();
								String BookCodesql = String.format("update tblBook set publisher = '%s' where seq = %s ",
										publicsherUpdateInput, UpdateBookseq);

								stat2 = conn2.createStatement();
								stat2.executeUpdate(BookCodesql);

								System.out.println("\t\t\t수정 완료되었습니다.");

								stat.close();
								conn.close();

							} // publicsherUpdateInput else

							BacktoMain(adminUser);

						} else if (UserInput.equals("2")) {
							// 2. 저자
							Connection conn2 = null;
							Statement stat2 = null;

							System.out.println("\t\t\t수정 내용을 입력해주세요.");
							System.out.print("\t\t\t▷입력: ");
							String authorUpdateInput = scan.nextLine();

							// 0입력 시 뒤로가기
							if (authorUpdateInput.equals("0")) {
								return;
							} else {

								conn2 = util.open();
								String BookCodesql = String.format("update tblBook set author = '%s' where seq = %s ",
										authorUpdateInput, UpdateBookseq);

								stat2 = conn2.createStatement();
								stat2.executeUpdate(BookCodesql);

								System.out.println("\t\t\t수정 완료되었습니다.");

								stat.close();
								conn.close();

							} // authorUpdateInput else
							BacktoMain(adminUser);

						} else if (UserInput.equals("3")) {
							// 3. 도서명
							Connection conn2 = null;
							Statement stat2 = null;

							System.out.println("\t\t\t수정 내용을 입력해주세요.");
							System.out.print("\t\t\t▷입력: ");
							String nameUpdateInput = scan.nextLine();

							// 0입력 시 뒤로가기
							if (nameUpdateInput.equals("0")) {
								return;
							} else {

								conn2 = util.open();
								String BookCodesql = String.format("update tblBook set book_name = '%s' where seq = %s ",
										nameUpdateInput, UpdateBookseq);

								stat2 = conn2.createStatement();
								stat2.executeUpdate(BookCodesql);

								System.out.println("\t\t\t수정 완료되었습니다.");

								stat.close();
								conn.close();
							} // nameUpdateInput else

							BacktoMain(adminUser);

						} else if (UserInput.equals("0")) {
							// 0. 뒤로가기 -> UpdateBookMain
							return;

						} else { // exception
							System.out.println("\t\t\t다시 입력해주세요.");
						} // else

						// close
						rs.close();
						stat.close();
						conn.close();

					} // UpdateUserInput else
				} // UpdateBookList else
			} catch (Exception e) {

				System.out.println("\t\t\t에러발생.,,");

			} // catch

		}

		/**
		 * 뒤로가기를 구현 하기 위한 메소드
		 * @param adminUser 로그인한 관리자의 객체
		 */
		public void BacktoMain(AdminUser adminUser) {
			// 뒤로가기 구현 Method

			Scanner scan = new Scanner(System.in);

			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String BackUserInput = scan.nextLine();

			if (BackUserInput.equals("0")) {
				// 0.뒤로가기
				UpdateBookList(adminUser);
			} else {
				System.out.println("\t\t\t다시 입력해주세요.");
			}
		}

		/**
		 * 신규 도서를 추가 할 수 있는 메소드
		 * - 신규 도서 정보를 입력 시 자동적으로 도서코드를 생성하여 기록가능
		 * @param adminUser 로그인한 관리자의 객체
		 */
		public void AddBook2(AdminUser adminUser) {
			// 2. 도서 추가

			Scanner scan = new Scanner(System.in);
			Connection conn = null;
			CallableStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();

			// User Input
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t추가하려는 도서의 정보를 입력해주세요.");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷도서명: ");
			String BookNameInput = scan.nextLine();

			// 0입력 시 뒤로가기
			if (BookNameInput.equals("0")) {
				return;

			} else {

				System.out.print("\t\t\t▷출판사: ");
				String PublisherInput = scan.nextLine();

				// 0입력 시 뒤로가기
				if (PublisherInput.equals("0")) {
					return;

				} else {

					System.out.print("\t\t\t▷저자: ");
					String AuthorInput = scan.nextLine();

					// 0입력 시 뒤로가기
					if (AuthorInput.equals("0")) {
						return;

					} else {

						// 대분류 번호 출력
						DecimalSmallList(adminUser);
						System.out.print("\t\t\t▷소분류 코드: ");
						String DecimalInput = scan.nextLine();

						// 0입력 시 뒤로가기
						if (DecimalInput.equals("0")) {
							return;

						} else {

							// 중분류 번호 출력
							DecimalMiddle(adminUser,DecimalInput);
							System.out.print("\t\t\t▷중분류 코드: ");
							String MiddleSeriesInput = scan.nextLine();

							// 0입력 시 뒤로가기
							if (MiddleSeriesInput.equals("0")) {
								return;

							} else {

								// 소분류 리스트 출력
								DecimalList(adminUser,DecimalInput);
								System.out.print("\t\t\t▷대분류 코드: ");
								String SmallSeriesInput = scan.nextLine();

								// 0입력 시 뒤로가기
								if (SmallSeriesInput.equals("0")) {
									return;

								} else {

									// 중복책번호
									System.out.print("\t\t\t▷수량 : ");
									String SameBook = scan.nextLine();

									// 0입력 시 뒤로가기
									if (SameBook.equals("0")) {
										return;

									} else {

										String location = null;

										while (true) {
											System.out.print("\t\t\t▷위치: ");
											location = scan.nextLine();
											if (Pattern.matches("^[a-zA-Z]*$", location)) {
												break;
												// for location
											} else {
												System.out.println("\t\t\t다시 입력해주세요.");
											} // else
										} // while
										
										try {
											// tblBook insert procedure
											String Booksql = "{ call ProcAddBook(?,?,?,?,?,?,?) }";

											// open
											conn = util.open();

											// connect tblBook
											stat = conn.prepareCall(Booksql);

											// Location의 유효성 검사

											System.out.println("1");

											// 도서 코드 생성
											String Bookcode = SmallSeriesInput + "-" + BookNameInput.substring(0, 1)
													+ AuthorInput.substring(0, 1) + "-s" + MiddleSeriesInput + "-" + "v0"
													+ SameBook;

											// 유효성검사
											if (SmallSeriesInput.length() < 1000 && MiddleSeriesInput.length() < 100
													&& DecimalInput.length() < 10) {

												// tblBook setString
												stat.setString(1, BookNameInput);
												stat.setString(2, PublisherInput);
												stat.setString(3, AuthorInput);
												stat.setString(4, SmallSeriesInput);
												stat.setString(5, MiddleSeriesInput);
												stat.setString(6, Bookcode);
												stat.setString(7, location);

											} else {
												System.out.println("\t\t\t범위 내 숫자만 입력가능합니다.");
												AddBook2(adminUser); // 다시입력
											} // else

											System.out.println("2");
											stat.executeQuery();

											System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
											System.out.println("\t\t\t등록 완료 했습니다.");
											System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

											// 결과출력
											System.out.printf(
													"\n\t\t\t[도서명]: %s\n\t\t\t[출판사]: %s\n\t\t\t[저자]: %s\n\t\t\t[대분류 코드]: %s\n\t\t\t[중분류 코드]: %s\n\t\t\t[소분류 코드]: %s\n\t\t\t[도서코드]: %s\n\t\t\t[중복책권수]: %s\n\t\t\t[위치]: %s\n",
													BookNameInput, PublisherInput, AuthorInput, SmallSeriesInput,
													MiddleSeriesInput, DecimalInput, Bookcode, SameBook, location);

											// close
											stat.close();
											conn.close();

										} catch (Exception e) {
											e.printStackTrace();
											System.out.println("Error in AddBook2");

										} // catch

									} // BookNameInput else
								}
							} // AuthorInput else

						} // DecimalInput else

					} // MiddleSeriesInput else

				} // SmallSeriesInput else

			} // samebook else
			System.out.println("\n\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			String AddUserInput = scan.nextLine();

			if (AddUserInput.equals("0")) {
				// 0. 뒤로가기 -> UpdateBookMain
				return;

			} else {
				System.out.println("\t\t\t번호를 다시 입력해주세요.");

			} // else

		}// AddBook2()

		
	/**
	 *  도서 십진분류코드의 중분류 리스트를 저장한 메소드
	 * @param 로그인한 관리자의 객체
	 */
		public void DecimalMiddle(AdminUser adminUser, String DecimalInput) {
			// 도서 십진분류코드 중분류

			ArrayList<String> list = new ArrayList<String>();
			Connection conn = null;
			PreparedStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();

			try {
				
					// open
					conn = util.open();

					// 명령문
					String sql = "select * from  tblDecimalCategory where num between 10 and 99 and num like ?";
					// connect
					stat = conn.prepareStatement(sql);
					// 입력값 넣어주기
					stat.setString(1, "%" + DecimalInput);
					// 불러오기
					rs = stat.executeQuery();

				System.out.println();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t" + "<십진중분류>");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				System.out.println("\t\t\t[번호]\t[코드]\t[항목]");

				// 출력
				while (rs.next()) {
					list.add(rs.getString("decimalcode") + "\t" + rs.getString("item"));

				}
				page2(list, adminUser);

				// close
				rs.close();
				stat.close();
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		/**
		 *  도서 십진분류코드의 소분류 리스트를 저장한 메소드
		 * @param 로그인한 관리자의 객체
		 */
		public void DecimalSmallList(AdminUser adminUser) {
			// 도서 십진분류코드 소분류

			Connection conn = null;
			CallableStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();
			ArrayList<String> list = new ArrayList<String>();

			try {
				// 리스트 생성

				// 프로시저 부르기
				String smallsql = "{ call ProcDecimalsmall(?) }";

				// open
				conn = util.open();

				stat = conn.prepareCall(smallsql);

				// 커서부르기
				stat.registerOutParameter(1, OracleTypes.CURSOR);

				// 출력요청
				stat.executeQuery();

				// 출력값 담기
				rs = (ResultSet) stat.getObject(1);

				System.out.println();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t" + "<십진소분류>");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				System.out.println("\t\t\t[번호]\t[코드]\t[항목]");

				// 출력
				while (rs.next()) {
					list.add(rs.getString("decimalcode") + "\t" + rs.getString("item"));
				}
				page3(list, adminUser);
				// close
				rs.close();

				stat.close();

				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		/**
		 *  도서 십진분류코드의 중분류 리스트를 저장한 메소드
		 * @param 로그인한 관리자의 객체
		 */
		public void DecimalList(AdminUser adminUser,String DecimalInput) {
			// 도서 십진분류코드 대분류 리스트

			ArrayList<String> list = new ArrayList<String>();
			Connection conn = null;
			PreparedStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();

			try {
				
					// open
					conn = util.open();

					// 명령문
					String sql = "select * from  tblDecimalCategory where num between 100 and 999 and num like ?";
					// connect
					stat = conn.prepareStatement(sql);
					// 입력값 넣어주기
					stat.setString(1, "%" + DecimalInput);
					// 불러오기
					rs = stat.executeQuery();
				System.out.println();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t" + "<십진대분류> ");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				System.out.println("\t\t\t[번호]\t[코드]\t[항목]");

				// 출력
				while (rs.next()) {
					list.add(rs.getString("decimalcode") + "\t" + rs.getString("item"));

				}
				page(list, adminUser);

				// close
				rs.close();
				stat.close();
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}//Catch
		}// DecimalList
		
	}// UpdateBook
}