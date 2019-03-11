package nl.knokko.util.random.test;

import java.io.IOException;

import nl.knokko.util.random.CrazyRandom;
import nl.knokko.util.random.JavaRandom;
import nl.knokko.util.random.PseudoRandom;
import nl.knokko.util.random.Random;

public class RandomTester {

	public static void main(String[] args) throws IOException {
		testJava();
		testLightPseudo();
		testMediumPseudo();
		testHeavyPseudo();
		testWeakCrazy();
		testStrongCrazy();
	}
	
	public static void testRandom(Random random, String name, int factor) {
		long startTime = System.nanoTime();
		System.out.println("Test " + name + ":");
		System.out.println("Best score is 0 and worst score is 1");
		System.out.println("meanByte score is " + Math.abs((meanByte(random, 500 * factor) + 0.5) / Byte.MIN_VALUE));
		System.out.println("meanShort score is " + Math.abs((meanShort(random, 250 * factor) + 0.5) / Short.MIN_VALUE));
		System.out.println("meanInt score is " + Math.abs((meanInt(random, 125 * factor) + 0.5) / Integer.MIN_VALUE));
		long endTime = System.nanoTime();
		System.out.println("Performance weight is " + (((endTime - startTime) / 1000000.0) / factor) + " (lower is better)");
		System.out.println();
	}
	
	public static void testJava() {
		testRandom(new JavaRandom(), "java Random", 3);
	}
	
	public static void testLightPseudo() {
		testRandom(new PseudoRandom(PseudoRandom.Configuration.LIGHT), "light PseudoRandom", 3);
	}
	
	public static void testMediumPseudo() {
		testRandom(new PseudoRandom(PseudoRandom.Configuration.MEDIUM), "medium PseudoRandom", 3);
	}
	
	public static void testHeavyPseudo() {
		testRandom(new PseudoRandom(PseudoRandom.Configuration.LEGACY), "heavy PseudoRandom", 3);
	}
	
	public static void testWeakCrazy() {
		testRandom(CrazyRandom.createWeak(), "weak CrazyRandom", 3);
	}
	
	public static void testStrongCrazy() throws IOException {
		testRandom(CrazyRandom.createStrong(), "strong CrazyRandom", 3);
	}
	
	public static double meanByte(Random random, int amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount (" + amount + ") must be greater than 0");
		}
		long total = 0;
		for (int counter = 0; counter < amount; counter++) {
			total = Math.addExact(total, random.nextByte());
		}
		return total / (double) amount;
	}
	
	public static double meanShort(Random random, int amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount (" + amount + ") must be greater than 0");
		}
		long total = 0;
		for (int counter = 0; counter < amount; counter++) {
			total = Math.addExact(total, random.nextShort());
		}
		return total / (double) amount;
	}
	
	public static double meanInt(Random random, int amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount (" + amount + ") must be greater than 0");
		}
		long total = 0;
		for (int counter = 0; counter < amount; counter++) {
			total = Math.addExact(total, random.nextInt());
		}
		return total / (double) amount;
	}
}