package com.javaex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.vo.PhoneVo;
@Repository
//이걸 붙여야지만 자동으로 new를 붙이게 된다.
public class PhoneDao {
	@Autowired//데이터소스를 알아서 관리해줌
	private DataSource dataSource;
	
	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	//기존 db정보는 세팅으로 해놨음
	// db연결
	private void getConnection() {
		try {
			conn = dataSource.getConnection();//알아서 비어있는걸준다.
		} catch (SQLException e) {//null처리는 알아서 해줄거임
			System.out.println("error:" + e);
		}
	}

	// 자원 정리
	private void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 번호 저장
	public int personInsert(PhoneVo pvo) {
		int count = 0;
		getConnection();
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			// "INSERT INTO person VALUES (seq_person_id.nextval,?,?,?)"
			String query = "INSERT INTO person VALUES (seq_person_id.nextval,?,?,?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, pvo.getName());
			pstmt.setString(2, pvo.getHp());
			pstmt.setString(3, pvo.getCompany());
			System.out.println(query);

			count = pstmt.executeUpdate();
			// 4.결과처리
			System.out.println("[DAO]: " + count + "건이 저장되었습니다.");
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return 0;
	}

	// 번호삭제
	public int persondelete(int id) {
		int count = 0;
		getConnection();
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "DELETE FROM person WHERE person_id = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			System.out.println(query);
			count = pstmt.executeUpdate();
			// 4.결과처리
			System.out.println("[DAO]: " + count + "건이 삭제되었습니다.");
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}

	// 번호수정
	public int personUpdate(PhoneVo pvo) {
		getConnection();
		int count = 0;
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			// UPDATE person SET name =?,hp=?,company=? WHERE person_id = ? String query =
			// "";
			String query = "";
			query += " UPDATE person ";
			query += " SET name =?, ";
			query += " hp=?, ";
			query += " company=? ";
			query += " WHERE person_id = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, pvo.getName());
			pstmt.setString(2, pvo.getHp());
			pstmt.setString(3, pvo.getCompany());
			pstmt.setInt(4, pvo.getPersonid());
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("[DAO]: " + count + "건이 수정되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;

	}

	// 번호 리스트
	public List<PhoneVo> getpersonList() {
		List<PhoneVo> pList = new ArrayList<PhoneVo>();
		getConnection();
		try {
			// "INSERT INTO person VALUES (seq_person_id.nextval,?,?,?)"
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " select person_id, ";
			query += " 		  name, ";
			query += "        hp, ";
			query += "        company ";
			query += " FROM person ";
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();// select

			// 4.결과처리
			while (rs.next()) {

				int pid = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");
				PhoneVo pvo01 = new PhoneVo(pid, name, hp, company);
				pList.add(pvo01);

			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();

		return pList;

	}

	// 검색
	public List<PhoneVo> phoneSearch(String Search) {

		List<PhoneVo> phoneVoList = new ArrayList<PhoneVo>();

		getConnection();

		Search = "%" + Search + "%";

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " select	person_id,";
			query += "			name,";
			query += "			hp,";
			query += "			company";
			query += " from person";
			query += " where name like ?";
			query += " or hp like ?";
			query += " or company like ?";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, Search);
			pstmt.setString(2, Search);
			pstmt.setString(3, Search);

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int personId = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");

				PhoneVo phoneVo = new PhoneVo(personId, name, hp, company);
				phoneVoList.add(phoneVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return phoneVoList;
	
	}
	
	//사람 1명 정보 가지고 오기
		public PhoneVo getPerson(int personId) {
			PhoneVo phoneVo =null;//먼저 값은 주지 않음
			getConnection();
			// 3. SQL문 준비 / 바인딩 / 실행
			try {
				String query="";
				query += " select  person_id,";
				query += "  	   name,";
				query += " 		   hp,";
				query += "         company";
				query += " from person";
				query += " WHERE person_id=?";
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, personId);
				rs = pstmt.executeQuery();
			
			
				while (rs.next()) {
					int personID = rs.getInt("person_id");
					String name = rs.getString("name");
					String hp = rs.getString("hp");
					String company = rs.getString("company");
					phoneVo =new PhoneVo(personID,name,hp,company);
					//받은 주소를 넣음
				}
			
			}catch(SQLException e){
				
			}
			close();
			return phoneVo;
		}
}
