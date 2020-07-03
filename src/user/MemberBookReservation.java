package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;


import admin.DBUtil;
import oracle.jdbc.internal.OracleTypes;

/**
 * 
 * @author shin
 * 회원이 책을 빌리고 싶은데 해당 책이 미반납인 경우에 예약을 해준다.
 *
 */
public class MemberBookReservation {
	
	/**
	 * 회원이 책을 예약할 수 있게 해주는 메서드
	 * @param user 회원정보
	 * @param bookCode 도서정보번호
	 */
	public void rentOrBack(MemberUser user,int bookCode) {
		Scanner scan = new Scanner(System.in);
		
		List<String[]> dataList = new ArrayList<String[]>();//예약 정보를 담아주는 리스트.
		
		boolean flag = true;
		
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("\t\t\t해당 책은 아직 미반납 상태여서 예약만 가능합니다. ");
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓" ;
		System.out.println();
		System.out.println();
		System.out.println();
			
		while(flag) {//예약 메뉴 
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1.해당 책 예약하기 ");
			System.out.println("\t\t\t0.뒤로가기 ");
			System.out.print("\t\t\t▷번호 선택: ");
			String input = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
			MemberTransSeq mts = new MemberTransSeq();
			int userSeq = mts.userSeq(user.pw, user.id);//회원의 회원번호를 불러오기
			
			if (input.equals("1")) {//1.해당 책 예약하기
				flag = false;//반복문 끊어주기
			
				/* 고려해야 할 가능성
				 * 1. 내가 해당책을 이미 예약한 경우
				 * 2. 다른사람이 먼저 예약을해서 내 순위가 밀리는 경우.
				 **3. 내가 해당 책을 가지고 있는경우 ㅋㅋㅋㅋ 
				 **/
				
				dataList = checkMyList(bookCode);//예약 정보를 담아주는 리스트.
				
				boolean my = true;//현재는 해당 책에 대한 예약이 가능한 상태
				
				for (int i = 0; i < dataList.size(); i++) {
					
					if (dataList.get(i)[0].equals(""+userSeq)) {//내가 이미 예약 해놓은것
						my = false;//빌릴 수 없음
					}
				}//for
				
				//내가 해당책을 이미 대여중인경우도 불가능하다.-> 메서드로 뺴주자
				boolean myRent = myRentList(userSeq,bookCode);//내가 빌리지 않았으면 true 가 나올것 빌렸으면 false가 나올것
				
				
				//if(myRentList(userSeq,bookCode))
				
				
				
				
				if (my && myRent) { //예약이 가능한 경우
					
					//메서드를 따로 빼서 쓰자
					resPossible(dataList,userSeq,bookCode);//예약을 진행
					
				} else if (my == false){//예약이 불가능한 경우 -> 내가 이미 예약을 한 경우
					System.out.println("\t\t\t회원님 께서는 이미 해당 도서에 대한 예약내역이 존재합니다.");
					System.out.println("\t\t\t계속 하시려면 엔터를 누르세요 ");
					String back = scan.nextLine();//뒤로 돌아가기
				} else {//내가 해당책을 이미 소유하고 있는 경우
					System.out.println("\t\t\t회원님 께서는 이미 해당 도서에 대한 대여내역이 존재합니다.");
					System.out.println("\t\t\t계속 하시려면 엔터를 누르세요 ");
					String back = scan.nextLine();//뒤로 돌아가기
					
				}
				
				
				
				
				
			} else if (input.equals("0")) {//0.뒤로가기
				flag = false;
			} else {//잘못 입력하는 경우
				System.out.println("\t\t\t1또는 0번을 눌러주세요.");

			}
			
			
		}//while()
		
		
		
	}//rentOrBack()
	
	/**
	 * 해당 책을 이미 예약을 한 사람이 있는지 확인해주는 메서드
	 * @param bookInfo 도서정보번호
	 * @return 해당 책을 예약한 예약자들의 정보
	 */
	public List<String[]> checkMyList(int bookInfo) {
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		List<String[]> dataList = new ArrayList<String[]>();//예약 정보를 담아주는 리스트.
		
		try {
			
			String[] dataArr = new String[2];//멤버번호와 예약날짜를 담기위함
			
			String sql = "{call procListOfRes(?,?)}";//프로시저 불러주기
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, bookInfo);//도서정보번호 넣어주기
			stat.registerOutParameter(2, OracleTypes.CURSOR);//커서 넣어주기!
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			while(rs.next()) {
				dataArr[0] = "" + rs.getInt(1);//**멤버번호
				dataArr[1] = rs.getString(2).substring(0,11);//**예약날짜
				
				dataList.add(dataArr);//리스트에 넣어줌
				
			}
			
			stat.close();
			conn.close();
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("뭔가 잘못됨");
		}
		
