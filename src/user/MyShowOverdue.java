package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import admin.DBUtil;
import oracle.jdbc.internal.OracleTypes;

/**
 * 
 * @author Sujin Shin
 * 회원 마이페이지의 "연체 내역 조회 클래스"
 *
 */
public class MyShowOverdue {

	/**
	 * 
	 * 연체 내역을 보여주는 메소드
	 * - 현재 단위기간 누적 연체일을 보여줌
	 * - 기간을 입력받음
	 * - 입력받은 기간 내의 모든 연체 내역 출력
	 * @param memberUser 로그인한 회원의 객체
	 */
	public void show(MemberUser memberUser) {
		
		Scanner sc = new Scanner(System.in);
		
		MemberUser user = memberUser;
		int seq = user.getNum();	// 회원번호
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		CallableStatement callstat = null;
		ResultSet rs = null;
		
		conn = util.open("localhost", "lms", "java1234");
		
		// 현재 단위기간 누적 연체일 체크 및 출력
		int overdueDate = 0;
		Calendar now = Calendar.getInstance();
		int nYear = now.get(Calendar.YEAR);
		String nYearStart = String.format("%d-01-01", nYear);
		String nYearEnd = String.format("%d-12-31", nYear);

		try {
					
			String sql = "{ call procshowOverdue(?, ?, ?, ?) }";
			callstat = conn.prepareCall(sql);
			
			callstat.setInt(1, seq);
			callstat.setString(2, nYearStart);
			callstat.setString(3, nYearEnd);
			callstat.registerOutParameter(4, OracleTypes.CURSOR);
			
			callstat.executeUpdate();
			
			rs=(ResultSet)callstat.getObject(4);

			while (rs.next()) {
				overdueDate += Integer.parseInt(rs.getString("overdue"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println();
		System.out.printf("\t\t\t %d년 회원님의 누적 연체일 : %d", nYear, overdueDate);
		System.out.println();
		
		// 기간 입력
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println();
		System.out.println("\t\t\t 0. 뒤로가기");
		System.out.println("\t\t\t 조회할 지난 연체 내역 기간을 입력하세요.");
		System.out.println();
		System.out.println("\t\t\t 시작일");
		String sYear, sMonth, sDate, eYear, eMonth, eDate, start = null, end = null;
		
		//뒤로가기 flag
		boolean flag = true;
		while (flag) {
			
			// 기간입력 flag
			boolean term = true;
			while (term) {
				
				while (true) {
					System.out.print("\t\t\t 년(YYYY) : ");
					sYear = sc.nextLine();
					try {
						if (sYear.equals("0")) {
							System.out.println("\t\t\t 이전 메뉴로 돌아가겠습니다. 계속하시려면 아무키나 눌러주세요.");
							sc.nextLine();
							flag = false;
							term = false;
							break;
						} else if (Integer.parseInt(sYear) >= 2018 
								&& Integer.parseInt(sYear) <= now.get(Calendar.YEAR)) {
							break;
						} else {
							System.out.println("\t\t\t 2018년 이후만 검색 가능합니다.");
							System.out.println();					
						}
					} catch (Exception e) {
						System.out.println("\t\t\t 올바른 값을 입력하세요.");
						System.out.println();
					}
				}
				if (flag == false) { break; }
				while (true) {
					System.out.print("\t\t\t 월(MM) : ");
					sMonth = sc.nextLine();
					try {
						if (sMonth.equals("0")) {
							System.out.println("\t\t\t 이전 메뉴로 돌아가겠습니다. 계속하시려면 아무키나 눌러주세요.");
							sc.nextLine();
							flag = false;
							term = false;
							break;
						} else if (Integer.parseInt(sMonth) >= 01
								&& Integer.parseInt(sMonth) <= 12) {
							break;
						} else {
							System.out.println("\t\t\t 올바른 값을 입력하세요.");
							System.out.println();
						}
					} catch (Exception e) {
						System.out.println("\t\t\t 올바른 값을 입력하세요.");
						System.out.println();
					}
				}
				if (flag == false) { break; }
				while (true) {
					System.out.print("\t\t\t 일(DD) : ");
					sDate = sc.nextLine();
					try {
						if (sDate.equals("0")) {
							System.out.println("\t\t\t 이전 메뉴로 돌아가겠습니다. 계속하시려면 아무키나 눌러주세요.");
							sc.nextLine();
							flag = false;
							term = false;
							break;
						} else if (Integer.parseInt(sDate) >= 01
								&& Integer.parseInt(sDate) <= 31) {
							break;
						} else {
							System.out.println("\t\t\t 올바른 값을 입력하세요.");
							System.out.println();
						}
					} catch (Exception e) {
						System.out.println("\t\t\t 올바른 값을 입력하세요.");
						System.out.println();
					}
				}
				if (flag == false) { break; }
				System.out.println();
				System.out.println("\t\t\t 종료일");
				while (true) {
					System.out.print("\t\t\t 년(YYYY) : ");
					eYear = sc.nextLine();
					try {
						if (eYear.equals("0")) {
							System.out.println("\t\t\t 이전 메뉴로 돌아가겠습니다. 계속하시려면 아무키나 눌러주세요.");
							sc.nextLine();
							flag = false;
							term = false;
							break;
						} else if (Integer.parseInt(eYear) >= 2018 
								&& Integer.parseInt(eYear) <= now.get(Calendar.YEAR)) {
							break;
						} else {
							System.out.println("\t\t\t 2018년 이후만 검색 가능합니다.");
							System.out.println();
						}
					} catch (Exception e) {
						System.out.println("\t\t\t 올바른 값을 입력하세요.");
						System.out.println();
					}
				}
				if (flag == false) { break; }
				while (true) {
					System.out.print("\t\t\t 월(MM) : ");
					eMonth = sc.nextLine();
					try {
						if (eMonth.equals("0")) {
							System.out.println("\t\t\t 이전 메뉴로 돌아가겠습니다. 계속하시려면 아무키나 눌러주세요.");
							sc.nextLine();
							flag = false;
							term = false;
							break;
						} else if (Integer.parseInt(eMonth) >= 01
								&& Integer.parseInt(eMonth) <= 12) {
							break;
						} else {
							System.out.println("\t\t\t 올바른 값을 입력하세요.");
							System.out.println();
						}
					} catch (Exception e) {
						System.out.println("\t\t\t 올바른 값을 입력하세요.");
						System.out.println();
					}
				}
				if (flag == false) { break; }
				while (true) {
					System.out.print("\t\t\t 일(DD) : ");
					eDate = sc.nextLine();
					try {
						if (eDate.equals("0")) {
							System.out.println("\t\t\t 이전 메뉴로 돌아가겠습니다. 계속하시려면 아무키나 눌러주세요.");
							sc.nextLine();
							flag = false;
							term = false;
							break;
						} else if (Integer.parseInt(eDate) >= 01
								&& Integer.parseInt(eDate) <= 31) {
							break;
						} else {
							System.out.println("\t\t\t 올바른 값을 입력하세요.");
							System.out.println();
						}
					} catch (Exception e) {
						System.out.println("\t\t\t 올바른 값을 입력하세요.");
						System.out.println();
					}
				}
				if (flag == false) { break; }
				System.out.println();
				
				// 시작일, 종료일
				Calendar startDay = Calendar.getInstance();
				startDay.set(Integer.parseInt(sYear), Integer.parseInt(sMonth)-1, Integer.parseInt(sDate));
				Calendar endDay = Calendar.getInstance();
				endDay.set(Integer.parseInt(eYear), Integer.parseInt(eMonth)-1, Integer.parseInt(eDate));
				start = String.format("%tF", startDay);
				end = String.format("%tF", endDay);
				
				if (endDay.getTimeInMillis() - startDay.getTimeInMillis() < 0) {
					System.out.println("\t\t\t 종료일이 시작일보다 이전입니다. 기간을 다시 입력하세요.");
					System.out.println();
				} else {
					break;
				}
			}

			if (term == false) { break; }
			System.out.println();
			
			// 연체 대여 내역을 저장할 ArrayList
			List<String> overduelist = new ArrayList<String>();
			
			try {
				
				// 연체 대여 내역이 있는지 확인하기
				String sql = "{ call procshowOverdue(?, ?, ?, ?) }";
				callstat = conn.prepareCall(sql);
				
				callstat.setInt(1, seq);
				callstat.setString(2, start);
				callstat.setString(3, end);
				callstat.registerOutParameter(4, OracleTypes.CURSOR);
				
				callstat.executeUpdate();
				
				rs=(ResultSet)callstat.getObject(4);
				
				while (rs.next()) {
					String overdue = rs.getString("overdue");
					if (overdue.contains(".")) {
						overdue = overdue.substring(0,overdue.indexOf("."));
					}
					String list = String.format("%s▣%s▣%s▣%s▣%s▣%s"
							, rs.getString("bname")
							, rs.getString("bpubli")
							, rs.getString("rentd").split(" ")[0]
									, rs.getString("returnd").split(" ")[0]
											, rs.getString("exten")
											, overdue);
					overduelist.add(list);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (overduelist.size() > 0) {
				// 입력 기간 내에 연체 기록 있음
				if(overduelist.size() <= 10) {
					// 연체기록 10건 이내
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println();
					System.out.println("[번호]\t[대여일]\t[반납일]    [연장횟수] [연체일수]\t\t\t[책제목]\t\t\t\t\t[출판사]");
					
					for (int i=1; i<=overduelist.size(); i++) {
						
						String[] list = overduelist.get(i-1).split("▣");
						int ext = Integer.parseInt(list[5]);
						
						if(ext <= 0) {
							System.out.printf("  %d\t%s\t%s\t  %s\t   %s\t\t%-60s%-30s\r\n"
									, i, list[2], list[3], list[4], "0", list[0], list[1]);					
						} else {
							System.out.printf("  %d\t%s\t%s\t  %s\t   %s\t\t%-60s%-30s\r\n"
									, i, list[2], list[3], list[4], list[5], list[0], list[1]);
						}
						
					}
					
					System.out.println();
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t계속 하시려면 아무 키나 입력하세요.");
					sc.nextLine();
					System.out.println();
					break;
				} else {
					int pageNum = overduelist.size()/10;
					if (overduelist.size()%10==0) {
						pageNum--;
					}
					int nowpage = 0;
					
					boolean pageflag = true;
					while (pageflag) {
						
						System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println();
						System.out.println("[번호]\t[대여일]\t[반납일]    [연장횟수] [연체일수]\t\t\t[책제목]\t\t\t\t\t[출판사]");
						
						int nowmin = (nowpage * 10) + 1;
						int nowmax = (nowpage * 10) + 10;
						
						if(nowpage == pageNum) {
							nowmax = overduelist.size();
						}
						
						for (int i=nowmin; i<=nowmax; i++) {
							
							String[] list = overduelist.get(i-1).split("▣");
							int ext = Integer.parseInt(list[5]);
							
							if(ext <= 0) {
								System.out.printf("  %d\t%s\t%s\t  %s\t   %s\t\t%-60s%-30s\r\n"
										, i, list[2], list[3], list[4], "0", list[0], list[1]);					
							} else {
								System.out.printf("  %d\t%s\t%s\t  %s\t   %s\t\t%-60s%-30s\r\n"
										, i, list[2], list[3], list[4], list[5], list[0], list[1]);
							}
							
						}

						System.out.println();
						System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						
						System.out.println("\t\t\t1. 다음 페이지"); 
						System.out.println("\t\t\t2. 이전 페이지"); 
						System.out.println("\t\t\t0. 뒤로가기");
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						while (true) {
							System.out.print("\t\t\t ▷ 입력: ");
							String input = sc.nextLine();
							if (input.equals("0")) {
								System.out.println("\t\t\t 이전 메뉴로 돌아가겠습니다.");
								System.out.println("\t\t\t 계속하시려면 아무키나 눌러주세요.");
								sc.nextLine();
								pageflag = false;
								flag = false;
								break;
							} else if (input.equals("1")) {
								if (nowpage == pageNum) { nowpage = 0; }
								else { nowpage++; }
								break;
							} else if (input.equals("2")) {
								if (nowpage == 0) { nowpage = pageNum; }
								else { nowpage--; }
								break;
							} else {
								System.out.println("\t\t\t 올바른 값을 입력하세요.");
								System.out.println();
							}
						}
					}
				}
				
			} else {
				
				// 입력 기간 내에 연체 기록 없음
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t 입력한 기간 내에 연체하신 내역이 없습니다.");
				System.out.println("\t\t\t 이전 메뉴로 돌아가겠습니다. 계속하시려면 아무키나 눌러주세요.");
				sc.nextLine();
				System.out.println();
				break;
			}
		}
			
		
	}//show
}
