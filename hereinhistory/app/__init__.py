from flask import Flask, render_template, request, redirect, url_for, session
from flask_login import LoginManager, logout_user, login_required, login_user
from werkzeug import secure_filename
import os
import MySQLdb
import json

from flask_sqlalchemy import SQLAlchemy
app = Flask(__name__)
app.config.from_object('config')
db = SQLAlchemy(app)

from models import tours, stops, User

login_manager = LoginManager(app)
login_manager.init_app(app)
login_manager.login_view = "login"

@login_manager.user_loader
def load_user(id):
    return User.query.get(int(id))

@app.route("/")
def index():
    tourList = tours.query.all()
    return render_template('home.html', tours=tourList,active="home")

@app.route("/login",methods=['GET','POST'])
def login():
    return render_template('login.html')

@app.route("/newTour", methods=['GET', 'POST'])
def newTour():
    mydb = MySQLdb.connect(host='localhost',user='root',passwd='smallmiricles',db='hereinhistory')
    cur = mydb.cursor()
    
    longitudes = request.form.getlist('longitudes')
    latitudes = request.form.getlist('latitudes')
    descriptions = request.form.getlist('descriptions')
    
    if request.method == "POST":
        tour = tours(request.form['titleTB'],1);
        db.session.add(tour);
        db.session.commit()
        tourID = tours.query.all()[-1]
        tourID = tourID.id
        for i in range(int(request.form['numStops'])):

            description = request.form['description' + str(i)]
            description = description.replace("0xc9", "")
            description = description.replace("0x99","")
            description = description.replace("0xc9","")
            description = description.replace("0x9b","")
            description = description.encode('ascii','ignore')
            stop = stops(
                request.form['lat' + str(i)],
                request.form['lng' + str(i)],
                description,
                i+1,
                tourID,
                request.form['stopTitle' + str(i)],
                request.form['urlForYouTube' + str(i)],
                request.form['imageLink' + str(i)],
                request.form['date' + str(i)])
            
            db.session.add(stop)
            
        db.session.commit()
            
    return render_template('newTour.html',active="new")


@app.route("/viewTour", methods=["GET","POST"])
def viewTour():
    twoTourList = []
    tourList = tours.query.all()
    mapData2 = []
    if request.method == "POST":
        tour = tours.query.get(int(request.form['firstTourID']))
        twoTourList.append(tour)
        mapData = stops.query.filter_by(tour_id=tour.id).all()

        if request.form['pressed'] == "secondTour":
            tour = tours.query.get(int(request.form['secondTourID']))
            twoTourList.append(tour)
            mapData2 = stops.query.filter_by(tour_id=tour.id).all()
        

    return render_template("mapTest.html",mapData=mapData,twoTourList=twoTourList,tourList=tourList,mapData2=mapData2)
