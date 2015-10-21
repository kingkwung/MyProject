import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookInfoFrame {

	private static final int width = 505;

	private static final int height = 500 - 5;

	int panelHeight = 180;

	int panelWidth = 490;

	private JFrame frame = new JFrame("Book manage frame");

	private JPanel imagePanel = new JPanel();

	private JPanel informPanel = new JPanel();

	private JTextField nameText = new JTextField();

	private JTextField authorText = new JTextField();

	private JTextField genreText = new JTextField();

	private JTextField new_oldText = new JTextField();

	private JTextField durationText = new JTextField();

	private JTextField checkReservedText = new JTextField();

	private JTextField borrowListText = new JTextField();

	private JTextField seriesText = new JTextField();
	
	

	private JLabel img = new JLabel();

	public BookInfoFrame() {

		frame.setLayout(null);

		frame.setBounds(300, 100, width - 10, height - 10 + 23);

		frame.setResizable(false);

		// 그림부분 패널 설정~!

		imagePanel.setBounds(0, 0, panelWidth, panelHeight);

		imagePanel.setLayout(null);

		imagePanel.setBackground(new Color(230, 230, 230));

		img.setBounds(10, 10, 100 + 20, 143 + 20);
		ImageIcon bookImg = new ImageIcon("book" + "1" + ".png");// 받아
		img.setIcon(bookImg);
		imagePanel.add(img);
		
		
		frame.add(imagePanel);

		// 그림부분 패널 설정 끝~!

		informPanel.setBounds(0, panelHeight, panelWidth, height);

		informPanel.setLayout(null);

		informPanel.setBackground(new Color(219, 252, 182));

		nameText.setEditable(false);

		nameText.setText("NAME : ");

		nameText.setBounds(10, 10, panelWidth - 20, 35);

		informPanel.add(nameText);

		authorText.setEditable(false);

		authorText.setText("AUTHOR : ");

		authorText.setBounds(10, 45, panelWidth - 20, 35);

		informPanel.add(authorText);

		genreText.setEditable(false);

		genreText.setText("GENRE : ");

		genreText.setBounds(10, 80, panelWidth - 20, 35);

		informPanel.add(genreText);

		new_oldText.setEditable(false);

		new_oldText.setText("NEW/OLD : ");

		new_oldText.setBounds(10, 115, panelWidth - 20, 35);

		informPanel.add(new_oldText);

		durationText.setEditable(false);

		durationText.setText("DURATION : ");

		durationText.setBounds(10, 150, panelWidth - 20, 35);

		informPanel.add(durationText);

		checkReservedText.setEditable(false);

		checkReservedText.setText("RESERVED : ");

		checkReservedText.setBounds(10, 185, panelWidth - 20, 35);

		informPanel.add(checkReservedText);

		borrowListText.setEditable(false);

		borrowListText.setText("BORROWED LIST : ");

		borrowListText.setBounds(10, 220, panelWidth - 20, 35);

		informPanel.add(borrowListText);

		seriesText.setEditable(false);

		seriesText.setText("SERIES : ");

		seriesText.setBounds(10, 255, panelWidth - 20, 35);

		informPanel.add(seriesText);

		frame.add(informPanel);

		frame.setVisible(true);

	}

	public static void main(String[] args) {

		BookInfoFrame bookSearch = new BookInfoFrame();

	}

}
