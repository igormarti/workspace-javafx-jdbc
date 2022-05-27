package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

	private static DepartmentDao departmentDao = DaoFactory.createDepartamentDao();
	
	public static List<Department> findAll(){
		return departmentDao.findAll();
	}

}
;