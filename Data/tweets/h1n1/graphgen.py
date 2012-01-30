import networkx as nx

G = nx.Graph()
uids = []
nodes=0
edges=0

print('loading nodes')

for line in open('allSentiment.csv','r'):
    if line.split('\t')[0].strip() == 'Id' :
        continue
    sp = line.split('\t')
    G.add_node(sp[0].strip(),sentiment=sp[1],tweets=sp[2])
    uids.append(sp[0].strip())
    nodes = nodes+1

uids = set(uids)
print('loading edges')

print(nodes)


for line in open('fullEdgeList.csv','r'):
    if line.split(';')[0].strip() in uids:
        if line.split(';')[1].strip() in uids:
            G.add_edge(line.split(';')[0].strip(),line.split(';')[1].strip(),type=line.split(';')[2].strip())
            edges = edges+1
            
            

print(edges)
print(str(nx.number_connected_components(G)))