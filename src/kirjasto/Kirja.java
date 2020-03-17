package kirjasto;

import java.io.*;

/**
 * @author antontuominen
 * @version 10 Mar 2020
 *
 */
public class Kirja {
    
    private int id;
    private int kirjailijaId;
    private String kirjanNimi = "";
    private String kustantaja = "";
    private int vuosi;
    private String kieli = "";
    private int sivumaara;
    private int arvio;
    
    private static int seuraavaId;
    
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
     * Annetaan kirjalle uusi id sekä kirjailijaId
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
        kirjailijaId = seuraavaId;
        seuraavaId++;
        return id;
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
     * Testiohjelma kirjalle
     * @param args ei käytössä
     */
    public static void main(String [] args) {
        Kirja kirja1 = new Kirja();
        kirja1.vastaaKafkaRannalla();
        kirja1.rekisteroi();
        kirja1.tulosta(System.out);
    }

}
