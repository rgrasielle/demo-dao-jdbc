package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

// Fábrica auxiliar responsável por instanciar os Daos (por meio de operações estáticas)

public class DaoFactory {  
	
	// Instancia uma implementação
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection()); 
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}

}
