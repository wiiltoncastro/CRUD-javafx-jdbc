package model.dao;

import java.util.List;

import model.entidades.Departamento;

public interface DepartamentoDao {

	void inserir(Departamento departamento);
	void atualizar(Departamento departamento);
	void deletePorId(Integer id);
	Departamento acharPorId(Integer id);
	List<Departamento> acharTodos();
}
