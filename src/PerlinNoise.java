//Michal Jez
//17/06/2016
//Class to generate the heightmap for the game
//http://freespace.virgin.net/hugo.elias/models/m_perlin.htm
//Methods and instructions were taken from this site to develop the following methods and tailor them to get nive fluid heightmaps

import java.util.*;

public class PerlinNoise
{
	private int n1, n2, n3, n4, octaves;
	private double persistance;
	
	
	
	private static boolean isPrime(int n)
	{	/*Returns true if the number is a prime number*/
		int sq = (int) Math.sqrt(n);
		for (int i = 2; i <= sq; i++)
		{
			if (n % i == 0)
				return false;
		}
		return true;
	}
	
	public static int getRandomPrimeNumber(int low, int high)
	{
		ArrayList<Integer> primes = new ArrayList<Integer> ();
		int count = 0;
		int num = 0;
		for (int i = low; i < high; i++)
		{
			if (isPrime(i))
			{
				primes.add(i);
				count++;
				num++;
				if (num == 16)
				{
					System.out.println();
					num = 0;
				}
				System.out.print(i + ", ");
			}
		}
		return primes.get((int) (Math.random() * primes.size()));
	}
	
	public PerlinNoise(double pers, int octs)
	{
		n1 = 15731;
		n2 = 789221;
		n3 = 1376312589;
		n4 = 1073741824;
		persistance = pers;
		octaves = octs;
	}
	
	private double Noise(int x)
	{
		x = (x << 13) ^ x;
		return (1.0 - ((x * (x * x * n1 + n2) + n3) & 0x7fffffff) / n4);
	}
	
	private double Smoothed_Noise(double x)
	{
		return Noise((int) x) / 2 + Noise((int) x - 1) / 4 + Noise((int) x + 1) / 4;
	}
	
	private double Cosine_Interpolate(double a, double b, double x)
	{
		double ft = x * Math.PI;
		double f = (1 - Math.cos(ft)) * 0.5;
		
		return a * (1 - f) + b * f;
	}
	
	private double Interpolated_Noise(double x)
	{
		int intX = (int) x;
		double fractionalX = x - intX;
		
		double v1 = Smoothed_Noise(intX);
		double v2 = Smoothed_Noise(intX + 1);
		
		return Cosine_Interpolate(v1, v2, fractionalX);
	}
	
	public double getNoise(double x)
	{
		double total = 0.0;
		
		for (int i = 0; i < octaves; i++)
		{
			int freq = 2 << (i - 1); 		//"2 ** i" in python
			double amp = Math.pow(persistance, i);
			
			total += Interpolated_Noise(x * freq) * amp;
		}
		return total;
	}
		
}