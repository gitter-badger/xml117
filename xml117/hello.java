package xml117;

class hello {
 	public static void printBlanks(int i){
		for(int j = 0; j < i; j++) System.out.println();
	}

	public static void main(String[] args){
		printBlanks(20);
		
		for(int j=0;j < (args == null ? 1 : Integer.parseInt(args[0]));j++) {   
			System.out.println("\t\t\t\t\t\t\t\thello world!");
		}
		printBlanks(10);
	}	
}

