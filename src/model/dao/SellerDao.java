package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

//Implementação do Dao de Seller

public interface SellerDao {
	
		void insert(Seller obj); // insere no banco de dados o objeto enviado como entrada
		void update(Seller obj);
		void deleteById(Integer id);
		Seller findById(Integer id);  // Consulta no BD o objeto que tem o id informado. Retorna um Department se exitir o id, ou nulo se não existir
		List<Seller> findAll();  // Retorna uma lista com todos os departamentos
		List<Seller> findByDepartment(Department department);
}


