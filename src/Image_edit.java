import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

class Image_edit{
	
	JFrame f;									// �ثe������
	JPanel panel;								// �ثe�����O
	JLabel label_r, label_g, label_b;			//Label
	JSlider slider_r, slider_g, slider_b;		//Slider
	JButton light, dark, original, dark_land;	//Button
	JMenuBar menuBar;							//MenuBar
	JMenuItem save;								//MenuItem
	JScrollPane scrollPane;						// ���b
	
	String Filename = null; // �ثe���ɦW 
	BufferedImage image = null; // �ثe���v��
	//BufferedImage image; 						// �ثe���v��
	
	public Image_edit(){
		
	}
	public void showWindow(){
		//�إߵ����åB��ܹϧΪ��{���X
		f = new JFrame("�v���ĪG");
		
		// �إ�Menu		
		menuBar = new JMenuBar();
		
		// �إ� File �ﶵ 
		JMenu File_Menu=new JMenu("File");
		
		// �� File �[��D���W 
		menuBar.add(File_Menu);
		
		// �إ� MenuItem
		save = new JMenuItem("�s��");
		
		// �� MenuItem �[�� Menu
		File_Menu.add(save);
		
		// �� Menu �[�����
		f.setJMenuBar(menuBar);

		// �إ߭��O
		panel=new JPanel();
		
		// �إ� Slider
		slider_r=new JSlider();
		slider_g=new JSlider();
		slider_b=new JSlider();
		
		// �إ� Button
		dark_land=new JButton("�t��");
		light=new JButton("�[�G");
		dark=new JButton("�ܷt");
		original=new JButton("��_�쪬");
		
		// �إ� Label
		label_r=new JLabel("");
		label_g=new JLabel("");
		label_b=new JLabel("");
		
		// �]�w����
		setWindow();
		
		panel.add(slider_r);
		panel.add(label_r);
		panel.add(slider_g);
		panel.add(label_g);
		panel.add(slider_b);
		panel.add(label_b);
		panel.add(dark_land);
		panel.add(light);
		panel.add(dark);
		panel.add(original);
		
		// Step 1: �Y�v���W�L�ù��A �h�[�J���b 
		scrollPane = new JScrollPane(new JLabel("�зs�W�Ϥ�"));
		
		// �[�J����
		f.getContentPane().add(scrollPane);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		f.getContentPane().add(BorderLayout.WEST, panel);
		
		f.pack(); 
		
		// Step 2: �]�w�I�� x ����������� 
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		// �]�w�����j�p
		f.setSize(800, 600);
		
		// Step 4: �]�w������ܦb�ù����� 
		f.setLocationRelativeTo(null); 
		
		// Step 5: ��ܥX���� 
		f.setVisible(true);	

	}
	public void setWindow(){
		// Step 3: �]�w�������D 
		//f.setTitle(Filename+" "+image.getWidth()+ " x "+image.getHeight());
		
		// �]�w Slider �̤j��
		int max=200;
		slider_r.setMaximum(max);
		slider_g.setMaximum(max);
		slider_b.setMaximum(max);
				
		// �]�w Slider �̤p��
		int min=0;
		slider_r.setMinimum(min);
		slider_g.setMinimum(min);
		slider_b.setMinimum(min);
				
		// �]�w Slider Value
		int value=100;
		slider_r.setValue(value);
		slider_g.setValue(value);
		slider_b.setValue(value);
				
		// �]�w Label Value
		label_r=new JLabel(slider_r.getValue()/100.0+"");
		label_g=new JLabel(slider_g.getValue()/100.0+"");
		label_b=new JLabel(slider_b.getValue()/100.0+"");
		
	}
}