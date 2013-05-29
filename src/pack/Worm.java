package pack;

public abstract class Worm extends Creature{

	Worm(int x, int y, double hp, double speed) {
		super(x, y, hp);
		
		this.speed = speed;
		
		for(int i = 0; i < staart.length; i++){
			staart[i][0] = -1;
			staart[i][1] = -1;
		}
		
		staart[0][0] = x;
		staart[0][1] = y;
		
		for(int i = 0; i < headPoints.length; i++){
			headPoints[i][0] = x;
			headPoints[i][1] = y;
		}

		staartLength = 1;
	}
	
	private double[][] staart = new double[512][2];
	private double[][] headPoints = new double[512][2];
	
	private double[] goal;
	private int staartLength;
	private double speed;
	
	private final int fragmentDistance = 7; //Set the distance between differnt 'tale parts'
	
	protected void fixLength(){
		staartLength = 0;
		for(int i = 0; i < staart.length; i++){
			if(staart[i][0] != -1 && staart[i][1] != -1){
				staartLength++;
				continue;
			}
		}
		
		if(staartLength > getHp()){
			staart[staartLength - 1][0] = -1;
			staart[staartLength - 1][1] = -1;
		}
		
		if(staartLength < getHp()){
			try{
				staart[staartLength][0] = staart[staartLength - 1][0];
				staart[staartLength][1] = staart[staartLength - 1][1];
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println("That happend...");
				this.setHp(0);
			}
		}
	}
	
	protected void move(double x, double y){
		if(getHp() == -1 || x < 0 || y < 0 || x > ScreenDisplay.screenWidth || y > ScreenDisplay.screenHeight){
			return;
		}
		while(staartLength != Math.ceil( getHp() ) ){
			fixLength();
		}
		
		//System.out.println("Moveing a worm from: ("+getX()+", "+getY()+") to: ("+x+", "+y+")");
		
		for(int i = staartLength - 1; i >= 0; i--){
			if(staart[i] != null){				
				staart[i][0] = headPoints[i][0];
				staart[i][1] = headPoints[i][1];
			}else{
				//break;
			}
		}
		
		if(Math.max(headPoints[0][0], x) - Math.min(headPoints[0][0], x) > fragmentDistance || Math.max(headPoints[0][1], y) - Math.min(headPoints[0][1], y) > fragmentDistance)
		if(headPoints[0][0] - x > fragmentDistance || headPoints[0][1] - y > fragmentDistance || x - headPoints[0][0] > fragmentDistance || y - headPoints[0][1] > fragmentDistance){
			for(int i = headPoints.length - 1; i > 0; i--){
				if(headPoints[i - 1] != null){
					if(i > 0){
						headPoints[i][0] = headPoints[i - 1][0];
						headPoints[i][1] = headPoints[i - 1][1];
					}
				}
			}
			headPoints[0][0] = x;
			headPoints[0][1] = y;
		}
		
		staart[0][0] = getX();
		staart[0][1] = getY();
		setX(x);
		setY(y);
	}
	
	protected void split(WorldRunner wr){
		if(this instanceof GreenWorm){
			if(getSpeed() > 0.5){
				wr.createGreenWorm((int)(getX() + 20), (int)(getY() - 20), getHp() / 2, getSpeed() + ((Math.random() * 1) - 0.5), getColor());
				wr.createGreenWorm((int)(getX() - 20), (int)(getY() + 20), getHp() / 2, getSpeed() + ((Math.random() * 1) - 0.5), getColor());
			}else{
				wr.createGreenWorm((int)(getX() + 20), (int)(getY() - 20), getHp() / 2, getSpeed() + ((Math.random() * 0.4) - 0.2), getColor());
				wr.createGreenWorm((int)(getX() - 20), (int)(getY() + 20), getHp() / 2, getSpeed() + ((Math.random() * 0.4) - 0.2), getColor());
			}
		}else if(this instanceof RedWorm){
			wr.createRedWorm((int)(getX() + 20), (int)(getY() - 20), getHp() / 2, getSpeed()/* + ((Math.random() * 0.5) - 0.25)*/);
			wr.createRedWorm((int)(getX() - 20), (int)(getY() + 20), getHp() / 2, getSpeed()/* + ((Math.random() * 0.5) - 0.25)*/);
		}
		
		for(int i = 0; i < wr.creatures.length; i++){
			if(wr.creatures[i] == this){
				wr.creatures[i] = null;
				break;
			}
		}
		this.setHp(-1);
	}
	
	protected void eat(Creature c, WorldRunner wr){
		for(int i = 0; i < wr.creatures.length; i++){
			if(wr.creatures[i] == c){
				wr.creatures[i] = null;
				break;
			}
		}
		
		if(c instanceof Plant){
			setHp(getHp() + (c.getHp() / 10.0));
		}else{
			setHp(getHp() + (c.getHp() / 1.2));
		}
		c.setHp(-1);
	}
	
	public double[][] getHeadPoints(){
		return headPoints;
	}
	
	public double[][] getStaart(){
		return staart;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed; 
	}

	public double[] getGoal() {
		return goal;
	}

	public void setGoal(double[] goal) {
		this.goal = goal;
	}
}
