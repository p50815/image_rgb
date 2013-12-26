import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;

class test
    extends JComponent {
  Image image;
  int Height, Width; // 這個元件的大小
  int[] pixels; // 放圖形內容的地方
  private JButton m_btScale = null; // 放大縮小按鈕

  // 由檔案載入圖檔
  // (為了方便起見, 我們只處理 jpg 檔, 如果你對任意檔讀取有興趣,
  // 請到 http://mqjing.twbbs.org.tw/~ching/Course/AdvancedC++Course/__Page/Advanced_PChome/page.html)
  public test(String filename) {
    try {
      Image image = Toolkit.getDefaultToolkit().getImage(filename);
      init(image);

      m_btScale = new JButton("Scale");
      m_btScale.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          // javax.swing.JOptionPane.showMessageDialog(null,"test");
          test.this.Scale(5.0, 5.0);

          System.out.println("aa=" + test.this.getPreferredSize());
        }
      }
      );
    }
    catch (Exception e) {
      System.out.println(e);
    }
  }

  private void init(Image image) throws Exception {
    this.image = image;
    // 等待 Image 載入
    MediaTracker mt = new MediaTracker(this);
    mt.addImage(image, 0);
    try {
      mt.waitForAll();
    }
    catch (InterruptedException e) {
      throw new Exception("載入圖形錯誤");
    }

    Height = image.getHeight(null);
    Width = image.getWidth(null);

    setSize(Width, Height); // 設定 ImageComponent 的外觀物件大小

    // [提供 Layout Manager 的資訊]
    setPreferredSize(new Dimension(Width, Height)); // 提供 Layout Manager, 這個元件的預設大小
    setMinimumSize(new Dimension(Width, Height)); //  最小不能低於 MinimumSize

    // 取得image 的圖
    getPixels();

  }

  private void getPixels() {
    // 取得image 的圖
    pixels = new int[Height * Width];
    PixelGrabber pg = new PixelGrabber(image, 0, 0, Width, Height, pixels, 0,
                                       Width);
    try {
      pg.grabPixels();
    }
    catch (InterruptedException e) {}
  }

  // 畫出這個元件的表面
  public void paint(Graphics g) {
    g.drawImage(image, 0, 0, this);
  }

  private JFrame m_ShowFrame = null;

  public void Show(String Title) {
    if (m_ShowFrame == null) {
      m_ShowFrame = new JFrame(Title); // 建立視窗顯示圖形
      m_ShowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 使用者按關閉, 則立即離開程式
      // 設定元件由上至下擺放
      m_ShowFrame.setLayout(new BoxLayout(m_ShowFrame.getContentPane(),
                                          BoxLayout.Y_AXIS));
    }
    m_ShowFrame.getContentPane().add(this); // 加入影像元件
    m_ShowFrame.getContentPane().add(m_btScale); // 加入 scale 按鈕
    m_ShowFrame.pack(); // 排列所有的元件
    m_ShowFrame.setVisible(true); // 顯示視窗
  }

  public void Scale(double x, double y) {
    test.this.show();
    this.Width = (int) (x * Width);
    this.Height = (int) (y * Height);

    // 如果你發現,offscreen = null
    // 則先檢查這個ImageComponent 可能尚未被 display
    Image offscreen = createImage(Width, Height);
    Graphics2D gg = (Graphics2D) offscreen.getGraphics();

    // 設定平滑
    gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
    gg.scale(x, y);

    //利用graphics2D把圖畫上去
    gg.drawImage(image, 0, 0, this);
    image = offscreen;
    getPixels();

    // 重新設定元件的大小
    this.setSize(this.Width, this.Height); // 設定元件新的大小
    // [提供 Layout Manager 的資訊]
    setPreferredSize(new Dimension(Width, Height)); // 提供 Layout Manager, 這個元件的預設大小
    setMinimumSize(new Dimension(Width, Height)); //  最小不能低於 MinimumSize

    //  立即根據新元件大小改變, 重新計算所有元件的大小
    m_ShowFrame.pack();
  }
}
