package jschoi;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

public class Method {
	//update, delete method return Boolean(if work success then true nor false)
	//select method return ArrayList<String[]>(about multiple row) or String[](about single row)
	//sql 문 옆의 숫자는 Suggestionsprocedure.txt에서 procedure 번호
	static Scanner scan=new Scanner(System.in);
	
	/**
	 * 건의사항 정보를 최근 날짜순으로 가져옵니다. true 건의사항 모든 정보, false 미답변 건의사항
	 * @param boolean true일 경우 건의사항 모든 정보, false일 경우 미답변 상태의 건의사항 정보를 가져옵니다.
	 * @return ArrayList<String[]> [0]글번호 [1]제목 [2]이름 [3]날짜 [4]건의사항 [5]답변 [6]전화번호
	 */
	
	public ArrayList<String[]> procGetSuggestions(boolean flag){
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		ArrayList<String[]> row=new ArrayList<String[]>();
		try {
			String sql="";
			if(flag) {
				sql = "{call procgetSuggestions(?)}";//1. 건의사항 정보 가져오기
			}else {
				sql = "{call procgetSuggestionsnoanswer(?)}";//3.답변이 안된 건의사항 조회하기
			}
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.registerOutParameter(1, OracleTypes.CURSOR);
			stat.executeQuery();
			
			rs=(ResultSet)stat.getObject(1);
			while(rs.next()) {
				String[] str= {rs.getString("글번호")
							  ,rs.getNString("제목")
							  ,rs.getNString("이름")
							  ,rs.getNString("날짜")
							  ,rs.getNString("건의사항")
							  ,rs.getNString("답변")
							  ,rs.getNString("전화번호")};
				row.add(str);
			}
			
			
			stat.close();
			conn.close();
			return row;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("데이터를 가져오는데 실패했습니다.");
		}
		
		return row;
	}//procgetSuggestions()
	
	/**
	 * 회원이 작성한 건의사항을 최근 순서로 가져옵니다.
	 * @param pmember_seq 회원번호
	 * @return ArrayList<String[]> [0]글번호 [1]제목 [2]이름 [3]날짜 [4]건의사항 [5]답변 [6]전화번호
	 */
	public ArrayList<String[]> procGetSuggestions(int pmember_seq){
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		ArrayList<String[]> row=new ArrayList<String[]>();
		try {
			String sql = "{call procgetSuggestionsbymem_seq(?,?)}";//2. 회원번호를 받아서(로그인 정보를 통해) 자신의 건의사항 확인하기
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.registerOutParameter(1, OracleTypes.CURSOR);
			stat.setInt(2, pmember_seq);
			stat.executeQuery();
			
			rs=(ResultSet)stat.getObject(1);
			while(rs.next()) {
				String[] str= {rs.getString("글번호")
							  ,rs.getNString("제목")
							  ,rs.getNString("이름")
							  ,rs.getNString("날짜")
							  ,rs.getNString("건의사항")
							  ,rs.getNString("답변")
							  ,rs.getNString("전화번호")};
				row.add(str);
			}
			
			
			stat.close();
			conn.close();
			
			return row;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("데이터를 가져오는데 실패했습니다.");
		}
		
		return row;
	}//procgetSuggestions(int member_seq)
	
