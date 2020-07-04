package admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import oracle.jdbc.OracleTypes;


public class AdminBook {
	//관리자 - 도서 관리
	
	public void bookMenu(AdminUser adminUser) {
		
		while (true) {
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t            도서 관리");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 도서 조회"); 
			System.out.println("\t\t\t2. 대여 확인"); 
			System.out.println("\t\t\t3. 예약 확인"); 
			System.out.println("\t\t\t4. 반납 처리 및 확인"); 
			System.out.println("\t\t\t5. 도서 추가 및 삭제");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String num = sc.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			// 사용자에게 번호 입력받음

			// 도서 조회
			if (num.equals("1")) {
				SelectBookList list = new SelectBookList();
				list.SelectBook(adminUser);
				
			}
			// 대여 확인
			else if (num.equals("2")) {
				CheckRentalBook  rent = new CheckRentalBook();
				rent.RentalCheck(adminUser);
			}
			// 예약 확인
			else if (num.equals("3")) {
				CheckReservation reserve = new CheckReservation();
				reserve.SelectReservation(adminUser);
				
			}
			// 반납 처리 및 확인
			else if (num.equals("4")) {
				ReturnBook  ReturnBook = new ReturnBook();
				ReturnBook.RentalCheck(adminUser);
				
			}
			// 도서 추가 및 삭제
			else if (num.equals("5")) {
				UpdateBook update = new UpdateBook();
				update.UpdateBookList(adminUser);
				
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
		
		
	}//bookMenu
	
	
	
	//1. 도서조회
	public class SelectBookList {
	
		public void SelectBook(AdminUser adminUser) {
			//도서관리 1. 도서목록조회 

			Scanner scan = new Scanner(System.in);
			Connection conn = null;
			PreparedStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();

			try {
				// 사용자 입력
				System.out.println("\t\t\t조회할 도서코드를 입력하세요. ");
				System.out.print("\t\t\t▷입력: ");
				String SelectUserInput = scan.nextLine();
				// 문자열의 공백제거
				SelectUserInput = SelectUserInput.replaceAll("\\p{Z}", "");
				// open
				conn = util.open();

				// 명령문
				String sql = "select * from vwSelectBook where 도서번호  like ?";
				// connect
				stat = conn.prepareStatement(sql);
				//입력값 넣어주기
				stat.setString(1, "%" + SelectUserInput + "%");
				//불러오기
				rs = stat.executeQuery();

				System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t[번호]\t[도서코드]\t\t\t[분류]\t[위치]\t[도서명]");
				System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
				// 출력
				while (rs.next()) {

					System.out.printf("\t%s\t%s\t\t%s\t%s\t%s\n", rs.getString("seq"), rs.getString("도서번호"),
							rs.getString("분류"), rs.getString("위치"), rs.getString("도서명"));
				} // while
			
				System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				//close
				stat.close();
				conn.close();

			} catch (Exception e) {
				// TODO: handle exception
			}
		
			// 뒤로가기
			System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String backtUserInput = scan.nextLine();

			if (backtUserInput.equals("0")) {
				// 0. 뒤로가기

				AdminBook main = new AdminBook();
				main.bookMenu(adminUser);

			} else {
				System.out.println("\t\t\t다시 입력해주세요.");
			}

		}// SelectBook()
	}// SelectBookList
	
	//2. 대여 확인	
	public class CheckRentalBook {
		
			public void page(List<String> arrayList,String MemberNumInput, String BookNumInput, AdminUser adminUser) {

		    //10개씩 분할-> 페이지 메소드
				
		    List<String[]> depart = new ArrayList<String[]>();
		    int firstindex = 0;
		    int lastindex = 10;
		    int index = 0;
		    int totalcount = arrayList.size();

		    while (true) {
		       // 10개의 String 이 들어갈 묶음
		       String[] ranking = new String[10];

		       for (int j = firstindex; j < lastindex; j++) {
		          if (j >= arrayList.size()) {
		             break;
		          }
		          ranking[j - (index) * 10] = arrayList.get(j);
		          }
		       depart.add(ranking);
		       
		       firstindex = firstindex + 10;
		       lastindex = lastindex + 10;
		       index++;

		       if (firstindex >= totalcount) {
		          break;
		       }
		    
		    }
		    
		    
		    
		    //페이지 수 
		    int count = 0;
		    
		    int num = 1;
		    
		    //페이지가 한개만 있는 경우
		    if(depart.size() == 1) {
		       //첫페이지 보여주기
		       for(int i = 0; i < depart.get(0).length; i++) {
		          if( depart.get(0)[i] != null) {
		          System.out.printf("\t\t\t%d. %s\n",i+1,depart.get(0)[i]);
		          }
		       }
		        System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		       System.out.println("\t\t\t"+"넘어갈 페이지가 없습니다.");
		        System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		    }
		    else {
		    
		    //페이지가 여러개인 경우
		    while(true) {
		       

		       System.out.println();
		       for(int i = 0; i < 10; i++) {
		          System.out.printf("\t\t\t%d. ", num);
		          if( depart.get(count)[i] != null) {
		             System.out.printf("\t%s\t" , depart.get(count)[i]);
		          }
		          System.out.println();
		          num++;
		       }
		       //페이지 반복
		       System.out.println();
		       System.out.println();
		        System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		       System.out.printf("\t\t\t"+"%d페이지 입니다.\n", count + 1);
		        System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		       System.out.println("\t\t\t"+"1. 이전 페이지");
		       System.out.println("\t\t\t"+"2. 다음 페이지");
		       System.out.println("\t\t\t"+"0. 뒤로 가기");
		       System.out.println();
		        System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");   
		       //번호 고르기
		       Scanner scan = new Scanner(System.in);
		       System.out.print("\t\t\t"+"▷입력:  ");
		       String answer = scan.nextLine();
		      // scan.skip("\r\n"); //엔터 무시
		        System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		       //이전페이지
		       if(answer.equals("1")) {
		          //첫페이지 일 경우
		          if(count == 0) {
		             count = depart.size() - 1 ;
		             num = depart.size() * 10 -9;
		          } else {
		             count = count-1;
		             num -= 20;
		          }
		       } else if(answer.equals("2")){
		          //다음페이지
		          
		          //마지막 페이지일 경우
		          if(count == depart.size() -1 ) {
		             count = 0;
		             num = 1;
		          } else {
		             count = count+1;
		             
		          }

		       } else if(answer.equals("0")) {
		     	 //뒤로가기
		    	   AdminBook main = new AdminBook();
		    	   main.bookMenu(adminUser);
		     	  
		     	  
		       }
		             
		       else {
		          //System.out.println("\t\t\t"+"페이지 프로그램을 종료합니다.");
		          
		          
		          break;
		       }
		       
		       
		       
		       
		    } //while

		    }
			}
		public void RentalCheck(AdminUser adminUser) {
			
			// 1. Statement : 정적 쿼리
			// 2. PreparedStatement : 동적 쿼리(매개변수)
			// 3. CallableStatement : 프로시저 전용

			
			Connection conn = null;
			PreparedStatement stat = null;
			DBUtil util = new DBUtil();

			Scanner scan = new Scanner(System.in);
			
			//insert new info to tblRent table
			String Sql = "insert into tblRent(seq,member_seq,book_code,rent_date,return_date,extension_count) values (Rent_seq.nextVal,?,?,?,?,'0')";
			try {
				
				
				//open
				conn = util.open();
				stat = conn.prepareStatement(Sql);
				
				
				//User Input
				System.out.println("\t\t\t대여 정보를 입력해주세요.");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t▷회원번호 입력: ");
				String MemberNumInput = scan.nextLine();
				System.out.print("\t\t\t▷도서코드 입력: ");
				String BookNumInput = scan.nextLine();
				System.out.println("\t\t\t대여 시작 날짜를 입력해주세요.");
				System.out.print("\t\t\t▷년도(year)(yy): ");
				String STYearInput = scan.nextLine();
				System.out.print("\t\t\t▷ 월(month)(mm): ");
				String STMonthInput =scan.nextLine();
				System.out.print("\t\t\t▷일(day)(dd): ");
				String STDaynput = scan.nextLine();
				
				System.out.println("\t\t\t대여 종료 날짜를 입력해주세요.");
				System.out.print("\t\t\t▷년도(year)(yy): ");
				String LTYearInput = scan.nextLine();
				System.out.print("\t\t\t▷ 월(month)(mm): ");
				String LTMonthInput =scan.nextLine();
				System.out.print("\t\t\t▷일(day)(dd): ");
				String LTDayInput = scan.nextLine();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
				//유효성검사
				if (Pattern.matches("^[0-9]*$",STYearInput) || Pattern.matches("^[0-9]*$",STMonthInput) || Pattern.matches("^[0-9]*$",STDaynput) || 
					Pattern.matches("^[0-9]*$",LTYearInput) || Pattern.matches("^[0-9]*$",LTMonthInput) || Pattern.matches("^[0-9]*$",LTDayInput)) {

				
				//input new info
				stat.setString(1, MemberNumInput);
				stat.setString(2, BookNumInput);
				stat.setString(3, STYearInput + "/" + STMonthInput + "/" + STDaynput);
				stat.setString(4, LTYearInput + "/" + LTMonthInput + "/" + LTDayInput);
				
				} else {
					System.out.println("\t\t\t다시 입력해주세요.");
				}//else
				System.out.println("\t\t\t대여 등록 완료 되었습니다.");
				System.out.println();
				
				//close
				stat.close();
				conn.close();
				
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t▷계속 입력하려면 y아니면 n: ");
				String YNInput = scan.nextLine();
				
				if (YNInput.equals("y")) {
					//return to RentalCheck
					RentalCheck(adminUser);
					
				} else if (YNInput.equals("n")) {
					//next page
					 SelectRentalBook(MemberNumInput,BookNumInput,adminUser);
					
				} else {
					//exception
					System.out.println("\t\t\t다시 입력해주세요.");
				}
				
			} catch (Exception e) {
				System.out.println("\t\t\terror in RentalBook");
			}
			

		}

			public void SelectRentalBook(String MemberNumInput, String BookNumInput, AdminUser adminUser) {
				//select about new info in table
				
				ArrayList <String> list = new ArrayList<String>();
				Scanner scan = new Scanner(System.in);
				Connection conn = null;
				Statement stat = null;
				DBUtil util = new DBUtil();
				ResultSet rs = null;
				
			try {	
					
				//open
				conn = util.open();
				//connet
				stat = conn.createStatement();

				String sql = String.format("select * from vwCheckRentBook where 회원번호 = %s", MemberNumInput);
				
				//호출
				rs = stat.executeQuery(sql);

				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t[번호]\t[회원번호]\t[고객명]\t[도서코드]\t\t[대여시작일]\t[대여종료일]\t[도서명]");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				
				while(rs.next()) { list.add(rs.getString("회원번호") + "\t" + rs.getString("성함") + "\t" + rs.getString("도서코드") + "\t" +
									rs.getString("대여시작일")+"\t"+ rs.getString("대여종료일")+"\t"+rs.getString("도서명"));
					
				
				
				}//while
				
				//페이지
				CheckRentalBook add = new CheckRentalBook();
				add.page(list,MemberNumInput, BookNumInput, adminUser);
				
			} catch (Exception e) {
				System.out.println("\t\t\terror in SelectRentalBook");
			}//catch
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
			
			}//SelectRentalBook()
			

		}//CheckRentalBook
	
	
	
	
	
	//3. 예약 확인
	public class CheckReservation {
		//3. 예약확인
			
			public void SelectReservation(AdminUser adminUser) {
				
				Scanner scan = new Scanner(System.in);
				
				
				Connection conn = null;
				CallableStatement stat = null;
				DBUtil util = new DBUtil();
				ResultSet rs = null;


			try {
				//procedure 불러오기
				String sql = "{ call procCheckReserve(?,?) }";
				
				//사용자 입력
				System.out.println("\t\t\t조회할 예약날짜를 입력하세요. ");
				System.out.print("\t\t\t▷년도(year)(yy): ");
				String SelectYearInput = scan.nextLine();
				System.out.print("\t\t\t▷ 월(month)(mm): ");
				String SelectMonthInput =scan.nextLine();
				System.out.print("\t\t\t▷일(day)(dd): ");
				String SelectDayInput = scan.nextLine();
				if (Pattern.matches("^[0-9]*$",SelectYearInput) || (Pattern.matches("^[0-9]*$",SelectMonthInput) || (Pattern.matches("^[0-9]*$",SelectDayInput)))) {
			

					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("[번호]\t\t[회원번호]\t\t[회원명]\t\t[예약날짜]\t\t[저자명]\t\t[도서명]");
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					
					//oepn sql
					conn=util.open();
					
					//sql 호출
					stat = conn.prepareCall(sql);
				
					//사용자 입력값 명령값으로 넣기
					stat.setString(1,SelectYearInput+"/"+SelectMonthInput+"/"+SelectDayInput );
					
					//그외 정보 불러오기
					stat.registerOutParameter(2, OracleTypes.CURSOR);
					stat.executeQuery();
					rs = (ResultSet) stat.getObject(2);
					ResultSetMetaData rsmd = rs.getMetaData();
				} else {
					System.out.println("다시 입력하세요.");
				}
			
					//출력
					while (rs.next()) { 
						
						System.out.printf("%s\t\t%s\t\t%s\t\t%s\t%s\t%s\n",rs.getString("번호")
																,rs.getString("회원번호")
																,rs.getString("회원명")
																,rs.getString("예약날짜")
																,rs.getString("저자명")
																,rs.getString("도서명"));
					}//while
					
					System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				
					//close
					rs.close();
					stat.close();
					conn.close();
				
					} catch (Exception e) {
						System.out.println("\t\t\t에러발생,,");
						
					
					}//catch
			

				
					//뒤로가기
					System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t0. 뒤로가기");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t▷입력: ");
					String backtUserInput = scan.nextLine();
				
					
					if (backtUserInput.equals("0")) {
					//0. 뒤로가기
						
					AdminBook main = new AdminBook();
					main.bookMenu(adminUser);
				
				} else {
					System.out.println("\t\t\t다시 입력해주세요.");
				 
				}//else


			
			}//SelectReservation()

		}//CheckReservation
	
	
	//4. 반납 처리 및 확인
	public class ReturnBook {

