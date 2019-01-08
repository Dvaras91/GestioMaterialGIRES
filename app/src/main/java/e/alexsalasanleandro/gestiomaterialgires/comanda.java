package e.alexsalasanleandro.gestiomaterialgires;

public class comanda {
    String id;
    String name,usuari,data;
    Boolean entrega;

    public comanda (String name,String usuari,String data){
        this.name = name;
        this.usuari = usuari;
        this.data = data;
        //this.entrega = false;
    }
    public comanda (String name){
        this.name = name;
        this.usuari = "Lola";
        this.data = "01/12/2019";
        //this.entrega = false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsuari() {
        return usuari;
    }

    public void setUsuari(String usuari) {
        this.usuari = usuari;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getEntrega() {
        return entrega;
    }

    public void setEntrega(Boolean entrega) {
        this.entrega = entrega;
    }
}
