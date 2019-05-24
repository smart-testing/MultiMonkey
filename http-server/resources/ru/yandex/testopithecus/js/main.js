document.addEventListener("DOMContentLoaded", function(event) {
    const options = getOptions();
    let graph = initGraph();
    let json = {};
    fillGraph(graph, json);
    let network = drawGraph(graph, options);

    setInterval(function() {
        $.ajax({
            url: 'graph-json',
            success: function(response) {
                json = response;
                const new_graph = initGraph();
                fillGraph(new_graph, json);
                graph.nodes.update(new_graph.nodes.get());
                graph.edges.update(new_graph.edges.get());
            }
        })
    }, 2000);

});

function getOptions() {
    return {
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
        physics: true,
    };
}

function initGraph() {
    return {
        nodes: new vis.DataSet([]),
        edges: new vis.DataSet([])
    };
}

function drawGraph(graph, options) {
    const container = document.getElementById('content');
    return new vis.Network(container, graph, options);
}

function fillGraph(graph, json) {
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
