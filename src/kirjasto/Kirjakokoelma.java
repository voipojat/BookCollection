package kirjasto;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author antontuominen
 * @version 12 Mar 2020 kirjailijat ja kirjat
 * @version 19 Apr 2020 viimeistelyä
 *
 */
public class Kirjakokoelma {
    
    private Kirjat kirjat = new Kirjat();
    private Kirjailijat kirjailijat = new Kirjailijat();
   
    /**
     * palauttaa kirjakokoelman kirjojen määrän
     * @return kirjojen määrä
     */
    public int getKirjat() {
        return kirjat.getLkm();
    }
    
    /**
     * palauttaa kirjakokoelman kirjailijoiden määrän
     * @return kirjailijoiden määrä
     */
    public int getKirjailijat() {
        return kirjailijat.getLkm();
    }
    
    /**
     * @return kirjailjat kloonin
     */
    public Kirjailijat annaKirjailijat() {
        return kirjailijat.clone();
    }
    
    /**
     * @return kirjat kloonin
     */
    public Kirjat annaKirjat() {
        return kirjat.clone();
    }
    
    /**
     * Korvaa kirjan tietorakenteessa. Ottaa kirjan omistukseensa. 
     * Etsitään samalla id:llä oleva kirja. Jos ei löydy, 
     * niin lisätään uutena kirjana.
     * @param kirja kirjan viite
     */
    public void korvaaTaiLisaa(Kirja kirja)  { 
        kirjat.korvaaTaiLisaa(kirja); 
    } 

    
    /**
     * Poistaa kirjakokoelmasta ne joilla on nro. 
     * @param nro viitenumero, jonka mukaan poistetaan
     * @return montako kirjaa poistettiin
     */
    public int poista(@SuppressWarnings("unused") int nro) {
        return 0;
    }

    /**
     * Lisätään uusi kirja kirjakokoelmaan
     * @param kirja lisättävä kirja
     * @throws SailoException jos lisäystä ei voi tehdä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     *    Kirjakokoelma kirjakokoelma = new Kirjakokoelma();
     *    Kirja kirja1 = new Kirja(), kirja2 = new Kirja();
     *    kirja1.rekisteroi(); kirja2.rekisteroi();
     *    kirjakokoelma.getKirjat() === 0;
     *    kirjakokoelma.lisaa(kirja1); kirjakokoelma.getKirjat() === 1;
     *    kirjakokoelma.lisaa(kirja2);
     *    kirjakokoelma.getKirjat() === 2;
     *    kirjakokoelma.annaKirja(0) === kirja1;
     *    kirjakokoelma.annaKirja(1) === kirja2;
     *    kirjakokoelma.annaKirja(3) === kirja1; #THROWS IndexOutOfBoundsException
     *    kirjakokoelma.lisaa(kirja1);
     *    kirjakokoelma.lisaa(kirja1);
     *    kirjakokoelma.lisaa(kirja1);
     *    kirjakokoelma.lisaa(kirja1);  
     * </pre>
     */
    public void lisaa(Kirja kirja) throws SailoException{
        kirjat.lisaa(kirja);
    }
    
