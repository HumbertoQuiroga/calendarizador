package tesis1.datos;
import java.util.Vector;
import tesis1.io.*;
import tesis1.genetic.*;

/**
 * Title:        Asignacion de horarios
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      ITSON
 * @author Adolfo Espinoza
 * @version 1.0
 */

public class VectorGrupos extends Vector implements Cloneable{

  public VectorGrupos(String archGrupos, VectorMaestros maestros) {
    super(1,1);
    leeArchGrupos(archGrupos, maestros);
  }
   public VectorGrupos() {
    super(1,1);
  }

  public Object clone(){
    VectorGrupos aux = new VectorGrupos();
    int nGrps=this.size();
    for(int i=0; i<nGrps; i++){
      aux.addElement( ((Grupo)this.elementAt(i)).clone() );
    }
    return aux;
  }

  private void leeArchGrupos(String arch, VectorMaestros maestros){
     //agregado por HQ 
    arch = "C:\\Users\\hq82\\Documents\\NetBeansProjects\\Calendarizador\\real\\tabGrupos.txt"; 
     
    String params [];
    int numParams;
    ParamFileRead grps = new ParamFileRead(arch);
    boolean eof=false;
    while(null!=(params = grps.readParams()) ){//Mientras no termine el archivo
      numParams = params.length;
      if (numParams==3){
        Grupo buff = new Grupo();
        buff.setClave( params[0] );
        buff.setNum( Integer.parseInt( params[1]) );

        //Coloca el indice de la clave en el atributo "maestro" del
        //objeto "grupo".
        buff.setMaestro( maestros.getIndexClave(params[2]) );

        this.addElement(buff); // agrega nuevo grupo al vector
      }
    }
  }

/**
 * Coloca el indice del aula en la tabla aulas lo coloca en el atributo
 * aula del grupo.
 */
  public void asignaAulas(VectorAulas aulas, VectorMaterias materias){

    int nGrups = this.size();
    for(int i=0; i<nGrups; i++){
      String mat = ((Grupo)this.elementAt(i)).getClave();
      int indxMat = materias.getIndexClave(mat);
      String aul=((Materia)materias.elementAt(indxMat)).getTipoAula();
      int indxAula = aulas.getIndexClave(aul);
      ((Grupo)this.elementAt(i)).setAula(indxAula);
    }
  }

/**
 * Asigna el vector de horarios factibles para cada grupo. Todos los horarios
 * que se le pueden asignar a ese tipo de materia y que caen dentro del horario
 * factible del maestro.
 */
  public void asignaOpcsSlotTimes(VectorSlotTime horas,
                          VectorMaestros maestros, VectorMaterias materias){

    int numGrups = this.size();

    for(int g=0; g<numGrups; g++){
      Grupo grup = (Grupo)this.elementAt(g);
      Maestro maest = (Maestro)maestros.elementAt(grup.getMaestro());

      String claveMat=((Grupo)this.elementAt(g)).getClave();
      int indxMat = materias.getIndexClave(claveMat);
      Materia mate = (Materia) materias.elementAt(indxMat);

      grup.setOpcs( horas.getHorsFacts(mate, maest) );
    }
  }

  public Individuo getIndividuo(){
    Individuo aux = new Individuo(this.size());
    for(int i=0; i<this.size(); i++){
      aux.setGene(i,((Grupo)this.elementAt(i)).getHora());
    }
    return aux;
  }

}