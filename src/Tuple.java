public class Tuple {
    public int jump, length;
    public byte nextByte;

    public Tuple(int jump, int length, byte nextByte) {
        this.jump = jump;
        this.length = length;
        this.nextByte = nextByte;
    }

    @Override
    public String toString() {
        return "<" + jump + "," + length + "," + nextByte + ">";
    }
}
