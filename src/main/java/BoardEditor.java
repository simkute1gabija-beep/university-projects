import java.util.*;

public class BoardEditor {
    private Board board;
    private Scanner sc = new Scanner(System.in);

    public BoardEditor(Board board){
        this.board = board;
    }

    public void EditMeniu(){
        while(true){
            System.out.println("\n=== LENTOS REDAGAVIMAS ===");
            System.out.println("Dabartiniu laukeliu skaicius: " + board.size());
            board.printBoardVisual();

            System.out.println("1. Prideti nauja laukeli");
            System.out.println("2. Istrinti laukeli pagal pozicija");
            System.out.println("3. Istrinti laukeli pagal pavadinima");
            System.out.println("4. perziureti lenta");
            System.out.println("0. Baigti redagavima");
            System.out.print("Pasirinkite: ");

            int pasirinkimas = sc.nextInt();
            sc.nextLine();

            switch(pasirinkimas){
                case 1:
                    pridetiNaujaLaukeli();
                    break;
                case 2:
                    istrintiPagalPozicija();
                    break;
                case 3:
                    istrintiPagalPavadinima();
                    break;
                case 4:
                    board.printBoardVisual();
                    break;
                case 0:
                System.out.println("Redagavimas baigtas.");
                return;
                default:
                System.out.println("Neteisingas pasirinkimas.");
            }
        }
    }

    private void pridetiNaujaLaukeli(){
        System.out.println("\n--- NAUJO LAUKELIO KURIMAS ---");
        System.out.println("Pavadinimas: ");
        String pavadinimas = sc.nextLine();

        System.out.print("Tipas (loterija/nuosavybe/prison/airport/specialus): ");
        String tipas = sc.nextLine();

        Tile naujasTile;

        if(tipas.equalsIgnoreCase("nuosavybe")){
            System.out.print("Kaina: ");
            int kaina = sc.nextInt();
            System.out.print("Nuoma: ");
            int nuoma = sc.nextInt();
            System.out.print("Pastato kaina: ");
            int pasatatoKaina = sc.nextInt();
            sc.nextLine();
            System.out.println("Spalva: ");
            String spalva = sc.nextLine();

            naujasTile = new Tile(pavadinimas, tipas, kaina, nuoma, pasatatoKaina, spalva);
        } else {
            naujasTile = new Tile(pavadinimas, tipas);
        }

        System.out.print("i kuria pozicija ideti (0 - pradzia, " + board.size() + " - galas): ");
        int pozicija = sc.nextInt();
        sc.nextLine();

        board.insertTileAt(pozicija, naujasTile);
        System.out.println("Laukelis pridetas!");
    }

    private void istrintiPagalPozicija(){
        System.out.print("Iveskite laukelio pozicija: ");
        int pozicija = sc.nextInt();
        sc.nextLine();
        boolean pasalinta = board.removeTileAt(pozicija);
        System.out.println(pasalinta ? "Laukelis pasalintas." : "Pzocija nerasta.");//sąlyga ? reikšmė_jei_true : reikšmė_jei_false
    }
    //klaustukai tai kaip else if ir veikia kaip true, jeigu pasalinta tai isveda laukelis pasalintas, jeigu ne tai kita isveda
    private void istrintiPagalPavadinima(){
        System.out.print("Iveskite laukelio pavadinima: ");
        String pav = sc.nextLine();
        boolean pasalinta = board.removeTileByName(pav);
        System.out.println(pasalinta ? "Laukelis pasalintas." : "Pavadinimas nerastas.");
    }
}
