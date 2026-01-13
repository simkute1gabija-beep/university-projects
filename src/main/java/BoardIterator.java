public class BoardIterator {
    private Node current;

    public BoardIterator(Node start) {
        this.current = start;
    }

    public Tile next() {
        current = current.next;
        return current.data;
    }
    public Node getCurrentNode() {
        return current;
    }
}
