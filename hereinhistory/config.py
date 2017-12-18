#db stuff
SQLALCHEMY_DATABASE_URI = "mysql://root:smallmiricles@localhost/hereinhistory"
SQLALCHEMY_TRACK_MODIFICATIONS = True
SECRET_KEY = "as;dlfja;sdlfkja;sldkfj"

#general
DEBUG = True

import os
BASE_DIR = os.path.abspath(os.path.dirname(__file__))

THREADS_PER_PAGE = 2

#security
CSRF_ENABLED = True
CSRF_SESSION_KEY = "kddkkdkdkdkkdkdk"
SECRET_KEY = "ljkasd;flkjas;dlfkjas;dfkajs;dflj"


UPLOAD_FOLDER = '/home/www/bitcamp/app/static/pdf'
ALLOWED_EXTENSIONS = set(['pdf'])
