#!/usr/bin/env python
# -*- coding: utf-8 -*-

import matplotlib
import matplotlib.pyplot as plt
import datetime


f=open("./results.txt")
a=[i.split() for i in f.readlines()]

categories = {}

for i in a: 

    print i
    categ = i[0]
    numb = int(i[1])
    time = datetime.datetime.fromtimestamp(int(i[2])).strftime('%a %H:%M:%S')

    if categ in categories: 
        categories[categ].append([numb,time])
        

    else:
        categories[categ] = [[numb,time]]

# Filter data to plot
x_total = []
y_total = []
labels = []

for i in categories:

    x = []
    y = []

    for j in categories[i]:

        y.append(j[0])
        time = datetime.datetime.strptime(j[1],'%a %H:%M:%S')
        x.append(time)
        #y.append(matplotlib.dates.date2num(j[1]))
    x_total.append(x)
    y_total.append(y)
    labels.append(i)

print x_total
print y_total

for n in range(len(x_total)):
    plt.plot(x_total[n],y_total[n],label=labels[n])


plt.legend(loc = 0)
plt.title("Analysis in time")
plt.xlabel("Time")
plt.ylabel("Number of Mentions")
plt.gcf().autofmt_xdate()

plt.show()





