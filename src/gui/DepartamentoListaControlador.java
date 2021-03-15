package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entidades.Departamento;

public class DepartamentoListaControlador implements Initializable{

	
	@FXML
	private Button botaoNovo;
	@FXML
	private TableView<Departamento> tableViewDepartamentos;
	@FXML
	private TableColumn<Departamento, Integer> tableColunaId;
	@FXML
	private TableColumn<Departamento, String> tableColunaNome;
	
	
	@FXML
	public void onBotaoNovoAction() {
		System.out.println("onBotaoNovoAction");
	}
	
	//metodos
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

}
