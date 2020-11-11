
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class GUI extends javax.swing.JFrame {

    /**
     * Creates new form GUI
     */
    private static int WIDTH = 800, HEIGHT = 400;
    private int widthRect = 60;
    private int space = 150;
    int x = WIDTH;
    Graphics g = null;
    Random r;
    Thread t, t2;
    ArrayList<JButton> arr;
    JButton bird;
    int c1 = 0;
    int up = 0;
    int max = 0;
    int v = 1;
    int point = 0;
    int birdy;
    boolean isLose = false;
    boolean isRunning = false;
    boolean isPause = false;

    public GUI() {
        initComponents();
        
        v = 3;
        r = new Random();
        arr = new ArrayList<>();
        pnmain.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i < arr.size(); i = i + 2) {
                        if (arr.get(i).getX() + widthRect < 200 && arr.get(i).getX() + widthRect >= 197) {
                            point++;
                            lbpoint.setText(point + "");
                        }
                        arr.get(i).setLocation(arr.get(i).getX() - v, arr.get(i).getY());
                        arr.get(i + 1).setLocation(arr.get(i + 1).getX() - v, arr.get(i + 1).getY());
                        if (isLose==false&&arr.get(i).getBounds().intersects(bird.getBounds()) || arr.get(i + 1).getBounds().intersects(bird.getBounds())) {
                            isLose=true;                   
                            int a = JOptionPane.showConfirmDialog(rootPane, "game over! continue?");
                            if (a == JOptionPane.NO_OPTION || a == JOptionPane.CANCEL_OPTION) {
                                System.exit(0);
                            } else {
                                clear();                                                     
                                isLose=false;
                            }
                        }
                        // pnmain.add(arr.get(i));
                        // pnmain.add(arr.get(i + 1));
                    }
                    if (c1 == 150) {
                        c1 = 0;
                        AddRect();
                    }
                    c1++;
                    try {
                        Thread.sleep(10);
                        //pnmain.removeAll();
                        // pnmain.repaint();
                        if (arr.get(0).getX() + widthRect <= 0) {
                            pnmain.remove(arr.get(1));
                            pnmain.remove(arr.get(0));
                            arr.remove(1);
                            arr.remove(0);

                        }
                    } catch (Exception e) {

                    }

                }
            }
        });
        t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (bird.getY() >= HEIGHT&&isLose==false) {
                        isLose=true;
                        int a = JOptionPane.showConfirmDialog(rootPane, "game over! continue?");
                        if (a == JOptionPane.NO_OPTION || a == JOptionPane.CANCEL_OPTION) {
                            System.exit(0);
                        } else {
                                clear();                                                
                                isLose=false;
                                
                        }
                    }
                    if (bird.getY() <= max) {
                        up = 0;
                    }
                    if (up == 0) {
                        bird.setLocation(bird.getX(), bird.getY() + 1);
                    } else {
                        bird.setLocation(bird.getX(), bird.getY() - 10);
                    }
                    try {
                        Thread.sleep(6);
                    } catch (Exception e) {
                    }
                }
            }
        });
        this.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                System.out.println("isrunning"+isRunning);
                System.out.println("ispause"+isPause);           
                    if (isRunning == false) {
                        t.start();
                        t2.start();
                        isRunning = true;
                    } else {
                        if (isPause == true) {
                            t.resume();
                            t2.resume();
                            isPause = false;
                        } else {
                            if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
                                max = bird.getY() - 70;                  
                                up = 1;
                            }
                        }
                    }
                

            }
        });
        
    }

    void Start() {
        int option = JOptionPane.showConfirmDialog(rootPane, "do you want to continue?");
        if (option == JOptionPane.NO_OPTION || option == JOptionPane.CANCEL_OPTION) {
            clear();

        } else {
            LoadData();

        }
    }

    void AddRect() {
        int y1 = r.nextInt(HEIGHT - space - 50);
        int y2 = y1 + space;
        JButton bt1 = new JButton();
        bt1.setBounds(WIDTH - widthRect, -10000, widthRect, y1);
        JButton bt2 = new JButton();
        bt2.setBounds(WIDTH - widthRect, y2, widthRect, HEIGHT - y2);
        arr.add(bt1);
        arr.add(bt2);
        bt1.setFocusable(false);
        bt2.setFocusable(false);
        pnmain.add(bt1);
        pnmain.add(bt2);
    }

    void LoadData() {
        pnmain.removeAll();
        pnmain.repaint();
        arr.clear();
        try {
            File f = new File("data.txt");
            if (f.isFile()) {
                if (f.length() != 0) {
                    FileReader fr = new FileReader(f);
                    BufferedReader bf = new BufferedReader(fr);
                    String line = "";
                    while ((line = bf.readLine()) != null) {
                        // System.out.println(line);
                        String[] data = line.split("\\|");
                        //System.out.println(data[0]);
                        c1 = Integer.parseInt(data[0]);
                        up = Integer.parseInt(data[1]);
                        max = Integer.parseInt(data[2]);
                        v = Integer.parseInt(data[3]);
                        point = Integer.parseInt(data[4]);
                        birdy = Integer.parseInt(data[5]);
                        System.out.println(birdy);
                        bird = new JButton();
                        bird.setBounds(200, birdy, 40, 30);
                        Icon icon = new ImageIcon("bird1.png");
                        bird.setIcon(icon);
                        bird.setBackground(pnmain.getBackground());
                        pnmain.add(bird);
                        lbpoint.setText(point + "");
                    }
                }
            }
            File f2 = new File("button.txt");
            if (f2.isFile()) {
                if (f2.length() != 0) {
                    FileReader fr = new FileReader(f2);
                    BufferedReader bf = new BufferedReader(fr);
                    String line = "";
                    while ((line = bf.readLine()) != null) {
                        System.out.println(line);
                        String[] data = line.split("\\|");
                        JButton bt = new JButton();
                        bt.setBounds(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                                Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                        arr.add(bt);
                        pnmain.add(bt);
                    }
                } else {
                    clear();
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void clear() {
        pnmain.removeAll();
        pnmain.repaint();
        arr.clear();
        AddRect();
        // isRunning = false;
       
        c1 = 0;
        up = 0;
        max = 0;
        v = 3;
        point = 0;
        birdy = HEIGHT / 2;
        lbpoint.setText("0");
        bird = new JButton();
        bird.setBounds(200, birdy, 40, 30);
        
        ImageIcon icon = new ImageIcon("bird1.png");
         bird.setBackground(pnmain.getBackground());
        bird.setIcon(icon);
        pnmain.add(bird);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnmain = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btpause = new javax.swing.JButton();
        btsave = new javax.swing.JButton();
        btexit = new javax.swing.JButton();
        lbpoint = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnmain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnmainMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnmainLayout = new javax.swing.GroupLayout(pnmain);
        pnmain.setLayout(pnmainLayout);
        pnmainLayout.setHorizontalGroup(
            pnmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 487, Short.MAX_VALUE)
        );
        pnmainLayout.setVerticalGroup(
            pnmainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );

        jLabel3.setText("Point:");

        btpause.setText("Pause");
        btpause.setFocusable(false);
        btpause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btpauseActionPerformed(evt);
            }
        });

        btsave.setText("Save");
        btsave.setFocusable(false);
        btsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btsaveActionPerformed(evt);
            }
        });

        btexit.setText("Exit");
        btexit.setFocusable(false);
        btexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btexitActionPerformed(evt);
            }
        });

        lbpoint.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(btpause)
                .addGap(36, 36, 36)
                .addComponent(btsave)
                .addGap(36, 36, 36)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(lbpoint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addComponent(btexit)
                .addGap(52, 52, 52))
            .addComponent(pnmain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnmain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(btpause)
                    .addComponent(btsave)
                    .addComponent(btexit)
                    .addComponent(lbpoint))
                .addGap(0, 31, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btpauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btpauseActionPerformed
        if (isPause == false) {
            t.suspend();
            t2.suspend();
            btpause.setText("resume");
            isPause = true;
        } else {
            t.resume();
            t2.resume();
            btpause.setText("pause");
            isPause = false;
        }

    }//GEN-LAST:event_btpauseActionPerformed

    private void pnmainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnmainMouseClicked
        
    }//GEN-LAST:event_pnmainMouseClicked

    private void btsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsaveActionPerformed
        try {
            FileWriter myWriter = new FileWriter("button.txt");
            FileWriter myWriter1 = new FileWriter("data.txt");
            String content = "";
            String content1 = "";
            for (int i = 0; i < arr.size(); i++) {
                content += arr.get(i).getX() + "|" + arr.get(i).getY() + "|"
                        + arr.get(i).getWidth() + "|" + arr.get(i).getHeight() + "\n";
            }
            content1 += c1 + "|" + up + "|" + max + "|" + v + "|" + point + "|" + bird.getY();
            myWriter1.write(content1);
            myWriter1.close();
            myWriter.write(content);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
        }
        t.suspend();
        t2.suspend();
        isPause = true;
    }//GEN-LAST:event_btsaveActionPerformed

    private void btexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btexitActionPerformed
       System.exit(0);
    }//GEN-LAST:event_btexitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               
                GUI gu = new GUI();
                gu.setVisible(true);
                gu.setSize(WIDTH, HEIGHT + 200);
                gu.setResizable(false);
                gu.Start();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btexit;
    private javax.swing.JButton btpause;
    private javax.swing.JButton btsave;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lbpoint;
    private javax.swing.JPanel pnmain;
    // End of variables declaration//GEN-END:variables
}
