package firstconnection;

/*POJO - Plain Old Java Object
    osztályváltozókat, getter/settert és konstruktort tartalmaznak maximum
    nincs bennük általában semmilyen egyéb függvény/metódus
    Az adatbázishoz csinált pojo speciális -> entitásnak nevezzük
    A tábla neve a users, ennek egy sorából csinálunk objektumot a User osztállyal
        - a tábla minden oszlopához kell osztályváltozó
        -egy reord (sor) képvisel egy objetumot*/
public class User {
    private String name;
    private String address;

    public User(){
        
    }
    
    public User(String name, String address){
        this.name = name;
        this.address = address;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }  
}
