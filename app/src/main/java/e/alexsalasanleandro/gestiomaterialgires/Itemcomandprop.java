package e.alexsalasanleandro.gestiomaterialgires;

public class Itemcomandprop {
    private String text,id;
    private boolean checked;
    private int numlloguer, numtotal,precio;

    public Itemcomandprop(String text) {
        this.text = text;
        this.checked = false;
        this.numlloguer =1;
        this.numtotal=1;
    }

    public Itemcomandprop(String text, String id) {
        this.text = text;
        this.id = id;
    }

    public Itemcomandprop(String text, boolean checked) {
        this.text = text;
        this.checked = checked;
        this.numlloguer =1;
        this.numtotal=1;
    }

    public Itemcomandprop(String text, int numtotal) {
        this.text = text;
        this.numtotal = numtotal;
    }

    public Itemcomandprop(String text, boolean checked, int numlloguer, int numtotal) {
        this.text = text;
        this.checked = checked;
        this.numlloguer = numlloguer;
        this.numtotal = numtotal;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toggleChecked() {
        this.checked = !this.checked;
    }

    public int getNumlloguer() {
        return numlloguer;
    }

    public void setNumlloguer(int numlloguer) {
        this.numlloguer = numlloguer;
    }

    public int getNumtotal() {
        return numtotal;
    }

    public void setNumtotal(int numtotal) {
        this.numtotal = numtotal;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
