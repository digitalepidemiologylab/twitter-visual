#generates f^{+,-}_{1..7} as described in socialContagion.pdf
import random

users = {} #dictionary of all users


class TwitterUser(object):
    
    def __init__(self, id):
        self.id = id
        self.friends = set([])
        self.followers = set([])
        
        #tweet counts
        self.pos = 0
        self.neg = 0
        self.neutral = 0
        
        users[id] = self
        
    def addFriend(self, other_id):
        self.friends.add(other_id)
        
    def addFollow(self, other_id):
        self.followers.add(other_id)
        
    def addTweet(self, type):
        if type == '+' :
            self.pos = self.pos + 1
        elif  type == '-' :
            self.neg = self.neg + 1
        elif type == 'O' :
            self.neutral = self.neutral +1
        else :
            print 'error: odd tweet type \' 0 \' '.format(type)
            
    def sentimentScore(self):
        return 1.0 * (self.pos - self.neg) / (self.pos + self.neg + self.neutral)
        
    def tweetCount(self):
        return (self.pos + self.neg + self.neutral)
        
    def avgPosTweets(self):
        total = 0
        for user in self.friends:
            total = total + user.pos
        return 1.0 * total / len(self.friends)
        
    def posFriends(self):
        total = 0
        for user in self.friends:
            if sentimentScore(users[user]) > 0 :
                total = total + 1
        return total
        
    def avgFriendsOfPosFollow(self):
        total = 0
        for user in self.friends:
            if sentimentScore(users[user]) > 0 :
                total = total + len(users[user].followers)
        return 1.0 * total / posFriends(self)

    def avgFriendsOfPosFriends(self):
        total = 0
        for user in self.friends:
            if sentimentScore(users[user]) > 0 :
                total = total + len(users[user].friends)
        return 1.0 * total / posFriends(self)

    def reciprocatedPosFriends(self):
        total = 0
        for user in self.friends:
            if sentimentScore(users[user]) > 0 :
                if users[user].friends.contains(self) :
                    total = total + 1
        return 1.0 * total / posFriends(self)
        
    def avgSharedFriendsBetweenPositive(self):
        total = 0
        for user in self.friends:
            if sentimentScore(users[user]) > 0 :
                total = total + len(self.friends.intersection(users[user].friends))
        return 1.0 * total / posFriends(self)
        
    def avgSharedFollowBetweenPositive(self):
        total = 0
        for user in self.friends:
            if sentimentScore(users[user]) > 0 :
                total = total + len(self.followers.intersection(users[user].followers))
        return 1.0 * total / posFriends(self)
        
        
for line in open('reindexed-tweets.txt'):
    sp = line.split('\t')
    uid = sp[2].strip()
    if not (uid in users) :
        TwitterUser(uid)
    
    classification = sp[5].strip()
    users[uid].addTweet(classification)
    
print 'read tweets'
    
for line in open('noDupEdges'):
    sp = line.split(';')
    src = sp[0].strip()
    tar = sp[1].strip()
    type = sp[2].strip()
    if not (tar in users):
        continue
    if not (src in users):
        continue
    
    if random.randint(0,10000) == 0:
        print '{0}'.format(src)
        
    if type == 'FR':
        users[src].addFriend(tar)
    elif type == 'FO' :
        users[src].addFollow(tar)
    else:
        print 'error: odd edge type \' 0 \' '.format(type)
        
for user in users:
    print 'followers: {0} \t friends: {1}'.format(len(users[user].followers), len(users[user].friends))