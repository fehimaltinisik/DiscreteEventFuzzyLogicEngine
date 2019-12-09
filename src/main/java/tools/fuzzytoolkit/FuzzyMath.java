package main.java.tools.fuzzytoolkit;

import java.util.Arrays;

public class FuzzyMath{
	public static float fmin(float left, float right) {
		return Float.min(left, right);
	}
	
	public static float[] fmin(float activation_rule, float[] x) {
		float[] temp = new float[x.length];
		
		for(int i = 0; i < temp.length; i++) {
			temp[i] = Float.min(activation_rule, x[i]);
		}
		
		return temp;
	}
	
	public static float[] fmin(float[] x, float[] y) {
		float[] temp = new float[x.length];
		
		for(int i = 0; i < temp.length; i++) {
			temp[i] = Float.min(x[i], y[i]);
		}
		
		return temp;
	}
	
	public static float fmax(float left, float right) {
		return Float.max(left, right);
	}
	
	public static float[] fmax(float activation_rule, float[] x) {
		float[] temp = new float[x.length];
		
		for(int i = 0; i < temp.length; i++) {
			temp[i] = Float.max(activation_rule, x[i]);
		}
		
		return temp;
	}
	
	public static float[] fmax(float[] x, float[] y) {
		float[] temp = new float[x.length];
		
		for(int i = 0; i < temp.length; i++) {
			temp[i] = Float.max(x[i], y[i]);
		}
		
		return temp;
	}
	
	public static float[] linspace(float start, float stop, float numberOfElements) {
		float step = (stop - start) / numberOfElements;
		float [] domain = new float[(int) numberOfElements + 1]; 
		
		for(int i = 0; i < numberOfElements + 1; i++) {
			domain[i] = start + step * i;
		}
		
		System.out.println(Arrays.toString(domain));
				
		return domain;
	}

	public static float[] zeros_like(float[] x_tip) {
		float[] temp = new float[x_tip.length];
		Arrays.fill(temp, 0);
		
		return temp;
	}

	public static float finfo() {
		return Float.MIN_VALUE;
	}	
}
