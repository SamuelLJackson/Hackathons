import subprocess, shlex
from subprocess import call
import random
import re
import xml.etree.ElementTree as ET
from sets import Set
import MySQLdb

A = 65
Z = 90
a = 97
z = 122

####################################################################
# Input: Raw text from the pdf (text) , Empty Dictionary (keyWords)#
# Output: Dictionary of all bolded words                           #   
####################################################################
def makeKeyWords(text, keyWords):
    # Make the keyword set
    out_lines = text.split("\n")
    for line in out_lines:
        line = line.strip()
        if line == line.upper() and line != "" and line[0] > "9" and len(line) > 2 and ("+" not in line and "=" not in line and "-" not in line and "/" not in line and "*" not in line and "." not in line and "\\" not in line and "[" not in line and "]" not in line and ")" not in line and "(" not in line and "," not in line and ":" not in line):
            keyWords.add(line)
            
    return keyWords
        
        
################################################################
# Input: List of strings (sentances), set of strings (keyWords)#
# Output: list of strings that contain a keyword               #
################################################################
def makeTargetSentances(sentances, keyWords):
    target_sentances = []
    # make the target sentances
    for line in sentances:
        for keyWord in keyWords:
            if ((keyWord.lower() in line or keyWord in line) and "=" not in line):
                target_sentances.append(line)
                
    return target_sentances
                        

###############################################################################################################
# Input: List of strings (sentances), set of strings (keyWords), dictionary of stings and lists (keyWordsDict)#
# Output: completed keyWords dictionary                                                                       # 
###############################################################################################################
def makeQuizLines(sentances, keyWords, keyWordsDict):
    
    # Go through the lines and create quiz questions by blanking out keywords
    for line in sentances:
        
        for keyWord in keyWords:
            newLine = line
            if ((keyWord.lower() in line or keyWord in line) and keyWord != line and len(keyWord) > 2):
#                if keyWordsDict[keyWord] == []:
                newLine = line.replace(keyWord.lower(), " __________")
                newLine = newLine.replace(keyWord, " __________")
                if (len(newLine) < 250 and newLine not in keyWordsDict[keyWord]):
                    keyWordsDict[keyWord].append(newLine)

    return keyWordsDict


def writeChapter(dictOfKeyWords, chapter):
    chapterFile = open("chapters/"+chapter, 'w')
    for key in dictOfKeyWords:
        if (len(dictOfKeyWords[key]) > 0):
            randomValue = random.randrange(0, len(dictOfKeyWords[key]))
            chapterFile.write(dictOfKeyWords[key][randomValue] + "\n" +"\n")


# Input: xmlFile
# Output: list of page number
def getChapterPageNumbers(xmlFile):
    tree = ET.parse(xmlFile)
    root = tree.getroot()
    numberList = []
    chapterTitle = []
    for pageno in root.findall('outline'):
        if pageno.get('level') == "2":
            number = pageno.find('pageno').text
            numberList.append(number)
            title = pageno.get('title')
            chapterTitle.append(title)
    return numberList, chapterTitle

def fixXml(fileName):
    originalXML = open("tableContentsOld.txt", "r")
    newXML = open("tableContents.txt", "w")
    i = 1
    noWriteZone = False
    lock = False
    for line in originalXML:

        if "outline" in line and lock == False and i != 1:
            noWriteZone = True
        if noWriteZone == False:
            newXML.write(line)
            
        if "/outline" in line and lock == False:    
            noWriteZone = False
            lock = True
        i += 1
    originalXML.close()
    newXML.close()
    call("mv tableContents.txt tableContents.xml", shell=True)

def getChapter(pdfFile, startPage, endPage):
    numPages = int(endPage) - int(startPage)
    pagesString = ""
    for i in range(numPages):
        if ( i+int(startPage) < int(endPage) - 1):
            pagesString += str(int(startPage) + i) + ", "
        else:
            pagesString += str(int(startPage) + i)

    out_bytes = subprocess.check_output( ["pdf2txt.py", "-p", pagesString, pdfFile] )
    return out_bytes

def getStandAlone(pdfFile):
    return subprocess.check_output( ["pdf2txt.py", pdfFile] )

