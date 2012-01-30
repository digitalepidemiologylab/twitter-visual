# Pipe in reindexed-tweets.txt
# Outputs the sentiment score of each user
#
# Score is calculated by (pos-neg)/(pos+neg+neutral)

import sys

positive = {}
negative = {}
neutral = {}
users = []

#sys.stdout.write('uid\tsentiment score\ttotal vac tweets\n')

for line in sys.stdin:
    sp = line.split('\t')
    uid = sp[2].strip(); 
    if not (uid in users) :
        positive[uid]=0
        negative[uid]=0
        neutral[uid]=0
        users = users + [uid]
    
    classification = sp[5].strip()
    
    if classification == '+' :
        positive[uid] = positive[uid]+1
        
    if classification == '-' :
        negative[uid] = negative[uid]+1
        
    if classification == 'O' :
        neutral[uid] = neutral[uid]+1
        
for uid in users:
    sys.stdout.write(uid)
    sys.stdout.write('\t')
    sys.stdout.write(str(1.0*(positive[uid]-negative[uid])/(positive[uid]+negative[uid]+neutral[uid])))
    sys.stdout.write('\t')
    sys.stdout.write(str(positive[uid]+negative[uid]+neutral[uid]))
    sys.stdout.write('\n')
    