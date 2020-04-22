package kirjasto;

import java.io.*;
import fi.jyu.mit.ohj2.Mjonot;

/**
 * Kirjakokoelman kirja, joka tietää omat kenttänsä
 * ja osaa huolehtia id:stään
 * 
 * @author antontuominen
 * @version 10 Mar 2020
 *
 */
public class Kirja implements Cloneable{
    
    private int id;
    private int kirjailijaId;
    private String kirjanNimi = "";
    private String kustantaja = "";
    private int vuosi;
    private String kieli = "";
    private int sivumaara;
    private int arvio;
    
    private static int seuraavaId = 1;

    /**
     * @return kirjan nimi
     * @example
     * <pre name="test">
     *    Kirja kirja = new Kirja();
     *    kirja.vastaaKafkaRannalla();
     *    kirja.getKirjanNimi() =R= "Kafka rannalla";
     * </pre>
     */
    public String getKirjanNimi() {
        return kirjanNimi;
    }
    
   
    /**
     * Palauttaa kirjan kenttien lukumäärän
     * @return kenttien lukumäärä
     */
    
    public int getKenttienLkm() {
        return 7;
    }

    /**
     * Eka kenttä joka on mielekäs kysyttäväksi
     * @return ekan kentän indeksi
     */
    
    public int ekaKentta() {
        return 0;
    }
    
    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + kirjanNimi;
        case 1: return "" + kirjanNimi; //sinänsä turha, koska kirjan kirjailija annetaan eri tavalla
        case 2: return "" + kustantaja;
        case 3: return "" + vuosi;
        case 4: return "" + kieli;
        case 5: return "" + sivumaara;
        case 6: return "" + arvio;
     
