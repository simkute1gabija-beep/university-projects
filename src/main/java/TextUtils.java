public class TextUtils {
    
    public static void sleep(int ms){
        try{
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public static void slowPrint(String text, int delay){
        for(char c : text.toCharArray()){
            System.out.print(c);
            try{
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
        System.out.println();
    }
}
