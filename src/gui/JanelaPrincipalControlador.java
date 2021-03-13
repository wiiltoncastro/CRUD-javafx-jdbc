package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class JanelaPrincipalControlador implements Initializable{

	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println("onMenuItemVendedorAction");
	}
	
	@FXML
	public void onMenuItemDepartamentoAction() {
		System.out.println("onMenuItemDepartamentoAction");
	}
	
	@FXML
	public void onMenuItemSobreAction() {
		carregarJanela("/gui/Sobre.fxml");
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}

	private synchronized void carregarJanela(String nomeAbsoluto) {
		
		try {
			FXMLLoader carregador = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			VBox novoVBox = carregador.load();
			
			Scene cenaPrincipal = Main.getCenaPrincipal();
			VBox vBoxPrincipal = ((VBox)((ScrollPane)cenaPrincipal.getRoot()).getContent());
			
			Node menuPrincipal = vBoxPrincipal.getChildren().get(0);
			vBoxPrincipal.getChildren().clear();
			vBoxPrincipal.getChildren().add(menuPrincipal);
			vBoxPrincipal.getChildren().addAll(novoVBox.getChildren());
			
			
		} catch(IOException e) {
			Alertas.showAlert("IO Exceção", "Erro ao carregar tela", e.getMessage(), AlertType.ERROR);
		}
		
	}
	
}
