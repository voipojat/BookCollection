package kirjasto;
import java.io.*;
import fi.jyu.mit.ohj2.Mjonot;
/**
 * @author antontuominen
 * @version 11 Mar 2020
 *
 */
public class Kirjailija {
    
    private int id;
    private String kirjailijanNimi = "";
    
    /**
     * Alustetaan kirjailija
     */
    public Kirjailija() {
        //
    }

    /**
     * Alustetaan tietyn kirjan kirjailija
     * @param kirjailijaId kirjan kirjailijaId
     */
    public Kirjailija(int kirjailijaId) {
        id = kirjailijaId;
    }
    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Kirjailijalle
     * @param kirjailijaId viite kirjaan, jonka kirjailijasta on kyse
     * @example
     * <pre name="test">
     *    #import java.io.ByteArrayOutputStream;
     *    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
     *    Kirjailija kir = new Kirjailija();
     *    kir.vastaaHarukiMurakami(1);
     *    kir.tulosta(outContent);
     *    outContent.toString() === "1 Haruki Murakami\n";
     * </pre>
     */
    public void vastaaHarukiMurakami(int kirjailijaId) {
        id = kirjailijaId;
        kirjailijanNimi = "Haruki Murakami";
    }
    
    /**
     * Tulostetaan kirjailijan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(id + " " + kirjailijanNimi);
    }

    /**
     * Tulostetaan kirjan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Palautetaan kirjailijan oma id
     * @return kirjailijan id
     */
    public int getKirjailijaId() {
        return id;
    }
    
    /**
     * Palautetaan kirjailijan nimi
     * @return kirjailijan nimi
     */
    public String getKirjailijanNimi() {
        return kirjailijanNimi;
    }
    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setId(int nr) {
        id = nr;
    }

    /**
     * Palauttaa kirjailijan tiedot merkkijonona, jonka voi tallentaa tiedostoon
     * @return kirjailija tolppaeroteltuna merkkijonona
     */
    @Override
    public String toString() {
        return "" + getKirjailijaId() + "|" + kirjailijanNimi;
    }

    
    /**
     * Selvittää kirjailijan tiedot | -erotellusta merkkijonosta
     * @param rivi josta kirjailijan tiedot otetaan
     * @example
     * <pre name="test">
     *   Kirjailija kirjailija = new Kirjailija();
     *   kirjailija.parse("   2   |  Haruki Murakami");
     *   kirjailija.getKirjailijaId() === 2;
     *   kirjailija.toString()    === "2|Haruki Murakami"; 
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setId(Mjonot.erota(sb, '|', getKirjailijaId()));
        kirjailijanNimi = Mjonot.erota(sb, '|', kirjailijanNimi);
    }
    
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }
    

    @Override
    public int hashCode() {
        return id;
    }



    /**
     * Testiohjelma Kirjailijalle
     * @param args ei käytössä
     */
    public static void main(String [] args) {
        Kirjailija kirjailija = new Kirjailija();
        kirjailija.vastaaHarukiMurakami(1);
        kirjailija.tulosta(System.out);     
    }
}
