import os
from matplotlib import pyplot as plt
import numpy as np
fileNames = ["Chinese", "Color", "coursework", "Large", "Medium", "Small", "video"]
# fileNames = ["Medium", "Color", "video"]


def openFile(fileName, mode):
    if mode == "r":
        return open(os.getcwd() + "/Log Files/" + fileName + ".log", 'r').read()
    return open(os.getcwd() + "/Log Files/" + fileName + ".log", 'w')


# Window Bits, Length Bits, Encoding Time Taken (ms), Decoding Time Taken (ms), Compression Rate (Original / Compressed)
def plotGraph(fileName):
    content = openFile(fileName, "r").split("&")
    titleNames = ["Encoder Run Time " + ("(s)" if len(content[1]) > 4 else "(ms)"), "Decoder Run Time (ms) "]
    content = content[0].split("#")
    windowSizes = [[], [], []]
    encodingTimes = [[], [], []]
    decodingTimes = [[], [], []]
    for i in range(len(content)):
        content[i] = content[i].split("\n")
        for line in content[i]:
            data = line.split(",")
            if len(line) > 2:
                windowSizes[i].append(int(data[0]))
                encodingTimes[i].append(int(data[2]))
                decodingTimes[i].append(int(data[3]))
    parameters = [encodingTimes, decodingTimes]
    for graphIndex in range(len(titleNames)):
        color = ['r', 'g', 'b']
        plt.title(titleNames[graphIndex])
        plt.xlabel('Window Bits')
        plt.ylabel(titleNames[graphIndex])
        for parameterIndex in range(len(windowSizes)):
            plt.scatter(windowSizes[parameterIndex], parameters[graphIndex][parameterIndex], color=color[parameterIndex])
            plt.plot(windowSizes[parameterIndex], parameters[graphIndex][parameterIndex], color=color[parameterIndex], linewidth=1, linestyle=':')
        plt.savefig(os.getcwd() + "/Graph Files/" + fileName + " " + titleNames[graphIndex] + ".png")
        plt.close()


def cleanFile(fileName):
    content = open(os.getcwd() + "/log Files/" + fileName + ".log", "r").read()
    file = open(os.getcwd() + "/Log Files/" + fileName + ".log", "w")
    for line in content.split("\n"):
        if len(line) > 10:
            data = line.split(",")
            data[2] = str(round(float(data[2])))
            file.write(",".join(data) + "\n")
        else:
            file.write(line + "\n")


def fileReset(fileName):
    content = open(os.getcwd() + "/log Files/Mac" + fileName + ".log", "r").read()
    file = open(os.getcwd() + "/Log Files/" + fileName + ".log", "w")
    for line in content.split("\n"):
        file.write(line + "\n")
    content = open(os.getcwd() + "/log Files/MacBig" + fileName + ".log", "r").read()
    for line in content.split("\n"):
        if len(line) > 2:
            file.write(line + "\n")


def cleanHuffman(fileName):
    content = open(os.getcwd() + "/log Files/MacHuffman" + fileName + ".log", "r").read()
    file = open(os.getcwd() + "/Log Files/MacHuffman" + fileName + ".log", "w")
    file.write(content.split("\n")[0])

for file in fileNames:
    plotGraph(file)