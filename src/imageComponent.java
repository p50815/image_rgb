import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;

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
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class imageComponent implements Runnable, ChangeListener, MouseListener, ActionListener{
	String url,name;
	String Filename; // 目前的檔名 
	BufferedImage image; // 目前的影像 
	
	JFrame f; // 目前的視窗
	JPanel panel;
	JLabel label_r, label_g, label_b;
	JSlider slider_r, slider_g, slider_b;
	JButton light, dark, original, dark_land;
	JMenuBar menuBar;
	JMenuItem save;
	
	int c=0;
	int[] data;
	int Height, Width;
	int[][] Or,Og,Ob;
	int center_x,center_y;
	
	public imageComponent() {
		//載入圖形的程式碼
		Open();
		image=LoadImage(Filename);
		Height=image.getHeight();
		Width=image.getWidth();
		data=image.getRGB(0, 0, Width, Height, null, 0, Width);
		image=CreateBufferedImage_Direct(data, Height, Width);
		
		center_x=Width/2;
		center_y=Height/2;
	}
	public void Open(){ 
		FileDialog fd = new FileDialog(f, "Open...", FileDialog.LOAD); 
		fd.setVisible(true); 
		if(fd!=null){ 
			Filename=fd.getDirectory() + System.getProperty("file.separator").charAt(0) + fd.getFile();
			url=fd.getDirectory();
			name=fd.getFile();
		} 
	}
		
	public void Show(){
		//要求系統以"event-dispatching"執行序，執行目前目建中run的程式碼
		SwingUtilities.invokeLater(this);
	}
	public void run() {
		//建立視窗並且顯示圖形的程式碼
		f = new JFrame(""); 
		
		menuBar = new JMenuBar();
		// 建立 File 選項 
		JMenu File_Menu=new JMenu("File");
		// 把 File 加到主選單上 
		menuBar.add(File_Menu);
		save = new JMenuItem("存檔");
		File_Menu.add(save);
		f.setJMenuBar(menuBar);
		save.addActionListener(this);
		
		// Step 1: 若影像超過螢幕， 則加入捲軸 
		JScrollPane scrollPane = new JScrollPane( new JLabel(new ImageIcon(image)));
		
		panel=new JPanel();
		
		slider_r=new JSlider();
		slider_r.setMaximum(200);
		slider_r.setMinimum(0);
		slider_r.setValue(100);
		slider_g=new JSlider();
		slider_g.setMaximum(200);
		slider_g.setMinimum(0);
		slider_g.setValue(100);
		slider_b=new JSlider();
		slider_b.setMaximum(200);
		slider_b.setMinimum(0);
		slider_b.setValue(100);
		
		label_r=new JLabel(slider_r.getValue()/100.0+"");
		label_g=new JLabel(slider_g.getValue()/100.0+"");
		label_b=new JLabel(slider_b.getValue()/100.0+"");
		
		dark_land=new JButton("暗角");
		light=new JButton("加亮");
		dark=new JButton("變暗");
		original=new JButton("恢復原狀");
		
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
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		f.getContentPane().add(BorderLayout.WEST, panel);
		f.getContentPane().add(scrollPane);
	
		f.pack(); 
		// Step 2: 設定點選 x 表示關閉視窗 
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		// Step 3: 加入視窗標題 
		f.setTitle(Filename+" "+image.getWidth()+ " x "+image.getHeight()); 
		// Step 4: 設定視窗顯示在螢幕中央 
		f.setLocationRelativeTo(null); 
		// Step 5: 顯示出視窗 
		f.setVisible(true);
		
		slider_r.addChangeListener(this);
		slider_g.addChangeListener(this);
		slider_b.addChangeListener(this);
		slider_r.addMouseListener(this);
		slider_g.addMouseListener(this);
		slider_b.addMouseListener(this);
		dark_land.addActionListener(this);
		light.addActionListener(this);
		dark.addActionListener(this);
		original.addActionListener(this);
		
		Or =new int[Width][Height];
		Og =new int[Width][Height];
		Ob =new int[Width][Height];
		for(int y=0;y<Height;y++){ 
			for(int x=0;x<Width;x++){ 
				int offset=y*Width+x; 
				int rgb=data[offset]; 
				
				Or[x][y]=(rgb&0x00ff0000)>>16; 
				Og[x][y]=(rgb&0x0000ff00)>>8; 
				Ob[x][y]=rgb&0x000000ff;

			} 
		}
	}
	
	// 建立 BufferedImage (int[]陣列版本) (非常快) 
	// 方法: 
	// 直接建立 BufferedImage， 把 int[] 陣列內容當作資料來源 
	// 優點: 
	// 只要改變 rgbData 的內容， 
	// 直接會反應到 BufferedImage 
	public static BufferedImage CreateBufferedImage_Direct(int[] rgbData,int Height,int Width){ 
		// Step 1: 建立 DataBuffer 物件, 把資料封裝起來 
		DataBuffer db = new DataBufferInt(rgbData, Height*Width); 
		WritableRaster raster = Raster.createPackedRaster(db, Width, Height, Width, new int[] {0xff0000, 0xff00, 0xff}, null);
		
		// Step 2: 建立 ColorModel 物件, 用來向系統解釋 
		// 我們的資料與顏色的關係 
		ColorModel cm = new DirectColorModel(24, 0xff0000, 0xff00, 0xff); 
		// Step 3: 最後得到 BufferedImage, 這個影像物件 // 將與我們的資料陣列同步改變內容
		return new BufferedImage(cm, raster, false, null);
	}
	
	public static BufferedImage LoadImage(String Filename){ 
		BufferedImage image; 
		try{ 
			image=ImageIO.read(new File(Filename)); 
		}catch(Exception e){ 
			javax.swing.JOptionPane.showMessageDialog(null, "載入圖檔錯誤: "+Filename); 
			image=null; 
		} 
		return image; 
	}
	
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource()==slider_r){
			label_r.setText(""+slider_r.getValue()/100.0);
		}
		else if (arg0.getSource()==slider_g){
			label_g.setText(""+slider_g.getValue()/100.0);
		}
		else if (arg0.getSource()==slider_b){
			label_b.setText(""+slider_b.getValue()/100.0);
		}
	}
	public void change(double cr, double cg, double cb){ 
		for(int y=0;y<Height;y++){ 
			for(int x=0;x<Width;x++){ 
				int offset=y*Width+x; 
				int rgb=data[offset]; 
				int r=(rgb&0x00ff0000)>>16; 
				int g=(rgb&0x0000ff00)>>8; 
				int b=rgb&0x000000ff; 

				r=(int)(Or[x][y]*cr)+(5*c);
				g=(int)(Og[x][y]*cg)+(5*c);
				b=(int)(Ob[x][y]*cb)+(5*c);
				if (r>255) r=255;
				if (g>255) g=255;
				if (b>255) b=255;
				if (r<0) r=0;
				if (g<0) g=0;
				if (b<0) b=0;
				rgb=(0xff000000|(r<<16)|(g<<8)|b);
				data[offset]=rgb; 
			} 
		}
		f.repaint();
	}
	public void Original(){ 
		for(int y=0;y<Height;y++){ 
			for(int x=0;x<Width;x++){ 
				int offset=y*Width+x; 
				int rgb=(0xff000000|(Or[x][y]<<16)|(Og[x][y]<<8)|Ob[x][y]);
				data[offset]=rgb; 
			} 
		} 
		f.repaint();
	}
	public void change(int d){ 
		for(int y=0;y<Height;y++){ 
			for(int x=0;x<Width;x++){ 
				int offset=y*Width+x; 
				int rgb=data[offset]; 
				int r=(rgb&0x00ff0000)>>16; 
				int g=(rgb&0x0000ff00)>>8; 
				int b=rgb&0x000000ff; 

				r+=d;
				g+=d;
				b+=d;
				if (r>255) r=255;
				if (g>255) g=255;
				if (b>255) b=255;
				if (r<0) r=0;
				if (g<0) g=0;
				if (b<0) b=0;
				rgb=(0xff000000|(r<<16)|(g<<8)|b);
				data[offset]=rgb; 
			} 
		} 
		f.repaint();
	}
	public void dark_land(){ 
		for(int y=0;y<Height;y++){ 
			for(int x=0;x<Width;x++){ 
				int offset=y*Width+x; 
				int rgb=data[offset]; 
				int r=(rgb&0x00ff0000)>>16; 
				int g=(rgb&0x0000ff00)>>8; 
				int b=rgb&0x000000ff;
				
				double min;
				if (Width>Height) min=distance(Width/2, 0)*0.8;
				else min=distance(0, Height/2);
				if (distance(x, y)>=min) {
					r=(int)(r-(distance(x, y)-min)*0.8);
					g=(int)(g-(distance(x, y)-min)*0.8);
					b=(int)(b-(distance(x, y)-min)*0.8);
				}
				
				if (r>255) r=255;
				if (g>255) g=255;
				if (b>255) b=255;
				if (r<0) r=0;
				if (g<0) g=0;
				if (b<0) b=0;
				rgb=(0xff000000|(r<<16)|(g<<8)|b);
				data[offset]=rgb;
			} 
		} 
		f.repaint();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		long start=System.currentTimeMillis(); // 計算處理時間 (起始)
		
		if (arg0.getSource()==slider_r){
			label_r.setText(""+slider_r.getValue()/100.0);
		}
		else if (arg0.getSource()==slider_g){
			label_g.setText(""+slider_g.getValue()/100.0);
		}
		else if (arg0.getSource()==slider_b){
			label_b.setText(""+slider_b.getValue()/100.0);
		}
		change(slider_r.getValue()/100.0,slider_g.getValue()/100.0,slider_b.getValue()/100.0);
		long end=System.currentTimeMillis(); // 計算處理時間 (結束)
		System.out.println("耗時"+(end-start)+"ms");
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
		if (arg0.getSource()==light){
			c+=5;
			change(slider_r.getValue()/100.0,slider_g.getValue()/100.0,slider_b.getValue()/100.0);
			label_r.setText(slider_r.getValue()/100.0+"");
			label_g.setText(slider_g.getValue()/100.0+"");
			label_b.setText(slider_b.getValue()/100.0+"");
		}
		else if (arg0.getSource()==dark){
			c-=5;
			change(slider_r.getValue()/100.0,slider_g.getValue()/100.0,slider_b.getValue()/100.0);
		}
		else if (arg0.getSource()==original){
			c=0;
			Original();
			slider_r.setValue(100);
			slider_g.setValue(100);
			slider_b.setValue(100);
			label_r.setText("1.0");
			label_g.setText("1.0");
			label_b.setText("1.0");
		}
		else if (arg0.getSource()==dark_land){
			dark_land();
		}
		else if (arg0.getSource()==save){
			Save();
		}
	}
	public double distance(int x,int y) {
		return Math.sqrt(Math.pow(center_x-x,2.0)+Math.pow(center_y-y,2.0));
	}
	public void Save(){ 
		try{ 
			FileDialog fd = new FileDialog(f);
            
            fd.setMode(FileDialog.SAVE);
            fd.setDirectory(url);
            fd.setFile(name);
            fd.setVisible(true); 
			
            if(fd!=null){
            	ImageIO.write(image,"jpg",new File(fd.getDirectory()+fd.getFile()));
			} 
		}
		catch(Exception e){ 
			javax.swing.JOptionPane.showMessageDialog(null, "存圖錯誤: "+Filename); 
			image=null; 
		} 
	}
}
















