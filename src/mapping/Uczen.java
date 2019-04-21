package mapping;
// Generated 21 kwi 2019, 11:03:06 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Uczen generated by hbm2java
 */
public class Uczen  implements java.io.Serializable {


     private long pesel;
     private Klasa klasa;
     private String imie;
     private String nazwisko;
     private Autoryzacja autoryzacja;
     private Set rodzics = new HashSet(0);
     private Set ocenas = new HashSet(0);
     private Set skladKlasies = new HashSet(0);
     private Set obecnoscs = new HashSet(0);

    public Uczen() {
    }

	
    public Uczen(long pesel) {
        this.pesel = pesel;
    }
    public Uczen(long pesel, Klasa klasa, String imie, String nazwisko, Autoryzacja autoryzacja, Set rodzics, Set ocenas, Set skladKlasies, Set obecnoscs) {
       this.pesel = pesel;
       this.klasa = klasa;
       this.imie = imie;
       this.nazwisko = nazwisko;
       this.autoryzacja = autoryzacja;
       this.rodzics = rodzics;
       this.ocenas = ocenas;
       this.skladKlasies = skladKlasies;
       this.obecnoscs = obecnoscs;
    }
   
    public long getPesel() {
        return this.pesel;
    }
    
    public void setPesel(long pesel) {
        this.pesel = pesel;
    }
    public Klasa getKlasa() {
        return this.klasa;
    }
    
    public void setKlasa(Klasa klasa) {
        this.klasa = klasa;
    }
    public String getImie() {
        return this.imie;
    }
    
    public void setImie(String imie) {
        this.imie = imie;
    }
    public String getNazwisko() {
        return this.nazwisko;
    }
    
    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }
    public Autoryzacja getAutoryzacja() {
        return this.autoryzacja;
    }
    
    public void setAutoryzacja(Autoryzacja autoryzacja) {
        this.autoryzacja = autoryzacja;
    }
    public Set getRodzics() {
        return this.rodzics;
    }
    
    public void setRodzics(Set rodzics) {
        this.rodzics = rodzics;
    }
    public Set getOcenas() {
        return this.ocenas;
    }
    
    public void setOcenas(Set ocenas) {
        this.ocenas = ocenas;
    }
    public Set getSkladKlasies() {
        return this.skladKlasies;
    }
    
    public void setSkladKlasies(Set skladKlasies) {
        this.skladKlasies = skladKlasies;
    }
    public Set getObecnoscs() {
        return this.obecnoscs;
    }
    
    public void setObecnoscs(Set obecnoscs) {
        this.obecnoscs = obecnoscs;
    }




}


