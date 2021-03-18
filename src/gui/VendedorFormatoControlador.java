package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.MudancaDadosListener;
import gui.util.Alertas;
import gui.util.Restricoes;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entidades.Departamento;
import model.entidades.Vendedor;
import model.excecoes.ExcecaoValidacao;
import model.servicos.DepartamentoServico;
import model.servicos.VendedorServico;

public class VendedorFormatoControlador implements Initializable {

	// dependencias
	private Vendedor entidade;
	private VendedorServico servico;
	private DepartamentoServico departamentoServico;

	private List<MudancaDadosListener> listaMudancaDadosListeners = new ArrayList<>();

	// atributos
	@FXML
	private Button botaoSalvar;

	@FXML
	private Button botaoCancelar;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpDataNascimento;

	@FXML
	private TextField txtSalarioBase;

	@FXML
	private ComboBox<Departamento> comboBoxDepartamento;

	@FXML
	private Label labelErroNome;

	@FXML
	private Label labelErroEmail;

	@FXML
	private Label labelErroDataNascimento;

	@FXML
	private Label labelErroSalarioBase;

	private ObservableList<Departamento> obsLista;

	// set
	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}

	public void setServicos(VendedorServico servico, DepartamentoServico departamentoServico) {
		this.servico = servico;
		this.departamentoServico = departamentoServico;
	}

	public void inscreverMudancaDadosListener(MudancaDadosListener ouvinte) {
		listaMudancaDadosListeners.add(ouvinte);
	}

	// metodos
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
			notificarMudancaDadosListener();
			Utils.palcoAtual(evento).close();
		} catch (ExcecaoValidacao e) {
			setErroMensagem(e.getErros());
		} catch (DbException e) {
			Alertas.showAlert("Erro salvando Objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notificarMudancaDadosListener() {
		for (MudancaDadosListener ouvinte : listaMudancaDadosListeners) {
			ouvinte.onMudancaDados();
		}
	}

	private Vendedor getDadoFormulario() {
		Vendedor dep = new Vendedor();

		ExcecaoValidacao excecao = new ExcecaoValidacao("Erro Validação!");

		dep.setId(Utils.tentarConverterParaInteiro(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excecao.adicionarErro("nome", " O campo não pode ser vazio");
		}
		dep.setNome(txtNome.getText());

		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			excecao.adicionarErro("email", " O campo não pode ser vazio");
		}
		dep.setEmail(txtEmail.getText());
		
		if (dpDataNascimento.getValue() == null) {
			excecao.adicionarErro("dataNascimento", " O campo não pode ser vazio");
		}
		else {
			Instant instant = Instant.from(dpDataNascimento.getValue().atStartOfDay(ZoneId.systemDefault()));
			dep.setDataNascimento(Date.from(instant));
		}
		
		if (txtSalarioBase.getText() == null || txtSalarioBase.getText().trim().equals("")) {
			excecao.adicionarErro("salarioBase", " O campo não pode ser vazio");
		}
		dep.setSalarioBase(Utils.tentarConverterParaDouble(txtSalarioBase.getText()));
		
		dep.setDepartamento(comboBoxDepartamento.getValue());
		
		if (excecao.getErros().size() > 0) {
			throw excecao;
		}

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
		Restricoes.setTextFieldMaxLength(txtNome, 70);
		Restricoes.setTextFieldDouble(txtSalarioBase);
		Restricoes.setTextFieldMaxLength(txtEmail, 70);
		Utils.formatDatePicker(dpDataNascimento, "dd/MM/yyyy");
		
		inicializaComboBoxDepartamento();
	}

	public void atualizarDadosFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade era nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
		txtEmail.setText(entidade.getEmail());
		Locale.setDefault(Locale.US);
		txtSalarioBase.setText(String.format("%.2f", entidade.getSalarioBase()));
		if (entidade.getDataNascimento() != null) {
			dpDataNascimento.setValue(LocalDate.ofInstant(entidade.getDataNascimento().toInstant(), ZoneId.systemDefault()));
		}
		if (entidade.getDepartamento() == null) {
			comboBoxDepartamento.getSelectionModel().selectFirst();
		}
		else {
			comboBoxDepartamento.setValue(entidade.getDepartamento());
		}
	}

	public void carregarObjetosAssociados() {
		if (departamentoServico == null) {
			throw new IllegalStateException("Departamento estava nulo");
		}
		List<Departamento> lista = departamentoServico.encontrarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		comboBoxDepartamento.setItems(obsLista);
	}

	private void setErroMensagem(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		labelErroNome.setText((campos.contains("nome") ? erros.get("nome") : ""));
		labelErroEmail.setText((campos.contains("email") ? erros.get("email") : ""));
		labelErroDataNascimento.setText((campos.contains("dataNascimento") ? erros.get("dataNascimento") : ""));
		labelErroSalarioBase.setText((campos.contains("salarioBase") ? erros.get("salarioBase") : ""));
	}

	private void inicializaComboBoxDepartamento() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxDepartamento.setCellFactory(factory);
		comboBoxDepartamento.setButtonCell(factory.call(null));
	}

}
