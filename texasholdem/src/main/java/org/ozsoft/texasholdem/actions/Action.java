package org.ozsoft.texasholdem.actions;

public abstract class Action {
    
    public static final Action ALL_IN = new AllInAction();

    public static final Action BET = new BetAction(0);
    
    public static final Action BIG_BLIND = new BigBlindAction();
    
    public static final Action CALL = new CallAction();
    
    public static final Action CHECK = new CheckAction();
    
    public static final Action CONTINUE = new ContinueAction();
    
    public static final Action FOLD = new FoldAction();
    
    public static final Action RAISE = new RaiseAction(0);
    
    public static final Action SMALL_BLIND = new SmallBlindAction();
    
    private final String name;
    
    private final String verb;
    
    private final int amount;
    
    public Action(String name, String verb) {
        this(name, verb, 0);
    }
    
    public Action(String name, String verb, int amount) {
        this.name = name;
        this.verb = verb;
        this.amount = amount;
    }
    
    public final String getName() {
        return name;
    }
    
    public final String getVerb() {
        return verb;
    }
    
    public final int getAmount() {
        return amount;
    }
    
    @Override
    public String toString() {
        return name;
    }

}
