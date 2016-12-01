package org.yourorghere;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.Scanner;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_FRONT;
import static javax.media.opengl.GL.GL_SHININESS;
import static javax.media.opengl.GL.GL_SPECULAR;
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
   static Koparka koparka;
   int i=0;
 private static float xrot = 0.0f, yrot = 0.0f;
 public static float ambientLight[] = { 0.3f, 0.3f, 0.3f, 1.0f };//swiat?o otaczaj?ce
public static float diffuseLight[] = { 0.7f, 0.7f, 0.7f, 1.0f };//?wiat?o rozproszone
public static float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f}; //?wiat?o odbite
public static float lightPos[] = { 0.0f, 150.0f, 150.0f, 1.0f };
public static float lightPos2[] = { 0.0f, 150.0f, -150.0f, 1.0f };
public static float direction[]={0,0,0};//pozycja ?wiat?a
    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();
       /* Scanner sc= new Scanner(System.in);
System.out.println("Podaj wspolrzedna x:");
p1=sc.nextFloat();
System.out.println("Podaj wspolrzedna y:");
p2=sc.nextFloat();
System.out.println("Podaj srednice:");
s=sc.nextFloat();*/
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
    ambientLight = new float[]{ambientLight[0]+0.1f, ambientLight[0]+0.1f, ambientLight[0]+0.1f,1.0f};
if(e.getKeyChar() == 'w')
    ambientLight = new float[]{ambientLight[0]+0.1f, ambientLight[0]+0.1f, ambientLight[0]+0.1f,1.0f};
if(e.getKeyChar() == 'a')
    diffuseLight = new float[]{diffuseLight[0]-0.1f, diffuseLight[0]-0.1f, diffuseLight[0]-0.1f,1.0f};
if(e.getKeyChar() == 's')
    diffuseLight = new float[]{diffuseLight[0]+0.1f, diffuseLight[0]+0.1f, diffuseLight[0]+0.1f,1.0f};
if(e.getKeyChar() == 'z')
    specular = new float[]{specular[0]-0.1f, specular[0]-0.1f, specular[0]-0.1f};
if(e.getKeyChar() == 'x')
    specular = new float[]{specular[0]+0.1f, specular[0]+0.1f, specular[0]+0.1f};
if(e.getKeyChar() == 'e')
    lightPos = new float[]{lightPos[0]-0.1f, lightPos[0]-0.1f, lightPos[0]-0.1f};
if(e.getKeyChar() == 'r')
    lightPos = new float[]{lightPos[0]+0.1f, lightPos[0]+0.1f, lightPos[0]+0.1f};
if(e.getKeyCode() == KeyEvent.VK_NUMPAD1)
    if(koparka.boki<0.0f&&koparka.boki>=-90.0f)
        koparka.boki+=1.0f;
if(e.getKeyCode() == KeyEvent.VK_NUMPAD2)
    if(koparka.boki<=0.0f&&koparka.boki>-90.0f)
        koparka.boki-=1.0f;
if(e.getKeyCode() == KeyEvent.VK_NUMPAD4)
    if(koparka.jeden<53.0f&&koparka.jeden>=-35.0f)
 koparka.jeden+=1.0f;
if(e.getKeyCode() == KeyEvent.VK_NUMPAD5)
    if(koparka.jeden<=53.0f&&koparka.jeden>-35.0f)
 koparka.jeden-=1.0f;
if(e.getKeyCode() == KeyEvent.VK_NUMPAD7)
    if(koparka.dwa<45.0f&&koparka.dwa>=-100.0f)
 koparka.dwa+=1.0f;
if(e.getKeyCode() == KeyEvent.VK_NUMPAD8)
    if(koparka.dwa<=45.0f&&koparka.dwa>-100.0f)
 koparka.dwa-=1.0f;
if(e.getKeyCode() == KeyEvent.VK_NUMPAD6)
    if(koparka.trzy<25.0f&&koparka.trzy>=-120.0f)
 koparka.trzy+=1.0f;
