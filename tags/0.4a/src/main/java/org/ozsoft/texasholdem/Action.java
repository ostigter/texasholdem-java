// This file is part of the 'texasholdem' project, an open source
// Texas Hold'em poker application written in Java.
//
// Copyright 2009 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.texasholdem;

/**
 * A Texas Hold'em poker action.
 * 
 * @author Oscar Stigter
 */
public enum Action {
    
    /** Posting the small blind. */
    SMALL_BLIND("Small Blind", "posts the small blind"),

    /** Posting the big blind. */
    BIG_BLIND("Big Blind", "posts the big blind"),
    
    /** Checking. */
    CHECK("Check", "checks"),
    
    /** Calling a bet. */
    CALL("Call", "calls"),
    
    /** Place an initial bet. */
    BET("Bet", "bets"),
    
    /** Raising the current bet. */
    RAISE("Raise", "raises"),
    
    ALL_IN("All-in", "goes all-in"),
    
    /** Folding. */
    FOLD("Fold", "folds"),
    
    /** Continuing the game. */
    CONTINUE("Continue", "continues"),
    
    ;
    
    /** The name. */
    private final String name;
    
    /** The verb. */
    private final String verb;
    
    /**
     * Constructor.
     * 
     * @param name
     *            The name.
     */
    Action(String name, String verb) {
        this.name = name;
        this.verb = verb;
    }
    
    /**
     * Returns the name.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the verb form of this action.
     * 
     * @return The verb.
     */
    public String getVerb() {
        return verb;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return name;
    }

}
