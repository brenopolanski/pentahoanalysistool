
// The following code is for Save/Save As functionality

var gCtrlr = new WaqrProxy(); // this is a required variable

// this is a required object
function WaqrProxy() {

    this.wiz = new Wiz();
    this.repositoryBrowserController = new RepositoryBrowserControllerProxy();
    
}

// this is a required object
function Wiz() {
    currPgNum = 0;
}

// this is a required function
function savePg0() {
}

// this is a required object
function RepositoryBrowserControllerProxy() {

    // This function is called after the Save dialog has been used
    this.remoteSave = function( myFilename, mySolution, myPath, myType, myOverwrite ) {

        // This is where you take action to save the state of this document, via Ajax or form post.
        // After the save has happened you should call refreshBrowsePanel()
    	top.patGwtSave( myFilename, mySolution, myPath, myType, myOverwrite );
        
    }
}
