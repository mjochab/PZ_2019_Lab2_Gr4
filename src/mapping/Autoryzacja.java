package mapping;
// Generated 22 kwi 2019, 16:31:19 by Hibernate Tools 4.3.1

/**
 * Autoryzacja generated by hbm2java
 */
public class Autoryzacja  implements java.io.Serializable {

     private long pesel;
     private Nauczyciel nauczyciel;
     private String login;
     private String haslo;
     private String kto;

    public Autoryzacja() {
    }

    public Autoryzacja(Nauczyciel nauczyciel, String login, String haslo, String kto) {
       this.nauczyciel = nauczyciel;
       this.login = login;
       this.haslo = haslo;
       this.kto = kto;
    }
   
    public long getPesel() {
        return this.pesel;
    }
    
    public void setPesel(long pesel) {
        this.pesel = pesel;
    }
    public Nauczyciel getNauczyciel() {
        return this.nauczyciel;
    }
    
    public void setNauczyciel(Nauczyciel nauczyciel) {
        this.nauczyciel = nauczyciel;
    }
    public String getLogin() {
        return this.login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    public String getHaslo() {
        return this.haslo;
    }
    
    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }
    public String getKto() {
        return this.kto;
    }
    
    public void setKto(String kto) {
        this.kto = kto;
    }




}


