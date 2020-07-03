package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import admin.DBUtil;
import oracle.jdbc.internal.OracleTypes;

/**
 * 
 * @author shin
 *	회원이 책을 빌리고 그 정보를 저장하는 작업을 수행하는 클래스.
 */
public class MemberRentTheBook {
	
	/**
	 * 
	 * @param bookSeq 도서정보번호
	 * @param bookList 해당 저자의 책 정보 리스트
	 * @param user 회원정보
	 * 
	 */
	public void rentBook(int bookSeq,List<String[]> bookList,MemberUser user) {
		Scanner scan = new Scanner(System.in);
		
		
		MemberTransSeq mts = new MemberTransSeq();//회원정보를 통해서 회원번호를 받아와주려는 것.
		int userNum = mts.userSeq(user.pw, user.id);//회원고유번호
		
		List<String> notReturnList = new ArrayList<String>();//내가 아직 반납안한 책 정보번호를 담을 리스트
		
		String bookCode = "";//도서코드-> 도서정보번호가 아니다.
		String bookInfoNum = "";//도서정보번호
		String[] bookInfo = new String[3];//찾고자 하는 도서의 정보. 
		
		for (int i = 0; i < bookList.size(); i++) {
			
			if ((Integer.parseInt(bookList.get(i)[7]) == bookSeq) 
					&& (bookList.get(i)[9].equals("대여가능"))) {//해당 저자의 도서중에 도서정보 번호와 일치하는걸 찾고 대여가능한 도서코드를 받아온다.
				 bookCode = bookList.get(i)[6];
				 bookInfo[0] = bookList.get(i)[0];//도서이름
				 bookInfo[1] = bookList.get(i)[1];//출판사
				 bookInfo[2] = bookList.get(i)[2];//저자
				 bookInfoNum = bookList.get(i)[7];//도서정보번호를 받고있다.
				 break;//잘 받아왔으면 멈추면 된다. 더 돌 이유가 없다.
			}
		}//for()
		
		Connection conn = null;
		Statement stat = null;
		DBUtil util = new DBUtil();
		
		
		
		try {//이제 오라클과 연동하여 테이블에 고객 대여정보를 넣어준다.
			
			//여기서 지금 회원이 빌린 도서 내역을 보고 일치하는게 있으면 못 빌리도록 조치를 취하겠다.
			
			notReturnList = checkMyRentList(user);
			
			boolean flag = true;//flag 가 false 이면 내가 지금 빌리려고 하는 책이 내가 이미 빌려서 아직 반납을 안한 책이라고 생각하면 된다.
			
			for (int i = 0; i < notReturnList.size(); i++) {
				
				if(notReturnList.get(i).equals(bookInfoNum)) {
					flag = false;
				}	
			}//for()
			
			if (flag) {//내가 책을 대여 할 수 있는 경우
				//System.out.println(bookCode);//이거 지울것.???
				//System.out.println(userNum);
				
				String sql = String.format("insert into tblRent values (rent_seq.nextVal,%d,'%s',sysdate,sysdate+7,0)",
						userNum,
						bookCode);//회원번호와 책코드만 넘겨주면 예약 되는방식으로 하자.
				
				conn = util.open("localhost", "lms", "java1234");
				stat = conn.createStatement();

				int num = stat.executeUpdate(sql);//몇개의 행이 들어갔는지 확인용.
				
				System.out.println();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t대여가 완료되었습니다.");
				MemberBookInformationPrint mbip = new MemberBookInformationPrint();//출력용
				mbip.bookPrint(bookSeq);
				Calendar c = Calendar.getInstance();
				addCalendar(c,7);
				System.out.printf("\t\t\t%tF 까지 반납해주세요.\n",c);
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				
				
				
//				System.out.printf("%d 입력성공\n",num);
				
//				for (int i = 0; i < bookInfo.length; i++) {
//					System.out.print(bookInfo[i] + " ");
//				}
//				System.out.println();
				
				
				
				
				stat.close();
				conn.close();
				
			} else {//내가 책을 대여 할 수 없는 경우
				
				System.out.println("\t\t\t회원님 께서는 해당도서를 아직 반납 안하신 상태입니다.");
				
				
			}
			
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("뭔가 잘못됨");
		}
		
		//여기에 쉬어가는 한템포 쓰면 좋다.
		System.out.println("\t\t\t계속하시려면 엔터를 누르세요.");
		String rest = scan.nextLine();
		
	}//rentBook()
	
	
	
	/**
	 * 자신이 빌려서 아직 반납 안한 책들을 조회해주는 메서드 : 만약 자기가 a라는 책을 빌려서 아직 반납을 안한 상태인데 또 다시 a를 빌릴수 없게 하려고 
	 * @param user 회원정보
	 * @return 회원자신이 아직 반납하지 안은 책들의 정보 리스트
	 */
	public List<String> checkMyRentList(MemberUser user) {
		
		MemberTransSeq mts = new MemberTransSeq();
		int userSeq = mts.userSeq(user.pw, user.id);//회원의 번호를 가져온다.
		
		List<String> notReturnList = new ArrayList<String>();//내가 아직 반납안한 책 정보번호를 담을 리스트
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		try {
			
			String sql = "{call procUserNotRe(?,?)}";//회원이 아직 빌려가서 돌려주지 않은 책에 대한 정보 불러오는 프로시져
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			stat.setInt(1, userSeq);//유저번호 넣어주기
			stat.registerOutParameter(2, OracleTypes.CURSOR);//커서 넣어주기
			
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			while(rs.next()) {
				notReturnList.add("" + rs.getInt(8));//자신이 빌린 도서정보번호를 넣어준다.
			}
			
			stat.close();
			conn.close();

			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("뭔가 잘못됨");
		}
		
		
		return notReturnList;
		
		
	}
	
	
	
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