/*
 public void doGray(){ 
		int Height=image.getHeight(); 
		int Width=image.getWidth(); 
		for(int y=0;y<Height;y++){ 
			for(int x=0;x<Width;x++){ 
				int rgb=image.getRGB(x,y); 
				int r=(rgb&0x00ff0000)>>16; // 取得紅色的資料 
				int g=(rgb&0x0000ff00)>>8; // 取得綠色資料 
				int b=rgb&0x000000ff; // 取得藍色資料 
				int gray=(r+g+b)/3; // 計算灰階值
				
				r*=1.3;
				g*=1.3;
				b*=1.3;
				if (r>255) r=255;
				if (g>255) g=255;
				if (b>255) b=255;
				if (r<0) r=0;
				if (g<0) g=0;
				if (b<0) b=0;
				
				rgb=(0xff000000|(r<<16)|(g<<8)|b); 
				//rgb=(0xff000000|(gray<<16)|(gray<<8)|gray);
				image.setRGB(x,y,rgb);
			} 
		} 
	}
	public void doGray_Speed(){ 
		for(int y=0;y<Height;y++){ 
			for(int x=0;x<Width;x++){ 
				int offset=y*Width+x; 
				int rgb=data[offset]; 
				int r=(rgb&0x00ff0000)>>16; 
				int g=(rgb&0x0000ff00)>>8; 
				int b=rgb&0x000000ff; 
				
				r*=1.3;
				g*=1.2;
				b*=0.8;
				if (r>255) r=255;
				if (g>255) g=255;
				if (b>255) b=255;
				if (r<0) r=0;
				if (g<0) g=0;
				if (b<0) b=0;
				//int gray=(r+g+b)/3; 
				//rgb=(0xff000000|(gray<<16)|(gray<<8)|gray); 
				rgb=(0xff000000|(r<<16)|(g<<8)|b);
				data[offset]=rgb; 
			} 
		} 
	}
*/