def lineCleaner(line):
    newLine = line
    if "\"\"" in newLine:
        newLine = newLine.replace("\"\"", "")

    if "\"" in newLine:
        newLine = newLine.replace("\"", "")
        
    if "\"" in newLine:
        newLine = newLine.replace("\"", "")

    if "\"" in newLine:
        newLine = newLine.replace("\"", "")

    if "\\" in newLine:
        newLine = newLint.replace("\\", "")

    if "|" in newLine:
        newLine = newLine.replace("|", "")

    if "\'" in newLine:
        newLine = newLine.replace("\'", "")

    if "," in newLine:
        newLine = newLine.replace(",", "")

    if "\r" in newLine:
        newLine = newLine.replace("\r", "")

    if "\t" in newLine:
        newLine = newLine.replace("\t", "")

    if "\0" in newLine:
        newLine = newLine.replace("\0", "")

    if "/f" in newLine:
        newLine = newLine.replace("/f", "")
        
    return newLine

def translatePDFtoDataBase(pdfFile, textBookName):

    ########################## CHANGE #########################################
    call("dumppdf.py -T " + pdfFile + " > tableContentsOld.txt", shell=True)
    fixXml("tableContentsOld.txt")
    pageNumbers, chapterTitles = getChapterPageNumbers("tableContents.xml")

    myDB = MySQLdb.connect(host="localhost", user="root", db="textbooktest", passwd="smallmiricles")
    cur = myDB.cursor()

    trunkatedChapterTitles = []
    noChapters = False
    for chapter in chapterTitles:
        
        if chapter[0].isdigit():
            trunkatedChapterTitles.append(chapter)
            
    if len(trunkatedChapterTitles) == 0:
        noChapters = True
        trunkatedChapterTitles.append("Chapter 1")
        

    for i in range(len(trunkatedChapterTitles)):
    
            if noChapters == False:
                out_bytes = getChapter(pdfFile, pageNumbers[i], pageNumbers[i+1])
            else:
                out_bytes = getStandAlone(pdfFile)
            keyWords = Set()
            keyWordsDict = {}
            sentances = []
            target_sentances = []
    
            out_text  = out_bytes.decode("utf-8")
            out_text  = out_text.encode('ascii', 'ignore')

            # find all keywords in the text    
            keyWords = makeKeyWords(out_text, keyWords)
            
            # initialize the keyword dictionary
            # comprised of a {string:list}
            # string being the keyword and list being the list of sentances that contain that word
            for keyWord in keyWords:
                
                keyWordsDict[keyWord] = []
                
                # clean up the lines
                out_sentances_string = out_text.replace("-\n", "")
                out_sentances_string = out_sentances_string.replace("\n", " ")
                sentencePattern = re.compile("([A-Z][a-z].*?[A-Z a-z 0-9]\. )")
                out_sentances = sentencePattern.findall(out_sentances_string)
                
                target_sentances = makeTargetSentances(out_sentances, keyWords)
            
            # creates dictionary of sentances containing keywords
            keyWordsDict = makeQuizLines(target_sentances, keyWords, keyWordsDict)
           
            if (i == 0):
                titleQuery = "INSERT INTO books (title) VALUES (\"{:s}\")".format(textBookName)
                cur.execute(titleQuery)
                myDB.commit()
            
            getBookIdQuery = "SELECT id from books order by id desc limit 1"
            cur.execute(getBookIdQuery)
        
            bookId = cur.fetchall()[0][0]

            if noChapters == False:
                currentChapterTitle = chapterTitles[i]
            else:
                currentChapterTitle = "Chapter 1"
            
            chapterQuery = "INSERT INTO chapters (title, book_id) VALUES (\"{:s}\", \"{:d}\")".format(str(currentChapterTitle), int(bookId))
            cur.execute(chapterQuery)
            myDB.commit()
            
            getChapterId = "SELECT id from chapters order by id desc limit 1"
            cur.execute(getChapterId)
            
            chapterId = cur.fetchall()[0][0]
            
            for words in keyWords:
                
                wordsQuery = "INSERT INTO words (word, chapter_id) VALUE(\"{:s}\", \"{:d}\")".format(words, int(chapterId))
                cur.execute(wordsQuery)
                myDB.commit()
                
                getWordId = "SELECT id from words order by id desc limit 1"
                cur.execute(getWordId)
                wordId = cur.fetchall()[0][0]
                
                for sentence in keyWordsDict[words]:

                    if len(keyWordsDict[words]) > 1:
                        cleanSentence = lineCleaner(sentence)
                                    
                        sentenceQuery = "INSERT INTO sentences (sentence_text, word_id) VALUE (\"{:s}\", \"{:d}\")".format(cleanSentence, int(wordId))
                        cur.execute(sentenceQuery)
                        myDB.commit()
                    
        
                          
    cur.close()
    myDB.close()

           

        

