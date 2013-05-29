package pack;

public class WorldRunner {
	WorldRunner(int screenWidth, int screenHeight){
		sunLight = 1;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 5; j++){
				createPlant(i * 300 + 300, j * 200 + 100,i * 300 + 300, j * 200 + 100, 1);
			}
		}
	}
	
	public int sunLight;
	public final int plantGrowHp = 10;
	public final int greenWormSplitHp = 7;
	public final int redWormSplitHp = 13;
	
	private final int screenWidth;
	private final int screenHeight;
	
	public int plantCount = 0;
	public int redWormCount = 0;
	public int greenWormCount = 0;
	
	Creature[] creatures = new Creature[512];
	
	public void applyPhysics(int frameNumber, int gameSpeed) {
		if(gameSpeed != 0){ // 31 for superspeed
			if(frameNumber % gameSpeed == 0){
				doStuff();
			}
		}else{
			for(int i = 0; i < 8; i++){
				doStuff();
			}
		}
	}
	
	private void doStuff(){
		plantCount = 0;
		redWormCount = 0;
		greenWormCount = 0;			
		
		reGrowPlants();
		for(int i = 0; i < creatures.length; i++){
			if(creatures[i] != null){
				if(creatures[i].getHp() == - 1){
					creatures[i].setX(-1);
					creatures[i].setY(-1);
					creatures[i] = null;
					continue;
				}
			}
			
			if(creatures[i] == null){
				continue;
			}

			creatures[i].act(this);
			
			if(creatures[i] instanceof Plant){
				plantCount++;
			}else if(creatures[i] instanceof RedWorm){
				redWormCount++;
			}else if(creatures[i] instanceof GreenWorm){
				greenWormCount++;
			}
			
			if(creatures[i] instanceof Worm){
				if(creatures[i].getHp() < 2){
					if(greenWormCount > 1){
						creatures[i].setHp(-1);
						creatures[i] = null;
					}else if(creatures[i] instanceof GreenWorm){
						creatures[i].setHp(creatures[i].getHp() + 1);
					}else{ //Only 1 green left, current is red
						creatures[i].setHp(-1);
						creatures[i] = null;
					}
				}
				Worm tmp = (Worm) creatures[i];
				try{
					if(creatures[i] instanceof GreenWorm){
						double hp = tmp.getHp() - (0.005 * (tmp.getSpeed())) - 0.0006;
						if(hp < tmp.getHp()){
							tmp.setHp(hp); //Living is quite hungering 		//Way less sustain then redWorms
						}
					}
					if(creatures[i] instanceof RedWorm){
						double hp = tmp.getHp() - (0.005 * (tmp.getSpeed())) - 0.00025;
						if(hp < tmp.getHp()){
							tmp.setHp(hp); //Living is quite hungering 		//RedWorms have way more sustain
						}
					}
					if(tmp.getX() < 0){
						tmp.setX(tmp.getX()+1);
					}
					if(tmp.getX() > screenWidth){
						tmp.setX(tmp.getX()-1);
					}
					if(tmp.getY() < 0){
						tmp.setY(tmp.getY()+1);
					}
					if(tmp.getY() > screenHeight){
						tmp.setY(tmp.getY()-1);
					}
				}catch(Exception e){
					//void
				}
			}
		}
	}
	
	private void reGrowPlants(){
		if(sunLight > 0){
			final Creature tmp = new Plant((int)(Math.random() * ScreenDisplay.screenWidth), (int)(Math.random() * ScreenDisplay.screenHeight), 0, 0, - 1);			
			for(int i = 0; i < creatures.length; i++){
				if(creatures[i] != null){
					if(creatureDistance(tmp, creatures[i]) < 250){
						return;
					}
				}
			}
			//System.out.println("Repopulating at ("+tmp.getX()+", "+tmp.getY()+") ");
			createPlant((int)(tmp.getX()), (int)(tmp.getY()), (int)(tmp.getX()), (int)(tmp.getY()), 2);
		}
	}
	
	public void createGreenWorm(int x, int y, double hp, double speed, float[] colorAr){
		GreenWorm g = new GreenWorm(x, y, hp, speed, colorAr);
		
		for(int i = 0; i < creatures.length; i++){
			if(creatures[i] == null){
				creatures[i] = g;
				break;
			}
		}
	}
	
	public void createRedWorm(int x, int y, double hp, double speed){
		RedWorm r = new RedWorm(x, y, hp, speed);
		
		for(int i = 0; i < creatures.length; i++){
			if(creatures[i] == null){
				creatures[i] = r;
				break;
			}
		}
	}
	
	public void createPlant(int x, int y, int stemX, int stemY, double hp){
		if(x < ScreenDisplay.screenWidth && y < ScreenDisplay.screenHeight && x > 0 && y > 0){
			Plant p = new Plant(x, y, stemX, stemY, hp);
			
			for(int i = 0; i < creatures.length; i++){
				if(creatures[i] != null){
					if(Math.max(creatures[i].getX(), x) - Math.min(creatures[i].getX(), x) < 15 && Math.max(creatures[i].getY(), y) - Math.min(creatures[i].getY(), y) < 15){
						return;
					}
				}
			}		
			for(int i = 0; i < creatures.length; i++){
				if(creatures[i] == null){
					creatures[i] = p;
					break;
				}
			}
		}
	}
	
	public int creatureDistance(Creature c1, Creature c2){
		int dis = ( (int) ( Math.sqrt( Math.pow( Math.max(c1.getX(), c2.getX()) - Math.min(c1.getX(), c2.getX()), 2) + Math.pow( Math.max(c1.getY(), c2.getY()) - Math.min(c1.getY(), c2.getY()), 2)) ));
		return dis;
	}
	public void erase(int x, int y, int radius) {
		for(int i = 0; i < creatures.length; i++){
			if(creatures[i] != null){
				Creature c = creatures[i];

				if(c.getHp() > 0 && c.getX() - x < radius && c.getY() - y < radius && c.getX() - x > -radius && c.getY() - y > -radius){
					c.setHp(-1);
					c = null;
				}
			}
		}		
	}
	
	public void erase() {
		for(int i = 0; i < creatures.length; i++){
			if(creatures[i] != null){
				Creature c = creatures[i];
				
				if(c.getHp() > 0){
					c.setHp(-1);
					c = null;
				}
			}
		}		
	}
}




