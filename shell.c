#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


main(){
	while(1){
		char *in = calloc(1000,1);
		int fp = read(0,in,100);
		in[fp] = '\0';
		char * pch;
  		pch = strtok (in," ,.-");
	
	    if(strstr(pch,"create")){
			//write(1,pch,100);
			printf(pch);
			//int fp = creat(1,O_WRONLY|O_CREAT);
			//close(fp);
		}
		
	}
}
