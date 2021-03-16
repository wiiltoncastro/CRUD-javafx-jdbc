package model.dao;

import java.util.List;

import model.entidades.Departamento;
import model.entidades.Vendedor;

public interface VendedorDao {

	void inserir(Vendedor departamento);
	void atualizar(Vendedor departamento);
	void deletePorId(Integer id);
	Vendedor acharPorId(Integer id);
	List<Vendedor> acharTodos();
	List<Vendedor> acharPorDepartamento(Departamento departamento);
}
