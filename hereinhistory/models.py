from app import db

class User(db.Model):
    __tablename__ = "users"

    id = db.Column(db.Integer, primary_key = True)
    email = db.Column(db.String(200))
    password = db.Column(db.String(20))
    def __init__(self, email, password):
        self.email = email
        self.password = password

    def is_authenticated(self):
        return True

    def is_active(self):
        return True

    def is_anonymous(self):
        return False

    def get_id(self):
        return unicode(self.id)

    def __repr__(self):
        return '<User %r>' % (self.email)

class tours(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    title = db.Column(db.String(300))
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'))

    user = db.relationship('User',foreign_keys=user_id)

    def __init__(self, title, user_id):
        self.title = title
        self.user_id = user_id


class stops(db.Model):
    id = db.Column(db.Integer, primary_key = True)
    latitude = db.Column(db.Float)
    longitude = db.Column(db.Float)
    description = db.Column(db.String(10000))
    stop_number = db.Column(db.Integer)
    title = db.Column(db.String(200))
    youtube_link = db.Column(db.String(300))
    image_link = db.Column(db.String(300))
    tour_id = db.Column(db.Integer, db.ForeignKey('tours.id'))
    date = db.Column(db.String(200))

    tour = db.relationship('tours', foreign_keys=tour_id)


    def __init__(self, latitude,longitude,description,
                 stop_number, tour_id, title,youtube_link,image_link, date):
        self.latitude = latitude
        self.longitude = longitude
        self.description = description
        self.stop_number = stop_number
        self.tour_id = tour_id
        self.title = title
        self.youtube_link = youtube_link
        self.image_link = image_link
        self.date = date


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
                        

