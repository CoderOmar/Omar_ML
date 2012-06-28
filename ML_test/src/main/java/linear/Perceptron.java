package linear;

/**
 * 感知机
 * @author ChenJie
 *
 */
public class Perceptron {

	/**
	 * 例子：
	 * 正点：(3,3),(4,3)
	 * 负点：(1,1)
	 * 求感知机模型f(x)=sign(w*x+b)
	 * w=(w1,w2),x=(x1,x2)
	 * lrate=1
	 */
	
	public static void main(String[] args){
		double[][] inputs={{3,3},{4,3},{1,1}};
		int[] label={1,1,-1};
		
		double[] w={0,0};//init w
		double b=0;
		int errorNum=0;
		do{
			errorNum=3;
			train:for(int i=0;i<inputs.length;i++){
				double[] sample=inputs[i];
				double f= vectorMuplt(w,sample)+b;
				int res=sign(f);
				if(res!=label[i]){
					w[0]=w[0]+label[i]*sample[0]; //參數改變策略
					w[1]=w[1]+label[i]*sample[1]; //參數改變策略
					b=b+label[i];
					System.out.println("w:{"+w[0]+","+w[1]+"} -- b:"+b+"--for -- errorNum:"+errorNum);
					break train;//更新策略：遇到錯誤分類就重新計算...
				}else{
					errorNum--;
				}
			}
		}while(errorNum!=0);
		
		System.out.println("w:{"+w[0]+","+w[1]+"} -- b:"+b);
		
	}
	
	/**
	 * 簡單相乘
	 * @param v1
	 * @param v2
	 * @return
	 * Jun 28, 2012
	 */
	static double  vectorMuplt(double[] v1,double[] v2){
		double res=0.0;
		for(int i =0 ;i<v1.length;i++){
			double temp=v1[i]*v2[i];
			res+=temp;
		}
		return res;
	}
	
	/**
	 * 符号函数
	 * @param input
	 * @return
	 * Jun 28, 2012
	 */
	private static int sign(double input){
		if(input>0){
			return 1;
		}else{
			return -1;
		}
	}
}
