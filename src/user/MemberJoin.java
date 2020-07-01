package user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import admin.DBUtil;
import oracle.jdbc.OracleTypes;

/**
 * 회원 가입 관련 메소드를 멤버로 갖는 클래스입니다.
 * @author Doyun Lee
 *
 */
public class MemberJoin {

// 테스트용 메인메소드
//	public static void main(String[] args) {
//		MemberJoin m = new MemberJoin();
//		m.procAddMember();
//	}

	/**
	 * 회원가입을 위한 메소드입니다.
	 */
	public void procAddMember() {
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("\t\t\t\t            회원 가입");
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println();
		
		while (true) {
			
			System.out.println("\t\t\t이전 메뉴로 돌아가시려면 0을 입력하세요.");
			System.out.println();
			
			//주민번호 입력받아 올바른 주민번호인지 확인하고 올바른 경우 입력값을 리턴해주는 메소드
			String ssn = checkProperSSN();
			
			//사용자가 0을 입력했을 경우
			if (ssn.equals("0")) {
				
				//종료하고 뒤로가기
				break;
				
			//사용자가 제대로 된 주민번호를 입력했을 경우 
			} else {
				
				//연락처 입력받아 중복검사, 유효성검사
				String tel = checkProperTel();
					
				//사용자가 전화번호에 0(뒤로가기)를 입력했을 경우
				if (tel.equals("0")) {
					
					//종료하고 뒤로가기
					break;
					
				//제대로된 전화번호를 입력한 경우
				} else {
					
					//이름 입력받아 유효성검사 하기
					String name = checkProperName();
					
					//사용자가 이름에 0을 입력했을 경우
					if (name.equals("0")) {
						//뒤로가기
						break;
					
					//제대로된 이름을 리턴받았을 경우
					} else {
						
						//주소 입력받기.. 이건 유효성 검사를 어떻게 해야할지..???
						System.out.print("\t\t\t▷주소 입력: ");
						String adr = scan.nextLine();
						System.out.println();
						
						
						try {
							
							//프로시저 연결 준비
							conn = util.open("localhost", "lms", "java1234");
							String sql = "{ call procAddMember(?,?,?,?,?) }";
							stat = conn.prepareCall(sql);
							
							//변수 설정
							stat.setString(1, name);
							stat.setString(2, tel);
							stat.setString(3, adr);
							stat.setString(4, ssn);
							stat.registerOutParameter(5, OracleTypes.NUMBER);
							
							//쿼리 실행
							stat.executeQuery();
							System.out.println(stat.getInt(5));
							
							//성공(0), 실패(1) 결과값 받아오기
							if (stat.getInt(5) == 0) {
								
								//안내멘트 출력 후 메소드 종료
								System.out.println("\t\t\t회원가입이 성공적으로 완료되었습니다.");
								System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
								System.out.println();
								break;
							
							//실패한 경우
							} else {
								System.out.println("\t\t\t회원가입에 실패했습니다.");
								System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
								System.out.println();
							}

						} catch (Exception e) {
							e.printStackTrace();
						}//try-catch			
					}//if			
				}//if			
			}//if
		}//while
		
		System.out.println("\t\t\t엔터를 입력하면 이전 화면으로 돌아갑니다.");
		scan.nextLine();
	}//procAddMember
	
