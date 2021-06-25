package tesis1.datos;

import java.util.Vector;
import java.io.*;
import java.util.ArrayList;
import tesis1.io.ParamFileRead;

/**
 * Title:        Asignaci�n de horarios
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      ITSON
 * @author Adolfo Espinoza
 * @version 1.0
 */

public class VectorMaestros extends ArrayList<Maestro> implements Cloneable{

  public VectorMaestros(String archMaestros) {
    
    //super(1,1);
     
    leeArchMaestros(archMaestros);
  }

  public VectorMaestros() {
    //super(1,1);
  }

  public Object clone(){
    VectorMaestros aux = new VectorMaestros();
    int nMaes=this.size();
    for(int i=0; i<nMaes; i++){
      aux.add((Maestro)this.get(i).clone());
    }
    return aux;
  }


   public void leeArchMaestros(String nomArch){
     //agregado por HQ 
     
    //nomArch = "‪.\\real\\tabMaes.dat";
    //ParamFileRead hors = new ParamFileRead(nomArch);  
    int longitud;
    DataInput arch;
    MaestroArch maestroEnt;
    Maestro maestro;
    this.clear();
    this.trimToSize();

    try{
      arch = new DataInputStream(new FileInputStream(new File(nomArch)));
      longitud =arch.readInt();   //Lee numero de maestros

      for(int i=0; i<longitud; i++){  //LEE CADA MAESTRO
        maestroEnt=new MaestroArch(); // Crea objetos Maestro
        maestroEnt.cargarMaestro(arch);  // Asigna valores de archivo
        maestro = new Maestro();
        maestro.copiaDesde(maestroEnt);  // copia datos de MaestroEnt a Maestro
        System.out.println(maestro.toString());
        this.add(maestro);      // Lo agrega al vector
      }

    }
    catch(IOException e) {
      System.out.println("Error leyendo archivo de datos de maestros, "+e.getMessage());
    }
  }

  public int getIndexClave(String claveBuscada){
    String claveEnVect;
    int n =this.size();
    int cont;
    for(cont=0; cont<n; cont++){
      claveEnVect=((Maestro)this.get(cont)).getClave();
      claveEnVect=claveEnVect.substring(1,claveEnVect.length());

      if(claveEnVect.equals(claveBuscada))break;
    }
    if(cont==n){
      System.out.println("Maestro no encontrado en el archivo tabMaestros.dat");
      System.out.println("Clave Buscada = "+claveBuscada);
    }
    return cont;
  }


  public int getEmpalmes(){
    int suma=0;
    for(int i=0; i<this.size(); i++){
      suma += (int)((Maestro)this.get(i)).cuentaEmpalmes();
    }
    return suma;
  }

  /* Entrega el n�mero de horas continuas m�s de 3 que imparte un maestro*/
  public int getHrsContinuas(){
    int suma=0;
    for(int i=0; i<this.size(); i++){
      suma += (int)((Maestro)this.get(i)).horsContinuas(6);
    }
    return suma;
  }

  public void desasignaTodos(){
    int n=this.size();
    for(int i=0; i<n; i++){
      ((Maestro)this.get(i)).desasignaTodo();
    }
  }

}
