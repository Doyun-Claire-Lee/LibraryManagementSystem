package admin;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	
	private Connection conn = null;

	//서버 연결
	public Connection open(){
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String id = "lms";
		String pw = "java1234";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			
			conn = DriverManager.getConnection(url,id,pw);
			
			return conn;
	
			
		} catch (Exception e) {
			System.out.println("DBUtil.getConnection()");
			e.printStackTrace();
		}
		
		return null;
		
	}//open
	
	
	//연결 시작
	public Connection open(String host, String id, String pw){
		
		
		String url = "jdbc:oracle:thin:@"+ host +":1521:xe";
	
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			
			conn = DriverManager.getConnection(url,id,pw);
			
			return conn;
	
			
		} catch (Exception e) {
			System.out.println("DBUtil.getConnection()");
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	//연결 종료
	public void close() {
	try {
		conn.close();
	} catch (Exception e) {
		System.out.println("DBUtil.close()");
		e.printStackTrace();
	}
		
	}//close
	
}
