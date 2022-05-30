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
	
	public static void saveOrUpdate(Department department) {
		if(department.getId()==null) {
			departmentDao.insert(department);
		} else {
			departmentDao.update(department);
		}
	}

	public static void remove(Department department) {
		departmentDao.delete(department.getId());
	}
}
;