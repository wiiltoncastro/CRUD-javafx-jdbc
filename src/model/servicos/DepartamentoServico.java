package model.servicos;

import java.util.List;

import model.dao.DaoFabrica;
import model.dao.DepartamentoDao;
import model.entidades.Departamento;

public class DepartamentoServico {

	private DepartamentoDao dao = DaoFabrica.criarDepartamentoDao();
	
	public List<Departamento> encontrarTodos() {
		return dao.acharTodos();
	}
	
	public void salvarOuAtualizar(Departamento dep) {
		if(dep.getId() == null) {
			dao.inserir(dep);
		}
		else {
			dao.atualizar(dep);
		}
	}
	
	public void remover(Departamento dep) {
		dao.deletePorId(dep.getId());	
	}
	
}
