package pack;

import java.util.Random;

public class GreenWorm extends Worm{
	GreenWorm(int x, int y, double hp, double speed, float[] colorAr) {
		super(x, y, hp, speed);

		this.setColor(colorAr[0], colorAr[1], colorAr[2]);
	}
	
	Random rand = new Random();

	@Override
	public void act(WorldRunner wr) {
		fixLength();
		
		double[] tmpGoal = {-1, -1};
		setGoal(tmpGoal);
		if(!fleeFromRed(wr)){
			searchForPlants(wr);
		}
		
		if(getGoal()[0] != -1 && getGoal()[1] != -1){
			if(getX() > getGoal()[0]){
				move(getX() - getSpeed(), getY());
			}
			if(getX() < getGoal()[0]){
				move(getX() + getSpeed(), getY());
			}
			if(getY() > getGoal()[1]){
				move(getX(), getY() - getSpeed());
			}
			if(getY() < getGoal()[1]){
				move(getX(), getY() + getSpeed());
			}
		}
		
		if(getHp() >= wr.greenWormSplitHp){
			split(wr);
		}
	}
	
	private boolean fleeFromRed(WorldRunner wr){
		for(int i = 0; i < wr.creatures.length; i++){
			if(wr.creatures[i] != null){
				if(wr.creatures[i] instanceof RedWorm){
					if(wr.creatureDistance(this, wr.creatures[i]) < 50){
						double[] tmpGoal = {getX(), getY()};
						if(wr.creatures[i].getX() > getX()){
							tmpGoal[0]-=5;
						}else{
							tmpGoal[0]+=5;
						}
						if(wr.creatures[i].getY() > getY()){
							tmpGoal[1]-=5;
						}else{
							tmpGoal[1]+=5;
						}
						setGoal(tmpGoal);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void searchForPlants(WorldRunner wr){
		for(int i = 0; i < wr.creatures.length; i++){
			if(wr.creatures[i] != null){
				if(wr.creatures[i] instanceof Plant){
					//if(getGoal() != null){
						if(getGoal()[0] == -1 && getGoal()[1] == -1){
							if(wr.creatures[i].getHp() != -1){
								double[] tmpGoal = {wr.creatures[i].getX(), wr.creatures[i].getY()};
								setGoal(tmpGoal);
							}
						}else{
							Creature tmpCreature = new Plant((int)getGoal()[0], (int)getGoal()[1], -1, -1, -1);
							
							int tmpDis1 = (wr.creatureDistance(wr.creatures[i], this));
							int tmpDis2 = (wr.creatureDistance(tmpCreature, this));
							
							if(tmpDis1 <= tmpDis2){
								double[] tmpGoal = {wr.creatures[i].getX(), wr.creatures[i].getY()};
								setGoal(tmpGoal);
							}
						}
					//}
					
					if(wr.creatureDistance(this, wr.creatures[i]) < 10){
						eat(wr.creatures[i], wr);
					}
				}
			}
		}
	}
}
