package tesis1.datos;

import java.util.Vector;
import java.io.*;

/**
 * Title:        Asignaci�n de horarios
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      ITSON
 * @author Adolfo Espinoza
 * @version 1.0
 */

public class VectorAulas extends Vector implements Cloneable{

  public VectorAulas(String archAulas) {
    super(1,1);
    leeArchAulas(archAulas);
  }

  public VectorAulas(){
    super(1,1);
  }


  public Object clone(){
    VectorAulas aux = new VectorAulas();
    int nAuls=this.size();
    for(int i=0; i<nAuls; i++){
      aux.addElement( ((Aula)this.elementAt(i)).clone() );
    }
    return aux;
  }

  public void leeArchAulas(String nomArch){
      //agregado por HQ
    nomArch = "C:\\Users\\hq82\\Documents\\NetBeansProjects\\Calendarizador\\real\\tabAulas.dat";
      
    int longitud;
    DataInput arch;
    AulaArch aulaEnt;
    Aula aula;
    this.removeAllElements();
    this.trimToSize();

    try{
      arch = new DataInputStream(new FileInputStream(new File(nomArch)));
      longitud =arch.readInt();   //Lee numero de aulas

      for(int i=0; i<longitud; i++){  //LEE CADA AULA
        aulaEnt=new AulaArch(); // Crea objetos Aula
        aulaEnt.cargarAula(arch);  // Asigna valores de archivo
        aula = new Aula();
        aula.copiaDesde(aulaEnt);  // copia datos de AulaEnt a Aula

        this.addElement(aula);      // Lo agrega al vector
      }
    }
    catch(IOException e) {
      System.out.print("\n Error leyendo archivo de datos de aulas \n");
    }
  }

  public int getIndexClave(String claveBuscada){
  // Regresa el indice (int) del elemento que tiene el String claveBuscada
  // en el atributo clave.
    String claveEnVect;
    int n =this.size();
    int cont;
    for(cont=0; cont<n; cont++){
      claveEnVect=((Aula)this.elementAt(cont)).getClave();
      if(claveEnVect.equals(claveBuscada))break;
    }
    if(cont==n){
      System.out.println("Aula no encontrada en el archivo tabAulas.dat");
    }
    return cont;
  }

  public int getEmpalmes(){
    int suma=0;
    for(int i=0; i<this.size(); i++){
      suma += (int)((Aula)this.elementAt(i)).cuentaEmpalmes();
    }
    return suma;
  }

  public void desasignaTodos(){
    for(int i=0; i<this.size(); i++){
      ((Aula)this.elementAt(i)).desasignaTodo();
    }
  }
}