/******************************************************************************
 *                                                                            *
 * Copyright 2018 Jan Henrik Weinstock                                        *
 *                                                                            *
 * Licensed under the Apache License, Version 2.0 (the "License");            *
 * you may not use this file except in compliance with the License.           *
 * You may obtain a copy of the License at                                    *
 *                                                                            *
 *     http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 *                                                                            *
 ******************************************************************************/

package org.vcml.explorer.ui.services;

import java.util.Collection;

import org.eclipse.jface.util.IPropertyChangeListener;

import org.vcml.session.Module;
import org.vcml.session.Session;
import org.vcml.session.SessionException;

public interface ISessionService {

    /**
     * Fired when a new session is discovered
     */
    public static final String PROP_ADDED = "added";

    /**
     * Fired when a session is no longer available
     */
    public static final String PROP_REMOVED = "removed";

    /**
     * Fired when the state of a session is updated
     */
    public static final String PROP_UPDATED = "updated";

    /**
     * Fired when current session has changed
     */
    public static final String PROP_SELECT = "selected";

    public static final String SESSION_TOPIC = "org/vcml/session";

    public static final String SESSION_ADDED_TOPIC = SESSION_TOPIC + "/added";

    public static final String SESSION_REMOVED_TOPIC = SESSION_TOPIC + "/removed";

    public static final String SESSION_UPDATED_TOPIC = SESSION_TOPIC + "/updated";

    public static final String SESSION_SELECTED_TOPIC = SESSION_TOPIC + "/selected";

    /**
     * The collection of sessions we are currently connected to.
     * 
     * @return an unmodifiable Collection. For looking, not touching. Will not be
     *         <code>null</code>.
     */
    public Collection<Session> getSessions();

    /**
     * Adds a remote session.
     * 
     * @param URI Specifies session location as a string value.
     */
    public void addRemoteSession(String URI, boolean connect);

    /**
     * Checks for new sessions.
     */
    public void refreshSessions();

    /**
     * Refreshes the specified session.
     */
    public void refreshSession(Session session);

    /**
     * Returns the currently connected session or <code>null</code> if not
     * connected.
     * 
     * @return currently connected session or <code>null</code>.
     */
    public Session currentSession();

    /**
     * Creates and connects to a new session.
     */
    public void connectSession(Session session);

    /**
     * Disconnects from the current session.
     */
    public void disconnectSession(Session session);

    /**
     * Starts simulation of the currently connected session.
     */
    public void startSimulation(Session session);

    /**
     * Stops simulation of the currently connected session.
     */
    public void stopSimulation(Session session);

    /**
     * Steps the simulation for a single quantum.
     */
    public void stepSimulation(Session session);

    /**
     * Quits simulation and disconnects the session.
     */
    public void quitSimulation(Session session);

    /**
     * Finds a module within the module hierarchy.
     */
    public Module findModule(Session session, String name);

    /**
     * Reports an error for the given session.
     * 
     * @param session Session that has received an error
     * @param e       Description of the error
     */
    public void reportSessionError(Session session, SessionException e);

    /**
     * Listen for changes to sessions managed by this service.
     * <p>
     * Note: this services cleans up listeners when it is disposed.
     * </p>
     * 
     * @param listener the property change listener. Has no effect if an identical
     *                 listener is already registered. Must not be <code>null</code>
     * @see #PROP_ADDED
     * @see #PROP_REMOVED
     * @see #PROP_UPDATED
     * @see ISessionService#removeSessionChangeListener(IPropertyChangeListener)
     */
    public void addSessionChangeListener(IPropertyChangeListener listener);

    /**
     * Remove the change listener.
     * 
     * @param listener the property change listener. Has no effect if it is not
     *                 already registered. Must not be <code>null</code>.
     */
    public void removeSessionChangeListener(IPropertyChangeListener listener);

}
