package mapping;
// Generated 6 kwi 2019, 22:02:14 by Hibernate Tools 4.3.1



/**
 * Rodzic generated by hbm2java
 */
public class Rodzic  implements java.io.Serializable {


     private int pesel;
     private Integer dziecko;
     private String imie;
     private String nazwisko;

    public Rodzic() {
    }

	
    public Rodzic(int pesel) {
        this.pesel = pesel;
    }
    public Rodzic(int pesel, Integer dziecko, String imie, String nazwisko) {
       this.pesel = pesel;
       this.dziecko = dziecko;
       this.imie = imie;
       this.nazwisko = nazwisko;
    }
   
    public int getPesel() {
        return this.pesel;
    }
    
    public void setPesel(int pesel) {
        this.pesel = pesel;
    }
    public Integer getDziecko() {
        return this.dziecko;
    }
    
    public void setDziecko(Integer dziecko) {
        this.dziecko = dziecko;
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




}


