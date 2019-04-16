from flask import Flask
from flask import jsonify
from flask import render_template

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html', loggedIn=True)

@app.route('/api/top-tweets', methods=['POST'])
def top_tweets():
    tweets = []
    import random
    for i in range(random.randint(2, 10)):
        tweets.append({'title': random.randint(1, 100)})
    fake_data = {'tweets': tweets}
    return jsonify(fake_data)

if __name__ == '__main__':
    app.run(debug=True)