		public void RentalCheck(AdminUser adminUser) {
			// 도서관리 - 반납확인
			Scanner scan = new Scanner(System.in);

			System.out.println("\t\t\t1. 도서 반납 처리하기");
			System.out.println("\t\t\t2. 도서 반납 내역 조회");
			System.out.println("\t\t\t0. 뒤로가기");
			// 사용자 입력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\n\t\t\t▷입력: ");
			String RentablUserInput = scan.nextLine();

			if (RentablUserInput.equals("1")) {
				// 1. 도서 반납 처리하기
				UpdateRental(adminUser);

			} else if (RentablUserInput.equals("2")) {
				// 2. 도서 반납 내역 조회
				CheckPeriodBook(adminUser);

			} else if (RentablUserInput.equals("0")) {
				// 0. 뒤로가기
				AdminBook main = new AdminBook();
				main.bookMenu(adminUser);

			} else {
				System.out.println("\t\t\t다시 입력해주세요.");
			}

		}// RentalCheck()

		public void UpdateRental(AdminUser adminUser) {
			// 1. 도서 반납 처리하기

			Connection conn = null;
			CallableStatement stat = null;
			DBUtil util = new DBUtil();
			ResultSet rs = null;
			Scanner scan = new Scanner(System.in);

			try {

				// procedure 호출
				String sql = "{call procretrunCheck(?,?)}";

				// 사용자 입력
				System.out.print("\t\t\t▷도서코드 입력: ");
				String InputBookCode = scan.nextLine();
				// 문자열의 공백제거
				InputBookCode = InputBookCode.replaceAll("\\p{Z}", "");

				System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("[회원번호]\t\t[회원명]\t\t[대여일]\t\t[반납일]\t\t[반납여부]\t\t[연체횟수]\t\t[도서코드]\t\t\t[도서명]");
				System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				// oepn sql
				conn = util.open();

				// sql 호출
				stat = conn.prepareCall(sql);

				// 사용자 입력값 명령값으로 넣기
				stat.setString(1, InputBookCode);

				// 그외 정보 불러오기
				stat.registerOutParameter(2, OracleTypes.CURSOR);
				stat.executeQuery();
				rs = (ResultSet) stat.getObject(2);
				ResultSetMetaData rsmd = rs.getMetaData();

				// 출력
				while (rs.next()) {

					System.out.printf("%s\t\t%s\t\t%s\t%s\t\t%s\t\t%s\t\t%s\t\t%s\n", rs.getString("회원번호"),
							rs.getString("성함"), rs.getString("대여날짜"), rs.getString("반납날짜"), rs.getString("반납여부"),
							rs.getString("연체여부"), rs.getString("도서코드"), rs.getString("도서명"));

				} // while

				System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t1. 반납처리하기");
				System.out.println("\t\t\t0. 뒤로가기");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t▷입력: ");
				String UserInput = scan.nextLine();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				if (UserInput.equals("1")) {
					// 1.반납처리하기
					returnstate(InputBookCode, adminUser);

				} else if (UserInput.equals("0")) {
					// 0.뒤로가기
					RentalCheck(adminUser);

				} else { // exception
					System.out.println("\t\t\t다시 입력해주세요.");
				}

				// close
				rs.close();
				stat.close();
				conn.close();

			} catch (Exception e) {
				System.out.println("\t\t\t조회되지 않습니다. 다시 입력해주세요.");
				UpdateRental(adminUser);
			} // catch

		} // UpdateRental()

