package admin;
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
							  ,rs.getNString("답변")==null?"답변이 아직 등록되지 않았습니다.":rs.getString("답변").replace(".", ".\r\n\t\t\t")
							  ,rs.getNString("전화번호")};
				row.add(str);
			}
			
			
			stat.close();
			conn.close();
			return row;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("\t\t\t데이터를 가져오는데 실패했습니다.");
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
							  ,rs.getNString("답변")==null?"답변이 아직 등록되지 않았습니다.":rs.getString("답변").replace(".", ".\r\n\t\t\t")
							  ,rs.getNString("전화번호")};
				row.add(str);
			}
			
			
			stat.close();
			conn.close();
			
			return row;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("\t\t\t데이터를 가져오는데 실패했습니다.");
		}
		
		return row;
	}//procgetSuggestions(int member_seq)
	
	/**
	 * 글번호를 받아서 건의사항 내용 확인합니다.
	 * @param psug_seq 글번호
	 * @return ArrayList<String[]> [0]글번호 [1]제목 [2]이름 [3]날짜 [4]건의사항 [5]답변 [6]전화번호
	 */
	public ArrayList<String[]> procGetSuggestionsInfo(){//3. 글번호를 받아서 건의사항 내용 확인하기
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		ArrayList<String[]> row = new ArrayList<String[]>();
		while (true) {
			int psug_seq = seqCheck();// 글번호 입력받기
			ArrayList<String> allSeq = hasSeq(procGetSuggestions(true));
			if (allSeq.contains(psug_seq+"")) {
				try {
					String sql = "{call procgetsuggestionsbysug_seq(?,?)}";
					conn = util.open();
					stat = conn.prepareCall(sql);
					stat.registerOutParameter(1, OracleTypes.CURSOR);
					stat.setInt(2, (psug_seq));
					stat.executeQuery();

					rs = (ResultSet) stat.getObject(1);
					while (rs.next()) {
						
						String[] str = { rs.getString("글번호")
								, rs.getString("제목")
								, rs.getString("이름")
								, rs.getString("날짜")
								, rs.getString("건의사항").replace(".", ".\r\n\t\t\t")
								, rs.getString("답변")==null?"답변이 아직 등록되지 않았습니다.":rs.getString("답변").replace(".", ".\r\n\t\t\t")
								, rs.getString("전화번호") };
						row.add(str);
					}

					stat.close();
					conn.close();
					return row;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					System.out.println("\t\t\t데이터를 가져오는데 실패했습니다.");
				}

				return row;
			} else {
				System.out.println("\t\t\t없는 글 번호입니다");
				
			}
		}
	}//procGetSuggestionsInfo(int psug_seq)
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
							  ,rs.getNString("답변")==null?"답변이 아직 등록되지 않았습니다.":rs.getString("답변").replace(".", ".\r\n\t\t\t")
							  ,rs.getNString("전화번호")};
				row.add(str);
			}
			
			
			stat.close();
			conn.close();
			return row;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("\t\t\t데이터를 가져오는데 실패했습니다.");
		}
		
		return row;
	}//procgetSuggestions(String keyWord)
	/**
	 * 새로운 건의사항을 작성합니다.
	 * @param member_seq
	 * @return true 성공, false 실패
	 */
	public boolean procSetSuggestion(int member_seq)  {//5.건의사항 작성하기
		
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		
		int pmember_seq=member_seq;
		
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
			System.out.print("\t\t\t제목 입력: ");
			ptitle=scan.nextLine();
			
			if(ptitle.getBytes().length<100) {
				break;
			}else{
				System.out.println("\t\t\t제목 길이 초과입니다.");
				System.out.printf("\t\t\t제한 : 100byte, 입력 : %dbyte\n",ptitle.getBytes().length);
			}
			
		}//while 제목작성 및 유효성 검사
		return ptitle;
	}//checkTitle

	private String checkContent(int max) {
		String pcontents="";

		while(true) {//내용 유효성 검사 max byte 이하
			System.out.print("\t\t\t내용 입력: ('q'를 입력하면 입력을 마칩니다.)\n");
			boolean flag=true;
			
			while(flag) {//반복문을 통해 Enter를 입력해도 작성을 중지하지 않는다. 'q'를 입력하면 작성 완료
				System.out.print("\t\t\t▶");
				String str=scan.nextLine();
				if(str.toLowerCase().equals("q")) {
					System.out.println("\t\t\t작성을 완료했습니다.");
					flag=false;
				}else {
					pcontents+=str+" \\r\\n";
				}
				
			}//while 건의사항 작성
			
			if(pcontents.getBytes().length<max) {
				break;
			}else{
				System.out.println("\t\t\t내용 길이 초과입니다.");
				System.out.printf("\t\t\t제한 : %dbyte, 입력 : %dbyte\n",max,pcontents.getBytes().length);
			}//제목 유효성 검사
		}//while 건의사항 작성 및 유효성 검사
		return pcontents;
	}//checkContent
	/**
	 * 게시된 글을 삭제합니다.
	 * @return 삭제시 true, 실패시 false
	 */
	public boolean procSetDelSuggestion(int member_seq) {//6. 게시글 삭제하기 , 글 번호도 입력받아야되네...
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ArrayList<String[]> row=procGetMySuggestions(member_seq);
		ArrayList<String> allSeq=new ArrayList<String>();
		int i=0;
		while(i<row.size()) {
			allSeq.add(row.get(i)[0]);
			i++;
		}
		int seq=-1;
		while(true) {
			seq=seqCheck();//글번호
			if(allSeq.contains(seq+"")) {
				break;
			}else {
				System.out.println("\t\t\t회원님이 작성한 글번호가 아닙니다.");
			}
		}
		try {
			String sql = "{call procsetdelSuggestion(?)}";
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.setInt(1, seq);
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
	public boolean procSetDelSuggestion() {//6-1. 게시글 삭제하기(관리자)
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ArrayList<String[]> row=procGetSuggestions(true);//모든 글 중에서 삭제되지 않은 글
		ArrayList<String> allSeq=new ArrayList<String>();//삭제되지 않은 글번호
		int i=0;
		while(i<row.size()) {
			allSeq.add(row.get(i)[0]);
			i++;
		}
		int seq=-1;
		while(true) {
			seq=seqCheck();//글번호
			if(allSeq.contains(seq+"")) {
				break;
			}else {
				System.out.println("\t\t\t존재하지 않는 글번호 입니다.");
			}
		}
		try {
			String sql = "{call procsetdelSuggestion(?)}";
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.setInt(1, seq);
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
	 * 게시글 수정하기(회원)
	 * 
	 * @param member_seq 회원번호
	 * @return true 성공, false 실패
	 */
	public boolean procSetSuggestionUpdate(int member_seq) {// 7. 게시글 수정하기(회원)
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		
		
		ArrayList<String[]> row=procGetMySuggestions(member_seq); //자기가 쓴 글만 가져옴
		ArrayList<String> mySuggestion=new ArrayList<String>();//자기가 쓴 글 번호를 저장
		int i=0;
		while(i<row.size()) {
			mySuggestion.add(row.get(i)[0]);
			i++;
		}
		while(true) {
			int pseq = seqCheck();// 자기가 쓴것만 지워야하는데...

			if (mySuggestion.contains(pseq + "")) {

				String ptitle = "";
				String pcontents = "";
				ptitle = checkTitle(); // 제목 유효성 검사 메소드
				pcontents = checkContent(4000);// 내용 유효성 검사 메소드

				try {
					String sql = "{call procsetSuggestionupdate(?,?,?)}";
					conn = util.open();
					stat = conn.prepareCall(sql);
					stat.setInt(1, (pseq));
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
			} else {
				System.out.print("\t\t\t회원님이 작성하신 글이 아닙니다.\n");
				continue;
			}
		}
	}//procsetSuggestionupdate
	/**
	 * 게시글 수정하기(관리자)
	 * @return true 성공, false 실패
	 */
	public boolean procSetSuggestionUpdate() {// 7-1. 게시글 수정하기(관리자)
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		
		
		ArrayList<String[]> row=procGetSuggestions(true); //삭제안된 모든 글
		ArrayList<String> allSeq=new ArrayList<String>();//모든 글 번호를 저장
		int i=0;
		while(i<row.size()) {
			allSeq.add(row.get(i)[0]);
			i++;
		}
		while(true) {
			int pseq = seqCheck();

			if (allSeq.contains(pseq + "")) {

				String ptitle = "";
				String pcontents = "";
				ptitle = checkTitle(); // 제목 유효성 검사 메소드
				pcontents = checkContent(4000);// 내용 유효성 검사 메소드

				try {
					String sql = "{call procsetSuggestionupdate(?,?,?)}";
					conn = util.open();
					stat = conn.prepareCall(sql);
					stat.setInt(1, (pseq));
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
			} else {
				System.out.print("\t\t\t존재하지 않는 글번호 입니다.\n");
				continue;
			}
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
		
		ArrayList<String> allSeq = hasSeq(procGetSuggestions(false));//seq 번호를 가지고 있는지 확인하고 싶다.
		
		int pseq = seqCheck();
		
		
		boolean flag=true;
		while(flag) {
			
			if(allSeq.contains(pseq+"")) {
				flag=false;
			}else {
				System.out.println("\t\t\t이미 답변이 작성되었거나 없는 글 번호입니다.");
				System.out.println("\t\t\t다시 입력 : ");
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

	private ArrayList<String> hasSeq(ArrayList<String[]> collection) {//글번호를 확인할 컬렉션 입력받음
		ArrayList<String> allSeq=new ArrayList<String>(); //  글번호를 넣을 컬렉션
		int i=0;
		
		ArrayList<String[]> row=collection;
		
		while(i<row.size()) {// 글번호를 모두 가져옴
			allSeq.add(row.get(i)[0]);
			i++;
		}//while
		return allSeq;
	}

	/**
	 *	회원번호를 입력받아 자신이 쓴 글들의 정보를 가져옵니다.
	 * @param member_seq 회원번호
	 * @return  ArrayList<String[]> [0]글번호 [1]제목 [2]이름 [3]날짜 [4]건의사항 [5]답변 [6]전화번호
	 */
	public ArrayList<String[]> procGetMySuggestions(int member_seq){//10.회원번호를 입력받아 글 가져오기
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		ArrayList<String[]> row=new ArrayList<String[]>();
		try {
			String sql = "{call procgetsuggestionsbymem_seq(?,?)}";
			conn = util.open();
			stat = conn.prepareCall(sql);
			stat.registerOutParameter(1, OracleTypes.CURSOR);
			stat.setInt(2, member_seq);
			
			stat.executeQuery();
			rs=(ResultSet)stat.getObject(1);
			while(rs.next()) {
				String[] str= {rs.getString("글번호")
							  ,rs.getNString("제목")
							  ,rs.getNString("이름")
							  ,rs.getNString("날짜")
							  ,rs.getNString("건의사항")
							  ,rs.getNString("답변")==null?"답변이 아직 등록되지 않았습니다.":rs.getString("답변").replace(".", ".\r\n\t\t\t")
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
			return row;
		}
	}//procgetmysuggestions
	
	private int seqCheck() {//글번호 유효성 검사 메소드
		System.out.print("\t\t\t글번호 입력: ");
		String pseq="";
		while(true) {
			pseq=scan.nextLine();
			if(isNumber(pseq)) {
				break;
			}else {
				System.out.println("\t\t\t숫자가 아닙니다.");
				System.out.print("\t\t\t재입력 :");
			}
		}
		return Integer.parseInt(pseq);
	}//seqCheck

	
	private static boolean isNumber(String str) {//문자열에 숫자만 있는지 확인하는 메소드
		return str.matches("^[0-9]*$");
	}//isNumber
}


