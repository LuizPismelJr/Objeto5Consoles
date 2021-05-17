import java.awt.EventQueue;
import javax.swing.JFrame;

public class Aplication extends JFrame 
{
	public Aplication()
	{
		StartUI();
	}

	private void StartUI() 
	{
		add(new Board());
     
		setResizable(false);
		pack();
     
		setTitle("Objeto-5-Consoles-QuadTree");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) 
	{
        EventQueue.invokeLater(() -> 
        {
            JFrame ex = new Aplication();
            ex.setVisible(true);
        });
    }
	
}