		public void returnstate(String InputBookCode, AdminUser adminUser) {
			// 반납처리하기

			Connection conn = null;
			Statement stat = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			DBUtil util = new DBUtil();
			Scanner scan = new Scanner(System.in);

			System.out.println("\t\t\t반납 처리로 변경하려는 회원번호을 입력해주세요.");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			String MemberNumInput = scan.nextLine();

			try {

				// open sql
				conn = util.open();

				// connet with sql
				stat = conn.createStatement();

				// sql update 명령
				String sql = String.format(
						"update tblRent set return_date = sysdate where member_seq= %s and book_code= '%s'", MemberNumInput,
						InputBookCode);

				// sql select 명령
				String selectsql = String.format("select * vwCheckReturnBook where 도서코드 = %s and 회원번호 = %s", InputBookCode,
						MemberNumInput);

				// 명령값가져오기
				rs = stat.executeQuery(sql);

				// 실행
				System.out.printf("\t\t\t회원번호: %s\n\t\t\t도서코드:%s\n\n\t\t\t반납 처리 완료되었습니다.\n\n", MemberNumInput,
						InputBookCode);

				// 뒤로가기
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t0. 뒤로가기");
				System.out.print("\t\t\t▷입력: ");
				String BackUserInput = scan.nextLine();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				if (BackUserInput.equals("0")) {
					// 0.뒤로가기
					RentalCheck(adminUser);

				} else { // exception
					System.out.println("\t\t\t번호를 다시 입력해주세요.");
				}

			} catch (Exception e) {
				System.out.println("\t\t\t조회 되지 않습니다. 다시 입력해주세요.");
				returnstate(InputBookCode, adminUser);
			}

		}

