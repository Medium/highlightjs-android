var common = {
    onSelectionChange: function() {
        clearTimeout(common.selectionTimer);
        common.selectionTimer = setTimeout(
            function () {
                common.selectionTimer = null;
                common.notifySelection();
            },
            100
        );
    },

    notifySelection: function() {
            var selection = document.getSelection();

            if (selection.anchorNode == null || selection.focusNode == null || selection.isCollapsed) {
                jsBridge.onSelectionChange(null);
                return;
            }
            jsBridge.onSelectionChange(
                selection.toString()
            );
        },
};

function setup() {
    document.oncontextmenu = function(e) { e.preventDefault(); };
    document.onselectionchange = common.onSelectionChange;

    return common;
}

