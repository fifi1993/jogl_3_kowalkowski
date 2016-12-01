package org.yourorghere;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


/**
 * Kowalkowski.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class Kowalkowski implements GLEventListener {
//statyczne pola okre?laj?ce rotacj? wok?? osi X i Y
 private static float xrot = 0.0f, yrot = 0.0f;
 static float ambientLight[] = { 0.3f, 0.3f, 0.3f, 1.0f };//swiat?o otaczaj?ce
 static float diffuseLight[] = { 0.7f, 0.7f, 0.7f, 1.0f };//?wiat?o rozproszone
 static float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f}; //?wiat?o odbite
 static float lightPos[] = { 0.0f, 150.0f, 150.0f, 1.0f };//pozycja ?wiat?a
 static BufferedImage image1 = null,image2 = null;
 static Texture t1 = null, t2 = null;


    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new Kowalkowski());
        frame.add(canvas);
        frame.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        
        //Obs?uga klawiszy strza?ek
 frame.addKeyListener(new KeyListener()
 {
 public void keyPressed(KeyEvent e)
 {
 if(e.getKeyCode() == KeyEvent.VK_UP)
 xrot -= 1.0f;
 if(e.getKeyCode() == KeyEvent.VK_DOWN)
 xrot +=1.0f;
 if(e.getKeyCode() == KeyEvent.VK_RIGHT)
 yrot += 1.0f;
 if(e.getKeyCode() == KeyEvent.VK_LEFT)
 yrot -=1.0f;
 
 if(e.getKeyChar() == 'q')
     ambientLight = new float[] {ambientLight[0]-0.1f, ambientLight[0]-0.1f,ambientLight[0]-0.1f,1.0f};
 if(e.getKeyChar() == 'w')
     ambientLight = new float[] {ambientLight[0]+0.1f, ambientLight[0]+0.1f,ambientLight[0]+0.1f,1.0f};
 if(e.getKeyChar() == 'e')
     diffuseLight = new float[] {diffuseLight[0]-0.1f, diffuseLight[0]-0.1f,diffuseLight[0]-0.1f,1.0f};
 if(e.getKeyChar() == 'r')
     diffuseLight = new float[] {diffuseLight[0]+0.1f, diffuseLight[0]+0.1f,diffuseLight[0]+0.1f,1.0f};
 if(e.getKeyChar() == 't')
     specular = new float[] {specular[0]-0.1f, specular[0]-0.1f,specular[0]-0.1f,1.0f};
 if(e.getKeyChar() == 'y')
     specular = new float[] {specular[0]+0.1f, specular[0]+0.1f,specular[0]+0.1f,1.0f};
 if(e.getKeyChar() == 'u')
     lightPos[3]=0;
 if(e.getKeyChar() == 'i')
     lightPos[3]=1;
 }
 
 public void keyReleased(KeyEvent e){}
 public void keyTyped(KeyEvent e){}
 });
        
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        
        //wy??czenie wewn?trzych stron prymityw?w
        gl.glEnable(GL.GL_CULL_FACE);
        
        //warto?ci sk?adowe o?wietlenia i koordynaty ?r?d?a ?wiat?a
        
        //(czwarty parametr okre?la odleg?o?? ?r?d?a:
        //0.0f-niesko?czona; 1.0f-okre?lona przez pozosta?e parametry)
        gl.glEnable(GL.GL_LIGHTING); //uaktywnienie o?wietlenia
        //ustawienie parametr?w ?r?d?a ?wiat?a nr. 0
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat?o otaczaj?ce
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //?wiat?o rozproszone
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //?wiat?o odbite
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0); //pozycja ?wiat?a
        gl.glEnable(GL.GL_LIGHT0); //uaktywnienie ?r?d?a ?wiat?a nr. 0
        gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie ?ledzenia kolor?w
        //kolory b?d? ustalane za pomoc? glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasno?ci i odblaskowo?ci obiekt?w
        float specref[] = { 1.0f, 1.0f, 1.0f, 1.0f }; //parametry odblaskowo?ci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR,specref,0);
        
        gl.glMateriali(GL.GL_FRONT,GL.GL_SHININESS,128);

        gl.glEnable(GL.GL_DEPTH_TEST);
        // Setup the drawing area and shading mode
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        
        
        try
{
image1 = ImageIO.read(getClass().getResourceAsStream("/pokemon.jpg"));
image2 = ImageIO.read(getClass().getResourceAsStream("/android.jpg"));
}
catch(Exception exc)
{
JOptionPane.showMessageDialog(null, exc.toString());
return;
}

t1 = TextureIO.newTexture(image1, false);
t2 = TextureIO.newTexture(image2, false);
gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
 GL.GL_DECAL | GL.GL_MODULATE);
gl.glEnable(GL.GL_TEXTURE_2D);
gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);



    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!
        
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
          gl.glLoadIdentity();
          //kolo(0.0f, 0.0f, 0.5f, gl);
          gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuni?cie o 6 jednostek
            gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wok?? osi X
            gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wok?? osi Y
            gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat?o otaczaj?ce
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //?wiat?o rozproszone
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //?wiat?o odbite
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0); //pozycja ?wiat?a
            gl.glBindTexture(GL.GL_TEXTURE_2D, t2.getTextureObject());
            gl.glBegin(GL.GL_QUADS);
//?ciana przednia czerwona
gl.glColor3f(1.0f,0.0f,0.0f);
gl.glNormal3f(0.0f,0.0f,1.0f);
gl.glTexCoord2f(2.0f, 2.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glTexCoord2f(0.0f, 2.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glTexCoord2f(0.0f, 0.0f);
gl.glVertex3f(1.0f,1.0f,1.0f);
gl.glTexCoord2f(2.0f, 0.0f);
gl.glVertex3f(-1.0f,1.0f,1.0f);
//sciana tylna zielona
gl.glColor3f(0.0f,1.0f,0.0f);
gl.glNormal3f(0.0f,0.0f,-1.0f);
gl.glTexCoord2f(1.0f, 0.0f);
gl.glVertex3f(-1.0f,1.0f,-1.0f);
gl.glTexCoord2f(0.0f, 0.0f);
gl.glVertex3f(1.0f,1.0f,-1.0f);
gl.glTexCoord2f(0.0f, 1.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
gl.glTexCoord2f(1.0f, 1.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
//?ciana lewa niebieska
gl.glColor3f(0.0f,0.0f,1.0f);
gl.glNormal3f(-1.0f,0.0f,0.0f);
gl.glTexCoord2f(0.0f, 0.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glTexCoord2f(0.0f, 1.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glTexCoord2f(1.0f, 1.0f);
gl.glVertex3f(-1.0f,1.0f,1.0f);
gl.glTexCoord2f(1.0f, 0.0f);
gl.glVertex3f(-1.0f,1.0f,-1.0f);
gl.glEnd();
gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());
gl.glBegin(GL.GL_QUADS);
//?ciana prawa
gl.glColor3f(1.0f,1.0f,0.0f);
gl.glNormal3f(1.0f,0.0f,0.0f);
gl.glTexCoord2f(0.0f, 0.0f);
gl.glVertex3f(1.0f,1.0f,-1.0f);
gl.glTexCoord2f(0.0f, 1.0f);
gl.glVertex3f(1.0f,1.0f,1.0f);
gl.glTexCoord2f(1.0f, 1.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glTexCoord2f(1.0f, 0.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
//?ciana dolna
gl.glColor3f(1.0f,0.0f,1.0f);
gl.glNormal3f(0.0f,-1.0f,0.0f);
gl.glTexCoord2f(0.0f, 1.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glTexCoord2f(1.0f, 1.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glTexCoord2f(1.0f, 0.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
gl.glTexCoord2f(0.0f, 0.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
//?ciana g?rna
gl.glColor3f(1.0f,0.0f,1.0f);
gl.glNormal3f(0.0f,1.0f,0.0f);
gl.glTexCoord2f(1.0f, 0.0f);
gl.glVertex3f(-1.0f,1.0f,1.0f);
gl.glTexCoord2f(0.0f, 0.0f);
gl.glVertex3f(1.0f,1.0f,1.0f);
gl.glTexCoord2f(0.0f, 1.0f);
gl.glVertex3f(1.0f,1.0f,-1.0f);
gl.glTexCoord2f(1.0f, 1.0f);
gl.glVertex3f(-1.0f,1.0f,-1.0f);




          gl.glEnd();
          gl.glFlush();
       
}
    
    
    public void kolo(float xsrodek, float ysrodek, float rozmiar, GL gl) {
         float kat;
         gl.glBegin(GL.GL_TRIANGLE_FAN);
         gl.glVertex3f(xsrodek, ysrodek, -6.0f);
         for (kat = 0.0f; kat < (2.0f * Math.PI);
                 kat += (Math.PI / 32.0f)) {
             float x = rozmiar * (float) Math.sin(kat) + xsrodek;
             float y = rozmiar * (float) Math.cos(kat) + ysrodek;
 
             gl.glVertex3f(x, y, -6.0f);
         }
     }
    
    
   


    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}