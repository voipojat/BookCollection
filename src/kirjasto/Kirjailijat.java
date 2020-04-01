package kirjasto;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;



/**
 * @author antontuominen
 * @version 11 Mar 2020
 *
 */
public class Kirjailijat implements Iterable<Kirjailija>{
    
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "";
    
    
    
    /**Taulukko kirjailijoista*/
    private final Collection<Kirjailija> kirjailijat = new ArrayList<Kirjailija>();
    
    
    /**
     * Oletusmuodostaja
     */
    public Kirjailijat() {
        //
    }
    
    /**
     * Palauttaa kirjaston kirjailijoiden lukumäärän
     * @return kirjailijoiden lukumäärä
     */
    public int getLkm() {
        return kirjailijat.size();
    }

    /**
     * Lisää uuden kirjailijan tietorakenteeseen
     * @param kirjailija lisättävä kirjailija
     */
    public void lisaa(Kirjailija kirjailija) {
        kirjailijat.add(kirjailija);  
        muutettu = true;
    }
    
    
    /**
     * Lukee kirjailijat tiedostosta
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *   Kirjailijat kirjailijat = new Kirjailijat();
     *   Kirjailija haru1 = new Kirjailija(); haru1.vastaaHarukiMurakami(1);
     *   Kirjailija haru2 = new Kirjailija(); haru2.vastaaHarukiMurakami(1);
     *   String tiedNimi = "testikirjat";
     *   File ftied = new File(tiedNimi+".dat");
     *   ftied.delete();
     *   kirjailijat.lueTiedostosta(tiedNimi); #THROWS SailoException
     *   kirjailijat.lisaa(haru1);
     *   kirjailijat.lisaa(haru2);
     *   kirjailijat.tallenna();
     *   kirjailijat = new Kirjailijat();
     *   kirjailijat.lueTiedostosta(tiedNimi);
     *   Iterator<Kirjailija> i = kirjailijat.iterator();
     *   i.next().toString() === haru1.toString();
     *   i.next().toString() === haru2.toString();
     *   i.hasNext() === false;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Kirjailija kir = new Kirjailija();
                kir.parse(rivi); 
                lisaa(kir);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    
    /**
     * Tallentaa kirjailijat tiedostoon
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Kirjailija kir : this) {
                fo.println(kir.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }


    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }

    
    /**
     * Iteraattori kaikkien kirjailijoiden läpikäymiseen
     * @return kirjailijaiteraattori
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     *    Kirjailijat kirjailijat = new Kirjailijat();
     *    
     *    Kirjailija esim1 = new Kirjailija(1); kirjailijat.lisaa(esim1);
     *    Kirjailija esim2 = new Kirjailija(2); kirjailijat.lisaa(esim2);
     *    Kirjailija esim3 = new Kirjailija(3); kirjailijat.lisaa(esim3);
     *    
     *    Iterator<Kirjailija> i2 = kirjailijat.iterator();
     *    i2.next() === esim1;
     *    i2.next() === esim2;
     *    i2.next() === esim3;
     *    i2.next() === esim1; #THROWS NoSuchElementException
     *    
     *    int n = 0;
     *    int kirIdt[] = {1, 2, 3};
     *  
     *    for ( Kirjailija kir:kirjailijat ) { 
     *      kir.getKirjailijaId() === kirIdt[n]; n++;  
     *    }
     *  
     *    n === 3;
     *  
     * </pre>
     */
    @Override
    public Iterator<Kirjailija> iterator() {
        return kirjailijat.iterator();
    }
    
    /**
     * Haetaan kirjan kirjailija
     * @param kirjailijaId kirjan kirjailijaId, jolla kirjailija haetaan
     * @return kirjan kirjailija
     * @example
     * <pre name="test">
     * #import java.util.*;
     *    Kirjailijat kirjailijat = new Kirjailijat();
     *    Kirjailija esim1 = new Kirjailija(1); kirjailijat.lisaa(esim1);
     *    Kirjailija esim2 = new Kirjailija(2); kirjailijat.lisaa(esim2);
     *    Kirjailija esim3 = new Kirjailija(3); kirjailijat.lisaa(esim3);
     *    kirjailijat.annaKirjailija(2) === esim2;
     *    kirjailijat.annaKirjailija(1) === esim1;
     * </pre>
     */
    public Kirjailija annaKirjailija(int kirjailijaId)  {
        Kirjailija tyhja = new Kirjailija();
        for (Kirjailija kir : kirjailijat)
            if (kir.getKirjailijaId() == kirjailijaId) return kir;
        return tyhja; 
    }

    /**
     * Testiohjelma kirjailijoille
     * @param args ei käytössä
     */
    public static void main(String [] args) {
        Kirjailijat kirjailijat = new Kirjailijat();
        Kirjailija haru1 = new Kirjailija();
        haru1.vastaaHarukiMurakami(1);
        Kirjailija haru2 = new Kirjailija();
        haru2.vastaaHarukiMurakami(2);
        Kirjailija haru3 = new Kirjailija();
        haru3.vastaaHarukiMurakami(1);
        Kirjailija haru4 = new Kirjailija();
        haru4.vastaaHarukiMurakami(1);
        
        kirjailijat.lisaa(haru1);
        kirjailijat.lisaa(haru2);
        kirjailijat.lisaa(haru3);
        kirjailijat.lisaa(haru4);
        
        
        System.out.println("============= Kirjailijat testi =================");

        Kirjailija kirjailija = kirjailijat.annaKirjailija(2);

        System.out.print(kirjailija.getKirjailijaId() + " ");
        kirjailija.tulosta(System.out);
    }

}