		return dataList;
	}//checkMyList()
	
	
	
	/**
	 * 해당 회원이 해당 도서를 예약을 했는지 확인해주는 메소드
	 * @param userSeq 유저번호
	 * @param bookCode 도서정보번호
	 * @return 참 또는 거짓
	 */
	public boolean myRentList(int userSeq,int bookCode) {
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		boolean answer = true;//해당 책 번호가 존재하면 false 로 바꾸어 줄것이다.
		
		
		try {
			
			String sql = "{call procNotReturnBookList(?,?)}";//프로시저 불러주기
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, userSeq);//회원번호 집어넣어주기
			stat.registerOutParameter(2, OracleTypes.CURSOR);//커서 넣어주기
			
			stat.executeQuery();
		
			rs = (ResultSet)stat.getObject(2);
			
			while(rs.next()) {
				if (rs.getInt(4)==bookCode) {
					answer = false;
				}
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("오류가 발생");
		}
		
		return answer;
		
	}
	
	/** 
	 * 도서에 대한 예약을 진행한다.
	 * @param dataList 예약 정보를 담아주는 리스트.
	 * @param userSeq 회원번호
	 * @param bookInfo 도서정보번호
	 */
	public void resPossible(List<String[]> dataList, int userSeq,int bookInfo) {
		//dataList -> (멤버번호,예약날짜)
		//여기서 고려해줘야 하는것은 자신보다 먼저 해당책을 모두 예약한 사람이 있는경우에 자신의 예약날짜가 미뤄져야 한다는것이다.
		//만일 예약내역이 없다면 그냥 내가 빌릴 수 있는것이다. -> 즉 예약내역이 없으려면 dataList의 길이가 0 이면된다.
		
		
		if (dataList.size() == 0) {//기다리지 않고 예약을 할 수 있다.
			//책을 가져간 사람들 중에 날짜가 반납 날짜가 더 빠른 사람을 커넥시켜서 그날짜 +1 에 대여할 수 있도록 만들어준다. 
			
			Connection conn = null;
			CallableStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();
			
			try {
				
				//여기서 시간을 고려해서 더 빠른 시간 + 1일 로 예약을 잡아주면 된다. 그리고 나서 예약을 진행해주면 된다.
				
				String sql = "{call procRentPossible(?,?)}";//프로시저 불러주기
				
				conn = util.open("localhost", "lms", "java1234");
				stat = conn.prepareCall(sql);
				
				
				stat.setInt(1, bookInfo);//도서정보번호 넣어주기
				stat.registerOutParameter(2, OracleTypes.CURSOR);//커서 넣어주기
				
				stat.executeQuery();
				
				rs = (ResultSet)stat.getObject(2);
				
				long minDate = Long.MAX_VALUE;//틱값을 받아줄 놈을 대기시킨다. -> 가장 높은값으로 진행.
				
				Calendar rc = Calendar.getInstance();
				
				while(rs.next()) {
					Calendar c = Calendar.getInstance();
			
					StringTokenizer stk = new StringTokenizer(rs.getString(1).substring(0,11),"-");//반납일 가져오기
					int year = Integer.parseInt(stk.nextToken());//년
					int mon = Integer.parseInt(stk.nextToken())-1;//월 -> 1을 빼줘야함
					int date = Integer.parseInt(stk.nextToken().trim());//일
					
					c.set(year,mon,date);
					
					long getTick = c.getTimeInMillis();
					
					//minDate = (minDate >= getTick) ? getTick : minDate;//최소값을 불러준다.
					
					if (minDate >= getTick) {//반납일자가 더 가까운 날을 받아온다.
						minDate = getTick;//최소값 집어넣기
						rc = c;//캘린더 객체 넣어주기
					}
					
				}//while()
				
				
				//최소일 기준으로 +1일을 하여 예약 해줘야한다.
				rc.add(Calendar.DATE, 1);//최소 날짜에 1을 더해준다.
				
				String getYear = "" + rc.get(Calendar.YEAR);//최소년도
				String getMon = format("" + (rc.get(Calendar.MONTH)+1));//최소 월 다시 1을 더해줘야 우리가 원하는 월이 나오게 된다.
				String getDate = format("" + rc.get(Calendar.DATE));//최소일 
				
				
				realReservationBook(userSeq, bookInfo,getYear, getMon, getDate);//예약실행
				
				//신청되었다는 정보를 출력해줘야 할것이다.
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t예약신청이 완료되었습니다.");
				System.out.printf("\t\t\t%tF에 대여해가시면 됩니다.\n",rc);
				System.out.printf("\t\t\t반납은 %tF일 까지 입니다. 반납기간이 지연되면 연체요금이 발생합니다.\n",addCalendar(rc,7));
				MemberBookInformationPrint mbip = new MemberBookInformationPrint();
				mbip.bookPrint(bookInfo);
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				
				
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("뭔가 잘못됨");
			}
			
			
			
			
			
		} else {//예약을 대기 해야 한다. ->
			
			int bookCount = howBookCount(bookInfo);//해당 책(도서정보번호에 부합하는)의 개수가 몇권인지 파악.
			
			
			
			Calendar rc = Calendar.getInstance();
			
			
			if (bookCount == 1) {//책의 개수가 한권일때
				
				long maxDate = Long.MIN_VALUE;//틱값을 받아줄 놈을 대기시킨다. -> 가장 낮은값으로 넣어준다.
				
				
				for (int i = 0; i < dataList.size(); i++) {
					Calendar c = Calendar.getInstance();
					
					StringTokenizer stk = new StringTokenizer(dataList.get(i)[1],"-");//날짜를 받아오고 그것을 년월일로 쪼갤것이다.
					int year = Integer.parseInt(stk.nextToken());//년
					int mon = Integer.parseInt(stk.nextToken())-1;//월
					int date = Integer.parseInt(stk.nextToken().trim());//일-> 문제가 발생
					
					c.set(year,mon,date);
					
					long getTick = c.getTimeInMillis();
					
					if (getTick >= maxDate) {//예약일자가 더 먼~ 날을 받아온다.
						maxDate = getTick;//최소값 집어넣기.
						rc = c;//캘린더 객체 넣어주기.
					}
					
				}//for
				
				
				
				
			} else {//책의 개수가 1권이 아닐때
				
				//책의 개수가 한권이 아닐땐, 예약대기를 봐줘야 한다 홀수인 경우와 짝수인 경우에 따라 나눠서 생각해주는게 좋다.
				
				int dataLen = dataList.size();//예약정보를 담아주는 리스트의 길이 -> 몇명이 예약을 했는지 보여준다. 
				
				if (dataLen == 1) {// 예약 내역이 단 한건만 있는 경우에 -> 두건이 이미 대여가 되었으므로 두건의 대여중 가장 먼날짜에 반납되는것 + 1 예약을 수행

					Connection conn = null;
					CallableStatement stat = null;
					ResultSet rs = null;
					DBUtil util = new DBUtil();

					try {

						// 여기서 시간을 고려해서 더 느린 시간 + 1일 로 예약을 잡아주면 된다. 그리고 나서 예약을 진행해주면 된다.

						String sql = "{call procRentPossible(?,?)}";// 프로시저 불러주기

						conn = util.open("localhost", "lms", "java1234");
						stat = conn.prepareCall(sql);

						stat.setInt(1, bookInfo);// 도서정보번호 넣어주기
						stat.registerOutParameter(2, OracleTypes.CURSOR);// 커서 넣어주기

						stat.executeQuery();

						rs = (ResultSet) stat.getObject(2);

						long maxDate = Long.MIN_VALUE;// 틱값을 받아줄 놈을 대기시킨다. -> 가장 낮은값으로 진행.

						while (rs.next()) {
							Calendar c = Calendar.getInstance();

							StringTokenizer stk = new StringTokenizer(rs.getString(1).substring(0, 11), "-");// 반납일 가져오기
							int year = Integer.parseInt(stk.nextToken());// 년
							int mon = Integer.parseInt(stk.nextToken()) - 1;// 월 -> 1을 빼줘야함
							int date = Integer.parseInt(stk.nextToken().trim());// 일

							c.set(year, mon, date);

							long getTick = c.getTimeInMillis();

							if (getTick >= maxDate) {// 반납일자가 더 먼 날을 받아온다.
								maxDate = getTick;// 최대값 집어넣기
								rc = c;// 캘린더 객체 넣어주기
							}

						} // while()

					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("뭔가 잘못됨");
					}
					// 예약내역이 1건만 있는 경우에 처리
				} else {//예약 내역이 2건 이상 있는 경우에 처리
					
					Calendar c = Calendar.getInstance();
					
					String totalDate = dataList.get(dataLen-2)[1];//예약날짜 받아옴
					StringTokenizer stk = new StringTokenizer(totalDate,"-");//예약일 가져오기
					
					int year = Integer.parseInt(stk.nextToken());// 년
					int mon = Integer.parseInt(stk.nextToken()) - 1;// 월 -> 1을 빼줘야함
					int date = Integer.parseInt(stk.nextToken().trim());// 일
					
					c.set(year, mon, date);
					c.add(Calendar.DATE, 7);//예약일 +7일까지가 대여기간이므로
					
					rc = c;
					
				}
				
				
				
				
				
				
			}//else
			
			
			//여기가 예약쪽인거 같은데...많이 겹칠거 같은데 아닌가?
			//최소일 기준으로 +1일을 하여 예약 해줘야한다.
			rc.add(Calendar.DATE, 1);//최소 날짜에 1을 더해준다.
			
			String getYear = "" + rc.get(Calendar.YEAR);//최소년도
			String getMon = format("" + (rc.get(Calendar.MONTH)+1));//최소 월 다시 1을 더해줘야 우리가 원하는 월이 나오게 된다.
			String getDate = format("" + rc.get(Calendar.DATE));//최소일 
			
			
			realReservationBook(userSeq, bookInfo,getYear, getMon, getDate);//예약실행
			
			//신청되었다는 정보를 출력해줘야 할것이다.
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t예약신청이 완료되었습니다.");
			System.out.printf("\t\t\t%tF에 대여해가시면 됩니다.\n",rc);
			System.out.printf("\t\t\t반납은 %tF일 까지 입니다.\n",addCalendar(rc,7));//7일 뒤에는 가져와라 이말
			MemberBookInformationPrint mbip = new MemberBookInformationPrint();
			mbip.bookPrint(bookInfo);
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
			
		
			
			
			
		}//else

	}//resPossible()
	
	/**
	 * tblReservation에 insert 해주는 메서드
	 * @param userSeq  유저번호
	 * @param bookInfo 도서정보번호
	 * @param year 년
	 * @param mon 월
	 * @param date 일
	 */
	public void realReservationBook(int userSeq, int bookInfo, String year, String mon, String date) {
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		try {
			
			String sql = "{call procReserveBook(?,?,?,?,?)}";//예약을 담당해주는 프로시저
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, userSeq);//회원번호 넣어주기
			stat.setInt(2, bookInfo);//도서정보번호 넣어주기
			stat.setString(3, year);//년도 넣어주기
			stat.setString(4, mon);//월 넣어주기
			stat.setString(5, date);//일 넣어주기
			
			int a = stat.executeUpdate();
			
			//System.out.println(a + "개 완료");
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("오류발생");
		}
	}
	
	/**
	 * 어떠한 도서 정보번호가 존재할때, 해당 도서정보번호에 해당하는 책의 개수가 몇권인지 확인하는 메서드
	 * @param bookSeq 도서정보번호
	 * @return 해당 도서번호에 해당하는 도서의 개수
	 */
	public int howBookCount(int bookSeq) {
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		int bookCount = 0;//책의 개수
		
		try {
			
			String sql = "{call procBookCount(?,?)}";//예약을 담당해주는 프로시저
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, bookSeq);//도서번호 넣어주기
			stat.registerOutParameter(2, OracleTypes.CURSOR);//커서 넣어주기
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			while(rs.next()) {
				bookCount = rs.getInt(1);
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("오류발생");
		}
		return bookCount;
		
	}// howBookCount()
	
	
	/**
	 * 오라클에 넣을때 월이랑 일을 두자리 형식으로 맞춰서 넣어야 하기때문에 형식을 맞춰주는 메서드
	 * @param input 월이나 일
	 * @return 월이나 일을 두자리로 만들어준다
	 */
	public String format(String input) {
		
		String result = input;
		
		if (input.length() == 1) {
			result = "0" + input;
		}
		
		return result;
		 
	}//format()
	
	
	
	/**
	 * 날짜를 계산해주는 메서드
	 * @param c 날짜 객체
	 * @param days 몇일을 더할건지
	 * @return
	 */
	public Calendar addCalendar(Calendar c , int days) {
		
		Calendar newC = c;
		newC.add(Calendar.DATE, days);
		
		return newC;
		
		
	}//addCalendar()
	
	
	

}
