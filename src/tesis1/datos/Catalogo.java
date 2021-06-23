package tesis1.datos;

import java.util.Random;
import tesis1.genetic.*;

/**
 * Title:        Asignaci�n de horarios
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      ITSON
 * @author Adolfo Espinoza
 * @version 1.0
 */

public class Catalogo implements Cloneable{
  public VectorGrupos grupos;
  public VectorMaestros maestros;
  public VectorAulas aulas;
  public VectorSlotTime horas;
  public VectorSemCarrs semCarrs;
  public VectorMaterias materias;
  private Random genRand = new Random(4);
  private Costo costo = new Costo();

  public Catalogo(VectorGrupos grups, VectorMaestros maests,
                    VectorAulas auls, VectorSlotTime hors,
                    VectorSemCarrs seCs, VectorMaterias mats ){
    this.grupos=grups;
    this.maestros=maests;
    this.aulas=auls;
    this.horas=hors;
    this.semCarrs=seCs;
    this.materias = mats;
  }

  public Catalogo(String archGrps, String archMaests,
                    String archAuls, String archHors,
                    String archSeCs, String archMats){
    try {
      System.out.println("Lee Horarios....");
      /*Obtiene informaci�n de los tipos de horarios del archivo "archHors" */
      horas = new VectorSlotTime(archHors);

      System.out.println("Lee Maestros....");
      /*Obtiene informaci�n de los maestros del archivo "archMaests" */
      maestros = new VectorMaestros(archMaests);

      System.out.println("Lee Aulas....");
      /*Obtiene informaci�n de las aulas del archivo "archAuls" */
      aulas = new VectorAulas(archAuls);

      System.out.println("Lee Materias....");
      /*Obtiene informaci�n de las aulas del archivo "archMats" */
      materias = new VectorMaterias(archMats);

      System.out.println("Lee Grupos....");
      /*Obtiene informaci�n de los grupos del archivo "archGrps" */
      grupos = new VectorGrupos(archGrps, maestros);

      /*Asigna el tipo de aula que le corresponde a cada grupo*/
      grupos.asignaAulas(aulas, materias);

      /*Asigna el vector de SlotTimes v�lidos para cada grupo
      considerando tipos de horarios y disponibilidad de maestros*/
      grupos.asignaOpcsSlotTimes(horas, maestros, materias);

      System.out.println("Lee Semestres-Carreras....");
      /*Obtiene informaci�n de los grupos del archivo "archSeCs" */
      semCarrs = new VectorSemCarrs(archSeCs,grupos);
      semCarrs.setSlotsTime(horas);
    }catch(Exception e) {
    }
  }


  public Object clone(){
    VectorGrupos    gruposNuevo = (VectorGrupos)this.grupos.clone();
    VectorMaestros  maestrosNuevo = (VectorMaestros)this.maestros.clone();
    VectorAulas     aulasNuevo = (VectorAulas )this.aulas.clone();
    VectorSlotTime  horasNuevo = (VectorSlotTime)this.horas.clone();
    VectorSemCarrs  semCarrsNuevo= (VectorSemCarrs)this.semCarrs.clone() ;
    VectorMaterias  matsNuevo= (VectorMaterias)this.materias.clone() ;

    Catalogo catNuevo = new Catalogo( gruposNuevo, maestrosNuevo,
                                      aulasNuevo, horasNuevo,
                                      semCarrsNuevo, matsNuevo);
    return catNuevo;
  }

  public int getEmpalmesMaestros(){
    return maestros.getEmpalmes();
  }

  public int getEmpalmesAulas(){
    return aulas.getEmpalmes();
  }


  public int getBloqueaSemCarr(){
    return  semCarrs.getBloqueads();
  }


