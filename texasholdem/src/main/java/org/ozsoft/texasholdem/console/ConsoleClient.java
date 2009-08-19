package org.ozsoft.texasholdem.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import org.ozsoft.texasholdem.Action;
import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.PlayerClient;

/**
 * Console implementation of a player client, intended for human players with a
 * console interface.
 * 
 * @author Oscar Stigter
 */
public class ConsoleClient implements PlayerClient {
    
	/** The console reader. */
	private final BufferedReader consoleReader;
    
	/**
	 * Constructor.
	 */
    public ConsoleClient() {
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }
    
    /*
     * (non-Javadoc)
     * @see th.PlayerClient#act(java.util.Set, th.Card[], java.util.List, int, int)
     */
    @Override
	public Action act(Set<Action> actions, Card[] holeCards, List<Card> boardCards, int minBet, int currentBet) {
    	StringBuilder sb = new StringBuilder("Please select an action: ");
    	int i = actions.size();
    	for (Action action : actions) {
    		sb.append(action);
    		i--;
    		if (i > 1) {
    			sb.append(", ");
    		} else if (i == 1) {
    			sb.append(" or ");
    		} else {
    			// No more actions.
    		}
    	}
    	sb.append("? ");
    	String prompt = sb.toString();
    	Action selectedAction = null;
    	while (selectedAction == null) {
        	System.out.print(prompt);
            try {
                String input = consoleReader.readLine();
                if (input != null) {
	                for (Action action : actions) {
	                	String command = action.toString().toLowerCase();
	                	if (command.startsWith(input.toLowerCase())) {
	                		selectedAction = action;
	                		break;
	                	}
	                }
	                if (selectedAction == null) {
	                	System.out.println("Invalid action -- please try again.");
	                }
                }
            } catch (IOException e) {
            	// The VM is killed; ignore.
            }
    	}
    	return selectedAction;
	}

}
