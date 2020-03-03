//zombie.c
//Author: Yaniv Bronshtein
//CS 340
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main()
{
  //Create child process
  int pid = fork();

  //In the parent process
  if (pid > 0)
  {
    printf("I am the parent and my PID is %d\n",getpid());
    sleep(30);
  }
  //In the child process
  else if (pid == 0) 
  {
    printf("I am the child with PID %d and my parent has PID %d \n", getpid(),getppid());
    sleep(1);
    exit(0); //Exit program and child with succesful exit code
  } else 
  {
    printf("Failed to create child!\n");
    exit(1); //Exit program with failure exit code
  }
  return 0;
}