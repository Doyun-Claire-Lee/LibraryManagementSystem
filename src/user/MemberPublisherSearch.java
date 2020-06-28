package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import admin.DBUtil;
import oracle.jdbc.internal.OracleTypes;

/**
 * 출판사별 검색
 * @author shin
 *
 */
public class MemberPublisherSearch {
	List<String[]> notReturnList;//반납하지 않은 책 정보
	List<String[]> bookList;//해당 출판사의 책 정보를 넣어주기 위함.
	List<String[]> printBookList;//중복되는 책이름을 제외한 -> 유저에게 보여줄것.
	
	public void search(MemberUser user) {
		
		MemberUser mu = user;//유저 객체 받아오기
		
		//반납안한 책에대한 정보 받아오기.-----------
		MemberNotReturnBookInfoList mnrbi = new MemberNotReturnBookInfoList();
		notReturnList = mnrbi.notReturnBook();
		//----------------------------------
		
		
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.print("\t\t\t▷출판사 이름 입력: ");
		String inputName = scan.nextLine();//출판사 이름을 입력받는다.
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		
		bookList = new ArrayList<String[]>();//해당 출판사 에 관련된 책의 정보를 넣어주기 위함. **새로운 객체 생성

		try {
			
			
			String sql = "{call procPublisherSearch(?,?)}";//출판사 이름으로 책을 찾아주는 프로시저
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			
			stat.setString(1, inputName);//출판사 이름 넣어주기
			stat.registerOutParameter(2, OracleTypes.CURSOR);//커서 넣어주기
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			
			
			while(rs.next()) {
				
				String[] bookData = new String[10];
				bookData[0] = rs.getString(2);//책 제목
				bookData[1] = rs.getString(3);//출판사
				bookData[2] = rs.getString(4);//저자
				bookData[3] = rs.getString(5);//십진분류 번호
				bookData[4] = rs.getString(6);//시리즈 번호
				bookData[5] = rs.getString(7);//삭제여부
				bookData[6] = rs.getString(8);//도서코드
				bookData[7] = rs.getString(9);//도서정보 번호
				bookData[8] = rs.getString(10);//도서위치
				
				

				
				
				/*여기서 고려해야할 가능성
				 * 1. 미반납 되어서 내가 대여를 할 수 없는경우
				 */
				
				
				
				boolean borrow = true;//해당 책이 대여중인지 아닌지 판별~~?
				
				for (int i = 0; i < notReturnList.size(); i++) {
					if (rs.getString(8).equals(notReturnList.get(i)[1])) {
						borrow = false;
					}
				}//해당 책이 대여중인지 아닌지 판별 :  대여중이면 false 
				

				
				if (!borrow) {
					bookData[9] = "미반납";
				} else {
					bookData[9] = "대여가능";
				}
				
				 
				bookList.add(bookData);//리스트에 넣어주기
				
			}//while()
			
			stat.close();
			conn.close();
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("뭔가 잘못됨");
		}
		
		
			//여기서 예약으로 빠지느냐 그냥 진짜 대여로 가느냐를 선택
		
		if (bookList.size() != 0) {//해당 도서의 정보가 존재하는 경우 -> 대여가능한지 예약으로 빠져야하는지가 다음단계
			SearchPrintForm spf = new SearchPrintForm();//중복되지 않는 도서를 보여주기 위해 화면단을 꾸미는 클래스의 객체 생성
			printBookList = spf.searchPublish(inputName, bookList);//중복되는 책이름을 제외한 -> 유저에게 보여줄것.
			
			int[] selectNum = new int[2];//대여가능번호,도서정보번호
			SelectBookOrReserve sbr = new SelectBookOrReserve();
			selectNum = sbr.distinguish(printBookList);//유저에게 제목중복을 제외한 책의 리스트를 보여주며 바로 대여가능한 책인지 예약을 해야하는 책인지 판별해 줄것.
			
			if (selectNum[0] == 1) {//대여가능 -> 여기에서도 대여신청 하거나 뒤로가거나의 선택지를 준다.
				
				MemberRentTheBook mrt = new MemberRentTheBook();
				mrt.rentBook(selectNum[1],bookList,mu);//도서정보번호를 넘긴다.
				
				
				
				
			} else if (selectNum[0] == 0){//미반납 -> 예약을 해야함 -> 여기서도 예약을 할건지 아니면 그만할건지 선택되도록 해준다.
				
				//*****
				
				MemberBookReservation mbrv = new MemberBookReservation();
				mbrv.rentOrBack(mu, selectNum[1]);//회원객체와 도서정보번호를 넘겨준다.
				
				
			}
			
			
			
			//--
		} else {//입력한 정보를 토대로 도서의 정보가 존재하지 않는 경우 -> 유효성 검사 필요.
			
			boolean whileFlag = true;
			
			while(whileFlag) {
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t해당 출판사가 존재하지 않습니다.");
				System.out.println("\t\t\t1.재 검색하기 ");
				System.out.println("\t\t\t0.뒤로가기 ");
				System.out.print("\t\t\t▷번호 선택: ");
				String input = scan.nextLine();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				
				if (input.equals("1")) {//1.재검색 하기
					search(mu);//여기서 문제가 생길수도 있으려나?
					whileFlag = false;
				} else if (input.equals("0")) {//0.뒤로가기
					whileFlag = false;
				} else {//이상한거 눌렀을때
					System.out.println("\t\t\t1또는 0번을 눌러주세요.");
				}
				
			}//while()
			
			
		}

	}//search()
}