	/**
	 * 입력받은 주민번호가 올바른 형식인지 확인해주고 올바를 경우 그 입력값을 반환해주는 메소드입니다.
	 * @return 올바른 주민번호를 반환
	 */
	public String checkProperSSN() {
		
		Scanner scan = new Scanner(System.in);
		String ssn = "";
		
		while(true) {
			
			System.out.print("\t\t\t▷주민등록번호 입력: ");
			ssn = scan.nextLine();
			System.out.println();
			
			//0이 아닌 다른값을 입력했을 경우
			if (!ssn.equals("0")) {
				
				//주민번호가 13자인 경우에만 중복검사 실행
				if (ssn.replace("-", "").length() == 13) {
					
					//주민번호를 숫자로 올바르게 입력했는지 확인하기 위해 '-'을 제외하고 숫자로 변경해봄.
					try {
						
						Long ssnnum = Long.parseLong(ssn.replace("-", ""));
						
						//중복 검사를 하기 위해 주민번호 형식으로 다시 만들어줌(혹시몰라서)
						ssn = ssn.replace("-", "").substring(0,6) + "-" + ssn.replace("-", "").substring(6);
						int count = checkDuplicatedSSN(ssn);
						
						//중복값이 있는 경우
						if (count != 0) {
							System.out.println("\t\t\t이미 등록된 주민번호입니다.");
							System.out.println();
							
						//중복값이 없는 경우
						} else {
							System.out.println("\t\t\t등록 가능한 주민번호입니다.");
							System.out.println();
							break;
						}//if
						
	
					//에러가 나면 올바른 주민번호가 아님!
					} catch (NumberFormatException e) {
						System.out.println("\t\t\t올바른 주민번호를 입력하세요.");
						System.out.println();
					}
					
				//주민번호 길이가 안맞을 경우
				} else {
					System.out.println("\t\t\t올바른 주민번호를 입력하세요.");
					System.out.println();
				}
				
			//0(뒤로가기)을 입력했을 경우	
			} else {
				//while문 바깥으로 빠져나가 "0"을 리턴하고 메소드 끝!
				break;
			}
		}//while
		
		return ssn;
		
	}//checkProperSSN
	
	
	/**
	 * 입력받은 주민번호가 중복되는지 확인하여 중복되는 행 수를 반환하는 메소드입니다.
	 * @param ssn 사용자에게 입력받은 주민번호
	 * @return 중복되는 행 수 반환
	 */
	public int checkDuplicatedSSN(String ssn) {
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		//쿼리값(중복행 수)을 가져오기 위해 int 변수 선언
		int count = -1;

		try {
	
			//주민번호 중복 확인 쿼리 실행 준비
			String sql = String.format("select count(*) as count from tblmember where ssn = '%s'", ssn);
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.createStatement();
			
			//쿼리 실행
			rs = stat.executeQuery(sql);	
			
			while (rs.next()) {
				//변수에 count(*)값 대입
				count = rs.getInt("count");
			}//while
			

		} catch(Exception e) {
			e.printStackTrace();
		}
		//count값 리턴하고 종료!
		return count;		
	}//checkDuplicatedSSN
	
	
	/**
	 * 입력받은 전화번호가 올바른 형식인지 확인해주고 올바를 경우 그 입력값을 반환해주는 메소드입니다.
	 * @return 올바른 전화번호를 반환
	 */
	public String checkProperTel() {
		
		Scanner scan = new Scanner(System.in);
		String tel = "";
		
		while(true) {
			
			System.out.print("\t\t\t▷휴대전화번호 입력: ");
			tel = scan.nextLine();
			System.out.println();
			
			//0이 아닌 다른값을 입력했을 경우
			if (!tel.equals("0")) {
				
				//주민번호를 숫자로 올바르게 입력했는지 확인하기 위해 '-'을 제외하고 숫자로 변경해봄.
				try {
					
					Long ssnnum = Long.parseLong(tel.replace("-", ""));
					
					//전화번호가 11자리인 경우만 중복검사 실행 
					if (tel.replace("-", "").length() == 11) {
						
						//중복 검사를 하기 위해 전화번호 형식으로 다시 만들어줌(혹시몰라서)
						tel = tel.replace("-", "").substring(0,3) + "-" 
								+ tel.replace("-", "").substring(3, 7) + "-" 
								+ tel.replace("-", "").substring(7, 11);
						
						//중복검사 실행 후 중복행 수 받아옴
						int count = checkDuplicatedTel(tel);
						
						//중복값이 있는 경우
						if (count != 0) {
							System.out.println("\t\t\t이미 등록된 전화번호입니다.");
							System.out.println();
							
							//중복값이 없는 경우
						} else {
							System.out.println("\t\t\t등록 가능한 전화번호입니다.");
							System.out.println();
							break;
						}//if
					
					} else {
						System.out.println("\t\t\t올바른 전화번호를 입력하세요.");
						System.out.println();
					}
					

				//에러가 나면 올바른 전화번호가 아님!
				} catch (NumberFormatException e) {
					System.out.println("\t\t\t올바른 전화번호를 입력하세요.");
					System.out.println();
				}

			//0(뒤로가기)을 입력했을 경우	
			} else {
				//while문 바깥으로 빠져나가 "0"을 리턴하고 메소드 끝!
				break;
			}
		}//while
		
		return tel;
		
	}//checkProperTel
	
	
	/**
	 * 입력받은 전화번호가 중복되는지 확인하여 중복되는 행 수를 반환하는 메소드입니다.
	 * @param ssn 사용자에게 입력받은 전화번호
	 * @return 중복되는 행 수 반환
	 */
	public int checkDuplicatedTel(String tel) {
		
		DBUtil util = new DBUtil();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		//쿼리값(중복행 수)을 가져오기 위해 int 변수 선언
		int count = -1;

		try {
	
			//전화번호 중복 확인 쿼리 실행 준비
			String sql = String.format("select count(*) as count from tblmember where tel = '%s'", tel);
			conn = util.open("localhost", "lms", "java1234");
			stat = conn.createStatement();
			
			//쿼리 실행
			rs = stat.executeQuery(sql);	
			
			while (rs.next()) {
				//변수에 count(*)값 대입
				count = rs.getInt("count");
			}//while
			

		} catch(Exception e) {
			e.printStackTrace();
		}
		//count값 리턴하고 종료!
		return count;		
	}//checkDuplicatedTel
	
	
	/**
	 * 사용자에게 이름을 입력받아 유효성검사 후 반환하는 메소드입니다.
	 * @return 입력받은 이름
	 */
	public String checkProperName() {
		
		Scanner scan = new Scanner(System.in);
		String name = "";
		
		while (true) {
			
			//이름 입력받기
			System.out.print("\t\t\t▷이름 입력: ");
			name = scan.nextLine();
			System.out.println();
			
			//사용자가 0을 입력한 경우
			if(name.equals("0")) {
				//뒤로가기
				break;
			
			//0 이외의 문자를 입력한 경우
			} else {
				
				//글자수(2~5글자) 및 한글인지 확인
				if (name.length() >= 2 & name.length() <=5) {
					
					//한글인지 아닌지 체크해줄 변수
					int check = 0;
					
					//한글자씩 잘라서 한글인지 검사
					for (int i=0; i<name.length(); i++) {
						
						//한글자씩 자르기
						char c = name.charAt(i);
						
						//한글이 아닐 경우
						if (c < '가' || c > '힣') {
							//확인용 변수에 값을 더해줌
							check += 1;
						}
					}//for
					
					//한글이 아닌 값을 입력받았을 경우
					if (check != 0) {
						System.out.println("\t\t\t올바른 이름을 입력하세요.");
						System.out.println();
					
					
					//이름이 2~5자이고 한글일 경우
					} else {
						
						//while문 바깥으로 나가 입력받은 이름을 return
						break;
					}
				
				//이름이 1글자이거나 6글자 이상인 경우
				} else {
					System.out.println("\t\t\t올바른 이름을 입력하세요.");
					System.out.println();
				}//if
			}//if
		}//while
		
		return name;
		
	}//checkProperName
	
	
	
}
