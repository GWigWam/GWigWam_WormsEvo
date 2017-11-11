package pack;

import java.util.Random;

public class RedWorm extends Worm{
	RedWorm(int x, int y, double hp, double speed) {
		super(x, y, hp, speed);

		this.setColor(0.5f, 0, 0);
	}
	
	Random rand = new Random();

	@Override
	public void act(WorldRunner wr) {
		fixLength();
		
		double[] tmpGoal = {-1, -1};
		setGoal(tmpGoal);
		speedBoost(wr);
		searchForFood(wr);
		
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
		
		if(getHp() >= wr.redWormSplitHp){
			split(wr);
		}
	}
	
	private void speedBoost(WorldRunner wr){
		for(int i = 0; i < wr.creatures.length; i++){
			if(wr.creatures[i] != null){
				if(wr.creatures[i] instanceof GreenWorm){
					if(wr.creatureDistance(this, wr.creatures[i]) < 100){
						if(getSpeed() < (((Worm) wr.creatures[i]).getSpeed() - 0.3) + 1.0){
							setSpeed(getSpeed() + 0.014);
						}
						if(getColor()[0] < 1.0){
							float[] tmpColor = {getColor()[0] + 0.005f, getColor()[1], getColor()[2]};
							setColor(tmpColor);
						}
						setHp(getHp() - 0.006); //Boost burns energy
						return;
					}
				}
			}
		}
		if(getColor()[0] > 0.45){
			float[] tmpColor = {getColor()[0] - 0.002f, getColor()[1], getColor()[2]};
			setColor(tmpColor);
		}
		if(getSpeed() > 0.4){
			setSpeed(getSpeed() - 0.01);
		}
	}
	
	private void searchForFood(WorldRunner wr){
		for(int i = 0; i < wr.creatures.length; i++){
			if(wr.creatures[i] != null){
				if(wr.creatures[i] instanceof GreenWorm){
					//if(getGoal() != null){
						if(getGoal()[0] == -1 && getGoal()[1] == -1){
							if(wr.creatures[i].getHp() != -1){
								double[] tmp = {wr.creatures[i].getX(), wr.creatures[i].getY()};
								setGoal(tmp);
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
						//setGoal(null);
					}
				}
			}
		}
	}
}