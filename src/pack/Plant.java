package pack;

public class Plant extends Creature{

	Plant(int x, int y, int stemX, int stemY, double hp) {
		super(x, y, hp);
		
		int[] stemAr = {stemX, stemY};
		setStem(stemAr);
		
		this.setColor(0.2f, 0.8f, 0.2f);
	}
	
	private int[] stem = {0, 0};
	
	@Override
	public void act(WorldRunner wr) {
		//Grow
		if(getHp() != -1){
			setHp(getHp() + (wr.sunLight / 18.0) * 1.05);
		}
		
		//Reproduce
		if(getHp() > wr.plantGrowHp){
			if(Math.random() > 0.3){
				wr.createPlant((int)(getX() + (Math.random() * 10)), (int)(getY() + 20 + (Math.random() * 10)), getStem()[0], getStem()[1], /*(int)getX(), (int)getY()*/ 1 + Math.random());
			}
			if(Math.random() > 0.5){
				wr.createPlant((int)(getX() + (Math.random() * 10) - 20), (int)(getY() + (Math.random() * 10) - 20), getStem()[0], getStem()[1], /*(int)getX(), (int)getY()*/ 1 + Math.random());
			}
			if(Math.random() > 0.5){
				wr.createPlant((int)(getX() + (Math.random() * 10) + 20), (int)(getY() + (Math.random() * 10) - 20), getStem()[0], getStem()[1], /*(int)getX(), (int)getY()*/ 1 + Math.random());
			}
			setHp(-1);
		}
	}
	public void setStem(int[] stemAr){
		stem = stemAr;
	}
	
	public int[] getStem() {
		return stem;
	}

}
