
class image_test{
	public static void main(String ar[]){
		
		if (System.getProperty("os.name").indexOf("Mac") != -1) 
			System.setProperty("apple.laf.useScreenMenuBar", "true");
				
		long start=System.currentTimeMillis(); // �p��B�z�ɶ� (�_�l)
		
//		imageComponent image=new imageComponent();
//		image.Show();
		
		Image_edit image=new Image_edit();//.showWindow();
		image.showWindow();
		image.setWindow();
		
		long end=System.currentTimeMillis(); // �p��B�z�ɶ� (����)
		System.out.println("�Ӯ�"+(end-start)+"ms");
	}
}