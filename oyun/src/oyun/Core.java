package oyun;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Core extends JPanel {

    public static int WIDTH = 500;
    public static int HEIGHT = 400;
    public int DELAY = 10;

    private double aci = 0;
    private int mermiSayisi = 15;
    private int puan = 150;
    private int can = 3;
    private int time = 0;
    private int maxHiz = 20;
    private boolean gameOver = false;
    private boolean canAzaldimi = false;

    double cannonDiameter = 50;
    double cannonRectWidth = cannonDiameter/4;
    double CannonRectHeight = cannonDiameter/2 + 10;
    double cannonCircleX = WIDTH/2 - cannonDiameter/2;
    double cannonCircleY = HEIGHT - cannonDiameter/2;
    double cannonRectX = cannonCircleX + cannonDiameter/2 - cannonRectWidth/2;
    double cannonRectY = HEIGHT - cannonDiameter;

    List<Top> toplar = new ArrayList<Top>();
    List<Mermi> mermiler = new ArrayList<Mermi>();

    private AffineTransform transform;

    Dimension d = new Dimension(WIDTH, HEIGHT);

    @Override
    public Dimension getPreferredSize() {
        return d;
    }

    public Core(){
        setBackground(new Color(32,33,36));
        setFocusable(true);
        requestFocusInWindow();

        for(int i = 0; i <10; i++) {
            toplar.add(new Top());
        }

        anaTimer.start();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP:
                        if(mermiSayisi > 0) {
                            mermiler.add(new Mermi(aci));
                            mermiSayisi--;
                            puan -= 10;
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if(aci < 85 && !gameOver)
                        aci += 5;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if(aci > -85 && !gameOver)
                        aci -= 5;
                        break;
                    case KeyEvent.VK_SPACE:
                        if(gameOver && can > 0){
                            for(int i = 0; i < 10; i++) {
                                toplar.add(new Top());
                            }
                            mermiSayisi = 15;
                            puan = 150;
                            gameOver = false;
                            time = 0;
                            aci = 0;
                            canAzaldimi = false;
                            anaTimer.start();
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        transform = new AffineTransform();

        anaTimer.start();
        kontrolTimer.start();
    }

    Timer anaTimer = new Timer(DELAY, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            time++;

            for(int i = 0; i < toplar.size(); i++){
                Top top = toplar.get(i);
                Rectangle2D rTop = new Rectangle2D.Double(top.x, top.y, top.diameter, top.diameter);
                for(int j = 0; j < mermiler.size(); j++){
                    Mermi mermi = mermiler.get(j);
                    Rectangle2D rMermi = new Rectangle2D.Double(mermi.x, mermi.y, mermi.diameter, mermi.diameter);
                    if(rTop.intersects(rMermi) && top.startTime < time){
                        toplar.remove(i);
                        mermiler.remove(j);
                        puan += 10;
                    }
                }
            }

            for (Top toplar: toplar){
                if(toplar.y + toplar.diameter >= HEIGHT){
                    toplar.asagi = false;
                    if(toplar.hiz < maxHiz) toplar.hiz = toplar.hiz + 2;
                }
                if(toplar.y + 1 <= 0){
                    toplar.asagi = true;
                    if(toplar.hiz < maxHiz) toplar.hiz = toplar.hiz + 2;
                }
                if(toplar.startTime <= time){
                    if(toplar.asagi){
                        toplar.y += toplar.hiz;
                    }else{
                        toplar.y -= toplar.hiz;
                    }
                }
            }

            for (Mermi mermiler: mermiler){
                mermiler.x -= mermiler.Vx;
                mermiler.y -= mermiler.Vy;
            }

            repaint();
        }
    });

    Timer kontrolTimer = new Timer(DELAY, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(mermiSayisi == 0 && mermiler.isEmpty()){
                anaTimer.stop();
                gameOver = true;
                for(int i = 0; i < toplar.size(); i++) {
                    toplar.remove(i);
                }
                for(int i = 0; i < mermiler.size(); i++) {
                    mermiler.remove(i);
                }
                if(!canAzaldimi){
                    can--;
                    canAzaldimi = true;
                }
            }

            if(toplar.isEmpty()){
                anaTimer.stop();
                gameOver = true;
                if(!canAzaldimi){
                    can--;
                    canAzaldimi = true;
                }
            }

            for(int i = 0; i < mermiler.size(); i++){
                Mermi mermi = mermiler.get(i);
                if(mermi.y < -10) mermiler.remove(i);
            }

            repaint();
        }
    });


    private void doDrawing(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.drawString("Puan: ",430,340);
        g2d.drawString(String.valueOf(puan),470,340);
        g2d.drawString("Can: ",430,355);
        g2d.drawString(String.valueOf(can),470,355);
        g2d.drawString("Kalan Mermi: ",410,370);
        g2d.drawString(String.valueOf(mermiSayisi),480,370);

        if ((mermiSayisi == 0 && mermiler.isEmpty()) || toplar.isEmpty()) {

            g2d.setColor(Color.WHITE);
            g2d.drawString("Yeniden oynamak için", 300, 300);
            g2d.drawString("SPACE tuşuna basın", 300, 330);
        }


        for (Mermi mermiler: mermiler){
            g2d.setColor(mermiler.color);
            g2d.fill(new Ellipse2D.Double(mermiler.x, mermiler.y, mermiler.diameter, mermiler.diameter));
        }

        for(Top toplar: toplar){
            g2d.setColor(toplar.color);
            g2d.fill(new Ellipse2D.Double(toplar.x, toplar.y, toplar.diameter, toplar.diameter));
        }

        g2d.setColor(new Color(232, 234, 237));
        g2d.fill(new Ellipse2D.Double(cannonCircleX, cannonCircleY, cannonDiameter, cannonDiameter));
        transform.setToTranslation(64, 100);
        transform.rotate(Math.toRadians(-aci), WIDTH/2, HEIGHT);
        g2d.setTransform(transform);
        g2d.fill(new Rectangle2D.Double(cannonRectX, cannonRectY, cannonRectWidth, CannonRectHeight));

        g2d.dispose();
        Toolkit.getDefaultToolkit().sync();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}
