package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.servicos.DepartamentoServico;
import model.servicos.VendedorServico;

public class JanelaPrincipalControlador implements Initializable{

	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	public void onMenuItemVendedorAction() {
		carregarJanela("/gui/JanelaVendedorLista.fxml", (VendedorListaControlador controlador) -> {
			controlador.setVendedorservico(new VendedorServico());
			controlador.atualizarTableView();
		});
	}
	
	@FXML
	public void onMenuItemDepartamentoAction() {
		carregarJanela("/gui/JanelaDepartamentoLista.fxml", (DepartamentoListaControlador controlador) -> {
			controlador.setDepartamentoservico(new DepartamentoServico());
			controlador.atualizarTableView();
		});
	}
	
	@FXML
	public void onMenuItemSobreAction() {
		carregarJanela("/gui/JanelaSobre.fxml", x -> {});
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}

	private synchronized <T> void carregarJanela(String nomeAbsoluto, Consumer<T> iniciandoAcao) {
		
		try {
			FXMLLoader carregador = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			VBox novoVBox = carregador.load();
			
			Scene cenaPrincipal = Main.getCenaPrincipal();
			VBox vBoxPrincipal = ((VBox)((ScrollPane)cenaPrincipal.getRoot()).getContent());
			
			Node menuPrincipal = vBoxPrincipal.getChildren().get(0);
			vBoxPrincipal.getChildren().clear();
			vBoxPrincipal.getChildren().add(menuPrincipal);
			vBoxPrincipal.getChildren().addAll(novoVBox.getChildren());
			
			T controlador = carregador.getController();
			iniciandoAcao.accept(controlador);
			
			
		} catch(IOException e) {
			Alertas.showAlert("IO Exceção", "Erro ao carregar tela", e.getMessage(), AlertType.ERROR);
		}
		
	}
	
}
