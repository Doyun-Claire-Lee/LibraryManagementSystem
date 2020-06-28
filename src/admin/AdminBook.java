package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;


public class AdminBook {
	//관리자 - 도서 관리
	
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
				
			}
			// 예약 확인
			else if (num.equals("3")) {
				CheckReservation reserve = new CheckReservation();
				reserve.SelectReservation(adminUser);
				
			}
			// 반납 처리 및 확인
			else if (num.equals("4")) {
				ReturnBook  ReturnBook = new ReturnBook();
				ReturnBook.RentalCheck(adminUser);
				
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
	
	
	
	//1. 도서조회
	public class SelectBookList {
	
		public void SelectBook(AdminUser adminUser) {
			//도서관리 1. 도서목록조회 
	
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
				// open
				conn = util.open();
	
				// 명령문
				String sql = "select * from vwSelectBook where 도서번호  like ?";
				// connect
				stat = conn.prepareStatement(sql);
				//입력값 넣어주기
				stat.setString(1, "%" + SelectUserInput + "%");
				//불러오기
				rs = stat.executeQuery();
	
				System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t[번호]\t[도서코드]\t\t\t[분류]\t[위치]\t[도서명]");
				System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
				// 출력
				while (rs.next()) {
	
					System.out.printf("\t%s\t%s\t\t%s\t%s\t%s\n", rs.getString("seq"), rs.getString("도서번호"),
							rs.getString("분류"), rs.getString("위치"), rs.getString("도서명"));
				} // while
			
				System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	
				//close
				stat.close();
				conn.close();
	
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
	
				AdminBook main = new AdminBook();
				main.bookMenu(adminUser);
	
			} else {
				System.out.println("\t\t\t다시 입력해주세요.");
			}
	
		}// SelectBook()
	}// SelectBookList
	
	
	
	//3. 예약 확인
	public class CheckReservation {
		
			
			public void SelectReservation(AdminUser adminUser) {
				
				Scanner scan = new Scanner(System.in);
				
				
				Connection conn = null;
				CallableStatement stat = null;
				DBUtil util = new DBUtil();
				ResultSet rs = null;


			try {
				//procedure 불러오기
				String sql = "{ call procCheckReserve(?,?) }";
				
				//사용자 입력
				System.out.println("\t\t\t조회할 예약날짜를 입력하세요. ");
				System.out.print("\t\t\t▷입력(YY/MM/DD): ");
				String SelectUserInput = scan.nextLine();
			
				//유효성 검사 맞으면 진행
			if (SelectUserInput.matches("\\d{2}/\\d{2}/\\d{2}")) {
			
				
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("[번호]\t\t[회원번호]\t\t[회원명]\t\t[예약날짜]\t\t[저자명]\t\t[도서명]");
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					
					//oepn sql
					conn=util.open();
					
					//sql 호출
					stat = conn.prepareCall(sql);
				
					//사용자 입력값 명령값으로 넣기
					stat.setString(1, SelectUserInput);
					
					//그외 정보 불러오기
					stat.registerOutParameter(2, OracleTypes.CURSOR);
					stat.executeQuery();
					rs = (ResultSet) stat.getObject(2);
					ResultSetMetaData rsmd = rs.getMetaData();
			
			//유효성 검사 else
			} else {
				System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t잘못입력하셨습니다. 다시 입력해주세요.");
				SelectReservation(adminUser);
				
			}//else 
					
					//출력
					while (rs.next()) { 
						
						System.out.printf("%s\t\t%s\t\t%s\t\t%s\t%s\t%s\n",rs.getString("번호")
																,rs.getString("회원번호")
																,rs.getString("회원명")
																,rs.getString("예약날짜")
																,rs.getString("저자명")
																,rs.getString("도서명"));
					}//while
					
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				
					//close
					rs.close();
					stat.close();
					conn.close();
				
					} catch (Exception e) {
						System.out.println("\t\t\t에러발생,,");
						
					
					}//catch
			

				
					//뒤로가기
					System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t0. 뒤로가기");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t▷입력: ");
					String backtUserInput = scan.nextLine();
				
					
					if (backtUserInput.equals("0")) {
					//0. 뒤로가기
						
					AdminBook main = new AdminBook();
					main.bookMenu(adminUser);
				
				} else {
					System.out.println("\t\t\t다시 입력해주세요.");
				 
				}//else


			
			}//SelectReservation()

		}//CheckReservation
	
	
	//4. 반납 처리 및 확인
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
					AdminBook main = new AdminBook();
					main.bookMenu(adminUser);
	
				} else {
					System.out.println("\t\t\t다시 입력해주세요.");
				}
	
			}// RentalCheck()
	
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
					// 문자열의 공백제거
					InputBookCode = InputBookCode.replaceAll("\\p{Z}", "");
	
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("[회원번호]\t\t[회원명]\t\t[대여일]\t\t[반납일]\t\t[반납여부]\t\t[연체횟수]\t\t[도서코드]\t\t\t[도서명]");
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	
					// oepn sql
					conn = util.open();
	
					// sql 호출
					stat = conn.prepareCall(sql);
	
					// 사용자 입력값 명령값으로 넣기
					stat.setString(1, InputBookCode);
	
					// 그외 정보 불러오기
					stat.registerOutParameter(2, OracleTypes.CURSOR);
					stat.executeQuery();
					rs = (ResultSet) stat.getObject(2);
					ResultSetMetaData rsmd = rs.getMetaData();
	
					// 출력
					while (rs.next()) {
	
						System.out.printf("%s\t\t%s\t\t%s\t%s\t\t%s\t\t%s\t\t%s\t\t%s\n", rs.getString("회원번호"),
								rs.getString("성함"), rs.getString("대여날짜"), rs.getString("반납날짜"), rs.getString("반납여부"),
								rs.getString("연체여부"), rs.getString("도서코드"), rs.getString("도서명"));
	
					} // while
	
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
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
						RentalCheck(adminUser);
	
					} else { // exception
						System.out.println("\t\t\t다시 입력해주세요.");
					}
	
					// close
					rs.close();
					stat.close();
					conn.close();
	
				} catch (Exception e) {
					System.out.println("\t\t\t조회되지 않습니다. 다시 입력해주세요.");
					UpdateRental(adminUser);
				} // catch
	
			} // UpdateRental()
	
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
	
				try {
	
					// open sql
					conn = util.open();
	
					// connet with sql
					stat = conn.createStatement();
	
					// sql update 명령
					String sql = String.format(
							"update tblRent set return_date = sysdate where member_seq= %s and book_code= '%s'", MemberNumInput,
							InputBookCode);
	
					// sql select 명령
					String selectsql = String.format("select * vwCheckReturnBook where 도서코드 = %s and 회원번호 = %s", InputBookCode,
							MemberNumInput);
	
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
						RentalCheck(adminUser);
	
					} else { // exception
						System.out.println("\t\t\t번호를 다시 입력해주세요.");
					}
	
				} catch (Exception e) {
					System.out.println("\t\t\t조회 되지 않습니다. 다시 입력해주세요.");
					returnstate(InputBookCode, adminUser);
				}
	
			}
	
			public void CheckPeriodBook(AdminUser adminUser) {
				// 도서관리-반납확인-내역조회
	
				Connection conn = null;
				Statement stat = null;
				ResultSet rs = null;
				DBUtil util = new DBUtil();
				Scanner scan = new Scanner(System.in);
	
				try {
	
					// 사용자 입력
					System.out.println("\t\t\t조회할 날짜를 입력해주세요");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t▷시작일 입력(YY/MM/DD): ");
					String StartDateInput = scan.nextLine();
					System.out.print("\t\t\t▷종료일 입력(YY/MM/DD): ");
					String EndDateInput = scan.nextLine();
					// 유효성 검사 맞으면 진행
					if (StartDateInput.matches("\\d{2}/\\d{2}/\\d{2}") && EndDateInput.matches("\\d{2}/\\d{2}/\\d{2}")) {
	
						// open
						conn = util.open();
	
						// connet
						stat = conn.createStatement();
	
						// sql select 명령
						String sql = String.format("select * from vwCheckReturnBook where 대여날짜 = '%s' and 반납날짜 = '%s'",
								StartDateInput, EndDateInput);
	
						// 값 가져오기
						rs = stat.executeQuery(sql);
	
						// 유효성 검사 else
					} else {
						System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println("\t\t\t잘못입력하셨습니다. 다시 입력해주세요.");
					} // else
	
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("[도서코드]\t\t\t[고객명]\t\t[회원번호]\t\t[대여일]\t\t[반납일]\t\t[반납여부]\t\t[연체횟수]\t\t[도서명]");
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	
					// 명령값 출력
					while (rs.next()) {
	
						System.out.printf("%s\t\t%s\t\t%s\t\t%s\t%s\t%s\t\t%s\t\t%s\n", rs.getString("도서코드"), rs.getString("성함"),
								rs.getString("회원번호"), rs.getString("대여날짜"), rs.getString("반납날짜"), rs.getString("반납여부"),
								rs.getString("연체여부"), rs.getString("도서명"));
					} // while
	
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	
					// 뒤로가기
					System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t0. 뒤로가기");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t▷입력: ");
					String BackUserInput = scan.nextLine();
					System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					if (BackUserInput.equals("0")) {
						// 0.뒤로가기
						RentalCheck(adminUser);
	
					} else { // exception
						System.out.println("\t\t\t번호를 다시 입력해주세요.");
					}
	
				} catch (Exception e) {
					System.out.println("\t\t\t조회 되지 않습니다. 다시 입력해주세요.");
					CheckPeriodBook(adminUser);
				}
	
			}
	
		}// ReturnBook
	
	
}
