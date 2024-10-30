package hr.vsite.messageapp.utils;

public class Variables {

    public enum Role {

        ROLE_USER("USER");

        private final String value;

        Role(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }


}
