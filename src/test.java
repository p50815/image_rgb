import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;

class test
    extends JComponent {
  Image image;
  int Height, Width; // �o�Ӥ��󪺤j�p
  int[] pixels; // ��ϧΤ��e���a��
  private JButton m_btScale = null; // ��j�Y�p���s

  // ���ɮ׸��J����
  // (���F��K�_��, �ڭ̥u�B�z jpg ��, �p�G�A����N��Ū��������,
  // �Ш� http://mqjing.twbbs.org.tw/~ching/Course/AdvancedC++Course/__Page/Advanced_PChome/page.html)
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
    // ���� Image ���J
    MediaTracker mt = new MediaTracker(this);
    mt.addImage(image, 0);
    try {
      mt.waitForAll();
    }
    catch (InterruptedException e) {
      throw new Exception("���J�ϧο��~");
    }

    Height = image.getHeight(null);
    Width = image.getWidth(null);

    setSize(Width, Height); // �]�w ImageComponent ���~�[����j�p

    // [���� Layout Manager ����T]
    setPreferredSize(new Dimension(Width, Height)); // ���� Layout Manager, �o�Ӥ��󪺹w�]�j�p
    setMinimumSize(new Dimension(Width, Height)); //  �̤p����C�� MinimumSize

    // ���oimage ����
    getPixels();

  }

  private void getPixels() {
    // ���oimage ����
    pixels = new int[Height * Width];
    PixelGrabber pg = new PixelGrabber(image, 0, 0, Width, Height, pixels, 0,
                                       Width);
    try {
      pg.grabPixels();
    }
    catch (InterruptedException e) {}
  }

  // �e�X�o�Ӥ��󪺪�
  public void paint(Graphics g) {
    g.drawImage(image, 0, 0, this);
  }

  private JFrame m_ShowFrame = null;

  public void Show(String Title) {
    if (m_ShowFrame == null) {
      m_ShowFrame = new JFrame(Title); // �إߵ�����ܹϧ�
      m_ShowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �ϥΪ̫�����, �h�ߧY���}�{��
      // �]�w����ѤW�ܤU�\��
      m_ShowFrame.setLayout(new BoxLayout(m_ShowFrame.getContentPane(),
                                          BoxLayout.Y_AXIS));
    }
    m_ShowFrame.getContentPane().add(this); // �[�J�v������
    m_ShowFrame.getContentPane().add(m_btScale); // �[�J scale ���s
    m_ShowFrame.pack(); // �ƦC�Ҧ�������
    m_ShowFrame.setVisible(true); // ��ܵ���
  }

  public void Scale(double x, double y) {
    test.this.show();
    this.Width = (int) (x * Width);
    this.Height = (int) (y * Height);

    // �p�G�A�o�{,offscreen = null
    // �h���ˬd�o��ImageComponent �i��|���Q display
    Image offscreen = createImage(Width, Height);
    Graphics2D gg = (Graphics2D) offscreen.getGraphics();

    // �]�w����
    gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
    gg.scale(x, y);

    //�Q��graphics2D��ϵe�W�h
    gg.drawImage(image, 0, 0, this);
    image = offscreen;
    getPixels();

    // ���s�]�w���󪺤j�p
    this.setSize(this.Width, this.Height); // �]�w����s���j�p
    // [���� Layout Manager ����T]
    setPreferredSize(new Dimension(Width, Height)); // ���� Layout Manager, �o�Ӥ��󪺹w�]�j�p
    setMinimumSize(new Dimension(Width, Height)); //  �̤p����C�� MinimumSize

    //  �ߧY�ھڷs����j�p����, ���s�p��Ҧ����󪺤j�p
    m_ShowFrame.pack();
  }
}
