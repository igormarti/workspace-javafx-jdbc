package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface DepartmentDao {

	void insert(Department departament);
	void update(Department departament);
	void delete(Integer id);
	Department findById(Integer id);
	Department findBySeller(Seller seller);
	List<Department> findAll();
}
