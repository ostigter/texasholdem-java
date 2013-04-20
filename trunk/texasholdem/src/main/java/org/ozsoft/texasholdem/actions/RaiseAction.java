package org.ozsoft.texasholdem.actions;

public class RaiseAction extends Action {

    public RaiseAction(int amount) {
        super("Raise", "raises", amount);
    }
    
    @Override
    public String toString() {
        return String.format("Raise(%d)", getAmount());
    }
    
}
