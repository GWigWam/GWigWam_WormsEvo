package pack;

import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

public class ScreenDisplay {
	
	public final static int screenWidth = Display.getDesktopDisplayMode().getWidth();
	public final static int screenHeight = Display.getDesktopDisplayMode().getHeight();
	
	//Create objects
	private WorldRunner wr = new WorldRunner(screenWidth, screenHeight);
	
	// Create vars DON'T YET DEFINE VALUE
	private int frameNumber;
	private int FPS;
	private int slices;
	public int gameSpeed;
	private long time;
	
	final float[][] colorArray = new float[5][3];
	// [0]=red
	// [1]=green
	// [2]=blue
	// [3]=white
	// [4]=black
	
	ScreenDisplay(){ // CONSTRUCTOR
		initGL(screenWidth, screenHeight);
		load();
		startDisplayLoop();
	}


	private void initGL(int width, int height){
	
		try {
			//DisplayMode mode = new DisplayMode(width,height);
			//DisplayMode mode = Display.getDesktopDisplayMode(); Old code, for not-fullscreen stuff
			Display.setDisplayMode(Display.getDesktopDisplayMode());
			Display.setFullscreen(true);
			Display.create();
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			System.out.println("Catched an error in intiGL");
			e.printStackTrace();
			System.exit(0);	
		}
 
		// init OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
	
	private void load(){
		gameSpeed = 30;
		
		frameNumber = 61;
		FPS = 0;
		slices = 100;
		
		colorArray[0][0] = 1;
		colorArray[0][1] = 0;
		colorArray[0][2] = 0;
		
		colorArray[1][0] = 0.2f;
		colorArray[1][1] = 1;
		colorArray[1][2] = 0.2f;
		
		colorArray[2][0] = 0.3f;
		colorArray[2][1] = 0.3f;
		colorArray[2][2] = 1;
		
		colorArray[3][0] = 1;
		colorArray[3][1] = 1;
		colorArray[3][2] = 1;
		
		colorArray[4][0] = 0;
		colorArray[4][1] = 0;
		colorArray[4][2] = 0;
		
		/*loadTexture("tmpNaam", "TX_test.png");*/
	}
	
	private float[] randomColorArray(){
		float[] array = new float[3];
		
		array[0] = (float) Math.random();
		array[1] = (float) Math.random();
		array[2] = (float) Math.random();
		
		return array;
	}
	
	private void startDisplayLoop(){
		while(!Display.isCloseRequested()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
					
			checkInput();
			wr.applyPhysics(frameNumber, gameSpeed);
						
			drawStuff();
			
			endThisLoop();
		}
		endProgram();
		Display.destroy();
	}
	
	private void endProgram() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		System.exit(0);
	}


