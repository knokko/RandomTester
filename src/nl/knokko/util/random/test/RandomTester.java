package nl.knokko.util.random.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import nl.knokko.util.bits.BitHelper;
import nl.knokko.util.random.CrazyRandom;
import nl.knokko.util.random.IntBasedRandom;
import nl.knokko.util.random.IntMatrixRandom;
import nl.knokko.util.random.JavaRandom;
import nl.knokko.util.random.PseudoRandom;
import nl.knokko.util.random.Random;

public class RandomTester {

	public static void main(String[] args) throws IOException {
		testJava();
		testIntMatrix();
		testLightPseudo();
		testMediumPseudo();
		testHeavyPseudo();
		//testWeakCrazy();
		//testStrongCrazy();
		//testIntBasedRandom();
	}
	
	static void testIntBasedRandom() {
		boolean[] buffer = new boolean[32];
		MockIntRandom mock = new MockIntRandom();
		Set<Integer> testSet = new HashSet<Integer>(5000000);
		for (long current = Integer.MIN_VALUE; current <= Integer.MAX_VALUE; current += 1234) {
			mock.setNextInt((int) current);
			for (int index = 0; index < 32; index++) {
				buffer[index] = mock.nextBoolean();
			}
			int fromBits = BitHelper.makeInt(BitHelper.byteFromBinary(buffer), 
					BitHelper.byteFromBinary(buffer, 8), BitHelper.byteFromBinary(buffer, 16), 
					BitHelper.byteFromBinary(buffer, 24));
			assert testSet.add(fromBits);
		}
	}
	
	static class MockIntRandom extends IntBasedRandom {
		
		private int nextInt;
		
		void setNextInt(int nextInt) {
			this.nextInt = nextInt;
		}

		@Override
		public int nextInt() {
			return nextInt;
		}

		@Override
		public boolean isPseudo() {
			return false;
		}

		@Override
		public Random clone() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static void testRandom(Random random, String name, int factor) {
		long startTime = System.nanoTime();
		System.out.println("Test " + name + ":");
		System.out.println("Best score is 0 and worst score is 1");
		double meanByte = Math.abs((meanByte(random, 500 * factor) + 0.5) / Byte.MIN_VALUE);
		double medianByte = (double) Math.abs(medianByte(random, 500 * factor)) / Byte.MAX_VALUE;
		double meanShort = Math.abs((meanShort(random, 250 * factor) + 0.5) / Short.MIN_VALUE);
		double medianShort = (double) Math.abs(medianShort(random, 250 * factor)) / Short.MAX_VALUE;
		double meanInt = Math.abs((meanInt(random, 125 * factor) + 0.5) / Integer.MIN_VALUE);
		double medianInt = (double) Math.abs(medianInt(random, 125 * factor)) / Integer.MAX_VALUE;
		double distByte = distByte(random, 1000 * factor);
		double distShort = distShort(random, 500 * factor);
		/*
		System.out.println("meanByte score is " + meanByte);
		System.out.println("medianByte score is " + medianByte);
		System.out.println("meanShort score is " + meanShort);
		System.out.println("medianShort score is " + medianShort);
		System.out.println("meanInt score is " + meanInt);
		System.out.println("medianInt score is " + medianInt);
		*/
		System.out.println("score is " + randomScore(meanByte, medianByte, meanShort, medianShort, meanInt, 
				medianInt, distByte, distShort));
		long endTime = System.nanoTime();
		System.out.println("Performance weight is " + (((endTime - startTime) / 1000000.0) / factor) + " (lower is better)");
		System.out.println();
	}
	
	private static double randomScore(double...scores) {
		double result = 0;
		for (double score : scores) {
			result += score * score;
		}
		return result;
	}
	
	public static void testJava() {
		testRandom(new JavaRandom(), "java Random", 10);
	}
	
	public static void testIntMatrix() {
		IntMatrixRandom random2 = new IntMatrixRandom(2, new int[] {6,7,8,9}, 0);
		random2.next();
		assert Arrays.equals(random2.getBackingArray(), new int[] {92, 105, 120, 137});
		for (int length = 2; length < 20; length++) {
			testRandom(new IntMatrixRandom(length), length + "x" + length + " Int Matrix Random", 10);
		}
	}
	
	public static void testLightPseudo() {
		testRandom(new PseudoRandom(PseudoRandom.Configuration.LIGHT), "light PseudoRandom", 10);
	}
	
	public static void testMediumPseudo() {
		testRandom(new PseudoRandom(PseudoRandom.Configuration.MEDIUM), "medium PseudoRandom", 10);
	}
	
	public static void testHeavyPseudo() {
		testRandom(new PseudoRandom(PseudoRandom.Configuration.LEGACY), "heavy PseudoRandom", 10);
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
	
	public static byte medianByte(Random random, int amount) {
		byte[] array = new byte[amount];
		for (int index = 0; index < amount; index++) {
			array[index] = random.nextByte();
		}
		Arrays.sort(array);
		return array[amount / 2];
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
	
	public static short medianShort(Random random, int amount) {
		short[] array = new short[amount];
		for (int index = 0; index < amount; index++) {
			array[index] = random.nextShort();
		}
		Arrays.sort(array);
		return array[amount / 2];
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
	
	public static int medianInt(Random random, int amount) {
		int[] array = new int[amount];
		for (int index = 0; index < amount; index++) {
			array[index] = random.nextInt();
		}
		Arrays.sort(array);
		return array[amount / 2];
	}
	
	public static double distByte(Random random, int amount) {
		int[] counts = new int[256];
		for (int counter = 0; counter < amount; counter++) {
			counts[random.nextByte() & 0xFF]++;
		}
		long total = 0;
		for (int count : counts) {
			total += count * count;
		}
		long worst = (long) amount * amount;
		long best = Math.max(worst / 256, amount);
		return (double) total / best / 256;
	}
	
	public static double distShort(Random random, int amount) {
		int[] counts = new int[Character.MAX_VALUE + 1];
		for (int counter = 0; counter < amount; counter++) {
			counts[random.nextChar()]++;
		}
		long total = 0;
		for (int count : counts) {
			total += count * count;
		}
		long worst = (long) amount * amount;
		long best = Math.max(worst / counts.length, amount);
		return (double) total / best / counts.length;
	}
}