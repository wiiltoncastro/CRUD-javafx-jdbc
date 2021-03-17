package model.excecoes;

import java.util.HashMap;
import java.util.Map;

public class ExcecaoValidacao extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private Map<String, String> erros = new HashMap<>();
	
	public ExcecaoValidacao(String msg) {
		super(msg);
	}

	public Map<String, String> getErros() {
		return erros;
	}
	
	public void adicionarErro(String nomeCampo, String erroMensagem) {
		erros.put(nomeCampo, erroMensagem);
	}
	
}