if(e.getKeyCode() == KeyEvent.VK_NUMPAD9)
    if(koparka.trzy<=25.0f&&koparka.trzy>-120.0f)
 koparka.trzy-=1.0f;

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
        koparka = new Koparka();
        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); 
gl.glEnable(GL.GL_CULL_FACE);// try setting this to GL_FLAT and see what happens.
//warto?ci sk?adowe o?wietlenia i koordynaty ?r?d?a ?wiat?a


//(czwarty parametr okre?la odleg?o?? ?r?d?a:
//0.0f-niesko?czona; 1.0f-okre?lona przez pozosta?e parametry)
gl.glEnable(GL.GL_LIGHTING); //uaktywnienie o?wietlenia
//ustawienie parametr?w ?r?d?a ?wiat?a nr. 0
gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat?o otaczaj?ce
gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //?wiat?o rozproszone
gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //?wiat?o odbite
gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0);
gl.glLightfv( GL.GL_LIGHT0, GL.GL_SPOT_DIRECTION, direction ,0);
gl.glLightf( GL.GL_LIGHT0, GL.GL_SPOT_EXPONENT, 1.0f );
gl.glLightf( GL.GL_LIGHT0, GL.GL_SPOT_CUTOFF, 1.0f);//pozycja ?wiat?a
gl.glEnable(GL.GL_LIGHT0);

gl.glLightfv(GL.GL_LIGHT1,GL.GL_AMBIENT,ambientLight,0); //swiat?o otaczaj?ce
gl.glLightfv(GL.GL_LIGHT1,GL.GL_DIFFUSE,diffuseLight,0); //?wiat?o rozproszone
gl.glLightfv(GL.GL_LIGHT1,GL.GL_SPECULAR,specular,0); //?wiat?o odbite
gl.glLightfv(GL.GL_LIGHT1,GL.GL_POSITION,lightPos2,0); //pozycja ?wiat?a
gl.glEnable(GL.GL_LIGHT1);//uaktywnienie ?r?d?a ?wiat?a nr. 0
gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie ?ledzenia kolor?w
//kolory b?d? ustalane za pomoc? glColor
gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
//Ustawienie jasno?ci i odblaskowo?ci obiekt?w
float specref[] = { 1.0f, 1.0f, 1.0f, 1.0f }; //parametry odblaskowo?ci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR,specref,0);
        
        gl.glMateriali(GL.GL_FRONT,GL.GL_SHININESS,128);
        gl.glEnable(GL.GL_DEPTH_TEST);
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
        glu.gluPerspective(100.0f, h, 1.0, 200.0);
       // gl.glViewport(200, 200, 100, 100);

           /*     float ilor;
if(width<=height)
{
ilor = height/width;
gl.glOrtho(-10.0f,10.0f,-10.0f*ilor,10.0f*ilor,-10.0f,10.0f);
}
else
{
ilor = width/height;
 gl.glOrtho(-10.0f*ilor,10.0f*ilor,-10.0f,10.0f,-10.0f,10.0f);
}

    */
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

    }
    public void display(GLAutoDrawable drawable) {
//Tworzenie obiektu
GL gl = drawable.getGL();
//Czyszczenie przestrzeni 3D przed utworzeniem kolejnej klatki
 gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
 //Resetowanie macierzy transformacji

 gl.glLoadIdentity();
 gl.glTranslatef(0.0f, 0.0f, -16.0f); //przesuni?cie o 6 jednostek
 gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wok?? osi X
 gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wok?? osi Y
 gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat?o otaczaj?ce
gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //?wiat?o rozproszone
gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0);


