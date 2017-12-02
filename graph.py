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
closingPrice = []

if not os.path.isfile(filename):
    print('File does not exist.')
else:  
    with open(filename) as f:
        content = f.read().splitlines()

check = True
for line in content:
    if "*****" in str(line):
    	check = False
    if check == True:
    	print(line)
    	vals = line.split()
    	print(vals)
    	ovbVals.append(float(vals[0].strip()))
    	date = dt.strptime(vals[2].strip(), '%Y-%m-%d').date()
    	obvDates.append(date)
    	closingPrice.append(float(vals[1].strip()))

startDate = obvDates[0]
endDate = obvDates[len(obvDates) - 1]

splitStartDate = str(startDate).split("-")
start = dt(int(splitStartDate[0]),int(splitStartDate[1]), int(splitStartDate[2])) #starting date to print stock charts as well
end = dt.today()
print(closingPrice[2])

valArr = np.array(ovbVals)
# print(valArr)
#apple = web.DataReader("AAPL", "yahoo", start, end)

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



def createChart(values, startDate, endDate, obvDates, yAxis):
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
	ax.set_ylabel(yAxis)

	fig.autofmt_xdate()
	plt.show()


createChart(valArr, startDate, endDate, obvDates, "OBV")
createChart(closingPrice, startDate, endDate, obvDates, "Price")
