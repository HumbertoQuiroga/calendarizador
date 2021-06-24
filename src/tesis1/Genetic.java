package tesis1;

import tesis1.datos.*;
import tesis1.genetic.*;
import java.util.Random;

public class Genetic {

    private Random genRandPP = new Random(4);

    /**
     * Construct the application
     */
    public Genetic() {

        try {

            genetico1();
            //genetico2();
            //hillclimbing();
            //annealing();
            //tabu();

        } catch (Exception e) {
        }
    }

    /**
     * Main metho
     */
    public static void main(String[] args) {
        Genetic ag = new Genetic();

        //ag.genetico1();

    }

    /**
     * *************************************************************************
     * Rutinas para pruebas
     */
    private void testImprimeSlotsTime(VectorSlotTime horas) {
        //Imprime todos los slotTime`s
        int a, b;
        int cpa = horas.size();
        for (a = 0; a < cpa - 1; a++) {
            for (b = a + 1; b < cpa; b++) {
                System.out.print(((SlotTime) horas.get(a)).getOffsetStringHora());
                System.out.print(((SlotTime) horas.get(a)).getDescripcion() + "   ");
                System.out.print(((SlotTime) horas.get(b)).getOffsetStringHora());
                System.out.println(((SlotTime) horas.get(b)).getDescripcion());

                if (((SlotTime) horas.get(a)).
                        isEmpalme((SlotTime) horas.get(b))) {
                    System.out.println("Empalme");
                } //then "empalme"
                else {
                    System.out.println("No Empalme");
                }   //else "no empalme"

                //imprime matrices;
                testImprimeMatrizSlotTime(((SlotTime) horas.get(a)));
                testImprimeMatrizSlotTime(((SlotTime) horas.get(b)));
            }
        }
    }

