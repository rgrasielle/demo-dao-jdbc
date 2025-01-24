package model.dao;

import model.dao.impl.SellerDaoJDBC;

// Fábrica auxiliar responsável por instanciar os Daos (por meio de operações estáticas)

public class DaoFactory {  
	
	// Instancia uma implementação
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(); 
	}

}
