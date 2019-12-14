package main.java.tools.fuzzytoolkit;

public class FuzzyOperations {
	public static float interpolateMembership(float[] x, float[] xmf, float crisp, boolean zeroOutsideX) {	
		
	if (zeroOutsideX) {
		if(crisp < x[0]) {
			return interpolateMembership(x, xmf, x[0], zeroOutsideX);
		}else if(crisp > x[x.length - 1]) {
			return interpolateMembership(x, xmf, x[x.length - 1], zeroOutsideX);
		}
			
	}else {
		throw new IllegalArgumentException(String.format("Crisp value: %f is out of domain: [%.2f, %.2f]", 
				crisp, x[0], x[x.length]));
	}
	
	int integer = (int)crisp;
	double decimal = crisp - (int)crisp;
	
	int idx = 0;
	float distance = Float.MAX_VALUE;
	
	for(int i = 1; i < x.length - 1; i++) {
		float indexdistance = Math.abs(x[i] - integer);
		// float indexdistance = integer - x[i];
		if (indexdistance < distance) {
			distance = indexdistance;
			idx = i;
		}
	}
	
	int x1 = idx;
	int x2 = (int) (idx + 1 * Math.signum(decimal));
	
	float y1 = xmf[x1];
	float y2 = xmf[x2];
	
	float fuzzy = (float) (y1 + (decimal / (x2 - x1)) * (y2 - y1));

//	System.out.printf("x1index: %d, x2index: %d,", x1, x2);
//	System.out.printf("x1: %.2f, x2: %.2f, y1: %.2f, y2: %.2f\n", x[x1], x[x2], y1, y2);
//	System.out.printf("%d, %.2f, %.2f\n", integer, decimal, fuzzy);
//	
//	System.out.printf("%d, %.2f, %.2f\n", integer, decimal, fuzzy);
	
	if (Float.isNaN(fuzzy)) {
		return 0;
	}else{
		return fuzzy;	
			
	}
	
	
	}

	public static float defuzz(float[] x, float[] xmf, String string) {
		float sum_moment_area = 0.0f;
		float sum_area = 0.0f;
				    
		if (x.length == 1)
			return x[0] * xmf[0] / FuzzyMath.fmax(xmf[0], FuzzyMath.finfo());

	    for (int i = 1; i < x.length; i++) { 
	        
	    	float x1 = x[i - 1];
	    	float x2 = x[i];
	    	float y1 = xmf[i - 1];
	    	float y2 = xmf[i];
	    	
	    	float moment;
	    	float area;
	    	
	        if (!((y1 == y2 && y2== 0.0) || (x1 == x2))) {
	            if (y1 == y2) { // rectangle
	                moment = 0.5f * (x1 + x2);
	                area = (x2 - x1) * y1;
	            }else if( y1 == 0.0f && y2 != 0.0) {  // triangle, height y2
	            	moment = 2.0f / 3.0f * (x2 - x1) + x1;
	            	area = 0.5f * (x2 - x1) * y2;
	            }else if( y2 == 0.0f && y1 != 0.0) {  // triangle, height y1
	            	moment = 1.0f / 3.0f * (x2 - x1) + x1;
	            	area = 0.5f * (x2 - x1) * y1;
	            }else {
	            	moment = (2.0f / 3.0f * (x2-x1) * (y2 + 0.5f * y1)) / (y1 + y2) + x1;
	            	area = 0.5f * (x2 - x1) * (y1 + y2);
	            }
	            
	            sum_moment_area += moment * area;
	            sum_area += area;
	        }
	        
	    
	    }
	    
//	    System.out.println(sum_moment_area);
//	    System.out.println(sum_area);
	    
	    return sum_moment_area / FuzzyMath.fmax(sum_area,
                FuzzyMath.finfo());
	}
	
}
