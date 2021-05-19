import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class Board extends JPanel implements ActionListener {
	
	private final int B_WIDTH = 650;
    private final int B_HEIGHT = 650;
    
    private Quad quad = new Quad(1,1,B_WIDTH-3, B_HEIGHT-3);
    private QuadTree quadTree = new QuadTree(quad,4,B_WIDTH,B_HEIGHT);
    static List<QuadTree> tree = new ArrayList();
    
    private final int DOT_SIZE = 10; 
    private final int RAND_POS = B_WIDTH/10;
    private final int DELAY = 140;
    
    private JButton button01;
    private JButton button02;
    private JButton button03;
    private JButton button04;
    private JTextField inputField;
    private int numeroParticulas;
    
    private Dot[] particulas;
    
    private boolean inGame = false;
    private boolean inquadTree, inbrutalForce;
    
    private Timer timer;
    
    public Board() 
    {
        
        initBoard();
    }
    private void initBoard() 
    {
    	
    	JPanel panel = new JPanel();
    	this.add(panel);
    	
    	iniUi(panel);
        
        setBackground(Color.black);
        
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        
        
        
        initGame();
    }

    private void iniUi(JPanel panel) 
    {
    	button01 = new JButton("QuadTree");
    	button01.setSize(50, 50);
    	button01.setVisible(true);
    	panel.add(button01);
    	button01.addActionListener(this);
    	
    	inputField = new JTextField(10);
    	add(inputField);
    	
    	button02 = new JButton("BrutalForce");
    	button02.setSize(50,50);
    	button02.setVisible(true);
    	panel.add(button02);
    	button02.addActionListener(this);
    	
    	button03 = new JButton("IniciarParticulas");
    	button03.setSize(50,50);
    	button03.setVisible(true);
    	panel.add(button03);
    	button03.addActionListener(this);
    	
    	button04 = new JButton("Parar");
    	button04.setSize(50,50);
    	button04.setVisible(true);
    	panel.add(button04);
    	button04.addActionListener(this);
    }
    
    private void initGame() 
    {	
        timer = new Timer(DELAY, this);
        timer.start();    
     }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    private void doDrawing(Graphics g) {
        
        if (inGame) 
        {
        	if(particulas != null) 
        	{
        		if(inquadTree) 
        		{
        		g.drawRect(1,1,B_WIDTH-3, B_HEIGHT-3);
            	g.setColor(Color.white);
            	    	
            	for (int i = 0; i < tree.size(); i++) 
            	{
            		g.drawRect(tree.get(i).quadra.x, tree.get(i).quadra.y, tree.get(i).quadra.w, tree.get(i).quadra.h);
            	}
        		}
        		
        		for(int i = 0; i < particulas.length; i++) 
        		{
        			if(particulas[i].impact) 
        			{
        				g.setColor(Color.cyan);
        				
        			}
        			else
        			{
        				g.setColor(Color.darkGray);
        			}
        			g.fillOval(particulas[i].x,particulas[i].y,10,10);
        		}
        	}
        	java.awt.Toolkit.getDefaultToolkit().sync();
        }       
    }
    
    private void createDots(int coeficiente)
    {
    	particulas = new Dot[coeficiente];
    	
    	Random ale = new Random();
    	for(int i = 0; i < particulas.length; i++) 
    	{    		
    		int w = (int) (Math.random() * RAND_POS);	
            int h = (int) (Math.random() * RAND_POS);
    		Dot particula = new Dot( w * DOT_SIZE, h * DOT_SIZE);
    		particulas[i] = particula;
            particulas[i].id = i;
            particulas[i].axis = ale.nextInt(8);
            particulas[i].velocity = ale.nextInt(10) + 1;
    	}
    }
    private void move() 
    {	
    	for(Dot d : particulas) 
    	{
    		
    		switch (d.axis) 
    		{
    		case 1:
    			d.x += d.velocity;
    			break;
    		case 2:
    			d.x -= d.velocity;
    			break;
    		case 3:
    			d.y += d.velocity;
    			break;
    		case 4:
    			d.y -= d.velocity;
    			break;
    		case 5:
    			d.x -= d.velocity;
    			d.y -= d.velocity;
    			break;
    		case 6:
    			d.x += d.velocity;
    			d.y += d.velocity;
    			break;
    		case 7:
    			d.x += d.velocity;
    			d.y -= d.velocity;
    			break;
    		case 8:
    			d.x -= d.velocity;
    			d.y += d.velocity;
    			break;
    		}
    	    
    		if(inquadTree) 
    	    {
    		quadTree.inserir(d);
    	    }
    	}
    	
   }
    
   private void collisionBrutalForce() 
   {
	   Random rnd = new Random();
	   
	   for(int i = 0; i < particulas.length; i++ ) 
	   {
		   int k = 0;
		   
		   for(Dot d : particulas) 
		   {
			   if(doPitagoras(particulas[i],d) && particulas[i] != d) 
			   {
				   k++;
			   }
		   }
		   
		   if(k > 0) 
		   {
			   particulas[i].axis = rnd.nextInt(8);
			   particulas[i].impact = true;
		   }
		   else
		   {
			   particulas[i].impact = false;
		   }
		   
	   }
   }
   
   private void outOfScreen() 
   {
	   for (Dot d : particulas) 
	   {
		   if (d.x > B_WIDTH -10) 
			{
				d.axis = 2;
			}
		
			if (d.x < 0) 
			{
				d.axis = 1;
			}
		
			if (d.y > B_HEIGHT - 10) 
			{
				d.axis = 4;
			}
		
			if (d.y < 0) 
			{
				d.axis = 3;
			}
		}
   }
   
    @Override
    public void actionPerformed(ActionEvent e) {

    	if (inGame) 
        {
        	outOfScreen();
        	tree.clear();
        	
        	System.out.println("Tempo inicial  " + System.currentTimeMillis());
        	
        	if(inquadTree) 
    		{
        		quadTree = new QuadTree(quad, 4, B_WIDTH,B_HEIGHT);
    		}
        	
        	move();
        	
        	if(inquadTree) 
    		{
        		for(int i = 0; i < tree.size(); i++)
        		{
        			tree.get(i).collisionsCheck();
        		}
        		System.out.println("Tempo emQuadTree   " + System.currentTimeMillis());
    		}
        	
        	else if(inbrutalForce) 
        	{
        		collisionBrutalForce();
        		System.out.println("Tempo BrutalForce   " + System.currentTimeMillis());
        	}
        	else 
        	{
        		inbrutalForce = false;
        		inquadTree = false;
        	}
        	
        }
        repaint();
        
        if (e.getSource() == button01) 
        {
        	inquadTree = true;
        	inbrutalForce = false;
        }
        
        if (e.getSource() == button02) 
        {
        	inbrutalForce = true;
        	inquadTree = false;
        }
        
        if (e.getSource() == button03 ) 
        {
        	try 
        	{
        		this.numeroParticulas = Integer.parseInt(inputField.getText());
        		System.out.println(this.numeroParticulas);
        		createDots(numeroParticulas);
        		if(inGame == false) 
        		{
        			this.inGame = true;
        		}
        	}
        	catch(NumberFormatException f)
        	{
        		inputField.setText("Insert Numbers");
        	}
        }
        
        if (e.getSource() == button04) 
        {
        	inGame = false;
        	inbrutalForce = false;
    		inquadTree = false;
        }
    }
    
    boolean doPitagoras(Dot particula1, Dot particula2)
	{
		return(((particula2.x - particula1.x) * (particula2.x - particula1.x) + (particula2.y - particula1.y) * (particula2.y - particula1.y)) <= 24.5f);					
	}
}