// Tu piszemy kod rysuj?cy grafik? 3D
/*gl.glBegin(GL.GL_TRIANGLES);
gl.glColor3f(1.0f,0.0f,0.0f);

gl.glVertex3f(-1.0f, 2.0f, -6.0f);
gl.glVertex3f(-3.0f,1.0f, -6.0f);
gl.glVertex3f( 1.0f,1.0f, -6.0f);
gl.glEnd();

gl.glBegin(GL.GL_QUADS);
gl.glColor3f(1.0f,3.0f,0.0f);
gl.glVertex3f(-2.9f,1.0f,-6.0f);
gl.glVertex3f(-2.9f,-1.0f,-6.0f);
gl.glVertex3f(0.9f,-1.0f,-6.0f);
gl.glVertex3f(0.9f,1.0f,-6.0f);
gl.glEnd();

gl.glBegin(GL.GL_QUADS);
gl.glColor3f(0.4f,0.2f,0.2f);
gl.glVertex3f(-2.8f,-0.0f,-6.0f);
gl.glVertex3f(-2.8f,-1.0f,-6.0f);
gl.glVertex3f(-2.2f,-1.0f,-6.0f);
gl.glVertex3f(-2.2f,-0.0f,-6.0f);
gl.glEnd();

gl.glBegin(GL.GL_QUADS);
gl.glColor3f(0.2f,0.4f,1.0f);
gl.glVertex3f(0.2f,0.8f,-6.0f);
gl.glVertex3f(0.2f,0.2f,-6.0f);
gl.glVertex3f(0.8f,0.2f,-6.0f);
gl.glVertex3f(0.8f,0.8f,-6.0f);
gl.glEnd();*/



//kolo(gl,p1,p2,s);
/* float x1=-1;
 float y1=0;
 float x2=1;
 float y2=0;
 float x3=0;
 float y3=1;
 Random rd=new Random();
        
 for(int i=0;i<2;i++){
     
 trojkat(gl,x1,y1,x2,y2,x3,y3,-6,0.1f+i/3,0.1f+i/3,0.1f+i/3);
 y3=y1;
 x1=(x3-x1)/2;
 y1=(y3-y1)/2;
 x2=(x3-x2)/2;
 y2=(y3-y2)/2;
 x3=(x2-x1)/2;
 
 }*/
