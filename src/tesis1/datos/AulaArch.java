package tesis1.datos;
import java.io.*;
/**
 * Title:        Asignaci�n de horarios
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      ITSON
 * @author Adolfo Espinoza
 * @version 1.0
 */

public class AulaArch {
  private String  clave, descripcion;
  public HorarioSemana horario= new HorarioSemana();

  public AulaArch() {
  }

  public AulaArch(String clv, String des) {
        setClaveYDescrip(clv,des);
  }

  public void guardarAula(DataOutput archSalida){
    try{
      archSalida.writeBytes(this.clave);
      archSalida.writeByte(0);
      archSalida.writeBytes(this.descripcion);
      archSalida.writeByte(0);
      horario.guardarHorario(archSalida);
    }
    catch(IOException e) {
      System.out.print("\n error de archivo salida Aulas\n");
    }
  }

  public void cargarAula(DataInput arch){
  // Retorna un objeto de la clase Maestro leido del archivo
    try{
      this.clave = readString(arch);
      this.descripcion = readString(arch);
      this.horario.cargarHorario(arch);
    }
    catch(Exception e) {
      System.out.print("\n error de lectura en archivo Aulas\n");
    }
  }

  public void setClaveYDescrip(String clv, String des){
    this.clave= clv;
    this.descripcion=des;
  }


  public String getClaveYDescrip(){
    return this.clave+"  "+this.descripcion;
  }

  public String getClave(){
    return this.clave;
  }

  public String getDescrip(){
    return this.descripcion;
  }

  public Object clone(){
    AulaArch nuevaAula=new AulaArch( this.getClave(),this.getDescrip());
    //nuevaAula.horario.setHorario(this.horario.getHorario());
    return nuevaAula;
  }

  public void copiarAula(AulaArch original){
    this.setClaveYDescrip(original.getClave(),original.getDescrip());
    this.horario.setHorario((HorarioSemana)original.horario.clone());
  }

  public void copiarClavYDescrip(AulaArch original){
     this.setClaveYDescrip(original.getClave(),original.getDescrip());
  }

   private String readString(DataInput arch){
    char[] bufCadena =  new char[20];
    int apChar=0;
    try{
      do{
        bufCadena[apChar++] = (char)arch.readByte();
      }while(bufCadena[apChar-1]!=0);
    }
    catch(IOException e) {
      System.out.print("\n error de archivo entrada\n");
    }
    return new String(bufCadena,0,apChar-1);
  }


}

