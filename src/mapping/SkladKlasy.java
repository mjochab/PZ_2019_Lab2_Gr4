package mapping;
// Generated 9 maj 2019, 12:00:04 by Hibernate Tools 4.3.1



/**
 * SkladKlasy generated by hbm2java
 */
public class SkladKlasy  implements java.io.Serializable {


     private Integer id;
     private Klasa klasa;
     private Uczen uczen;

    public SkladKlasy() {
    }

    public SkladKlasy(Klasa klasa, Uczen uczen) {
       this.klasa = klasa;
       this.uczen = uczen;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Klasa getKlasa() {
        return this.klasa;
    }
    
    public void setKlasa(Klasa klasa) {
        this.klasa = klasa;
    }
    public Uczen getUczen() {
        return this.uczen;
    }
    
    public void setUczen(Uczen uczen) {
        this.uczen = uczen;
    }




}


