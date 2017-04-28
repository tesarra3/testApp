package radek.tesar.ab.Enum;

/**
 * Created by tesar on 28.04.2017.
 */

public enum Direction {

        incoming ("INCOMING"),
        outgoing ("OUTGOING");

        private final String name;

        private Direction(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }

}
