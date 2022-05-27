package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {
	
	void insert(Seller seller);
	void update(Seller seller);
	void delete(Integer id);
	Seller findById(Integer id);
	List<Seller> findAll();
	List<Seller> findByDepartment(Integer departament_id);
	
}
