package fxKirjat;

import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import java.io.PrintStream;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import kirjasto.Kirjakokoelma;
import kirjasto.SailoException;
import kirjasto.Kirja;
import kirjasto.Kirjailija;
import java.util.Collection;


/**
 * @author antontuominen
 * @version 28 Jan 2020
 * @version 12.3.2020 - lisätty kirjojen käsittely
 * Luokka käyttöliittymän tapahtumien hoitamiseksi
 */
public class KirjatGUIController implements Initializable{
    
    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private ScrollPane panelKirja;
    @FXML private ListChooser<Kirja> chooserKirjat;
    @FXML private Label labelVirhe;
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();      
    }
    
    @FXML private void handleHakuehto() {
        if ( kirjaKohdalla != null )
            hae(kirjaKohdalla.getId()); 
    }

    @FXML private void handlePoistaKirja() {
        Dialogs.showMessageDialog("Vielä ei osata poistaa kirjaa!");
    }
    
    @FXML private void handleLisaaKirja() {
        uusiKirja();
    }
    
    @FXML private void handleMuokkaaKirjaa() {
        ModalController.showModal(KirjatGUIController.class.getResource("MuokkaaKirjaa.fxml"), "Kirja", null, "");
    }
    
    @FXML private void handleOhje() {
        ModalController.showModal(KirjatGUIController.class.getResource("Ohje.fxml"), "Ohje", null, "");
    }
    
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    @FXML private void handlePeruuta() {
        Dialogs.showMessageDialog("Ei osata vielä peruuttaa!");
    }
    @FXML private void handleAvaa() {
        avaa();
    }
    
    
    //===========================================================================
    
    private String kirjakokoelmanNimi = "Matin_kirjat";
    
    private Kirjakokoelma kirjakokoelma;

    private Kirja kirjaKohdalla;
    
    private TextArea areaKirja = new TextArea();
 
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa kirjojen tiedot.
     * Alustetaan myös kirjalistan kuuntelija 
     */
    protected void alusta() {
        panelKirja.setContent(areaKirja);
        areaKirja.setFont(new Font("Courier New", 12));
        panelKirja.setFitToHeight(true);
        
        chooserKirjat.clear();
        chooserKirjat.addSelectionListener(e -> naytaKirja());
    }
    private void setTitle(String title) {
        ModalController.getStage(hakuehto).setTitle(title);
    }
    
    
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }


    
    
    /**
     * Alustaa kirjakokoelman lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedosto josta kirjakokoelman tiedot luetaan
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    protected String lueTiedosto(String nimi) {
        kirjakokoelmanNimi = nimi;
        setTitle("Kirjakokoelma - " + kirjakokoelmanNimi);
        try {
            kirjakokoelma.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
     }
    
    
    /**
     * Kysytään tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
        String uusinimi = KirjakokoelmanNimiController.kysyNimi(null, kirjakokoelmanNimi);
        if (uusinimi == null) return false;
        lueTiedosto(uusinimi);
        return true;
    }

    /**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    private String tallenna() {
        try {
            kirjakokoelma.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    
    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }

    /**
     * Näyttää listasta valitun kirjan tiedot, tilapäisesti yhteen isoon edit-kenttään
     */
    protected void naytaKirja() {
        kirjaKohdalla = chooserKirjat.getSelectedObject();

        if (kirjaKohdalla == null) return;

        areaKirja.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaKirja)) {
            tulosta(os,kirjaKohdalla);  
        }
    }

    /**
     * Luodaan uusi kirja kokoelmaan
     */
    protected void uusiKirja() {
        Kirja uusi = new Kirja();
        uusi.rekisteroi();
        uusi.vastaaKafkaRannalla();
        
        lisaaKirjailija(uusi);
        
        try {
            kirjakokoelma.lisaa(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
        hae(uusi.getId());
    
    }
    
    /**
     * Lisätään kirjailija kirjalle ja lisätään se kirjailijoihin, jos kyseistä
     * kirjailijaa ei ole vielä kirjailijataulukossa
     * TODO tarkistetaan ANNETUSTA kirjailijan nimestä onko se jo 
     * lisätty kirjailijoihin, nyt käytössä vain tuo Murakami,
     * koska kaikki lisättävät kirjat toistaiseksi samoja
     * @param kirja jolle kirjailija lisätään
     */
    protected void lisaaKirjailija(Kirja kirja) {
        
        for(int i = 0; i < kirjakokoelma.getKirjailijat(); i++) {
            if(kirjakokoelma.annaKirjailija(i).getKirjailijanNimi().equalsIgnoreCase("Haruki Murakami")) {
                kirja.setKirjailijaId(kirjakokoelma.annaKirjailija(i).getKirjailijaId());
                return;
            }
        }
        int kirjanKirjailijaId = kirja.getKirjailijaId();
        Kirjailija haru = new Kirjailija(kirjanKirjailijaId);
        haru.vastaaHarukiMurakami(kirjanKirjailijaId);
        kirjakokoelma.lisaa(haru);
    }
    
    /**
     * Hakee kirjojen tiedot listaan
     * @param kirjaId kirjan id, joka aktivoidaan haun jälkeen
     */
    protected void hae(int kirjaId) {
        int k = cbKentat.getSelectionModel().getSelectedIndex();
        String ehto = hakuehto.getText(); 
        if (k > 0 || ehto.length() > 0)
            naytaVirhe(String.format("Ei osata hakea (kenttä: %d, ehto: %s)", k, ehto));
        else
            naytaVirhe(null);
        
        chooserKirjat.clear();

        int index = 0;
        Collection<Kirja> kirjat;
        try {
            //tässä vaiheessa hakee vielä kaikki kirjat
            kirjat = kirjakokoelma.etsi(ehto, k);
            int i = 0;
            for (Kirja kirja:kirjat) {
                if (kirja.getId() == kirjaId) index = i;
                chooserKirjat.add(kirja.getKirjanNimi(), kirja);
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Jäsenen hakemisessa ongelmia! " + ex.getMessage());
        }
        chooserKirjat.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää kirjan

    }


    /**
     * @param kirjakokoelma jota käytetään tässä käyttöliittymässä
     */
    public void setKirjakokoelma(Kirjakokoelma kirjakokoelma) {
        this.kirjakokoelma = kirjakokoelma;
        naytaKirja();
    }


    /**
     * Tulostaa kirjan tiedot
     * @param os tietovirta johon tulostetaan
     * @param kirja tulostettava kirja
     */
    public void tulosta(PrintStream os, final Kirja kirja) {
        os.println("----------------------------------------------");
        kirja.tulosta(os);
        os.println("----------------------------------------------");
        Kirjailija kirjailija = kirjakokoelma.annaKirjailija(kirja.getKirjailijaId());   
        kirjailija.tulosta(os); 
            
    }
    
    /**
     * Tulostaa listassa olevat kirjat tekstialueeseen
     * @param text alue johon tulostetaan
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki jäsenet");
            Collection<Kirja> kirjat = kirjakokoelma.etsi("", -1); 
            for (Kirja kirja:kirjat) { 
                tulosta(os, kirja);
                os.println("\n\n");
            }
        } catch (SailoException ex) { 
            Dialogs.showMessageDialog("Jäsenen hakemisessa ongelmia! " + ex.getMessage()); 
        }
    }
}
