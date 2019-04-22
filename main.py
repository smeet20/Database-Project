from flask import Flask
from flask import jsonify
from flask import render_template
from flask import g
import sqlite3


app = Flask(__name__)

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

@app.teardown_appcontext
def close_connection(exception):
    db = getattr(g, '_database', None)
    if db is not None:
        db.close()
        
@app.route('/')
def index():
    return render_template('index.html', loggedIn=True)

@app.route('/api/top-tweets', methods=['POST'])
def top_tweets():
    tweets = []
    res = query_db('select * from tweets')
    for tweet in res:
        tweets.append({'text': tweet['text']})
    data = {'tweets': tweets}
    return jsonify(data)

if __name__ == '__main__':
    threading.Thread(target=collect_data).start()
    app.run(debug=True)

