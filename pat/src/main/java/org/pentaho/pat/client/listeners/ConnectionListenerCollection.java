package org.pentaho.pat.client.listeners;

import java.util.ArrayList;
import java.util.Iterator;

import org.pentaho.pat.client.panels.services.ControlBar;

import com.google.gwt.user.client.ui.Widget;

/**
 * A helper class for implementers of the SourcesConnectionEvents interface. This
 * subclass of {@link ArrayList} assumes that all objects added to it will be of
 * type {@link @link org.pentaho.pat.client.ConnectionListener}.
 */
public class ConnectionListenerCollection extends ArrayList {

  /**
   * Fires a click event to all listeners.
   * 
   * @param sender the widget sending the event.
   */
  public void fireConnectionMade(Widget sender) {
    for (Iterator it = iterator(); it.hasNext();) {
      ConnectionListener listener = (ConnectionListener) it.next();
      listener.onConnectionMade(sender);
    }
  }
  
  public void fireConnectionBroken(Widget sender) {
    for (Iterator it = iterator(); it.hasNext();) {
      ConnectionListener listener = (ConnectionListener) it.next();
      listener.onConnectionBroken(sender);
    }    
  }

}