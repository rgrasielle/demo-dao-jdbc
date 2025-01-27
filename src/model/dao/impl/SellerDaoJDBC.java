package model.dao.impl;  // Pacote onde está a implementação do Dao

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	// Implementação dos métodos
	
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();  // executa e coloca na variável rowsAffected
			 
			if (rowsAffected > 0) {  // se tiver inserido
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {  // if se a inserção for de só 1 dado, while se for de mais
					int id = rs.getInt(1);  // pega o id gerado
					obj.setId(id);
				} 	
				DB.closeResultSet(rs);
			} 
			else {  // se a inserção foi feita e nenhuma linha foi afetada
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			
		}
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}
	
	// Implementação do método para retornar um vendedor por ID
	@Override
	public Seller findById(Integer id) {  
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if (rs.next()) {  // se veio algum resultado na consulta
				Department dep = instantiateDepartment(rs); // chama o método para instanciar o departamento		
				Seller obj = instantiateSella(rs, dep);
				return obj;
			}
			return null;  // se a consulta não retornou nenhum registro
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {  // para fechar os recursos rs e st
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	// Método para instanciar o vendedor
	private Seller instantiateSella(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id")); // pega o Id do vendedor
		obj.setName(rs.getString("Name"));  // pega o nome do vendedor
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep); // associa com o obj Department instanciado acima (dep)
		return obj;
	}

	// Método para instanciar o departamento
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));  // pega o Id do departamento
		dep.setName(rs.getString("DepName"));  // pega o nome do departamento
		return dep;
	}

	// Busca todos os vendedores e ordena por nome
	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					// Query do SQL:
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
		
			rs = st.executeQuery(); // executar a query
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); // guarda qualquer departamento que for instanciado
			
			while (rs.next()) {  // para cada valor do resultSet
				
				// Testar se o departamento já existe (se existir, será reaproveitado)
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {  // se não existir um departamento no map, retornará null
					dep = instantiateDepartment(rs); // chama o método para instanciar o departamento
					map.put(rs.getInt("DepartmentId"), dep); // salva o departamento dentro do map
				}
					
				Seller obj = instantiateSella(rs, dep); // chama o método para instanciar o vendedor
				list.add(obj); // adiciona o vendedor na lista
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {  // para fechar os recursos rs e st
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					// Query do SQL:
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, department.getId()); // Configurar o valor da ?
		
			rs = st.executeQuery(); // executar a query
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); // guarda qualquer departamento que for instanciado
			
			while (rs.next()) {  // para cada valor do resultSet
				
				// Testar se o departamento já existe (se existir, será reaproveitado)
				Department dep = map.get(rs.getInt("DepartmentId"));  
				
				if (dep == null) {  // se não existir um departamento no map, retornará null
					dep = instantiateDepartment(rs); // chama o método para instanciar o departamento
					map.put(rs.getInt("DepartmentId"), dep); // salva o departamento dentro do map
				}
					
				Seller obj = instantiateSella(rs, dep); // chama o método para instanciar o vendedor
				list.add(obj); // adiciona o vendedor na lista
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {  // para fechar os recursos rs e st
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
