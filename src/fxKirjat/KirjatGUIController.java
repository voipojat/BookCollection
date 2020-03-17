package fxKirjat;

import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import java.io.PrintStream;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List; 
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import kirjasto.Kirjakokoelma;
import kirjasto.SailoException;
import kirjasto.Kirja;
import kirjasto.Kirjailija;
import kirjasto.Kirjailijat;

/**
 * @author antontuominen
 * @version 28 Jan 2020
 * @version 12.3.2020 - lisätty kirjojen käsittely
 * Luokka käyttöliittymän tapahtumien hoitamiseksi
 */
public class KirjatGUIController implements Initializable{
    
    
    @FXML private ScrollPane panelKirja;
    @FXML private ListChooser<Kirja> chooserKirjat;
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();      
    }

	//
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
        Dialogs.showMessageDialog("Ei osata vielä tallentaa!");
    }
    
    @FXML private void handlePeruuta() {
        Dialogs.showMessageDialog("Ei osata vielä peruuttaa!");
    }
    @FXML private void handleAvaa() {
        Dialogs.showMessageDialog("Ei osata vielä avata uutta kokoelmaa!");
    }
    
    
    //===========================================================================
    
    private Kirjakokoelma kirjakokoelma = new Kirjakokoelma();

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
     * lisätty kirjailijoihin, nyt käytössä vain tuo Murakami
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
        chooserKirjat.clear();

        int index = 0;
        for (int i = 0; i < kirjakokoelma.getKirjat(); i++) {
            Kirja kirja = kirjakokoelma.annaKirja(i);
            if (kirja.getId() == kirjaId) index = i;
            chooserKirjat.add(kirja.getKirjanNimi(), kirja);
        }
        chooserKirjat.setSelectedIndex(index); 
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
            os.println("Tulostetaan kaikki kirjat");
            for (int i = 0; i < kirjakokoelma.getKirjat(); i++) {
                Kirja kirja = kirjakokoelma.annaKirja(i);
                tulosta(os, kirja);
                os.println("\n\n");
            }
        }
    }

}
