package fxKirjat;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import kirjasto.Kirja;
import kirjasto.Kirjakokoelma;
import kirjasto.SailoException;
import kirjasto.Liima;
import kirjasto.Kirjailijat;
import kirjasto.Kirjailija;

/**
 * @author antontuominen
 * @version 11 Feb 2020
 * @version 19 Apr 2020
 * Luokka kirjan tietojen muokkaukseen liittyvien tapahtumien käsittelyyn
 */
public class MuokkausController implements ModalControllerInterface<Liima>, Initializable{
    
    
    @FXML private TextField editNimi;

    @FXML private TextField editKustantaja;
    @FXML private TextField editKieli; 
    @FXML private TextField editVuosi; 
    @FXML private TextField editSivumaara;
    @FXML private TextField editArvio;
    @FXML private ScrollPane panelKirja;
    @FXML private GridPane gridKirja;
    @FXML private Label labelVirhe;
    @FXML private ComboBoxChooser<String> editKirjailija;

    @FXML private void handleTallenna() {
        tallenna();
        
    }
    
    @FXML
    private void handleLisaaKirjailija() {
        lisaaKirjailija();
    }

    
    @FXML private void handlePeruuta() {
        
        ModalController.closeStage(labelVirhe);
    }
    
    // ================================================================
    
    
    private Liima liima;
    
    
    private Kirja apuKirja;
    private Kirjakokoelma kirjakokoelma;
    private Kirjailijat tmpKirjailijat;
    
