#!/usr/bin/env python
import matplotlib.pyplot as plt
import sys
from matplotlib.dates import DateFormatter, WeekdayLocator,\
    DayLocator, MONDAY
import numpy as np
import matplotlib.dates as mdates
from mpl_finance import candlestick_ohlc
from pylab import rcParams
from datetime import datetime as dt
rcParams['figure.figsize'] = 13, 7


import os.path
 
# Define a filename.
filename = "data.txt"

ovbVals = []
obvDates = []
 
if not os.path.isfile(filename):
    print('File does not exist.')
else:  
    with open(filename) as f:
        content = f.read().splitlines()

    for line in content:
        vals = line.split("\t")
        ovbVals.append(float(vals[0].strip()))
        date = dt.strptime(vals[1].strip(), '%Y-%m-%d').date()
        obvDates.append(date)

startDate = obvDates[0]
endDate = obvDates[len(obvDates) - 1]


valArr = np.array(ovbVals)
# print(valArr)


minVal = np.amin(valArr)
maxVal = np.amax(valArr)



def normalizeVales(value, minVal, maxVal):
	y = (value - minVal) / (maxVal - minVal)
	return y

#print(minVal, maxVal)
j = 0
for i in valArr:
	valArr[j] = normalizeVales(i, minVal, maxVal)
	j += 1

# print("after normalizing")
# print(valArr)



def createChart(values, startDate, endDate, obvDates):
	# print(values, startDate, endDate)

	years = mdates.YearLocator()    #standardize 
	months = mdates.MonthLocator() 
	monthsFmt = mdates.DateFormatter('%Y-%m')

	fig, ax = plt.subplots()

	ax.plot(obvDates, values, color="red")
	fig.subplots_adjust(bottom=0.2)

	ax.fmt_xdata = DateFormatter('%Y-%m')



	ax.xaxis.set_major_locator(months)    #months and year
	ax.xaxis.set_major_formatter(monthsFmt)
	ax.xaxis.set_minor_locator(years)
	#customizing graph frame 
	rcParams['xtick.major.pad']='8'	

	ax.set_xlim(startDate, endDate)
	ax.set_xlabel("({} - {})".format(startDate, endDate))
	ax.set_ylabel("OBV")

	fig.autofmt_xdate()
	plt.show()


createChart(valArr, startDate, endDate, obvDates)
