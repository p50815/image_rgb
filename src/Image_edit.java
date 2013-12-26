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
	
	JFrame f;									// 目前的視窗
	JPanel panel;								// 目前的面板
	JLabel label_r, label_g, label_b;			//Label
	JSlider slider_r, slider_g, slider_b;		//Slider
	JButton light, dark, original, dark_land;	//Button
	JMenuBar menuBar;							//MenuBar
	JMenuItem save;								//MenuItem
	JScrollPane scrollPane;						// 捲軸
	
	String Filename = null; // 目前的檔名 
	BufferedImage image = null; // 目前的影像
	//BufferedImage image; 						// 目前的影像
	
	public Image_edit(){
		
	}
	public void showWindow(){
		//建立視窗並且顯示圖形的程式碼
		f = new JFrame("影像效果");
		
		// 建立Menu		
		menuBar = new JMenuBar();
		
		// 建立 File 選項 
		JMenu File_Menu=new JMenu("File");
		
		// 把 File 加到主選單上 
		menuBar.add(File_Menu);
		
		// 建立 MenuItem
		save = new JMenuItem("存檔");
		
		// 把 MenuItem 加到 Menu
		File_Menu.add(save);
		
		// 把 Menu 加到視窗
		f.setJMenuBar(menuBar);

		// 建立面板
		panel=new JPanel();
		
		// 建立 Slider
		slider_r=new JSlider();
		slider_g=new JSlider();
		slider_b=new JSlider();
		
		// 建立 Button
		dark_land=new JButton("暗角");
		light=new JButton("加亮");
		dark=new JButton("變暗");
		original=new JButton("恢復原狀");
		
		// 建立 Label
		label_r=new JLabel("");
		label_g=new JLabel("");
		label_b=new JLabel("");
		
		// 設定視窗
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
		
		// Step 1: 若影像超過螢幕， 則加入捲軸 
		scrollPane = new JScrollPane(new JLabel("請新增圖片"));
		
		// 加入視窗
		f.getContentPane().add(scrollPane);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		f.getContentPane().add(BorderLayout.WEST, panel);
		
		f.pack(); 
		
		// Step 2: 設定點選 x 表示關閉視窗 
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		// 設定視窗大小
		f.setSize(800, 600);
		
		// Step 4: 設定視窗顯示在螢幕中央 
		f.setLocationRelativeTo(null); 
		
		// Step 5: 顯示出視窗 
		f.setVisible(true);	

	}
	public void setWindow(){
		// Step 3: 設定視窗標題 
		//f.setTitle(Filename+" "+image.getWidth()+ " x "+image.getHeight());
		
		// 設定 Slider 最大值
		int max=200;
		slider_r.setMaximum(max);
		slider_g.setMaximum(max);
		slider_b.setMaximum(max);
				
		// 設定 Slider 最小值
		int min=0;
		slider_r.setMinimum(min);
		slider_g.setMinimum(min);
		slider_b.setMinimum(min);
				
		// 設定 Slider Value
		int value=100;
		slider_r.setValue(value);
		slider_g.setValue(value);
		slider_b.setValue(value);
				
		// 設定 Label Value
		label_r=new JLabel(slider_r.getValue()/100.0+"");
		label_g=new JLabel(slider_g.getValue()/100.0+"");
		label_b=new JLabel(slider_b.getValue()/100.0+"");
		
	}
}