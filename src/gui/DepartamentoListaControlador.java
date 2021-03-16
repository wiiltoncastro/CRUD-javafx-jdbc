package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidades.Departamento;
import model.servicos.DepartamentoServico;

public class DepartamentoListaControlador implements Initializable{

	private DepartamentoServico departamentoservico;
	
	//atributos
	@FXML
	private Button botaoNovo;
	
	@FXML
	private TableView<Departamento> tableViewDepartamentos;
	
	@FXML
	private TableColumn<Departamento, Integer> tableColunaId;
	
	@FXML
	private TableColumn<Departamento, String> tableColunaNome;
	
	private ObservableList<Departamento> obsLista;
	
	//set
	public void setDepartamentoservico(DepartamentoServico departamentoservico) {
		this.departamentoservico = departamentoservico;
	}
	
	//metodos
	@FXML
	public void onBotaoNovoAction(ActionEvent evento) {
		Stage parentStage = Utils.palcoAtual(evento);
		criarFormatoDeDialogo("/gui/DepartamentoFormato.fxml", parentStage);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializeNodes();
	}

	private void inicializeNodes() {
		tableColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		Stage stage = (Stage)Main.getCenaPrincipal().getWindow();
		tableViewDepartamentos.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void atualizarTableView() {
		if(departamentoservico == null) {
			throw new IllegalStateException("Serviço era nulo");
		}
		List<Departamento> listaDepartamento = departamentoservico.encontrarTodos();
		obsLista = FXCollections.observableArrayList(listaDepartamento);
		tableViewDepartamentos.setItems(obsLista);
	}
	
	private void criarFormatoDeDialogo(String nomeAbsoluto, Stage parentStage) {
		try {
			FXMLLoader carregador = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane painel = carregador.load();
			
			Stage dialogoPalco = new Stage();
			dialogoPalco.setTitle("Dados do Departamento");
			dialogoPalco.setScene(new Scene(painel));
			dialogoPalco.setResizable(false);
			dialogoPalco.initOwner(parentStage);
			dialogoPalco.initModality(Modality.WINDOW_MODAL);
			dialogoPalco.showAndWait();
					
		}
		catch(IOException e) {
			Alertas.showAlert("IO Exceção", "Error! Carregando Janela", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