  public void imprimeCatalog(){
    int n=grupos.size();
    Grupo element;
    for(int i=0; i<n; i++){
      element= (Grupo)grupos.elementAt(i);
      Materia m = (Materia)materias.elementAt(materias.getIndexClave(element.getClave()));

      System.out.print( element.getClave() + ",  G" );
      System.out.print( element.getNum());
      System.out.print(",\t"+m.getDescrip()+",\t");

      int y = element.getMaestro();

      SlotTime s2 = (SlotTime)horas.elementAt(element.getHora());
      System.out.print( s2.getOffsetStringHora()+s2.getDescripcion()+",  ");

      System.out.print( ((Maestro)maestros.elementAt(y)).getNombre()+",\t\t");

      y=element.getAula();
      System.out.println( ((Aula)aulas.elementAt(y)).getClave() );

    }
  }

  public void asignaHorRandom(){
    for(int i=0; i<grupos.size(); i++){
      Grupo g = (Grupo)grupos.elementAt(i);
      int h = g.getOpcs()[genRand.nextInt(g.getOpcs().length)];
      asignaHora(i,h);
    }
  }

  public void cambiaHora(int grup, int sT){
    desasignaHora(grup);
    asignaHora(grup, sT);
  }

  /**
   * Asigna al grupo grup el horario sT. El maestro y el aula relacionados se
   * ocupan a esa hora.
     * @param grup
     * @param sT
   */
  public void asignaHora(int grup, int sT){
    int aul = ((Grupo)grupos.elementAt(grup)).getAula();
    int maes = ((Grupo)grupos.elementAt(grup)).getMaestro();
    ((Aula)aulas.elementAt(aul)).asigna((SlotTime)horas.elementAt(sT));
    ((Maestro)maestros.elementAt(maes)).asigna((SlotTime)horas.elementAt(sT));
    ((Grupo)grupos.elementAt(grup)).setHora(sT);
  }


  /**
   * Deasigna al grupo grup. El maestro y el aula relacionados se
   * les desocupa esa hora.
     * @param grup
   */
  public void desasignaHora(int grup){
    int sT = ((Grupo)grupos.elementAt(grup)).getHora();
    int aul = ((Grupo)grupos.elementAt(grup)).getAula();
    int maes = ((Grupo)grupos.elementAt(grup)).getMaestro();
    ((Aula)aulas.elementAt(aul)).desAsigna((SlotTime)horas.elementAt(sT));
    ((Maestro)maestros.elementAt(maes)).desAsigna((SlotTime)horas.elementAt(sT));
    //((Grupo)grupos.elementAt(grup)).setHora(null);
  }


  public void verificaDatos(){
    Grupo gpoAux;
    int cont=0;
    for(int i=0; i<grupos.size();i++){
      gpoAux=(Grupo)grupos.elementAt(i);

      if(gpoAux.getOpcs().length==0){
        System.out.println("No hay opciones para el grupo:");
        System.out.println(gpoAux.getClave()+"  "+
          ((Maestro)maestros.elementAt(gpoAux.getMaestro())).getNombre());
        cont++;
      }
    }
    if(cont>0){
      System.exit(0);
    }
  }

  public void borraAsignados(){
    this.maestros.desasignaTodos();
    this.aulas.desasignaTodos();
  }

  public Individuo getIndividuo(){
    return grupos.getIndividuo();
  }

  public Individuo getIndividuo2(){
    VectorGrupos gps=this.grupos;
    int nGps=gps.size(),hora;

    Individuo indAux = new Individuo(nGps);
    int llave=0;
    for(int i=0; i<nGps; i++){
      hora=((Grupo)gps.elementAt(i)).getHora();
      llave=llave+hora;
      indAux.setGene(i,hora);
    }
    indAux.setCosto((Costo)this.costo.clone());
    indAux.setLlave(llave);
    return indAux;
  }



  /** Asigna al catalogo de grupos el horario dado por los genes de "Individuo
     * @param indiv"*/
  public void asignaIndividuo(Individuo indiv){
    for(int i=0; i<grupos.size(); i++){
      this.asignaHora(i,indiv.getGenes()[i]);
    }
  }


