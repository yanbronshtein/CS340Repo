#!/bin/bash
# Please make sure to do chmod 755 hw2.sh 
# @author: Yaniv Bronshtein
#Part A Standard Directories and Files
# Create the hw2 txt file in the home directory
cd ~
touch hw2.txt

echo "Part A" >> hw2.txt

echo "****** BEGIN Root Directory(/) ******" >> hw2.txt
cd / # Takes the user to the root directory
ls -l | head -6 >> ~/hw2.txt #Copy the contents to hw2.txt 
echo "****** END Root Directory(/) ******" >> hw2.txt

echo "****** BEGIN /bin ******" >> ~/hw2.txt
cd /bin # Takes the user to the bin directory
ls -l | head -6 >> ~/hw2.txt #Copy the contents to hw2.txt
echo "****** END /bin ******" >> ~/hw2.txt

echo "****** BEGIN /dev ******" >> ~/hw2.txt
cd /dev # Takes the user to the dev directory
ls -l | head -6 >> ~/hw2.txt #Copy the contents to hw2.txt
echo "****** END /dev ******" >> ~/hw2.txt


echo "****** BEGIN /etc ******" >> ~/hw2.txt
cd /etc # Takes the user to the etc directory
ls -l | head -6 >> ~/hw2.txt #Copy the contents to hw2.txt
echo "****** END /etc ******" >> ~/hw2.txt

echo "****** BEGIN /lib ******" >> ~/hw2.txt
cd /lib # Takes the user to the lib directory
ls -l | head -6 >> ~/hw2.txt #Copy the contents to hw2.txt
echo "****** END /lib ******" >> ~/hw2.txt

echo "****** BEGIN /tmp ******" >> ~/hw2.txt
cd /tmp # Takes the user to the tmp directory
ls -l | head -6 >> ~/hw2.txt #Copy the contents to hw2.txt
echo "****** END /tmp ******" >> ~/hw2.txt

echo "****** BEGIN /etc/passwd ******" >> ~/hw2.txt
cat /etc/passwd >> ~/hw2.txt #Copy the contents to hw2.txt
echo "****** END /etc/passwd ******" >> ~/hw2.txt