    /**
     * Lisätään uusi kirjailija kirjakokoelmaan
     * @param kirjailija lisättävä kirjailija
     */
    public void lisaa(Kirjailija kirjailija) {
        kirjailijat.lisaa(kirjailija);
    }
    
    
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien kirjojen viitteet 
     * @param hakuehto hakuehto  
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä kirjoista
     * @throws SailoException Jos jotakin menee väärin
     */ 
    public Collection<Kirja> etsi(String hakuehto, int k) throws SailoException { 
        return kirjat.etsi(hakuehto, k); 
    } 

    
    /**
     * Palauttaa i:n kirjan
     * @param i monesko kirja palautetaan
     * @return viite i:teen kirjaan
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Kirja annaKirja(int i) throws IndexOutOfBoundsException {
        return kirjat.anna(i);
    }
    
    /**
     * Haetaan kirjan kirjailija
     * @param kirjailijaId jolla kirjailija haetaan
     * @return kirjailija
     */
    public Kirjailija annaKirjailija(int kirjailijaId) {
        return kirjailijat.annaKirjailija(kirjailijaId);
    }
    
    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        kirjat.setTiedostonPerusNimi(hakemistonNimi + "kirjat");
        kirjailijat.setTiedostonPerusNimi(hakemistonNimi + "kirjailijat");
    }
    
    
    /**
     * Poistaa kirjan kirjakokoelmasta
     * @param kirja joka poistetaan
     * @return montako kirjaa poistettiin
     */
    public int poista(Kirja kirja) {
        if ( kirja == null ) return 0;
        int ret = kirjat.poista(kirja.getId());
        return ret; 
    }

    
    /**
     * Lukee kirjakokoelman tiedot tiedostosta
     * @param nimi jota käytetään lukemiseen
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     * 
     *  Kirjakokoelma kirjakokoelma = new Kirjakokoelma();
     *  
     *  Kirja kafka = new Kirja(); kafka.vastaaKafkaRannalla(); kafka.rekisteroi();
     *  Kirja kafka1 = new Kirja(); kafka1.vastaaKafkaRannalla(); kafka1.rekisteroi();
     *  
     *  Kirjailija haru = new Kirjailija(); haru.vastaaHarukiMurakami(kafka.getId(), "Haruki Murakami");
     *  Kirjailija haru1 = new Kirjailija(); haru1.vastaaHarukiMurakami(kafka1.getId(), "Haruki Murakami");
     *  
     *  
     *  String hakemisto = "testikirjat";
     *  File dir = new File(hakemisto);
     *  File ftied  = new File(hakemisto+"/kirjat.dat");
     *  File fhtied = new File(hakemisto+"/kirjailijat.dat");
     *  dir.mkdir();  
     *  ftied.delete();
     *  fhtied.delete();
     *  kirjakokoelma.lueTiedostosta(hakemisto); #THROWS SailoException
     *  
     *  kirjakokoelma.lisaa(kafka);
     *  kirjakokoelma.lisaa(kafka1);
     *  kirjakokoelma.lisaa(haru);
     *  kirjakokoelma.lisaa(haru1);
     *  
     *  kirjakokoelma.tallenna();
     *  kirjakokoelma = new Kirjakokoelma();
     *  kirjakokoelma.lueTiedostosta(hakemisto);
     *  Collection<Kirja> kaikki = kirjakokoelma.etsi("",-1); 
     *  Iterator<Kirja> it = kaikki.iterator();
     *  it.next() === kafka;
     *  it.next() === kafka1;
     *  it.hasNext() === false;
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        kirjat = new Kirjat(); // jos luetaan olemassa olevaan niin helpoin tyhjentää näin
        kirjailijat = new Kirjailijat();

        setTiedosto(nimi);
        kirjat.lueTiedostosta();
        kirjailijat.lueTiedostosta();
    }
    
    
    /**
     * Tallentaa kirjakokoelman tiedot tiedostoon.  
     * Vaikka kirjojen tallentaminen epäonistuisi, niin yritetään silti tallentaa
     * kirjailijoita ennen poikkeuksen heittämistä.
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            kirjat.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            kirjailijat.tallenna();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }
    
    
    /**
     * Asettaa kirjailijat uusiksi (tallennus)
     * @param tmpKirjailijat uudet kirjailijat
     */
    public void set(Kirjailijat tmpKirjailijat) {
        kirjailijat = tmpKirjailijat;
    }
    
    
    /**
     * Palauttaa hakuehtoon vastaavien kirjojen viitteet 
     * @param kirjailijaId kirjailijan id, jonka kirjat halutaan
     * @param kirjaId valitun kirjan id, ei haluta näyttää samaa uudestaan
     * @return tietorakenne löytyneistä kirjoista
     * @example
     * <pre name="test">
     *   #THROWS CloneNotSupportedException, SailoException
     *   Kirjakokoelma kirjakokoelma = new Kirjakokoelma();
     *   Kirja k1 = new Kirja();
     *   k1.rekisteroi();
     *   k1.setKirjailijaId(2);
     *   kirjakokoelma.lisaa(k1); 
     *   Collection<Kirja> loytyneet = kirjakokoelma.kirjailijanKirjat(2, 1);
     *   loytyneet.size() === 1;
     *   Iterator<Kirja> it = loytyneet.iterator();
     *   it.next() == k1 === true;
     * </pre>
     */
    public Collection<Kirja> kirjailijanKirjat(int kirjailijaId, int kirjaId) {
        List<Kirja> loytyneet = new ArrayList<Kirja>();
        for (Kirja kirja : kirjat)
            if (kirja.getKirjailijaId() == kirjailijaId
                    && kirja.getId() != kirjaId)
                loytyneet.add(kirja);
        return loytyneet;
    }

    /**
     * Testiohjelma kirjakokoelmalle
     * @param args ei käytössä
     */
    public static void main(String [] args) {
        Kirjakokoelma kirjakokoelma = new Kirjakokoelma();
        
        try {
        Kirja kafka1 = new Kirja(), kafka2 = new Kirja();
        kafka1.rekisteroi();
        kafka1.vastaaKafkaRannalla();
        kafka2.rekisteroi();
        kafka2.vastaaKafkaRannalla();

        kirjakokoelma.lisaa(kafka1);
        kirjakokoelma.lisaa(kafka2);
        int id1 = kafka1.getId();
        int id2 = kafka2.getId();
        Kirjailija haru1 = new Kirjailija(id1); haru1.vastaaHarukiMurakami(id1, "Haruki Murakami"); kirjakokoelma.lisaa(haru1);
        Kirjailija haru2 = new Kirjailija(id2); haru2.vastaaHarukiMurakami(id2, "Haruki Murakami"); kirjakokoelma.lisaa(haru2);

        System.out.println("============= Kirjakokoelman testi =================");

        for (int i = 0; i < kirjakokoelma.getKirjat(); i++) {
            Kirja kirja = kirjakokoelma.annaKirja(i);
            System.out.println("Kirja paikassa: " + i);
            kirja.tulosta(System.out);
            Kirjailija loydetty = kirjakokoelma.annaKirjailija(kirja.getKirjailijaId());
            loydetty.tulosta(System.out);
            System.out.println("====================================================");
        }

    } catch (SailoException ex) {
        System.out.println(ex.getMessage());
    }

    }
}
