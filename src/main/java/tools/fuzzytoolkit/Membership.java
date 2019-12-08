package main.java.tools.fuzzytoolkit;

import java.util.Arrays;

// TODO : trimf() works as expected.
// TODO : Should test trapmf() and gassianmf().

public class Membership {
	public static float [] trimf(float [] x, float a, float b, float c) {
		
		float [] y = new float[x.length];
		float [] temp = new float[x.length];
		
		Arrays.fill(y, 0);
		
		// Left
		
		if (a != b) {
			for (int i = 0; i < x.length; i++) {
				temp[i] = (a < x[i] & x[i] < b) ? 1 : 0;
			}
			
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] != 0) {
					y[i] = (x[i] - a) / (b - a);
				}else
					continue;
			}
			
		}
		
		// Right
	    
		if (b != c) {
			for (int i = 0; i < x.length; i++) {
				temp[i] = (b < x[i] & x[i] < c) ? 1 : 0;
			}
			
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] != 0) {
					y[i] = (c - x[i] ) / (c - b);
				}else
					continue;
			}
		}
		
		for (int i = 0; i < x.length; i++) {
			if(x[i] == b) {
				y[i] = 1;
			}
		}
		
		System.out.println(Arrays.toString(y));
		
	    return y;

	}
	
	public static float [] trapmf(float [] x, float a, float b, float c, float d) {
		
		float [] y = new float[x.length];
		float [] temp = new float[x.length];
				
		Arrays.fill(y, 1);
		
		//
		
		for (int i = 0; i < x.length; i++) {
			temp[i] = (x[i] <= b) ? 1 : 0;
		}
		
		int counter = 0;
		float [] trimx = new float[temp.length];
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != 0) {
				trimx[counter] = x[i];
				counter++;
			}
		}
		
		float [] trimedx = trimf(Arrays.copyOfRange(trimx, 0, counter), a, b, b); 
		
		for (int i = 0, j = 0; i < temp.length; i++) { 
			if (temp[i] != 0) {
				y[i] = trimedx[j];
				j++;
			} 
		}
		
		//
		
		for (int i = 0; i < x.length; i++) {
			temp[i] = (x[i] >= c) ? 1 : 0;
		}
		
		counter = 0;
		Arrays.fill(trimx, 0);
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != 0) {
				trimx[counter] = x[i];
				counter++;
			}
		}
		
		trimedx = trimf(Arrays.copyOfRange(trimx, 0, counter), c, c, d); 
		
		for (int i = 0, j = 0; i < temp.length; i++) { 
			if (temp[i] != 0) {
				y[i] = trimedx[j];
				j++;
			} 
		}
		
		//
		
		for (int i = 0; i < x.length; i++) {
			temp[i] = (x[i] < a) ? 1 : 0;
		}
		
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != 0) {
				y[i] = 0;
			}else
				continue;
		}
		
		//
		
		for (int i = 0; i < x.length; i++) {
			temp[i] = (x[i] > d) ? 1 : 0;
		}
		
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != 0) {
				y[i] = 0;
			}else
				continue;
		}
		
		System.out.println(Arrays.toString(temp));
		
		return y;
	}

	public static float [] gaussianmf(float [] x, float mean, float sigma) {
		
		float [] temp = new float[x.length];
		
		for (int i = 0; i < x.length; i++) {
			temp[i] = (float) Math.exp(Math.pow(x[i] - mean, 2) / (2 * Math.pow(sigma, 2)));
		}
		
		System.out.println(Arrays.toString(temp));
		
		return temp;
	}
	
}
