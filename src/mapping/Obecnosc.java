package mapping;
// Generated 6 kwi 2019, 22:02:14 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Obecnosc generated by hbm2java
 */
public class Obecnosc  implements java.io.Serializable {


     private int pesel;
     private Date data;
     private Boolean status;

    public Obecnosc() {
    }

	
    public Obecnosc(int pesel) {
        this.pesel = pesel;
    }
    public Obecnosc(int pesel, Date data, Boolean wartosc) {
       this.pesel = pesel;
       this.data = data;
       this.status = wartosc;
    }
   
    public int getPesel() {
        return this.pesel;
    }
    
    public void setPesel(int pesel) {
        this.pesel = pesel;
    }
    public Date getData() {
        return this.data;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    public Boolean getStatus() {
        return this.status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }




}


