# Pipe in reindexed-graph.txt
# Outputs "id   friend_count follower_count" (tab seperated)

import sys

for line in sys.stdin:
    sp = line.split('|')
    if sp[1] == 'I':
        sys.stdout.write(sp[0])
        sys.stdout.write('\t')
        sys.stdout.write(sp[2]);