	/**
	 * 키워드를 입력받아 제목과 건의사항 내용에서 일치하는 키워드를 가진 정보를 돌려줍니다.
	 * @param keyWord 제목과 건의사항 내용에서 검색하고자 하는 키워드입니다.
	 * @return ArrayList<String[]> [0]글번호 [1]제목 [2]이름 [3]날짜 [4]건의사항 [5]답변 [6]전화번호
	 */
	public ArrayList<String[]> procGetSuggestions(String keyWord){
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		ArrayList<String[]> row=new ArrayList<String[]>();
		try {
			String sql = "{call procgetSuggestionsbykeyword(?,?)}";//4. 키워드를 입력받아 제목과 건의사항 내용 검색하기
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.registerOutParameter(1, OracleTypes.CURSOR);
			stat.setString(2, keyWord);
			stat.executeQuery();
			
			rs=(ResultSet)stat.getObject(1);
			while(rs.next()) {
				String[] str= {rs.getString("글번호")
							  ,rs.getNString("제목")
							  ,rs.getNString("이름")
							  ,rs.getNString("날짜")
							  ,rs.getNString("건의사항")
							  ,rs.getNString("답변")
							  ,rs.getNString("전화번호")};
				row.add(str);
			}
			
			
			stat.close();
			conn.close();
			return row;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("데이터를 가져오는데 실패했습니다.");
		}
		
		return row;
	}//procgetSuggestions(String keyWord)
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean procSetSuggestion()  {
		
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		System.out.print("회원번호");//삭제
		int pmember_seq=scan.nextInt();//로그인 정보에서 회원번호를 가져온다.
		scan.nextLine();
		
		String ptitle="";
		String pcontents="";
		ptitle = checkTitle(); // 제목 유효성 검사 메소드
		pcontents = checkContent(4000);// 내용 유효성 검사 메소드 최대 byte수 입력
		
		try {
			
			String sql = "{call procsetSuggestion(?,?,?)}";//5.건의사항 작성하기
			conn = util.open();

			stat = conn.prepareCall(sql);

			stat.setInt(1, pmember_seq);
			stat.setString(2, ptitle);
			stat.setString(3, pcontents);
			stat.executeQuery();

			
			stat.close();
			conn.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}//procsetSuggestion

	private String checkTitle()  {
		String ptitle="";
		while(true) {//제목 유효성 검사 100byte 이하
			System.out.print("제목 입력: ");
			ptitle=scan.nextLine();
			
			if(ptitle.getBytes().length<100) {
				break;
			}else{
				System.out.println("제목 길이 초과입니다.");
				System.out.printf("제한 : 100byte, 입력 : %dbyte\n",ptitle.getBytes().length);
			}
			
		}//while 제목작성 및 유효성 검사
		return ptitle;
	}//checkTitle

	private String checkContent(int max) {
		String pcontents="";

		while(true) {//내용 유효성 검사 max byte 이하
			System.out.print("내용 입력: ");
			boolean flag=true;
			
			while(flag) {//반복문을 통해 Enter를 입력해도 작성을 중지하지 않는다. 'q'를 입력하면 작성 완료
				String str=scan.nextLine();
				if(str.toLowerCase().equals("q")) {
					System.out.println("작성을 완료했습니다.");
					flag=false;
				}else {
					pcontents+=str+" \r\n";
				}
			}//while 건의사항 작성
			
			if(pcontents.getBytes().length<max) {
				break;
			}else{
				System.out.println("제목 길이 초과입니다.");
				System.out.printf("제한 : %dbyte, 입력 : %dbyte\n",max,pcontents.getBytes().length);
			}//제목 유효성 검사
		}//while 건의사항 작성 및 유효성 검사
		return pcontents;
	}//checkContent
	/**
	 * 게시된 글을 삭제합니다.
	 * @return 삭제시 true, 실패시 false
	 */
	public boolean procSetDelSuggestion() {//6. 게시글 삭제하기
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		
		
		int member_seq=scan.nextInt();//삭제
		scan.nextLine();
		
		try {
			String sql = "{call procsetdelSuggestion(?)}";
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.setInt(1, member_seq);
			stat.executeQuery();

			stat.close();
			conn.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}//procsetdelSuggestion()
	/**
	 * 게시글 수정하기
	 * @return true 성공, false 실패
	 */
	public boolean procSetSuggestionUpdate() {// 7. 게시글 수정하기(회원) null값은 '0'으로
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		
		
		System.out.print("글번호 입력: ");//자기가 쓴것만 지워야하는데...
		int pseq=scan.nextInt();
		scan.nextLine();
		String ptitle="";
		String pcontents="";
		ptitle = checkTitle(); // 제목 유효성 검사 메소드
		pcontents = checkContent(4000);// 내용 유효성 검사 메소드
		
		
		try {
			String sql = "{call procsetSuggestionupdate(?,?,?)}";
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.setInt(1, pseq);
			stat.setString(2, ptitle);
			stat.setString(3, pcontents);
			stat.executeQuery();

			stat.close();
			conn.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}//procsetSuggestionupdate
	/**
	 * 건의사항에 답변을 작성합니다.
	 * @return true 답변 완료, false 답변 실패
	 */
	public boolean procsetSuggestionAnswer() {//8.(3과 연계) 건의사항에 답변 작성하기
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		
		ArrayList<String> allSeq=new ArrayList<String>(); // 미답변 글번호를 넣을 컬렉션
		int i=0;
		ArrayList<String[]> row=procGetSuggestions(false);
		
		while(i<row.size()) {//미답변 글번호를 모두 가져옴
			allSeq.add(row.get(i)[0]);
			i++;
		}//while
		
		System.out.print("글번호 입력: ");
		int pseq=-1;
		boolean flag=true;
		while(flag) {
			pseq=scan.nextInt();
			scan.nextLine();
			if(allSeq.contains(pseq+"")) {
				flag=false;
			}else {
				System.out.println("이미 답변이 작성되었거나 없는 글 번호입니다.");
				System.out.println("다시 입력 : ");
			}
			
		}//글번호 유효성 검사
		String panswer=checkContent(1000);//답변은 100byte
		
		
		try {
			String sql = "{call procsetSuggestionanswer(?,?)}";
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.setInt(1, pseq);
			stat.setString(2, panswer);

			stat.executeQuery();

			stat.close();
			conn.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}//procsetSuggestionAnswer()
}


