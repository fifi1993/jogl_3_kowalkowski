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
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel)
 * <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class Kowalkowski implements GLEventListener {
//statyczne pola okre?laj?ce rotacj? wokó? osi X i Y

    private static float xrot = 0.0f, yrot = 0.0f;
    static float ambientLight[] = {0.3f, 0.3f, 0.3f, 1.0f};//swiat³o otaczaj¹ce
    static float diffuseLight[] = {0.7f, 0.7f, 0.7f, 1.0f};//œwiat³o rozproszone
    static float specular[] = {1.0f, 1.0f, 1.0f, 1.0f}; //œwiat³o odbite
    static float lightPos[] = {0.0f, 150.0f, 150.0f, 1.0f};//pozycja œwiat³a

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
        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    xrot -= 1.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    xrot += 1.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    yrot += 1.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    yrot -= 1.0f;
                }

                if (e.getKeyChar() == 'q') {
                    ambientLight = new float[]{ambientLight[0] - 0.1f, ambientLight[0] - 0.1f, ambientLight[0] - 0.1f, 1.0f};
                }
                if (e.getKeyChar() == 'w') {
                    ambientLight = new float[]{ambientLight[0] + 0.1f, ambientLight[0] + 0.1f, ambientLight[0] + 0.1f, 1.0f};
                }
                if (e.getKeyChar() == 'e') {
                    diffuseLight = new float[]{diffuseLight[0] - 0.1f, diffuseLight[0] - 0.1f, diffuseLight[0] - 0.1f, 1.0f};
                }
                if (e.getKeyChar() == 'r') {
                    diffuseLight = new float[]{diffuseLight[0] + 0.1f, diffuseLight[0] + 0.1f, diffuseLight[0] + 0.1f, 1.0f};
                }
                if (e.getKeyChar() == 't') {
                    specular = new float[]{specular[0] - 0.1f, specular[0] - 0.1f, specular[0] - 0.1f, 1.0f};
                }
                if (e.getKeyChar() == 'y') {
                    specular = new float[]{specular[0] + 0.1f, specular[0] + 0.1f, specular[0] + 0.1f, 1.0f};
                }
                if (e.getKeyChar() == 'u') {
                    lightPos[3] = 0;
                }
                if (e.getKeyChar() == 'i') {
                    lightPos[3] = 1;
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });

        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    private float[] WyznaczNormalna(float[] punkty, int ind1, int ind2, int ind3) {
        float[] norm = new float[3];
        float[] wektor0 = new float[3];
        float[] wektor1 = new float[3];

        for (int i = 0; i < 3; i++) {
            wektor0[i] = punkty[i + ind1] - punkty[i + ind2];
            wektor1[i] = punkty[i + ind2] - punkty[i + ind3];
        }

        norm[0] = wektor0[1] * wektor1[2] - wektor0[2] * wektor1[1];
        norm[1] = wektor0[2] * wektor1[0] - wektor0[0] * wektor1[2];
        norm[2] = wektor0[0] * wektor1[1] - wektor0[1] * wektor1[0];
        float d
                = (float) Math.sqrt((norm[0] * norm[0]) + (norm[1] * norm[1]) + (norm[2] * norm[2]));
        if (d == 0.0f) {
            d = 1.0f;
        }
        norm[0] /= d;
        norm[1] /= d;
        norm[2] /= d;

        return norm;
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

        //wy??czenie wewn?trzych stron prymitywów
        gl.glEnable(GL.GL_CULL_FACE);

        //wartoœci sk³adowe oœwietlenia i koordynaty Ÿród³a œwiat³a
        //(czwarty parametr okreœla odleg³oœæ Ÿród³a:
        //0.0f-nieskoñczona; 1.0f-okreœlona przez pozosta³e parametry)
        gl.glEnable(GL.GL_LIGHTING); //uaktywnienie oœwietlenia
        //ustawienie parametrów Ÿród³a œwiat³a nr. 0
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0); //swiat³o otaczaj¹ce
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0); //œwiat³o rozproszone
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0); //œwiat³o odbite
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0); //pozycja œwiat³a
        gl.glEnable(GL.GL_LIGHT0); //uaktywnienie Ÿród³a œwiat³a nr. 0
        gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie œledzenia kolorów
        //kolory bêd¹ ustalane za pomoc¹ glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasnoœci i odblaskowoœci obiektów
        float specref[] = {1.0f, 1.0f, 1.0f, 1.0f}; //parametry odblaskowoœci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specref, 0);

        gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 128);

        gl.glEnable(GL.GL_DEPTH_TEST);
        // Setup the drawing area and shading mode
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width/2, height/2);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(90.0f, h, 1.0, 20.0);
        /*
        float ilor;
        if (width <= height) {
            ilor = height / width;
            gl.glOrtho(-10.0f, 10.0f, -10.0f * ilor, 10.0f * ilor, -10.0f, 20.0f);
        } else {
            ilor = width / height;
            gl.glOrtho(-10.0f * ilor, 10.0f * ilor, -10.0f, 10.0f, -10.0f, 20.0f);
        }
        */

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        //kolo(0.0f, 0.0f, 0.5f, gl);
        gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuni?cie o 6 jednostek
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wokó? osi X
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wokó? osi Y
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0); //swiat³o otaczaj¹ce
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0); //œwiat³o rozproszone
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0); //œwiat³o odbite
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0); //pozycja œwiat³a

        /*      gl.glBegin(GL.GL_QUADS);
         //podstawa
         gl.glColor3f(1.0f,0.0f,1.0f);
         gl.glNormal3f(0.0f,-1.0f,0.0f);
         gl.glVertex3f(-1.0f,-1.0f,1.0f);
         gl.glVertex3f(-1.0f,-1.0f,-1.0f);
         gl.glVertex3f(1.0f,-1.0f,-1.0f);
         gl.glVertex3f(1.0f,-1.0f,1.0f);

         gl.glEnd();
         //sciana tylna
         gl.glBegin(GL.GL_TRIANGLES);
         gl.glColor3f(0.0f,1.0f,0.0f);

         float[] scianka4={-1.0f, -1.0f, -1.0f, //wpó³rzêdne pierwszego punktu
         0.0f, 1.0f, 0.0f, //wspó³rzêdne drugiego punktu
         1.0f, -1.0f, -1.0f}; //wspó³rzêdne trzeciego punktu
         float[] normalna4 = WyznaczNormalna(scianka4,0,3,6);
         gl.glNormal3fv(normalna4,0);
         gl.glVertex3fv(scianka4,0); //wspó³rzêdne 1-go punktu zaczynaj¹ siê od indeksu 0
         gl.glVertex3fv(scianka4,3); //wspó³rzêdne 2-go punktu zaczynaj¹ siê od indeksu 3
         gl.glVertex3fv(scianka4,6); //wspó³rzêdne 3-go punktu zaczynaj¹ siê od indeksu 6

         //?ciana lewa
         gl.glColor3f(0.0f,0.0f,1.0f);

         float[] scianka3={-1.0f, -1.0f, -1.0f, //wpó³rzêdne pierwszego punktu
         -1.0f, -1.0f, 1.0f, //wspó³rzêdne drugiego punktu
         0.0f, 1.0f, 0.0f}; //wspó³rzêdne trzeciego punktu
         float[] normalna3 = WyznaczNormalna(scianka3,0,3,6);
         gl.glNormal3fv(normalna3,0);
         gl.glVertex3fv(scianka3,0); //wspó³rzêdne 1-go punktu zaczynaj¹ siê od indeksu 0
         gl.glVertex3fv(scianka3,3); //wspó³rzêdne 2-go punktu zaczynaj¹ siê od indeksu 3
         gl.glVertex3fv(scianka3,6); //wspó³rzêdne 3-go punktu zaczynaj¹ siê od indeksu 6

         //?ciana prawa
         gl.glColor3f(1.0f,1.0f,0.0f);

         float[] scianka2={1.0f, -1.0f, 1.0f, //wpó³rzêdne pierwszego punktu
         1.0f, -1.0f, -1.0f, //wspó³rzêdne drugiego punktu
         0.0f, 1.0f, 0.0f}; //wspó³rzêdne trzeciego punktu
         float[] normalna2 = WyznaczNormalna(scianka2,0,3,6);
         gl.glNormal3fv(normalna2,0);
         gl.glVertex3fv(scianka2,0); //wspó³rzêdne 1-go punktu zaczynaj¹ siê od indeksu 0
         gl.glVertex3fv(scianka2,3); //wspó³rzêdne 2-go punktu zaczynaj¹ siê od indeksu 3
         gl.glVertex3fv(scianka2,6); //wspó³rzêdne 3-go punktu zaczynaj¹ siê od indeksu 6


         //?ciana przednia
         gl.glColor3f(1.0f,0.0f,0.0f);
         float[] scianka1={-1.0f, -1.0f, 1.0f, //wpó³rzêdne pierwszego punktu
         1.0f, -1.0f, 1.0f, //wspó³rzêdne drugiego punktu
         0.0f, 1.0f, 0.0f}; //wspó³rzêdne trzeciego punktu
         float[] normalna1 = WyznaczNormalna(scianka1,0,3,6);
         gl.glNormal3fv(normalna1,0);
         gl.glVertex3fv(scianka1,0); //wspó³rzêdne 1-go punktu zaczynaj¹ siê od indeksu 0
         gl.glVertex3fv(scianka1,3); //wspó³rzêdne 2-go punktu zaczynaj¹ siê od indeksu 3
         gl.glVertex3fv(scianka1,6); //wspó³rzêdne 3-go punktu zaczynaj¹ siê od indeksu 6




         gl.glEnd();
         */
        /*
         walec(gl);
         gl.glTranslatef(0.0f, 0.0f, -1.0f);
         stozek(gl);
         gl.glTranslatef(0.0f, 0.0f, -1.5f);
         gl.glScalef(0.7f, 0.7f, 1.0f);
         stozek(gl);
         gl.glTranslatef(0.0f, 0.0f, -1.7f);
         gl.glScalef(0.5f, 0.5f, 0.5f);
         stozek(gl);
         */
        for (int j = 0; j < 5; j++) {
            choinka(gl);
            gl.glTranslatef(4.0f, 0.0f, 0.0f);

        }
        gl.glTranslatef(0.0f, 4.0f, 0.0f);
        for (int j = 0; j < 5; j++) {
            choinka(gl);
            gl.glTranslatef(-4.0f, 0.0f, 0.0f);

        }

        //choinka(gl);
        gl.glFlush();

    }

    void choinka(GL gl) {
        gl.glPushMatrix();
        walec(gl);
        gl.glTranslatef(0.0f, 0.0f, -1.0f);
        stozek(gl);
        gl.glTranslatef(0.0f, 0.0f, -1.5f);
        gl.glScalef(0.7f, 0.7f, 1.0f);
        stozek(gl);
        gl.glTranslatef(0.0f, 0.0f, -1.7f);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        stozek(gl);
        gl.glPopMatrix();
    }

    void walec(GL gl) {
//wywo³ujemy automatyczne normalizowanie normalnych
//bo operacja skalowania je zniekszta³ci
        gl.glEnable(GL.GL_NORMALIZE);
        float x, y, kat;
        gl.glBegin(GL.GL_QUAD_STRIP);
        gl.glColor3f(0.7f, 0.4f, 0.0f);
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glNormal3f((float) Math.sin(kat), (float) Math.cos(kat), 0.0f);
            gl.glVertex3f(x, y, -1.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        x = y = kat = 0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, -1.0f); //srodek kola
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glVertex3f(x, y, -1.0f);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        x = y = kat = 0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for (kat = 2.0f * (float) Math.PI; kat > 0.0f; kat -= (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
    }

    void stozek(GL gl) {
//wywo³ujemy automatyczne normalizowanie normalnych
        gl.glEnable(GL.GL_NORMALIZE);
        float x, y, kat;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, -2.0f); //wierzcholek stozka
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = (float) Math.sin(kat);
            y = (float) Math.cos(kat);
            gl.glNormal3f((float) Math.sin(kat), (float) Math.cos(kat), -2.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for (kat = 2.0f * (float) Math.PI; kat > 0.0f; kat -= (Math.PI / 32.0f)) {
            x = (float) Math.sin(kat);
            y = (float) Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
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
