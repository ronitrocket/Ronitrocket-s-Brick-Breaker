package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

	private boolean play = false;
	private int score = 0;

	private boolean paused = false;
	private int pausedToggle = 0;

	private int randomNumber;

	private int ballXdirSave;
	private int ballYdirSave;

	private int level = 1;
	private boolean gameOver = false;
	private boolean gameWin = false;

	private int rows = 3;
	private int cols = 7;

	private int totalBricks = rows * cols;

	private Timer time;
	private int delay = 8;

	private boolean powerOnScreen = false;
	private boolean powerupOnScreen = false;
	private boolean powerdownOnScreen = false;
	private boolean poweruped = false;
	private int powerupXpos;
	private int powerupYpos;
	private int powerdownXpos;
	private int powerdownYpos;

	private int paddleXpos = 310;
	private int paddleXsize = 100;
	private int paddleXhitbox = paddleXsize / 100;

	private int ballposX = 340;
	private int ballposY = 400;
	private int ballXdir = 0;
	private int ballYdir = -2;

	private MapGenerator map;

	public Gameplay() {
		map = new MapGenerator(rows, cols);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(true);
		time = new Timer(delay, this);
		time.start();
	}

	public void paint(Graphics g) {
		// background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);

		// borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);

		// map
		map.draw((Graphics2D) g);

		// paddle
		g.setColor(Color.green);
		g.fillRect(paddleXpos, 550, paddleXsize, 8);

		// ball
		g.setColor(Color.blue);
		g.fillOval(ballposX, ballposY, 20, 20);

		// powerUp
		if (powerupOnScreen) {
			g.setColor(Color.green);
			g.setFont(new Font("serif", Font.BOLD, 17));
			g.drawString("P", powerupXpos, powerupYpos);
		}

		// powerDown
		if (powerdownOnScreen) {
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 17));
			g.drawString("P", powerdownXpos, powerdownYpos);
		}

		// scores
		g.setColor(Color.CYAN);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Score: " + score, 575, 30);

		// Instructions
		if (!play && level == 1 && !gameWin && !gameOver) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("serif", Font.BOLD, 10));
			g.drawString(
					"Arrow Keys to move. R to restart the ball. P to pause. Red powers are bad. Green ones are good.",
					130, 30);
		}

		// levelCounter
		g.setColor(Color.orange);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Level: " + level, 10, 30);

		// onWin
		if (totalBricks <= 0) {
			if (level >= 15) {
				gameWin = true;
				play = false;
				ballXdir = 0;
				ballYdir = 0;
				ballXdir = 0;
				ballYdir = 0;
				powerdownYpos = 0;
				powerdownXpos = 0;
				powerupYpos = 0;
				powerupXpos = 0;
				powerupOnScreen = false;
				powerdownOnScreen = false;
				powerOnScreen = false;
				paddleXsize = 100;
				g.setColor(Color.red);
				g.setFont(new Font("serif", Font.BOLD, 30));
				g.drawString("You won the game! Score: " + score, 190, 300);

				g.setFont(new Font("serif", Font.BOLD, 20));
				g.drawString("Press enter to restart!", 230, 350);
			} else {
				play = false;
				ballXdir = 0;
				ballYdir = 0;
				powerdownYpos = 0;
				powerdownXpos = 0;
				powerupYpos = 0;
				powerupXpos = 0;
				powerupOnScreen = false;
				powerdownOnScreen = false;
				powerOnScreen = false;
				paddleXsize = 100;
				g.setColor(Color.red);
				g.setFont(new Font("serif", Font.BOLD, 30));
				g.drawString("You won Level " + level + "! Score: " + score, 190, 300);

				g.setFont(new Font("serif", Font.BOLD, 20));
				g.drawString("Press enter to go to the next level!", 230, 350);
			}
		}

		// onLose
		if (ballposY > 570) {
			gameOver = true;
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			powerdownYpos = 0;
			powerdownXpos = 0;
			powerupYpos = 0;
			powerupXpos = 0;
			powerupOnScreen = false;
			powerdownOnScreen = false;
			powerOnScreen = false;
			poweruped = false;
			paddleXsize = 100;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over! Score: " + score, 190, 300);

			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart!", 230, 350);
		}

		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		time.start();

		paddleXhitbox = paddleXsize / 100;

		if (play) {
			if (new Rectangle(powerupXpos, powerupYpos, 17, 17)
					.intersects(new Rectangle(paddleXpos, 550, paddleXsize, 8))) {
				powerupOnScreen = false;
				powerOnScreen = false;
				poweruped = true;
				paddleXsize = 200;
			}
			if (new Rectangle(powerdownXpos, powerdownYpos, 17, 17)
					.intersects(new Rectangle(paddleXpos, 550, paddleXsize, 8))) {
				powerdownOnScreen = false;
				powerOnScreen = false;
				poweruped = false;
				paddleXsize = 100;
			}

			if (new Rectangle(ballposX, ballposY, 20, 20)
					.intersects(new Rectangle(paddleXpos, 550, paddleXsize / 5, 8))) {
				ballYdir = 1;
				ballYdir = -ballYdir;
				ballXdir = -2;
				Toolkit.getDefaultToolkit().beep();
			} else if (new Rectangle(ballposX, ballposY, 20, 20)
					.intersects(new Rectangle(paddleXpos + 80 * paddleXhitbox, 550, paddleXsize / 5, 8))) {
				ballYdir = 1;
				ballYdir = -ballYdir;
				ballXdir = 2;
				Toolkit.getDefaultToolkit().beep();
			} else if (new Rectangle(ballposX, ballposY, 20, 20)
					.intersects(new Rectangle(paddleXpos + 20 * paddleXhitbox, 550, paddleXsize / 5, 8))) {
				ballYdir = 2;
				ballYdir = -ballYdir;
				ballXdir = -1;
				Toolkit.getDefaultToolkit().beep();
			} else if (new Rectangle(ballposX, ballposY, 20, 20)
					.intersects(new Rectangle(paddleXpos + 60 * paddleXhitbox, 550, paddleXsize / 5, 8))) {
				ballYdir = 2;
				ballYdir = -ballYdir;
				ballXdir = 1;
				Toolkit.getDefaultToolkit().beep();
			} else if (new Rectangle(ballposX, ballposY, 20, 20)
					.intersects(new Rectangle(paddleXpos + 40 * paddleXhitbox, 550, paddleXsize / 5, 8))) {
				ballYdir = 2;
				ballYdir = -ballYdir;
				ballXdir = 0;
				Toolkit.getDefaultToolkit().beep();
			}

			A: for (int i = 0; i < map.map.length; i++) {
				for (int j = 0; j < map.map[0].length; j++) {
					if (map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;

						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;

						if (ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;
							if (!powerOnScreen) {
								Random ran = new Random();
								randomNumber = ran.nextInt((5 - 0) + 1) + 0;
								if (randomNumber == 0) {
									System.out.println("No powerup :C");
								} else if (randomNumber == 1) {
									System.out.println("No powerup :C");
								} else if (randomNumber == 2) {
									System.out.println("No powerup :C");
								} else if (randomNumber == 3) {
									powerdownXpos = brickX + 25;
									powerdownYpos = brickY + 25;
									powerdownOnScreen = true;
									powerOnScreen = true;
								} else if (randomNumber == 4) {
									powerdownXpos = brickX + 25;
									powerdownYpos = brickY + 25;
									powerdownOnScreen = true;
									powerOnScreen = true;
								} else if (randomNumber == 5) {
									powerupXpos = brickX + 25;
									powerupYpos = brickY + 25;
									powerupOnScreen = true;
									powerOnScreen = true;
								}
							}
							Toolkit.getDefaultToolkit().beep();
							if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							} else {
								ballYdir = -ballYdir;
							}
							break A;
						}
					}
				}
			}

			if (poweruped) {
				paddleXsize = 200;
			} else if (!poweruped) {
				paddleXsize = 100;
			}

			if (powerupYpos > 0) {
				powerupYpos += 2;
			}

			if (powerdownYpos > 0) {
				powerdownYpos += 2;
			}

			if (powerupYpos > 570) {
				powerupYpos = 0;
				powerupOnScreen = false;
				powerOnScreen = false;
			}

			if (powerdownYpos > 570) {
				powerdownYpos = 0;
				powerdownOnScreen = false;
				powerOnScreen = false;
			}

			ballposX += ballXdir;
			ballposY += ballYdir;

			if (ballposX < 0) {
				ballXdir = -ballXdir;
				Toolkit.getDefaultToolkit().beep();
			}
			if (ballposY < 0) {
				ballYdir = -ballYdir;
				Toolkit.getDefaultToolkit().beep();
			}
			if (ballposX > 670) {
				ballXdir = -ballXdir;
				Toolkit.getDefaultToolkit().beep();
			}
		}

		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (!paused) {
				play = true;
				paddleXpos += 15;
			} else {
				System.out.println("Is paused.");
			}

			if (poweruped) {
				if (paddleXpos >= 500) {
					paddleXpos = 500;
				}
			} else {
				if (paddleXpos >= 600) {
					paddleXpos = 600;
				}
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (!paused) {
				play = true;
				paddleXpos -= 15;
			} else {
				System.out.println("Is paused.");
			}

			if (paddleXpos < 10) {
				paddleXpos = 10;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!play) {
				if (gameOver == true) {
					play = true;
					gameOver = false;
					ballposX = 120;
					ballposY = 350;
					ballXdir = -1;
					ballYdir = -2;
					paddleXpos = 310;
					score = 0;
					totalBricks = cols * rows;
					map = new MapGenerator(rows, cols);

					repaint();
				} else if (gameWin == true) {
					play = true;
					gameWin = false;
					ballposX = 120;
					ballposY = 350;
					ballXdir = -1;
					ballYdir = -2;
					paddleXpos = 310;
					score = 0;
					level = 1;
					cols = 7;
					rows = 3;
					totalBricks = cols * rows;
					map = new MapGenerator(rows, cols);

					repaint();
				}
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_P) {
			if (pausedToggle == 0 && (play)) {
				ballXdirSave = ballXdir;
				ballYdirSave = ballYdir;
				pausedToggle = 1;
				paused = true;
				ballXdir = 0;
				ballYdir = 0;
			} else if (pausedToggle >= 1 && (play)) {
				pausedToggle = 0;
				paused = false;
				ballXdir = ballXdirSave;
				ballYdir = ballYdirSave;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_R && (play)) {
			ballposX = 120;
			ballposY = 350;
			ballXdir = -1;
			ballYdir = -2;
		}
	}

	public void moveRight() {
		if (!paused) {
			play = true;
			paddleXpos += 10;
		} else {
			System.out.println("Is paused.");
		}
	}

	public void moveLeft() {
		if (!paused) {
			play = true;
			paddleXpos -= 15;
		} else {
			System.out.println("Is paused.");
		}
	}

}
