package org.ozsoft.texasholdem.actions;

public class BetAction extends Action {

    public BetAction(int amount) {
        super("Bet", "bets", amount);
    }
    
    @Override
    public String toString() {
        return String.format("Bet(%d)", getAmount());
    }
    
}
