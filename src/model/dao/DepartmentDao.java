package model.dao;

import java.util.List;

import model.entities.Department;

// Implementação do Dao de Department

public interface DepartmentDao {
	
	void insert(Department obj); // insere no banco de dados o objeto enviado como entrada
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id);  // Consulta no BD o objeto que tem o id informado. Retorna um Department se exitir o id, ou nulo se não existir
	List<Department> findAll();  // Retorna uma lista com todos os departamentos
}


