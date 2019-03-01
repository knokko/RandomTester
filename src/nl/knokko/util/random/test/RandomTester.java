package nl.knokko.util.random.test;

import java.io.IOException;

import nl.knokko.util.random.CrazyRandom;
import nl.knokko.util.random.PseudoRandom;
import nl.knokko.util.random.Random;

public class RandomTester {

	public static void main(String[] args) throws IOException {
		testLightPseudo();
		testMediumPseudo();
		testHeavyPseudo();
		testWeakCrazy();
		testStrongCrazy();
	}
	
	public static void testRandom(Random random, String name, int factor) {
		System.out.println("meanByte of " + name + " is " + meanByte(random, 500 * factor));
		System.out.println("meanShort of " + name + " is " + meanShort(random, 250 * factor));
		System.out.println("meanInt of " + name + " is " + meanInt(random, 125 * factor));
	}
	
	public static void testLightPseudo() {
		testRandom(new PseudoRandom(PseudoRandom.Configuration.LIGHT), "light PseudoRandom", 150);
	}
	
	public static void testMediumPseudo() {
		testRandom(new PseudoRandom(PseudoRandom.Configuration.MEDIUM), "medium PseudoRandom", 100);
	}
	
	public static void testHeavyPseudo() {
		testRandom(new PseudoRandom(PseudoRandom.Configuration.LEGACY), "heavy PseudoRandom", 70);
	}
	
	public static void testWeakCrazy() {
		testRandom(CrazyRandom.createWeak(), "weak CrazyRandom", 1);
	}
	
	public static void testStrongCrazy() throws IOException {
		testRandom(CrazyRandom.createStrong(), "strong CrazyRandom", 1);
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