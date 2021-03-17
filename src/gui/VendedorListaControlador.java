package gui;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entidades.Vendedor;
import model.servicos.VendedorServico;

public class VendedorListaControlador implements Initializable, MudancaDadosListener {

	private VendedorServico departamentoservico;

	// atributos
	@FXML
	private Button botaoNovo;

	@FXML
	private TableView<Vendedor> tableViewVendedors;

	@FXML
	private TableColumn<Vendedor, Integer> tableColunaId;

	@FXML
	private TableColumn<Vendedor, String> tableColunaNome;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColunaEditar;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColunaRemover;

	private ObservableList<Vendedor> obsLista;

	// set
	public void setVendedorservico(VendedorServico departamentoservico) {
		this.departamentoservico = departamentoservico;
	}

	// metodos
	@FXML
	public void onBotaoNovoAction(ActionEvent evento) {
		Stage parentStage = Utils.palcoAtual(evento);
		Vendedor dep = new Vendedor();
		criarFormatoDeDialogo(dep, "/gui/VendedorFormato.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializeNodes();
	}

	private void inicializeNodes() {
		tableColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		Stage stage = (Stage) Main.getCenaPrincipal().getWindow();
		tableViewVendedors.prefHeightProperty().bind(stage.heightProperty());
	}

	public void atualizarTableView() {
		if (departamentoservico == null) {
			throw new IllegalStateException("Serviço era nulo");
		}
		List<Vendedor> listaVendedor = departamentoservico.encontrarTodos();
		obsLista = FXCollections.observableArrayList(listaVendedor);
		tableViewVendedors.setItems(obsLista);
		iniciarButaoEditar();
		iniciarButaoRemover();
	}

	private void criarFormatoDeDialogo(Vendedor dep, String nomeAbsoluto, Stage parentStage) {
//		try {
//			FXMLLoader carregador = new FXMLLoader(getClass().getResource(nomeAbsoluto));
//			Pane painel = carregador.load();
//
//			VendedorFormatoControlador controlador = carregador.getController();
//			controlador.setVendedor(dep);
//			controlador.setVendedorServico(new VendedorServico());
//			controlador.inscreverMudancaDadosListener(this);
//			controlador.atualizarDadosFormulario();
//
//			Stage dialogoPalco = new Stage();
//			dialogoPalco.setTitle("Dados do Vendedor");
//			dialogoPalco.setScene(new Scene(painel));
//			dialogoPalco.setResizable(false);
//			dialogoPalco.initOwner(parentStage);
//			dialogoPalco.initModality(Modality.WINDOW_MODAL);
//			dialogoPalco.showAndWait();
//
//		} catch (IOException e) {
//			Alertas.showAlert("IO Exceção", "Error! Carregando Janela", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onMudancaDados() {
		atualizarTableView();
	}

	private void iniciarButaoEditar() {
		tableColunaEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColunaEditar.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarFormatoDeDialogo(obj, "/gui/VendedorFormato.fxml", Utils.palcoAtual(event)));
			}
		});
	}

	private void iniciarButaoRemover() {
		tableColunaRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColunaRemover.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
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

	private void removerEntidade(Vendedor dep) {
		Optional<ButtonType> resultado = Alertas.showConfirmation("Confirmação",
				"Tem certeza de que deseja excluir o Vendedor permanentemente?");

		if (resultado.get() == ButtonType.OK) {
			if (departamentoservico == null) {
				throw new IllegalStateException("Serviço era nulo");
			}
			try {
				departamentoservico.remover(dep);
				atualizarTableView();
			} catch (DbIntegrityException e) {
				Alertas.showAlert("Erro removendo Vendedor", null, e.getMessage(), AlertType.ERROR);
			}
		}

	}

}
