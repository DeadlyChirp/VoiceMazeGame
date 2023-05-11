package com.VocalMaze.ViewUtils;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image img;
  
    public ImagePanel(String img) {
      	this(new ImageIcon(img).getImage());
    }
  
    public ImagePanel(Image img) {
		this.img = img;

		// Get the screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Set the size of the panel to the screen size
		setPreferredSize(screenSize);
		setMinimumSize(screenSize);
		setMaximumSize(screenSize);
		setSize(screenSize);

		setLayout(null);
  	}

  	public void paintComponent(Graphics g) {
    	g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
  	} 
  
}