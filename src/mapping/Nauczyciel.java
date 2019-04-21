package mapping;
// Generated 21 kwi 2019, 11:03:06 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Nauczyciel generated by hbm2java
 */
public class Nauczyciel  implements java.io.Serializable {


     private long pesel;
     private String imie;
     private String nazwisko;
     private Set zajecias = new HashSet(0);
     private Set klasas = new HashSet(0);
     private Autoryzacja autoryzacja;

    public Nauczyciel() {
    }

	
    public Nauczyciel(long pesel) {
        this.pesel = pesel;
    }
    public Nauczyciel(long pesel, String imie, String nazwisko, Set zajecias, Set klasas, Autoryzacja autoryzacja) {
       this.pesel = pesel;
       this.imie = imie;
       this.nazwisko = nazwisko;
       this.zajecias = zajecias;
       this.klasas = klasas;
       this.autoryzacja = autoryzacja;
    }
   
    public long getPesel() {
        return this.pesel;
    }
    
    public void setPesel(long pesel) {
        this.pesel = pesel;
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
    public Set getZajecias() {
        return this.zajecias;
    }
    
    public void setZajecias(Set zajecias) {
        this.zajecias = zajecias;
    }
    public Set getKlasas() {
        return this.klasas;
    }
    
    public void setKlasas(Set klasas) {
        this.klasas = klasas;
    }
    public Autoryzacja getAutoryzacja() {
        return this.autoryzacja;
    }
    
    public void setAutoryzacja(Autoryzacja autoryzacja) {
        this.autoryzacja = autoryzacja;
    }




}


