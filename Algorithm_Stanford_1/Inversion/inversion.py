#! /usr/bin/python

import string
import sys

globalCounter = 0

def merge_and_count(lArray, rArray, left, right):
    mergedArray = [None] * (left + right)
    m = 0
    n = 0
    for i in range(left + right):
        if (m <= left - 1) and (n <= right - 1):
            if (lArray[m] < rArray[n]):
                mergedArray[i] = lArray[m]
                m = m + 1
            elif (lArray[m] > rArray[n]):
                mergedArray[i] = rArray[n]
                n = n + 1
                global globalCounter
                globalCounter = globalCounter + left - m
        elif (m > left - 1) and (n <= right - 1):
            mergedArray[i] = rArray[n]
            n = n + 1
            m = m + 1
        elif (m <= left - 1) and (n > right - 1):
            mergedArray[i] = lArray[m]
            m = m + 1
    return mergedArray

def sort_and_count(intArray, number):
    if number == 1:
        return intArray
    else:
        left_half = int(number / 2)
        right_half = number - left_half
        left_array = sort_and_count(intArray[:left_half], left_half)
        right_array = sort_and_count(intArray[left_half:], right_half)
        return merge_and_count(left_array, right_array, left_half, right_half)

def main():
    intergerArray = open("IntegerArray.txt", 'r')
    intergerList = []
    numberCounter = 0
    for number in intergerArray:
        numerical = eval(number)
        intergerList.append(numerical)
        numberCounter = numberCounter + 1

    print "Total Number: ", str(numberCounter)
    sort_and_count(intergerList, numberCounter)

    print "number of inversion is: ", str(globalCounter)
    intergerArray.close()

main()
