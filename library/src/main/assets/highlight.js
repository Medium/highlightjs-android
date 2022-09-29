function setupHighlights(_container){
    highlights.container = _container;
}

function applyHighlights(items) {
    highlights.items = items;
    highlights.rebuild();
}

var ranges = [];

var highlights = {

    common: null,

    container: null,

    items: [],

    rebuild: function() {

        while (highlights.container.firstChild) {
            highlights.container.removeChild(highlights.container.firstChild);
        }

        for (var i = 0; i < highlights.items.length; i++) {
            if(highlights.items[i].isMine) {
                highlights._addHighlightOverlays(highlights.items[i], "gl-highlight-overlay-mine");
            } else {
                highlights._addHighlightOverlays(highlights.items[i], "gl-highlight-overlay-other");
            }
        }
    },

    _addHighlightOverlays: function(highlight, additionalClassName) {

        var containerRect = highlights.container.getBoundingClientRect();
        var range = document.createRange();

        highlights.findNodeForIndex(highlight.startOffset, true, range);
        highlights.findNodeForIndex(highlight.endOffset, false, range);

        var inset = 2;
        var rects = range.getClientRects();
        ranges.push(range);
        for (var i = 0; i < rects.length; i++) {
            var r = rects[i];
            var overlay = document.createElement("div");
            overlay.classList.add("gl-highlight-overlay");
            overlay.classList.add(additionalClassName);
            overlay.range = range;
            overlay.style.left = (r.left - containerRect.left - inset) + "px";
            overlay.style.top = (r.top - containerRect.top - inset) + "px";
            overlay.style.width = (r.width + 2 * inset) + "px";
            overlay.style.height = (r.height + 2 * inset) + "px";
            highlights.container.appendChild(overlay);
        }
    },

    findNodeForIndex: function(targetOffset, isStart, range){
        var node = document.getElementsByTagName('pre')[0];
        var charCount = 0;

        var nodeInfo = new Object();
        nodeInfo.isStart = isStart;
        nodeInfo.targetOffset = targetOffset;
        nodeInfo.charCount = 0;
        nodeInfo.range = range;
        nodeInfo.node = node;
        nodeInfo.startSet = false;
        nodeInfo.endSet = false;
        highlights.processNode(
            nodeInfo
        )
    },

    processNode: function(nodeInfo) {
        if(nodeInfo.node.nodeType == Node.ELEMENT_NODE) {
            var nextNodeProcess = Object.assign({}, nodeInfo);;
            var currentNodes = nextNodeProcess.node.childNodes;
            for (var i = 0; i < currentNodes.length; i++) {
                var newNodeInfo = new Object();
                newNodeInfo.isStart = nextNodeProcess.isStart;
                newNodeInfo.targetOffset = nextNodeProcess.targetOffset;
                newNodeInfo.charCount = nextNodeProcess.charCount;
                newNodeInfo.node = currentNodes[i];
                newNodeInfo.range = nodeInfo.range;
                newNodeInfo.startSet = nodeInfo.startSet;
                newNodeInfo.endSet = nodeInfo.endSet;
                nextNodeProcess = highlights.processNode(newNodeInfo);
                if(nextNodeProcess.startSet && nodeInfo.isStart){
                    return nextNodeProcess;
                }

                if(nextNodeProcess.endSet && !nodeInfo.isStart){
                    return nextNodeProcess;
                }

            }
            return nextNodeProcess;
        } else if (nodeInfo.node.nodeType == Node.TEXT_NODE) {
            if(nodeInfo.charCount + nodeInfo.node.textContent.length >= nodeInfo.targetOffset){
                if(nodeInfo.isStart) {
                    nodeInfo.startSet = true
                    nodeInfo.range.setStart(nodeInfo.node, nodeInfo.targetOffset - nodeInfo.charCount);
                } else {
                    nodeInfo.endSet = true
                    nodeInfo.range.setEnd(nodeInfo.node, nodeInfo.targetOffset - nodeInfo.charCount);
                }
                return nodeInfo;
            } else {
                nodeInfo.charCount = nodeInfo.charCount + nodeInfo.node.textContent.length;
                return nodeInfo;
            }
        }
    },

    isHighlightClicked: function(posX, posY){
        var highlightsChildren = this.container.children;
        for (var index = 0; index < highlightsChildren.length; index++) {
            var clientRectList = highlightsChildren[index].getClientRects();
            for (var rectIndex = 0; rectIndex < clientRectList.length; rectIndex++) {
                var rect = clientRectList[rectIndex];
                if( rect.left <= posX && rect.right >= posX && rect.top <= posY && rect.bottom >= posY) {
                    document.getSelection().removeAllRanges();
                    document.getSelection().addRange(highlightsChildren[index].range);
                    return true;
                }
            }
        }
        return false;
    }
};