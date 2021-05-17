import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class Board extends JPanel implements ActionListener {
	// Tamanho do tabuleiro
	private final int B_WIDTH = 500;
    private final int B_HEIGHT = 500;
    
    private final int DOT_SIZE = 10; // Incremento de movimento 
    private final int ALL_DOTS = 900; // Quantidade maxima de nos da serpente
    private final int RAND_POS = 39;
    private final int DELAY = 140; // Frequencia do game loop

    // Array contendo a posicao de cada elemento do corpo da serpente
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots; // Armazena a quantidade de elementos no corpo da serpente
    private int apple_x; // Posicao x da maca
    private int apple_y; // Posicao y da maca

    // Direcao da serpente
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    
    private JButton button01;
    private JButton button02;
    private JButton button03;
    private JTextField inputField;
    private int numeroParticulas;
    
    private Dot[] particulas;
    
    
    // Jogo ainda em andamento?
    private boolean inGame = true;

    // Temporizador para configurar a velocidade do jogo
    private Timer timer;
    
    public Board() 
    {
        
        initBoard();
    }
    
    /*** 
     * Funcao para inicializar o tabuleiro
     */
    private void initBoard() {
    	
    	//criando painel
    	JPanel panel = new JPanel();
    	this.add(panel);
    	
    	//iniciando interface
    	iniUi(panel);
    	
    	// Adiciona um objeto de classe responsavel pela leitura do teclado
        addKeyListener(new TAdapter());
        
        // Seta a cor de fundo da janela
        setBackground(Color.white);
        
        // Coloca o foco nesta janela
        setFocusable(true);

        // Configura a dimensao da janela
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        
        // Inicializa o jogo
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
    
    /*** 
     * Funcao para inicializar o jogo
     */
    private void initGame() {
    	
 	// Numero de elementos iniciais no corpo da serpente
        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
        
        
     
     }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    /***
     * Metodo responsavel pelo desenho dos elementos na tela
     * @param g
     */
    private void doDrawing(Graphics g) {
        
        if (inGame) {
        	g.setColor(Color.GREEN);
        	g.fillOval(apple_x, apple_y, 10, 10);
        	
        	
        	if(particulas != null) 
        	{
        		for(int i = 0; i < particulas.length; i++) 
        		{
        			g.setColor(Color.yellow);
        			g.fillOval(particulas[i].x,particulas[i].y,10,10);
        		}
        	}
        	

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                	g.setColor(Color.red);
                	g.fillOval(x[z], y[z], 10, 10);
                } else {
                	g.setColor(Color.BLUE);
                	g.fillOval(x[z], y[z], 10, 10);
                }
            }

            java.awt.Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
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

    /***
     * Metodo para desenhar a funcao de game over
     * @param g
     */
    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    /** 
     * Funcao que verifica se a maca foi comida
     */
    private void checkApple() {

    	if (x[0] == apple_x && y[0] == apple_y)
    	{
    		dots += 1;
    		locateApple();
    	}
    	
        // TODO
    }

    /***
     * Método responsavel por mover a serpente
     */
    private void move() {
    	
    	for (int i = dots; i > 0; i--) {
    		
    		if (i != 0) {
        		x[i] = x[i - 1];
        		y[i] = y[i - 1];
        	}

    	}
    	
    	if (rightDirection) {
    		x[0] += 10;
    	}
    	
    	if (leftDirection) {
    		x[0] -= 10;
    	}
    	
    	if (upDirection) {
    		y[0] -= 10;
    	}
    	
    	if (downDirection) {
    		y[0] += 10;
    	}

        // TODO
    }

    
    /***
     * Verificar se houve colisao com a propria cobra ou se a mesma saiu da tela
     */
    private void checkCollision() {
    	
    	if (x[0] < 0 || x[0] >= B_WIDTH || y[0] < 0 || y[0] >= B_HEIGHT) 
    	{
    		inGame = false;
    	}
    	
    	for (int i = dots; i > 0; i--)
    	{
    		if(x[i] == x[0] && y[i] == y[0]) 
    		{
    			inGame = false;
    		}
    	}
    }

    
    /***
     * Metodo responsavel por reposicionar a maca
     */
    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    /*** 
     * Metodo responsavel pelo game loop do jogo
     */
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            //checkApple();
            //checkCollision();
            //move();
            
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
        		//dots = numeroParticulas;
        		
        	}
        	catch(NumberFormatException f)
        	{
        		inputField.setText("Insert Numbers");
        	}
        }
    }

    /*** 
     * 
     * @author josericardo
     * Classe que verifica se houve mudanca de orientacao
     */
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            
            if ((key == KeyEvent.VK_SPACE)) 
            {
            	System.out.println("teste");
            }
            
        }
    }
}