	private void checkInput(){
		if(Mouse.isButtonDown(0)){
			leftHold(Mouse.getX(), Mouse.getY());
		}
		while(Mouse.next()){
			if(Mouse.getEventButtonState() && Mouse.getEventButton() == 0){
				leftClick(Mouse.getX(), Mouse.getY());
			}
			
			if(Mouse.getEventButtonState() && Mouse.getEventButton() == 1){
				rightClick(Mouse.getX(), Mouse.getY());
			}			
		}
		while(Keyboard.next()){
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				System.exit(0);
			}
			
			if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_LEFT){
				if(wr.sunLight >= 1){
					wr.sunLight--;
				}
			}
			if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_RIGHT){
				if(wr.sunLight <= 3){
					wr.sunLight++;
				}
			}
			
			if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_R){
				wr.createRedWorm(Mouse.getX(), Mouse.getY(), 7, 0.4);
			}
			if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_G){
				wr.createGreenWorm(Mouse.getX(), Mouse.getY(), 3, 0.8, randomColorArray());
			}
			if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_P){
				wr.createPlant(Mouse.getX(), Mouse.getY(), Mouse.getX(), Mouse.getY(), 1);
			}
			
			if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_SPACE){
				if(gameSpeed == 1){
					gameSpeed = 0;
				}else if(gameSpeed == 0){
					gameSpeed = 30;
				}else{
					gameSpeed = 1;
				}
			}
			if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_RETURN){

			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			if(gameSpeed < 30){
				gameSpeed++;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			if(gameSpeed > 1){
				gameSpeed--;
			}
		}
	}
	private void leftHold(int x, int y){
		
	}
	
	private void leftClick(int x, int y) {
		wr.erase(x, y, 20);
	}
	
	private void rightClick(int x, int y){
		wr.erase();
	}

	private void drawStuff() {
		float[] tmp = {0.8f, 0.7f, 0.7f}; //BackgroundColor
		drawQuad(0, 0, screenHeight, screenWidth, tmp); //Background
		drawCreatures();		
				
		
		drawSunLight();
		
		GL11.glColor3f(0, 0, 0);
		SimpleText.drawString("wormsevo                     fps "+FPS+"                     speed +"+(31 - gameSpeed)+"                     Plants, Red, Green "+wr.plantCount+" "+wr.redWormCount+" "+wr.greenWormCount+"                     sunLight +"+wr.sunLight+"                     displayed in: "+screenWidth+"x"+screenHeight, 1, screenHeight - 10);
	}

	private void endThisLoop(){
		//FPS meter
		if (time <= System.currentTimeMillis() - 1000) {			
			Display.setTitle("Simulating gravity @ " + frameNumber + " FPS"); //Normal
			FPS = frameNumber;
			frameNumber = 0;
			time = System.currentTimeMillis();
		} else {
			frameNumber++;
		}
		Display.update();
		Display.sync(60);
	}
	
	private void drawCreatures() {
		for(int i = 0; i < wr.creatures.length; i++){
			if(wr.creatures[i] != null){
				Creature c = wr.creatures[i];
				
				if(c instanceof Plant){
					Plant p = (Plant) c;
					
					drawLine((int)p.getX(), (int)p.getY(), p.getStem()[0], p.getStem()[1], p.getColor(), 0.2f);
					if(p.getHp() != 0){
						drawCircle(p.getX(), p.getY(), (int)(p.getHp()) + 3, p.getColor(), 0.2f);
						drawCircle(p.getX(), p.getY(), (int)(p.getHp()) + 2, p.getColor(), 0.3f);
						drawCircle(p.getX(), p.getY(), (int)(p.getHp()) + 1, p.getColor());
					}
				}
				
				if(c instanceof GreenWorm){
					GreenWorm w = (GreenWorm) c;
					
					drawCircle(w.getX(), w.getY(), 11, colorArray[4]);
					
					for(int j = 0; j < w.getStaart().length; j++){
						if(w.getStaart()[j] != null){
							if(w.getStaart()[j][0] != -1 && w.getStaart()[j][1] != -1){
								if(w.getStaart()[j][0] != 0 && w.getStaart()[j][1] != 0){
									drawCircle(w.getStaart()[j][0], w.getStaart()[j][1], 9, colorArray[4], 0.3f);
									drawCircle(w.getStaart()[j][0], w.getStaart()[j][1], 8, colorArray[4], 0.6f);
									drawCircle(w.getStaart()[j][0], w.getStaart()[j][1], 7, colorArray[4]);
									drawCircle(w.getStaart()[j][0], w.getStaart()[j][1], 6, colorArray[1]);
									//System.out.println("("+w.getX()+", "+w.getY()+")");
								}
							}	
						}
					}
					
					drawCircle(w.getX(), w.getY(), 9,  colorArray[1]);
					drawCircle(w.getX(), w.getY(), 3, w.getColor());
					
					//System.out.println("Head: ("+w.getX()+", "+w.getY()+")");
					
					//System.out.println("------");
				}
				
				if(c instanceof RedWorm){
					RedWorm w = (RedWorm) c;
					
					drawCircle(w.getX(), w.getY(), 11, colorArray[4]);
					
					for(int j = 0; j < w.getStaart().length; j++){
						if(w.getStaart()[j] != null){
							if(w.getStaart()[j][0] != -1 && w.getStaart()[j][1] != -1){
								if(w.getStaart()[j][0] != 0 && w.getStaart()[j][1] != 0){
									drawCircle(w.getStaart()[j][0], w.getStaart()[j][1], 9, colorArray[4], 0.3f);
									drawCircle(w.getStaart()[j][0], w.getStaart()[j][1], 8, colorArray[4], 0.6f);
									drawCircle(w.getStaart()[j][0], w.getStaart()[j][1], 7, colorArray[4]);
									drawCircle(w.getStaart()[j][0], w.getStaart()[j][1], 6, w.getColor());
									//System.out.println("("+w.getX()+", "+w.getY()+")");
								}
							}	
						}
					}
					
					drawCircle(w.getX(), w.getY(), 9, w.getColor());
					drawCircle(w.getX(), w.getY(), 2, colorArray[4]);
					
					//System.out.println("Head: ("+w.getX()+", "+w.getY()+")");
					
					//System.out.println("------");
				}
			}
		}		
	}
	
	private void drawSunLight(){
		float[] tmp = {1f, 1f, 1f};
		drawQuad(0, 0, screenHeight, screenWidth, tmp, ((float) (wr.sunLight * 0.07)));
	}
	
	private void drawLine(int X1, int Y1, int X2, int Y2, float color[]){
		GL11.glColor3f(color[0], color[1], color[2]);
		
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(X1, Y1);
			GL11.glVertex2f(X2, Y2);
		GL11.glEnd();
	}
	
	private void drawLine(int X1, int Y1, int X2, int Y2, float color[], float a){
		GL11.glColor4f(color[0], color[1], color[2], a);
		
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f(X1, Y1);
			GL11.glVertex2f(X2, Y2);
		GL11.glEnd();
	}
	
	private void drawQuad(int x, int y, int h, int w, float color[]){
		GL11.glColor3f(color[0], color[1], color[2]);

		// draw quad
		GL11.glBegin(GL11.GL_QUADS);
		    GL11.glVertex2f(x, y);
		    GL11.glVertex2f(x+w,y);
		    GL11.glVertex2f(x+w,y+h);
		    GL11.glVertex2f(x,y+h);
		GL11.glEnd();
	}
	
	private void drawQuad(int x, int y, int h, int w, float color[], float alpha){
		GL11.glColor4f(color[0], color[1], color[2], alpha);

		// draw quad
		GL11.glBegin(GL11.GL_QUADS);
		    GL11.glVertex2f(x, y);
		    GL11.glVertex2f(x+w,y);
		    GL11.glVertex2f(x+w,y+h);
		    GL11.glVertex2f(x,y+h);
		GL11.glEnd();
	}
	
	private void drawCircle(double x, double y, int radius, float color[]){
		GL11.glColor3f(color[0], color[1], color[2]);
		
		float incr = (float) (2 * Math.PI / slices);
		/*xCoord = xCoord + radius;
		yCoord = yCoord + radius;*/
		
    GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for(int i = 0; i < slices; i++)
        {
              float angle = incr * i;

              float Xc = (float) (x +  Math.cos(angle) * radius);
              float Yc = (float) (y +  Math.sin(angle) * radius);

              GL11.glVertex2f(Xc, Yc);
        }
     GL11.glEnd();	
	}


	private void drawCircle(double x, double y, int radius, float color[], float alpha){		
		GL11.glColor4f(color[0], color[1], color[2], alpha);
	
		float incr = (float) (2 * Math.PI / slices);
		/*xCoord = xCoord + radius;
		yCoord = yCoord + radius;*/
	
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		for(int i = 0; i < slices; i++){
			float angle = incr * i;

			float Xc = (float) (x +  Math.cos(angle) * radius);
			float Yc = (float) (y +  Math.sin(angle) * radius);

          GL11.glVertex2f(Xc, Yc);
		}
		GL11.glEnd();	
	}
}