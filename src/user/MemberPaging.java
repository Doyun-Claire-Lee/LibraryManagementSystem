package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemberPaging {
	/**
	 * 페이징해주는 메소드
	 * @param inputList 페이징할 리스트를 적는다 
	 * @param count 한페이지당 몇개의 정보를 가져올건지 정해준다
	 */
	public void page(List<String[]> inputList,int cut)//여기서 직접적으로 페이징을 해줄것이다 cut 
	{	
		
		int contLen = inputList.get(0).length;//내부요소 길이
		
		//어쩔수 없어 값복사를 해주자 -> 앞에 인덱스를 붙이는 행위 수행
		List<String[]> newInputList = new ArrayList<String[]>();
		int indNum = 1;//인덱싱 해줄것
		
		for (int i = 0; i < inputList.size(); i++) {
			String[] data = new String[contLen+1];
			data[0] = "" + indNum;//인덱싱 넘버 넣기
			
			for (int j = 0; j < contLen; j++) {
				data[j+1] = inputList.get(i)[j];
			}
			
			newInputList.add(data);
			indNum++;
		}//for
		
		
		
		
		Scanner scan = new Scanner(System.in);
		
		int listLen = newInputList.size();//리스트 전체의 길이
		
		int q = listLen/cut;//몫
		int r = listLen%cut;//나머지 -> 나머지가 0이 아니면 바로 올려준다!
		
		
		if (q == 0) {//즉 요소가 cut 개가 안되었을때는 다음페이지가 없을것이므로 그냥 넘어가주면 된다
			
			for (int i = 0; i < listLen; i++) {
				for (int j = 0; j < newInputList.get(0).length; j++) {
					System.out.print(newInputList.get(i)[j] + "\t");
				}
				System.out.println();
			}//for
			
			
		} else {//다음 페이지가 필요한 경우
			if (r != 0) q++;//나머지가 0이 아니면 몫을 하나 올려준다 -> 즉 페이지를 하나 더 만들어준다는 의미가 된다!
			
			//0 ~ cut-1까지 나올것
			
			int index = 1;//인덱스
			int startIndex = 0;//시작인덱스
			
			boolean flag = true;
			
			while(flag) {
				
				//딱 떨어지는 숫자와 아닌것의 차이가 존재한다!
				if (startIndex == cut*(q-1) && r!=0) {//딱 떨어지는 숫자는 여기에 필터 걸릴 일이 없음
					//System.out.println("여기로들어옴");
					for (int i = startIndex; i < startIndex + r; i++) {
						for (int j = 0; j < newInputList.get(0).length; j++) {
							System.out.print(newInputList.get(i)[j] + "\t");
						}
						System.out.println();
						
					}//for
					
				} else {
					for (int i = startIndex; i < cut * index; i++) {
						
						for (int j = 0; j < newInputList.get(i).length; j++) {
																			
							System.out.print(newInputList.get(i)[j] + "\t");
						}
						System.out.println();

					} // for

				}
				
				if (startIndex == 0) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t1. 다음페이지");
					System.out.println("\t\t\t0. 다음단계로");
					System.out.print("\t\t\t입력 : ");
					String input = scan.nextLine();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					
					if (input.equals("1")) {
						startIndex += cut;
						index++;
					} else {
						flag = false;
					}
					
					
				} else if (startIndex == cut*(q-1)) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t1. 이전페이지");
					System.out.println("\t\t\t0. 다음단계로");
					System.out.print("\t\t\t입력 : ");
					String input = scan.nextLine();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					
					if (input.equals("1")) {
						startIndex -= cut;
						index--;
					} else {
						flag = false;
					}
					
					
				} else {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t1. 다음페이지");
					System.out.println("\t\t\t2. 이전페이지");
					System.out.println("\t\t\t0. 다음단계로");
					System.out.print("\t\t\t입력 : ");
					String input = scan.nextLine();
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					
					if (input.equals("1")) {
						startIndex += cut;
						index++;
					} else if (input.equals("2")){
						startIndex -= cut;
						index--;
					} else {
						flag = false;
					}
					
					
				}


			}//while()
		
		}//else
		
		
		

 		
		
		
	}//page()
}
