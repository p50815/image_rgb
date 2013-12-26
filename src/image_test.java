
class image_test{
	public static void main(String ar[]){
		
		if (System.getProperty("os.name").indexOf("Mac") != -1) 
			System.setProperty("apple.laf.useScreenMenuBar", "true");
				
		long start=System.currentTimeMillis(); // 計算處理時間 (起始)
		
//		imageComponent image=new imageComponent();
//		image.Show();
		
		Image_edit image=new Image_edit();//.showWindow();
		image.showWindow();
		image.setWindow();
		
		long end=System.currentTimeMillis(); // 計算處理時間 (結束)
		System.out.println("耗時"+(end-start)+"ms");
	}
}