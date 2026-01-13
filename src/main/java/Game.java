import java.util.*;


public class Game {
    private final Scanner sc= new Scanner (System.in);
    private Board board;
    private Queue<Player> zaidejai = new ArrayDeque<>();
    private Random dice = new Random();
    private List<Card> kortos;
    private int tikslas;
    private boolean zaidimasBaigtas = false;

    public Game(Board board, int tikslas){ // preduoda lenta ir tiksla
        this.board = board;
        this.tikslas = tikslas;
    }
    public void setCards(List<Card> kortos){
        this.kortos = kortos;
    }
     public Board getBoard(){
        return board;
    }
    public void addPlayer(Player p){ // prideda zaideja i eile
        p.setPozicija(board.getHead());
        zaidejai.add(p);
    }
    public void zaisti(){
        System.out.println(Colors.GREEN + "\n Zaidimas prasidejo!" + Colors.RESET);
        board.printBoardVisual();
        

        while(true){
            if (checkWinner()) break;

             Player current = zaidejai.poll(); // paima pirma zaideja is eiles ir pasalina
                if(current == null) break;

            if(current.praleidziaEjima){
                System.out.println(Colors.RED + current.vardas + " praleidzia ejima (kalejimas)."+  Colors.RESET);
                current.praleidziaEjima = false;
                zaidejai.add(current); // zaidejas pridedamas i gala
                continue;
            } 
            boolean extraTurn = false;
            do {
                extraTurn = false;

            System.out.println(Colors.BLUE + "\nEina: " + current.vardas + " (" + current.pinigai + "$)" + Colors.RESET);
            System.out.println(Colors.BLUE + "Dabar stovi ant: " + current.pozicija.data.pavadinimas + Colors.RESET);
            System.out.println(Colors.BLUE + " Spausk ENTER, kad mestum kauliuka, arba 'E' kad iseitum is zaidimo." + Colors.RESET);
            String input = sc.nextLine().trim();

            if(input.equalsIgnoreCase("E")){
                current.pasirinkoIseiti = true;

                TextUtils.slowPrint(Colors.BLUE + current.vardas + " pasirinko iseiti is zaidimo."+  Colors.RESET, 35);
                removePlayerAssets(current);
                TextUtils.sleep(800);
                TextUtils.slowPrint(Colors.RED + current.vardas + " paliko zaidima." +  Colors.RESET, 35);

                Queue<Player> likusieji = new ArrayDeque<>();
                for(Player z: zaidejai){
                    if(!z.vardas.equals(current.vardas)){
                        likusieji.add(z);
                    }
                }
                zaidejai = likusieji;

                if(zaidejai.size() <= 1){
                    checkWinner();
                    return;
                }
                continue;
            }


            int roll = dice.nextInt(6)+1; // atsitiktinis sk nuo 1 iki 6
            TextUtils.slowPrint("\n" + current.vardas + " isrideno: " + roll,40);

            Tile tile = movePlayer(current, roll);

            TextUtils.sleep(1000);
            TextUtils.slowPrint(current.vardas + " sustojo ant: " + tile.pavadinimas + " (" + tile.tipas + ")", 40);
            
            if("nuosavybe".equals(tile.tipas)){
              System.out.println(Colors.GREEN + "» Informacija apie laukeli:" +  Colors.RESET);  
              TextUtils.slowPrint(tile.getInfo(),35);
              System.out.println("-----------------------------");
            }

            extraTurn = handleTile(current, tile);


            if(current.pinigai <= 0){
                TextUtils.slowPrint(Colors.RED + "\n" + current.vardas + " bankrutavo!" +  Colors.RESET,35);
                removePlayerAssets(current);
                TextUtils.sleep(1000);
                TextUtils.slowPrint(Colors.RED + current.vardas + " iskrenta iš zaidimo." +  Colors.RESET, 40);

                if (checkWinner()) break;
                break;
            }
        } while(extraTurn);

        if(current.pinigai>0 && !current.pasirinkoIseiti) zaidejai.add(current);
        
    }
}
    private void removePlayerAssets(Player player){
        Node current = board.getHead();
        do{
            Tile t = current.data;

            if(player.vardas.equals(t.savininkas)){
                t.savininkas = null;
                t.pastatai.clear();
            }
            current = current.next;
        } while (current != board.getHead());
        TextUtils.slowPrint(Colors.RED + player.vardas + " pasalintas is zadimo." +  Colors.RESET, 35);
    }

