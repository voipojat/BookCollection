package kirjasto.test;
// Generated by ComTest BEGIN
import kirjasto.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2020.03.17 13:48:07 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class KirjailijatTest {



  // Generated by ComTest BEGIN
  /** testIterator44 */
  @Test
  public void testIterator44() {    // Kirjailijat: 44
    Kirjailijat kirjailijat = new Kirjailijat(); 
    Kirjailija esim1 = new Kirjailija(1); kirjailijat.lisaa(esim1); 
    Kirjailija esim2 = new Kirjailija(2); kirjailijat.lisaa(esim2); 
    Kirjailija esim3 = new Kirjailija(3); kirjailijat.lisaa(esim3); 
    Iterator<Kirjailija> i2 = kirjailijat.iterator(); 
    assertEquals("From: Kirjailijat line: 54", esim1, i2.next()); 
    assertEquals("From: Kirjailijat line: 55", esim2, i2.next()); 
    assertEquals("From: Kirjailijat line: 56", esim3, i2.next()); 
    try {
    assertEquals("From: Kirjailijat line: 57", esim1, i2.next()); 
    fail("Kirjailijat: 57 Did not throw NoSuchElementException");
    } catch(NoSuchElementException _e_){ _e_.getMessage(); }
    int n = 0; 
    int kirIdt[] = { 1, 2, 3} ; 
    for ( Kirjailija kir:kirjailijat ) {
    assertEquals("From: Kirjailijat line: 63", kirIdt[n], kir.getKirjailijaId()); n++; 
    }
    assertEquals("From: Kirjailijat line: 66", 3, n); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testAnnaKirjailija80 */
  @Test
  public void testAnnaKirjailija80() {    // Kirjailijat: 80
    Kirjailijat kirjailijat = new Kirjailijat(); 
    Kirjailija esim1 = new Kirjailija(1); kirjailijat.lisaa(esim1); 
    Kirjailija esim2 = new Kirjailija(2); kirjailijat.lisaa(esim2); 
    Kirjailija esim3 = new Kirjailija(3); kirjailijat.lisaa(esim3); 
    assertEquals("From: Kirjailijat line: 86", esim2, kirjailijat.annaKirjailija(2)); 
    assertEquals("From: Kirjailijat line: 87", esim1, kirjailijat.annaKirjailija(1)); 
  } // Generated by ComTest END
}