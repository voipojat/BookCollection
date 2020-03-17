package kirjasto;

/**
 * @author antontuominen
 * @version 12 Mar 2020
 *
 */
public class Kirjakokoelma {
    
    private final Kirjat kirjat = new Kirjat();
    private final Kirjailijat kirjailijat = new Kirjailijat();
   
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
     *    kirjakokoelma.lisaa(kirja1);  #THROWS SailoException
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
        Kirjailija haru1 = new Kirjailija(id1); haru1.vastaaHarukiMurakami(id1); kirjakokoelma.lisaa(haru1);
        Kirjailija haru2 = new Kirjailija(id2); haru2.vastaaHarukiMurakami(id2); kirjakokoelma.lisaa(haru2);

        System.out.println("============= Kirjakokoelman testi =================");

        for (int i = 0; i < kirjakokoelma.getKirjat(); i++) {
            Kirja kirja = kirjakokoelma.annaKirja(i);
            System.out.println("Jäsen paikassa: " + i);
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
