package kirjasto;
import java.io.*;
import fi.jyu.mit.ohj2.Mjonot;
/**
 * Kirjakokoelman kirjailija, joka tietää kenttänsä
 * ja osaa huolehtia id:stään
 * 
 * @author antontuominen
 * @version 11 Mar 2020
 *
 */
public class Kirjailija {
    
    private int id;
    private String kirjailijanNimi = "";
    private static int seuraavaId = 1;
    
    /**
     * Alustetaan kirjailija
     */
    public Kirjailija() {
        //
    }

    /**
     * Alustetaan tietyn kirjan kirjailija
     * @param text kirjailijan nimi
     */
    public Kirjailija(String text) {
        kirjailijanNimi = text;
    }
    
    /**
     * Alustetaan tietyn kirjan kirjailija
     * @param kirjailijaId kirjan kirjailijaId
     */
    public Kirjailija(int kirjailijaId) {
        id = kirjailijaId;
    }

    /**
     * Annetaan kirjailijalle uusi id
     * @return kirjailijan uusi id
     * @example
     * <pre name="test">
     *  Kirjailija kir1 = new Kirjailija();
     *  kir1.getKirjailijaId() === 0;
     *  kir1.rekisteroi();
     *  Kirjailija kir2 = new Kirjailija();
     *  kir2.rekisteroi();
     *  int n1 = kir1.getKirjailijaId();
     *  int n2 = kir2.getKirjailijaId();
     *  n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        id = seuraavaId;
        seuraavaId++;
        return id;
    }

    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Kirjailijalle
     * @param kirjailijaId viite kirjaan, jonka kirjailijasta on kyse
     * @param text kirjailijan nimi
     * @example
     * <pre name="test">
     *    #import java.io.ByteArrayOutputStream;
     *    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
     *    Kirjailija kir = new Kirjailija();
     *    kir.vastaaHarukiMurakami(1, "Haruki Murakami");
     *    kir.tulosta(outContent);
     *    outContent.toString() === "1 Haruki Murakami\n";
     * </pre>
     */
    public void vastaaHarukiMurakami(int kirjailijaId, String text) {
        id = kirjailijaId;
        kirjailijanNimi = text;
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
     * Asettaa id:n ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava id
     */
    private void setId(int nr) {
        id = nr;
        if (id >= seuraavaId)
            seuraavaId = id + 1;

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
        kirjailija.vastaaHarukiMurakami(1, "Haruki Murakami");
       
        kirjailija.tulosta(System.out);     
    }
}
