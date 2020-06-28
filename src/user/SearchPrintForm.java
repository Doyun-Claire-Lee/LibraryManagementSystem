package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import admin.DBUtil;
import oracle.jdbc.internal.OracleTypes;

/**
 * 
 * @author shin
 *	해당 클래스는 회원들에게 중복되지 않는 도서를 보여주기 위해 화면단을 꾸미는 클래스이다.
 */
public class SearchPrintForm {
	
	/**
	 * 
	 * @param author 저자이름
	 * @param bookList 책 제목의 중복을 허용한 도서 데이터 리스트
	 * @return 중복되지 않는 책 정보 의 성질을 띄는 리스트
	 */
	
	//여기서도 저자,책제목,출판사별로 받을 수 있도록 해야한다.
	public List<String[]> searchAuthor(String author,List<String[]> bookList) {
		
		//List<String[]> totalList = bookList;//저자명에 부합하는 책정보 리스트 -> 값복사를 굳이 해주지 않는다.(삭제대기)
		List<String[]> resultList = new ArrayList<String[]>();//중복되지 않는 제목정보를 돌려준다.
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		
		 
		try {
			
			String sql = "call procWriterDuplicationDelete(?,?)";//중복되지 않는 책 목록 보여주기 -> 회원에게 보여줄때 용도  저자로 검색용
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			stat.setString(1, author);//저자 이름 넣어주기
			stat.registerOutParameter(2, OracleTypes.CURSOR);//커서 넣어주기
			
			stat.executeQuery();
			rs = (ResultSet)stat.getObject(2);
			
			
			while(rs.next()) {
				
				String[] inputData = new String[6];//리스트에 넣어줄 책에 관한 데이터
				
				boolean flag = false;//모두다 빌릴수 없는 책이라고 가정하고 시작한다
				
				for (int i = 0; i < bookList.size(); i++) {
					if (rs.getString(5).equals(bookList.get(i)[7])) {
						if (bookList.get(i)[9].equals("대여가능")) {
							flag = true;// 대여가능한 책이 하나라도 나오면 flag에 true를 걸어서 대여가능한 책으로 만든다.
						}
					}
				}//for()
				
				inputData[0] = rs.getString(1);//책 이름
				inputData[1] = rs.getString(2);;//저자명
				inputData[2] = rs.getString(3);//출판사 이름
				inputData[3] = rs.getString(6);//도서위치
				inputData[4] = rs.getString(5);//도서정보번호
				
				if (flag) {//빌릴 수 있는 책일 떄
					inputData[5] = "대여가능";
					
				} else {//빌릴 수 없는 책일 때
					inputData[5] = "미반납";
				}
				
				resultList.add(inputData);//정보리스트를 넣어주기
				
			}//while()
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("오류발생");
		}
		
		return resultList;//책 정보 반환값.(중복되는 책 이름 안보임.)
	}//searchAuthor()
	
	
	/**
	 * 
	 * @param title 책 제목
	 * @param bookList 책 제목의 중복을 허용한 도서 데이터 리스트
	 * @return 중복되지 않는 책 정보 의 성질을 띄는 리스트
	 */
	public List<String[]> searchSubject(String title,List<String[]> bookList) {
		
		//List<String[]> totalList = bookList;//저자명에 부합하는 책정보 리스트 -> 값복사를 굳이 해주지 않는다.(삭제대기)
		List<String[]> resultList = new ArrayList<String[]>();//중복되지 않는 제목정보를 돌려준다.
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		
		 
		try {
			
			String sql = "call procSubjectDuplicationDelete(?,?)";//중복되지 않는 책 목록 보여주기 -> 회원에게 보여줄때 용도 제목별로 검색용
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			stat.setString(1, title);//책 제목 넣어주기
			stat.registerOutParameter(2, OracleTypes.CURSOR);//커서 넣어주기
			
			stat.executeQuery();
			rs = (ResultSet)stat.getObject(2);
			
			
			while(rs.next()) {
				
				String[] inputData = new String[6];//리스트에 넣어줄 책에 관한 데이터
				
				boolean flag = false;//모두다 빌릴수 없는 책이라고 가정하고 시작한다
				
				for (int i = 0; i < bookList.size(); i++) {
					if (rs.getString(5).equals(bookList.get(i)[7])) {
						if (bookList.get(i)[9].equals("대여가능")) {
							flag = true;// 대여가능한 책이 하나라도 나오면 flag에 true를 걸어서 대여가능한 책으로 만든다.
						}
					}
				}//for()
				
				inputData[0] = rs.getString(1);//책 이름
				inputData[1] = rs.getString(2);//저자
				inputData[2] = rs.getString(3);//출판사 이름
				inputData[3] = rs.getString(6);//도서위치
				inputData[4] = rs.getString(5);//도서정보번호
				
				if (flag) {//빌릴 수 있는 책일 떄
					inputData[5] = "대여가능";
					
				} else {//빌릴 수 없는 책일 때
					inputData[5] = "미반납";
				}
				
				resultList.add(inputData);//정보리스트를 넣어주기
				
			}//while()
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("오류발생");
		}
		
		return resultList;//책 정보 반환값.(중복되는 책 이름 안보임.)
	}//searchSubject()
	
	/**
	 * 
	 * @param publisher
	 * @param bookList
	 * @return
	 */
	public List<String[]> searchPublish(String publisher,List<String[]> bookList) {
		
		//List<String[]> totalList = bookList;//저자명에 부합하는 책정보 리스트 -> 값복사를 굳이 해주지 않는다.(삭제대기)
		List<String[]> resultList = new ArrayList<String[]>();//중복되지 않는 제목정보를 돌려준다.
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		
		 
		try {
			
			String sql = "call procPublisherDuplicationDelete(?,?)";//중복되지 않는 책 목록 보여주기 -> 회원에게 보여줄때 용도 출판사별 검색용
			
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.prepareCall(sql);
			
			stat.setString(1, publisher);//출판사 이름 넣어주기
			stat.registerOutParameter(2, OracleTypes.CURSOR);//커서 넣어주기
			
			stat.executeQuery();
			rs = (ResultSet)stat.getObject(2);
			
			
			while(rs.next()) {
				
				String[] inputData = new String[6];//리스트에 넣어줄 책에 관한 데이터
				
				boolean flag = false;//모두다 빌릴수 없는 책이라고 가정하고 시작한다
				
				for (int i = 0; i < bookList.size(); i++) {
					if (rs.getString(5).equals(bookList.get(i)[7])) {
						if (bookList.get(i)[9].equals("대여가능")) {
							flag = true;// 대여가능한 책이 하나라도 나오면 flag에 true를 걸어서 대여가능한 책으로 만든다.
						}
					}
				}//for()
				
				inputData[0] = rs.getString(1);//책 이름
				inputData[1] = rs.getString(2);//저자
				inputData[2] = rs.getString(3);//출판사 이름
				inputData[3] = rs.getString(6);//도서위치
				inputData[4] = rs.getString(5);//도서정보번호
				
				if (flag) {//빌릴 수 있는 책일 떄
					inputData[5] = "대여가능";
					
				} else {//빌릴 수 없는 책일 때
					inputData[5] = "미반납";
				}
				
				resultList.add(inputData);//정보리스트를 넣어주기
				
			}//while()
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("오류발생");
		}
		
		return resultList;//책 정보 반환값.(중복되는 책 이름 안보임.)
	}//searchSubject()
	

	
	
	

}
