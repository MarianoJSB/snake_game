package snake2;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Random;

public class Snake2 extends JFrame {
    
    ImagenSnake imagenSnake;
    Point snake;
    Point comida;
    ArrayList<Point> listaPosiciones = new ArrayList<Point>();

    int longitud = 2;
    //WINDOW SIZE//
    int width = 1000;
    int height = 720;
    
    //SNAKE AND APPLE SIZE//
    int widthPoint = 20;
    int heightPoint = 20;

    
    String direccion = "RIGHT";
    long frequency = 50;//SPEED//

    boolean gameOver = false;
    
    //START GAME//
    public Snake2() {
		setTitle("Snake");

        startGame();
        imagenSnake = new ImagenSnake();

        this.getContentPane().add(imagenSnake);

		setSize(width,height);
		
		this.addKeyListener(new Teclas());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(false);
		setUndecorated(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        setVisible(true);
        Momento momento = new Momento();
		Thread trid = new Thread(momento);
		trid.start();
    }

    public void startGame() {
		comida = new Point(200,100);	
        snake = new Point(320,240);
		listaPosiciones = new ArrayList<Point>();
        listaPosiciones.add(snake);

		longitud = listaPosiciones.size();        
    }
        //APPLE PART//
	public void generarComida() {
		Random rnd = new Random();
		
		comida.x = (rnd.nextInt(width)) + 5;
		if((comida.x % 5) > 0) {
			comida.x = comida.x - (comida.x % 5);
		}

		if(comida.x < 5) {
			comida.x = comida.x + 10;
		}
		if(comida.x > width) {
			comida.x = comida.x - 10;
		}

		comida.y = (rnd.nextInt(height)) + 5;
		if((comida.y % 5) > 0) {
			comida.y = comida.y - (comida.y % 5);
		}	

		if(comida.y > height) {
			comida.y = comida.y - 10;
		}
		if(comida.y < 0) {
			comida.y = comida.y + 10;
		}

	}
        //APPLE RESPAWN//
	public void actualizar() {

        listaPosiciones.add(0,new Point(snake.x,snake.y));
		listaPosiciones.remove(listaPosiciones.size()-1);

        for (int i=1;i<listaPosiciones.size();i++) {
            Point point = listaPosiciones.get(i);
            if(snake.x == point.x && snake.y  == point.y) {
                gameOver = true;
            }
        }

		if((snake.x > (comida.x-20) && snake.x < (comida.x+20)) && (snake.y > (comida.y-20) && snake.y < (comida.y+20))) {
            listaPosiciones.add(0,new Point(snake.x,snake.y));
			System.out.println(listaPosiciones.size());
			generarComida();
		}
        imagenSnake.repaint();

	}

	public static void main(String[] args) {
		Snake2 snake1 = new Snake2();
	}
    //GAME OVER MENU//
    public class ImagenSnake extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if(gameOver) {
                g.setColor(new Color(0,0,0));
            } else {
                g.setColor(new Color(0,0,0));
            }
            g.fillRect(0,0, width, height);
            g.setColor(new Color(87,166,57));
    
            if(listaPosiciones.size() > 0) {
                for(int i=0;i<listaPosiciones.size();i++) {
                    Point p = (Point)listaPosiciones.get(i);
                    g.fillRect(p.x,p.y,widthPoint,heightPoint);
                }
            }
            
            g.setColor(new Color(255,0,0));
            g.fillOval(comida.x,comida.y,20,20);
            
            if(gameOver) {
                g.setFont(new Font("Berlin Sans", Font.BOLD, 30));
                g.setColor(new Color(255,255,255));
                g.drawString("JUEGO TERMINADO", 150, 200);
                g.drawString("PUNTUACION: "+(listaPosiciones.size()-1), 150, 240);

                g.setFont(new Font("Berlin Sans", Font.BOLD, 20));
                g.setColor(new Color(255,255,255));
                g.drawString("ENTER para reiniciar", 150, 380);
                g.drawString("ESC para salir", 150, 400);
                }
            }

        }

        //CONTROLS//
	public class Teclas extends java.awt.event.KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            gameOver=true;
                            if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                                System.exit(0);
                            }
			} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {

			  if(direccion != "LEFT") {
                    direccion = "RIGHT";
				}
			}
                        else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				if(direccion != "RIGHT") {
                    direccion = "LEFT";
				}
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				if(direccion != "DOWN") {
                    direccion = "UP";
				}
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(direccion != "UP") {
                    direccion = "DOWN";
				}				
			
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                gameOver = false;
                startGame();				
			}
		}

	}

	public class Momento extends Thread {
		
		private long last = 0;
		
		public Momento() {
			
		}
                //RUN GAME//
		public void run() {
			while(true) {
				if((java.lang.System.currentTimeMillis() - last) > frequency) {
					if(!gameOver) {

                        if(direccion == "RIGHT") {
                            snake.x = snake.x + widthPoint;
                            if(snake.x > width) {
                                gameOver=true;
                            }
                        } else if(direccion == "LEFT") {
                            snake.x = snake.x - widthPoint;
                            if(snake.x < 0) {
                                gameOver=true;
                            }                        
                        } else if(direccion == "UP") {
                            snake.y = snake.y - heightPoint;
                            if(snake.y < 0) {
                                gameOver=true;
                            }                        
                        } else if(direccion == "DOWN") {
                            snake.y = snake.y + heightPoint;
                            if(snake.y > height) {
                               gameOver=true;
                            }                        
                        }
                    }
                    actualizar();
					
					last = java.lang.System.currentTimeMillis();
				}

			}
		}
	}

}

