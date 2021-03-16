package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public void onBotaoNovoAction() {
		System.out.println("onBotaoNovoAction");
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
}
