/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.CoppiaTipologie;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<LocalDate> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<CoppiaTipologie> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	if(boxCategoria.getValue()!=null && boxGiorno.getValue()!=null) {
    		model.creaGrafo(boxCategoria.getValue(), boxGiorno.getValue());
    		if(model.getArchiStampa()!=null) {
    			txtResult.setText("Archi con peso inferiore del mediano: ");
	    		for(CoppiaTipologie c : model.getArchiStampa())
	    			txtResult.appendText("\n" + c.toString());
	    		boxArco.getItems().clear();
	    		if(model.getArchiStampa().size()>0)
	    			boxArco.getItems().addAll(model.getArchiStampa());
    		} else
    			txtResult.setText("Grafo senza archi");
    	} else 
    		txtResult.setText("Seleziona categoria e giorno");
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	if(boxArco.getValue()!=null) {
    		txtResult.setText("Percorso: ");
    		for(String s : model.percorso(boxArco.getValue()))
    			txtResult.appendText("\n" + s);
    	} else {
    		txtResult.setText("Seleziona un arco");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxCategoria.getItems().addAll(model.getCategorie());
    	boxGiorno.getItems().addAll(model.getGiorni());
    }
}
