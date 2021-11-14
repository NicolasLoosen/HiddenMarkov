public class Node {
    public int prevState;
	public double value;
	public double forward;
	public double backward;
	public double posteriori;

	public Node( int prevState, double value) {
		this.prevState = prevState;
		this.value = value;
	}
}
