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
 * 회원 마이페이지의 "대여 내역 조회 클래스"
 *
 */
public class MyShowRent {

	/**
	 * 
	 * 도서 대여 내역 조회 메뉴를 보여주는 메소드
	 * - 현재 대여 내역과 지난 대여 내역을 확인할 수 있음
	 * - 현재 대여 내역에서는 대여중인 도서 연장신청 가능
	 * @param memberUser 로그인한 회원의 객체
	 */
	public void menu (MemberUser memberUser) {

		Scanner sc = new Scanner(System.in);
		
		while (true) {
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t                도서 대여 내역");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 현재 대여 내역 조회"); 
			System.out.println("\t\t\t2. 지난 대여 내역 조회"); 
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String num = sc.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
		
			// 현재 대여 내역 조회
			if (num.equals("1")) {
				nowRent(memberUser);
			}
			// 지난 대여 내역 조회
			else if (num.equals("2")) {
				prevRent(memberUser);
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
		
	}//menu
	
	/**
	 * 
	 * 현재 대여중인 도서 내역을 보여주는 메소드
	 * - 대여중인 도서 출력
	 * - 연장하고 싶은 도서 선택 후 연장 가능
	 * @param memberUser 로그인한 회원의 객체
	 */
	private void nowRent(MemberUser memberUser) {
		
		Scanner sc = new Scanner(System.in);
		
		MemberUser user = memberUser;
		int seq = user.getNum();	// 회원번호
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		CallableStatement callstat = null;
		ResultSet rs = null;
		
		// 현재 대여 내역을 저장할 ArrayList
		List<String> rentlist = new ArrayList<String>();

		conn = util.open("localhost", "lms", "java1234");
		
		try {
			
			// 현재 대여내역이 있는지 확인하기
			String sql = "{ call procShowNowrent(?, ?) }";
			callstat = conn.prepareCall(sql);
			
			callstat.setInt(1, seq);
			callstat.registerOutParameter(2, OracleTypes.CURSOR);
			
			callstat.executeUpdate();
			
			rs=(ResultSet)callstat.getObject(2);
			
			
			while (rs.next()) {
				String overdue = rs.getString("overdue");
				if (overdue.contains(".")) {
					overdue = overdue.substring(0,overdue.indexOf("."));
				}
				String list = String.format("%s▣%s▣%s▣%s▣%s▣%s▣%s"
						, rs.getString("bname")
						, rs.getString("bpubli")
						, rs.getString("rentd").split(" ")[0]
						, rs.getString("expect_returnd").split(" ")[0]
						, rs.getString("exten")
						, overdue
						, rs.getString("rentseq"));
				rentlist.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (rentlist.size() > 0) {
			
			// 현재 대여중인 도서가 있는 경우
			
			System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			System.out.println("[번호]\t[대여일]\t[반납예정일] [연장횟수] [연체일수]\t\t\t[책제목]\t\t\t\t\t[출판사]");
			
			String[] list = new String[7];
			for (int i=1; i<=rentlist.size(); i++) {
				
				list = rentlist.get(i-1).split("▣");
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
			System.out.println();
			while (true) {
				
				System.out.println("\t\t\t0. 뒤로가기");
				System.out.println("\t\t\t연장 신청을 하실 도서 번호를 입력해주세요.");
				System.out.print("\t\t\t▷ 입력: ");
				String input = sc.nextLine();
				
				if (Integer.parseInt(input) > 0 && Integer.parseInt(input) <= rentlist.size()) {
					
					// 연장신청
					String rentseq = rentlist.get(Integer.parseInt(input)-1).split("▣")[6];

					try {
						
						String sql = "{ call procUpdateExtension(?, ?) }";
						callstat = conn.prepareCall(sql);
						
						callstat.setString(1, rentseq);
						callstat.registerOutParameter(2, OracleTypes.NUMBER);
						
						callstat.executeUpdate();
						
						if (callstat.getString(2).equals("1")) {
							
							// 연장 신청 완료
							System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println("\t\t\t 연장 신청이 완료되었습니다.");
							System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
							sc.nextLine();
							
						} else if (callstat.getString(2).equals("2")) {
							
							// 예약 존재함. 연장 불가
							System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println("\t\t\t 이후 예약이 존재합니다. 연장을 할 수 없습니다.");
							System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
							sc.nextLine();
							
						}  else if (callstat.getString(2).equals("3")) {
							
							// 예약 2번 완료. 연장 불가
							System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println("\t\t\t 예약 가능 횟수는 2번 입니다.");
							System.out.println("\t\t\t 이미 2번 연장 하였으므로 추가 연장이 불가합니다.");
							System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
							sc.nextLine();
							
						} else {
							
							// 연장 실패
							System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println("\t\t\t 연장 신청을 실패하였습니다.");
							System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
							sc.nextLine();
						}
				
					} catch (Exception e) {
						e.printStackTrace();
					}
						
				} else if (input.equals("0")) {
					
					// 뒤로가기
					break;
					
				} else {
					
					// 잘못된 입력
					System.out.println("\t\t\t 번호를 다시 입력해주세요");
					System.out.println();
				}
				
			}
			
		} else {
			
			// 현재 대여중인 도서가 없는 경우
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t 현재 대여중인 도서가 없습니다.");
			System.out.println("\t\t\t 이전 메뉴로 돌아가겠습니다.");
			System.out.println("\t\t\t 계속 하시려면 아무 키나 입력하세요.");
			sc.nextLine();
			System.out.println();
			
		}
				
	}//nowRent
	
	/**
	 * 
	 * 이전 대여 내역을 보여주는 메소드
	 * - 조회할 기간 입력
	 * - 이전 대여 내역 출력
	 * @param memberUser 로그인한 회원의 객체
	 */
	private void prevRent (MemberUser memberUser) {

		Scanner sc = new Scanner(System.in);
		
		Calendar now = Calendar.getInstance();
		// 기간 입력
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println();
		System.out.println("\t\t\t 0. 뒤로가기");
		System.out.println("\t\t\t 조회할 지난 대여 내역 기간을 입력하세요.");
		System.out.println();
		System.out.println("\t\t\t 시작일");
		String sYear, sMonth, sDate, eYear, eMonth, eDate, start = null, end = null;
		// 뒤로가기 flag
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
		
			MemberUser user = memberUser;
			int seq = user.getNum();	// 회원번호
			
			DBUtil util = new DBUtil();
			Connection conn = null;
			CallableStatement callstat = null;
			ResultSet rs = null;
			
			// 이전 대여 내역을 저장할 ArrayList
			List<String> prevrentlist = new ArrayList<String>();
	
			conn = util.open("localhost", "lms", "java1234");
			
			try {
				
				// 이전 대여내역이 있는지 확인하기
				String sql = "{ call procshowPreviewrent(?, ?, ?, ?) }";
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
					prevrentlist.add(list);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (prevrentlist.size() > 0) {
				// 이전 대여 도서가 있는 경우
				if(prevrentlist.size() <= 10) {
					// 이전 대여 도서가 10개 이하인 경우
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println();
					System.out.println("[번호]\t[대여일]\t[반납일]    [연장횟수] [연체일수]\t\t\t[책제목]\t\t\t\t\t[출판사]");
					
					for (int i=1; i<=prevrentlist.size(); i++) {
						
						String[] list = prevrentlist.get(i-1).split("▣");
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
					break;
				} else {
					int pageNum = prevrentlist.size()/10;
					if (prevrentlist.size()%10==0) {
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
							nowmax = prevrentlist.size();
						}
						
						for (int i=nowmin; i<=nowmax; i++) {
							
							String[] list = prevrentlist.get(i-1).split("▣");
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
				
				// 이전 대여 도서가 없는 경우
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t이전에 대여한 도서가 없습니다. 이전 메뉴로 돌아가겠습니다.");
				System.out.println("\t\t\t계속 하시려면 아무 키나 입력하세요.");
				sc.nextLine();
				System.out.println();
				break;
			}
		}
	}//prevRent
	
}
