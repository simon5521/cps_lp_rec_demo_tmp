package test;

import hu.bme.mit.gamma.usecase.epas.epasenv.EpasEnv1;

public class Main {

	public static void main(String[] args) {
		EpasEnv1 epas = new EpasEnv1();
		epas.reset();
		epas.getEpas().getS1AFault().raiseDet();
		epas.runFullCycle();
		System.out.println("Asd");
	}

}