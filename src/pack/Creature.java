package pack;

public abstract class Creature {
	Creature(int x, int y, double hp){
		this.setX(x);
		this.setY(y);
		this.hp = hp;
	}
	
	private double x;
	private double y;
	
	private double hp;
	
	private float[] color = new float[3];
	
	public abstract void act(WorldRunner wr);	

	public float[] getColor() {
		return color;
	}

	public void setColor(float[] color) {
		this.color = color;
	}
	
	public void setColor(float r, float g, float b) {
		float[] tmp = {r, g, b};
		this.color = tmp;
	}

	public double getHp() {
		return hp;
	}

	public void setHp(double hp) {
		this.hp = hp;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
