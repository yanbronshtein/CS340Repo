// A C program to demonstrate Zombie Process.  
// Child becomes Zombie as parent is sleeping 
// when child process exits. 
#include <stdlib.h> 
#include <sys/types.h> 
#include <unistd.h> 
#include <stdio.h>
int main() 
{ 
    // Fork returns process id 
    // in parent process 
    pid_t child_pid = fork(); 
  
    // Parent process  
    if (child_pid > 0) {
        printf("Parent process %d going to sleep\n",getpid());
        sleep(200); 
        printf("Parent Finished execution with PID: %d\n",getpid());

    }
  
    // Child process 
    else        
        printf("In child with PID: %d\n",getpid());
        
        for (int i = 0; i < 10; i++)
        {
          printf("From Child with PID:%d\n",getpid());
          printf("Count: %d\n",i);
          sleep(10);
          
        }
        
        printf("Exiting from Child with PID: %d\n",getpid());
        exit(0); 
  
    return 0; 
} 