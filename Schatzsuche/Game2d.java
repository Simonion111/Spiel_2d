import name.panitz.game2d.Game;
import name.panitz.game2d.GameObj;
import name.panitz.game2d.ImageObject;
import name.panitz.game2d.TextObject;
import name.panitz.game2d.Vertex;

import java.awt.event.*;
import java.util.*;

import static java.awt.event.KeyEvent.*;


public record Game2d( GameObj player, List<List<? extends GameObj>> goss
        , int width, int height,int[] Leben, List<GameObj> gegner,List<GameObj> Anzeigen, List<GameObj> hintergrund,List<GameObj> Geschönke,List<GameObj> suivit) implements Game{
    public Game2d (){
        this(new ImageObject("vvv.png"),new ArrayList<>(),1150,580,new int[] {11,0,3},new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
}
//----------------------------------------------------------------------------------------------------------------------
    @Override
    public void init(){
        hintergrund().clear();
        goss().clear();
        gegner().clear();
        Anzeigen().clear();
        Geschönke().clear();

        goss().add(hintergrund());
        goss().add(Geschönke());
        goss().add(gegner());
        goss().add(Anzeigen());


        hintergrund().add(new ImageObject("wwww1.jpg"));
        hintergrund().add(new ImageObject(new Vertex(-60, -50), new Vertex(0, 0), "Tür0.png"));

        hintergrund().add(new ImageObject(new Vertex(1010, 430), new Vertex(0, 0), "Tür3.png"));

        Geschönke().add(new ImageObject("monalisa.jpg"));

        gegner().add(new ImageObject(new Vertex(-100, 75), new Vertex(0.3, 0), "2.png"));
        gegner().add(new ImageObject(new Vertex(-100, 150), new Vertex(-0.5, 0), "3.png"));
        gegner().add(new ImageObject(new Vertex(-100, 250), new Vertex(0.8, 0), "2.png"));


        Anzeigen().add(new TextObject(new Vertex(950,40),"Leben                  :"+Leben() [0]));
        Anzeigen().add(new TextObject(new Vertex(950,60),"Gestohlenen Bilder:" +Leben() [1]));
        Anzeigen().add(new TextObject(new Vertex(950,80),"Anzahl der Polizei  :" +Leben() [2]));

        Geschönkk();

    }


//----------------------------------------------------------------------------------------------------------------------
    private GameObj createNewGegner() {
        double initialX = new Random().nextDouble(width());
        double initialY = new Random().nextDouble(height());
        double speedX = 0.5;
        double speedY = 0.0;
        return new ImageObject(new Vertex(initialX, initialY), new Vertex(speedX, speedY), "1.png");
    }




//----------------------------------------------------------------------------------------------------------------------
    private void Geschönkk() {
        Geschönke().get(0).pos().x = new Random().nextDouble(width-Geschönke().get(0).width());
        Geschönke().get(0).pos().y = new Random().nextDouble(height-Geschönke().get(0).height());
    }

//----------------------------------------------------------------------------------------------------------------------
    @Override
    public void doChecks() {
            for (var g:gegner()){
                if (g.velocity().x>0 && g.pos().x >width()) g.pos().x=-g.width()-50;
                if (g.velocity().x<0 && g.pos().x <-g.width()) g.pos().x=width()+50;
                if (g.touches(player())) {
                    Leben()[0]--;
                    g.pos().x = new Random().nextDouble(width() - g.width());
                    g.pos().y = new Random().nextDouble(height() - g.height());
                    Anzeigen().set(0, new TextObject(new Vertex(950, 40), "Leben                  :" + Leben()[0]));
                }
            }

        for (var g : gegner().subList(3, gegner().size())) {
            double deltaX = player().pos().x - g.pos().x;
            double deltaY = player().pos().y - g.pos().y;
            double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double speed = 1;
            double directionX = (deltaX / length) * speed;
            double directionY = (deltaY / length) * speed;

            g.velocity().x = directionX;
            g.velocity().y = directionY;


            if (g.touches(player())) {
                Leben()[0]--;
                g.pos().x = new Random().nextDouble(width() - g.width());
                g.pos().y = new Random().nextDouble(height() - g.height());
            }
        }

        for (var G : Geschönke()) {
            if (player().touches(G)) {


                if (Leben()[2]<6  ){
                    hintergrund().add(new ImageObject(new Vertex(120, 15), new Vertex(0, 0), "alarm.png"));
                    hintergrund().add(new ImageObject(new Vertex(705, 15), new Vertex(0, 0), "alarm.png"));
                    Leben()[1]++;
                    Anzeigen().set(1, new TextObject(new Vertex(950, 60), "Gestohlenen Bilder::" + Leben()[1]));
                    gegner().add(createNewGegner());
                    Leben()[2]++;
                    Anzeigen().set(2, new TextObject(new Vertex(950, 80), "polizeiAnzahl  :" + Leben()[2]));
                Geschönkk();
                } if (Leben()[2]>=6  ){
                    Leben()[1]++;
                    Anzeigen().set(1, new TextObject(new Vertex(950, 60), "Gestohlenen Bilder::" + Leben()[1]));
                    Geschönkk();
                }
            }
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    @Override
    public void keyPressedReaction(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case VK_RIGHT -> player().velocity().x += 0.1;
            case VK_LEFT -> player().velocity().x -= 0.1;
            case VK_DOWN -> player().velocity().y += 0.1;
            case VK_UP -> player().velocity().y -= 0.1;
            case VK_SPACE -> {
                player().velocity().y *= 0.9;
                player().velocity().x *= 0.9;
            }
        }
        double playerWidth = player().width();
        double playerHeight = player().height();

        double screenWidth = 1100;
        double screenHeight = 700;

        if (player().pos().x < -playerWidth) player().pos().x = screenWidth;
        if (player().pos().x > screenWidth) player().pos().x = -playerWidth;
        if (player().pos().y < -playerHeight) player().pos().y = screenHeight;
        if (player().pos().y > screenHeight) player().pos().y = -playerHeight;
}
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean won() {
    return Leben()[1]>=7 ;
    }
    @Override
    public boolean lost() {
        return Leben()[0]<=0;
    }
     public static void main(String[] args) {
        new Game2d().play();
    }
}