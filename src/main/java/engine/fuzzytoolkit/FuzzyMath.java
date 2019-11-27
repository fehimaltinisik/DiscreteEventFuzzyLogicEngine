package main.java.engine.fuzzytoolkit;

import java.util.Arrays;
import java.lang.Math;

public class FuzzyMath {
	public static float[] linspace(float start, float stop, float numberOfElements) {
		float step = (stop - start) / numberOfElements;
		float [] domain = new float[(int) numberOfElements + 1]; 
		
		for(int i = 0; i < numberOfElements + 1; i++) {
			domain[i] = start + step * i;
		}
		
		System.out.println(Arrays.toString(domain));
		
		return domain;
	}	
}
