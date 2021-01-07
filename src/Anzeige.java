import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.desktop.QuitEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Anzeige extends JPanel implements KeyListener, MouseMotionListener, MouseWheelListener, MouseListener{

	private static final long serialVersionUID = 1L; //don't know what it is but java wants it
	private boolean shiftPressed = false;
	private int[][] playGround;
	private int mode = 0;
	private int numberPlayer = 0;
	private int round = 1;
	private ArrayList<Point>[] playerPos;
	private ArrayList<Point> startPlayer;
	private ArrayList<Point>[] playerVektor;
	private boolean[] playerOK;
	private static int k�stchen = 20;
	private ArrayList<Color> playerColor;
	private int turn;
	private double verschiebungX = 0.;
	private double verschiebungY = 0.;
	private Point mouseStart = new Point(0,0);
	private boolean startDrag = true;
	private int actButton;
	private ArrayList<Integer> rank;
	private PlayGround pGround;
	private String initial = "";	//to save() with a specific name
	private int positionSave;
	private KI ki;
	private String link = "Links";

	private final int width = 60;  //60x44 is about Din-A4
	private final int height = 44;

	public Anzeige(KI nKi, int nPositionSave) {
		positionSave = nPositionSave;
		pGround = new PlayGround(width, height);
		playGround = new int[width][height];
		ki= nKi;
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addMouseListener(this);
		this.setFocusable(true);
//		update();
		playerColor = new ArrayList<Color>();
		startPlayer = new ArrayList<Point>();
		rank = new ArrayList<Integer>();
	}

	public Anzeige(PlayGround nPGround) {
		pGround = nPGround;
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addMouseListener(this);
		this.setFocusable(true);
		playGround = pGround.getPlayGround();
		playerColor = new ArrayList<Color>();
		playerColor.add(null);
		update();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Point actVerschiebung = new Point((int) (verschiebungX*k�stchen), (int) (verschiebungY*k�stchen));
		Graphics2D g2d = (Graphics2D) g;
		setBackground(Color.WHITE);
		g2d.setStroke(new BasicStroke(0.03F*k�stchen));
		g2d.setFont(new Font("Arial Bold", 0, 40));
		g.setColor(Color.GRAY);  // Draw grid
		for(int i = 0; i < playGround[0].length; i++) {
			g2d.drawLine(0+actVerschiebung.x, i*k�stchen+actVerschiebung.y, k�stchen*(playGround.length-1)+actVerschiebung.x, i*k�stchen+actVerschiebung.y);
		}
		for(int i = 0; i < playGround.length; i++) {
			g2d.drawLine(i*k�stchen+actVerschiebung.x, 0+actVerschiebung.y, i*k�stchen+actVerschiebung.x, k�stchen*(playGround[0].length-1)+actVerschiebung.y);
		}

		for(int i = 0; i < playGround.length; i++) {
			for(int j = 0; j < playGround[i].length;j++) {
				switch(playGround[i][j]) {
				case(1):
					g.setColor(Color.GRAY);
				g.fillOval(i*k�stchen-k�stchen/4+actVerschiebung.x, j*k�stchen-k�stchen/4+actVerschiebung.y, k�stchen/2, k�stchen/2);
				break;
				case(2):g.setColor(Color.GREEN);
				g.fillOval(i*k�stchen-k�stchen/4+actVerschiebung.x, j*k�stchen-k�stchen/4+actVerschiebung.y, k�stchen/2, k�stchen/2);
				break;
				case(3):g.setColor(Color.RED);
				g.fillOval(i*k�stchen-k�stchen/4+actVerschiebung.x, j*k�stchen-k�stchen/4+actVerschiebung.y, k�stchen/2, k�stchen/2);
				break;
				}
			}
		}

		g2d.setStroke(new BasicStroke(0.1F*k�stchen));
		if(mode >= 4 && mode != 6) {
			for(int i = 0; i < numberPlayer; i++) {
				g.setColor(playerColor.get(i));
				for(int j = 0; j < playerPos[i].size(); j++) {
					g.fillOval(playerPos[i].get(j).x*k�stchen-k�stchen/4+actVerschiebung.x, playerPos[i].get(j).y*k�stchen-k�stchen/4+actVerschiebung.y, k�stchen/2, k�stchen/2);
					if(j > 0) {
						g2d.drawLine(playerPos[i].get(j).x*k�stchen+actVerschiebung.x, playerPos[i].get(j).y*k�stchen+actVerschiebung.y, playerPos[i].get(j-1).x*k�stchen+actVerschiebung.x, playerPos[i].get(j-1).y*k�stchen+actVerschiebung.y);
					}
				}
			}
			g2d.setStroke(new BasicStroke(0.08F*k�stchen));
			g2d.setColor(playerColor.get(turn));
			g2d.drawRect((playerPos[turn].get(round-1).x + playerVektor[turn].get(round-1).x) * k�stchen -k�stchen + actVerschiebung.x, (playerPos[turn].get(round-1).y+playerVektor[turn].get(round-1).y)*k�stchen -k�stchen + actVerschiebung.y, k�stchen*2, k�stchen*2);
			g2d.setStroke(new BasicStroke(0.1F*k�stchen));
			g2d.drawOval((playerPos[turn].get(round-1).x + playerVektor[turn].get(round-1).x) * k�stchen -k�stchen/8 + actVerschiebung.x, (playerPos[turn].get(round-1).y+playerVektor[turn].get(round-1).y)*k�stchen -k�stchen/8 + actVerschiebung.y, k�stchen/4, k�stchen/4);
		}else {
			for(int i = 0; i < numberPlayer; i++) {
				g.setColor(playerColor.get(i));
				g.fillOval(startPlayer.get(i).x*k�stchen-k�stchen/4+actVerschiebung.x, startPlayer.get(i).y*k�stchen-k�stchen/4+actVerschiebung.y, k�stchen/2, k�stchen/2);
			}
		}

		//Display who's turn it is
		if(playerColor.size() != 0){
			g.setColor(playerColor.get(turn));
			g.fillRect(0, 0, 30, 30);
		}

		if(mode == 5) {
			for(int i = 0; i < rank.size(); i++) {
				g.setColor(playerColor.get(rank.get(i)));
				g.fillRect(i*100, 0, 100, 100);
			}
			g2d.drawString("The other Players did'n finish the race", 0, 200);
		}
	}

	private void start(Point p) {
		int x = (int) Math.round(p.x*1./k�stchen*1.);
		int y = (int) Math.round(p.y*1./k�stchen*1.);
		playGround[(int) x][(int) y] = 2;
	}

	private void ziel(Point p) {
		int x = (int) Math.round(p.x*1./k�stchen*1.);
		int y = (int) Math.round(p.y*1./k�stchen*1.);
		playGround[x][y] = 3;
	}

	private void setPlayer(Point p) {
		int x = (int) Math.round(p.x*1./k�stchen*1.);
		int y = (int) Math.round(p.y*1./k�stchen*1.);
		if(playGround[x][y] == 2) {
			startPlayer.add(new Point(x, y));
			playerColor.add(null);
			refreshColor();
			this.repaint();
			numberPlayer++;
		}
	}

	private void refreshColor() { //mindfuck but works... (creates Colors for each player in a way that they differ as much as possible)
		double colorSpace = 0.6/(playerColor.size()-1);
		for(int i = 0; i < playerColor.size(); i++) {
			double colorMulti = colorSpace*i;
			int r = 0;
			if(colorMulti < 1./3.) {
				r = (int) (765 * (1./3. - colorMulti)); // 255*3 = 765
			}else if( colorMulti > 2./3.) {
				r = (int) (765 * (colorMulti - 2./3.));
			}
			int g = 0;
			if(colorMulti < 2./3.) {
				g = (int) (765 * (1./3. - Math.abs(1./3. - colorMulti)));
			}
			int b = 0;
			if(colorMulti > 1./3.) {
				b = (int) (765 * (1./3. - Math.abs(2./3. - colorMulti)));
			}
			playerColor.set(i, new Color(r,g,b));
		}
	}

	private void addPlayGround(Point point) { //TODO there is a better way
		if(shiftPressed) {
			for(int i = 0; i < playGround.length; i++) {
				for(int j = 0 ; j < playGround[i].length; j++) {
					if(Math.abs(i*k�stchen-point.x)+Math.abs(j*k�stchen-point.y) < 30) {
						playGround[i][j] = 0;
					}
				}
			}
		}else {
			for(int i = 0; i < playGround.length; i++) {
				for(int j = 0 ; j < playGround[i].length; j++) {
					if(Math.abs(i*k�stchen-point.x)+Math.abs(j*k�stchen-point.y) < 30) {
						playGround[i][j] = 1;
					}
				}
			}
		}
		this.repaint();
	}

	@SuppressWarnings("unchecked")
	private void readKey(char keyChar) {
		if(mode != 4 && mode != 5) {
			switch(keyChar) {
			case('d'): mode = 0;	//draw road
			break;
			case('s'): mode = 1;	//set StartPoint
			break;
			case('z'): mode = 2;	//set EndPoint
			break;
			case('g'): mode = 3;	//set Player
			break;
			case('l'):	//start the game
				mode = 4;
			playerPos = new ArrayList[numberPlayer];
			playerVektor = new ArrayList[numberPlayer];
			playerOK = new boolean[numberPlayer];
			for(int i = 0; i < numberPlayer; i++) {
				playerOK[i] = true;
				playerPos[i] = new ArrayList<Point>();
				playerPos[i].add(startPlayer.get(i));
				playerVektor[i] = new ArrayList<Point>();
				playerVektor[i].add(new Point(0,0));
			}
			pGround.setPlayer(playerPos, playerVektor, playerOK);
			update();
			break;
			case('k'):	//KI
				mode = 6; //TODO (propably finished)
			ki.startThatPGround(pGround);
			update();
			break;
			case('u'):	//save the PlayGround
				update();
			save();
			break;
			default:
				break;
			}
		}else if(mode == 4){
			int value = Character.getNumericValue(keyChar);
			if(value > 0 && value < 10) {
				if(pGround.moveChoosed(value)) {
					update();
				}else {
					rank = pGround.getRank();
					if(rank.size() != 0) {	//save only if player made it to the goal
						save();
					}
					finish();
				}
			}
		}else if(mode == 5){	//should not occur. Already done in update()
			update();
			save();
			finish();
		}
	}

	private void update() {
		playerOK = pGround.getPlayerOk();
		playerVektor = pGround.getPlayerVektor();
		playerPos = pGround.getPlayerPos();
		playGround = pGround.getPlayGround();
		turn = pGround.getTurn();
		round = pGround.getRound();
		//		if(pGround.getFinished()) {	//game is finished
		//			mode = 5;
		//			rank= pGround.getRank();
		//			save();
		//			finish();
		//		}
		refreshColor();
		this.repaint();
	}

	private void finish() {	//restart the Game
		mode = 0;
		round = 1;
		numberPlayer = 0;
		pGround = new PlayGround(width, height);
		playerColor = new ArrayList<Color>();
		startPlayer = new ArrayList<Point>();
		rank = new ArrayList<Integer>();
		update();
	}

	private void save() {
		new WriteFile(initial+positionSave).savePG(playGround, playerVektor[0]);
		new WriteFile(link).add(initial+positionSave);
		positionSave++;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(actButton == 3) {
			//			System.out.println("mS: (" + mouseStart.x + "; " + mouseStart.y + ") actLoc: (" + arg0.getPoint().x + "; " + arg0.getPoint().y + ") verschiebung: (" + verschiebung.x + "; " + verschiebung.y + ")");
			if(startDrag) {
				startDrag = false;
			}else {
				verschiebungX += (arg0.getPoint().x - mouseStart.x)*1./k�stchen;
				verschiebungY += (arg0.getPoint().y - mouseStart.y)*1./k�stchen;
				this.repaint();
			}
			mouseStart = new Point(arg0.getPoint().x, arg0.getPoint().y);
		}else if(mode == 0) {
			addPlayGround(new Point((int) (arg0.getPoint().x-verschiebungX*k�stchen), (int) (arg0.getPoint().y-verschiebungY*k�stchen)));
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		shiftPressed = e.isShiftDown();
		readKey(e.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		shiftPressed = e.isShiftDown();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(shiftPressed) {
			k�stchen -= e.getScrollAmount();
		}else {
			k�stchen += e.getScrollAmount();
		}
		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		actButton = e.getButton();
		if(actButton == 1) {
			Point p = new Point((int) (e.getPoint().x-verschiebungX*k�stchen), (int) (e.getPoint().y-verschiebungY*k�stchen));
			if(mode == 1) {
				start(p);
			}else if(mode == 2) {
				ziel(p);
			}else if(mode == 3) {
				setPlayer(p);
			}
			this.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		startDrag = true;
	}
}
