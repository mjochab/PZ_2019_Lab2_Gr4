package mapping;
// Generated 6 kwi 2019, 22:02:14 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Klasa generated by hbm2java
 */
public class Klasa  implements java.io.Serializable {


     private String nazwaKlasy;
     private Date rokSzkolny;
     private Integer wychowawca;

    public Klasa() {
    }

	
    public Klasa(String nazwaKlasy) {
        this.nazwaKlasy = nazwaKlasy;
    }
    public Klasa(String nazwaKlasy, Date rokSzkolny, Integer wychowawca) {
       this.nazwaKlasy = nazwaKlasy;
       this.rokSzkolny = rokSzkolny;
       this.wychowawca = wychowawca;
    }
   
    public String getNazwaKlasy() {
        return this.nazwaKlasy;
    }
    
    public void setNazwaKlasy(String nazwaKlasy) {
        this.nazwaKlasy = nazwaKlasy;
    }
    public Date getRokSzkolny() {
        return this.rokSzkolny;
    }
    
    public void setRokSzkolny(Date rokSzkolny) {
        this.rokSzkolny = rokSzkolny;
    }
    public Integer getWychowawca() {
        return this.wychowawca;
    }
    
    public void setWychowawca(Integer wychowawca) {
        this.wychowawca = wychowawca;
    }




}

