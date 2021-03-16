package model.dao;

import db.DB;
import model.dao.implementacao.DepartamentoDaoJDBC;
import model.dao.implementacao.VendedorDaoJDBC;

public class DaoFabrica {

	public static VendedorDao criarVendedorDao() {
		return new VendedorDaoJDBC(DB.getConnection());
	}
	
	public static DepartamentoDao criarDepartamentoDao() {
		return new DepartamentoDaoJDBC(DB.getConnection());
	}
}