		public void CheckPeriodBook(AdminUser adminUser) {
			// 도서관리-반납확인-내역조회

			Connection conn = null;
			Statement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();
			Scanner scan = new Scanner(System.in);

			try {

				// 사용자 입력
				System.out.println("\t\t\t조회할 반납일을 입력해주세요");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t▷년도(year)(YY): ");
				String SelectYearInput = scan.nextLine();
				System.out.print("\t\t\t▷ 월(month)(MM): ");
				String SelectMonthInput =scan.nextLine();
				System.out.print("\t\t\t▷일(day)(DD): ");
				String SelectDayInput = scan.nextLine();
				if (Pattern.matches("^[0-9]*$",SelectYearInput) || (Pattern.matches("^[0-9]*$",SelectMonthInput) || (Pattern.matches("^[0-9]*$",SelectDayInput)))) {
					
				
					// open
					conn = util.open();

					// connet
					stat = conn.createStatement();

					// sql select 명령
					String sql = String.format("select * from vwCheckReturnBook where 반납날짜 = '%s'",SelectYearInput+"/"+SelectMonthInput+"/"+SelectDayInput);

					// 값 가져오기
					rs = stat.executeQuery(sql);
				}
				else {
					System.out.println("\t\t\t다시 입력해주세요.");
				}
				

				System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("[도서코드]\t\t\t[고객명]\t\t[회원번호]\t\t[대여일]\t\t[반납일]\t\t[반납여부]\t\t[연체횟수]\t\t[도서명]");
				System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				// 명령값 출력
				while (rs.next()) {

					System.out.printf("%s\t\t%s\t\t%s\t\t%s\t%s\t%s\t\t%s\t\t%s\n", rs.getString("도서코드"), rs.getString("성함"),
							rs.getString("회원번호"), rs.getString("대여날짜"), rs.getString("반납날짜"), rs.getString("반납여부"),
							rs.getString("연체여부"), rs.getString("도서명"));
				} // while

				System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				// 뒤로가기
				System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t0. 뒤로가기");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t▷입력: ");
				String BackUserInput = scan.nextLine();
				System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				if (BackUserInput.equals("0")) {
					// 0.뒤로가기
					RentalCheck(adminUser);

				} else { // exception
					System.out.println("\t\t\t번호를 다시 입력해주세요.");
				}

			} catch (Exception e) {
				System.out.println("\t\t\t조회 되지 않습니다. 다시 입력해주세요.");
				CheckPeriodBook(adminUser);
			}

		}

	}// ReturnBook
	
		
		
	//5. 도서 추가 및 삭제
		public class UpdateBook {
			// 도서 추가 및 삭제

			public void page(List<String> arrayList, String BookNameInput, String PublisherInput, String AuthorInput,
					AdminUser adminUser) {

				// 10개씩 분할
				List<String[]> depart = new ArrayList<String[]>();
				int firstindex = 0;
				int lastindex = 10;
				int index = 0;
				int totalcount = arrayList.size();

				while (true) {
					// 10개의 String 이 들어갈 묶음
					String[] ranking = new String[10];

					for (int j = firstindex; j < lastindex; j++) {
						if (j >= arrayList.size()) {
							break;
						}
						ranking[j - (index) * 10] = arrayList.get(j);
					}
					depart.add(ranking);

					firstindex = firstindex + 10;
					lastindex = lastindex + 10;
					index++;

					if (firstindex >= totalcount) {
						break;
					}

				}

				// 페이지 수
				int count = 0;

				int num = 1;

				// 페이지가 한개만 있는 경우
				if (depart.size() == 1) {
					// 첫페이지 보여주기
					for (int i = 0; i < depart.get(0).length; i++) {
						if (depart.get(0)[i] != null) {
							System.out.printf("\t\t\t%d. %s\n", i + 1, depart.get(0)[i]);
						}
					}
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t" + "넘어갈 페이지가 없습니다.");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				} else {

					// 페이지가 여러개인 경우
					while (true) {

						System.out.println();
						for (int i = 0; i < 10; i++) {
							System.out.printf("\t\t\t%d. ", num);
							if (depart.get(count)[i] != null) {
								System.out.printf("\t%s\t", depart.get(count)[i]);
							}
							System.out.println();
							num++;
						}
						// 페이지 반복
						System.out.println();
						System.out.println();
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.printf("\t\t\t" + "%d페이지 입니다.\n", count + 1);
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.println("\t\t\t" + "1. 이전 페이지");
						System.out.println("\t\t\t" + "2. 다음 페이지");
						System.out.println("\t\t\t" + "3. 코드 입력하기");
						System.out.println();
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						// 번호 고르기
						Scanner scan = new Scanner(System.in);
						System.out.print("\t\t\t" + "▷입력:  ");
						String answer = scan.nextLine();
						// scan.skip("\r\n"); //엔터 무시
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						// 이전페이지
						if (answer.equals("1")) {
							// 첫페이지 일 경우
							if (count == 0) {
								count = depart.size() - 1;
								num = depart.size() * 10 - 9;
							} else {
								count = count - 1;
								num -= 20;
							}
						} else if (answer.equals("2")) {
							// 다음페이지

							// 마지막 페이지일 경우
							if (count == depart.size() - 1) {
								count = 0;
								num = 1;
							} else {
								count = count + 1;

							}

						} else if (answer.equals("3")) {
							UpdateBook add = new UpdateBook();
							add.AddBook2(BookNameInput, PublisherInput, AuthorInput, adminUser);

						}

						else {
							System.out.println("\t\t\t잘못입력하셨습니다.");
							// System.out.println("\t\t\t"+"페이지 프로그램을 종료합니다.");

							break;
						}

					} // while

				}
			}

			public void UpdateBookList(AdminUser adminUser) {

				Scanner scan = new Scanner(System.in);

				System.out.println("\t\t\t1. 도서 추가");
				System.out.println("\t\t\t2. 도서 수정");
				System.out.println("\t\t\t3. 도서 삭제");
				System.out.println("\t\t\t0. 뒤로 가기");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t입력: ");
				String AddBookUserInput = scan.nextLine();

				if (AddBookUserInput.equals("1")) {
					// 1. 도서 추가
					AddBook1(adminUser);

				} else if (AddBookUserInput.equals("2")) {
					// 2. 도서 수정
					UpdateBookName(adminUser);

				} else if (AddBookUserInput.equals("3")) {
					// 3. 도서 삭제
					DeleteBookInfo(adminUser);

				} else if (AddBookUserInput.equals("0")) {
					// 0. 뒤로 가기 -> AdminBook
					AdminBook main = new AdminBook();
					main.bookMenu(adminUser);
				} else {
					System.out.println("\t\t\t번호를 다시 입력해주세요.");
				}

			}// AddBookList()

			public void DeleteBookInfo(AdminUser adminUser) {
				// 3. 도서 삭제

				Scanner scan = new Scanner(System.in);
				Connection conn = null;

				PreparedStatement stat = null;
				DBUtil util = new DBUtil();
				ResultSet rs = null;

				try {
					// procedure 불러오기
					String sql = "select * from vwSelectBookList where 도서코드 like ? ";

					// 사용자 입력
					System.out.println("\t\t\t삭제할 도서코드를 입력해주세요.");
					System.out.print("\t\t\t▷입력: ");
					String DeleteUserInput = scan.nextLine();
					// 문자열의 공백제거
					DeleteUserInput = DeleteUserInput.replaceAll("\\p{Z}", "");

					// oepn sql
					conn = util.open();

					// sql 호출
					stat = conn.prepareStatement(sql);

					// 사용자 입력값 명령값으로 넣기
					stat.setString(1, "%" + DeleteUserInput + "%");

					// 그외 정보 불러오기
					rs = stat.executeQuery();

					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t[번호]\t[도서코드]\t\t[분류]\t\t[출판사]\t\t[저자]\t\t[도서명]");
					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// 출력
					while (rs.next()) {

						System.out.printf("\t%s\t%s\t%s\t\t%s\t\t%s\t%s\n", rs.getString("번호"), rs.getString("도서코드"),
								rs.getString("분류"), rs.getString("출판사"), rs.getString("저자"), rs.getString("도서명"));
					} // while

					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					System.out.println("\t\t\t삭제할 도서의 번호를 입력하세요. ");
					System.out.print("\t\t\t▷입력: ");
					String DeleteBookseq = scan.nextLine();
					// 유효성검사 (숫자만가능)

					if (Pattern.matches("^[0-9]*$", DeleteBookseq)) {

						System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

						System.out.println("\t\t\t1. 삭제하기");
						System.out.println("\t\t\t0. 뒤로가기");
						System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						System.out.print("\t\t\t▷입력: ");
						String DeleteUserInputNum = scan.nextLine();

						if (DeleteUserInputNum.equals("1")) {
							// 1. 삭제하기

							Connection conn2 = null;
							Statement stat2 = null;

							conn2 = util.open();

							String BookCodesql = String.format(
									"update tblBookstate set delete_exist = 1 where book_code = '%s' and book_seq = '%s' ",
									DeleteBookseq, DeleteBookseq);

							stat2 = conn2.createStatement();
							stat2.executeUpdate(BookCodesql);

							stat.close();
							conn.close();
							System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							System.out.println("\t\t\t삭제 되었습니다.\n");
							System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

							BacktoMain(adminUser);

						} else if (DeleteUserInputNum.equals("0")) {
							// 0. 뒤로가기

							UpdateBookList(adminUser);

						} else {
							System.out.println("\t\t\t다시 입력해주세요.");
						}

					} else {
						System.out.println("\t\t\t다시 입력해주세요.");
					}

					// close
					rs.close();
					stat.close();
					conn.close();

				} catch (Exception e) {
					System.out.println("\t\t\t에러발생,,");

				} // catch

			}

			public void UpdateBookName(AdminUser adminUser) {
				// 2. 도서 수정

				Scanner scan = new Scanner(System.in);
				Connection conn = null;
				PreparedStatement stat = null;
				DBUtil util = new DBUtil();
				ResultSet rs = null;

				try {
					// procedure 불러오기

					// 사용자 입력
					System.out.println("\t\t\t수정할 도서코드를 입력해주세요.");
					System.out.print("\t\t\t▷입력: ");
					String UpdateUserInput = scan.nextLine();
					// 문자열의 공백제거
					UpdateUserInput = UpdateUserInput.replaceAll("\\p{Z}", "");

					// oepn sql
					conn = util.open();

					String sql = "select * from vwSelectBookList where 도서코드 like ?";

					// sql 호출
					stat = conn.prepareStatement(sql);

					// 사용자 입력값 명령값으로 넣기
					stat.setString(1, "%" + UpdateUserInput + "%");

					// 정보 불러오기
					rs = stat.executeQuery();

					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t[번호]\t[도서코드]\t\t[분류]\t[출판사]\t\t[저자]\t\t[도서명]");
					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// 출력
					while (rs.next()) {

						System.out.printf("\t%s\t%s\t%s\t%s\t\t%s\t%s\n", rs.getString("번호"), rs.getString("도서코드"),
								rs.getString("분류"), rs.getString("출판사"), rs.getString("저자"), rs.getString("도서명"));
					} // while

					System.out.println("\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					System.out.println("\t\t\t수정할 도서의 번호를 입력하세요. ");
					System.out.print("\t\t\t▷입력: ");
					String UpdateBookseq = scan.nextLine();

					System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t1. 도서 코드/대분류/소분류");
					System.out.println("\t\t\t2. 출판사");
					System.out.println("\t\t\t3. 저자");
					System.out.println("\t\t\t4. 도서명");
					System.out.println("\t\t\t0. 뒤로가기");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t▷입력: ");
					String UserInput = scan.nextLine();
					System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					if (UserInput.equals("1")) {
						// 1. 도서 코드/대분류/소분류
						Connection conn2 = null;
						Statement stat2 = null;

						System.out.println("\t\t\t수정 내용을 입력해주세요.");
						System.out.print("\t\t\t▷입력: ");
						String CodeUpdateInput = scan.nextLine();

						conn2 = util.open();
						String BookCodesql = String.format("update tblBookstate set book_code = '%s' where book_code = '%s' ",
								CodeUpdateInput, UpdateUserInput);
						// 이거못함....ㅜㅜ
						stat2 = conn2.createStatement();
						stat2.executeUpdate(BookCodesql);

						System.out.println("\t\t\t수정 완료되었습니다.");

						stat.close();
						conn.close();

						BacktoMain(adminUser);

					} else if (UserInput.equals("2")) {
						// 2. 출판사
						Connection conn2 = null;
						Statement stat2 = null;

						System.out.println("\t\t\t수정 내용을 입력해주세요.");
						System.out.print("\t\t\t▷입력: ");
						String publicsherUpdateInput = scan.nextLine();

						conn2 = util.open();
						String BookCodesql = String.format("update tblBook set publisher = '%s' where seq = %s ",
								publicsherUpdateInput, UpdateBookseq);

						stat2 = conn2.createStatement();
						stat2.executeUpdate(BookCodesql);

						System.out.println("\t\t\t수정 완료되었습니다.");

						stat.close();
						conn.close();

						BacktoMain(adminUser);

					} else if (UserInput.equals("3")) {
						// 3. 저자
						Connection conn2 = null;
						Statement stat2 = null;

						System.out.println("\t\t\t수정 내용을 입력해주세요.");
						System.out.print("\t\t\t▷입력: ");
						String authorUpdateInput = scan.nextLine();

						conn2 = util.open();
						String BookCodesql = String.format("update tblBook set author = '%s' where seq = %s ",
								authorUpdateInput, UpdateBookseq);

						stat2 = conn2.createStatement();
						stat2.executeUpdate(BookCodesql);

						System.out.println("\t\t\t수정 완료되었습니다.");

						stat.close();
						conn.close();

						BacktoMain(adminUser);

					} else if (UserInput.equals("4")) {
						// 4. 도서명
						Connection conn2 = null;
						Statement stat2 = null;

						System.out.println("\t\t\t수정 내용을 입력해주세요.");
						System.out.print("\t\t\t▷입력: ");
						String nameUpdateInput = scan.nextLine();

						conn2 = util.open();
						String BookCodesql = String.format("update tblBook set book_name = '%s' where seq = %s ",
								nameUpdateInput, UpdateBookseq);

						stat2 = conn2.createStatement();
						stat2.executeUpdate(BookCodesql);

						System.out.println("\t\t\t수정 완료되었습니다.");

						stat.close();
						conn.close();

						BacktoMain(adminUser);

					} else if (UserInput.equals("0")) {
						// 0. 뒤로가기 -> UpdateBookMain
						UpdateBookList(adminUser);

					} else { // exception
						System.out.println("\t\t\t다시 입력해주세요.");
					}

					// close
					rs.close();
					stat.close();
					conn.close();

				} catch (Exception e) {
					System.out.println("\t\t\t에러발생.,,");

				} // catch

			}

			public void BacktoMain(AdminUser adminUser) {
				// 뒤로가기 구현 Method

				Scanner scan = new Scanner(System.in);

				System.out.println("\t\t\t0. 뒤로가기");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t▷입력: ");
				String BackUserInput = scan.nextLine();

				if (BackUserInput.equals("0")) {
					// 0.뒤로가기
					UpdateBookList(adminUser);
				} else {
					System.out.println("\t\t\t다시 입력해주세요.");
				}
			}

			public void AddBook1(AdminUser adminUser) {
				// 1. 도서 추가1

				Scanner scan = new Scanner(System.in);

				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t추가하려는 도서의 정보를 입력해주세요.");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t▷도서명: ");
				String BookNameInput = scan.nextLine();
				System.out.print("\t\t\t▷출판사: ");
				String PublisherInput = scan.nextLine();
				System.out.print("\t\t\t▷저자: ");
				String AuthorInput = scan.nextLine();

				// 대분류 번호 보여주고 입력받기
				DecimalList(BookNameInput, PublisherInput, AuthorInput, adminUser);

			}// AddBook1()

			public void AddBook2(String BookNameInput, String PublisherInput, String AuthorInput, AdminUser adminUser) {
				// 2. 도서 추가2 -> 페이지 때문에 나눠서 진행

				Scanner scan = new Scanner(System.in);
				Connection conn = null;
				Connection conn1 = null;
				Connection conn2 = null;
				CallableStatement stat = null;
				CallableStatement Statestat = null;
				ResultSet rs = null;
				DBUtil util = new DBUtil();
				Statement Selectstat = null;

				try {
					// tblBook insert procedure
					String Booksql = "{ call ProcAddBook(?,?,?,?,?) }";

					// tblBookstate insert procedure
					String Statesql = "{ call ProcAddBookstate(?,?,?,?) }";

					// open
					conn = util.open();
					conn1 = util.open();
					conn2 = util.open();

					// connect tblBook
					stat = conn.prepareCall(Booksql);

					// connect tblBookstate
					Statestat = conn2.prepareCall(Statesql);

					// User Input
					System.out.print("\t\t\t▷대분류 코드: ");
					String DecimalInput = scan.nextLine();
					System.out.print("\t\t\t▷소분류 코드: ");
					String SeriesInput = scan.nextLine();
					System.out.print("\t\t\t▷위치: ");
					String location = scan.nextLine();

					// 도서 코드 생성
					String Bookcode = DecimalInput + "-" + BookNameInput.substring(1, 2) + AuthorInput.substring(1, 2) + "-s"
							+ SeriesInput + "-" + "v01";

					// 유효성검사
					if (DecimalInput.length() < 1000 && SeriesInput.length() < 1000) {

						// tblBook setString
						stat.setString(1, BookNameInput);
						stat.setString(2, PublisherInput);
						stat.setString(3, AuthorInput);
						stat.setString(4, DecimalInput);
						stat.setString(5, SeriesInput);

						// for select connect
						Selectstat = conn1.createStatement();

						// tblbook의 새로 등록된 seq 가져오기
						String selectsql = "select max(seq) from tblBook";

						rs = Selectstat.executeQuery(selectsql);

						// tblBookstate setString
						Statestat.setString(1, Bookcode);
						Statestat.setString(2, selectsql);
						// Location의 유효성 검사
						if (Pattern.matches("^[a-zA-Z]*$", location)) {
							Statestat.setString(3, location);
							// for location
						} else {
							System.out.println("\t\t\t다시 입력해주세요.");
						} // else

						// for DecimalInput, SeriesInput
					} else {
						System.out.println("\t\t\t잘못 입력 하셨습니다. 다시 입력해주세요.");
						AddBook1(adminUser); // 다시입력
					} // else

					stat.executeQuery();

					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t등록 완료 했습니다.");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// 결과출력
					System.out.printf(
							"\n\t\t\t도서명: %s\n\t\t\t출판사: %s\n\t\t\t저자: %s\n\t\t\t대분류 코드: %s\n\t\t\t소분류 코드: %s\n\t\t\t도서코드: %s\n\t\t\t위치: %s\n",
							BookNameInput, PublisherInput, AuthorInput, DecimalInput, SeriesInput, Bookcode, location);

					Selectstat.close();
					stat.close();
					Statestat.close();
					conn.close();

				} catch (Exception e) {
					System.out.println("Error in AddBook2");
					// TODO: handle exception
				}

				System.out.println("\n\t\t\t0. 뒤로가기");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t▷입력: ");
				System.out.println("\n\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				String AddUserInput = scan.nextLine();

				if (AddUserInput.equals("0")) {
					// 0. 뒤로가기 -> UpdateBookMain
					UpdateBookList(adminUser);
				} else {
					System.out.println("\t\t\t번호를 다시 입력해주세요.");
				}

			}// AddBook2()

			public void DecimalList(String BookNameInput, String PublisherInput, String AuthorInput, AdminUser adminUser) {
				// 도서 십진분류코드 리스트

				Connection conn = null;
				CallableStatement stat = null;
				ResultSet rs = null;
				DBUtil util = new DBUtil();

				ArrayList<String> list = new ArrayList<String>(); // 페이지 arraylist

				try {
					// 리스트 생성

					// 프로시저 부르기
					String sql = "{ call ProDecimal(?) }";

					// open
					conn = util.open();
					stat = conn.prepareCall(sql);

					// 커서부르기
					stat.registerOutParameter(1, OracleTypes.CURSOR);

					// 출력요청
					stat.executeQuery();

					// 출력값 담기
					rs = (ResultSet) stat.getObject(1);

					System.out.println();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t\t" + "<십진분류>");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					System.out.println("\t\t\t[번호]\t[십진분류 코드]\t[항목]");

					// 출력
					while (rs.next()) {
						list.add(rs.getString("decimalcode") + "\t" + rs.getString("item"));
					}
					// page로 나누기
					UpdateBook pages = new UpdateBook();
					pages.page(list, BookNameInput, PublisherInput, AuthorInput, adminUser);

					// close
					rs.close();
					stat.close();
					conn.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
				UpdateBook add2 = new UpdateBook();
				add2.AddBook2(BookNameInput, PublisherInput, AuthorInput, adminUser);
			}
		}// UpdateBook

		
		
		
	
}