/*
//szescian\/
 gl.glBegin(GL.GL_QUADS);
//?ciana g?rna
 gl.glColor3f(0.0f, 1.0f, 0.0f);   
 gl.glNormal3f(0.0f,1.0f,0.0f);
      gl.glVertex3f( 1.0f, 1.0f, -1.0f);
      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
      gl.glVertex3f(-1.0f, 1.0f,  1.0f);
      gl.glVertex3f( 1.0f, 1.0f,  1.0f);
      
      
      
      //sciana przednia
 gl.glColor3f(0.0f, 1.0f, 0.0f); 
 gl.glNormal3f(0.0f,0.0f,1.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glVertex3f(1.0f,1.0f,1.0f);
gl.glVertex3f(-1.0f,1.0f,1.0f);

//sciana tylnia
 gl.glColor3f(0.0f, 1.0f, 0.0f); 
 gl.glNormal3f(0.0f,0.0f,1.0f);
gl.glVertex3f(-1.0f,1.0f,-1.0f);
gl.glVertex3f(1.0f,1.0f,-1.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);

//?ciana lewa
 gl.glColor3f(0.0f, 1.0f, 0.0f); 
 gl.glNormal3f(1.0f,0.0f,0.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(-1.0f,1.0f,1.0f);
gl.glVertex3f(-1.0f,1.0f,-1.0f);



//?ciana prawa
 gl.glColor3f(0.0f, 1.0f, 0.0f); 
 gl.glNormal3f(1.0f,0.0f,0.0f);
gl.glVertex3f(1.0f,1.0f,-1.0f);
gl.glVertex3f(1.0f,1.0f,1.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);

//?ciana dolna
 gl.glColor3f(0.0f, 1.0f, 0.0f); 
 gl.glNormal3f(0.0f,1.0f,0.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);

gl.glEnd();*/
//ostroslup\/
/*gl.glBegin(GL.GL_QUADS);
gl.glColor3f(1.0f,0.0f,1.0f);
gl.glNormal3f(0.0f,1.0f,0.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glEnd();
gl.glBegin(GL.GL_TRIANGLES);
gl.glColor3f(1.0f,0.0f,0.0f);
float[] scianka1={-1.0f, -1.0f, 1.0f, //wp??rz?dne pierwszego punktu
                    1.0f, -1.0f, 1.0f, //wsp??rz?dne drugiego punktu
 0.0f, 1.0f, 0.0f}; //wsp??rz?dne trzeciego punktu
float[] normalna1 = WyznaczNormalna(scianka1,0,3,6);
gl.glNormal3fv(normalna1,0);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glVertex3f(0.0f,1.0f,0.0f);

gl.glColor3f(0.0f,1.0f,0.0f);
float[] scianka2={1.0f, -1.0f, -1.0f, //wp??rz?dne pierwszego punktu
                    -1.0f, -1.0f, -1.0f, //wsp??rz?dne drugiego punktu
                    0.0f, 1.0f, 0.0f}; //wsp??rz?dne trzeciego punktu
float[] normalna2 = WyznaczNormalna(scianka1,0,3,6);
gl.glNormal3fv(normalna2,0);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glVertex3f(0.0f,1.0f,0.0f);

gl.glColor3f(0.0f,0.0f,1.0f);
float[] scianka3={-1.0f, -1.0f, -1.0f, //wp??rz?dne pierwszego punktu
                    -1.0f, -1.0f, 1.0f, //wsp??rz?dne drugiego punktu
                    0.0f, 1.0f, 0.0f}; //wsp??rz?dne trzeciego punktu
float[] normalna3 = WyznaczNormalna(scianka1,0,3,6);
gl.glNormal3fv(normalna3,0);
gl.glVertex3f(-1.0f,-1.0f,-1.0f);
gl.glVertex3f(-1.0f,-1.0f,1.0f);
gl.glVertex3f(0.0f,1.0f,0.0f);



gl.glColor3f(1.0f,1.0f,0.0f);
float[] scianka4={1.0f, -1.0f, 1.0f, //wp??rz?dne pierwszego punktu
                    1.0f, -1.0f, -1.0f, //wsp??rz?dne drugiego punktu
                    0.0f, 1.0f, 0.0f}; //wsp??rz?dne trzeciego punktu
float[] normalna4 = WyznaczNormalna(scianka1,0,3,6);
gl.glNormal3fv(normalna4,0);
gl.glVertex3f(1.0f,-1.0f,1.0f);
gl.glVertex3f(1.0f,-1.0f,-1.0f);
gl.glVertex3f(0.0f,1.0f,0.0f);
gl.glEnd();
*/
 
