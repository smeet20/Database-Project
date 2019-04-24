from flask import Flask
from flask import jsonify
from flask import render_template
from flask import g
from flask import request
from flask import session
from flask import redirect
from flask import url_for

from passlib.hash import sha256_crypt

import sqlite3

from textblob import TextBlob
from textblob.sentiments import NaiveBayesAnalyzer

app = Flask(__name__)
app.secret_key = 'I love rach**'

import threading

from db import *

DB = 'db/database.db'
DoneInit = False

def get_db():    
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = sqlite3.connect(DB)
    db.row_factory = sqlite3.Row
    
    return db

def query_db(query, args=(), one=False):
    cur = get_db().execute(query, args)
    rv = cur.fetchall()
    cur.close()
    return (rv[0] if rv else None) if one else rv

def create_user(name, email, password):
    pass_sh = sha256_crypt.encrypt(password)
    cur = get_db()
    query = cur.execute('insert into users (name, email, password) values (?, ?, ?)', (name, email, pass_sh))
    cur = get_db().execute(query)
 
def validate_user(email, password):
    query = 'select * from users where email = ?'
    record_pass = query_db(query, args=(email,), one=True)[3]
    result = sha256_crypt.verify(password, record_pass)
    return result

@app.route('/login', methods=['POST'])
def login():
    email = request.form['email']
    password = request.form['password']
    res = validate_user(email, password)
    if res:
        session['email'] = email
        return redirect(url_for('index'))
 
@app.route('/logout', methods=['POST'])
def logout():
    session.pop('email', None)
    return redirect(url_for('index'))

@app.route('/signup', methods = ['POST'])
def signup():
    name = request.form.get('name')
    email = request.form.get('email')
    password = request.form.get('password')
    if not name: return
    if not email: return
    if not password: return
    pass_sh = sha256_crypt.encrypt(password)
    db = get_db()
    try:
        db.execute('insert into users (name, email, password) values (?, ?, ?)', (name, email, pass_sh))
        db.commit()
        session['email'] = email
    except:
        db.rollback()
    return redirect(url_for('index'))

@app.teardown_appcontext
def close_connection(exception):
    db = getattr(g, '_database', None)
    if db is not None:
        db.close()
        
@app.route('/')
def index():
    email = session.get('email')
    loggedIn = True if email else False
    return render_template('index.html', loggedIn=loggedIn, email=email)


@app.route('/api/sentiment')
def sentiment():
    text = request.args.get('text')
    if not text:
        return jsonify({})
    blob = TextBlob(text, analyzer=NaiveBayesAnalyzer())
    sentiment = blob.sentiment
    c = '😊'
    if sentiment.classification == 'neg':
        c = '🤬'
    return jsonify({'c': c, 'p': sentiment.p_pos, 'n': sentiment.p_neg})


@app.route('/api/search')
def search():
    q = request.args['q']
    cnt = request.args.get('cnt')
    if not cnt:
        cnt = 10
    adv = request.args.get('adv')
    if not adv:
        adv = 0
    adv = int(adv)
    ordr = request.args.get('ord')
    if not ordr:
        ordr = 'desc'
    if ordr == '0':
        ordr = 'asc'
    else:
        ordr = 'desc'
    title = 'Tweets like "{}"'.format(q)
    
    lk = request.args.get('lk')
    rt = request.args.get('rt')
    rp = request.args.get('rp')
    lk = (int(lk) / 100) if lk else 0
    rp = (int(rp) / 100) if lk else 0
    rt = (int(rt) / 100) if lk else 0
    prm = '{}*likes + {}*retweets + {}*replies'.format(lk, rt, rp)
    if lk + rp + rt == 0:
        prm = 'time'
    
    query = "select * from tweets where text like '%{}%' order by {} {} limit {}".format(q, prm, ordr, cnt)
    if adv == 2:
        query = "select tweets.* from tweets natural join tweet_hashtag natural join hashtags natural join event_hashtag natural join events where name like '%{}%' order by {} {} limit {}".format(q, prm, ordr, cnt)
        title = 'Tweets from events like "{}"'.format(q)
    elif adv == 3:
        query = "select tweets.* from tweets natural join author where name like '%{}%' order by {} {} limit {}".format(q, prm, ordr, cnt)
        title = 'Tweets from author like "{}"'.format(q)
    res = query_db(query)
    tweets = tweets_res_helper(res)
    data = {'tweets': tweets, 'type': title}
    return jsonify(data)


@app.route('/api/recent-tweets', methods=['POST'])
def recent_tweets():
    res = query_db('select * from tweets order by time desc limit 10')
    tweets = tweets_res_helper(res)
    data = {'tweets': tweets, 'type': 'Recent Tweets'}
    return jsonify(data)


@app.route('/api/top-tweets', methods=['POST'])
def top_tweets():
    res = query_db('select * from tweets LIMIT 5')
    tweets = tweets_res_helper(res)
    data = {'tweets': tweets, 'type': 'Top Tweets'}
    return jsonify(data)


def tweets_res_helper(res):
    tweets = []
    tid = 0
    for tweet in res:
        tweets.append({'id': 'twt-{}'.format(tid), 'text': tweet['text'], 'likes': tweet['likes'], 'retweets': tweet['retweets'], 'replies': tweet['replies'], 'time': tweet['time']})
        tid += 1
    return tweets
    

if __name__ == '__main__':
    threading.Thread(target=collect_data).start()
    app.run(host='0.0.0.0', debug=True)

