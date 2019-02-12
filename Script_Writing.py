import os
from matplotlib import pyplot as plt
fileNames = ["Chinese", "Color", "coursework", "Large", "Medium", "Small", "video"]
# fileNames = ["Chinese", "coursework", "Small", "video"]


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
    compression = [[], [], []]
    for i in range(len(content)):
        content[i] = content[i].split("\n")
        for line in content[i]:
            data = line.split(",")
            if len(line) > 2:
                windowSizes[i].append(int(data[0]))
                encodingTimes[i].append(int(data[2]))
                decodingTimes[i].append(int(data[3]))
                # compression[i].append(float(data[4]))
    machineWindowSizes, machineEncodingTimes, machineDecodingTimes  = [[], [], []], [[], [], []], [[], [], []]
    parameters = [encodingTimes, decodingTimes]
    content = openFile("Windows" + fileName, "r").split("&")
    content = content[0].split("#")
    for i in range(len(content)):
        content[i] = content[i].split("\n")
        for line in content[i]:
            data = line.split(",")
            if len(line) > 2:
                machineWindowSizes[i].append(int(data[0]))
                machineEncodingTimes[i].append(round(float(data[2]), 3))
                machineDecodingTimes[i].append(int(data[3]))
    machineParameters = [machineEncodingTimes, machineDecodingTimes]
    # Get Parameters from Huffman
    for graphIndex in range(len(titleNames)):
        color = ['r', 'g', 'b']
        for parameterIndex in range(len(windowSizes)):
            plt.title(titleNames[graphIndex])
            plt.xlabel('Window Bits')
            plt.ylabel(titleNames[graphIndex] + " " + str(parameterIndex + 1) + " Bytes")
            plt.scatter(windowSizes[parameterIndex], parameters[graphIndex][parameterIndex], color=color[parameterIndex])
            plt.plot(windowSizes[parameterIndex], parameters[graphIndex][parameterIndex], color=color[parameterIndex], linewidth=1, label="Mac " + str(parameterIndex + 1) + " Bytes")
            plt.scatter(machineWindowSizes[parameterIndex], machineParameters[graphIndex][parameterIndex], color=color[parameterIndex])
            plt.plot(machineWindowSizes[parameterIndex], machineParameters[graphIndex][parameterIndex], color=color[parameterIndex], linewidth = 0.5, linestyle='-.', label= "Windows " + str(parameterIndex + 1) + " Bytes")
            plt.legend(loc = "upper right")
            plt.savefig(os.getcwd() + "/Graph Files/Comparison " + fileName + " " + str(parameterIndex + 1) + " " + titleNames[graphIndex] + ".png")
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
    content = open(os.getcwd() + "/log Files/Huffman" + fileName + ".log", "r").read()
    file = open(os.getcwd() + "/Log Files/Huffman" + fileName + ".log", "w")
    data = content.split(",")
    data[0], data[2] = str(int(data[0]) / 1000), str(round(float(data[2]), 3))
    file.write(",".join(data))


def cleanWindows(fileName):
    content = open(os.getcwd() + "/log Files/Windows" + fileName + ".log", "r").read().split("\n")
    file = open(os.getcwd() + "/Log Files/Windows" + fileName + ".log", "a+")
    file.write("&True")

for file in fileNames:
    plotGraph(file)
