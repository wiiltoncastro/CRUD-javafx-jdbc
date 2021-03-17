package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.MudancaDadosListener;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidades.Departamento;
import model.servicos.DepartamentoServico;

public class DepartamentoListaControlador implements Initializable, MudancaDadosListener {

	private DepartamentoServico departamentoservico;

	// atributos
	@FXML
	private Button botaoNovo;

	@FXML
	private TableView<Departamento> tableViewDepartamentos;

	@FXML
	private TableColumn<Departamento, Integer> tableColunaId;

	@FXML
	private TableColumn<Departamento, String> tableColunaNome;

	@FXML
	private TableColumn<Departamento, Departamento> tableColunaEditar;

	@FXML
	private TableColumn<Departamento, Departamento> tableColunaRemover;

	private ObservableList<Departamento> obsLista;

	// set
	public void setDepartamentoservico(DepartamentoServico departamentoservico) {
		this.departamentoservico = departamentoservico;
	}

	// metodos
	@FXML
	public void onBotaoNovoAction(ActionEvent evento) {
		Stage parentStage = Utils.palcoAtual(evento);
		Departamento dep = new Departamento();
		criarFormatoDeDialogo(dep, "/gui/DepartamentoFormato.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializeNodes();
	}

	private void inicializeNodes() {
		tableColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		Stage stage = (Stage) Main.getCenaPrincipal().getWindow();
		tableViewDepartamentos.prefHeightProperty().bind(stage.heightProperty());
	}

	public void atualizarTableView() {
		if (departamentoservico == null) {
			throw new IllegalStateException("Serviço era nulo");
		}
		List<Departamento> listaDepartamento = departamentoservico.encontrarTodos();
		obsLista = FXCollections.observableArrayList(listaDepartamento);
		tableViewDepartamentos.setItems(obsLista);
		iniciarButaoEditar();
		iniciarButaoRemover();
	}

	private void criarFormatoDeDialogo(Departamento dep, String nomeAbsoluto, Stage parentStage) {
		try {
			FXMLLoader carregador = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane painel = carregador.load();

			DepartamentoFormatoControlador controlador = carregador.getController();
			controlador.setDepartamento(dep);
			controlador.setDepartamentoServico(new DepartamentoServico());
			controlador.inscreverMudancaDadosListener(this);
			controlador.atualizarDadosFormulario();

			Stage dialogoPalco = new Stage();
			dialogoPalco.setTitle("Dados do Departamento");
			dialogoPalco.setScene(new Scene(painel));
			dialogoPalco.setResizable(false);
			dialogoPalco.initOwner(parentStage);
			dialogoPalco.initModality(Modality.WINDOW_MODAL);
			dialogoPalco.showAndWait();

		} catch (IOException e) {
			Alertas.showAlert("IO Exceção", "Error! Carregando Janela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onMudancaDados() {
		atualizarTableView();
	}

	private void iniciarButaoEditar() {
		tableColunaEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColunaEditar.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Departamento obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarFormatoDeDialogo(obj, "/gui/DepartamentoFormato.fxml", Utils.palcoAtual(event)));
			}
		});
	}

	private void iniciarButaoRemover() {
		tableColunaRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColunaRemover.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Departamento obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removerEntidade(obj));
			}
		});
	}

	private void removerEntidade(Departamento dep) {
		Optional<ButtonType> resultado = Alertas.showConfirmation("Confirmação",
				"Tem certeza de que deseja excluir o Departamento permanentemente?");

		if (resultado.get() == ButtonType.OK) {
			if (departamentoservico == null) {
				throw new IllegalStateException("Serviço era nulo");
			}
			try {
				departamentoservico.remover(dep);
				atualizarTableView();
			} catch (DbIntegrityException e) {
				e.printStackTrace();
				Alertas.showAlert("Erro removendo Departamento", null, e.getMessage(), AlertType.ERROR);
			}
		}

	}

}
