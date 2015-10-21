 
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class BackgroundImage extends JFrame {
    JScrollPane scrollPane;
    ImageIcon icon;
    public BackgroundImage() {
        icon = new ImageIcon("./img/tmp.png");

        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                //  Approach 1: Dispaly image at at full size
                g.drawImage(icon.getImage(), 0, 0, null);
                //  Approach 2: Scale image to size of component
                // Dimension d = getSize();
                // g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
                // Approach 3: Fix the image position in the scroll pane
                // Point p = scrollPane.getViewport().getViewPosition();
                // g.drawImage(icon.getImage(), p.x, p.y, null);
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        JButton button = new JButton("Hello");
        panel.add(button);
        scrollPane = new JScrollPane(panel);
        setContentPane(scrollPane);

        setSize(960, 720);
        setVisible(true);
    }

    public static void main(String[] args) {
        BackgroundImage frame = new BackgroundImage();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
