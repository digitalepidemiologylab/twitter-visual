#pipe in tab seperated table of values
#outputs n rows with largest values in the k-th column
#usage python largestnum.py n k < input
#
#k starts at 0!!!

import sys

n = int(sys.argv[1])
k = int(sys.argv[2])

array = []

for line in sys.stdin:
   if len(array) < n :
       array = array+[[int(line.split('\t')[k]) , line]]
       array.sort()
   else:
       if array[0][0] < int(line.split('\t')[k]) :
            del array[0]
            array = array+[[int(line.split('\t')[k]) , line]]
            array.sort()
        


array.sort(reverse=True)

for x in array:
    sys.stdout.write(x[1])