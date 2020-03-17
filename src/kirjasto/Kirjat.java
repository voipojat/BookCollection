package kirjasto;

/**
 * @author antontuominen
 * @version 11 Mar 2020
 *
 */
public class Kirjat {
    
    private static final int maxJasenmaara = 5;
    private int lkm;
    private Kirja alkiot [] = new Kirja[maxJasenmaara];
    
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
     *   kirjat.lisaa(kirja1); #THROWS SailoException 
     * </pre>
     */
    public void lisaa(Kirja kirja) throws SailoException {
        if (lkm >= alkiot.length) throw new SailoException("Liikaa alkioita");
        alkiot[lkm] = kirja;
        lkm++;
    }
    
    /**
     * @return palauttaa kirjaston kirjojen lukumäärän
     */
    public int getLkm() {
        return lkm;
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
