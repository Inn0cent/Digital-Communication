import os
from matplotlib import pyplot as plt
fileNames = ["Chinese", "Color", "coursework", "Large", "Medium", "Small", "video"]
# fileNames = ["Color", "Large", "video"]


def openFile(fileName, mode):
    if mode == "r":
        return open(os.getcwd() + "/Log Files/" + fileName + ".log", 'r').read()
    return open(os.getcwd() + "/Log Files/" + fileName + ".log", 'w')


# Window Bits, Length Bits, Encoding Time Taken (ms), Decoding Time Taken (ms), Compression Rate (Original / Compressed)
def plotGraph(fileName):
    content = openFile("Windows" + fileName, "r").split("&")
    titleNames = ["Encoder Run Time " + ("(s)" if len(content[1]) > 4 else "(ms)"), "Decoder Run Time (ms) ", "Compression Rate"]
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
                encodingTimes[i].append(float(data[2]))
                decodingTimes[i].append(int(data[3]))
                compression[i].append(float(data[4]))
    parameters = [encodingTimes, decodingTimes, compression]
    # Get Parameters from Huffman
    content = openFile("Huffman" + fileName, "r").split(",")
    huffmanEncoding, huffmanDecoding, huffmanCompression = float(content[0]), int(content[1]), float(content[2])
    huffmanParameters = [huffmanEncoding, huffmanDecoding, huffmanCompression]
    for graphIndex in range(len(titleNames)):
        print(titleNames[graphIndex])
        color = ['r', 'g', 'b']
        for parameterIndex in range(len(windowSizes)):
            plt.scatter(windowSizes[parameterIndex], parameters[graphIndex][parameterIndex], color=color[parameterIndex])
            plt.plot(windowSizes[parameterIndex], parameters[graphIndex][parameterIndex], color=color[parameterIndex], linewidth=1, label= str(parameterIndex + 1) + " Bytes")
        plt.plot([1, 18], [huffmanParameters[graphIndex], huffmanParameters[graphIndex]], label= "Huffman")
        plt.title(titleNames[graphIndex])
        plt.xlabel('Window Bits')
        plt.ylabel(titleNames[graphIndex])
        plt.legend(loc = "upper right")
        plt.savefig(os.getcwd() + "/Graph Files/More " + fileName + " " + titleNames[graphIndex] + ".png")
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


def cleanLarge():
    content = open(os.getcwd() + "/Log Files/LargePicture.txt", "r").read()
    file = open(os.getcwd() + "/Log Files/LargePicture.txt", "w")
    for dataSegment in content.split("-"):
        for parameter in dataSegment.split("#"):
            if len(parameter) > 2:
                index = 0
                print(len(parameter.split("\n")))
                for line in parameter.split("\n"):
                    if len(line) > 4:
                        if index != 3 and index != 5 and index != 7:
                            file.write(line + "\n")
                        index += 1
            file.write("#\n")
        file.write("-\n")

def cleanHuffman(fileName):
    content = open(os.getcwd() + "/log Files/Huffman" + fileName + ".log", "r").read()
    file = open(os.getcwd() + "/Log Files/Huffman" + fileName + ".log", "w")
    data = content.split(",")
    data[0], data[2] = str(float(data[0]) / 1000), str(round(float(data[2]), 3))
    file.write(",".join(data))


def cleanWindows(fileName):
    content = open(os.getcwd() + "/log Files/Windows" + fileName + ".log", "r").read().split("\n")
    file = open(os.getcwd() + "/Log Files/Windows" + fileName + ".log", "a+")
    file.write("&True")


cleanLarge()