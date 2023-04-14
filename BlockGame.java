package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BlockGame {
	
	static class MyFrame extends JFrame {
		
		// constant
		static int BALL_WIDTH = 20;
		static int BALL_HEIGHT = 20;
		static int BLOCK_ROWS = 5;
		static int BLOCK_COLUMNS = 10;
		static int BLOCK_WIDTH = 40;
		static int BLOCK_HEIGHT = 20;
		static int BLOCK_GAP = 3;
		static int BAR_WIDTH = 80;
		static int BAR_HEIGHT = 20;
		static int CANVAS_WIDTH = 415 + (BLOCK_GAP * BLOCK_COLUMNS);
		static int CANVAS_HEIGHT = 600;
		
		// variable
		static MyPanel myPanel = null;
		static int score = 0;
		static Timer timer = null;
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS];
		static Bar bar = new Bar ();
		static Ball ball = new Ball ();
		static int barXTarget = bar.x; // target value - 보관
		static int dir = 0; // 0 : up-right 1 : down-right 2 : up-left 3 : down_left
		static int ballSpeed = 5;
		static boolean isGameFinish = false;
		static boolean reStartGame = false;
		
		static class Ball {
			int x = CANVAS_WIDTH/2 - BALL_WIDTH/2; // ball이 시작할 때 화면 중앙에 위치. 볼의 중심에 위치할 수 있게 볼 반지름만큼 빼줌
			int y = CANVAS_HEIGHT/2 - BALL_HEIGHT/2;
			int width = BALL_WIDTH;
			int height = BALL_HEIGHT;
			
			// checkCollision 구현 할 떄 (bar와의 충돌처리)
			// 공과 벽돌의 충돌지점 설정
			Point getCenter() {
				return new Point(x+(BALL_WIDTH/2), y + (BALL_HEIGHT/2));
			}
			Point getBottomCenter() {
				return new Point(x+(BALL_WIDTH/2), y+(BALL_HEIGHT));
			}
			Point getTopCenter() {
				return new Point(x + (BALL_WIDTH/2), y);
			}
			Point getLeftCenter() {
				return new Point(x, y+(BALL_HEIGHT/2));
			}
			Point getRightCenter() {
				return new Point(x+(BALL_WIDTH), y+(BALL_HEIGHT/2));
			}
			
		}
		static class Bar {
			int x = CANVAS_WIDTH/2 - BAR_WIDTH/2;
			int y = CANVAS_HEIGHT - 100;
			int width = BAR_WIDTH;
			int height = BAR_HEIGHT;
		}
		static class Block { // block 개수 5*10 = 50 각각 위치 지정할 거라서 초기화 0으로 임시지정
			int x = 0;
			int y = 0;
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color = 0; // 0 : white 1 : yellow 2 : blue 3 : mage nta 4 : red
			boolean isHidden = false; // after collision, block will be hidden
		}
		static class MyPanel extends JPanel {  //CANVAS for Draw
			public MyPanel() {
				this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				this.setBackground(Color.black );
			}
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D)g;
				
				drawUI(g2d);
			}
			private void drawUI (Graphics2D g2d) {
				// draw Blocks
				for(int i=0; i<BLOCK_ROWS; i++) {
					for(int j=0; j<BLOCK_COLUMNS; j++) {
						if(blocks[i][j].isHidden) {
							continue;
						}
						if(blocks[i][j].color==0) {
							g2d.setColor(Color.WHITE);
						}
						else if(blocks[i][j].color==1) {
							g2d.setColor(Color.YELLOW);
						}
						else if(blocks[i][j].color==2) {
							g2d.setColor(Color.BLUE);
						}
						else if(blocks[i][j].color==3) {
							g2d.setColor(Color.MAGENTA);
						}
						else if(blocks[i][j].color==4) {
							g2d.setColor(Color.RED);
						}
						g2d.fillRect(blocks[i][j].x, blocks[i][j].y, 
								blocks[i][j].width, blocks[i][j].height);
					}
					
					//draw score
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
					g2d.drawString("score : " + score, CANVAS_WIDTH/2 - 45, 20);
//					if(isGameFinish) {
//						g2d.setColor(Color.WHITE);
//						g2d.drawString("Game Finished!", CANVAS_WIDTH/2 - 70, 50);
//					}
					
					// draw ball
					g2d.setColor(Color.WHITE);
					g2d.fillOval(ball.x, ball.y, BALL_WIDTH, BALL_HEIGHT);
					
					//draw bar
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
			
				}
			}
		}
		
		public MyFrame(String title) {
			super(title);
			this.setVisible(true);
			this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT); //(400, 600)
			this.setLocation(400,100); //화면 실행 위치 
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			initData();
			
			myPanel = new MyPanel();
			this.add("Center", myPanel);
			
			setKeyListener();
			startTimer();
		}
		public void initData() {
			for(int i=0; i<BLOCK_ROWS; i++) {
				for(int j=0; j<BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block(); 
					blocks[i][j].x = BLOCK_WIDTH*j + BLOCK_GAP*j;
					blocks[i][j].y = 100 + BLOCK_HEIGHT*i + BLOCK_GAP*i;
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = 4-i; // 0 : white 1 : yellow 2 : blue 3 : magenta 4 : red
					blocks[i][j].isHidden = false;
				}
			}
		}
		public void setKeyListener() {
			this.addKeyListener (new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_LEFT) {
						//System.out.println("Pressed Left Key");
						barXTarget -= 30;
						if(bar.x < barXTarget) { // key pressed...
							barXTarget = bar.x;
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
						//System.out.println("Pressed Right Key");
						barXTarget += 30;
						if(bar.x > barXTarget) { // key pressed...
							barXTarget = bar.x;
						}
					}
				}
			});
		}
		public void startTimer() {
			timer = new Timer(20, new ActionListener(){
				@Override 
				public void actionPerformed(ActionEvent e) {
					movement();
					checkCollision(); //wall, bar
					checkCollisionBlock(); // block 50
					myPanel.repaint(); //Redraw
					
					isGameFinish();
				}
			});
			timer.start(); //start timer
		}
		public void isGameFinish() {
			// game success 
			int count = 0;
			for(int i=0; i<BLOCK_ROWS; i++) {
				for(int j=0; j<BLOCK_COLUMNS; j++) {
					Block block = blocks[i][j];
					if(block.isHidden)
						count++;
				}
			}
			if(count == BLOCK_ROWS * BLOCK_COLUMNS) {
				
				//game finished
				//timer.stop();
				isGameFinish = true;
				reStartGame();
			}
		}
		// game restart
		public void reStartGame() {
				if(isGameFinish){
				initData();
				ball.x = CANVAS_WIDTH/2 - BALL_WIDTH/2;
				ball.y = CANVAS_HEIGHT/2 - BALL_HEIGHT/2;
				score = 0;
				}	
			dir=0;
			movement();
			checkCollision();
			checkCollisionBlock();
			myPanel.repaint();
			timer.start();
		}
		public void movement() {
			//bar move
			if(bar.x < barXTarget) {
				bar.x += 5;
			}
			else if (bar.x > barXTarget) {
				bar.x -= 5;
			}
			//ball move
			if(dir==0) { // 0 : up-right 
				ball.x += ballSpeed;
				ball.y -= ballSpeed;
			}
			else if(dir==1) { // 1 : down-right
				ball.x += ballSpeed;
				ball.y += ballSpeed;
			}
			else if(dir==2) { // 2 : up-left
				ball.x -= ballSpeed;
				ball.y -= ballSpeed;
			}
			else if(dir==3) { // 3 : down-left
				ball.x -= ballSpeed;
				ball.y += ballSpeed;
			}
		}	
		public boolean duplRect(Rectangle rect1, Rectangle rect2) {
			return rect1.intersects(rect2);  //check two rect is duplicated
		}
		public void checkCollision() { // wall, bar의 충돌 처리
			if(dir==0) { // 0 : up-right 
				// wall
				if(ball.y<0) {  // wall upper
					dir=1;
				}
				if(ball.x>CANVAS_WIDTH - BALL_WIDTH - BALL_WIDTH/2) { //wall right
					dir=2;
				}
				// bar - none
			}
			else if(dir==1) { // 1 : down-right
				// wall
				if(ball.y > CANVAS_HEIGHT - BALL_HEIGHT - BALL_HEIGHT) {  //wall bottom
					dir=0;
					
					//game reset
					dir = 0;
					ball.x = CANVAS_WIDTH/2 - BALL_WIDTH/2;
					ball.y = CANVAS_HEIGHT/2 - BALL_HEIGHT/2;
					score = 0;
				}
				if(ball.x>CANVAS_WIDTH - BALL_WIDTH - BALL_WIDTH/2) { // wall right
					dir=3;
				}
				// bar
				if(ball.getBottomCenter().y>= bar.y) {
					if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
								new Rectangle(bar.x, bar.y, bar.width, bar.height)) ) {
						dir = 0;
					}
				}
			}
			else if(dir==2) { // 2 : up-left
				// wall
				if(ball.y < 0) { //wall upper
					dir=3;
				}
				if(ball.x<0) { //wall left
					dir=0;
				}
				// bar-none
			}
			else if(dir==3) { // 3 : down-left
				// wall
				if(ball.y > CANVAS_HEIGHT - BALL_HEIGHT - BALL_HEIGHT) { //wall bottom
					dir=2;
					
					//game reset
					dir = 0;
					ball.x = CANVAS_WIDTH/2 - BALL_WIDTH/2;
					ball.y = CANVAS_HEIGHT/2 - BALL_HEIGHT/2;
					score = 0;
				}
				if(ball.x < 0) { //wall left
					dir=1;
				}
				// bar
				if(ball.getBottomCenter().y>= bar.y) {
					if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
								new Rectangle(bar.x, bar.y, bar.width, bar.height)) ) {
						dir = 2;
					}
				}
			}
		}
		public void checkCollisionBlock() {
			//0 : up-right 1 : down-right 2 : up-left 3 : down_left
			for(int i=0; i<BLOCK_ROWS; i++) {
				for(int j=0; j<BLOCK_COLUMNS; j++) {
					Block block = blocks[i][j];
					if(block.isHidden == false) {
						if(dir == 0) {  //0 : up-right
							if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
										new Rectangle(block.x, block.y, block.width, block.height)) ) {
								if(ball.x > block.x + 2 && 
									ball.getRightCenter().x <= block.x + block.width -2) {
									//block bottom collision
									dir = 1;
								}
								else {
									//block left collision
									dir = 2;
								}
								block.isHidden = true;
								if(block.color==0) {
									score += 10;
								}else if(block.color==1) {
									score += 20;
								}else if(block.color==2) {
									score += 30;
								}else if(block.color==3) {
									score += 40;
								}else if(block.color==4) {
									score += 50;
								}
							}
						}
						else if(dir == 1) {  //1 : down-right
							if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
										new Rectangle(block.x, block.y, block.width, block.height)) ) {
								if(ball.x > block.x + 2 && 
									ball.getRightCenter().x <= block.x + block.width -2) {
									//block top collision
									dir = 0;
								}
								else {
									//block left collision
									dir = 3;
								}
								block.isHidden = true;
								if(block.color==0) {
									score += 10;
								}else if(block.color==1) {
									score += 20;
								}else if(block.color==2) {
									score += 30;
								}else if(block.color==3) {
									score += 40;
								}else if(block.color==4) {
									score += 50;
								}
							}
						}
						else if(dir == 2) {  //2 : up-left
							if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
										new Rectangle(block.x, block.y, block.width, block.height)) ) {
								if(ball.x > block.x + 2 && 
									ball.getRightCenter().x <= block.x + block.width -2) {
									//block bottom collision
									dir = 3;
								}
								else {
									//block right collision
									dir = 0;
								}
								block.isHidden = true;
								if(block.color==0) {
									score += 10;
								}else if(block.color==1) {
									score += 20;
								}else if(block.color==2) {
									score += 30;
								}else if(block.color==3) {
									score += 40;
								}else if(block.color==4) {
									score += 50;
								}
							}
						}
						else if(dir == 3) {  //3 : down_left
							if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
										new Rectangle(block.x, block.y, block.width, block.height)) ) {
								if(ball.x > block.x + 2 && 
									ball.getRightCenter().x <= block.x + block.width -2) {
									//block top collision
									dir = 2;
								}
								else {
									//block right collision
									dir = 1;
								}
								block.isHidden = true;
								if(block.color==0) {
									score += 10;
								}else if(block.color==1) {
									score += 20;
								}else if(block.color==2) {
									score += 30;
								}else if(block.color==3) {
									score += 40;
								}else if(block.color==4) {
									score += 50;
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	public static void main(String [] args) {
		new MyFrame("Block Game");
	}
}
