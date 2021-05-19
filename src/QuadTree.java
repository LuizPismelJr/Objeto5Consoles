import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuadTree 
{
	
	public boolean divided = false;
	static int dividedCount = 0;
	
	public QuadTree noroeste;
	public QuadTree nordeste;
	public QuadTree sudeste;
	public QuadTree sudoeste;
	
	private List <Dot> particulas = new ArrayList<Dot>();
	int capacity;
	public Quad quadra;
	String setor;
	
	int larguraTela, alturaTela;
	
	public QuadTree(Quad quad, int capacity, int largura, int altura) 
	{
		larguraTela = largura;
		alturaTela = altura;
		if (setor == null) {
			setor = "AreaCompleta";
		}
		this.quadra = quad;
		this.capacity = capacity;
		Board.tree.add(this);
	}
	
	public void fragmentar() {
		
		Quad QuadNL = new Quad(quadra.x, quadra.y, quadra.w/2,quadra.h/2);
		this.noroeste = new QuadTree(QuadNL, capacity, larguraTela, alturaTela);
		this.noroeste.setor = "noroeste";
				
		Quad QuadNO = new Quad(quadra.x + quadra.w / 2 , quadra.y, quadra.w/2,quadra.h/2);
		this.nordeste = new QuadTree(QuadNO, capacity, larguraTela, alturaTela);
		this.nordeste.setor = "nordeste";
		
		Quad QuadSL = new Quad(quadra.x, quadra.y + quadra.h/2, quadra.w/2,quadra.h/2);
		this.sudeste = new QuadTree(QuadSL, capacity, larguraTela, alturaTela);
		this.sudeste.setor = "sudeste";
		
		Quad QuadSO = new Quad(quadra.x + quadra.w / 2 , quadra.y + quadra.h/2, quadra.w/2,quadra.h/2);
		this.sudoeste = new QuadTree(QuadSO, capacity, larguraTela, alturaTela);
		this.sudoeste.setor = "sudoeste";
		
		divided = true;				
	}
	
	public void inserir(Dot particula) 
	{
		if(!this.quadra.Contains(particula))
		{
			return;
		}	
		if(particulas.size() < capacity && !particulas.contains(particula)) 
		{
			particulas.add(particula);
		}
		else 
		{			
			if(!divided) 
			{			
				fragmentar();
				
				for (int i = 0; i < particulas.size(); i++) 
				{
					this.noroeste.inserir(particulas.get(i));
					this.nordeste.inserir(particulas.get(i));
					this.sudeste.inserir(particulas.get(i));
					this.sudoeste.inserir(particulas.get(i));		
				}
			}
			this.noroeste.inserir(particula);
			this.nordeste.inserir(particula);
			this.sudeste.inserir(particula);
			this.sudoeste.inserir(particula);
		}
	}
		
	boolean DoPitagoras(Dot particula1, Dot particula2)
	{
		return(((particula2.x - particula1.x) * (particula2.x - particula1.x) + (particula2.y - particula1.y) * (particula2.y - particula1.y)) <= 24.5f);					
	}
	
	public void collisionsCheck() 
	{
		Random rnd = new Random();
			
		for (int i = 0; i < particulas.size(); i++) 
		{
			int k = 0;
				
			for (int j = 0; j < particulas.size(); j++)
			{
				if (DoPitagoras(particulas.get(i), particulas.get(j)) && particulas.get(i) != particulas.get(j))
				{
					k++;
				}		
			}
				
			if(k > 0) 
			   {
				   particulas.get(i).axis = rnd.nextInt(8);
				   particulas.get(i).impact = true;
			   }
			   else
			   {
				   particulas.get(i).impact = false;
			   }
		}
	}	
}
