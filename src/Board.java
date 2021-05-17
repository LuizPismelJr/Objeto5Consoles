import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class Board extends JPanel implements ActionListener {
	
	private final int B_WIDTH = 500;
    private final int B_HEIGHT = 500;
    
    private final int DOT_SIZE = 10; 
    private final int RAND_POS = 49;
    private final int DELAY = 140;
    
    private JButton button01;
    private JButton button02;
    private JButton button03;
    private JTextField inputField;
    private int numeroParticulas;
    
    private Dot[] particulas;
    
    private boolean inGame = false;

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
        
        setBackground(Color.white);
        
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
    	
    	button03 = new JButton("CriarParticulas");
    	button03.setSize(50,50);
    	button03.setVisible(true);
    	panel.add(button03);
    	button03.addActionListener(this);
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
        		for(int i = 0; i < particulas.length; i++) 
        		{
        			g.setColor(Color.yellow);
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
            particulas[i].axis = ale.nextInt(4);
            particulas[i].velocity = ale.nextInt(10) + 1;
    	}
    }
    private void move() 
    {	
    	for(Dot d : particulas) 
    	{
    		//Random rand = new Random();
    		//d.axis = rand.nextInt(4);
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
    		}
    	 }
   }
    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) 
        {
        	move();
        }
        repaint();
        
        if (e.getSource() == button01) 
        {
        	
        }
        
        if (e.getSource() == button02) 
        {
        	
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
    }
}