        default: return "voi pojat";
        }
    }

    /**
     * Palauttaa k:tta kirjan kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
     * @return k:netta kenttää vastaava kysymys
     */
    
    public String getKysymys(int k) {
        switch ( k ) {
       
        case 0: return "Kirjan nimi";
        case 1: return "Kirjailijan nimi";
        case 2: return "Kustantaja";
        case 3: return "Vuosi";
        case 4: return "Kieli";
        case 5: return "Sivumäärä";
        case 6: return "Arvio";
  
        default: return "Äääliö";
        }
    }

    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot kirjalle
     */
    public void vastaaKafkaRannalla() {
        kirjanNimi = "Kafka rannalla";
        kustantaja = "Tammi";
        vuosi = 2015;
        kieli = "suomi";
        sivumaara = 486;
        arvio = 4;
    }
    
    
    /**
     * Annetaan kirjalle uusi id 
     * @return kirjan uusi id
     * @example
     * <pre name="test">
     *    Kirja kirja = new Kirja();
     *    kirja.getId() === 0;
     *    kirja.rekisteroi();
     *    Kirja kirja2 = new Kirja();
     *    kirja2.rekisteroi();
     *    int id1 = kirja.getId();
     *    int id2 = kirja2.getId();
     *    id1 === id2 - 1;
     * </pre>
     */
    public int rekisteroi() {
        id = seuraavaId;
        seuraavaId++;
        return id;
    }


    /**
     * Asettaa id:n ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava id
     */
    private void setId(int nr) {
        id = nr;
        if (id >= seuraavaId) seuraavaId = id + 1;
    }

    
    /**
     * Palauttaa kirjan id:n.
     * @return kirjan id
     */
    public int getId() {
        return id;
    }
    
    /**
     * palauttaa kirjan kirjailijan id:n
     * @return kirjan kirjailijan id
     */
    public int getKirjailijaId() {
       return kirjailijaId;
    }
    
    /**
     * Asetetaan kirjalle kirjailijan id
     * @param id kirjailijan uusi id
     */
    public void setKirjailijaId(int id) {
        kirjailijaId = id;
    }

    
    /**
     * Tulostetaan kirjan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("id: " + id + "\nKirja: " + kirjanNimi + "\nKirjailijan id: " + kirjailijaId
                + "\nKustantaja: " + kustantaja + "\nJulkaisuvuosi: " + vuosi + "\nKieli: "
                + kieli + "\nSivumäärä: " + sivumaara + "\nArvio: " + arvio);
    }


    /**
     * Tulostetaan kirjan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Palauttaa kirjan tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return kirja tolppaeroteltuna merkkijonona 
     * <pre name="test">
     *   Kirja kirja = new Kirja();
     *   kirja.parse("   2  |  4  | Kafka rannalla ");
     *   kirja.toString().startsWith("2|4|Kafka rannalla|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     * </pre>  
     */
    @Override
    public String toString() {
        return "" +
                getId() + "|" +
                getKirjailijaId() + "|" +
                kirjanNimi + "|" +
                kustantaja + "|" +
                vuosi + "|" +
                kieli + "|" +
                sivumaara + "|" +
                arvio;
    }
    
    /**
     * Selvittää kirjan tiedot | - erotellusta merkkijonosta
     * @param rivi josta kirjan tiedot luetaan
     * @example
     * <pre name="test">
     *   Kirja kirja = new Kirja();
     *   kirja.parse("   1  |  3   | Suuri lammasseikkailu");
     *   kirja.getId() === 1;
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setId(Mjonot.erota(sb, '|', getId()));
        setKirjailijaId(Mjonot.erota(sb, '|', getKirjailijaId()));
        kirjanNimi = Mjonot.erota(sb, '|', kirjanNimi);
        kustantaja = Mjonot.erota(sb, '|', kustantaja);
        vuosi = Mjonot.erota(sb, '|', vuosi);
        kieli = Mjonot.erota(sb, '|', kieli);
        sivumaara = Mjonot.erota(sb, '|', sivumaara);
        arvio = Mjonot.erota(sb, '|', arvio);
    }
    
    @Override
    public boolean equals(Object kirja) {
        if ( kirja == null ) return false;
        return this.toString().equals(kirja.toString());
    }
    
    @Override
    public Kirja clone()  { 
        Kirja klooni = new Kirja();
        klooni.setId(id);
        klooni.setNimi(kirjanNimi);
        klooni.setKirjailijaId(kirjailijaId);
        klooni.setKustantaja(kustantaja);
        klooni.setVuosi(vuosi);
        klooni.setKieli(kieli);
        klooni.setSivumaara(sivumaara);
        klooni.setArvio(arvio);
       
        return klooni;
    }
    
    /**
     * @return kustantaja
     */
    public String getKustantaja() {
        return kustantaja;
    }
    
    /**
     * @return kieli
     */
    public String getKieli() {
        return kieli;
    }
    
    /**
     * @return arvio
     */
    public int getArvio() {
        return arvio;
    }
    
    /**
     * @return sivumäärä
     */
    public int getSivumaara() {
        return sivumaara;
    }
    

    /**
     * @return kirjan julkaisuvuoden
     */
    public int getVuosi() {
        return vuosi;
    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * @param text asetettava kirjan nimi
     */
    public void setNimi(String text) {
        this.kirjanNimi = text;
        
    }


    /**
     * @param parseInt asetettava vuosi
     */
    public void setVuosi(int parseInt) {
        this.vuosi = parseInt;
        
    }


    /**
     * @param text asetettava kieli
     */
    public void setKieli(String text) {
       this.kieli = text;
        
    }


    /**
     * @param text asetettava kustantaja
     */
    public void setKustantaja(String text) {
        this.kustantaja = text;
        
    }


    /**
     * @param parseInt asetettava sivumäärä
     */
    public void setSivumaara(int parseInt) {
        this.sivumaara = parseInt;
        
    }


    /**
     * @param parseInt asetettava arvio
     */
    public void setArvio(int parseInt) {
        this.arvio = parseInt;
        
    }

    /**
     * Testiohjelma kirjalle
     * @param args ei käytössä
     */
    public static void main(String [] args) {
        Kirja kirja1 = new Kirja();
        kirja1.vastaaKafkaRannalla();
     
        kirja1.tulosta(System.out);
    }

}
