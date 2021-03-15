package model.servicos;

import java.util.ArrayList;
import java.util.List;

import model.entidades.Departamento;

public class DepartamentoServico {

	public List<Departamento> encontrarTodos() {
		//Dados MOCK
		List<Departamento> lista = new ArrayList<>();
		lista.add(new Departamento(1, "Livros"));
		lista.add(new Departamento(2, "Computadores"));
		lista.add(new Departamento(3, "Eletronicos"));
		return lista;
	}
}
