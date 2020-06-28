package user;

import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author shin
 * 도서정보가 생성되면 리스트로 변환되어 회원에게 대여나 예약을 받기위해 책의 인덱스를 선택하는 클래스
 */
public class SelectBookOrReserve {
	
	/**
	 * 
	 * @param printBookList 사용자에게 보여주는 화면 데이터리스트
	 * @return 대여/예약번호 , 도서코드
	 * 대여가 가능하다면 1 예약해야한다면 0으로 지정할것이다
	 */
	public int[] distinguish(List<String[]> printBookList) {//유저에게 제목중복을 제외한 책의 리스트를 보여주며 바로 대여가능한 책인지 예약을 해야하는 책인지 판별해주는 클래스
		Scanner scan = new Scanner(System.in);
		
		int finalIndex = printBookList.size();//해당 페이지에서 표시될 마지막 번호
		
		int[] resultArr = new int[2];//대여/예약번호 , 도서정보번호
		resultArr[0] = -3;//루프문을 벗어나기 위해 정해준것
		
		System.out.println("[번호]\t[책이름]\t\t[저자명]\t[출판사이름]\t[도서위치]\t\t[도서정보번호]\t[대여가능여부]");
		
		MemberPaging mp = new MemberPaging();
		mp.page(printBookList, 10);//페이징 기술
		
		
//		for (int i = 0; i < printBookList.size(); i++) {
//			System.out.printf("%d\t%s\t%s\t%s\t%s\t\t%s\t\t%s\n",
//					i+1,
//					printBookList.get(i)[0],//책이름
//					printBookList.get(i)[1],//저자명
//					printBookList.get(i)[2],//출판사 이름
//					printBookList.get(i)[3],//도서위치
//					printBookList.get(i)[4],//도서정보번호
//					printBookList.get(i)[5]);//대여가능여부
//		}
		
		
		//대여or예약 진행할지 뒤로갈지 정해준다.
		boolean firstFlag = true;
		
		while(firstFlag) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1.대여하기 ");
			System.out.println("\t\t\t0.뒤로가기 ");
			System.out.print("\t\t\t▷입력: ");
			String input1 = scan.nextLine();
			
			if (input1.equals("1")) {//1.대여하기
				
				boolean flag = true;
				firstFlag = false;
				
				int selectNum = -1;//아래에서 스캔으로 받을 input -> 사용자가 고른 책의 인덱스라고 보면된다.
				
				while(flag) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t▷대여할 책의 번호 선택: ");
					String input = scan.nextLine();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					
					
					/*
					 * <유효성 검사>
					 * 1. 회원이 인덱스 초과하는 번호를 누를 가능성이 존재 -> 제어문으로 해결
					 * 2. 회원이 이상한 기호를 쓸 가능성이 존재 -> 예외처리로 해결
					 * */
					try {//여기서 유효성 검증 해줄것이다.
						
						int intInput = Integer.parseInt(input);//회원이 입력한 번호
						
						//1. 회원이 인덱스 초과하는 번호를 누를 가능성이 존재 -> 제어문으로 해결
					
						if (intInput > finalIndex || intInput <= 0) {
							throw new NumberFormatException();//오류발생시켜서 다시  while문을 돌게 한다.
						} else {
							selectNum = intInput-1;
							flag = false;
						}
						
					} catch(NumberFormatException e) {//2. 회원이 이상한 기호를 쓸 가능성이 존재 -> 예외처리로 해결
						System.out.println("\t\t\t해당리스트에 존재하는 번호만을 입력해주세요 ");
					}

				}//while()
				
				
				
				if (printBookList.get(selectNum)[5].equals("대여가능")) {//해당 책이 대여가 가능한 경우
					resultArr[0] = 1;//대여가 가능하다는 1의표시
					
				} else {//해당 책이 대여가 불가능하여 예약을 해야하는 경우.
					resultArr[0] = 0;//대여가 불가능하다는 0의표시
				}

				resultArr[1] = Integer.parseInt(printBookList.get(selectNum)[4]);//도서정보번호
				
				
				
				
			} else if (input1.equals("0")) {//0.뒤로가기
				firstFlag = false;
				
			} else {
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}
			
			
		}//while()
		
		
		
		return resultArr;
		
	}//distinguish()
	
}
