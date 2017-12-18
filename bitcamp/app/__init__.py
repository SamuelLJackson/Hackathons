from flask import Flask, render_template, request, redirect, url_for, session
from werkzeug import secure_filename
from pdfInterpreterDraft1 import translatePDFtoDataBase
import os
import MySQLdb
import json

from flask_sqlalchemy import SQLAlchemy
app = Flask(__name__)
app.config.from_object('config')
db = SQLAlchemy(app)

from models import words, sentences, books, chapters

@app.route("/")
def index():
    return redirect(url_for('menu'))

@app.route("/load", methods=['GET', 'POST'])
def load():
    if request.method == "POST":
        file = request.files['file']
        filename = secure_filename(file.filename)
        file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
        translatePDFtoDataBase("/home/www/bitcamp/app/static/pdf/" + filename, request.form['textBook'])
    return render_template('loading.html', active="load")

@app.route("/menu", methods=['GET', 'POST'])
def menu():
    session['score'] = 0
    bookList = books.query.all()
    bookTitles = []
    bookChapters = []
    for book in bookList:
        bookID = int(book.id)
        tempBookList = []
        chapterList = chapters.query.filter_by(book_id=bookID).all()
        bookTitles.append(book.title)
        bookChapters.append(chapterList)

    basicInfo = {"bookTitles":bookTitles, "bookChapters":bookChapters}
    return render_template('menu.html',basicInfo=basicInfo, active="menu")

@app.route("/proxy", methods=['GET','POST'])
def proxy():
    session['attemptedAnswer'] = request.form['attemptedAnswer']
    session['questIndex'] = int(request.form['questIndex'])
    return redirect(url_for('test'))

@app.route("/test", methods=['GET', 'POST'])
def test():

    mydb = MySQLdb.connect(host='localhost',user='root',passwd='smallmiricles',db='textbooktest')
    cur = mydb.cursor()

    if request.method == "POST":
        if request.form['pressed'] == "chapterTitle":
            #create test table
            cur.execute("INSERT INTO test_ids()VALUES()")
            mydb.commit()
            cur.execute("SELECT id from test_ids order by id desc limit 1")
            result = cur.fetchall()[0][0]
            cur.execute("CREATE TABLE test{:s} (id int auto_increment primary key, word_id int, answered tinyint(1) DEFAULT 0, sentence_index mediumint DEFAULT 0, num_sentences mediumint, num_fails smallint DEFAULT 0, FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE);".format(str(result),str(result)))
            mydb.commit()

            chapterID = int(request.form['id'])
            session['chapterID'] = chapterID

            wordList = words.query.filter_by(chapter_id=chapterID).all()

            session['testID'] = result
            # insert questions into test table
            sentenceList = []
            for word in wordList:
                wordID = int(word.id)
                tempSentences = sentences.query.filter_by(word_id=wordID).all()
                if tempSentences:
            
                    tempSentenceList = []
                    for sentence in tempSentences:
                        sentenceHalves = str(sentence.sentence_text).split("__________")
                        tempSentenceList.append(sentenceHalves)

                    sentenceList.append(tempSentenceList)
                    cur.execute("INSERT INTO test{:s} (word_id,num_sentences) VALUES ({:d},{:d})".format(str(result), int(word.id),len(tempSentenceList)))
                    mydb.commit()

        else:

            # update the test table
            questionID = int(request.form['questIndex']) + 1
            if request.method == "POST" and request.form['pressed'] == "left":
                cur.execute("UPDATE test{:s} SET sentence_index = sentence_index - 1 WHERE id={:d}".format(str(session['testID']), questionID))
                mydb.commit()

            elif request.method == "POST" and request.form['pressed'] == "right":
                cur.execute("UPDATE test{:s} SET sentence_index = sentence_index + 1 WHERE id={:d}".format(str(session['testID']), questionID))
                mydb.commit()
                            

            sentenceList = []
            wordList = words.query.filter_by(chapter_id=session['chapterID']).all()
            for word in wordList:
                wordID = int(word.id)
                tempSentences = sentences.query.filter_by(word_id=wordID).all()
                if tempSentences:
                    tempSentenceList = []
                    for sentence in tempSentences:
                        sentenceHalves = str(sentence.sentence_text).split("__________")
                        tempSentenceList.append(sentenceHalves)

                    sentenceList.append(tempSentenceList)
    else:
        wordList = words.query.filter_by(chapter_id=session['chapterID']).all()
        word = wordList[int(session['questIndex'])]

        if str(session['attemptedAnswer']).upper() == str(word.word):
            #answers correctly
            cur.execute("UPDATE test{:s} SET answered=1 WHERE id={:d}".format(str(session['testID']), int(session['questIndex'])+1))
            mydb.commit()
            session['score'] += 10
        else:
            #answers incorrectly
            cur.execute("UPDATE test{:s} SET num_fails = num_fails + 1 WHERE id={:d}".format(str(session['testID']), int(session['questIndex'])+1))
            mydb.commit()
            session['score'] -= 1

        sentenceList = []
        wordList = words.query.filter_by(chapter_id=session['chapterID']).all()
        for word in wordList:
            wordID = int(word.id)
            tempSentences = sentences.query.filter_by(word_id=wordID).all()
            if tempSentences:
                tempSentenceList = []
                for sentence in tempSentences:
                    sentenceHalves = str(sentence.sentence_text).split("__________")
                    tempSentenceList.append(sentenceHalves)

                sentenceList.append(tempSentenceList)
            

    dictCursor = mydb.cursor(MySQLdb.cursors.DictCursor)
    dictCursor.execute("SELECT * from test{:s};".format(str(session['testID'])))
    quiz = dictCursor.fetchall()

    cur.close()
    dictCursor.close()
    mydb.close()

    chapter = chapters.query.get(session['chapterID'])
    testTitle = chapter.title + " Vocab Test"
    
    return render_template("test.html", quiz=quiz, sentenceList=sentenceList, testTitle=testTitle,
                           wordList=wordList, score=session['score'], active="test")

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
