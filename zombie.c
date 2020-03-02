// Author: Yaniv Bronshtein  
// The child process a Zombie (a.ka <defunct> process) when parent process is put to sleep and it has terminated 
// When the parent process wakes up the child stops being a zombie and all processes terminate successfully. 
#include <stdlib.h> 
#include <sys/types.h> 
#include <unistd.h> 
#include <stdio.h>
#include <sys/wait.h>
// #define NULL 0
int main() 
{ 
  // Fork returns process id 
  // in parent process 
  int pid = fork(); 
  
  // Parent process  
  if (pid > 0) {
    printf("I am the parent and my id is [%d]\n",getpid());
    sleep(30); //Force parent to sleep for 30 seconds so child can terminate first and turn into a zombie
  }
  // Child process 
  else {        
    printf("I am the child with pid [%d] and my parent has ppid [%d]\n",getpid(),getppid());
    sleep(1);
    exit(0); 
    printf("Result of wait(): %d \n",wait(NULL));
  }
  return 0; 
} 