//walec\/
/*
float x,y,kat;
gl.glBegin(GL.GL_QUAD_STRIP);
//gl.glVertex3f(0.0f,0.0f,-6.0f); //?rodek
gl.glColor3f(1.0f,1.0f,0.0f);
for(kat = 0.0f; kat < (2.0f*Math.PI);
kat+=(Math.PI/32.0f))
{
x = 1.0f*(float)Math.sin(kat);
y = 1.0f*(float)Math.cos(kat);
gl.glNormal3f(x,y,0f);
gl.glVertex3f(x, 2.0f, y);
gl.glVertex3f(x, -2.0f, y);//kolejne punkty
}
gl.glEnd();

float xx,yy,katt;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glColor3f(1.0f,0.0f,0.0f);
gl.glVertex3f(0.0f,2.0f,0.0f); //?rodek
for(katt = 0.0f; katt < (2.0f*Math.PI);
katt+=(Math.PI/32.0f))
{
xx = 1.0f*(float)Math.sin(katt);
yy = 1.0f*(float)Math.cos(katt);
gl.glNormal3f(xx,yy,0f);
gl.glVertex3f(xx, 2.0f, yy); //kolejne punkty
}
gl.glEnd();

float xxx,yyy,kattt;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glColor3f(0.0f,1.0f,0.0f);
gl.glVertex3f(0.0f,-2.0f,0.0f); //?rodek
for(kattt = (float) (2.0f*Math.PI); kattt > 0.0f;
kattt-=(Math.PI/32.0f))
{
xxx = 1.0f*(float)Math.sin(kattt);
yyy = 1.0f*(float)Math.cos(kattt);
gl.glNormal3f(xxx,yyy,0f);
gl.glVertex3f(xxx, -2.0f, yyy); //kolejne punkty
}
gl.glEnd();



gl.glBegin(GL.GL_QUAD_STRIP);
//gl.glVertex3f(0.0f,0.0f,-6.0f); //?rodek
gl.glColor3f(1.0f,1.0f,0.0f);
for(kat = 0.0f; kat < (2.0f*Math.PI);
kat+=(Math.PI/32.0f))
{
x = 1.0f*(float)Math.sin(kat);
y = 1.0f*(float)Math.cos(kat);
gl.glNormal3f(x+4,y,0f);
gl.glVertex3f(x+4, 2.0f, y);
gl.glVertex3f(x+4, -2.0f, y);//kolejne punkty
}
gl.glEnd();


gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glColor3f(1.0f,0.0f,0.0f);
gl.glVertex3f(4.0f,2.0f,0.0f); //?rodek
for(katt = 0.0f; katt < (2.0f*Math.PI);
katt+=(Math.PI/32.0f))
{
xx = 1.0f*(float)Math.sin(katt);
yy = 1.0f*(float)Math.cos(katt);
gl.glNormal3f(xx+4,yy,0f);
gl.glVertex3f(xx+4, 2.0f, yy); //kolejne punkty
}
gl.glEnd();


gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glColor3f(0.0f,1.0f,0.0f);
gl.glVertex3f(4.0f,-2.0f,0.0f); //?rodek
for(kattt = (float) (2.0f*Math.PI); kattt > 0.0f;
kattt-=(Math.PI/32.0f))
{
xxx = 1.0f*(float)Math.sin(kattt);
yyy = 1.0f*(float)Math.cos(kattt);
gl.glNormal3f(xxx+4,yyy,0f);
gl.glVertex3f(xxx+4, -2.0f, yyy); //kolejne punkty
}
gl.glEnd();
 */
/*float xx,yy,katt;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glColor3f(1.0f,0.0f,0.0f);
gl.glVertex3f(0.0f,2.0f,0.0f); //?rodek
for(katt = 0.0f; katt < (2.0f*Math.PI);
katt+=(Math.PI/32.0f))
{
xx = 1.0f*(float)Math.sin(katt);
yy = 1.0f*(float)Math.cos(katt);
gl.glVertex3f(xx, -2.0f, yy); //kolejne punkty
}
gl.glEnd();
float xxx,yyy,kattt;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glColor3f(0.0f,1.0f,0.0f);
gl.glVertex3f(0.0f,-2.0f,0.0f); //?rodek
for(kattt = (float) (2.0f*Math.PI); kattt > 0.0f;
kattt-=(Math.PI/32.0f))
{
xxx = 1.0f*(float)Math.sin(kattt);
yyy = 1.0f*(float)Math.cos(kattt);
gl.glVertex3f(xxx, -2.0f, yyy); //kolejne punkty
}
gl.glEnd();*/
 
 
/*Kolczatka \/
        float xx,yy,katt,sr=2.0f,xr=0.0f,yr=0.0f;
 for(int z=0;z<6;z++)
{
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glColor3f(1.0f,0.0f,0.0f);

gl.glVertex3f(xr,sr,yr); //?rodek
for(katt = (float) (2.0f*Math.PI); katt >0.0f ;
katt-=(Math.PI/32.0f))
{
xx =0.20f*(float)Math.sin(katt);
yy = 0.20f*(float)Math.cos(katt);
if(sr!=0)
gl.glVertex3f(xx, 0.0f, yy);
else
if(xr!=0)
gl.glVertex3f(0.0f,xx, yy);
else
if(yr!=0)
gl.glVertex3f(xx, yy, 0.0f);
//kolejne punkty
}
if(sr==0&&xr==0&&yr==2){
   

    sr=0;
    xr=0;
    yr=-2;

}
if(sr==0&&xr==-2){
   

    sr=0;
    xr=0;
    yr=2;

}
if(sr==0&&xr==2){
   

    sr=0;
    xr=-2;

}
if(sr==-2){
   

    sr=0;
    xr=2;

}
if(sr==2.0f){
    sr=-2.0f;
   
}

gl.glEnd();
}*/
 //Wykonanie wszystkich operacji znajduj?cych si? w buforze
 

