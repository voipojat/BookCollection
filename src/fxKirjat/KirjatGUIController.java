package fxKirjat;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import javafx.fxml.FXML;

/**
 * @author antontuominen
 * @version 28 Jan 2020
 * Luokka käyttöliittymän tapahtumien hoitamiseksi
 */
public class KirjatGUIController {
	//
    @FXML private void handlePoistaKirja() {
        Dialogs.showMessageDialog("Vielä ei osata poistaa kirjaa!");
    }
    
    @FXML private void handleLisaaKirja() {
        ModalController.showModal(KirjatGUIController.class.getResource("LisaaKirja.fxml"), "Kirja", null, "");
    }
    
    @FXML private void handleMuokkaaKirjaa() {
        ModalController.showModal(KirjatGUIController.class.getResource("MuokkaaKirjaa.fxml"), "Kirja", null, "");
    }
    
    @FXML private void handleOhje() {
        ModalController.showModal(KirjatGUIController.class.getResource("Ohje.fxml"), "Ohje", null, "");
    }
    
    @FXML private void handleTallenna() {
        Dialogs.showMessageDialog("Ei osata vielä tallentaa!");
    }
    
    @FXML private void handlePeruuta() {
        Dialogs.showMessageDialog("Ei osata vielä peruuttaa!");
    }
    @FXML private void handleAvaa() {
        Dialogs.showMessageDialog("Ei osata vielä avata uutta kokoelmaa!");
    }
}
