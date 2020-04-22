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
import fi.jyu.mit.ohj2.WildChars;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import kirjasto.Kirjakokoelma;
import kirjasto.SailoException;
import kirjasto.Kirja;
import kirjasto.Kirjailija;
import kirjasto.Liima;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author antontuominen
 * @version 28 Jan 2020
 * @version 12.3.2020 - lisätty kirjojen käsittely
 * @version 19 Apr 2020 - viimeistelyä
 * Luokka käyttöliittymän tapahtumien hoitamiseksi
 */
public class KirjatGUIController implements Initializable{
    
    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private ScrollPane panelKirja;
    @FXML private GridPane gridKirja;
    @FXML private ListChooser<Kirja> chooserKirjat;
    @FXML private ListChooser<Kirja> chooserMuut;
    @FXML private TextField editNimi;
    @FXML private TextField editKirjailija;
    @FXML private TextField editKustantaja;
    @FXML private TextField editKieli; 
    @FXML private TextField editVuosi; 
    @FXML private TextField editSivumaara;
    @FXML private TextField editArvio;
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
        poistaKirja();
    }
    
    @FXML private void handleLisaaKirja() {
        uusiKirja();
    }
    
    @FXML private void handleMuokkaaKirjaa() {
        muokkaa();
    }
    
    @FXML private void handleJarjestys() {
        jarjesta();
    }
    
    @FXML private void handleOhje() {
        ModalController.showModal(KirjatGUIController.class.getResource("Ohje.fxml"), "Ohje", null, "");
    }
    
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    @FXML private void handleAvaa() {
        avaa();
    }
    
    
    //===========================================================================
    
    private String kirjakokoelmanNimi = "Matin_kirjat";
    
    private Kirjakokoelma kirjakokoelma;

    private Kirja kirjaKohdalla;
    
    private Liima liima = new Liima(null, null);

    private static Kirja apukirja = new Kirja(); 
 
    /**
     * Tekee tarvittavat muut alustukset
     * Alustetaan myös kirjalistan kuuntelija 
     */
    protected void alusta() {
        chooserKirjat.clear();
       
        chooserKirjat.addSelectionListener(e -> naytaKirja(true));
        chooserMuut.addSelectionListener(e -> naytaKirja(false));
       

    }
    private void setTitle(String title) {
        ModalController.getStage(hakuehto).setTitle(title);
    }

    /**
     * Alustaa kirjakokoelman lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedosto josta kirjakokoelman tiedot luetaan
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    protected String lueTiedosto(String nimi) {
        kirjakokoelmanNimi = nimi;
        
        naytaKirja(true);
        tyhjenna();
        haeKirjailijat();
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
     * Näyttää listasta valitun kirjan tiedot
     * @param ohita ohitetaanko kirjailijan muiden kirjojen listaus
     */
    protected void naytaKirja(boolean ohita) {
        if (!ohita) {
            kirjaKohdalla = chooserMuut.getSelectedObject();
            chooserKirjat.setSelectedIndex(-1); // valinta pois
        } else {
            haeKirjailijat();
            kirjaKohdalla = chooserKirjat.getSelectedObject();
        }

        if (kirjaKohdalla == null) {
            tyhjenna();
            return;
        }
        editNimi.setText(kirjaKohdalla.getKirjanNimi());
        editKirjailija.setText(kirjakokoelma.annaKirjailija(kirjaKohdalla.getKirjailijaId()).getKirjailijanNimi());
        editKustantaja.setText(kirjaKohdalla.getKustantaja());
        editKieli.setText("" + kirjaKohdalla.getKieli());
        editVuosi.setText("" + kirjaKohdalla.getVuosi());
        editSivumaara.setText("" + kirjaKohdalla.getSivumaara());
        editArvio.setText("" + kirjaKohdalla.getArvio());
    }
    
    /**
     * Tyhjentään tekstikentät 
     */
    public void tyhjenna() {
        editNimi.setText("");
        editKirjailija.setPromptText("");
        editKustantaja.setPromptText("");
        editKieli.setText("");
        editVuosi.setText("");
        editSivumaara.setText("");
        editArvio.setText("");
    }

    /**
     * Luodaan uusi kirja kokoelmaan
     */
    protected void uusiKirja()  {
        Kirja uusi = new Kirja();
      
        try {
            kirjakokoelma.lisaa(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog(
                    "Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
        chooserKirjat.clear();
        chooserKirjat.add(uusi.getKirjanNimi(), uusi);
        chooserKirjat.setSelectedIndex(0);
        muokkaa();
        if (uusi.getKirjanNimi().equals(""))
            kirjakokoelma.poista(uusi);
        else
            uusi.rekisteroi();
        hae(kirjaKohdalla.getId());
    }
    
    /**
     * Hakee kirjojen tiedot listaan
     * @param kirjaId kirjan id, joka aktivoidaan haun jälkeen
     */
    protected void hae(int kirjaId) {
        int id = kirjaId; 
        if ( id <= 0 ) { 
            Kirja kohdalla = kirjaKohdalla; 
            if ( kohdalla != null ) id = kohdalla.getId(); 
        }
        
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apukirja.ekaKentta(); 
        
       
        String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 
        
        chooserKirjat.clear();

        int index = 0;
        Collection<Kirja> kirjat;
        if(k != 1) { //kun ei haeta kirjailijan nimellä
            try {
                kirjat = kirjakokoelma.etsi(ehto, k);
                int i = 0;
                for (Kirja kirja:kirjat) {
                    if (kirja.getId() == id) index = i;
                    chooserKirjat.add(kirja.getKirjanNimi(), kirja);
                    i++;
                }
            } catch (SailoException ex) {
                Dialogs.showMessageDialog("Kirjan hakemisessa ongelmia! " + ex.getMessage());
            }
            chooserKirjat.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää kirjan
            return;
        }
        //tässä haetaan ehkä hieman hankalasti kirjailijan nimellä kirjoja sen takia,
        //että Kirja-luokka ei tiedä kirjailijan nimeä vaan sen idn
        List<Kirja> loytyneet = new ArrayList<Kirja>(); 
        for(int i = 0; i < kirjakokoelma.getKirjat(); i++) {
            if (WildChars.onkoSamat(kirjakokoelma.annaKirjailija(kirjakokoelma.annaKirja(i).getKirjailijaId()).getKirjailijanNimi(), ehto))
                loytyneet.add(kirjakokoelma.annaKirja(i)); 
        }
        kirjat = loytyneet;
        int i = 0;
        for (Kirja kirja:kirjat) {
            if (kirja.getId() == id) index = i;
            chooserKirjat.add(kirja.getKirjanNimi(), kirja);
            i++;
        }
        chooserKirjat.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää kirjan
    }
    
    /**
     * Haetaan kirjailijan muut kirjat
     */
    private void haeKirjailijat() {
        kirjaKohdalla = chooserKirjat.getSelectedObject();
        if (kirjaKohdalla == null)
            return;
        chooserMuut.clear();
        Collection<Kirja> kirjailijanMuutKirjat = kirjakokoelma.kirjailijanKirjat(
                kirjaKohdalla.getKirjailijaId(), kirjaKohdalla.getId());
        for (Kirja kirja : kirjailijanMuutKirjat)
            chooserMuut.add(kirja.getKirjanNimi(), kirja);
    }

    /**
     * @param kirjakokoelma jota käytetään tässä käyttöliittymässä
     */
    public void setKirjakokoelma(Kirjakokoelma kirjakokoelma) {
        this.kirjakokoelma = kirjakokoelma;
        naytaKirja(true);
    }
    
    private void muokkaa()  { 
        if ( kirjaKohdalla == null )
            return;

        Kirja alkuKirja = kirjaKohdalla.clone();
        
        liima.set(kirjakokoelma, kirjaKohdalla);
      
        liima = ModalController.showModal(
                KirjatGUIController.class.getResource("MuokkaaKirjaa.fxml"),
                "Muokkaa", null, liima);
        kirjaKohdalla = liima.getKirja();
        kirjakokoelma = liima.getKokoelma();
       
        if ( kirjaKohdalla == null ) return; 
        if (!kirjaKohdalla.toString().equals(alkuKirja.toString()))
            kirjakokoelma.korvaaTaiLisaa(kirjaKohdalla);
       
        hae(kirjaKohdalla.getId());
    } 
    
    private void jarjesta() {
  
        List<Kirja> loytyneet = new ArrayList<Kirja>();
        for(Kirja kirja: kirjakokoelma.annaKirjat()) {
            loytyneet.add(kirja);
        }
        Collections.sort(loytyneet, new Comparator<Kirja>() {
            @Override
            public int compare(Kirja c1, Kirja c2) {
                return  c2.getArvio() - c1.getArvio();
            }
        });
        chooserKirjat.clear();
        for (Kirja kirja:loytyneet) {  
            chooserKirjat.add(kirja.getKirjanNimi(), kirja);   
        }
    } 

    /*
     * Poistetaan listalta valittu kirja
     */
    private void poistaKirja() {
        Kirja kirja = kirjaKohdalla;
        if ( kirja == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko kirja: " + kirja.getKirjanNimi(), "Kyllä", "Ei") )
            return;
        kirjakokoelma.poista(kirja);
        int index = chooserKirjat.getSelectedIndex();
        hae(0);
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
            Collection<Kirja> kirjat = kirjakokoelma.etsi("", -1); 
            for (Kirja kirja:kirjat) { 
                tulosta(os, kirja);
                os.println("\n\n");
            }
        } catch (SailoException ex) { 
            Dialogs.showMessageDialog("Kirjan hakemisessa ongelmia! " + ex.getMessage()); 
        }
    }
}
