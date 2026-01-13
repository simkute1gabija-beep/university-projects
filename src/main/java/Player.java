public class Player {
    String vardas;
    int pinigai;
    Node pozicija;
    int pozicijaIndex = 0;
    boolean praleidziaEjima = false;

    public Player(String vardas) {
        this.vardas = vardas;
        this.pinigai = 200;
    }
    public void keistiPinigus(int suma){
        pinigai += suma;
    }
    public void setPozicija(Node pozicija){
        this.pozicija = pozicija;
    }
    public String toString(){
        return vardas + " (" + pinigai + "$)";
    }
    public boolean pirmasEjimas = true;

    public boolean pasirinkoIseiti = false;
    
    public void moveSteps(int steps, Board board){
        if(pozicija == null) return;

        BoardIterator it = new BoardIterator(pozicija);
        Tile tile = null;
        for(int i=0; i<steps; i++){
            tile = it.next();
        }
        setPozicija(it.getCurrentNode());
        System.out.println(vardas + "pajudejo "+ steps + " laukeius ir sustojo ant: "
        + tile.pavadinimas + " (" + tile.tipas + ")");
        if("nuosavybe".equalsIgnoreCase(tile.tipas)){
            System.out.println(tile.getInfo());
        }
    }
        public void moveToTileById(int id, Board board) { // nukelia zaideja i konkretu
    Node current = board.getHead();
    Node found = null;

    do {
        if (current.data.id == id) {
            found = current;
            break;
        }
        current = current.next;
    } while (current != board.getHead());

    if (found != null) {
        setPozicija(found);
        System.out.println(vardas + " persoko į laukeli " + found.data.pavadinimas + " (" + found.data.tipas + ")");
    } else {
        System.out.println("Laukelis su id " + id + " nerastas lentoje.");
    }
}
}
