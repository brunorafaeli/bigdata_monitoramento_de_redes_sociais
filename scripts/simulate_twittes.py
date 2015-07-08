import os
import time

f=open("dataset.txt")
a=[i.split() for i in f.readlines()]

aux = 0
N = 10 # number of lines to read

for line in a:

	if aux <= N:
		aux += 1
		result = line[0]
		os.system("nc -lk 999 " + result)

	else:

		aux = 0
		# time to wait
		time.sleep(15)
