import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;

public class Menu {
    public static Game sukurtiZaidima(Board board) {
        Scanner sc = new Scanner(System.in);
        Game game = null;
        boolean paleistiZadima = false;

        while (!paleistiZadima) {
        System.out.println(Colors.GREEN +"== MONOPOLIS ==" + Colors.RESET);
        System.out.println("1. Skaityti taisykles");
        System.out.println("2. Redaguoti zaidimo lenta");
        System.out.println("3. Pradeti zaidima");
        System.out.println("0. Iseiti");
        System.out.print("Pasirinkite: ");

        String pasirinkimas = sc.nextLine();
        

        switch (pasirinkimas) {
            case "1":
                spausdintiTaisykles();
                System.out.println("\n(Paspauskite enter, kad griztumete i meniu)");
                sc.nextLine();
                break;
            case "2":
                BoardEditor editor = new BoardEditor(board);
                editor.EditMeniu();
                System.out.println(Colors.GREEN + "\n Lenta sekmingai atnaujinta!" + Colors.RESET);
                System.out.println("(Paspauskite ENTER, jo griztumete i meniu)");
                sc.nextLine();
                break;
            case "3":
            System.out.println(Colors.GREEN + "Pradedamas zaidimas..." + Colors.RESET);
            System.out.println( "Iveskite, kiek zaideju (2-3)");

            int kiek = sc.nextInt();
            sc.nextLine();

            System.out.print("Iveskite zaidimo tiksla (min 300$)");
            int tikslas = sc.nextInt();
            sc.nextLine();

            game = new Game(board, tikslas);

            for(int i=1; i<=kiek; i++){
            System.out.print("Iveskite " + i + " zaidejo varda: ");
            String vardas = sc.nextLine();
            game.addPlayer(new Player(vardas));
        }
        paleistiZadima = true;
        break;

        case "0":
            System.out.println(Colors.GREEN + "Iseinama is zaidimo. Viso gero!" + Colors.RESET);
            System.exit(0);
            break;

        default:
            System.out.println("Neteisingas pasirinkimas. Bandykite dar karta.");
            break;
        }
    }
        return game;

}
    private static void spausdintiTaisykles(){
        System.out.println(Colors.GREEN + "\n=== TAISYKLES ===" + Colors.RESET);
        try (BufferedReader br = new BufferedReader(new FileReader("data/rules.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Klaida skaitant taisykles: " + e.getMessage());
        }
        System.out.println("\n------------------------\n");
    }
}

