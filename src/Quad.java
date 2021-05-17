public class Quad {
	
	public int x;
	public int y;
	public int w;
	public int h;

	public Quad(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public boolean Contains(Dot dot) {
		
		return (dot.x > this.x &&
				dot.x < this.x + this.w &&
				dot.y > this.y &&
				dot.y < this.y + this.h);
	}
}