/*choinki\/
gl.glRotatef(90f, 1, 0, 0);
for(int iiii=0;iiii<10;iiii++)
{
    gl.glPushMatrix();
   
    gl.glTranslatef(iiii*2, -iiii*2, 0);
    
gl.glColor3f(0.0f, 0.4f, 0.0f);
stozek(gl);
gl.glTranslatef(0, 0, 1);
gl.glScalef(1.2f, 1.2f, 1.2f);
stozek(gl);
gl.glTranslatef(0, 0, 1);
gl.glScalef(1.2f, 1.2f, 1.2f);
stozek(gl);
gl.glTranslatef(0, 0, 0.5f);
gl.glColor3f(0.139f, 0.063f, 0.019f);
gl.glScalef(0.3f, 0.3f, 0.6f);
walec(gl);
 gl.glPopMatrix();
 gl.glEnd();
}
for(int iiii=0;iiii<10;iiii++)
{
    gl.glPushMatrix();
   
    gl.glTranslatef(iiii*2+3, -iiii*2+3, 0);
    
gl.glColor3f(0.0f, 0.4f, 0.0f);
stozek(gl);
gl.glTranslatef(0, 0, 1);
gl.glScalef(1.2f, 1.2f, 1.2f);
stozek(gl);
gl.glTranslatef(0, 0, 1);
gl.glScalef(1.2f, 1.2f, 1.2f);
stozek(gl);
gl.glTranslatef(0, 0, 0.5f);
gl.glColor3f(0.139f, 0.063f, 0.019f);
gl.glScalef(0.3f, 0.3f, 0.6f);
walec(gl);
 gl.glPopMatrix();
 gl.glEnd();
}*/

gl.glScalef(2,2,2);
koparka.Rysuj(gl);

 if(koparka.trzy<-70.0f&&i==0)
 {     
     
     i=1;
 }
 if(koparka.trzy>=23.0f&&i==1)
 {     
     
     i=0;
 }
if(koparka.jeden>-15.0f&&i==0)
{
    koparka.jeden-=0.1f;
    
}
if(koparka.jeden<=-5.0f&&i==0&&koparka.dwa>-70.0f)
    {
        koparka.dwa-=0.1f;
    }
 if(koparka.dwa<=-55.0f&&i==0&&koparka.trzy>-70.0f)
    {
        koparka.trzy-=0.1f;
        System.out.println(koparka.trzy);
    }

 if(koparka.jeden<65.0f&&i==1)
{
    koparka.jeden+=0.1f;
    
}
 if(koparka.jeden>40&&i==1&&koparka.trzy<25.0f)
     koparka.trzy+=0.1f;
