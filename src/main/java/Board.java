import java.util.*;

// lentos mazgas
class Node {
    Tile data;
    Node next;
    Node(Tile data) {
        this.data = data;
        this.next = null;
    }
}

public class Board {
    private Node head = null;
    private Node tail = null;

    // prideda nauja laukeli i lentos pabaiga
    public void addTile(Tile tile) {
        Node newNode = new Node(tile);
        if (head == null) {
            head = newNode;
            tail = newNode;
            newNode.next = head; // Circular link
        } else {
            tail.next = newNode;
            tail = newNode;
            tail.next = head; // Maintain circular link
        }
    }
    //spausdinimas
    public void printBoardVisual(){
        if (head == null) return;
        Node current = head;
        

        System.out.println(Colors.GREEN + "\nZaidimo lenta:" + Colors.RESET);
        System.out.println("___________________________________");
        
        int index = 0;

        do {
            Tile t= current.data;
            String simbolis;
            
            switch (t.tipas) {
                case "start":
                    simbolis = "[ S ]";
                    break;
                case "prison":
                    simbolis = "[ P ]";
                    break;
                case "loterija":
                    simbolis = "[ L ]";
                    break;
                case "airport":
                    simbolis = "[ A ]";
                    break;
                case "nuosavybe":
                    simbolis = "[ $ ]";
                    break;
                case "specialus":
                    simbolis = "[ @ ]";
                    break;
                default:
                    simbolis = "[ ]";
            }

            if(t.tipas.equals("nuosavybe")){//skaiciukai tai kad praleistu kazkiek simboliu kieki
                System.out.printf("%d %s %-15s | Kaina: %4d$ | Spalva: %-8s%n", 
                index, simbolis, t.pavadinimas, t.kaina, t.spalva);
            } else {
                System.out.printf("%d %s %-15s | Tipas: %s%n",
                index, simbolis, t.pavadinimas, t.tipas); 
            }
            current = current.next;
            index++;

        } while (current != head);
    System.out.println("___________________________________");
        System.out.println("<> (grizta i starta)\n");
    }
    public Node getHead() {
        return head;
    }

    public int size() {
        if( head == null) return 0;
        int count = 0;
        Node current = head;
        do{
            count++;
            current = current.next;
        } while (current != head);
        return count;
    }

    public void insertTileAt( int position, Tile tile){
        if("start".equalsIgnoreCase(tile.tipas)){
            System.out.println(Colors.RED + "Negalima prideti dar vieno starto laukelio!" + Colors.RESET);
            return;
        }
        if(position <= 0){
            System.out.println(Colors.RED + "Negalima iterpti pries starto laukeli!"+ Colors.RESET);
            return;
        }


        Node newNode = new Node(tile);

        if(head == null){

            head = newNode;
            tail = newNode;
            newNode.next = head;
            return;
        }
        int size = size();
        if( position <= 0){
            newNode.next = head;
            head = newNode;
            tail.next = head;
            return;
        }

        if( position >= size){
            tail.next = newNode;
            tail= newNode;
            tail.next = head;
            return;
        }

        Node prev = head;
        for(int i = 0; i <position - 1; i++){
            prev = prev.next;
        }

        newNode.next = prev.next;
        prev.next = newNode;
    }

    public boolean removeTileAt( int position){
        if (head== null) return false;

        int size = size();
        if(position < 0 || position >= size) return false;

        if(position == 0){
            System.out.println(Colors.RED + "Negalima istrinti starto laukelio!"+ Colors.RESET);
            return false;
        }

        if(size == 1){
            System.out.println(Colors.RED + "Lentroje liko tik startas - negalima jo trinti!"+ Colors.RESET);
            return true;
        }

        if(position == 0){
            head=head.next;
            tail.next = head;
            return true;
        }

        Node prev = head;
        for(int i = 0; i< position - 1; i++){
            prev = prev.next;
        }

        Node toDelete = prev.next;
        if("start".equalsIgnoreCase(toDelete.data.tipas)){
            System.out.println(Colors.RED + "Negalima istrinti starto laukelio!"+ Colors.RESET);
            return false;
        }

        prev.next = toDelete.next;

        if(toDelete == tail) tail= prev;
        return true;
    }

    public boolean removeTileByName(String name){
        if(head == null) return false;

        List<Integer> matches = new ArrayList<>();
        Node current = head;
        int index = 0;

        do{
            if(current.data.pavadinimas.equalsIgnoreCase(name)){
                if("start".equalsIgnoreCase(current.data.tipas)){
                    System.out.println(Colors.RED + "Negalima istrinti starto laukelio!"+ Colors.RESET);
                    return false;
                }
                matches.add(index);
            }
            current = current.next;
            index++;
        }while(current != head);

        if(matches.isEmpty()){
            System.out.println(Colors.RED + "Toks pavadinimas nerastas."+ Colors.RESET);
            return false;
        }

        if(matches.size() == 1){
            return removeTileAt(matches.get(0));
        }
        System.out.println("Rasti keli laukeliai su tokiu pavadinimu:");
        for(int i = 0; i< matches.size(); i++){
            System.out.println((i+1) + ". Pozicija: " + matches.get(i));
        }

        Scanner sc =  new Scanner(System.in);
        System.out.print("Pasirinkite kuri trinti: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if( choice < 1 || choice > matches.size()){
            System.out.println(Colors.RED + "Neteisingas pasirinkimas." + Colors.RESET);
            return false;
        }

        return removeTileAt(matches.get(choice - 1));
    }
}
