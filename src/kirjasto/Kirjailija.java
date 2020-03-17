package kirjasto;
import java.io.*;
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
     * Testiohjelma Kirjailijalle
     * @param args ei käytössä
     */
    public static void main(String [] args) {
        Kirjailija kirjailija = new Kirjailija();
        kirjailija.vastaaHarukiMurakami(1);
        kirjailija.tulosta(System.out);     
    }
}
