package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage palcoAtual(ActionEvent evento) {
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();
	}
	
	public static Integer tentarConverterParaInteiro(String string) {
		try {
			return Integer.parseInt(string);
		}
		catch(NumberFormatException e) {
			return null;
		}
	}
	
}