    private int tmpkir;
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
       tyhjenna();  
    }
    
    
    @Override
    public void setDefault(Liima oletus) {
        liima = oletus;
        apuKirja = oletus.getKirja();
        kirjakokoelma = oletus.getKokoelma();
        tmpKirjailijat = kirjakokoelma.annaKirjailijat();
       
        tmpkir = oletus.getKirja().getKirjailijaId();
        
        naytaKirja(apuKirja);
    }
    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        editNimi.requestFocus();
    }

    /**
     * Tallennetaan kirja kokoelmaan ja tyhjennetään
     * varoitukset
     */
    private void tallenna() {
        
        tyhjennaVaroitukset();
        if(!tarkista()) return;
       
        apuKirja.setNimi(editNimi.getText());
        apuKirja.setKirjailijaId(tmpKirjailijat.getId(editKirjailija.getSelectedText()));
     
        apuKirja.setKustantaja(editKustantaja.getText());
        apuKirja.setKieli(editKieli.getText());
        
        apuKirja.setVuosi(Integer.parseInt(editVuosi.getText()));
        apuKirja.setSivumaara(Integer.parseInt(editSivumaara.getText()));
        apuKirja.setArvio(Integer.parseInt(editArvio.getText()));
        
        liima.set(apuKirja);
        kirjakokoelma.set(tmpKirjailijat);
        try {
            kirjakokoelma.tallenna();
            
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
           
        }
        ModalController.closeStage(labelVirhe);
        
    }

    /**
     * Lisätään kirjailija kokoelmaan
     */
    private void lisaaKirjailija() {
        boolean palataanko = false;
        String nimi = Dialogs.showInputDialog("Anna kirjailijan nimi", "");
        for(Kirjailija kir : kirjakokoelma.annaKirjailijat()) {
            if(kir.getKirjailijanNimi().equalsIgnoreCase(nimi)) {
                Dialogs.showMessageDialog("Olet lisännyt kirjailijan " + nimi + 
                        " aikaisemmin kokoelmaan!\n" + "Valitse se siis listasta!",
                        dlg -> dlg.getDialogPane().setPrefWidth(400));
                palataanko = true;
            }  
        }
        if (nimi == null || palataanko)
            return;
        Kirjailija haru = new Kirjailija(nimi);
        haru.rekisteroi();
 
        tmpkir = tmpKirjailijat.lisaa(haru);
        setComboBox(editKirjailija, annaKirjailijat());
    }
    
    
    /**
     * ComboBoxChooseria varten tehty
     * @return kaikki kirjailijat, kirjan kirjailija ensimmäisenä
     */
    public String annaKirjailijat() {
        String kirjailija = tmpKirjailijat.annaKirjailija(tmpkir).getKirjailijanNimi();
        StringBuilder sb = new StringBuilder(kirjailija);
        // Muokkaus ei toimi jos kirjailijoita ei ole
        if (kirjailija.equals(""))
            sb.append("null"); 
        sb.append("\n");

        var iterator = tmpKirjailijat.iterator();
        for (int i = 0; i < tmpKirjailijat.getLkm(); i++) {
            String nyk = iterator.next().getKirjailijanNimi();
            if (!nyk.equals(kirjailija))
                sb.append(nyk).append("\n");
        }
        return sb.toString();
    }

    /**
     * Päivittää ComboBoxChooserin niin että valittu on ekana
     * @param lista merkkijono kohteet eroteltu rivinvaihdoin ja valittu ekana
     * @param kentta päivitettävä comboboxchooser
     */
    public void setComboBox(ComboBoxChooser<String> kentta, String lista) {
        kentta.setRivit(lista);
        if (kentta.getSelectedText().equals("null"))
            kentta.setRivit(kentta.getRivit().replace("null", "Ei valittu"));
    }

    
    /**
     * Palautetaan komponentin id:stä saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mikä arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna 
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }

    /**
     * Näytetään jäsenen tiedot TextField komponentteihin
     * @param kirja näytettävä kirja
     */
    public void naytaKirja( Kirja kirja) {
        if (kirja == null)
            return;
        editNimi.setText(kirja.getKirjanNimi());
        setComboBox(editKirjailija, annaKirjailijat());
        editKustantaja.setText(kirja.getKustantaja());
        editKieli.setText("" + kirja.getKieli());
        editVuosi.setText("" + kirja.getVuosi());
        editSivumaara.setText("" + kirja.getSivumaara());
        editArvio.setText("" + kirja.getArvio());
        
    }
    
    
    /**
     * Tyhjentään tekstikentät 
     */
    public void tyhjenna() {
        editNimi.setText("");
        editKirjailija.setPromptText("");
        editKustantaja.setPromptText("");
        editVuosi.setText("");
        editKieli.setText("");
        editSivumaara.setText("");
        editArvio.setText("");
    }
    
    /**
     * Tarkistetaan käyttämän antamat syötteet
     * ennen kuin tallennetaan
     * @return voidaanko tallentaa vai ei
     */
    private boolean tarkista() {
        boolean boo = tarkistaNimi();
        if(!boo) return boo;
        
        boo = tarkistaVuosi();
        if(!boo) return boo;
        
        boo = tarkistaSivumaara();
        if(!boo) return boo;
        
        boo = tarkistaArvio();
        if(!boo) return boo;
        return true;
    }
    /**
     * Tarkistetaan antoiko käyttäjä vuoden numeroina vai ei
     * @return onko näin vai ei
     */
    private boolean tarkistaVuosi() {
        Pattern pattern = Pattern.compile("^\\d{4}");
        Matcher m = pattern.matcher(editVuosi.getText());
        boolean b = m.matches();
        if(!b) {
            virheKentta("Anna vuosi numeroina", editVuosi);
            return b;
        }
        
        return b;
    }
    /**
     * Tarkistetaan antoiko käyttäjä sivumäärän numeroina vai ei
     * @return onko näin vai ei
     */
    private boolean tarkistaSivumaara() {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher m = pattern.matcher(editSivumaara.getText());
        boolean b = m.matches();
        if(!b) {
            virheKentta("Anna sivumäärä numeroina", editSivumaara);
            return b;
        }
        
        return b;
    }
    /**
     * Tarkistetaan antoiko käyttäjä arvion asteikolla 1-5
     * @return antoiko käyttäjä arvion oikein vai
     */
    private boolean tarkistaArvio() {
        Pattern pattern = Pattern.compile("^(?:[1-5]|0[1-5]|10)$");
        Matcher m = pattern.matcher(editArvio.getText());
        boolean b = m.matches();
        if(!b) {
            virheKentta("Anna arvio asteikolla 1-5", editArvio);
            return b;
        }
        
        return b;
    }
    /**
     * Tarkistetaan antoiko käyttäjä nimen
     * @return annettiinko nimi
     */
    private boolean tarkistaNimi() {
        if(editNimi.getText().length() == 0 ) {
            virheKentta("Anna kirjalle nimi!", editNimi);
            return false; 
        }
        return true;
    }
    
    
    /**
     * Näyttää virheen
     * @param virhe virheilmoitus
     * @param kentta kenttä jossa on virhe
     */
    private void virheKentta(String virhe, TextField kentta) {
        labelVirhe.setTextFill(Color.RED);
        labelVirhe.setText(virhe);
        kentta.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
    }
    /**
     * Tyhjennetään varoituskentät(otetaan punaset reunat pois)
     */
    private void tyhjennaVaroitukset() {
        editVuosi.setStyle(null);
        editSivumaara.setStyle(null);
        editArvio.setStyle(null);
    }

    @Override
    public Liima getResult() {
        return liima;
    }

}
