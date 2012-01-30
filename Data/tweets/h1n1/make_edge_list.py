# Outputs edge list of friends

import sys
import random

users = [] # list of tweeter_ids
tweets = {} #map of list of tweets for each tweeter

for line in open('reindexed-tweets.txt','r'):
    sp = line.split('\t')
    uid = sp[2].strip()
    if tweets.get(uid,None) == None:
        users = users + [uid]
        tweets[uid] = []
    tweets[uid] = tweets[uid]+[sp[0].strip()]

users = set(users)

for line in open('reindexed-graph.txt','r') :
    sp = line.split('|')
    tweetid = sp[0].strip()
    if not(sp[1] == 'I'):
        friends = sp[2].split(',')
        uid = ''
        for user in users:
            if tweetid in tweets[user]:
                for fr in friends :
                    sys.stdout.write(sp[0])
                    sys.stdout.write(';')
                    sys.stdout.write(fr.strip())
                    sys.stdout.write(';')
                    sys.stdout.write(sp[1])
                    sys.stdout.write('\n')
                break
                    
