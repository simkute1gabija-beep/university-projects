import com.google.gson.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader("data/lenta.json");

           JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
           Tile[] tiles = gson.fromJson(jsonObject.getAsJsonArray("board"), Tile[].class);
              
           Board board = new Board();
              for(Tile t : tiles) {
                  board.addTile(t);
              }

              Card[] cards = gson.fromJson(jsonObject.getAsJsonArray("cards"), Card[].class);
              List<Card> kortos = Arrays.asList(cards);

            reader.close();

            Game game = Menu.sukurtiZaidima(board);
            game.setCards(kortos);
            game.zaisti();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
