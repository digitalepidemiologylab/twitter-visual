# Pipe in reindexed-graph.txt
# Outputs edge list of friends

import sys

for line in sys.stdin:
    sp = line.split('|')
    if sp[1] == 'FR':
        friends = sp[2].split(',')
        for fr in friends:
            sys.stdout.write(sp[0])
            sys.stdout.write(';')
            sys.stdout.write(fr.strip())
            sys.stdout.write('\n')