  public Poblacion mejorVecinoTotal(int nVecinos){
    Individuo indActual;
    Grupo gSel;
    Poblacion vecinos= new Poblacion();
    int stOrig, stProp;

    for(int g=0; g<this.grupos.size(); g++){
      gSel=(Grupo)this.grupos.elementAt(g);
      stOrig = gSel.getHora(); //horario original del grupo seleccionado
      //Propone un horario nuevo al grupo seleccionado
      for(int h=0; h<gSel.getOpcs().length; h++){
        stProp=gSel.getOpcs()[h];
        if(stProp == stOrig) continue;
        this.cambiaHora(g,stProp);
//indActual=this.getIndividuo();
//indActual.evalua(this);
this.evaluaCosto();
indActual=this.getIndividuo2();

        this.cambiaHora(g,stOrig);

        for(int j=0; j<nVecinos; j++){
          if(j >= vecinos.size()){
            vecinos.add(indActual);
            break;
          }
          Individuo aux=(Individuo)vecinos.elementAt(j);
          if(indActual.getCosto().getCostoTotal() < aux.getCosto().getCostoTotal()){
            vecinos.add(j,indActual);
            break;
          }
        }//for j
        if(vecinos.size()==nVecinos+1){
          vecinos.removeElementAt(nVecinos);
        }
      }//for h
    }//for g
    return vecinos;
  }

  public Poblacion VecinosRnd(int nVecinos){
    Grupo gSel;
    Poblacion vecinos= new Poblacion();
    int gpoProp, stOrig, stNueva;
    Individuo indOrig = this.getIndividuo(), indActual;
    indOrig.evalua(this);

    int nGps=this.grupos.size();
    for(int i=0; i<nVecinos; i++){
      gpoProp=genRand.nextInt(nGps);
      gSel=(Grupo)this.grupos.elementAt(gpoProp);

      stOrig = gSel.getHora(); //horario original del grupo seleccionado
      //Propone un horario nuevo al grupo seleccionado
      stNueva=gSel.getOpcs()[genRand.nextInt(gSel.getOpcs().length)];

      this.cambiaHora(gpoProp,stNueva);
      indActual=this.getIndividuo();
      indActual.evalua(this);

      this.cambiaHora(gpoProp,stOrig);

      for(int j=0; j<nVecinos; j++){
        if(j >= vecinos.size()){
          vecinos.add(indActual);
          break;
        }
        Individuo aux=(Individuo)vecinos.elementAt(j);
        if(indActual.getCostoTotal() < aux.getCostoTotal()){
          vecinos.add(j,indActual);
          break;
        }
      }//for j
      if(vecinos.size()==nVecinos+1){
        vecinos.removeElementAt(nVecinos);
      }
    }//for i
    return vecinos;
  }



// Se va a eliminar con la clase Costo
  public int costo(){
    int empAulas =  this.aulas.getEmpalmes(); //medias horas empalmadas
    int empMaestro = this.maestros.getEmpalmes();//medias horas empalmadas
    int empGrpsSC = this.semCarrs.getBloqueads(); /*grupos que no estan en una
                                      trayectorioa de horario v�lido*/
    int turnos = this.semCarrs.costoTurnos();
    int hrsContinuas = this.maestros.getHrsContinuas();

    return empAulas + empMaestro + empGrpsSC + hrsContinuas + turnos;
  }



  public Costo evaluaCosto(){
    this.costo.setEmpMaestros(this.maestros.getEmpalmes());//medias horas empalmadas
    this.costo.setEmpAulas(this.aulas.getEmpalmes());//medias horas empalmadas
    this.costo.setSemCarrs(this.semCarrs.getBloqueads());//grupos que no estan en una
                                      //trayectorioa de horario v�lido
    this.costo.setHrsContinuas(this.maestros.getHrsContinuas());
    this.costo.setTurnos(this.semCarrs.costoTurnos());
    return (Costo)costo.clone();
  }

  public Costo getCosto(){
    return (Costo)costo.clone();
  }

}