    private boolean checkWinner(){
        if (zaidimasBaigtas) return true;

        if(zaidejai.size() == 1){
            Player winner = zaidejai.peek();
            TextUtils.slowPrint(Colors.GREEN + "\n" + winner.vardas + " laimejo zaidima su " + winner.pinigai + "$!" +  Colors.RESET, 35);
            zaidimasBaigtas = true;
            return true;
            }

        for (Player p : zaidejai){
            if(p.pinigai >= tikslas){
                TextUtils.slowPrint(Colors.GREEN + "\n" + p.vardas + " pasieke "+ tikslas + "$ ir laimejo zaidima!" +  Colors.RESET, 35);
                zaidimasBaigtas = true;
                return true;
            }
        }

        boolean visosParduotos = true;
        Node current = board.getHead();

        do{
            if("nuosavybe".equalsIgnoreCase(current.data.tipas) && current.data.savininkas == null){
                visosParduotos = false;
                break;
            }
            current = current.next;
        } while(current != board.getHead());

        if(visosParduotos){
            
            Player topPlayer = null;
            int maxNuosavybiu = 0;

            for(Player p: zaidejai){
                int count = skaiciuotiNuosavybes(p);
                if(count > maxNuosavybiu){
                    maxNuosavybiu = count;
                    topPlayer = p;
                } else if ( count == maxNuosavybiu && topPlayer != null){
                    boolean pturi3 = turiTrisPastatus(p);
                    boolean turi3 = turiTrisPastatus(topPlayer);
                    if(pturi3 && !turi3) topPlayer = p;
                }
            }
            if(topPlayer != null && turiTrisPastatus(topPlayer)){
                TextUtils.slowPrint(Colors.RED + "\nVisos nuosavybes nupirktos!" +  Colors.RESET, 35);
                TextUtils.slowPrint(Colors.PURPLE + "Daugiausia nuosavybiu turi: " + topPlayer + " (" + maxNuosavybiu + ")" +  Colors.RESET, 35);
                TextUtils.slowPrint(Colors.GREEN + topPlayer.vardas + " laimejo zaidima!" +  Colors.RESET, 30);
                zaidimasBaigtas = true;
                return true;
            }
        }
        return false;
    }
    private int skaiciuotiNuosavybes(Player p){
        int count = 0;
        Node node = board.getHead();

        do{
            Tile t=node.data;
            if("nuosavybe".equalsIgnoreCase(t.tipas) && p.vardas.equals(t.savininkas)){
                count++;
            }
            node = node.next;
        } while(node != board.getHead());
        return count;
    }

    private boolean turiTrisPastatus(Player p){
        Node node = board.getHead();
        do{
            Tile t = node.data;
            if("nuosavybe".equalsIgnoreCase(t.tipas) && p.vardas.equals(t.savininkas)){
                if(t.getPastatuKiekis() >= 3){
                    return true;
                }
            }
            node = node.next;
        } while(node != board.getHead());
        return false;
    }
    private Tile movePlayer(Player p, int steps){
        if(steps <= 0) return p.pozicija.data;

        Node startNode = board.getHead();
        BoardIterator it = new BoardIterator(p.pozicija);
        Tile tile = null;
        boolean perejoStarta = false;

        for(int i=0; i< steps; i++){
            tile = it.next();
            if(it.getCurrentNode() == startNode && !p.pirmasEjimas){
                perejoStarta = true;
            }
        }

        if(perejoStarta){
            p.keistiPinigus(200);
            TextUtils.slowPrint(Colors.GREEN + p.vardas + " gavo 200$ uz praejima per starta!" +  Colors.RESET, 40);

        }

        p.pirmasEjimas = false;
        p.setPozicija(it.getCurrentNode());
        return tile;
    }

