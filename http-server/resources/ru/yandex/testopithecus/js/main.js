let graph = initGraph();
fillGraph(graph);
drawGraph(graph);

function initGraph() {
    return {
        nodes: new vis.DataSet([]),
        edges: new vis.DataSet([])
    };
}

function drawGraph(graph) {
    const options = {
        autoResize: true,
        height: '100%',
        width: '100%',
        edges:{
            arrows: 'to',
            chosen: false,
            shadow: false,
            smooth: false,
        },
        nodes: { chosen: false },
        physics: false
    };
    const container = document.getElementById('content');
    const network = new vis.Network(container, graph, options);
}

function fillGraph(graph) {
    let json = ${graphJson};
    for (const key in json.vertices) {
        if (json.vertices.hasOwnProperty(key)) {
            let vertex = json.vertices[key];
            if (vertex.index !== 0) {
                addNode(graph, vertex);
            }
        }
    }
    for (const key in json.edges) {
        if (json.edges.hasOwnProperty(key)) {
            let edge = json.edges[key];
            if (edge.target !== 0) {
                addEdge(graph, edge)
            } else {
                addEmptyEdge(graph, edge.source)
            }
        }
    }
}

function addNode(graph, vertex) {
    const color = getColor(vertex.isModel);
    graph.nodes.add([
        {
            id: vertex.index,
            label: vertex.index.toString(),
            color: color
        }
    ]);
}

function addEdge(graph, edge) {
    const color = getColor(edge.isModel);
    graph.edges.add([
        {
            from: edge.source,
            to: edge.target,
            color: {
                color: color,
                inherit: false
            },
            label: edge.label
        }
    ])
}

function getColor(isModel) {
    return isModel ? 'red' : 'blue';
}

function addEmptyEdge(graph, source) {}
