package mapping;
// Generated 9 maj 2019, 12:00:04 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Klasa generated by hbm2java
 */
public class Klasa  implements java.io.Serializable {


     private String nazwaKlasy;
     private Nauczyciel nauczyciel;
     private Date rokSzkolny;
     private Set zajecias = new HashSet(0);
     private Set skladKlasies = new HashSet(0);
     private Set uczens = new HashSet(0);

    public Klasa() {
    }

	
    public Klasa(String nazwaKlasy) {
        this.nazwaKlasy = nazwaKlasy;
    }
    public Klasa(String nazwaKlasy, Nauczyciel nauczyciel, Date rokSzkolny, Set zajecias, Set skladKlasies, Set uczens) {
       this.nazwaKlasy = nazwaKlasy;
       this.nauczyciel = nauczyciel;
       this.rokSzkolny = rokSzkolny;
       this.zajecias = zajecias;
       this.skladKlasies = skladKlasies;
       this.uczens = uczens;
    }
   
    public String getNazwaKlasy() {
        return this.nazwaKlasy;
    }
    
    public void setNazwaKlasy(String nazwaKlasy) {
        this.nazwaKlasy = nazwaKlasy;
    }
    public Nauczyciel getNauczyciel() {
        return this.nauczyciel;
    }
    
    public void setNauczyciel(Nauczyciel nauczyciel) {
        this.nauczyciel = nauczyciel;
    }
    public Date getRokSzkolny() {
        return this.rokSzkolny;
    }
    
    public void setRokSzkolny(Date rokSzkolny) {
        this.rokSzkolny = rokSzkolny;
    }
    public Set getZajecias() {
        return this.zajecias;
    }
    
    public void setZajecias(Set zajecias) {
        this.zajecias = zajecias;
    }
    public Set getSkladKlasies() {
        return this.skladKlasies;
    }
    
    public void setSkladKlasies(Set skladKlasies) {
        this.skladKlasies = skladKlasies;
    }
    public Set getUczens() {
        return this.uczens;
    }
    
    public void setUczens(Set uczens) {
        this.uczens = uczens;
    }




}