gl.glFlush();
}


    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
    public void kolo(GL gl, float p1, float p2, float s)
    {
      
        gl.glBegin(GL.GL_TRIANGLE_FAN);
float x,y,kat;

gl.glVertex3f(p1,p2,-6.0f); //?rodek
for(kat = 0.0f; kat < (2.0f*Math.PI);
kat+=(Math.PI/32.0f))
{
x = s/2*(float)Math.sin(kat);
y = s/2*(float)Math.cos(kat);
gl.glVertex3f(x+p1, y+p2, -6.0f); //kolejne punkty
}
gl.glEnd(); 
    }
    public void trojkat(GL gl, float x1,float y1,float x2, float y2, float x3, float y3, float z,float c1, float c2, float c3)
    {
        
       gl.glBegin(GL.GL_TRIANGLES);
       gl.glColor3f(c1,c2,c3);
        gl.glVertex3f(x1, y1, z);
        gl.glVertex3f(x2,y2, z);
        gl.glVertex3f(x3,y3, z);
        gl.glEnd(); 
    }
    void walec(GL gl)
 {
//wywo?ujemy automatyczne normalizowanie normalnych
//bo operacja skalowania je zniekszta?ci
gl.glEnable(GL.GL_NORMALIZE);
float x,y,kat;
gl.glBegin(GL.GL_QUAD_STRIP);
for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f))
{
x = 0.5f*(float)Math.sin(kat);
y = 0.5f*(float)Math.cos(kat);
gl.glNormal3f((float)Math.sin(kat),(float)Math.cos(kat),0.0f);
gl.glVertex3f(x, y, -1.0f);
gl.glVertex3f(x, y, 0.0f);
}
gl.glEnd();
gl.glNormal3f(0.0f,0.0f,-1.0f);
x=y=kat=0.0f;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glVertex3f(0.0f, 0.0f, -1.0f); //srodek kola
for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f))
{
x = 0.5f*(float)Math.sin(kat);
y = 0.5f*(float)Math.cos(kat);
gl.glVertex3f(x, y, -1.0f);
}
gl.glEnd();
gl.glNormal3f(0.0f,0.0f,1.0f);
x=y=kat=0.0f;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
for(kat = 2.0f*(float)Math.PI; kat > 0.0f ; kat -= (Math.PI/32.0f))
{
x = 0.5f*(float)Math.sin(kat);
y = 0.5f*(float)Math.cos(kat);
gl.glVertex3f(x, y, 0.0f);
}
gl.glEnd();
}

void stozek(GL gl)
{
//wywo?ujemy automatyczne normalizowanie normalnych
gl.glEnable(GL.GL_NORMALIZE);
float x,y,kat;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glVertex3f(0.0f, 0.0f, -2.0f); //wierzcholek stozka
for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f))
{
x = (float)Math.sin(kat);
y = (float)Math.cos(kat);
gl.glNormal3f((float)Math.sin(kat),(float)Math.cos(kat),-2.0f);
gl.glVertex3f(x, y, 0.0f);
}
gl.glEnd();
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glNormal3f(0.0f,0.0f,1.0f);
gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
for(kat = 2.0f*(float)Math.PI; kat > 0.0f; kat -= (Math.PI/32.0f))
{
x = (float)Math.sin(kat);
y = (float)Math.cos(kat);
gl.glVertex3f(x, y, 0.0f);
}
gl.glEnd();
}

    private float[] WyznaczNormalna(float[] punkty, int ind1, int ind2, int ind3)
{
 float[] norm = new float[3];
 float[] wektor0 = new float[3];
 float[] wektor1 = new float[3];

 for(int i=0;i<3;i++)
 {
 wektor0[i]=punkty[i+ind1]-punkty[i+ind2];
 wektor1[i]=punkty[i+ind2]-punkty[i+ind3];
 }

 norm[0]=wektor0[1]*wektor1[2]-wektor0[2]*wektor1[1];
 norm[1]=wektor0[2]*wektor1[0]-wektor0[0]*wektor1[2];
 norm[2]=wektor0[0]*wektor1[1]-wektor0[1]*wektor1[0];
 float d=
(float)Math.sqrt((norm[0]*norm[0])+(norm[1]*norm[1])+ (norm[2]*norm[2]) );
 if(d==0.0f)
 d=1.0f;
 norm[0]/=d;
 norm[1]/=d;
 norm[2]/=d;

 return norm;
}
}
