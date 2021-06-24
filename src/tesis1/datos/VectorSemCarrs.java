package tesis1.datos;

import java.util.Vector;
import tesis1.io.*;

/**
 * Title:        Asignacion de horarios
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      ITSON
 * @author Adolfo Espinoza
 * @version 1.0
 */

public class VectorSemCarrs extends Vector implements Cloneable{

  private VectorGrupos grupos; //apuntador a vector de grupos

  public VectorSemCarrs(String archGrupos, VectorGrupos grps) {
    super(1,1);
    leeArchSemCarrs(archGrupos, grps);
    cuentaGrupos(grps);
    grupos=grps;
  }
   public VectorSemCarrs() {
    super(1,1);
  }


  //public Object clone(){
    //VectorGrupos aux = new VectorGrupos();
    //int nGrps=this.size();
    //for(int i=0; i<nGrps; i++){
    //  aux.addElement( ((Grupo)this.elementAt(i)).clone() );
    //}
    //return aux;
  //}

  private void leeArchSemCarrs(String arch, VectorGrupos grupos ){
      //agregado por HQ
   //arch = "C:\\Users\\hq82\\Documents\\NetBeansProjects\\Calendarizador\\real\\tabSemCarr.txt";
      
    String params [];
    int numParams;
    ParamFileRead grps = new ParamFileRead(arch);
    boolean eof=false;
    while(null!=(params = grps.readParams()) ){//Mientras no termine el archivo
      numParams = params.length;
      if (numParams==4){ // semCarr, claveMateria, IN, OUT
        SemCarrera semCarr = this.getSemCarrConClave(params[0]);
        if(semCarr==null){// Se crea un semestreCarrera si no existia
          semCarr = new SemCarrera(params[0],grupos);
          this.add(semCarr);
        }

        // Agregamos rama al semestreCarrera
        for(int j=0; j<grupos.size(); j++){
          if(((Grupo)grupos.elementAt(j)).getClave().equals(params[1])){
          // Se agrega un grupo al semestre carrera
            ElementoDeSemCarr elementoDeSemCarr =  new ElementoDeSemCarr(j,  Integer.parseInt(params[2]),Integer.parseInt(params[3]));
              System.out.println(elementoDeSemCarr.toString());
            semCarr.add(elementoDeSemCarr);
          }
        }//end for j
      }// if numParams
    }//end while
  }

  private SemCarrera getSemCarrConClave(String nombre){
  // Busca en el vector de semestres carrera alguno que su nombre sea igual
  // al encontrado en el archivo de datos. Entrega la referencia a este o
  // "null" si no hay ninguno
    int n = this.size();
    SemCarrera salida = null;
    SemCarrera aux;
    for(int i=0; i<n; i++){
      aux = ((SemCarrera)this.elementAt(i));
      if(aux.getNombre().equals(nombre)){salida=aux;}
    }
    return salida;
  }

  private void cuentaGrupos(VectorGrupos grupos){
  /* Cuenta el numero de grupos de cada materia y en cada semestre carrera
  y lo asigan al atributo ElementoDeSemCarr.n */
    SemCarrera sc;
    for(int s=0; s<this.size(); s++){
    // sc = semestre carrera,  r=elemento de semestre carrera que se compara con
    // todos los de mas (b)
      sc = ((SemCarrera)this.elementAt(s));
      for(int r=0; r<sc.size()-1; r++){
        int grupoR, grupoB;
        for( int b=r+1; b < sc.size();  b++){
          //Si los dos grupos tienen la misma clave incrementa cont
          grupoR = ((ElementoDeSemCarr)sc.elementAt(r)).grupo;
          grupoB = ((ElementoDeSemCarr)sc.elementAt(b)).grupo;
          if( ((Grupo)grupos.elementAt(grupoR)).getClave().equals(
                  ((Grupo)grupos.elementAt(grupoB)).getClave()   )  ){
            (((ElementoDeSemCarr)sc.elementAt(r)).n)++;
            (((ElementoDeSemCarr)sc.elementAt(b)).n)++;
          }
        }// end for b
      }// end for r
    }// end for s
  }// end cuentaGrupos


  public void muestra(VectorGrupos grupos, VectorSlotTime vslt){
  /* Muestra todos los elementos de todos los semestres carrera */
    SemCarrera sc;
    for(int s=0; s<this.size(); s++){
    // sc = semestre carrera,  r=elemento de semestre carrera
      sc = ((SemCarrera)this.elementAt(s));

      sc.calculaTraysValidas();

      System.out.println("\nSemestre Carrera ="+sc.getNombre());
      for(int r=0; r<sc.size(); r++){
        int gp;
        System.out.print(gp=((ElementoDeSemCarr)sc.elementAt(r)).grupo);
        System.out.print(", \t");
        System.out.print(((Grupo)grupos.elementAt(gp)).getHora());
        System.out.print(", \t");
        System.out.print( ((SlotTime)vslt.get(((Grupo)grupos.elementAt(gp)).getHora())).getOffsetStringHora()+
                          ((SlotTime)vslt.get(((Grupo)grupos.elementAt(gp)).getHora())).getDescripcion()  );
        System.out.print(", \t");
        System.out.print(((Grupo)grupos.elementAt(gp)).getClave());
        System.out.print(", \t");
        System.out.print(((ElementoDeSemCarr)sc.elementAt(r)).in);
        System.out.print(", ");
        System.out.print(((ElementoDeSemCarr)sc.elementAt(r)).out);
        System.out.print(", ");
        System.out.print(((ElementoDeSemCarr)sc.elementAt(r)).n);
        System.out.print(",    ");
        System.out.print(sc.trayectoriasValidas[r]);
        System.out.print(",    ");
        System.out.print(sc.getBloquea());
        System.out.print(",    ");
        System.out.print(sc.totTrays);
        System.out.print(",    mat=");
        System.out.print(sc.traysMatut);
        System.out.print(",    vesper=");
        System.out.println(sc.traysVesper);

      }// end for r
    }// end for s
  }// end cuentaGrupos


  public void setSlotsTime(VectorSlotTime horas){
    for(int i=0; i<this.size(); i++){
      ((SemCarrera)this.elementAt(i)).setSlotsTime(horas);
    }
  }


  public int getBloqueads(){
    int suma=0;
    for(int i=0;i<this.size();i++){
      suma += ((SemCarrera)this.elementAt(i)).getBloquea();
    }
    return suma;
  }

  public int costoTurnos(){
    /*int matus=0, vesps=0, t=0, s=0,suma=0;
    for(int i=0;i<this.size();i++){
      matus=((SemCarrera)this.elementAt(i)).traysMatut;
      matus=(matus>=5)?5:matus;
      vesps=((SemCarrera)this.elementAt(i)).traysVesper;
      vesps=(vesps>=5)?5:vesps;
      t=((SemCarrera)this.elementAt(i)).totTrays;
      s=matus+vesps;
      suma += s;
    }
    return ((suma)<=0)?0:suma;
      */
    int matus=0, vesps=0, t=0, s=0,suma=0, n;
    SemCarrera scAux;
    for(int i=0;i<this.size();i++){
      scAux=(SemCarrera)this.elementAt(i);
      n = scAux.size();
      for(int j=0; j<n; j++){
        if(scAux.tMatu[j] + scAux.tVesp[j] == 0) suma++;
      }
    }
    return suma;
  }

}