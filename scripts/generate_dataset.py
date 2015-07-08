import random

f = open('dataset.txt','w+')

positive = ["bom","rapido","barato"]
negative = ["ruim","lento","caro"]
products = ["thinkpad","streamings","lotus","websphere","tivoli","db2","softlayer","bluemix","analytics","bigdata","cloud","IBMBluemix","#bigdata","#cloudcomputing","@IBMSoftware","@IBMbigdata","Supercomputer"]
cognitive = ["watson","cognitive"]
others = ["popcorn","abobora","bitcode"]
times = [5,10,15,20,25,30,35]

N = 10000
Influence = 600

for i in range(N):

	w = random.choice(others)

	f.write( w + '\n') 

	if i % 9 == 0:
		w = random.choice(positive)
		f.write( w + '\n')

	if i % 13 == 0:
		w = random.choice(cognitive)

		f.write( w + '\n')
	if i % 27 == 0:
		w = random.choice(negative)
		f.write( w + '\n')

	if i == Influence:
		for l in range(30):
			w = random.choice(products)
			f.write( w + '\n')


f.close()