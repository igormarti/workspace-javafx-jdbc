package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {

	private static List<Department> list;
	
	public static List<Department> findAll(){
		list = new ArrayList<>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		return list;
	}

}
;