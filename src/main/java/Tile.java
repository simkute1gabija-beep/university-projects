import java.util.Stack;

public class Tile {
    private static int nextId = 1;
    int id;
    String pavadinimas;
    String tipas;
    int kaina;
    String savininkas;
    String spalva;
    int pastatoKaina;
    Stack<Integer> pastatai = new Stack<>();
    int nuoma;
    int pastatoNuoma = 20;

    
    public Tile(String pavadinimas, String tipas){
        this.id = nextId++;
        this.pavadinimas = pavadinimas;
        this.tipas = tipas;
        this.kaina = 0;
        this.nuoma = 0;
        this.pastatoKaina = 0;
        this.spalva = null;
        this.savininkas = null;
    }
    public Tile(String pavadinimas, String tipas, int kaina, int nuoma, int pastatoKaina, String spalva){
        this.id = nextId++;
        this.pavadinimas = pavadinimas;
        this.tipas = tipas;
        this.kaina = kaina;
        this.nuoma = nuoma;
        this.pastatoKaina = pastatoKaina;
        this.spalva = spalva;
        this.savininkas = null;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id =id;
        if(id >= nextId) nextId = id + 1;
    }
    public int getBendraNuoma(){
        return nuoma + (pastatai.size() * pastatoNuoma);
    }
    public int getKitasPastatoKaina(){
        return pastatoKaina + (pastatai.size() * 20);
    }
        public void addPastatas(){
            if(pastatai.size()<3){
                pastatai.push(pastatoNuoma);
            } else {
                System.out.println("Negalima statyti daugiau pastatu");
            }
        }
        public int getPastatuKiekis(){
            return pastatai.size();
        }
        public void removePastatas(){
            if(!pastatai.isEmpty()){
                pastatai.pop();
            } else {
                System.out.println("Nera pastatu kuriuos butu galima nuimti");
            }
        }
        public String getInfo() {
            String sav = (savininkas == null) ? "Niekam nepriklauso " : "priklauso " + savininkas;
            String past = (pastatai.isEmpty()) ? "Nera pastatu" : pastatai.size() + " pastatai";
            return String.format("Laukelis %s: %s, %s, dabartine nuoma: %d$", pavadinimas, sav, past, getBendraNuoma());
        }

    }