    private void testImprimeMatrizSlotTime(SlotTime st) {
        int[][] ar1 = st.getMascaraArray();
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 5; c++) {
                System.out.print(ar1[r][c]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * *****************************************************************
     */
    private void hillclimbing() {
        Catalogo cat1 = new Catalogo("tabGrupos.txt", "tabMaes.dat",
                "tabAulas.dat", "tipoHors.txt",
                "tabSemCarr.txt", "tabMaterias.txt");
        cat1.verificaDatos();

        cat1.asignaHorRandom();

        cat1 = hillclimbing(cat1, 0);

        cat1.imprimeCatalog();
        cat1.semCarrs.muestra(cat1.grupos, cat1.horas);

        VectorAulas va = cat1.aulas;
        for (int i = 0; i < va.size(); i++) {
            int empalm = ((Aula) cat1.aulas.elementAt(i)).cuentaEmpalmes();
            if (empalm != 0) {
                System.out.print("Empalme de aula ");
                System.out.print(((Aula) va.elementAt(i)).getClave() + "  ");
                System.out.println(empalm);
            }
        }
        VectorMaestros vm = cat1.maestros;
        for (int i = 0; i < vm.size(); i++) {
            int empalm = ((Maestro) vm.elementAt(i)).cuentaEmpalmes();
            if (empalm != 0) {
                System.out.print("Empalme de maestro ");
                System.out.print(((Maestro) vm.elementAt(i)).getNombre() + "  ");
                System.out.println(empalm);
            }
        }

    }

    private Catalogo hillclimbing(Catalogo cat1, int costoSalida) {

        int ciclos = 0;
        Grupo gSel; //Grupo seleccionado para hacer cambio
        int stPropu;//horario seleccionado
        int stOrig;
        int indxGpo;
        int costoInic, costoFinal, cambios = 0, cicloUltimo = 0;
        int costoMinimo = 100000;
        boolean finProceso = false;

        costoInic = cat1.evaluaCosto().getCostoTotal();

        do {
            //Selecciona un grupo

            indxGpo = genRandPP.nextInt(cat1.grupos.size());
            gSel = (Grupo) cat1.grupos.elementAt(indxGpo);
            stOrig = gSel.getHora(); //horario del grupo seleccionado

            //Propone un horario nuevo al grupo seleccionado
            stPropu = gSel.getOpcs()[genRandPP.nextInt(gSel.getOpcs().length)];

            cat1.cambiaHora(indxGpo, stPropu);
            costoFinal = cat1.evaluaCosto().getCostoTotal();

            ciclos++;
            if ((costoFinal > costoInic)) {
                cat1.cambiaHora(indxGpo, stOrig);
            } else {
                costoInic = costoFinal;
                cambios++;
            }

            if (ciclos % 100 == 0) {
                //this.imprimeCosto(cat1);
                //System.out.print("ciclos=");

                System.out.print(ciclos + ",  ");
                cat1.evaluaCosto().imprimeCosto();

            }
            if ((ciclos >= 50000) || (costoInic <= costoSalida)) {
                finProceso = true;
            }

            if ((costoInic == costoMinimo) && (ciclos - cicloUltimo > 5000)) {
                cicloUltimo = ciclos;
                System.out.print(ciclos);
                System.out.print(", ");
                System.out.print(costoInic);
                System.out.print("\n");
                costoMinimo = costoInic;
                System.out.print("****************");
                System.out.print("\n");
                // cat1.imprimeCatalog();
                //cat1.semCarrs.muestra(cat1.grupos, cat1.horas);
                System.out.print("****************");
                System.out.print("\n");
            }

        } while (!finProceso);

        return cat1;
    }

    /**
     * *****************************************************************
     */
    private void annealing() {
        Catalogo cat1 = new Catalogo("tabGrupos.txt", "tabMaes.dat",
                "tabAulas.dat", "tipoHors.txt",
                "tabSemCarr.txt", "tabMaterias.txt");
        cat1.verificaDatos();
        cat1.asignaHorRandom();
        cat1 = annealing(cat1, 0);
        cat1.imprimeCatalog();
        cat1.semCarrs.muestra(cat1.grupos, cat1.horas);

        VectorAulas va = cat1.aulas;
        for (int i = 0; i < va.size(); i++) {
            int empalm = ((Aula) cat1.aulas.elementAt(i)).cuentaEmpalmes();
            if (empalm != 0) {
                System.out.print("Empalme de aula ");
                System.out.print(((Aula) va.elementAt(i)).getClave() + "  ");
                System.out.println(empalm);
            }
        }
        VectorMaestros vm = cat1.maestros;
        for (int i = 0; i < vm.size(); i++) {
            int empalm = ((Maestro) vm.elementAt(i)).cuentaEmpalmes();
            if (empalm != 0) {
                System.out.print("Empalme de maestro ");
                System.out.print(((Maestro) vm.elementAt(i)).getNombre() + "  ");
                System.out.println(empalm);
            }
        }

    }

    /**
     * *********************************************************************
     */
    private Catalogo annealing(Catalogo cat1, int costoSalida) {

        int ciclos = 0;
        Grupo gSel; //Grupo seleccionado para hacer cambio
        int stPropu;//horario seleccionado
        int stOrig;
        int indxGpo;
        int costoInic, costoFinal, cambios = 0, cicloUltimo = 0;
        int costoMinimo = 100000;
        boolean finProceso = false;
        double temp = 1.5;
        double factEnfr = 0.98;
        int ENFR = 2000;
        double incrCosto;

        cat1.evaluaCosto();
        Individuo mejorOpcionGlobal = cat1.getIndividuo2();
        costoInic = cat1.getCosto().getCostoTotal();

        do {

            //Selecciona un grupo
            indxGpo = genRandPP.nextInt(cat1.grupos.size());
            gSel = (Grupo) cat1.grupos.elementAt(indxGpo);
            stOrig = gSel.getHora(); //horario del grupo seleccionado
            //Propone un horario nuevo al grupo seleccionado
            stPropu = gSel.getOpcs()[genRandPP.nextInt(gSel.getOpcs().length)];

            cat1.cambiaHora(indxGpo, stPropu);
            costoFinal = cat1.evaluaCosto().getCostoTotal();

            if (costoFinal <= costoInic) {//mejor costo
                costoInic = costoFinal;
                cambios++;

                if (costoFinal <= mejorOpcionGlobal.getCosto().getCostoTotal()) {
                    mejorOpcionGlobal = cat1.getIndividuo2();
                }
            } else {// Tomar la nueva solucion probabilisticamente
                incrCosto = costoFinal - costoInic;

                //if(false){                                               //hillclimbing
                if (genRandPP.nextDouble() <= Math.exp(-incrCosto / temp)) { //annealing
                    costoInic = costoFinal;
                } else {
                    cat1.cambiaHora(indxGpo, stOrig);
                }
            }

            if (ciclos % 100 == 0) {
                System.out.print(ciclos + ",  ");
                cat1.evaluaCosto().imprimeCosto();
                System.out.println("mejor opci�n =" + mejorOpcionGlobal.getCosto().getCostoTotal() + ",  cambios =" + cambios);
                cambios = 0;
            }

            ciclos++;

            //Actualiza Temperatura
            if (ciclos % ENFR == 0) {
                temp = temp * factEnfr;
            }

        } while (!finProceso);

        return cat1;
    }

    /**
     * *********************************************************************
     */
    private void genetico1() {
        int POBLMIN = 400;
        int POBLMAX = 800;
        int ELITE = (int) (POBLMAX * 0.1);
        double FACTMUT = 0.05;
        int indxElem1, indxElem2, pivote1, pivote2;
        Individuo elem1, elem2, nuevo1, nuevo2;
        boolean finProceso = false;
        int camada = 0;

        //Lee datos de entrada
        Catalogo cat1 = new Catalogo(".\\real\\tabGrupos.txt", 
                                     ".\\real\\tabMaes.dat",
                                     ".\\real\\tabAulas.dat", 
                                     ".\\real\\tipoHors.txt",
                                     ".\\real\\tabSemCarr.txt", 
                                     ".\\real\\tabMaterias.txt");

        //Genera poblaci�n inicial
        Poblacion pob = new Poblacion();
        Individuo indAux;
        for (int i = 0; i < POBLMIN; i++) {
            cat1.borraAsignados();
            cat1.asignaHorRandom();

            /*
            indAux = cat1.getIndividuo();
            indAux.evalua(cat1);
             */
            cat1.evaluaCosto();
            indAux = cat1.getIndividuo2();

            pob.add(indAux);
        }

        do {
            //Mutacion
            //System.out.print("Mutaci�n,  ");

            int gpoIndx, opcIndx, selec;
            Grupo gpoAux;
            Individuo indiviNuevo;
            int tamañoInic = pob.size();
            for (int i = 0; i < tamañoInic; i++) {
                if (genRandPP.nextInt(10000) <= FACTMUT * 10000) {
                    indiviNuevo = (Individuo) ((Individuo) pob.elementAt(i)).clone();
                    gpoIndx = genRandPP.nextInt(indiviNuevo.getGenes().length);
                    gpoAux = (Grupo) cat1.grupos.elementAt(gpoIndx);
                    selec = gpoAux.getOpcs()[genRandPP.nextInt(gpoAux.getOpcs().length)];
                    indiviNuevo.setGene(gpoIndx, selec);

                    /*
                        indiviNuevo.evalua(cat1);
                     */
                    cat1.borraAsignados();
                    cat1.asignaIndividuo(indiviNuevo);
                    cat1.evaluaCosto();
                    indiviNuevo = cat1.getIndividuo2();

                    pob.add(indiviNuevo);
                }
            }

            //Cruzamiento
            //System.out.print("Cruza,  ");
            int tamaño = cat1.grupos.size();
            do {
                indxElem1 = genRandPP.nextInt(pob.size());
                do {
                    indxElem2 = genRandPP.nextInt(pob.size());
                } while (indxElem1 == indxElem2);
                elem1 = ((Individuo) pob.elementAt(indxElem1));
                elem2 = ((Individuo) pob.elementAt(indxElem2));
                nuevo1 = (Individuo) elem1.clone();
                nuevo2 = (Individuo) elem2.clone();

                pivote1 = genRandPP.nextInt(tamaño);
                do {
                    pivote2 = genRandPP.nextInt(tamaño);
                } while (pivote1 == pivote2);
                int inic = (pivote1 < pivote2) ? pivote1 : pivote2;
                int fin = (pivote1 > pivote2) ? pivote1 : pivote2;

                for (int i = inic; i <= fin; i++) {
                    nuevo1.setGene(i, elem2.getGenes()[i]);
                    nuevo2.setGene(i, elem1.getGenes()[i]);
                }

                /*
                nuevo1.evalua(cat1);
                 */
                cat1.borraAsignados();
                cat1.asignaIndividuo(nuevo1);
                cat1.evaluaCosto();
                nuevo1 = cat1.getIndividuo2();

                /*
                nuevo2.evalua(cat1);
                 */
                cat1.borraAsignados();
                cat1.asignaIndividuo(nuevo2);
                cat1.evaluaCosto();
                nuevo2 = cat1.getIndividuo2();

                pob.add(nuevo1);
                pob.add(nuevo2);

            } while (pob.size() < POBLMAX);

            //Seleccion
            //System.out.println("Selecciona,  ");
            pob.ordenaMenMayor();

            int costo1, costo2;
            do {
                //Ruleta

                //Torneo
                indxElem1 = genRandPP.nextInt(pob.size() - ELITE) + ELITE;
                indxElem2 = genRandPP.nextInt(pob.size() - ELITE) + ELITE;

                /*
                costo1=((Individuo)pob.elementAt(indxElem1)).getCostoTotal();
                costo2=((Individuo)pob.elementAt(indxElem2)).getCostoTotal();
                 */
                costo1 = ((Individuo) pob.elementAt(indxElem1)).getCosto().getCostoTotal();
                costo2 = ((Individuo) pob.elementAt(indxElem2)).getCosto().getCostoTotal();

                if (costo1 > costo2) {
                    pob.remove(indxElem1);
                } else if (costo1 < costo2) {
                    pob.remove(indxElem2);
                }
                if (costo1 == costo2) {
                    if (genRandPP.nextInt(2) == 0) {
                        pob.remove(indxElem1);
                    } else {
                        pob.remove(indxElem2);
                    }
                }
            } while (pob.size() > POBLMIN);

            int minimo = 100000;
            int maximo = 0;
            int promedio = 0;
            int indxMinimo = 0;
            int costo = 0;
            for (int i = 0; i < pob.size(); i++) {

                /*
                costo=((Individuo)pob.elementAt(i)).getCostoTotal();
                 */
                costo = ((Individuo) pob.elementAt(i)).getCosto().getCostoTotal();

                if (costo < minimo) {
                    minimo = costo;
                    indxMinimo = i;
                }
                //minimo= (costo<minimo)? costo : minimo;

                maximo = (costo > maximo) ? costo : maximo;
                promedio += costo;
            }
            promedio = promedio / pob.size();
            System.out.print(camada++ + ",   " + minimo + ", " + promedio + ", " + maximo + ",   ");

            ((Individuo) pob.elementAt(indxMinimo)).getCosto().imprimeCosto();

            if (camada % 10 == 0) {
                System.out.print(",  ini= " + pob.size());
                pob.eliminaRepetidos();
                System.out.println(",  fin= " + pob.size());
            }//else  System.out.println();

            if (minimo <= 42) {
                finProceso = true;
            }
        } while (!finProceso);

        for (int i = 0; i < 100; i++) {
            indAux = (Individuo) pob.elementAt(i);
            System.out.print(i + ",   ");
            for (int j = 0; j < indAux.getGenes().length; j++) {
                System.out.print(indAux.getGenes()[j] + ", ");
            }
            System.out.println();
        }

    }// end geneticol()

    /**
     * *********************************************************************
     */
    /**
     * *********************************************************************
     */
// variacion al algoritmo para que quede como recomnendo el asesor
// poblacion nueva a partir de seleccion, mutaci�n antes de cruzamiento del
//individuo
    private void genetico2() {
        int POBLMIN = 800;
        int POBLMAX = 800;
        int ELITE = (int) (POBLMAX * 0.1);
        double FACTMUT = 0.1;
        int indxElem1, indxElem2, pivote1, pivote2;
        Individuo elem1, elem2, nuevo1, nuevo2;
        boolean finProceso = false;
        int camada = 0;

        //Lee datos de entrada
        Catalogo cat1 = new Catalogo("tabGrupos.txt", "tabMaes.dat",
                "tabAulas.dat", "tipoHors.txt",
                "tabSemCarr.txt", "tabMaterias.txt");

        //Genera poblaci�n inicial
        Poblacion pob = new Poblacion();
        Individuo indAux;
        for (int i = 0; i < POBLMIN; i++) {
            cat1.borraAsignados();
            cat1.asignaHorRandom();

            /*
indAux = cat1.getIndividuo();
indAux.evalua(cat1);
             */
            cat1.evaluaCosto();
            indAux = cat1.getIndividuo2();

            pob.add(indAux);
        }

        Poblacion pobNueva;
        do {

            pob.ordenaMenMayor();
            pobNueva = new Poblacion();
            // incluye elite a la proxima generaci�n
            for (int i = 0; i < ELITE; i++) {
                if (!pobNueva.contiene((Individuo) pob.elementAt(i))) {
                    pobNueva.add((Individuo) pob.elementAt(i));
                }
            }

            //Mutacion
            //System.out.print("Mutaci�n,  ");

            /*

      int gpoIndx, opcIndx, selec;
      Grupo gpoAux;
      Individuo indiviNuevo;
      int tama�oInic = pob.size();
      for(int i=0; i<tama�oInic; i++){
        if(genRandPP.nextInt(10000)<=FACTMUT*10000){
          indiviNuevo=(Individuo)((Individuo)pob.elementAt(i)).clone();
          gpoIndx=genRandPP.nextInt(indiviNuevo.getGenes().length);
          gpoAux=(Grupo)cat1.grupos.elementAt(gpoIndx);
          selec=gpoAux.getOpcs()[genRandPP.nextInt(gpoAux.getOpcs().length)];
          indiviNuevo.setGene(gpoIndx,selec);

//indiviNuevo.evalua(cat1);

cat1.borraAsignados();
cat1.asignaIndividuo(indiviNuevo);
cat1.evaluaCosto();
indiviNuevo=cat1.getIndividuo2();

          pob.add(indiviNuevo);
        }
      }
             */
            //Mutacion, Cruzamiento y seleccion
            //System.out.print("Cruza,  ");
            int tamaño = cat1.grupos.size();
            do {

                //Arreglo de datos temporal para sacar las mejores cruzas.
                Poblacion pobAux = new Poblacion();

                indxElem1 = genRandPP.nextInt(pob.size());
                do {
                    indxElem2 = genRandPP.nextInt(pob.size());
                } while (indxElem1 == indxElem2);
                elem1 = ((Individuo) pob.elementAt(indxElem1));
                elem2 = ((Individuo) pob.elementAt(indxElem2));

                pobAux.add(elem1); //se agregan como candidatos a la pr�xima generacion
                pobAux.add(elem2);

//MUTACION
                int gpoIndx, opcIndx, selec;
                Grupo gpoAux;
                Individuo indiviNuevo;

                if (genRandPP.nextInt(10000) <= FACTMUT * 10000) {
                    indiviNuevo = (Individuo) elem1.clone();
                    gpoIndx = genRandPP.nextInt(indiviNuevo.getGenes().length);
                    gpoAux = (Grupo) cat1.grupos.elementAt(gpoIndx);
                    selec = gpoAux.getOpcs()[genRandPP.nextInt(gpoAux.getOpcs().length)];
                    indiviNuevo.setGene(gpoIndx, selec);

                    cat1.borraAsignados();
                    cat1.asignaIndividuo(indiviNuevo);
                    cat1.evaluaCosto();
                    elem1 = cat1.getIndividuo2();
                    pobAux.add(elem1);
                }

                if (genRandPP.nextInt(10000) <= FACTMUT * 10000) {
                    indiviNuevo = (Individuo) elem2.clone();
                    gpoIndx = genRandPP.nextInt(indiviNuevo.getGenes().length);
                    gpoAux = (Grupo) cat1.grupos.elementAt(gpoIndx);
                    selec = gpoAux.getOpcs()[genRandPP.nextInt(gpoAux.getOpcs().length)];
                    indiviNuevo.setGene(gpoIndx, selec);

                    cat1.borraAsignados();
                    cat1.asignaIndividuo(indiviNuevo);
                    cat1.evaluaCosto();
                    elem2 = cat1.getIndividuo2();
                    pobAux.add(elem2);
                }

//cruzas
                nuevo1 = (Individuo) elem1.clone();
                nuevo2 = (Individuo) elem2.clone();

                pivote1 = genRandPP.nextInt(tamaño);
                do {
                    pivote2 = genRandPP.nextInt(tamaño);
                } while (pivote1 == pivote2);
                int inic = (pivote1 < pivote2) ? pivote1 : pivote2;
                int fin = (pivote1 > pivote2) ? pivote1 : pivote2;

                for (int i = inic; i <= fin; i++) {
                    nuevo1.setGene(i, elem2.getGenes()[i]);
                    nuevo2.setGene(i, elem1.getGenes()[i]);
                }

                /*
nuevo1.evalua(cat1);
                 */
                cat1.borraAsignados();
                cat1.asignaIndividuo(nuevo1);
                cat1.evaluaCosto();
                nuevo1 = cat1.getIndividuo2();

                /*
nuevo2.evalua(cat1);
                 */
                cat1.borraAsignados();
                cat1.asignaIndividuo(nuevo2);
                cat1.evaluaCosto();
                nuevo2 = cat1.getIndividuo2();

                pobAux.add(nuevo1);//se agregan como candidatos a la pr�xima generacion
                pobAux.add(nuevo2);

                pobAux.ordenaMenMayor();
                if (!pobNueva.contiene((Individuo) pobAux.elementAt(0))) {
                    pobNueva.add((Individuo) pobAux.elementAt(0));
                }
                if (!pobNueva.contiene((Individuo) pobAux.elementAt(1))) {
                    pobNueva.add((Individuo) pobAux.elementAt(1));
                }
                if (!pobNueva.contiene((Individuo) pobAux.elementAt(2))) {
                    pobNueva.add((Individuo) pobAux.elementAt(2));
                }

            } while (pobNueva.size() < POBLMAX);

            pob = pobNueva;

            int minimo = 100000;
            int maximo = 0;
            int promedio = 0;
            int indxMinimo = 0;
            int costo = 0;
            for (int i = 0; i < pob.size(); i++) {
                costo = ((Individuo) pob.elementAt(i)).getCosto().getCostoTotal();

                if (costo < minimo) {
                    minimo = costo;
                    indxMinimo = i;
                }

                maximo = (costo > maximo) ? costo : maximo;
                promedio += costo;
            }
            float prom = ((float) promedio) / pob.size();
            System.out.print(camada++ + ",   " + minimo + ", " + prom + ", " + maximo + ",   ");

            ((Individuo) pob.elementAt(indxMinimo)).getCosto().imprimeCosto();

            if (camada % 10 == 0) {
                System.out.print(",  ini= " + pob.size());
                pob.eliminaRepetidos();
                System.out.println(",  fin= " + pob.size());
                pob.info();
            }//else  System.out.println();

            if (minimo <= 42) {
                finProceso = true;
            }
        } while (!finProceso);

        for (int i = 0; i < 100; i++) {
            indAux = (Individuo) pob.elementAt(i);
            System.out.print(i + ",   ");
            for (int j = 0; j < indAux.getGenes().length; j++) {
                System.out.print(indAux.getGenes()[j] + ", ");
            }
            System.out.println();
        }

    }// end genetico2()

    /**
     * *********************************************************************
     */
    private void tabu() {

        int nVecindad = 20;
        int nLista = 500;
        Individuo mejorSolucion, indActual, indPropuesto;
        Catalogo cat1 = new Catalogo("tabGrupos.txt", "tabMaes.dat",
                "tabAulas.dat", "tipoHors.txt",
                "tabSemCarr.txt", "tabMaterias.txt");
        int ciclos = 0;

        cat1.verificaDatos();
        cat1.asignaHorRandom();
        cat1.evaluaCosto();

        cat1 = hillclimbing(cat1, 500);

        cat1.evaluaCosto();
        indActual = cat1.getIndividuo2();

        mejorSolucion = (Individuo) indActual.clone();

        Poblacion listaTabu = new Poblacion();// lista tab�
        listaTabu.add(indActual);

        boolean finProceso = false;

        do {
            Poblacion vecindario = cat1.mejorVecinoTotal(nVecindad);

            for (int i = 0; i < nVecindad; i++) {
                indActual = (Individuo) vecindario.elementAt(i);
                if (listaTabu.contiene(indActual)) {
                    //individuo contenido en tabu

                } else {
                    listaTabu.add(indActual);
                    if (listaTabu.size() == nLista + 1) {
                        listaTabu.removeElementAt(0);
                    }
                    break;
                }
            }//for i

            cat1.borraAsignados();
            cat1.asignaIndividuo(indActual);

            System.out.print(ciclos++ + ",  ");
            indActual.getCosto().imprimeCosto();

        } while (!finProceso);

        cat1.imprimeCatalog();
        cat1.semCarrs.muestra(cat1.grupos, cat1.horas);

        VectorAulas va = cat1.aulas;
        for (int i = 0; i < va.size(); i++) {
            int empalm = ((Aula) cat1.aulas.elementAt(i)).cuentaEmpalmes();
            if (empalm != 0) {
                System.out.print("Empalme de aula ");
                System.out.print(((Aula) va.elementAt(i)).getClave() + "  ");
                System.out.println(empalm);
            }
        }
        VectorMaestros vm = cat1.maestros;
        for (int i = 0; i < vm.size(); i++) {
            int empalm = ((Maestro) vm.elementAt(i)).cuentaEmpalmes();
            if (empalm != 0) {
                System.out.print("Empalme de maestro ");
                System.out.print(((Maestro) vm.elementAt(i)).getNombre());
                System.out.println(empalm);
            }
        }

    }

}
