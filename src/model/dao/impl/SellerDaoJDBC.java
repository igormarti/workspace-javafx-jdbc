package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller seller) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = this.conn.prepareStatement(
					"INSERT INTO seller"
					+" (Name, Email, BirthDate, BaseSalary, DepartmentId)"
					+" VALUES(?, ?, ?, ?, ?)"
					,PreparedStatement.RETURN_GENERATED_KEYS
			);
			
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, new Date( seller.getBirthDate().getTime() ) );
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartament().getId());
			
			int rows = st.executeUpdate();
			
			if(rows < 1) {
				throw new DBException("Error to the try save seller.");
			}
			
			rs = st.getGeneratedKeys();
			
			if(rs.next()) {
				seller.setId(rs.getInt(1));
			}
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public void update(Seller seller) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = this.conn.prepareStatement(
					"UPDATE seller"
					+" SET Name=?, Email=?, BirthDate=?, BaseSalary=?, DepartmentId=?"
					+" WHERE Id= ?"
			);
			
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, new Date( seller.getBirthDate().getTime() ) );
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartament().getId());
			st.setInt(6, seller.getId());
			
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
					"DELETE FROM seller"
					+" WHERE Id= ?"
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
	public Seller findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = this.conn.prepareStatement(
					"SELECT s.*,  d.name as depName FROM seller s"
					+ " INNER JOIN department d ON s.DepartmentId = d.Id "
					+ " WHERE s.Id = ? ");
			st.setInt(1, id);
			
			rs = st.executeQuery();
		
			if(rs.next()) {
				
				Department departament = new Department(rs.getInt("DepartmentId"), rs.getString("depName"));
				
				return new Seller(id, rs.getString("Name"), rs.getString("Email"), 
								 rs.getDate("BirthDate"), rs.getDouble("BaseSalary"), departament);
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
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = this.conn.prepareStatement(
					"SELECT s.*, d.Name depName FROM seller s"
					+ " INNER JOIN department d ON s.DepartmentId = d.Id "
					+ " ORDER BY Name");
			
			rs = st.executeQuery();
		
			List<Seller> sellerList = new ArrayList<>(); 
			Map<Integer, Department> mapDepartment = new HashMap<>();
			
			while(rs.next()) {
				
				Department dep = mapDepartment.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = new Department(
											rs.getInt("DepartmentId"), 
											rs.getString("depName")
										);
					
					mapDepartment.put(dep.getId(), dep);
				}
				
				sellerList.add(new Seller(
									rs.getInt("Id"), rs.getString("Name"), 
									rs.getString("Email"), rs.getDate("BirthDate"), 
									rs.getDouble("BaseSalary"), dep
							));	
			}
			
			return sellerList;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Integer department_id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = this.conn.prepareStatement(
					"SELECT s.*, d.Name depName FROM seller s"
					+ " INNER JOIN department d ON s.DepartmentId = d.Id "
					+ " WHERE s.DepartmentId = ? "
					+ " ORDER BY Name");
			st.setInt(1, department_id);
			
			rs = st.executeQuery();
		
			List<Seller> sellerList = new ArrayList<>(); 
			Map<Integer, Department> mapDepartment = new HashMap<>();
			
			while(rs.next()) {
				
				Department dep = mapDepartment.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					 dep = new Department(
											rs.getInt("DepartmentId"), 
											rs.getString("depName")
										);
					
					mapDepartment.put(dep.getId(), dep);
				}
				
				sellerList.add(new Seller(
									rs.getInt("Id"), rs.getString("Name"), 
									rs.getString("Email"), rs.getDate("BirthDate"), 
									rs.getDouble("BaseSalary"), dep
							));	
			}
			
			return sellerList;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
