package kirjasto;

import java.util.*;

/**
 * @author antontuominen
 * @version 11 Mar 2020
 *
 */
public class Kirjailijat implements Iterable<Kirjailija>{
    
    
    
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