    private boolean handleTile(Player p, Tile t){
        switch (t.tipas){
            case "nuosavybe":
            if(t.savininkas == null){
                if(p.pinigai >= t.kaina){
                    System.out.println(p.vardas + ", ar norite isigyti " + t.pavadinimas + " uz " + t.kaina + "$?");
                    TextUtils.sleep(600);
                    System.out.print("Iveskite 1 - TAIP arba 0 - NE:");
                    int choice = sc.nextInt();
                    sc.nextLine();
                    
                    if(choice == 1){
                        t.savininkas = p.vardas;
                        p.keistiPinigus(-t.kaina);
                        TextUtils.sleep(800);
                        TextUtils.slowPrint(Colors.PURPLE + p.vardas + " isigijo " + t.pavadinimas + "!" +  Colors.RESET, 35);
                    } else {
                        TextUtils.slowPrint(Colors.PURPLE + p.vardas + " nusprende neipirkti " + t.pavadinimas + "." +  Colors.RESET, 30);
                    }
                } else {
                    TextUtils.slowPrint(Colors.RED + p.vardas + " neturi pakankamai pinigu nusipirkti " + t.pavadinimas + "." +  Colors.RESET, 30);
                }

            } else if(t.savininkas.equals(p.vardas)){
                int maxPastatai = 3;
                if(t.getPastatuKiekis() >= maxPastatai){
                    System.out.println(Colors.YELLOW + "Jau pastatyti visi pastatai." +  Colors.RESET);
                    break;
                }
                int statybosKaina = t.getKitasPastatoKaina();
                TextUtils.slowPrint(p.vardas + ", ar norite statyti pastata ant " + t.pavadinimas + " uz " + statybosKaina + "$? (1-TAIP, 0-NE)", 30);
                TextUtils.sleep(600);
                System.out.println("Statybos kaina: " + statybosKaina + "$, esamu pastatu skaicius: " + t.getPastatuKiekis());
                System.out.println(Colors.GREEN + "Dabar turi " + p.pinigai + "$." +  Colors.RESET);
                int choice = sc.nextInt();
                sc.nextLine();

                if(choice == 1){
                    if(p.pinigai >= statybosKaina){
                        t.addPastatas();
                        p.keistiPinigus(-statybosKaina);
                        TextUtils.slowPrint(Colors.YELLOW + p.vardas + " pastate pastata ant " + t.pavadinimas + "!" +  Colors.RESET, 30);
                    } else {
                        TextUtils.slowPrint(Colors.RED + p.vardas + " neturi pakankamai pinigu pastatui." +  Colors.RESET, 30);
                    }
                } else {
                    TextUtils.slowPrint(Colors.RED + p.vardas + " nusprende nestatyti pastato." +  Colors.RESET, 30);
                }
                
            } else {
                int rent = t.getBendraNuoma();
                TextUtils.sleep(600);
                TextUtils.slowPrint(Colors.RED + p.vardas + " moka " + rent + "$ nuoma " + t.savininkas +  Colors.RESET, 35);
                TextUtils.sleep(800);
                p.keistiPinigus(-rent);

                for(Player sav: zaidejai) {
                    if(sav.vardas.equals(t.savininkas)){
                        sav.keistiPinigus(rent);
                        TextUtils.sleep(800);
                        TextUtils.slowPrint(Colors.GREEN + t.savininkas + " gavo " + rent + "$ uz nuoma." +  Colors.RESET, 40);
                        break;
                    }
                }
            }
            break;

            case "prison":
                TextUtils.slowPrint(Colors.RED + p.vardas + " pateko i kalejima ir praleis sekanti ejima."+  Colors.RESET, 40);
                p.praleidziaEjima = true;
                break;

            case "police":
                TextUtils.slowPrint(Colors.RED + p.vardas + " buvo suimtas ir yra siunciamas i kalejima!" +  Colors.RESET, 40);
                Node currentnode = board.getHead();

                do{
                    if("prison".equals(currentnode.data.tipas)){
                        p.setPozicija(currentnode);
                        System.out.println(Colors.RED + p.vardas + " dabar yra kelijime."+  Colors.RESET);
                        p.praleidziaEjima = true;
                        break;
                    }
                    currentnode = currentnode.next;
                }while (currentnode != board.getHead());
                break;

            case "loterija":
                if(kortos != null && !kortos.isEmpty()) {
                    Card randomCard = kortos.get(new Random().nextInt(kortos.size()));
                    TextUtils.slowPrint("Loterijos kortele: " + randomCard.text, 35);
                    handleCard(p, randomCard);

                }
                break; 

            case "airport":
                System.out.println(Colors.GREEN + p.vardas + " atvyko i oro uosta!" +  Colors.RESET);
                System.out.println("1. Skristi i atvykimo oro uosta (kaina 200$)");
                System.out.println("2. Skristi i savo nuosavybe (kaina 160$)");
                System.out.println("3. Likti oro uoste");
                System.out.print("Pasirinkite: ");
                int choice = sc.nextInt();;
                sc.nextLine();

                switch (choice){
                    case 1:
                    if( p.pinigai >= 200){
                        Node currentNode = board.getHead();
                        Node arrivalNode = null;
                        do {
                            if("arrival".equalsIgnoreCase(currentNode.data.tipas)){
                                arrivalNode = currentNode;
                                break;
                            }
                            currentNode = currentNode.next;
                        } while (currentNode != board.getHead());
                        if(arrivalNode != null){

                            Node checkNode = p.pozicija;
                            boolean perejoStarta = false;

                            do{
                                checkNode=checkNode.next;
                                if("start".equalsIgnoreCase(checkNode.data.tipas)){
                                    perejoStarta = true;
                                    break;
                                }
                            }while (checkNode != arrivalNode && checkNode != board.getHead());

                            p.keistiPinigus(-250);

                            if(perejoStarta){
                                p.keistiPinigus(200);
                                TextUtils.slowPrint(Colors.GREEN + p.vardas + " praskrido START ir gavo bonusa!" +  Colors.RESET, 40);
                            }

                            p.setPozicija(arrivalNode);
                            TextUtils.sleep(800);
                            System.out.println(p.vardas + " nuskrido i atvykimo oro uosta.");
                            System.out.println(arrivalNode.data.getInfo());
                        } else {
                            System.out.println("Atvykimo oro uostas nerastas.");
                        }
                    } else {
                        System.out.println(Colors.RED + "Neturite pakankamai pinigu skrydziui." +  Colors.RESET);
                }
                break;

                case 2: 
                if(p.pinigai >= 160){
                    List<Node> nuosavybes = new ArrayList<>();
                    Node currentNode = board.getHead();
                    do{
                        if(currentNode.data.savininkas != null && currentNode.data.savininkas.equals(p.vardas)){
                            nuosavybes.add(currentNode);
                        }
                        currentNode = currentNode.next;
                        } while(currentNode != board.getHead());
                    if(nuosavybes.isEmpty()){
                        System.out.println(Colors.RED + "Neturite jokiu nuosavybiu skrydziui." +  Colors.RESET);
                    } else {
                        System.out.println("Pasirinkite nuosavybe skrydziui:");
                        for(int i=0; i<nuosavybes.size(); i++){
                            System.out.println((i+1) + ". " + nuosavybes.get(i).data.pavadinimas);
                        }

                        int pasirinktas = sc.nextInt();
                        sc.nextLine();

                        if(pasirinktas > 0 && pasirinktas <= nuosavybes.size()){
                            Node tiksloLaukas = nuosavybes.get(pasirinktas - 1);
                            p.keistiPinigus(-160);
                            p.setPozicija(tiksloLaukas);
                            TextUtils.slowPrint(Colors.YELLOW + p.vardas + " nuskrido i " + tiksloLaukas.data.pavadinimas +  Colors.RESET, 30);
                            System.out.println(tiksloLaukas.data.getInfo());

                            Tile TavoLaukas = tiksloLaukas.data;
                            if("nuosavybe".equalsIgnoreCase(TavoLaukas.tipas) && p.vardas.equals(TavoLaukas.savininkas)){
                                System.out.println(p.vardas + ", ar norite statyti pastata ant " + TavoLaukas.pavadinimas + " uz " + TavoLaukas.getKitasPastatoKaina() + "$? (1-TAIP, 0-NE)");
                                int statybosChoice = sc.nextInt();
                                sc.nextLine();

                                if(statybosChoice == 1){
                                    int statybosKaina = TavoLaukas.getKitasPastatoKaina();
                                    if(p.pinigai >= statybosKaina){
                                        TavoLaukas.addPastatas();
                                        p.keistiPinigus(-statybosKaina);
                                        TextUtils.slowPrint(Colors.YELLOW + p.vardas + " pastate pastata ant " + TavoLaukas.pavadinimas + "!" +  Colors.RESET, 30);
                                    } else {
                                        TextUtils.slowPrint(Colors.RED + p.vardas + " neturi pakankamai pinigu pastatui." +  Colors.RESET, 30);
                                    }
                                } else {
                                    TextUtils.slowPrint(p.vardas + " nusprende nestatyti pastato.", 30);
                            } }
                        } else {
                            System.out.println("Neteisingas pasirinkimas.");
                        }
                    }
                    } else {
                        TextUtils.slowPrint(Colors.RED + "Neturite pakankamai pinigu skrydziui."+  Colors.RESET, 30);
                    }
                break;

                case 3:
                    TextUtils.slowPrint(p.vardas + " nusprende likti oro uoste.", 20);
                    break;

                    default:
                    System.out.println("Neteisingas pasirinkimas. Nieko nevyksta.");
                    break;
            }
            break;

            case "specialus":
            System.out.println(Colors.GREEN + p.vardas + " gauna papildoma metima!" +  Colors.RESET);
            return true;

                default:
                    System.out.println("Nieko nevyksta.");
        }
        System.out.println(Colors.GREEN + p.vardas + " dabar turi " + p.pinigai + "$." +  Colors.RESET);
        TextUtils.sleep(700);
        return false;
    }
    private boolean handleCard(Player p, Card c){
        if( c== null) return false;
        switch(c.action){
            case "addMoney":
                p.keistiPinigus(c.amount);
                TextUtils.slowPrint("Gavai " + c.amount + "$", 30);
                break;

            case "removeMoney":
                p.keistiPinigus(-c.amount);
                TextUtils.slowPrint("Sumokejai " + c.amount + "$", 30);
                break;
            case "skip":
                p.praleidziaEjima = true;
                TextUtils.slowPrint("Turi praleisti 1 ejima.", 30);
                break;

            default:
                System.out.println("Nera tokios korteles veiksmo.");
        }
        return false;
    }
}