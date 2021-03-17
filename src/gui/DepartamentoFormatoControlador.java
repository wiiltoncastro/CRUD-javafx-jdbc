package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alertas;
import gui.util.Restricoes;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entidades.Departamento;
import model.servicos.DepartamentoServico;

public class DepartamentoFormatoControlador implements Initializable {

	//dependencias
	private Departamento entidade;
	private DepartamentoServico servico;
	
	//atributos
	@FXML
	private Button botaoSalvar;
	@FXML
	private Button botaoCancelar;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtId;
	@FXML
	private Label labelErroNome;
	
	//set
	public void setDepartamento(Departamento entidade) {
		this.entidade = entidade;
	}

	public void setDepartamentoServico(DepartamentoServico servico) {
		this.servico = servico;
	}

	//metodos
	@FXML
	public void onBotaoSalvarAction(ActionEvent evento) {
		if (entidade == null) {
			throw new IllegalStateException("Entidade estava nula");
		}
		if (servico == null) {
			throw new IllegalStateException("Servico estava nula");
		}
		try {
			entidade = getDadoFormulario();
			servico.salvarOuAtualizar(entidade);
			Utils.palcoAtual(evento).close();
		}
		catch (DbException e) {
			Alertas.showAlert("Erro salvando Objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private Departamento getDadoFormulario() {
		Departamento dep = new Departamento();
		
		dep.setId(Utils.tentarConverterParaInteiro(txtId.getText()));
		dep.setNome(txtNome.getText());
		
		return dep;
	}

	@FXML
	public void onBotaoCancelarAction(ActionEvent evento) {
		Utils.palcoAtual(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();
	}
	
	public void inicializarNodes() {
		Restricoes.setTextFieldInteger(txtId);
		Restricoes.setTextFieldMaxLength(txtNome, 30);
	}

	public void atualizarDadosFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade era nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
	}
}
