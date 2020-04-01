package kirjasto;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author antontuominen
 * @version 11 Mar 2020
 * @version 29 Mar 2020 - tiedostot hakemistossa
 */
public class Kirjat implements Iterable<Kirja>{
    
    private static final int maxKirjamaara = 5;
    private boolean muutettu = false;
    private int lkm;
    private String kokoNimi = "";
    private String tiedostonPerusNimi = "kirjat";
    private Kirja alkiot [] = new Kirja[maxKirjamaara];
    
    /**
     * Oletusmuodostaja
     */
    public Kirjat() {
        //
    }

    /**
     * @param kirja lisättävä kirja
     * @throws SailoException jos tietorakenne täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     *   Kirjat kirjat = new Kirjat();
     *   Kirja kirja1 = new Kirja(), kirja2 = new Kirja();
     *   kirjat.getLkm() === 0;
     *   kirjat.lisaa(kirja1);
     *   kirjat.lisaa(kirja2); kirjat.getLkm() === 2;
     *   kirjat.anna(0) === kirja1;
     *   kirjat.anna(1) === kirja2;
     *   kirjat.anna(2) === kirja1; #THROWS IndexOutOfBoundsException
     *   kirjat.lisaa(kirja1);
     *   kirjat.lisaa(kirja1);
     *   kirjat.lisaa(kirja1);
     *   kirjat.lisaa(kirja1); 
     * </pre>
     */
    public void lisaa(Kirja kirja) throws SailoException {
        if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm+20);
        alkiot[lkm] = kirja;
        lkm++;
        muutettu = true;
    }
    
    /**
     * @return palauttaa kirjaston kirjojen lukumäärän
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }

    
    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }

    
    /**
     * @param i monennenko kirjan viite halutaan
     * @return viite kirjaan, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Kirja anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    
    
    /**
     * Lukee kirjat tiedostosta
     * @param tied tiedoston perusnimi
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * #import java.io.*;
     * #import java.util.*;
     * 
     *  Kirjat kirjat = new Kirjat();
     *  
     *  Kirja kafka = new Kirja(); kafka.vastaaKafkaRannalla();
     *  Kirja kafka1 = new Kirja(); kafka1.vastaaKafkaRannalla();
     *  
     *  String hakemisto = "testikirjat";
     *  String tiedNimi = hakemisto+"/kirjat";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  kirjat.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  kirjat.lisaa(kafka);
     *  kirjat.lisaa(kafka1);
     *  kirjat.tallenna();
     *  kirjat = new Kirjat();            // Poistetaan vanhat luomalla uusi
     *  kirjat.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.
     *  Iterator<Kirja> i = kirjat.iterator();
     *  i.next() === kafka;
     *  i.next() === kafka1;
     *  i.hasNext() === false;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
           
            String rivi = fi.readLine();

            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Kirja kirja = new Kirja();
                kirja.parse(rivi); 
                lisaa(kirja);
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
    
    
    /**Tallentaa kirjat tiedostoon
     * Tiedoston muoto: 
     *  25
     *  0|0|Kafka rannalla|Tammi|2015|suomi|486|4
     *  1|0|Suuri lammasseikkailu|Tammi|2009|suomi|352|4
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(alkiot.length);
            for (Kirja kirja : this) {
                fo.println(kirja.toString());
            }
            //} catch ( IOException e ) { // ei heitä poikkeusta
            //  throw new SailoException("Tallettamisessa ongelmia: " + e.getMessage());
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    
    
    /**
     * Palauttaa Kirjakokoelman koko nimen
     * @return Kirjakokoelman koko nimi merkkijononna
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    
    
    /**
     * Luokka kirjojen iterointiin
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Kirjat kirjat = new Kirjat();
     * Kirja kafka = new Kirja(), kafka1 = new Kirja();
     * kafka.rekisteroi(); kafka1.rekisteroi();
     *
     * kirjat.lisaa(kafka); 
     * kirjat.lisaa(kafka1); 
     * String tulos = " " + kafka.getId() + " " + kafka1.getId();
     * 
     * StringBuffer ids = new StringBuffer(30);
     * 
     * for (Iterator<Kirja>  i=kirjat.iterator(); i.hasNext(); ) { 
     *   Kirja kirja = i.next();
     *   ids.append(" "+kirja.getId());           
     * }
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Kirja>  i=kirjat.iterator();
     * i.next() == kafka  === true;
     * i.next() == kafka1  === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     *    
     * </pre>
     */
    public class KirjatIterator implements Iterator<Kirja> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa kirjaa
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä kirjoja
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava kirja
         * @return seuraava kirja
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Kirja next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }


    /**
     * Palautetaan iteraattori jäsenistään.
     * @return kirja iteraattori
     */
    @Override
    public Iterator<Kirja> iterator() {
        return new KirjatIterator();
    }

    /**
     * Palauttaa taulukossa hakuehtoon vastaavien kirjojen viitteet,
     * tässä vaiheessa palauttaa kaikki
     * TODO ehdon mukaan hakeminen ja testit
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenteen löytyneistä kirjoista
     */
    @SuppressWarnings("unused")
    public Collection<Kirja> etsi(String hakuehto, int k) { 
        Collection<Kirja> loytyneet = new ArrayList<Kirja>(); 
        for (Kirja kirja : this) { 
            loytyneet.add(kirja);  
        } 
        return loytyneet; 
    }

    /**
     * Testiohjelma kirjoille
     * @param args ei käytössä
     */
    public static void main(String [] args) {
        Kirjat kirjat = new Kirjat();
        
        Kirja kafka = new Kirja(), kafka2 = new Kirja();
        kafka.rekisteroi();
        kafka.vastaaKafkaRannalla();
        kafka2.rekisteroi();
        kafka2.vastaaKafkaRannalla();
        
        try {
            kirjat.lisaa(kafka);
            kirjat.lisaa(kafka2);

            System.out.println("============= Kirjat testi =================");

            for (int i = 0; i < kirjat.getLkm(); i++) {
                Kirja kirja = kirjat.anna(i);
                System.out.println("Kirja nro: " + i);
                kirja.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
