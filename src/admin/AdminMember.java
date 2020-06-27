package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

/**
 * 관리자-회원관리 관련 메소드들을 멤버로 갖는 클래스입니다.
 * @author Doyun Lee
 *
 */
public class AdminMember {
	//관리자 - 회원 관리
	
	public static void main(String[] args) {
		
		AdminUser u = new AdminUser();
		AdminMember m = new AdminMember();
		m.memberMenu(u);
		
	}
	
	/**
	 * 관리자-회원관리 메인 메뉴입니다.
	 * @param adminUser	로그인한 관리자 객체를 매개변수로 받아옵니다.
	 */
	public void memberMenu(AdminUser adminUser) {
		
		while (true) {
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t            회원 관리");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 회원 조회"); 
			System.out.println("\t\t\t2. 연체 내역 조회"); 
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String num = sc.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			// 사용자에게 번호 입력받음

			// 회원 조회
			if (num.equals("1")) {
				findMember();				
			}
			// 연체 회원 관리
			else if (num.equals("2")) {
				lateMemberMenu();
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
	}//memberMenu
	
	/**
	 * 회원의 이름 중 1~2글자를 입력받아 회원번호 안내 후, 회원번호를 입력받아 1명의 회원정보를 출력하는 메소드입니다.
	 */
	public void findMember() {
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		try {
			
			//헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t            회원 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t▷이전 메뉴로 돌아가시려면 0을 입력하세요.");
			System.out.print("\t\t\t▷조회하고 싶은 이름에 포함된 글자를 입력하세요.: ");
			String name = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			//0을 입력하지 않은 경우
			if (!name.equals("0")) {
				
				//DB 연결
				conn = util.open("localhost","lms","java1234");
				stat = conn.createStatement();
				
				//입력받은 글자가 포함된 회원의 번호와 이름 출력을 위해 쿼리문 생성
				String sql = String.format("select * from tblMember where name like '%%%s%%' and withdrawal=0", name);
				
				//쿼리 실행 후 결과 받아옴
				rs = stat.executeQuery(sql);
				
				if (rs.next()) {
					
					//헤더 출력
					System.out.println("\t\t\t\t[회원번호]\t[회원이름]");
					
					//if에서 소비한 rs.next를 do-while문을 이용하여 소비하고 다음으로 넘어감.
					do  {
						System.out.printf("\t\t\t\t%6s\t\t  %s\n"
														, rs.getString("seq")
														, rs.getString("name"));
					} while (rs.next());
					
					//정보 조회할 회원 번호 입력받기
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t▷이전 메뉴로 돌아가시려면 0을 입력하세요.");
					System.out.print("\t\t\t▷회원 번호: ");
					String num = scan.nextLine();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println();
					
					//0을 입력하지 않은 경우
					if (!num.equals("0")) {
					
						//입력받은 회원 번호로 회원 정보 가져오기
						sql = String.format("select * from tblMember where seq = %s and withdrawal = 0", num);
						rs = stat.executeQuery(sql); 
						
						if (rs.next()) {
						//입력받은 회원 번호가 있는 경우 
							
							do { 
								System.out.printf("\t\t\t이름: %s\n", rs.getString("name"));
								System.out.printf("\t\t\t생년월일: %s\n", rs.getString("ssn").substring(0, 6));
								System.out.printf("\t\t\t전화번호: %s\n", rs.getString("tel"));
								System.out.printf("\t\t\t주소: %s\n", rs.getString("address"));		
								System.out.println();
								
							} while(rs.next());
							
							//하단부 출력
							System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println();
							
						} else {
							//입력받은 회원 번호가 없는 경우
							System.out.println("\t\t\t검색 결과가 없습니다.");
						}
						
					//회원 번호에 0을 입력한 경우
					} else {
						//아무 행동도 하지 않고 아래 else절 바깥으로 이동						
					}
				} else {
					//입력한 단어가 포함된 이름이 없을 경우
					System.out.println("\t\t\t검색 결과가 없습니다.");
				}
				
				//DB연결 끊어주기
				stat.close();
				conn.close();
			}
			
			//뒤로가기 전 잠시 쉬기
			System.out.println("\t\t\t엔터를 입력하면 이전 메뉴로 돌아갑니다.");
			scan.nextLine();
			
		} catch (Exception e) {
			e.printStackTrace();		
		}//try-catch
		
	}//findMember
	
	/**
	 * 연체회원관리 메뉴를 출력하고 세부 메뉴를 입력받는 메소드입니다.
	 */
	public void lateMemberMenu() {

		while (true) {
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t          연체 내역 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 기간별 조회"); 
			System.out.println("\t\t\t2. 회원별 조회"); 
			System.out.println("\t\t\t3. 도서별 조회"); 
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String num = sc.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			// 사용자에게 번호 입력받음

			// 날짜별 조회
			if (num.equals("1")) {
				procPrintLateListByDate();	
			}
			// 회원별 조회
			else if (num.equals("2")) {
				procPrintLateListByMember();
			}
			// 도서별 조회
			else if (num.equals("3")) {
				procPrintLateListByBook();
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
	}//lateMemberMenu
	
	/**
	 * 날짜를 입력받아 연체 내역을 출력하는 메소드입니다.
	 */
	public void procPrintLateListByDate() {
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("\t\t\t\t           기간별 조회");
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println();
		
		//사용자가 입력을 제대로 할 때 까지 반복하기 위해서 while문 사용
		while (true) {
			System.out.println("\t\t\t이전 메뉴로 돌아가시려면 0을 입력하세요.");
			System.out.println("\t\t\t시작 날짜 입력");
			System.out.print("\t\t\t▷년(YYYY): ");
			String syear = scan.nextLine();
			System.out.print("\t\t\t▷월(MM): ");
			String smonth = scan.nextLine();
			System.out.print("\t\t\t▷일(DD): ");
			String sday = scan.nextLine();
			System.out.println("\t\t\t종료 날짜 입력");
			System.out.print("\t\t\t▷년(YYYY): ");
			String eyear = scan.nextLine();
			System.out.print("\t\t\t▷월(MM): ");
			String emonth = scan.nextLine();
			System.out.print("\t\t\t▷일(DD): ");
			String eday = scan.nextLine();
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			//입력받은 모든 값이 0이면 while문 바깥으로 이동
			if (syear.equals("0") || smonth.equals("0") || sday.equals("0")
					|| eyear.equals("0") || emonth.equals("0") || eday.equals("0")) {
				
				break;	
			
			//입력받은 값이 0이 아닐 경우 유효성 검사 시작
			} else {
			
				try {
					//입력받은 날짜가 숫자인지 확인해 보기 위해 숫자로 변환해보기
					int sy = Integer.parseInt(syear);
					int sm = Integer.parseInt(smonth);
					int sd = Integer.parseInt(sday);
					int ey = Integer.parseInt(eyear);
					int em = Integer.parseInt(emonth);
					int ed = Integer.parseInt(eday);
					
					//년도를 2100년 이후로 입력했을 경우
					if (sy > 2100 || ey > 2100) {
						System.out.println("\t\t\t유효한 년도를 입력하세요.");
						System.out.println();
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println();
					
					//월을 1~12 이외의 값으로 입력했을 경우
					} else if ((sm > 12 || sm < 1) || (em > 12 || em < 1)) {
						System.out.println("\t\t\t유효한 월을 입력하세요.");
						System.out.println();
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println();
					
					//일을 1~31 이외의 값으로 입력했을 경우/
					} else if ((sd > 31 || sd < 1) || (ed > 31 || ed < 1)) {
						System.out.println("\t\t\t유효한 일을 입력하세요.");
						System.out.println();
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println();
					
					//유효한 날짜를 입력했을 경우
					} else {

						//DB연결
						conn = util.open("localhost", "lms", "java1234");
						String sql = "{ call procPrintLateListByDate(?,?,?) }";
						stat = conn.prepareCall(sql);
						
						//YYYY-MM-DD 형식으로 입력받은 날짜를 설정해줌
						String sdate = String.format("%s-%s-%s", syear, smonth, sday);
						String edate = String.format("%s-%s-%s", eyear, emonth, eday);
						
						//형식에 맞춘 날짜를 매개변수로 넣어줌
						stat.setString(1, sdate);
						stat.setString(2, edate);
						//cursor값을 받아오기 위해 미리 준비해줌
						stat.registerOutParameter(3, OracleTypes.CURSOR);
						
						//쿼리 실행
						stat.executeQuery();
						
						//커서값 받아오기
						rs = (ResultSet)stat.getObject(3);

						//해당 날짜에 연체 내역이 있는 경우
						if (rs.next()) {
						
							//헤더 출력
							System.out.println("\t[회원명]\t[대여날짜]\t[반납날짜]\t[연장횟수]\t[연체일수]\t[도서명]");
							
							//if에서 소비한 rs.next값을 지나치지 않기 위해 while문 대신 do-while문 사용
							do {
								System.out.printf("\t %s\t\t%s\t%s\t%7s회\t%7s일\t%s\n", rs.getString("member")
																			, rs.getString("rent")
																			, rs.getString("return")
																			, rs.getString("extension")
																			, rs.getString("lateDate")
																			, rs.getString("bookname"));
							} while(rs.next());
							
							//하단부 출력
							System.out.println();
							System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println();
							
							//DB연결 끊어주기
							stat.close();
							conn.close();

							//리스트 출력 완료 후 while문 바깥으로 빠져나가기
							break;
													
						//해당 날짜에 연체 내역이 없는 경우
						} else {
							System.out.println("\t\t\t입력하신 기간에 해당하는 연체 내역이 없습니다.");
							System.out.println();
							System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println();
							
							//DB연결 끊어주기
							stat.close();
							conn.close();
							
							//while문 바깥으로 나가줌
							break;
						}//if
					}//if					
		
				//에러가 발생한 경우 숫자가 아니기 때문에 숫자로 입력하라는 안내메세지 출력
				} catch (Exception e) {
					System.out.println("\t\t\t날짜를 숫자로 입력하세요.");
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println();
				}//try-catch
			}//if			
		}//while
		
		
		System.out.println("\t\t\t엔터를 입력하면 이전 메뉴로 돌아갑니다.");
		scan.nextLine();
		
	}//procPrintLateListByDate
	
	
	/**
	 * 회원명 출력 후 회원번호를 입력받아 연체 내역을 출력하는 메소드입니다.
	 */
	public void procPrintLateListByMember() {
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		Statement stat = null;
		CallableStatement cstat= null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		try {
			
			//헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t            회원별 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t▷이전 메뉴로 돌아가시려면 0을 입력하세요.");
			System.out.print("\t\t\t▷조회하고 싶은 이름에 포함된 글자를 입력하세요.: ");
			String name = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			//0을 입력하지 않은 경우
			if (!name.equals("0")) {
				
				
				//DB 연결
				conn = util.open("localhost","lms","java1234");
				stat = conn.createStatement();
				
				//입력받은 글자가 포함된 회원의 번호와 이름 출력을 위해 쿼리문 생성
				String sql = String.format("select * from tblMember where name like '%%%s%%' and withdrawal=0", name);
				
				//쿼리 실행 후 결과 받아옴
				rs = stat.executeQuery(sql);
				
				if (rs.next()) {
					
					//헤더 출력
					System.out.println("\t\t\t\t[회원번호]\t[회원이름]");
					
					//if에서 소비한 rs.next를 do-while문을 이용하여 소비하고 다음으로 넘어감.
					do  {
						System.out.printf("\t\t\t\t%6s\t\t  %s\n"
														, rs.getString("seq")
														, rs.getString("name"));
					} while (rs.next());
					
					//정보 조회할 회원 번호 입력받기
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t▷이전 메뉴로 돌아가시려면 0을 입력하세요.");
					System.out.print("\t\t\t▷회원 번호: ");
					String num = scan.nextLine();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println();
					
					//0을 입력하지 않은 경우
					if (!num.equals("0")) {
					
						//입력받은 회원 번호로 회원 정보 가져오기
						sql = "{ call procPrintLateListByMember(?,?) }";
						
						//쿼리 실행 준비
						cstat = conn.prepareCall(sql);
						cstat.setString(1, num);
						cstat.registerOutParameter(2, OracleTypes.CURSOR);
						
						//쿼리 실행
						cstat.executeQuery();
						
						rs = (ResultSet)cstat.getObject(2);
						
						if (rs.next()) {
						//입력받은 회원의 연체 내역이 있는 경우 

							System.out.println("\t[회원명]\t[대여날짜]\t[반납날짜]\t[연장횟수]\t[연체일수]\t[도서명]");
							
							//if에서 소비한 rs.next값을 지나치지 않기 위해 while문 대신 do-while문 사용
							do {
								System.out.printf("\t %s\t\t%s\t%s\t%7s회\t%7s일\t%s\n", rs.getString("member")
																						, rs.getString("rent")
																						, rs.getString("return")
																						, rs.getString("extension")
																						, rs.getString("lateDate")
																						, rs.getString("bookname"));
							} while(rs.next());
							
							//하단부 출력
							System.out.println();
							System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println();
							
							
						} else {
							//입력받은 회원 번호가 없는 경우
							System.out.println("\t\t\t연체 내역이 없습니다.");
						}
						
						//cstat 닫아줌
						cstat.close();
						
					//회원 번호에 0을 입력한 경우
					} else {
						//아무 행동도 하지 않고 아래 else절 바깥으로 이동						
					}
				} else {
					//입력한 단어가 포함된 이름이 없을 경우
					System.out.println("\t\t\t검색 결과가 없습니다.");
				}
				
				//DB연결 끊어주기
				stat.close();
				conn.close();
			}
			
			//뒤로가기 전 잠시 쉬기
			System.out.println("\t\t\t엔터를 입력하면 이전 메뉴로 돌아갑니다.");
			scan.nextLine();
			
		} catch (Exception e) {
			e.printStackTrace();		
		}//try-catch

	}//procPrintLateListByMember

	/**
	 * 도서에 포함된 단어를 입력받아 도서번호 조회 후 해당 도서번호를 입력하여 연체내역을 조회하는 메소드입니다.
	 */
	public void procPrintLateListByBook() {
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		Statement stat = null;
		CallableStatement cstat= null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		try {
			
			//헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t            도서별 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t▷이전 메뉴로 돌아가시려면 0을 입력하세요.");
			System.out.print("\t\t\t▷조회하고 싶은 도서명에 포함된 단어를 입력하세요.: ");
			String name = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			//0을 입력하지 않은 경우
			if (!name.equals("0")) {
				
				
				//DB 연결
				conn = util.open("localhost","lms","java1234");
				stat = conn.createStatement();
				
				//입력받은 단어가 포함된 도서의 번호와 이름 출력을 위해 쿼리문 생성
				String sql = String.format("select * from tblbook where replace(trim(book_name), ' ', '') like '%%%s%%' order by seq", name);
				
				//쿼리 실행 후 결과 받아옴
				rs = stat.executeQuery(sql);
				
				if (rs.next()) {
					
					//헤더 출력
					System.out.println("\t\t\t[도서번호]\t[도서명]");
					
					//if에서 소비한 rs.next를 do-while문을 이용하여 소비하고 다음으로 넘어감.
					do  {
						System.out.printf("\t\t\t%6s\t\t  %s\n"
														, rs.getString("seq")
														, rs.getString("book_name"));
					} while (rs.next());
					
					//정보 조회할 회원 번호 입력받기
					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t▷이전 메뉴로 돌아가시려면 0을 입력하세요.");
					System.out.print("\t\t\t▷도서 번호: ");
					String num = scan.nextLine();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println();
					
					//0을 입력하지 않은 경우
					if (!num.equals("0")) {
					
						//입력받은 회원 번호로 회원 정보 가져오기
						sql = "{ call procPrintLateListByBook(?,?) }";
						
						//쿼리 실행 준비
						cstat = conn.prepareCall(sql);
						cstat.setString(1, num);
						cstat.registerOutParameter(2, OracleTypes.CURSOR);
						
						//쿼리 실행
						cstat.executeQuery();
						
						rs = (ResultSet)cstat.getObject(2);
						
						if (rs.next()) {
						//입력받은 회원의 연체 내역이 있는 경우 

							System.out.println("\t[회원명]\t[대여날짜]\t[반납날짜]\t[연장횟수]\t[연체일수]\t[도서명]");
							
							//if에서 소비한 rs.next값을 지나치지 않기 위해 while문 대신 do-while문 사용
							do {
								System.out.printf("\t %s\t\t%s\t%s\t%7s회\t%7s일\t%s\n", rs.getString("member")
																						, rs.getString("rent")
																						, rs.getString("return")
																						, rs.getString("extension")
																						, rs.getString("lateDate")
																						, rs.getString("bookname"));
							} while(rs.next());
							
							//하단부 출력
							System.out.println();
							System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println();
							
							
						} else {
							//입력받은 회원 번호가 없는 경우
							System.out.println("\t\t\t연체 내역이 없습니다.");
						}
						
						//cstat 닫아줌
						cstat.close();
						
					//회원 번호에 0을 입력한 경우
					} else {
						//아무 행동도 하지 않고 아래 else절 바깥으로 이동						
					}
				} else {
					//입력한 단어가 포함된 이름이 없을 경우
					System.out.println("\t\t\t검색 결과가 없습니다.");
				}
				
				//DB연결 끊어주기
				stat.close();
				conn.close();
			}
			
			//뒤로가기 전 잠시 쉬기
			System.out.println("\t\t\t엔터를 입력하면 이전 메뉴로 돌아갑니다.");
			scan.nextLine();
			
		} catch (Exception e) {
			e.printStackTrace();		
		}//try-catch
		
	}
}



