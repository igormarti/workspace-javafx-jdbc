package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department departament) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = this.conn.prepareStatement(
					"INSERT INTO"
					+" department(Name)"
					+" VALUES(?)"
					, PreparedStatement.RETURN_GENERATED_KEYS
					);
			st.setString(1, departament.getName());
			
			int rows = st.executeUpdate();
			
			if(rows < 1) {
				throw new DBException("Error to the try create department");
			}
			
			rs = st.getGeneratedKeys();
			
			if(rs.next()) {
				departament.setId(rs.getInt(1));
			}
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public void update(Department departament) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = this.conn.prepareStatement(
					"UPDATE department"
					+" SET Name= ?"
					+" WHERE Id= ?"
					);
			st.setString(1, departament.getName());
			st.setInt(2, departament.getId());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public void delete(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = this.conn.prepareStatement(
					"DELETE FROM department WHERE Id= ?"
					);
			st.setInt(1, id);
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = this.conn.prepareStatement("SELECT * FROM department WHERE Id=?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if(rs.next()) {
				return new Department(rs.getInt("Id"), rs.getString("Name"));
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = this.conn.prepareStatement("SELECT * FROM department");
			
			rs = st.executeQuery();
			
			List<Department> departmentList = new ArrayList<>();
			
			while(rs.next()) {
				departmentList.add(new Department(rs.getInt("Id"), rs.getString("Name")));
			}
			
			return departmentList;
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public Department findBySeller(Seller seller) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = this.conn.prepareStatement(
					"SELECT d.* FROM department d"
					+" INNER JOIN seller s ON s.DepartmentId = d.Id"
					+" WHERE s.Id = ?"
					);
			st.setInt(1, seller.getId());
			rs = st.executeQuery();
			
			if(rs.next()) {
				return new Department(rs.getInt("Id"), rs.getString("Name"));
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
