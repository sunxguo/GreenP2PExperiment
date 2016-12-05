package createMatrix;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			FileOutputStream bos;
			bos = new FileOutputStream("adj1912.txt");
	        System.setOut(new PrintStream(bos));
	        //-------------初始化数据-------------start
			int maxNum=1912;
			int RoutesAmount=1600;//路由器数量
			//-------------初始化数据-------------end
			int matrixRowLength=(int) Math.sqrt(RoutesAmount);//行列数 40
			int PCsAmount=maxNum-RoutesAmount;//PC数量 312
			int fileSize=10;//文件大小
			int[] PCs=new int[PCsAmount];//PC
			int[] Route_PCs=new int[PCsAmount];//连接PC的路由器
			for(int i=0;i<PCsAmount;i++){
				PCs[i]=RoutesAmount+i+1;
			}
			int Rcount=0;
			for(int j=1;j<=RoutesAmount;j++){
				if(j<=matrixRowLength||j>=RoutesAmount-matrixRowLength+1||j%matrixRowLength==0||j%matrixRowLength==1){
					Route_PCs[Rcount]=j;
					Route_PCs[Rcount+1]=j;
					Rcount+=2;
				}
			}
			int SeedsAmount=30;
			int[] seeds=new int[SeedsAmount];
			for(int i=0;i<SeedsAmount;i++){
				int number=RoutesAmount+1+(int)(Math.random()*PCsAmount);
				seeds[i]=number;
				for(int j=0;j<i;j++){
					if(seeds[j]==number){
						i--;
						break;
					}
				}
			}
			int[] allSeedsRoutes=new int[SeedsAmount];
			for(int i=0;i<seeds.length;i++){
				for(int j=0;j<PCsAmount;j++){
					if(seeds[i]==PCs[j]){
//						System.out.println(i+"&"+j);
						allSeedsRoutes[i]=Route_PCs[j];
						break;
					}
				}
			}
			for(int i=0;i<seeds.length;i++){
//				System.out.println(seeds[i]);
			}
//			int[] seeds={1617,1618,1619,1620};
//			int[] allSeedsRoutes={9,9,10,10};
			
//			int[] seedsRoutes={9,10};
			int destPc=RoutesAmount+1+(int)(Math.random()*PCsAmount);//随机选择目的pc
			int destRoute=1601;//默认值
			for(int j=0;j<PCsAmount;j++){
				if(destPc==PCs[j]){
					destRoute=Route_PCs[j];//根据目的pc找到连接的目的路由器
					break;
				}
			}
			//---------------------------------end

//			int DownloaderPCsAmount=100;
//			int[] DownloaderPCs=new int[DownloaderPCsAmount];
//			for(int i=0;i<100;i++){
//				int number=1+(int)(Math.random()*PCsAmount);
//				DownloaderPCs[i]=number;
//				for(int j=0;j<i;j++){
//					if(DownloaderPCs[j]==number){
//						i--;
//						break;
//					}
//				}
//			}
			
			int[] seedsRoutes=array_unique(allSeedsRoutes);
			int matrix[][] = new int[maxNum][maxNum];
			for(int i=0;i<maxNum;i++){
				for(int j=0;j<maxNum;j++){
					matrix[i][j]=0;
				}
			}
			int dr_r=(int) Math.floor((destRoute-1)/matrixRowLength);//下载节点连接的路由器的行
			int dr_c=(destRoute-1)%matrixRowLength;//下载节点连接的路由器的列
			for(int i=0;i<seedsRoutes.length;i++){
				int sr=seedsRoutes[i];
				int sr_r=(int) Math.floor((sr-1)/matrixRowLength);//种子连接的路由器的行
				int sr_c=(sr-1)%matrixRowLength;//种子连接的路由器的列
				int rows_amount=Math.abs(sr_r-dr_r);
				int cols_amount=Math.abs(sr_c-dr_c);
				for(int r=0;r<=rows_amount;r++){
					int signal_r=sr_r>dr_r?-40:40;
					for(int c=0;c<=cols_amount;c++){
						int signal_c=sr_c>dr_c?-1:1;
						int route=sr+r*signal_r+c*signal_c;
						if(r!=rows_amount){
							matrix[route-1][route-1+signal_r]=1;
						}
						if(c!=cols_amount){
							matrix[route-1][route-1+signal_c]=1;
						}
					}
				}
			}
			for(int i=0;i<seeds.length;i++){
				matrix[seeds[i]-1][allSeedsRoutes[i]-1]=1;
			}
			matrix[destRoute-1][destPc-1]=1;//
			
			for(int i=0;i<matrix.length;i++){
				for(int j=0;j<matrix.length;j++){
					System.out.print(matrix[i][j]);
					if(j!=matrix.length-1){
						System.out.print(" ");
					}
				}
				if(i!=matrix.length-1){
					System.out.println();
				}
			}
			
			bos = new FileOutputStream("D:\\adj1920Contraints.txt");
	        System.setOut(new PrintStream(bos));
			int seedCount=1;
			String lastSeed="";
			for(int i=0;i<maxNum;i++){
				boolean exist=false;
				for(int j=0;j<seeds.length;j++){
					if(i+1==seeds[j]){
						exist=true;
						break;
					}
				}
				if(exist){
					if(seedCount!=seeds.length){
						System.out.print("t"+seedCount);
						lastSeed+="-t"+seedCount;
					}else{
						System.out.print(fileSize+lastSeed);
					}
					seedCount++;
				}else if(i+1==destPc){
					System.out.print("-"+fileSize);
				}else{
					System.out.print("0");
				}
				if(i!=maxNum-1){
					System.out.print(";");
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	//去除数组中重复的记录
	public static int[] array_unique(int[] allSeedsRoutes) {
	    // array_unique
	    List<Integer> list = new LinkedList<Integer>();
	    for(int i = 0; i < allSeedsRoutes.length; i++) {
	        if(!list.contains(allSeedsRoutes[i])) {
	            list.add(allSeedsRoutes[i]);
	        }
	    }
	    Integer[] data=list.toArray(new Integer[list.size()]);
	    int[] result = new int[data.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = data[i];
        }
        return result;
	}
	public static int getRouteByPC(int PC,int PCsAmount,int[] PCs,int[] Route_PCs){
		int route = 0;
		for(int j=0;j<PCsAmount;j++){
			if(PC==PCs[j]){
				route=Route_PCs[j];
				break;
			}
		}
		return route;
	}

}
