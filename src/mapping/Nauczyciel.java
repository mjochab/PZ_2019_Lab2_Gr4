package mapping;
// Generated 20 kwi 2019, 14:16:52 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Nauczyciel generated by hbm2java
 */
public class Nauczyciel  implements java.io.Serializable {


     private long pesel;
     private String imie;
     private String nazwisko;
     private Set klasas = new HashSet(0);
     private Set przedmiotPomocniczas = new HashSet(0);
     private Autoryzacja autoryzacja;

    public Nauczyciel() {
    }

	
    public Nauczyciel(long pesel) {
        this.pesel = pesel;
    }
    public Nauczyciel(long pesel, String imie, String nazwisko, Set klasas, Set przedmiotPomocniczas, Autoryzacja autoryzacja) {
       this.pesel = pesel;
       this.imie = imie;
       this.nazwisko = nazwisko;
       this.klasas = klasas;
       this.przedmiotPomocniczas = przedmiotPomocniczas;
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
    public Set getKlasas() {
        return this.klasas;
    }
    
    public void setKlasas(Set klasas) {
        this.klasas = klasas;
    }
    public Set getPrzedmiotPomocniczas() {
        return this.przedmiotPomocniczas;
    }
    
    public void setPrzedmiotPomocniczas(Set przedmiotPomocniczas) {
        this.przedmiotPomocniczas = przedmiotPomocniczas;
    }
    public Autoryzacja getAutoryzacja() {
        return this.autoryzacja;
    }
    
    public void setAutoryzacja(Autoryzacja autoryzacja) {
        this.autoryzacja = autoryzacja;
    }




}


