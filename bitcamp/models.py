from app import db

class books(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    title = db.Column(db.String(500))

    def __init__(self, title):
        self.title = title

class chapters(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    title = db.Column(db.String(500))
    book_id = db.Column(db.Integer, db.ForeignKey('books.id'))

    book = db.relationship('books', foreign_keys=book_id)
    def __init__(self, title, book_id):
        self.title = title
        self.book_id = book_id

class words(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    word = db.Column(db.String(500))
    chapter_id = db.Column(db.Integer, db.ForeignKey('chapters.id'))

    chapter = db.relationship('chapters', foreign_keys=chapter_id)

    def __init__(self, word, chapter_id):
        self.word = word
        self.chapter_id = chapter_id

class sentences(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    sentence_text = db.Column(db.String(10000))
    word_id = db.Column(db.Integer, db.ForeignKey('words.id'))

    word = db.relationship('words', foreign_keys=word_id)

    def __init__(self, sentence_text, word_id):
        self.sentence_text = sentence_text
        self.word_id = word_id
                        

'''
class tests(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    word_id = db.Column(db.Integer, db.ForeignKey('word_id'))
    answered = db.Column(db.Integer)

    word = db.relationship('words', foreign_keys=word_id)

    def __init__(self, word_id):
        self.word_id = word_id
        self.answered = 0
    
'''
