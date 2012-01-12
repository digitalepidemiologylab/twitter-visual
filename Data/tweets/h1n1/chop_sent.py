#pipe in results from generate_sentiment_score.py
#outputs the same but only for users with abs(score) >= argv[1]

import sys

cut = float(sys.argv[1])

for line in sys.stdin:
    if abs(float(line.split('\t')[1])) >= cut:
        sys.stdout.write(line)