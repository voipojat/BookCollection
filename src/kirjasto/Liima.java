package kirjasto;

/**
 * Apuluokka asioiden kuljettamiseen käyttöliittymän dialogien välillä
 * @author antontuominen
 * @version 12 Apr 2020
 *
 */
public class Liima {
    
    Kirjakokoelma kirjakokoelma;
    Kirja kirja;

    /**
     * Muodostaja
     * @param kirjakokoelma kirjakokoelma
     * @param kirja kirjaKohdalla
     */
    public Liima(Kirjakokoelma kirjakokoelma, Kirja kirja) {
        set(kirjakokoelma, kirja);
    }


    /**
     * Ettei aina tarvitse luoda uutta oliota
     * @param kirjakokoelma kirjakokoelma
     * @param kirja kirjaKohdalla
     */
    public void set(Kirjakokoelma kirjakokoelma, Kirja kirja) {
        set(kirjakokoelma);
        set(kirja);
    }


    /**
     * Ettei aina tarvitse luoda uutta oliota
     * @param kirjakokoelma kirjakokoelma
     */
    public void set(Kirjakokoelma kirjakokoelma) {
        this.kirjakokoelma = kirjakokoelma;
    }


    /**
    * Ettei aina tarvitse luoda uutta oliota
    * @param kirja kirjaKohdalla
    */
    public void set(Kirja kirja) {
        this.kirja = kirja;
    }


    /**
     * @return kirjakokoelman
     */
    public Kirjakokoelma getKokoelma() {
        return kirjakokoelma;
    }


    /**
     * @return kirjan
     */
    public Kirja getKirja() {
        return kirja;
    }


}
