package fxKirjat;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;

/**
 * @author antontuominen
 * @version 11 Feb 2020
 * Luokka kirjan tietojen muokkaukseen liittyvien tapahtumien käsittelyyn
 */
public class MuokkausController implements ModalControllerInterface<String>{
  
    @FXML private void handleTallenna() {
        Dialogs.showMessageDialog("Ei osata vielä tallentaa!");
    }
    
    @FXML private void handlePeruuta() {
        Dialogs.showMessageDialog("Ei osata vielä peruuttaa!");
    }
    
    @Override
    public String getResult() {
        return "";
    }
    
    @Override
    public void setDefault(String oletus) {
        //
    }
  
    @Override
    public void handleShown() {
          //
    }

}
