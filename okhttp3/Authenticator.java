package okhttp3;

import java.io.IOException;

public interface Authenticator {
    public static final Authenticator NONE = new C15331();

    static class C15331 implements Authenticator {
        C15331() {
        }

        public Request authenticate(Route route, Response response) {
            return null;
        }
    }

    Request authenticate(Route route, Response response) throws IOException;
}
