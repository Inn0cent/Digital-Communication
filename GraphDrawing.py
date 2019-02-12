import matplotlib.pyplot as plt
import os


# Contents Format: lookBehind (window), lookAhead (l), Encoding Time, Decoding Time, Compression Rate
# Contents for Huffman: Encoding Time, Decoding Time, Compression Rate
fileNames = ["BW.log", "Chinese.log", "Color.log", "coursework.log", "Large.log", "LargePicture.log", "LargeVideo.log", "Medium.log", "Small.log", "video.log"]
def plotGraph():
    lzWindow, LZEncodingTime,  LZDecodingTime, LZCompressionRate = [], [], [], [],
    huffEncodingTime, huffDecodingTime, huffCompressionRate = [], [], []
    for fileName in fileNames:
        contents = openFile(fileName)
        for line in contents:
            line = line.split(",")
            lzWindow.append(int(line[0]))
            LZEncodingTime.append(int(line[2]))
            LZDecodingTime.append(int(line[3]))
            LZCompressionRate.append(float(line[3]))

    plt.plot(x, y)
    plt.show()


def openFile(fileName):
    return open(os.getcwd() + "/Log Files/" + fileName, "r").read().split("\n")


plotGraph("BW.log")
