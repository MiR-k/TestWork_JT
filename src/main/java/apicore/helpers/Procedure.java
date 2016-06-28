package apicore.helpers;
/**
 *  Operation with Entity
 *  instructions for changing the amount of entity
 *  job,view,node
 */
public enum Procedure{
    creation(1),
    copy(1),
    rename(0),
    removal(-1);

    public int value;

    Procedure(final int values) {
        value = values;
    }

    public int getValue() {
        return value;
    }
}