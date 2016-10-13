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



/**
 * Kowalkowski.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class Kowalkowski implements GLEventListener {
//statyczne pola okre�laj�ce rotacj� wok� osi X i Y
 private static float xrot = 0.0f, yrot = 0.0f;

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
        
        //Obs�uga klawiszy strza�ek
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
        
        //wy��czenie wewn�trzych stron prymityw�w
        gl.glEnable(GL.GL_CULL_FACE);

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
          
          gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuni�cie o 6 jednostek
            gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wok� osi X
            gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wok� osi Y
            kolo(0.0f, 0.0f, 1.0f, gl);
            kolo2(0.0f, 0.0f, 1.0f, gl);
            tuba(1.0f, gl);
            /*
            gl.glBegin(GL.GL_QUADS);
            //podstawa
gl.glColor3f(1.0f,0.0f,1.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);

gl.glEnd();
//sciana tylna
gl.glBegin(GL.GL_TRIANGLES);
gl.glColor3f(0.0f,1.0f,0.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glVertex3f(0.0f,1.0f,0.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);

//�ciana lewa
gl.glColor3f(0.0f,0.0f,1.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(0.0f,1.0f,0.0f);

//�ciana prawa
gl.glColor3f(1.0f,1.0f,0.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
gl.glVertex3f(0.0f,1.0f,0.0f);


//�ciana przednia
gl.glColor3f(1.0f,0.0f,0.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glVertex3f(0.0f,1.0f,0.0f);


          gl.glEnd();
                    */
          gl.glFlush();
       
}
    
    
    public void kolo(float xsrodek, float ysrodek, float rozmiar, GL gl) {
         float kat;
         
         gl.glBegin(GL.GL_TRIANGLE_FAN);
         gl.glColor3f(1.0f,0.0f,0.0f);
         gl.glVertex3f(xsrodek, ysrodek, 2.0f);
         for (kat = (float) (2.0f * Math.PI); kat > 0;
                 kat -= (Math.PI / 32.0f)) {
             float x = rozmiar * (float) Math.sin(kat) + xsrodek;
             float y = rozmiar * (float) Math.cos(kat) + ysrodek;

             gl.glVertex3f(x, y, 2.0f);
         }
         gl.glEnd();
     }
    
    public void kolo2(float xsrodek, float ysrodek, float rozmiar, GL gl) {
         float kat;
         
         gl.glBegin(GL.GL_TRIANGLE_FAN);
         gl.glColor3f(0.0f,0.0f,1.0f);
         gl.glVertex3f(xsrodek, ysrodek, 0.0f);
         for (kat = 0.0f  ; kat < (2.0f * Math.PI);
                 kat += (Math.PI / 32.0f)) {
             float x = rozmiar * (float) Math.sin(kat) + xsrodek;
             float y = rozmiar * (float) Math.cos(kat) + ysrodek;

             gl.glVertex3f(x, y, 0.0f);
         }
         gl.glEnd();
     }
    
     public void tuba(float rozmiar, GL gl) {
         float kat;
         
         gl.glBegin(GL.GL_QUAD_STRIP);
         gl.glColor3f(0.0f,1.0f,0.0f);
         
         for (kat = 0.0f  ; kat < (2.0f * Math.PI);
                 kat += (Math.PI / 32.0f)) {
             float x = rozmiar * (float) Math.sin(kat);
             float y = rozmiar * (float) Math.cos(kat);

             gl.glVertex3f(x, y, 0.0f);
             gl.glVertex3f(x, y, 2.0f);
         }
         gl.glEnd();
     }
    
    
   


    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}

