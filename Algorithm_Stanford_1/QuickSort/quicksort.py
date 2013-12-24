#!/usr/bin/python
totalComparison = 0

def quickSort(anArray, start, end):
    if end - start <= 0:
        return anArray
    global totalComparison
    totalComparison = totalComparison + end - start
    p = choosePivot(anArray, start, end)
    newPivot = partitionArray(anArray, p, start, end)
    quickSort(anArray, start, newPivot - 1)
    quickSort(anArray, newPivot + 1, end)
    
def choosePivot(anArray, start, end):
    first = anArray[start]
    last = anArray[end]
    
    middle = int((start + end)/2)
    median = anArray[middle]
    pivot = 0
    if first < last:
        if median < first:
            pivot = start
        elif median > last:
            pivot = end
        else:
            pivot = middle 
    else: #last < first
        if median < last:
            pivot = end
        elif median > first:
            pivot = start
        else:
            pivot = middle
    return pivot
    
    #return start
    return end
            
def partitionArray(anArray, p, start, end):
    anArray[start], anArray[p] = anArray[p], anArray[start]
    pivot = anArray[start]
    i = start + 1
    for j in range(start + 1, end + 1):
        if anArray[j] < pivot:
            anArray[j], anArray[i] = anArray[i], anArray[j]
            i = i + 1
    anArray[start], anArray[i - 1] = anArray[i - 1], anArray[start]
    return i - 1
    
def main():
    numberSource = open('QuickSort.txt', 'r')
    numberList = []
    theLength = 0
    for number in numberSource:
        numberList.append(eval(number))
        theLength = theLength + 1
    numberSource.close()
    quickSort(numberList, 0, theLength - 1)
    print numberList[:100]
    print "comparisons: ", str(totalComparison)
main()
