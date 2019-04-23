import requests
from bs4 import BeautifulSoup

import time
import sqlite3

Queries = ['diwali', 'holi', 'end game']

Conn = None

def get_url_from_query(query):
    return "https://twitter.com/search?f=tweets&q={}".format(query)

def scrape(query):
    url = get_url_from_query(query)
    req = requests.get(url, headers={'user-agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.128 Safari/537.36'})
    html_doc = req.text
    soup = BeautifulSoup(html_doc, 'html.parser')
    
    # adding the event, we would not call scrape else.
    cur = Conn.cursor()
    cur.execute('insert into events (text) values (?)', (query,))
    for tweet in soup.select('li[data-item-type="tweet"]'):
        meta_data = tweet.div
        user_id = meta_data['data-user-id']
        user_name = meta_data['data-name']
        tweet_id = meta_data['data-tweet-id']
        parent_id = meta_data['data-conversation-id']
        timestamp = int(meta_data.select_one('._timestamp')['data-time-ms'])
        
        text = str(meta_data.select_one('.js-tweet-text-container'))
        
        try:
            image = meta_data.select_one('.AdaptiveMedia-container').select_one('img')['src']
            text += '<div class="TwitterImage" style="background-image: url({});"></div>'.format(image)
        except:
            pass
        
        likes = int(meta_data.select_one('.ProfileTweet-action--favorite > .ProfileTweet-actionCount')['data-tweet-stat-count'])
        replies = int(meta_data.select_one('.ProfileTweet-action--reply > .ProfileTweet-actionCount')['data-tweet-stat-count'])
        retweets = int(meta_data.select_one('.ProfileTweet-action--retweet > .ProfileTweet-actionCount')['data-tweet-stat-count'])
                        
        cur = Conn.cursor()
        try:
            cur.execute('insert into author (id, name) values (?, ?)', (user_id, user_name))
            cur.execute('insert into tweets (id, parent_id, time, likes, replies, retweets, text) values (?, ?, ?, ?, ?, ?, ?)', (tweet_id, parent_id, timestamp, likes, replies, retweets, text))
            Conn.commit()
        except sqlite3.Error as e:
            print(e)
            Conn.rollback()
            
def collect_data():
    global Conn
    Conn = sqlite3.connect('./database.db')
    while False:
        for query in Queries:
            scrape(query)
            time.sleep(120)
        
if __name__ == '__main__':
    collect_data()
    scrape('hello')
    scrape('Holi')
    scrape('Mia Bro')
