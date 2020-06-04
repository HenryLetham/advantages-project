package sample.configs;

public class SessionManager {
        public static SessionManager instance;
        private boolean isLogin = false;
        private String userName = "";
        private int userId;


        public static SessionManager getInstance() {
            if (instance == null) {
                instance = new SessionManager();
            }
            return instance;
        }

        public void login(String userName, int userId) {
            this.isLogin = true;
            this.userName = userName;
            this.userId = userId;
        }

        public void logout() {
            this.isLogin = false;
            this.userName = "";
        }

        public void setUserId(int userId) {
            userId = userId;
        }

        public boolean isLogin() {
            return isLogin;
        }

        public int getUserId() {
            return userId;
        }
}
