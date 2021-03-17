package model.servicos;

import java.util.List;

import model.dao.DaoFabrica;
import model.dao.VendedorDao;
import model.entidades.Vendedor;

public class VendedorServico {

	private VendedorDao dao = DaoFabrica.criarVendedorDao();
	
	public List<Vendedor> encontrarTodos() {
		return dao.acharTodos();
	}
	
	public void salvarOuAtualizar(Vendedor dep) {
		if(dep.getId() == null) {
			dao.inserir(dep);
		}
		else {
			dao.atualizar(dep);
		}
	}
	
	public void remover(Vendedor dep) {
		dao.deletePorId(dep.getId());	
	}
	
}
