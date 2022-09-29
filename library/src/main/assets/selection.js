var selection = {
    onSelectionChange: function() {
        clearTimeout(selection.selectionTimer);
        selection.selectionTimer = setTimeout(
            function () {
                selection.selectionTimer = null;
                selection.notifySelection();
            },
            100
        );
    },

    notifySelection: function() {
        var selection = document.getSelection();
        if (selection.anchorNode == null || selection.focusNode == null || selection.isCollapsed) {
            jsBridge.onSelectionChange(null);
        } else {
            jsBridge.onSelectionChange(
                selection.toString()
            );
        }
    },

    setup: function(){
        document.oncontextmenu = function(e) { e.preventDefault(); };
        document.onselectionchange = selection.onSelectionChange;
        document.addEventListener("click", function(event) {
            var posX = event.clientX;
            var posY = event.clientY;
            highlights.isHighlightClicked(posX, posY);
        });
        return selection;
    },
};

